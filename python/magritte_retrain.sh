#!/usr/bin/env bash

source "conf.sh"

python -m scripts.retrain \
    --image_dir=data/"${CATEGORY}"/ \
    --model_dir=output/models/"${ARCHITECTURE}" \
    --bottleneck_dir=output/"${CATEGORY}"/"${ARCHITECTURE}"/bottlenecks/ \
    --output_graph=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_retrained_graph.pb \
    --output_labels=output/"${CATEGORY}"/"${ARCHITECTURE}"/magritte_labels.txt \
    --summaries_dir=output/"${CATEGORY}"/"${ARCHITECTURE}"/training_summaries \
    --final_tensor_name=final_result_"${CATEGORY}" \
    --how_many_training_steps=1000 \
    --random_crop=0 \
    --random_scale=0 \
    --random_brightness=0 \
    --architecture="${ARCHITECTURE}" \
    --first_fit False