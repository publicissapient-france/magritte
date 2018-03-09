#!/usr/bin/env bash

source "conf.sh"

python -m tensorflow.python.tools.optimize_for_inference \
  --input=output/"${CATEGORY}"/"${ARCHITETCURE}"/magritte_retrained_graph.pb \
  --output=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_optimized_graph.pb \
  --input_names=input \
  --output_names=final_result_"${CATEGORY}"