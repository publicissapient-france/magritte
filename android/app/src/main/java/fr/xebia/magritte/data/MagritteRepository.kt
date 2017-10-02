package fr.xebia.magritte.data

import fr.xebia.magritte.model.MagritteData
import io.reactivex.Observable
import okhttp3.ResponseBody

interface MagritteRepository {

    fun getData(): Observable<MagritteData>

    fun getModelFile(): Observable<ResponseBody>
}