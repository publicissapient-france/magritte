#!/usr/bin/env bash

source "conf.sh"

# toco is currently broken: https://github.com/tensorflow/tensorflow/blob/master/tensorflow/contrib/lite/toco/g3doc/python_api.md
# place your retrained graph file in the /tmp folder
# run this from tensorflow workspace

# https://github.com/tensorflow/tensorflow/blob/master/tensorflow/contrib/lite/toco/g3doc/cmdline_examples.md
# This works for non-quantized graph

bazel run --config=opt \
  //tensorflow/contrib/lite/toco:toco -- \
  --input_file=/tmp/magritte_retrained_graph.pb \
  --output_file=/tmp/magritte_graph.tflite \
  --inference_type=FLOAT \
  --input_shape=1,224,224,3 \
  --input_array=input \
  --output_array=final_result_"${CATEGORY}" \
  --mean_value=128 \
  --std_value=128 \
  --default_ranges_min=0 \
  --default_ranges_max=6