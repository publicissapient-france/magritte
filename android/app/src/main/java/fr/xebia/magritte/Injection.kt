package fr.xebia.magritte

import fr.xebia.magritte.data.MagritteRepository
import fr.xebia.magritte.data.MagritteRepositoryImpl
import fr.xebia.magritte.service.MagritteService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class Injection {

    companion object {

        fun provideRepository(): MagritteRepository {
            val service = Retrofit.Builder()
                    .baseUrl(BuildConfig.API_ENDPOINT)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build().create(MagritteService::class.java)
            return MagritteRepositoryImpl(service)
        }
    }
}