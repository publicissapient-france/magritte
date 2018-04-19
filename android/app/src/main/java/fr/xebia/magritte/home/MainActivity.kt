package fr.xebia.magritte.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import fr.xebia.magritte.Dependency
import fr.xebia.magritte.MagritteApp
import fr.xebia.magritte.R
import fr.xebia.magritte.data.MagritteRepository
import fr.xebia.magritte.language.LanguageActivity
import fr.xebia.magritte.model.MagritteModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {

    private val TAG: String = MainPresenter::class.java.simpleName
    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, Dependency.get(MagritteRepository::class.java))
        intent.extras?.let {
            val model = it.getParcelable<MagritteModel>(EXTRA_MAGRITTE_MODEL)
            model?.let {
                MagritteApp.instance.injectModel(model)
                presenter.loadInitData(model)

                startButton.setOnClickListener {
                    startActivity(Intent(this, LanguageActivity::class.java))
                }

                retryButton.setOnClickListener {
                    presenter.loadInitData(model)
                }
            }
        }
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayLoading() {
        mainViewFlipper.displayedChild = VIEW_LOADING_CHILD
    }

    override fun displayLoadedWithSuccess() {
        mainViewFlipper.displayedChild = VIEW_SUCCESS_CHILD
        Toast.makeText(this, R.string.data_init_success, Toast.LENGTH_LONG).show()
    }

    override fun displayLoadingError() {
        mainViewFlipper.displayedChild = VIEW_ERROR_CHILD
        Toast.makeText(this, R.string.data_init_error, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val EXTRA_MAGRITTE_MODEL = "EXTRA_MAGRITTE_MODEL"

        const val VIEW_LOADING_CHILD = 0
        const val VIEW_SUCCESS_CHILD = 1
        const val VIEW_ERROR_CHILD = 2
    }
}
