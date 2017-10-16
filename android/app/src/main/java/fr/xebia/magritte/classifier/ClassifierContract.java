package fr.xebia.magritte.classifier;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;
import java.util.Locale;

import fr.xebia.magritte.model.FruitResource;

public interface ClassifierContract {

    interface View {

        Context getContext();

        void displayRecognitions(List<Classifier.Recognition> recognitionList);

        void displayTopMatch(FruitResource fruit);

        void speakResult(String title);
    }

    interface Presenter {

        void recognizeImage(Bitmap bitmap);

        String getLocalizedString(Locale locale, int resId);
    }
}
