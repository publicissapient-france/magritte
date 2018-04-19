package fr.xebia.magritte.model

import com.squareup.moshi.Json

enum class TFModelType(val typeName: String) {
    @Json(name = "tfmobile")
    TF_MOBILE("tfmobile"),

    @Json(name = "tflite")
    TF_LITE("tflite")
}