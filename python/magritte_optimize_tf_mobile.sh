#!/usr/bin/env bash

source "conf.sh"

echo $ARCHITECTURE

python -m tensorflow.python.tools.optimize_for_inference \
  --input=output/${CATEGORY}/${ARCHITECTURE}/magritte_retrained_graph.pb \
  --output=output/${CATEGORY}/${ARCHITECTURE}/magritte_optimized_graph.pb \
  --input_names=input \
  --output_names=final_result_${CATEGORY}