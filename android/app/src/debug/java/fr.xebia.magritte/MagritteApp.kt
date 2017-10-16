package fr.xebia.magritte

import android.app.Application

import com.facebook.stetho.Stetho

class MagritteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}
