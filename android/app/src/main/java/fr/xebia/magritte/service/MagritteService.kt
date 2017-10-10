package fr.xebia.magritte.service

import fr.xebia.magritte.model.MagritteData
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

interface MagritteService {

    @GET("categories")
    fun getData(): Observable<MagritteData>

    @Streaming
    @GET("model")
    fun getModelFile(): Observable<ResponseBody>
}
