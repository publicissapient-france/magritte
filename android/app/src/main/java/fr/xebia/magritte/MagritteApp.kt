package fr.xebia.magritte

import android.app.Application
import com.facebook.stetho.Stetho
import fr.xebia.magritte.classifier.ClassifierConfiguration

class MagritteApp : Application() {

    companion object {
        lateinit var instance: MagritteApp
            private set

        lateinit var configuration: ClassifierConfiguration
        lateinit var modelVersion: String
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Stetho.initializeWithDefaults(this)
    }
}