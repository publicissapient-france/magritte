package fr.xebia.magritte.language

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import fr.xebia.magritte.*
import fr.xebia.magritte.level.LevelActivity
import fr.xebia.magritte.model.MagritteData
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity(), LanguageContract.View {

    private val TAG = LanguageActivity::class.java.simpleName
    private lateinit var presenter: LanguageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        presenter = LanguagePresenter(this, Injection.provideRepository(applicationContext))
        presenter.getData()

        choice_fr.setOnClickListener { onLanguageClicked(LANG_FR) }
        choice_cn.setOnClickListener { onLanguageClicked(LANG_CN) }
        choice_it.setOnClickListener { onLanguageClicked(LANG_IT) }
    }

    private fun onLanguageClicked(code: Int) {
        val intent = Intent(this, LevelActivity::class.java)
        intent.putExtra(LANGUAGE_CHOICE, code)
        startActivity(intent)
    }

    override fun setPresenter(presenter: LanguageContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayResult(data: MagritteData) {
        Log.d(TAG, "Categories count: ${data.categories.size}")
        // TODO
    }

    override fun displayError(message: String) {
        // TODO
    }
}
