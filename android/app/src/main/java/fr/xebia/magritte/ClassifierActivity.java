/*
 * Copyright 2016 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.xebia.magritte;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Trace;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Size;
import android.view.Display;

import org.tensorflow.demo.env.ImageUtils;
import org.tensorflow.demo.env.Logger;

import java.util.List;

import fr.xebia.magritte.model.ClassifierContract;

public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener,
        ClassifierContract.View {
    private static final Logger LOGGER = new Logger();

    private static final int INPUT_SIZE = 299;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128;
    private static final String INPUT_NAME = "Mul";
    private static final String OUTPUT_NAME_FRUITS = "final_result_fruits";
    private static final String OUTPUT_NAME_VEGETABLES = "final_result_vegetables";

    private static final String MODEL_FILE = "file:///android_asset/magritte_model.pb";
    private static final String LABELS_FRUITS = "file:///android_asset/magritte_labels_fruits.txt";
    private static final String LABELS_VEGETABLES = "file:///android_asset/magritte_labels_vegetables.txt";

    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final boolean MAINTAIN_ASPECT = true;

    private Integer sensorOrientation;

    private int previewWidth = 0;
    private int previewHeight = 0;
    private byte[][] yuvBytes;
    private int[] rgbBytes = null;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;

    private boolean computing = false;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private ResultsView resultsView;

    private int currentMode;
    private TextToSpeech tts;

    private ClassifierContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentMode = bundle.getInt(LevelActivity.MODEL_TYPE);
        }
        String modelfileName = MODEL_FILE;
        String labelfileName;
        String outputName;
        final int levelPhrase;

        if (currentMode == 0) {
            labelfileName = LABELS_FRUITS;
            outputName = OUTPUT_NAME_FRUITS;
            levelPhrase = R.string.learn_fruit;
        } else {
            labelfileName = LABELS_VEGETABLES;
            outputName = OUTPUT_NAME_VEGETABLES;
            levelPhrase = R.string.learn_vegetable;
        }

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // TODO say hello in chosen language
                    speak(getString(levelPhrase));
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        Classifier classifier = TensorFlowImageClassifier.create(
                getAssets(),
                modelfileName,
                labelfileName,
                INPUT_SIZE,
                IMAGE_MEAN,
                IMAGE_STD,
                INPUT_NAME,
                outputName);

        presenter = new ClassifierPresenter(this, classifier);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.camera_fragment;
    }

    @Override
    protected int getDesiredPreviewFrameSize() {
        return INPUT_SIZE;
    }

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        resultsView = (ResultsView) findViewById(R.id.results);
        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        final Display display = getWindowManager().getDefaultDisplay();
        final int screenOrientation = display.getRotation();

        LOGGER.i("Sensor orientation: %d, Screen orientation: %d", rotation, screenOrientation);

        sensorOrientation = rotation + screenOrientation;

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbBytes = new int[previewWidth * previewHeight];
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        INPUT_SIZE, INPUT_SIZE,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        yuvBytes = new byte[3][];
    }

    @Override
    public void onImageAvailable(final ImageReader reader) {
        Image image = null;

        try {
            image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            if (computing) {
                image.close();
                return;
            }
            computing = true;

            Trace.beginSection("imageAvailable");

            final Plane[] planes = image.getPlanes();
            fillBytes(planes, yuvBytes);

            final int yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();
            ImageUtils.convertYUV420ToARGB8888(
                    yuvBytes[0],
                    yuvBytes[1],
                    yuvBytes[2],
                    rgbBytes,
                    previewWidth,
                    previewHeight,
                    yRowStride,
                    uvRowStride,
                    uvPixelStride,
                    false);

            image.close();
        } catch (final Exception e) {
            if (image != null) {
                image.close();
            }
            LOGGER.e(e, "Exception!");
            Trace.endSection();
            return;
        }

        rgbFrameBitmap.setPixels(rgbBytes, 0, previewWidth, 0, 0, previewWidth, previewHeight);
        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }

        presenter.recognizeImage(croppedBitmap);
        Trace.endSection();
    }

    @Override
    public void displayRecognitions(List<Classifier.Recognition> recognitionList) {
        resultsView.displayResults(recognitionList);
        computing = false;
    }

    @Override
    public void displayTopMatch(Classifier.Recognition recognition) {
        resultsView.displayTopMatch(recognition);
    }

    @Override
    public void speakResult(String title) {
        speak(title);
    }

    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}