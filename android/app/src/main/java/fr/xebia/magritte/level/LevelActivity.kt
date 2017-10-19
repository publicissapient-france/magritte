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
import java.util.*

@RuntimePermissions
class LevelActivity : AppCompatActivity(), LevelContract.View {

    private var languageChoice: Int = 0
    private lateinit var presenter: LevelContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        presenter = LevelPresenter(this, Injection.provideRepository(applicationContext))

        val bundle = intent.extras
        if (bundle != null) {
            languageChoice = bundle.getInt(LANGUAGE_CHOICE)
        }
        level_one.setOnClickListener {
            startClassifierWithPermissionCheck(CATEGORY_FRUIT)
        }
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun startClassifier(category: String) {
        presenter.loadData(category)
    }

    override fun displayClassifier(filePath: String?, labels: List<String>) {
        val intent = Intent(this, ClassifierActivity::class.java)
        intent.putExtra(LANGUAGE_CHOICE, languageChoice)
        intent.putExtra(MODEL_FILE, filePath)
        intent.putStringArrayListExtra(MODEL_LABELS, labels as ArrayList<String>)
        startActivity(intent)
    }

    override fun displayError() {
        // TODO
    }
}
