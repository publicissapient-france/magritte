package fr.xebia.magritte.classifier

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import fr.xebia.magritte.R
import fr.xebia.magritte.model.FruitResource
import kotlinx.android.synthetic.main.camera_fragment.view.*

class RecognitionResultView(context: Context, set: AttributeSet) : RelativeLayout(context, set), ResultsView {

    override fun displayResults(recognitions: List<Classifier.Recognition>) {
        debugResultTextView?.text = ""
        for (recognition in recognitions) {
            debugResultTextView?.append(resources.getString(R.string.result, recognition.title, recognition.confidence))
        }
    }

    override fun displayTopMatch(fruit: FruitResource) {
        when (fruit) {
            FruitResource.APPLE -> appleIconView?.setImageResource(fruit.filledRes)
            FruitResource.STRAWBERRY -> strawberry_iv?.setImageResource(fruit.filledRes)
            FruitResource.GRAPE -> grapeIconView?.setImageResource(fruit.filledRes)
            FruitResource.KIWI -> kiwiIconView?.setImageResource(fruit.filledRes)
            FruitResource.BANANA -> bananaIconView?.setImageResource(fruit.filledRes)
            else -> {
            }
        }
    }
}
