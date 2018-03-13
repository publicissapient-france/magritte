package fr.xebia.magritte.model

import com.google.gson.annotations.SerializedName

data class MagritteConfiguration(
        @SerializedName("input_size") val inputSize: Int,
        @SerializedName("image_mean") val imageMean: Int,
        @SerializedName("image_std") val imageStd: Int,
        @SerializedName("input_name") val inputName: String,
        @SerializedName("output_name") val outputName: String
)

val tfMobileConfiguration = MagritteConfiguration(
        224, 128, 128, "Mul", "final_result_fruits"
)

val tfLiteConfiguration = MagritteConfiguration(
        224, 128, 128, "Mul", "final_result_fruits"
)