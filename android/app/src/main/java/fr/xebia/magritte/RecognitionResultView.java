package fr.xebia.magritte;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.xebia.magritte.model.Fruit;

public class RecognitionResultView extends RelativeLayout implements ResultsView {

    private static final String TAG = RecognitionResultView.class.getSimpleName();

    private Classifier.Recognition recognition;

    @BindView(R.id.result)
    AppCompatTextView resultTextView;

    @BindView(R.id.strawberry_iv)
    AppCompatImageView strawberry;

    @BindView(R.id.apple_iv)
    AppCompatImageView apple;

    @BindView(R.id.grape_iv)
    AppCompatImageView grape;

    @BindView(R.id.kiwi_iv)
    AppCompatImageView kiwi;

    @BindView(R.id.banana_iv)
    AppCompatImageView banana;

    public RecognitionResultView(final Context context, final AttributeSet set) {
        super(context, set);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void displayResults(List<Classifier.Recognition> recognitions) {
        resultTextView.setText("");
        for (Classifier.Recognition recognition : recognitions) {
            resultTextView.append(getResources().getString(R.string.result, recognition.getTitle(), recognition.getConfidence()));
        }
    }

    @Override
    public void setTopMatch(Classifier.Recognition recognition) {
        try {
            Fruit fruit = Fruit.valueOf(recognition.getTitle().toUpperCase());
            switch (fruit) {
                case APPLE:
                    apple.setImageResource(R.drawable.ic_apple_filled);
                    break;
                case STRAWBERRY:
                    strawberry.setImageResource(R.drawable.ic_strawberry_filled);
                    break;
                case GRAPE:
                    grape.setImageResource(R.drawable.ic_grape_filled);
                    break;
                case KIWI:
                    kiwi.setImageResource(R.drawable.ic_kiwi_filled);
                    break;
                case BANANA:
                    banana.setImageResource(R.drawable.ic_banana_filled);
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "No matching");
        }
    }
}
