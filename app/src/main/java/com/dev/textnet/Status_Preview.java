package com.dev.textnet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Dell on 2018/01/29.
 */

public class Status_Preview extends Dialog  {
    TextView textView;
    int color;
    CardView cardView;
    String status;
    int contrast;
    public Status_Preview(@NonNull Context context,String statu,int col,int trast) {
        super(context);
        color=col;
        status=statu;
        contrast=trast;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.status_preview);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textView=findViewById(R.id.status_prev_textview);
        cardView=findViewById(R.id.status_prev_cardview);
        cardView.setCardBackgroundColor(color);
        textView.setText(status);
        textView.setTextColor(contrast);
    }

}
