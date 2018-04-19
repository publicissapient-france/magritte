package fr.xebia.magritte.language

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.xebia.magritte.*
import fr.xebia.magritte.level.LevelActivity
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        choice_fr.setOnClickListener { onLanguageClicked(LANG_FR) }
        choice_cn.setOnClickListener { onLanguageClicked(LANG_CN) }
        choice_it.setOnClickListener { onLanguageClicked(LANG_IT) }
    }

    private fun onLanguageClicked(code: Int) {
        val intent = Intent(this, LevelActivity::class.java)
        intent.putExtra(LANGUAGE_CHOICE, code)
        startActivity(intent)
    }
}
