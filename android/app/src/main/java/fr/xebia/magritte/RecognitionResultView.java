package fr.xebia.magritte;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.xebia.magritte.model.Fruit;

public class RecognitionResultView extends RelativeLayout implements ResultsView {

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
    public void displayTopMatch(Fruit fruit) {
        switch (fruit) {
            case APPLE:
                apple.setImageResource(fruit.getFilledRes());
                break;
            case STRAWBERRY:
                strawberry.setImageResource(fruit.getFilledRes());
                break;
            case GRAPE:
                grape.setImageResource(fruit.getFilledRes());
                break;
            case KIWI:
                kiwi.setImageResource(fruit.getFilledRes());
                break;
            case BANANA:
                banana.setImageResource(fruit.getFilledRes());
                break;
            default:
                break;
        }

    }
}
