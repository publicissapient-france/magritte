package fr.xebia.magritte.level

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.xebia.magritte.*
import fr.xebia.magritte.classifier.ClassifierActivity
import fr.xebia.magritte.data.SharedPreferenceHelper
import kotlinx.android.synthetic.main.activity_level.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class LevelActivity : AppCompatActivity() {

    private var languageChoice: Int = 0
    private lateinit var spHelper: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)
        val bundle = intent.extras
        if (bundle != null) {
            languageChoice = bundle.getInt(LANGUAGE_CHOICE)
        }
        level_one.setOnClickListener {
            startClassifierWithPermissionCheck(MODEL_FRUIT)
        }
        spHelper = SharedPreferenceHelper(applicationContext)
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun startClassifier(modelType: Int) {
        val intent = Intent(this, ClassifierActivity::class.java)
        intent.putExtra(MODEL_TYPE, modelType)
        intent.putExtra(LANGUAGE_CHOICE, languageChoice)
        intent.putExtra(MODEL_FILE, spHelper.getModelFilePath())
        startActivity(intent)
    }
}
