package fr.xebia.magritte.data

import android.content.Context
import fr.xebia.magritte.data.local.MagritteDatabase
import fr.xebia.magritte.data.local.MagritteLabel
import fr.xebia.magritte.model.MagritteData
import fr.xebia.magritte.model.TFLabel
import fr.xebia.magritte.service.MagritteService
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.ResponseBody
import okio.Okio
import java.io.File
import java.io.IOException

class MagritteRepository(val context: Context,
                         private val service: MagritteService,
                         private val database: MagritteDatabase) {

    private var spHelper: SharedPreferenceHelper = SharedPreferenceHelper(context)

    fun getData(): Observable<MagritteData> {
        return service.getData()
    }

    fun getModelLabel(category: String): Observable<List<TFLabel>> {
        return service.getLabels(category)
    }

    fun getModelFilePath(): String? {
        return spHelper.getModelFilePath()
    }

    fun getModelFile(): Observable<ResponseBody> {
        return service.getModelFile()
    }

    fun insertLabels(category: String, labels: List<TFLabel>): Observable<Unit>? {
        return Observable.fromCallable {
            database.magritteLabelDao().insertAll(labels.map { MagritteLabel(it.value, category) })
        }
    }

    fun readLabels(category: String): Flowable<List<MagritteLabel>> {
        return database.magritteLabelDao().getAllLabels(category)
    }

    private val MAGRITTE_MODEL_FILE_NAME: String = "magritte_model.pb"

    fun saveModelFileToDisk(responseBody: ResponseBody): Observable<File> {
        return Observable.create { emitter ->
            try {
                val destinationFile = File("${context.filesDir}${File.separator}${MAGRITTE_MODEL_FILE_NAME}")

                val bufferedSink = Okio.buffer(Okio.sink(destinationFile))
                bufferedSink.writeAll(responseBody.source())
                bufferedSink.close()

                spHelper.storeFile(destinationFile.absolutePath)

                emitter.onNext(destinationFile)
                emitter.onComplete()
            } catch (e: IOException) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    fun setInitDataLoadingStatus(loaded: Boolean) {
        spHelper.setInitDataStatus(loaded)
    }

    fun getInitDataLoadingStatus(): Boolean {
        return spHelper.getInitDataStatus()
    }
}