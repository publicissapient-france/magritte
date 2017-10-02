package fr.xebia.magritte.data

import fr.xebia.magritte.model.MagritteData
import fr.xebia.magritte.service.MagritteService
import io.reactivex.Observable
import okhttp3.ResponseBody

class MagritteRepositoryImpl(private val service: MagritteService) : MagritteRepository {

    override fun getData(): Observable<MagritteData> {
        return service.getData()
    }

    override fun getModelFile(): Observable<ResponseBody> {
        TODO()
    }
}