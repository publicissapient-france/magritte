#!/usr/bin/env bash

source "conf.sh"

# currently broken

toco \
  --input_file=output/${CATEGORY}/${ARCHITECTURE}/magritte_retrained_graph.pb \
  --output_file=output/${CATEGORY}/${ARCHITECTURE}/magritte_retrained_graph.lite \
  --input_format=TENSORFLOW_GRAPHDEF \
  --output_format=TFLITE \
  --input_shape=1,${IMAGE_SIZE},${IMAGE_SIZE},3 \
  --input_array=input \
  --output_array=final_result_"${CATEGORY}" \
  --inference_type=FLOAT \
  --input_data_type=FLOAT