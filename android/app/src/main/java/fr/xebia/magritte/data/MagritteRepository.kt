package fr.xebia.magritte.data

import fr.xebia.magritte.data.local.MagritteLabel
import fr.xebia.magritte.model.MagritteModel
import fr.xebia.magritte.model.MagritteVersion
import fr.xebia.magritte.model.TFLabel
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.ResponseBody
import java.io.File

interface MagritteRepository {

    // remote
    fun getVersion(): Observable<MagritteVersion>
    fun getModels(): Observable<List<MagritteModel>>
    fun getModelLabel(category: String): Observable<List<TFLabel>>
    fun downloadModelFile(modelPath: String): Observable<ResponseBody>

    // local
    fun storeModels(model: MagritteModel) : Observable<Unit>?

    fun insertLabels(category: String, labels: List<TFLabel>): Observable<Unit>?
    fun readLabels(category: String): Flowable<List<MagritteLabel>>
    fun saveModelFileToDisk(responseBody: ResponseBody, modelId: String): Observable<File>
    fun getModelFilePath(modelId: String): String?

    fun setInitDataLoadingStatus(loaded: Boolean)
    fun getInitDataLoadingStatus(): Boolean

}