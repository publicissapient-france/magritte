package fr.xebia.magritte.classifier;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import fr.xebia.magritte.model.FruitResource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ClassifierPresenter implements ClassifierContract.Presenter {

    private static final double MATCH_THRESHOLD = 0.9;
    private static final String TAG = ClassifierPresenter.class.getSimpleName();

    private Locale desiredLocale;

    private ClassifierContract.View view;
    private Classifier classifier;
    private Classifier.Recognition topMatch;

    public ClassifierPresenter(ClassifierContract.View view, Locale desiredLocale, Classifier classifier) {
        this.view = view;
        this.classifier = classifier;
        this.desiredLocale = desiredLocale;
    }

    @Override
    public void recognizeImage(final Bitmap bitmap) {
        Single<List<Classifier.Recognition>> recognitionSingle = Single.fromCallable(new Callable<List<Classifier.Recognition>>() {

            @Override
            public List<Classifier.Recognition> call() throws Exception {
                return classifier.recognizeImage(bitmap);
            }
        });

        recognitionSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Classifier.Recognition>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Classifier.Recognition> results) {
                        view.displayRecognitions(results);

                        Classifier.Recognition topMatch = getTopMatch(results);
                        if (topMatch != null) {
                            proceedTopMatch(topMatch);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private FruitResource getFruitResult(Classifier.Recognition recognition) {
        try {
            return FruitResource.valueOf(recognition.getTitle().toUpperCase());
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "No matching");
            return null;
        }
    }

    private void proceedTopMatch(Classifier.Recognition topMatch) {
        if (this.topMatch == null || !this.topMatch.getTitle().equals(topMatch.getTitle())) {
            FruitResource fruit = getFruitResult(topMatch);
            if (fruit != null) {
                view.displayTopMatch(fruit);
                view.speakResult(getLocalizedString(desiredLocale, fruit.getTitleRes()));
            }
            this.topMatch = topMatch;
        }
    }

    public String getLocalizedString(Locale desiredLocale, int resId) {
        Context context = view.getContext();
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources().getString(resId);
    }

    private Classifier.Recognition getTopMatch(List<Classifier.Recognition> recognitions) {
        for (Classifier.Recognition recognition : recognitions) {
            if (recognition.getConfidence() > MATCH_THRESHOLD) {
                return recognition;
            }
        }
        return null;
    }
}
