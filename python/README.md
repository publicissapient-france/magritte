## Magritte Model Training

Train and optimize a TensorFlow model for image recognition in smartphone app.

### Run Transfer Learning
```
python magritte_training/training/transfer_learning.py --image_dir="your_image_path" --how_many_training_steps=3000 --learning_rate=0.001
```

### Optimize the model
##### Optimize for inference
This step will remove all unused operations in the graph
```
python magritte_training/optimize/optimize_for_inference.py --input_model_name="input_model_name.pb" --output_model_name="optimized_model_name.pb"
```

##### Quantize the weights
Quantize the float weights to eight-bits to shrink the model size
```
python magritte_training/optimize/quantize.py --input="your_input_model.pb" --output_node_names="final_result" --output="your_output_model.pb" --mode=weights
```
