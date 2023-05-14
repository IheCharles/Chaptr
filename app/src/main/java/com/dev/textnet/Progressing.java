package com.dev.textnet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Dell on 2017/12/28.
 */

public class Progressing extends Dialog implements android.view.View.OnClickListener {
    public  Activity c;
    String Check;
    public TextView textView1;
    TextView textView2;
    TextView textView3;

    public Progressing(@NonNull Context context) {
        super(context);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.progressing);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textView1=findViewById(R.id.progressing_1);
        textView2=findViewById(R.id.progressing_2);
        textView3=findViewById(R.id.progressing_3);
    }

    @Override
    public void onClick(View view) {

    }
}
