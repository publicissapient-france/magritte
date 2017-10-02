package fr.xebia.magritte;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.xebia.magritte.classifier.ClassifierActivity;
import fr.xebia.magritte.model.Data;
import fr.xebia.magritte.service.MagritteService;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static fr.xebia.magritte.LanguageActivity.LANGUAGE_CHOICE;

@RuntimePermissions
public class LevelActivity extends AppCompatActivity {

    public static final String MODEL_TYPE = "MODEL_TYPE";
    public static final int MODEL_FRUIT = 0;
    public static final int MODEL_VEGETABLE = 1;

    private int languageChoice;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            languageChoice = bundle.getInt(LANGUAGE_CHOICE);
        }

        compositeDisposable = new CompositeDisposable();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void loadData() {
        MagritteService service = new Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(MagritteService.class);

        // get payload
        compositeDisposable.add(service.getData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse, this::handleError)
        );

        // TODO download model file
    }

    private void handleError(Throwable throwable) {

    }

    private void handleResponse(Data data) {
        // TODO store payload
    }

    @OnClick(R.id.level_one)
    public void onLevelOneClick() {
        LevelActivityPermissionsDispatcher.startClassifierWithPermissionCheck(this, MODEL_FRUIT);
    }

    @OnClick(R.id.level_two)
    public void onLevelTwoClick() {
        // TODO
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
        intent.putExtra(LANGUAGE_CHOICE, languageChoice);
        startActivity(intent);
    }
}
