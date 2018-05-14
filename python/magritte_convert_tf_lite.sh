#!/usr/bin/env bash

# source "conf.sh"

# toco \
#   --input_file=output/${CATEGORY}/${ARCHITECTURE}/magritte_retrained_graph.pb \
#   --output_file=output/${CATEGORY}/${ARCHITECTURE}/magritte_optimized_graph.lite \
#   --input_format=TENSORFLOW_GRAPHDEF \
#   --output_format=TFLITE \
#   --input_shape=1,${IMAGE_SIZE},${IMAGE_SIZE},3 \
#   --input_array=input \
#   --output_array=final_result_"${CATEGORY}" \
#   --inference_type=FLOAT \
#   --input_data_type=FLOAT
 
# toco is currently broken: https://github.com/tensorflow/tensorflow/blob/master/tensorflow/contrib/lite/toco/g3doc/python_api.md
# place your retrained graph file in the /tmp folder
# run this from tensorflow workspace

bazel run --config=opt \
  //tensorflow/contrib/lite/toco:toco -- \
  --input_file=/tmp/magritte_retrained_graph.pb \
  --output_file=/tmp/magritte_optimized_graph.lite \
  --input_format=TENSORFLOW_GRAPHDEF \
  --output_format=TFLITE \
  --input_shape=1,224,224,3 \
  --input_array=input \
  --output_array=final_result_fruits \
  --inference_type=FLOAT \
  --input_data_type=FLOAT