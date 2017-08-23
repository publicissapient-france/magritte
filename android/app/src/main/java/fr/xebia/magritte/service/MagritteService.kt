package fr.xebia.magritte.service

import fr.xebia.magritte.model.Data
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET

interface MagritteService {

    @get:GET("data")
    val data: Observable<Data>

    @GET("model")
    fun downloadModelFile(): Observable<ResponseBody>
}
