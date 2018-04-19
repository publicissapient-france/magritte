package fr.xebia.magritte.level

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.xebia.magritte.*
import fr.xebia.magritte.classifier.ClassifierActivity
import fr.xebia.magritte.data.MagritteRepository
import fr.xebia.magritte.model.MagritteModel
import fr.xebia.magritte.model.TFModelType
import kotlinx.android.synthetic.main.activity_level.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber
import java.util.*

@RuntimePermissions
class LevelActivity : AppCompatActivity(), LevelContract.View {

    private var languageChoice: Int = 0
    private lateinit var presenter: LevelContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        presenter = LevelPresenter(this, Dependency.get(MagritteRepository::class.java))

        intent.extras?.let {
            languageChoice = it.getInt(LANGUAGE_CHOICE)
            val currentModel = Dependency.get(MagritteModel::class.java)
            level_one.setOnClickListener {
                if (currentModel.modelType == TFModelType.TF_MOBILE) {
                    startClassifierWithPermissionCheck(currentModel.id, CATEGORY_FRUIT)
                } else {
                    // TODO
                    Timber.d("Launch lite version!")
                }
            }
        }
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun startClassifier(modelId: String, category: String) {
        presenter.loadData(modelId, category)
    }

    override fun displayClassifier(filePath: String?, labels: List<String>) {
        val intent = Intent(this, ClassifierActivity::class.java)
        intent.putExtra(LANGUAGE_CHOICE, languageChoice)
        intent.putExtra(MODEL_FILE, filePath)
        intent.putStringArrayListExtra(MODEL_LABELS, labels as ArrayList<String>)
        startActivity(intent)
    }

    override fun displayError() {
        Timber.e("Launch classifier error")
    }
}
