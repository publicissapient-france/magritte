#!/usr/bin/env bash

source "conf.sh"

toco \
  --input_file=output/${CATEGORY}/${ARCHITECTURE}/magritte_optimized_lite_graph.pb \
  --output_file=output/${CATEGORY}/${ARCHITECTURE}/magritte_optimized_lite_graph.lite \
  --input_format=TENSORFLOW_GRAPHDEF \
  --output_format=TFLITE \
  --input_shape=1,${IMAGE_SIZE},${IMAGE_SIZE},3 \
  --input_array=input \
  --output_array=final_result_"${CATEGORY}" \
  --inference_input_type=FLOAT
  --input_data_types=FLOAT
  