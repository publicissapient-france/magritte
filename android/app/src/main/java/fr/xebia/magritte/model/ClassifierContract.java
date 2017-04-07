package fr.xebia.magritte.model;

import android.graphics.Bitmap;

import java.util.List;

import fr.xebia.magritte.Classifier;

public interface ClassifierContract {

    interface View {

        void displayRecognitions(List<Classifier.Recognition> recognitionList);

        void displayTopMatch(Classifier.Recognition recognition);

        void speakResult(String title);
    }

    interface Presenter {

        void recognizeImage(Bitmap bitmap);
    }
}
