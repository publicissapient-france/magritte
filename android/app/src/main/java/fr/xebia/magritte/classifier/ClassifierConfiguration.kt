package fr.xebia.magritte.classifier

import android.os.Parcel
import android.os.Parcelable

class ClassifierConfiguration(val inputSize: Int,
                              val imageMean: Int,
                              val imageStd: Float,
                              val inputName: String,
                              val outputName: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readInt(),
            source.readFloat(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(inputSize)
        writeInt(imageMean)
        writeFloat(imageStd)
        writeString(inputName)
        writeString(outputName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ClassifierConfiguration> = object : Parcelable.Creator<ClassifierConfiguration> {
            override fun createFromParcel(source: Parcel): ClassifierConfiguration = ClassifierConfiguration(source)
            override fun newArray(size: Int): Array<ClassifierConfiguration?> = arrayOfNulls(size)
        }
    }
}

val normalModelConf = ClassifierConfiguration(299, 128, 128F, "Mul", "final_result_fruits")
val mobilenetsModelConf = ClassifierConfiguration(224, 128, 128F, "input", "final_result_fruits")
