package com.github.florent37.singledateandtimepicker.dialog;

/**
 * Created by nor on 1/2/2017.
 */

public abstract class BaseDialog {
    public static final int DEFAULT_ITEM_COUNT_MODE_CURVED = 7;
    public static final int DEFAULT_ITEM_COUNT_MODE_NORMAL = 5;

    private boolean isDiplaying;

    public void display() {
        setDiplaying(true);
    }

    public void close() {
        setDiplaying(false);
    }

    public boolean isDiplaying() {
        return isDiplaying;
    }

    public void setDiplaying(boolean isDisplaying) {
        this.isDiplaying = isDisplaying;
    }
    protected void onClose(){
        setDiplaying(false);
    }
}
