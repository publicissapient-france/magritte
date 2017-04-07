import argparse
import sys

import tensorflow as tf
from tensorflow.python.tools.optimize_for_inference_lib import optimize_for_inference

FLAGS = None


def main(_):
    input_graph_def = tf.GraphDef()

    with tf.gfile.Open(FLAGS.input_model_name, "r") as f:
        data = f.read()
        input_graph_def.ParseFromString(data)

    output_graph_def = optimize_for_inference(
            input_graph_def,
            ["Mul"], # an array of the input node(s)
            # ["final_result"], # an array of output nodes
            FLAGS.all_final_tensors_names_list.split(","),  # an array of output nodes
            tf.float32.as_datatype_enum)

    # Save the optimized graph
    f = tf.gfile.FastGFile(FLAGS.output_model_name, "w")
    f.write(output_graph_def.SerializeToString())


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument(
        '--input_model_name',
        type=str,
        default='/tmp/magritte/magritte_model.pb',
        help='Path to the model.'
    )
    parser.add_argument(
        '--output_model_name',
        type=str,
        default='/tmp/magritte/magritte_model_optimized.pb',
        help='Name of the model to load.'
    )
    parser.add_argument(
        '--all_final_tensors_names_list',
        type=str,
        default='final_result',
        help="""\
      The name of all the output classification layers in the retrained graph, comma separated.\
      """
    )

    FLAGS, unparsed = parser.parse_known_args()
    tf.app.run(main=main, argv=[sys.argv[0]] + unparsed)