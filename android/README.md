Magritte Android
================

Ceci n'est pas une pomme.

## Configuration

- #70 [TensorFlow nightly Android build](https://ci.tensorflow.org/view/Nightly/job/nightly-android/)
- `minSdkVersion` = 21

## Launch app

- Download the model file `.pb` and the label file `.txt` to the `app/src/main/assets` folder.
- Open your app in Android Studio 2.3
- Make sure the `.pb` & `.txt` names are correct in the `ClassifierActivity.java`
- Build & run the app

## Reference

- [TensorFlow Android example](https://github.com/tensorflow/tensorflow/tree/master/tensorflow/examples/android/)
- [Android Things TensorFlow Image Classifier](https://github.com/androidthings/sample-tensorflow-imageclassifier)
- [Tutorial: Build Your First Tensorflow Android App](https://omid.al/posts/2017-02-20-Tutorial-Build-Your-First-Tensorflow-Android-App.html)
- [TensorFlow on Android](https://www.oreilly.com/learning/tensorflow-on-android)