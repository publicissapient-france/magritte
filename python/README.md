Magritte model training
=======================

This folder contains scripts for retraining and optimizing TensorFlow models for Android & iOS.

Place your raw images under `/data` folder, for example: for fruit category, place all the apple images here `/data/fruits/apple` 

# Configure category & architecture

Configure the category and model architecure you want to use:

```
CATEGORY="fruits"
ARCHITECTURE="mobilenet_1.0_224_quantized"
```

# Transfer learning: retrain the model

Retrain model with your chosen architecture, launch following script to retrain the model for the chosen category:

```
$ ./magritte_retrain.sh
```

# Optimize the model

## For TensorFlow Mobile

```
$ ./magritte_optimize_tf_mobile.sh
```

# Convert to model to TFLite format

TFLite uses a different serialization format from regular TensorFlow. TensorFlow uses Protocol Buffers, while TFLite uses FlatBuffers.

```
$ ./magritte_convert_tf_lite.sh
```

# References

- [TensorFlow For Poets
](https://codelabs.developers.google.com/codelabs/tensorflow-for-poets/index.html#0)
- [TensorFlow for Poets 2: Optimize for Mobile
](https://codelabs.developers.google.com/codelabs/tensorflow-for-poets-2/#0)
- [TensorFlow for Poets 2: TFLite
](https://codelabs.developers.google.com/codelabs/tensorflow-for-poets-2-tflite/#0)

