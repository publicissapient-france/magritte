package fr.xebia.magritte.service

import fr.xebia.magritte.model.MagritteData
import fr.xebia.magritte.model.TFLabel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming

interface MagritteService {

    @GET("categories")
    fun getData(): Observable<MagritteData>

    @GET("labels")
    fun getLabels(@Query("category") category: String): Observable<List<TFLabel>>

    @Streaming
    @GET("model")
    fun getModelFile(): Observable<ResponseBody>
}
