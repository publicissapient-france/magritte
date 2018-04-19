package fr.xebia.magritte

import android.app.Application
import fr.xebia.magritte.model.MagritteModel

class MagritteApp : Application() {

    companion object {
        lateinit var instance: MagritteApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        MagritteAppComponent().initializeDependencies(this)
    }

    fun injectModel(model: MagritteModel) {
        MagritteAppComponent().injectModel(model)
    }

}