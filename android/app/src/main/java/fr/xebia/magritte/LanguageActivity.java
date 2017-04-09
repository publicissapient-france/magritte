package fr.xebia.magritte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageActivity extends AppCompatActivity {

    public static final String LANGUAGE_CHOICE = "LANGUAGE_CHOICE";

    public static final int LANG_FR = 1;
    public static final int LANG_CN = 2;
    public static final int LANG_IT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.choice_fr)
    public void onChoiceFranceClicked() {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(LANGUAGE_CHOICE, LANG_FR);
        startActivity(intent);
    }

    @OnClick(R.id.choice_cn)
    public void onChoiceChinaClicked() {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(LANGUAGE_CHOICE, LANG_CN);
        startActivity(intent);
    }

    @OnClick(R.id.choice_it)
    public void onChoiceItalyClicked() {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(LANGUAGE_CHOICE, LANG_IT);
        startActivity(intent);
    }
}
