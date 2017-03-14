import argparse
import sys

import tensorflow as tf
from tensorflow.python.tools.optimize_for_inference_lib import optimize_for_inference

FLAGS = None


def main(_):
    # Freeze the graph

    output_frozen_graph_name = '/Users/Yoann/Documents/Xebia/tests/datamobile/model/magritte_model.pb'
    output_optimized_graph_name = '/Users/Yoann/Documents/Xebia/tests/datamobile/model/optimized_magritte_model.pb'

    input_graph_def = tf.GraphDef()

    with tf.gfile.Open(output_frozen_graph_name, "r") as f:
        data = f.read()
        input_graph_def.ParseFromString(data)

    output_graph_def = optimize_for_inference(
            input_graph_def,
            ["Mul"], # an array of the input node(s)
            ["final_result"], # an array of output nodes
            tf.float32.as_datatype_enum)

    # Save the optimized graph
    f = tf.gfile.FastGFile(output_optimized_graph_name, "w")
    f.write(output_graph_def.SerializeToString())


if __name__ == '__main__':
    parser = argparse.ArgumentParser()

    FLAGS, unparsed = parser.parse_known_args()
    tf.app.run(main=main, argv=[sys.argv[0]] + unparsed)