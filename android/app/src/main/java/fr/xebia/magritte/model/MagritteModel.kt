package fr.xebia.magritte.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MagritteModel(
        @Json(name = "id") val id: String,
        @Json(name = "name") val name: String,
        @Json(name = "description") val description: String,
        @Json(name = "model_file_path") val modelFilePath: String,
        @Json(name = "model_type") val modelType: TFModelType,
        @Json(name = "configuration") val configuration: MagritteConfiguration
) : Parcelable