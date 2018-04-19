package fr.xebia.magritte.service

import fr.xebia.magritte.model.MagritteModel
import fr.xebia.magritte.model.MagritteVersion
import fr.xebia.magritte.model.TFLabel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface MagritteService {

    @GET("version")
    fun getVersion(): Observable<MagritteVersion>

    @GET("models")
    fun getModels(): Observable<List<MagritteModel>>

    @GET("labels")
    fun getLabels(@Query("category") category: String): Observable<List<TFLabel>>

    @Streaming
    @GET
    fun getModelFile(@Url url: String): Observable<ResponseBody>
}
