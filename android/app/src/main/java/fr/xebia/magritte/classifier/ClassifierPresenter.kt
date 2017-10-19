package fr.xebia.magritte.classifier

import android.content.res.Configuration
import android.graphics.Bitmap
import android.util.Log
import fr.xebia.magritte.model.FruitResource
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ClassifierPresenter(private val view: ClassifierContract.View, private val desiredLocale: Locale, private val classifier: Classifier) : ClassifierContract.Presenter {
    private var topMatch: Classifier.Recognition? = null

    override fun recognizeImage(bitmap: Bitmap) {
        val recognitionSingle = Single.fromCallable { classifier.recognizeImage(bitmap) }

        recognitionSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<MutableList<Classifier.Recognition>> {

                    override fun onSubscribe(d: Disposable) {}
                    override fun onSuccess(results: MutableList<Classifier.Recognition>) {
                        view.displayRecognitions(results)

                        val topMatch = getTopMatch(results)
                        if (topMatch != null) {
                            proceedTopMatch(topMatch)
                        }
                    }

                    override fun onError(e: Throwable) {}
                })
    }

    private fun getFruitResult(recognition: Classifier.Recognition): FruitResource {
        return try {
            FruitResource.valueOf(recognition.title.toUpperCase())
        } catch (e: IllegalArgumentException) {
            Log.d(TAG, "No matching")
            FruitResource.UNKNOWN
        }
    }

    private fun proceedTopMatch(topMatch: Classifier.Recognition?) {
        if (this.topMatch == null || this.topMatch!!.title != topMatch!!.title) {
            val fruit = topMatch?.let { getFruitResult(it) }
            if (fruit != FruitResource.UNKNOWN) {
                fruit?.let {
                    view.displayTopMatch(it)
                    view.speakResult(getLocalizedString(desiredLocale, fruit.titleRes))
                }
            }
            this.topMatch = topMatch
        }
    }

    override fun getLocalizedString(locale: Locale, resId: Int): String {
        val context = view.context
        var conf = context.resources.configuration
        conf = Configuration(conf)
        conf.setLocale(locale)
        val localizedContext = context.createConfigurationContext(conf)
        return localizedContext.resources.getString(resId)
    }

    private fun getTopMatch(recognitions: List<Classifier.Recognition>): Classifier.Recognition? {
        return recognitions.firstOrNull { it.confidence > MATCH_THRESHOLD }
    }

    companion object {

        private val MATCH_THRESHOLD = 0.85
        private val TAG = ClassifierPresenter::class.java.simpleName
    }
}
