package fr.xebia.magritte

import android.content.Context
import fr.xebia.magritte.data.MagritteRepository
import fr.xebia.magritte.service.MagritteService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class Injection {

    companion object {

        fun provideRepository(context: Context): MagritteRepository {
            val service = Retrofit.Builder()
                    .baseUrl(BuildConfig.API_ENDPOINT)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build().create(MagritteService::class.java)
            return MagritteRepository(context, service)
        }
    }
}