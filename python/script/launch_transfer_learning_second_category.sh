#!/usr/bin/env bash
export PYTHONPATH="."
python magritte_training/training/transfer_learning.py \
    --image_dir "data/images/vegetables/" \
    --model_dir "model/fruits/" \
    --bottleneck_dir "data/bottleneck/vegetables/"  \
    --output_graph "model/vegetables/output_graph_vegetables_1.0_224.pb" \
    --output_labels "model/vegetables/output_labels_veg.txt" \
    --final_tensor_name "final_result_veg" \
    --all_final_tensors_names_list "final_result_fruits,final_result_veg" \
    --summaries_dir "summaries/vegetables/1.0_224" \
    --intermediate_output_graphs_dir "model/vegetables/intermediate_graph_1.0_224" \
    --how_many_training_steps 3000 \
    --learning_rate 0.005 \
    --flip_left_right False \
    --random_crop 0 \
    --random_scale 0 \
    --random_brightness 0 \
    --train_batch_size 100 \
    --test_batch_size -1 \
    --validation_batch_size 100 \
    --testing_percentage 10 \
    --validation_percentage 10 \
    --eval_step_interval 10 \
    --print_misclassified_test_images True \
    --intermediate_store_frequency 500 \
    --architecture "mobilenet_1.0_224" \
    --first_fit False