package com.dev.textnet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Dell on 2017/12/16.
 */

public class Text_size  extends Dialog implements android.view.View.OnClickListener  {


    private CheckBox checkbox_14sp,checkbox_18sp,checkbox_22sp,checkbox_26sp,checkbox_30sp;
    public Activity c;
    private SharedPreferences.Editor editor;
    private int Textsize;
    EditText s;

    public Text_size(Activity a, int TS, EditText story) {
        super(a);
        this.c=a;
        Textsize=TS;
        s=story;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.text_size);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout linearLayout_14sp = findViewById(R.id.linearlayout_14sp);
        LinearLayout linearLayout_18sp = findViewById(R.id.linearlayout_18sp);
        LinearLayout linearLayout_22sp = findViewById(R.id.linearlayout_22sp);
        LinearLayout linearLayout_26sp = findViewById(R.id.linearlayout__26sp);
        LinearLayout linearLayout_30sp = findViewById(R.id.linearlayout__30sp);

        checkbox_14sp=findViewById(R.id.checkBox_14sp);
        checkbox_18sp=findViewById(R.id.checkBox_18sp);
        checkbox_22sp=findViewById(R.id.checkBox_22sp);
        checkbox_26sp=findViewById(R.id.checkBox_26sp);
        checkbox_30sp=findViewById(R.id.checkBox_30sp);

        checkbox_14sp.setClickable(false);
        checkbox_18sp.setClickable(false);
        checkbox_22sp.setClickable(false);
        checkbox_26sp.setClickable(false);
        checkbox_30sp.setClickable(false);

        SharedPreferences sharedPreferences = c.getSharedPreferences("TF", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        linearLayout_14sp.setOnClickListener(this);
        linearLayout_18sp.setOnClickListener(this);
        linearLayout_22sp.setOnClickListener(this);
        linearLayout_26sp.setOnClickListener(this);
        linearLayout_30sp.setOnClickListener(this);


        if (s.getTextSize()==14){
            checkbox_14sp.setChecked(true);
        }
        if (s.getTextSize()==18){
            checkbox_18sp.setChecked(true);
        }
        if (s.getTextSize()==22){
            checkbox_22sp.setChecked(true);
        }
        if (s.getTextSize()==26){
            checkbox_26sp.setChecked(true);
        }
        if (s.getTextSize()==30){
            checkbox_30sp.setChecked(true);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linearlayout_14sp:
                checkbox_14sp.setChecked(true);
                checkbox_18sp.setChecked(false);
                checkbox_22sp.setChecked(false);
                checkbox_26sp.setChecked(false);
                checkbox_30sp.setChecked(false);
                editor.putInt("TextSize",14);
                editor.apply();
                s.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            break;
            case R.id.linearlayout_18sp:
                checkbox_14sp.setChecked(false);
                checkbox_18sp.setChecked(true);
                checkbox_22sp.setChecked(false);
                checkbox_26sp.setChecked(false);
                checkbox_30sp.setChecked(false);
                editor.putInt("TextSize",18);
                editor.apply();
                s.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            break;
            case R.id.linearlayout_22sp:
                checkbox_14sp.setChecked(false);
                checkbox_18sp.setChecked(false);
                checkbox_22sp.setChecked(true);
                checkbox_26sp.setChecked(false);
                checkbox_30sp.setChecked(false);
                editor.putInt("TextSize",22);
                editor.apply();
                s.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
            break;
            case R.id.linearlayout__26sp:
                checkbox_14sp.setChecked(false);
                checkbox_18sp.setChecked(false);
                checkbox_22sp.setChecked(false);
                checkbox_26sp.setChecked(true);
                checkbox_30sp.setChecked(false);
                editor.putInt("TextSize",26);
                editor.apply();
                s.setTextSize(TypedValue.COMPLEX_UNIT_SP,26);
            break;
            case R.id.linearlayout__30sp:
                checkbox_14sp.setChecked(false);
                checkbox_18sp.setChecked(false);
                checkbox_22sp.setChecked(false);
                checkbox_26sp.setChecked(false);
                checkbox_30sp.setChecked(true);
                editor.putInt("TextSize",30);
                editor.apply();
                s.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
            break;

        }
    }
}
