package fr.xebia.magritte

import android.arch.persistence.room.Room
import android.content.Context
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import fr.xebia.magritte.data.MagritteRepository
import fr.xebia.magritte.data.MagritteRepositoryImpl
import fr.xebia.magritte.data.local.MagritteDatabase
import fr.xebia.magritte.model.MagritteModel
import fr.xebia.magritte.service.MagritteService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class MagritteAppComponent {

    fun initializeDependencies(context: Context) {
        Dependency.set(MagritteRepository::class.java, provideRepository(context))
    }

    fun injectModel(magritteModel: MagritteModel) {
        Dependency.set(MagritteModel::class.java, magritteModel)
    }

    private fun provideRepository(context: Context): MagritteRepository {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        val jsonConverter = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        val service = Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.API_ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(jsonConverter))
                .build().create(MagritteService::class.java)

        val dbName = String.format(MAGRITTE_DATABASE_NAME, BuildConfig.DB_VERSION)
        val database = Room.databaseBuilder(context, MagritteDatabase::class.java, dbName).build()
        return MagritteRepositoryImpl(context, service, database)
    }
}