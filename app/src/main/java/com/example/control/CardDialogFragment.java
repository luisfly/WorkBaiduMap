package com.example.control;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.workbaidumap.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 商品明细展示使用的弹窗
 */
public class CardDialogFragment extends DialogFragment {
    public CardDialogFragment() {

    }

    public static CardDialogFragment newInstance(String title, String message) {
        CardDialogFragment fragment = new CardDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 读取自定义 xml 文件
        View view = inflater.inflate(R.layout.card_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        // 读取控件信息，并设置

        // 父方法
        super.onViewCreated(view, savedInstanceState);
    }
}
