package fr.xebia.magritte.data

import android.content.Context
import fr.xebia.magritte.model.MagritteData
import fr.xebia.magritte.service.MagritteService
import io.reactivex.Observable
import okhttp3.ResponseBody
import okio.Okio
import java.io.File
import java.io.IOException


class MagritteRepository(val context: Context, private val service: MagritteService) {

    private var spHelper: SharedPreferenceHelper = SharedPreferenceHelper(context)

    fun getData(): Observable<MagritteData> {
        return service.getData()
    }

    fun getModelLabel(): Observable<ResponseBody> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getModelFilePath(): String? {
        return spHelper.getModelFilePath()
    }

    fun getModelFile(): Observable<File> {
        return service.getModelFile()
                .flatMap { saveModelFileToDisk(it) }
    }

    private val MAGRITTE_MODEL_FILE_NAME: String = "magritte_model.pb"

    private fun saveModelFileToDisk(responseBody: ResponseBody): Observable<File> {
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

}