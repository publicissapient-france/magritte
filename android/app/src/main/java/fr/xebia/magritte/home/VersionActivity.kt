package fr.xebia.magritte.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.xebia.magritte.MagritteApp
import fr.xebia.magritte.R
import fr.xebia.magritte.classifier.mobilenetsModelConf
import fr.xebia.magritte.classifier.normalModelConf
import kotlinx.android.synthetic.main.activity_version.*

class VersionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_version)

        // TODO list version from API

        modelNormal.setOnClickListener {
            MagritteApp.configuration = normalModelConf
            MagritteApp.modelVersion = "20170607"
            startActivity(Intent(this, MainActivity::class.java))
        }
        modelMobileNets.setOnClickListener {
            MagritteApp.configuration = mobilenetsModelConf
            MagritteApp.modelVersion = "20171016"
            startActivity(Intent(this, MainActivity::class.java))
        }
        modelMobileNetsQuantized.setOnClickListener {
            MagritteApp.configuration = mobilenetsModelConf
            MagritteApp.modelVersion = "20171021"
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
