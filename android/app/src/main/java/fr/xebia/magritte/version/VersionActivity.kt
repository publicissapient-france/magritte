package fr.xebia.magritte.version

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.xebia.magritte.Dependency
import fr.xebia.magritte.R
import fr.xebia.magritte.data.MagritteRepository
import fr.xebia.magritte.home.MainActivity
import fr.xebia.magritte.home.MainActivity.Companion.EXTRA_MAGRITTE_MODEL
import fr.xebia.magritte.model.MagritteModel
import fr.xebia.magritte.model.TFModelType
import kotlinx.android.synthetic.main.activity_version.*
import timber.log.Timber

class VersionActivity : AppCompatActivity(), VersionContract.View {

    private val TAG = VersionActivity::class.java.simpleName
    private lateinit var presenter: VersionContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_version)

        presenter = VersionPresenter(this, Dependency.get(MagritteRepository::class.java))
        presenter.getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun setPresenter(presenter: VersionContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayResult(models: List<MagritteModel>) {
        models.findLast { it.modelType == TFModelType.TF_MOBILE }?.let {
            tfMobile.isEnabled = true
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_MAGRITTE_MODEL, it)
            tfMobile.setOnClickListener {
                startActivity(intent)
            }
        }

        models.findLast { it.modelType == TFModelType.TF_LITE }?.let {
            tfLite.isEnabled = true
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_MAGRITTE_MODEL, it)
            tfLite.setOnClickListener {
                startActivity(intent)
            }
        }
    }

    override fun displayError(message: String) {
        Timber.tag(TAG).e(message)
    }
}
