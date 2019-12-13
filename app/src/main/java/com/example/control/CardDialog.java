package com.example.control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

public class CardDialog extends Dialog {

    private CardDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        public Builder(Context context) {

        }
    }
}
