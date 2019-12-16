package com.example.control;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.workbaidumap.R;

public class CardDialog extends Dialog {

    private CardDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public static class Builder {
        private View mLayout;
        private CardDialog mDialog;
        private TextView title;
        private TextView message;

        public Builder(Context context) {
            mDialog = new CardDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 加载布局文件
            mLayout = inflater.inflate(R.layout.card_dialog, null, false);
            // 添加布局文件到 Dialog
            mDialog.addContentView(mLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                       ViewGroup.LayoutParams.WRAP_CONTENT));
            title = (TextView) mLayout.findViewById(R.id.card_title);
            message = (TextView) mLayout.findViewById(R.id.card_message);
        }

        public Builder setTitle(@NonNull String mytitle) {
            title.setText(mytitle);
            return this;
        }

        public Builder setMessage(@NonNull String mymessage) {
            message.setText(mymessage);
            return this;
        }

        public CardDialog create() {
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);                //用户可以点击后退键关闭 Dialog
            mDialog.setCanceledOnTouchOutside(true);   //用户可以点击外部来关闭 Dialog
            return mDialog;
        }

        public CardDialog show() {
            final CardDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
