package fr.xebia.magritte.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MagritteConfiguration(
        @Json(name = "input_size") val inputSize: Int,
        @Json(name = "image_mean") val imageMean: Int,
        @Json(name = "image_std") val imageStd: Int,
        @Json(name = "input_name") val inputName: String,
        @Json(name = "output_name") val outputName: String
) : Parcelable