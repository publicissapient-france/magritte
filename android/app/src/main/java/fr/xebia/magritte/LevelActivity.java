package fr.xebia.magritte;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class LevelActivity extends AppCompatActivity {

    public static final String MODEL_TYPE = "MODEL_TYPE";
    public static final int MODEL_FRUIT = 0;
    public static final int MODEL_VEGETABLE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.level_one)
    public void onLevelOneClick() {
        LevelActivityPermissionsDispatcher.startClassifierWithCheck(this, MODEL_FRUIT);
    }

    @OnClick(R.id.level_two)
    public void onLevelTwoClick() {
        LevelActivityPermissionsDispatcher.startClassifierWithCheck(this, MODEL_VEGETABLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LevelActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void startClassifier(int modelType) {
        Intent intent = new Intent(this, ClassifierActivity.class);
        intent.putExtra(MODEL_TYPE, modelType);
        startActivity(intent);
    }
}
