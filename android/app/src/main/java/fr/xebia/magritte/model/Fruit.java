package fr.xebia.magritte.model;

import fr.xebia.magritte.R;

public enum Fruit {

    APPLE(R.drawable.ic_apple_outline, R.drawable.ic_apple_filled),
    GRAPE(R.drawable.ic_grape_outline, R.drawable.ic_grape_filled),
    KIWI(R.drawable.ic_kiwi_outline, R.drawable.ic_kiwi_filled),
    BANANA(R.drawable.ic_banana_outline, R.drawable.ic_banana_filled),
    STRAWBERRY(R.drawable.ic_strawberry_outline, R.drawable.ic_strawberry_filled);

    private int outlineRes;
    private int filledRes;

    Fruit(int outlineRes, int filledRes) {
        this.outlineRes = outlineRes;
        this.filledRes = filledRes;
    }

    public int getOutlineRes() {
        return outlineRes;
    }

    public int getFilledRes() {
        return filledRes;
    }
}
