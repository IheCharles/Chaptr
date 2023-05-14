package com.dev.textnet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Dell on 2018/02/11.
 */

public class Notice extends Dialog implements android.view.View.OnClickListener {
        TextView textView;
    private static final int SELECT_FILE = 2;
    Activity a;
    public Notice(Activity c) {
        super(c);
        a=c;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.notice);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textView=findViewById(R.id.textViewg);
        textView.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewg:
               dismiss();
                break;
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        a.startActivityForResult(intent, SELECT_FILE);
    }


}
