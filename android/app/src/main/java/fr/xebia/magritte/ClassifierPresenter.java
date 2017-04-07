package fr.xebia.magritte;

import android.graphics.Bitmap;

import java.util.List;
import java.util.concurrent.Callable;

import fr.xebia.magritte.model.ClassifierContract;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ClassifierPresenter implements ClassifierContract.Presenter {

    private static final double MATCH_THRESHOLD = 0.6;

    private ClassifierContract.View view;
    private Classifier classifier;
    private Classifier.Recognition topMatch;

    public ClassifierPresenter(ClassifierContract.View view, Classifier classifier) {
        this.view = view;
        this.classifier = classifier;
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
                        Classifier.Recognition topMatch = getTopMatch(results);
                        view.displayRecognitions(results);

                        if (topMatch != null) {
                            if (ClassifierPresenter.this.topMatch == null
                                    || !ClassifierPresenter.this.topMatch.getTitle().equals(topMatch.getTitle())) {
                                view.speakResult(topMatch.getTitle());
                                ClassifierPresenter.this.topMatch = topMatch;
                            }
                            view.displayTopMatch(topMatch);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private Classifier.Recognition getTopMatch(List<Classifier.Recognition> recognitions) {
        for (Classifier.Recognition recog : recognitions) {
            if (recog.getConfidence() > MATCH_THRESHOLD) {
                return recog;
            }
        }
        return null;
    }
}
