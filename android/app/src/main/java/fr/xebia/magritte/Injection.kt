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
                    .baseUrl(String.format(BuildConfig.API_ENDPOINT, MagritteApp.modelVersion))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build().create(MagritteService::class.java)

            val dbName = String.format(MAGRITTE_DATABASE, MagritteApp.modelVersion)
            val database = Room.databaseBuilder(context, MagritteDatabase::class.java, dbName).build()
            return MagritteRepository(context, service, database, MagritteApp.modelVersion)
        }
    }
}