## Magritte Model Training

Train and optimize a TensorFlow model for image recognition in smartphone app.

### Run Transfer Learning
```
./script/launch_transfer_learning.sh
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
