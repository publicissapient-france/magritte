#!/usr/bin/env bash

source "conf.sh"

python -m scripts.label_image \
    --graph=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_retrained_graph.pb  \
    --labels=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_labels.txt \
    --image=data/fruits/apple/image_apple125.jpeg \
    --output_layer=final_result_"${CATEGORY}"

python -m scripts.label_image \
    --graph=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_optimized_graph.pb  \
    --labels=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_labels.txt \
    --image=data/fruits/apple/image_apple125.jpeg \
    --output_layer=final_result_"${CATEGORY}"

python -m scripts.label_image \
    --graph=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_retrained_graph.pb  \
    --labels=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_labels.txt \
    --image=data/fruits/banana/image_banana1077.jpeg \
    --output_layer=final_result_"${CATEGORY}"

python -m scripts.label_image \
    --graph=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_optimized_graph.pb  \
    --labels=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_labels.txt \
    --image=data/fruits/banana/image_banana1077.jpeg \
    --output_layer=final_result_"${CATEGORY}"