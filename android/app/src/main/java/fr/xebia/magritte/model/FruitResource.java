package fr.xebia.magritte.model;

import fr.xebia.magritte.R;

public enum FruitResource {

    APPLE(R.drawable.ic_apple_outline, R.drawable.ic_apple_filled, R.string.apple),
    GRAPE(R.drawable.ic_grape_outline, R.drawable.ic_grape_filled, R.string.grape),
    KIWI(R.drawable.ic_kiwi_outline, R.drawable.ic_kiwi_filled, R.string.kiwi),
    BANANA(R.drawable.ic_banana_outline, R.drawable.ic_banana_filled, R.string.banana),
    STRAWBERRY(R.drawable.ic_strawberry_outline, R.drawable.ic_strawberry_filled, R.string.strawberry),
    UNKNOWN(0, 0, 0);

    private int outlineRes;
    private int filledRes;
    private int titleRes;

    FruitResource(int outlineRes, int filledRes, int titleRes) {
        this.outlineRes = outlineRes;
        this.filledRes = filledRes;
        this.titleRes = titleRes;
    }

    public int getOutlineRes() {
        return outlineRes;
    }

    public int getFilledRes() {
        return filledRes;
    }

    public int getTitleRes() {
        return titleRes;
    }
}
