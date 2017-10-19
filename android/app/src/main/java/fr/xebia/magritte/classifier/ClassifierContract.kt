package fr.xebia.magritte.classifier

import android.content.Context
import android.graphics.Bitmap
import fr.xebia.magritte.model.FruitResource
import java.util.*

interface ClassifierContract {

    interface View {

        val context: Context

        fun displayRecognitions(recognitionList: MutableList<Classifier.Recognition>)

        fun displayTopMatch(fruit: FruitResource)

        fun speakResult(title: String)
    }

    interface Presenter {

        fun recognizeImage(bitmap: Bitmap)

        fun getLocalizedString(locale: Locale, resId: Int): String
    }
}
