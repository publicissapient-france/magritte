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

import android.app.Activity;
import android.app.Fragment;
import android.media.Image.Plane;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Size;

import java.nio.ByteBuffer;

import fr.xebia.magritte.R;
import timber.log.Timber;

public abstract class CameraActivity extends Activity implements OnImageAvailableListener {

    private HandlerThread handlerThread;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Timber.d("onCreate " + this);
        super.onCreate(null);

        setContentView(R.layout.activity_camera);
        setFragment();
    }

    @Override
    public synchronized void onStart() {
        Timber.d("onStart " + this);
        super.onStart();
    }

    @Override
    public synchronized void onResume() {
        Timber.d("onResume " + this);
        super.onResume();

        handlerThread = new HandlerThread("inference");
        handlerThread.start();
    }

    @Override
    public synchronized void onPause() {
        Timber.d("onPause " + this);

        if (!isFinishing()) {
            Timber.d("Requesting finish");
            finish();
        }

        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
        } catch (final InterruptedException e) {
            Timber.e(e, "Exception!");
        }

        super.onPause();
    }

    @Override
    public synchronized void onStop() {
        Timber.d("onStop " + this);
        super.onStop();
    }

    @Override
    public synchronized void onDestroy() {
        Timber.d("onDestroy " + this);
        super.onDestroy();
    }

    protected void setFragment() {
        final Fragment fragment = CameraConnectionFragment.newInstance(
                new CameraConnectionFragment.ConnectionCallback() {
                    @Override
                    public void onPreviewSizeChosen(final Size size, final int rotation) {
                        CameraActivity.this.onPreviewSizeChosen(size, rotation);
                    }
                },
                this, getLayoutId(), getDesiredPreviewFrameSize());

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    protected void fillBytes(final Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                Timber.d("Initializing buffer %d at size %d", i, buffer.capacity());
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }

    protected abstract void onPreviewSizeChosen(final Size size, final int rotation);

    protected abstract int getLayoutId();

    protected abstract int getDesiredPreviewFrameSize();
}
