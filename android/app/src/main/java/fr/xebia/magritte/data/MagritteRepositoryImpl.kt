package fr.xebia.magritte.data

import android.content.Context
import fr.xebia.magritte.BuildConfig
import fr.xebia.magritte.data.local.MagritteDatabase
import fr.xebia.magritte.data.local.MagritteLabel
import fr.xebia.magritte.model.MagritteModel
import fr.xebia.magritte.model.MagritteVersion
import fr.xebia.magritte.model.TFLabel
import fr.xebia.magritte.service.MagritteService
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.ResponseBody
import okio.Okio
import java.io.File
import java.io.IOException

class MagritteRepositoryImpl(val context: Context,
                             private val service: MagritteService,
                             private val database: MagritteDatabase) : MagritteRepository {

    private var spHelper: SharedPreferenceHelper = SharedPreferenceHelper(context)

    override fun getVersion(): Observable<MagritteVersion> {
        return service.getVersion()
    }

    override fun getModels(): Observable<List<MagritteModel>> {
        return service.getModels()
    }

    override fun getModelLabel(category: String): Observable<List<TFLabel>> {
        return service.getLabels(category)
    }

    override fun downloadModelFile(modelPath: String): Observable<ResponseBody> {
        val fullUrl = "${BuildConfig.API_ENDPOINT}/$modelPath"
        return service.getModelFile(fullUrl)
    }

    override fun storeModels(model: MagritteModel): Observable<Unit>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertLabels(category: String, labels: List<TFLabel>): Observable<Unit>? {
        return Observable.fromCallable {
            database.magritteLabelDao().insertAll(labels.map { MagritteLabel(it.value, category) })
        }
    }

    override fun readLabels(category: String): Flowable<List<MagritteLabel>> {
        return database.magritteLabelDao().getAllLabels(category)
    }

    override fun saveModelFileToDisk(responseBody: ResponseBody, modelId: String): Observable<File> {
        return Observable.create { emitter ->
            try {
                val modelFileName: String = String.format("magritte_model_%s.pb", modelId)
                val destinationFile = File("${context.filesDir}${File.separator}$modelFileName")

                val bufferedSink = Okio.buffer(Okio.sink(destinationFile))
                bufferedSink.writeAll(responseBody.source())
                bufferedSink.close()

                spHelper.storeFile(modelId, destinationFile.absolutePath)

                emitter.onNext(destinationFile)
                emitter.onComplete()
            } catch (e: IOException) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    override fun getModelFilePath(modelId: String): String? {
        return spHelper.getModelFilePath(modelId)
    }

    override fun setInitDataLoadingStatus(loaded: Boolean) {
        spHelper.setInitDataStatus(loaded)
    }

    override fun getInitDataLoadingStatus(): Boolean {
        return spHelper.getInitDataStatus()
    }
}