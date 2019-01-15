#!/usr/bin/env bash

toco \
  --input_file=output/${CATEGORY}/${ARCHITECTURE}/magritte_optimized_graph.pb \
  --output_file=output/${CATEGORY}/${ARCHITECTURE}/magritte_optimized_lite_graph.pb \
  --input_format=TENSORFLOW_GRAPHDEF \
  --output_format=TENSORFLOW_GRAPHDEF \
  --input_shape=1,${IMAGE_SIZE},${IMAGE_SIZE},3 \
  --input_array=input \
  --output_array=final_result_${CATEGORY}

tflite_convert \
  --graph_def_file=/tmp/magritte_retrained_graph.pb \
  --output_file=/tmp/magritte_graph.tflite \
  --inference_type=FLOAT \
  --input_shape=1,224,224,3 \
  --input_array=input \
  --output_array=final_result_fruits \
  --default_ranges_min=0 \
  --default_ranges_max=6