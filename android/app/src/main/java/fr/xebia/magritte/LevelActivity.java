package fr.xebia.magritte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.level_one)
    public void onLevelOneClick() {
        startActivity(new Intent(this, ClassifierActivity.class));
    }
}
