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

package fr.xebia.magritte.classifier;

import android.content.Context;
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
import android.widget.Toast;

import org.tensorflow.demo.env.ImageUtils;

import java.util.List;
import java.util.Locale;

import fr.xebia.magritte.LanguageActivity;
import fr.xebia.magritte.LevelActivity;
import fr.xebia.magritte.R;
import fr.xebia.magritte.model.Fruit;
import timber.log.Timber;

public class ClassifierActivity extends CameraActivity implements OnImageAvailableListener,
        ClassifierContract.View {

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

    private static final String TAG = ClassifierActivity.class.getSimpleName();

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
    private int languageChoice;

    private TextToSpeech tts;

    private ClassifierContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentMode = bundle.getInt(LevelActivity.MODEL_TYPE);
            languageChoice = bundle.getInt(LanguageActivity.LANGUAGE_CHOICE);
        }
        String modelfileName = MODEL_FILE;
        String labelfileName;
        String outputName;

        if (currentMode == 0) {
            labelfileName = LABELS_FRUITS;
            outputName = OUTPUT_NAME_FRUITS;
        } else {
            // TODO
            labelfileName = LABELS_VEGETABLES;
            outputName = OUTPUT_NAME_VEGETABLES;
        }

        final Locale desiredLocale = getDesiredLocale(languageChoice);

        // TODO add tss language availability check
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Note: language setting needs to be done in onInit
                    speak("tts ok");
                    tts.setLanguage(desiredLocale);
                    Log.d(TAG, "Initialization succeeded! Speak" + desiredLocale.getLanguage());
                } else {
                    Toast.makeText(ClassifierActivity.this, "Initilization Failed!", Toast.LENGTH_LONG).show();
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

        presenter = new ClassifierPresenter(this, desiredLocale, classifier);
    }

    private Locale getDesiredLocale(int languageChoice) {
        Locale desiredLocale;
        if (languageChoice == LanguageActivity.LANG_FR) {
            desiredLocale = Locale.FRENCH;
        } else if (languageChoice == LanguageActivity.LANG_CN) {
            desiredLocale = Locale.CHINESE;
        } else if (languageChoice == LanguageActivity.LANG_IT) {
            desiredLocale = Locale.ITALIAN;
        } else {
            desiredLocale = Locale.US;
        }
        return desiredLocale;
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
        resultsView = findViewById(R.id.results);
        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        final Display display = getWindowManager().getDefaultDisplay();
        final int screenOrientation = display.getRotation();

        Timber.i("Sensor orientation: %d, Screen orientation: %d", rotation, screenOrientation);

        Integer sensorOrientation = rotation + screenOrientation;

        Timber.i("Initializing at size %dx%d", previewWidth, previewHeight);
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
                    previewWidth,
                    previewHeight,
                    yRowStride,
                    uvRowStride,
                    uvPixelStride,
                    rgbBytes);

            image.close();
        } catch (final Exception e) {
            if (image != null) {
                image.close();
            }
            Timber.e(e, "Exception!");
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
    public Context getContext() {
        return this;
    }

    @Override
    public void displayRecognitions(List<Classifier.Recognition> recognitionList) {
        resultsView.displayResults(recognitionList);
        computing = false;
    }

    @Override
    public void displayTopMatch(Fruit fruit) {
        resultsView.displayTopMatch(fruit);
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