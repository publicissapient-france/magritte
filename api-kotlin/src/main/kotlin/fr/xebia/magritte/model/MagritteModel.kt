package fr.xebia.magritte.model

import com.google.gson.annotations.SerializedName

data class MagritteModel(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("model_file") val modelFile: String,
        @SerializedName("model_type") val modelType: TFModelType,
        @SerializedName("configuration") val configuration: MagritteConfiguration
)