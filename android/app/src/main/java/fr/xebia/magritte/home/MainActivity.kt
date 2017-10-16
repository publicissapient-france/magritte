package fr.xebia.magritte.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import fr.xebia.magritte.Injection
import fr.xebia.magritte.R
import fr.xebia.magritte.language.LanguageActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {

    private val TAG: String = MainPresenter::class.java.simpleName
    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, Injection.provideRepository(applicationContext))
        presenter.loadInitData()

        startButton.setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }

        retryButton.setOnClickListener {
            presenter.loadInitData()
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
        val VIEW_LOADING_CHILD = 0
        val VIEW_SUCCESS_CHILD = 1
        val VIEW_ERROR_CHILD = 2
    }
}
