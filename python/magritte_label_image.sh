#!/usr/bin/env bash

source "conf.sh"

python -m scripts.label_image \
  --image=data/"${CATEGORY}"/apple/image_apple1017.jpeg \
  --graph=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_retrained_graph.pb \
  --labels=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_labels.txt \
  --output_layer=final_result_"${CATEGORY}"

python -m scripts.label_image \
  --image=data/"${CATEGORY}"/apple/image_apple1017.jpeg \
  --graph=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_optimized_graph.pb \
  --labels=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_labels.txt \
  --output_layer=final_result_"${CATEGORY}"