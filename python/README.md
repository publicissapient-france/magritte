Magritte Model Training
=======================

This folder contains scripts for retraining and optimizing TensorFlow models for Android & iOS.

Place your raw images under `/data` folder, for example: for fruit category, place all the apple images here `/data/fruits/apple` 

# Configure category & architecture

Configure the category and model architecure you want to use:
```
CATEGORY="fruits"
ARCHITECTURE="mobilenet_1.0_224_quantized"
```

# Transfer learning

Retrain model with `MobileNet_v1_1.0_224`, you can launch following script in a terminal from the root of this folder:

```
$ ./magritte_retrain.sh
```

# Optimize the model

## For TensorFlow Mobile

Reference:

```
$ ./magritte_optimize_tf_mobile.sh
```

# Convert to model to TFLite format

TFLite uses a different serialization format from regular TensorFlow. TensorFlow uses Protocol Buffers, while TFLite uses FlatBuffers.

```
$ ./magritte_convert_tf_lite.sh
```

# References


