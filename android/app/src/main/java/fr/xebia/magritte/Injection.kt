package fr.xebia.magritte

import android.arch.persistence.room.Room
import android.content.Context
import fr.xebia.magritte.data.MagritteRepository
import fr.xebia.magritte.data.local.MagritteDatabase
import fr.xebia.magritte.service.MagritteService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class Injection {

    companion object {

        fun provideRepository(context: Context): MagritteRepository {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()
            val service = Retrofit.Builder()
                    .client(client)
                    .baseUrl(BuildConfig.API_ENDPOINT)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build().create(MagritteService::class.java)

            val database = Room.databaseBuilder(context, MagritteDatabase::class.java, MAGRITTE_DATABASE).build()
            return MagritteRepository(context, service, database)
        }
    }
}