package com.dev.textnet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Dell on 2017/12/15.
 */

public class Typeface  extends Dialog implements android.view.View.OnClickListener  {


    private CheckBox checkBox_monospace,checkbox_serif,checkbox_sans,checkbox_normal,checkBox_Amatic_Bold,checkBox_Amatic_Regular,checkBox_Capture,checkBox_Caviar_Dreams,checkBox_Caviar_Dreams_Bold,checkBox_Ostrich,checkBox_OstrichSans,checkBox_Pacifico,checkBox_PlayfairDisplay_Bold,checkBox_PlayfairDisplay_Italic,checkBox_Respective;
    public Activity c;
    private SharedPreferences.Editor editor;
    private String TextStlye;
    EditText s;
    EditText t;

    public Typeface(Activity a, String TS, EditText story, EditText title) {
        super(a);
        this.c=a;
        TextStlye=TS;
        s=story;
        t=title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.text_style);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout linearLayout_monospace = findViewById(R.id.linearlayout_monospace);
        RelativeLayout linearLayout_normal = findViewById(R.id.linearlayout_normal);
        RelativeLayout linearLayout_sans = findViewById(R.id.linearlayout__sans);
        RelativeLayout linearLayout_serif = findViewById(R.id.linearlayout_serif);
        RelativeLayout linearLayout_Amatic_Bold = findViewById(R.id.linearlayout_Amatic_Bold);
        RelativeLayout linearLayout_Amatic_Regular = findViewById(R.id.linearlayout_Amatic_Regular);
        RelativeLayout linearLayout_Capture = findViewById(R.id.linearlayout_Capture);
        RelativeLayout linearLayout_Caviar_Dreams = findViewById(R.id.linearlayout_Caviar_Dreams);
        RelativeLayout linearLayout_Caviar_Dreams_Bold = findViewById(R.id.linearlayout_Caviar_Dreams_Bold);
        RelativeLayout linearLayout_Ostrich = findViewById(R.id.linearlayout_Ostrich);
        RelativeLayout linearLayout_OstrichSans = findViewById(R.id.linearlayout_OstrichSans);
        RelativeLayout linearLayout_Pacifico = findViewById(R.id.linearlayout_Pacifico);
        RelativeLayout linearLayout_PlayfairDisplay_Bold = findViewById(R.id.linearlayout_PlayfairDisplay_Bold);
        RelativeLayout linearLayout_PlayfairDisplay_Italic = findViewById(R.id.linearlayout_PlayfairDisplay_Italic);
        RelativeLayout linearLayout_Respective = findViewById(R.id.linearlayout_Respective);

        checkBox_monospace=findViewById(R.id.checkBox_monospace);
        checkbox_normal=findViewById(R.id.checkBox_normal);
        checkbox_sans=findViewById(R.id.checkBox_sans);
        checkbox_serif=findViewById(R.id.checkBox_serif);
        checkBox_Amatic_Bold=findViewById(R.id.checkBox_Amatic_Bold);
        checkBox_Amatic_Regular=findViewById(R.id.checkBox_Amatic_Regular);
        checkBox_Capture=findViewById(R.id.checkBox_Capture);
        checkBox_Caviar_Dreams=findViewById(R.id.checkBox_Caviar_Dreams);
        checkBox_Caviar_Dreams_Bold=findViewById(R.id.checkBox_Caviar_Dreams_Bold);
        checkBox_Ostrich=findViewById(R.id.checkBox_Ostrich);
        checkBox_OstrichSans=findViewById(R.id.checkBox_OstrichSans);
        checkBox_Pacifico=findViewById(R.id.checkBox_Pacifico);
        checkBox_PlayfairDisplay_Bold=findViewById(R.id.checkBox_PlayfairDisplay_Bold);
        checkBox_PlayfairDisplay_Italic=findViewById(R.id.checkBox_PlayfairDisplay_Italic);
        checkBox_Respective=findViewById(R.id.checkBox_Respective);

        checkbox_normal.setClickable(false);
        checkBox_monospace.setClickable(false);
        checkbox_sans.setClickable(false);
        checkbox_serif.setClickable(false);
        checkBox_Amatic_Bold.setClickable(false);
        checkBox_Amatic_Regular.setClickable(false);
        checkBox_Capture.setClickable(false);
        checkBox_Caviar_Dreams.setClickable(false);
        checkBox_Caviar_Dreams_Bold.setClickable(false);
        checkBox_Ostrich.setClickable(false);
        checkBox_OstrichSans.setClickable(false);
        checkBox_Pacifico.setClickable(false);
        checkBox_PlayfairDisplay_Bold.setClickable(false);
        checkBox_PlayfairDisplay_Italic.setClickable(false);
        checkBox_Respective.setClickable(false);
        SharedPreferences sharedPreferences = c.getSharedPreferences("TF", Context.MODE_PRIVATE);
         editor= sharedPreferences.edit();
        linearLayout_monospace.setOnClickListener(this);
        linearLayout_normal.setOnClickListener(this);
        linearLayout_sans.setOnClickListener(this);
        linearLayout_serif.setOnClickListener(this);
        linearLayout_Amatic_Bold.setOnClickListener(this);
        linearLayout_Amatic_Regular.setOnClickListener(this);
        linearLayout_Capture.setOnClickListener(this);
        linearLayout_Caviar_Dreams.setOnClickListener(this);
        linearLayout_Caviar_Dreams_Bold.setOnClickListener(this);
        linearLayout_Ostrich.setOnClickListener(this);
                linearLayout_OstrichSans.setOnClickListener(this);
                linearLayout_Pacifico.setOnClickListener(this);
                linearLayout_PlayfairDisplay_Bold.setOnClickListener(this);
                        linearLayout_PlayfairDisplay_Italic.setOnClickListener(this);
                linearLayout_Respective.setOnClickListener(this);

        TextView textview=findViewById(R.id.cursive_tv);
        TextView textview_m=findViewById(R.id.monospace_tv);
        TextView textview_s=findViewById(R.id.serif_tv);
        TextView textView_Amatic_Bold=findViewById(R.id.Amatic_Bold);
        TextView textView_Amatic_Regular=findViewById(R.id.Amatic_Regular);
        TextView textView_capture=findViewById(R.id.Capture);
        TextView textView_Caviar_Dreams=findViewById(R.id.Caviar_Dreams);
        TextView textView_Caviar_Dreams_Bold=findViewById(R.id.Caviar_Dreams_Bold);
        TextView textView_ostrich=findViewById(R.id.Ostrich);
        TextView textView_ostrichSans=findViewById(R.id.OstrichSans);
        TextView textView_Pacifico=findViewById(R.id.cursive_Pacifico);
        TextView textView_Play_bold=findViewById(R.id.PlayfairDisplay_Bold);
        TextView textView_Play_Italic=findViewById(R.id.cursive_PlayfairDisplay_Italic);
        TextView textView_Respective=findViewById(R.id.cursive_Respective);

        textview_s.setTypeface(android.graphics.Typeface.SERIF);
        textview_m.setTypeface(android.graphics.Typeface.MONOSPACE);
        textview.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/dancing-script.regular.ttf"));
        textView_Amatic_Bold.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Amatic-Bold.ttf"));
        textView_Amatic_Regular.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/AmaticSC-Regular.ttf"));
        textView_capture.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Capture_it.ttf"));
        textView_Caviar_Dreams.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/CaviarDreams.ttf"));
        textView_Caviar_Dreams_Bold.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Caviar_Dreams_Bold.ttf"));
        textView_ostrich.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/ostrich-regular.ttf"));
        textView_ostrichSans.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/OstrichSans-Heavy.otf"));
        textView_Pacifico.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Pacifico.ttf"));
        textView_Play_bold.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/PlayfairDisplay-Bold.otf"));
        textView_Play_Italic.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/PlayfairDisplay-Italic.otf"));
        textView_Respective.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Respective_Swashes_Slanted.ttf"));


        if (TextStlye.equals("NORMAL")){
            checkbox_normal.setChecked(true);
        }
        if (TextStlye.equals("MONOSPACE")){
            checkBox_monospace.setChecked(true);
        }
        if (TextStlye.equals("SERIF")){
            checkbox_serif.setChecked(true);
        }
        if (TextStlye.equals("CURSIVE")){
            checkbox_sans.setChecked(true);
        }
        if (TextStlye.equals("Amatic_Bold")){
            checkBox_Amatic_Bold.setChecked(true);
        }if (TextStlye.equals("Amatic_Regular")){
            checkBox_Amatic_Regular.setChecked(true);
        }if (TextStlye.equals("Capture")){
            checkBox_Capture.setChecked(true);
        }if (TextStlye.equals("Caviar_Dreams_Bold")){
            checkBox_Caviar_Dreams_Bold.setChecked(true);
        }if (TextStlye.equals("CaviarDreams")){
            checkBox_Caviar_Dreams.setChecked(true);
        }if (TextStlye.equals("Ostrich")){
            checkBox_Ostrich.setChecked(true);
        }if (TextStlye.equals("OstrichSans")){
            checkBox_OstrichSans.setChecked(true);
        }if (TextStlye.equals("Pacifico")){
            checkBox_Pacifico.setChecked(true);
        }if (TextStlye.equals("PlayfairBold")){
            checkBox_PlayfairDisplay_Bold.setChecked(true);
        }if (TextStlye.equals("PlayfairItalic")){
            checkBox_PlayfairDisplay_Italic.setChecked(true);
        }if (TextStlye.equals("Respective")){
            checkBox_Respective.setChecked(true);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linearlayout_monospace:
                checkBox_monospace.setChecked(true);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkbox_sans.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);
                editor.putString("Typeface","MONOSPACE");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.MONOSPACE);
                t.setTypeface(android.graphics.Typeface.MONOSPACE);
                break;
            case  R.id.linearlayout_normal:
                checkbox_normal.setChecked(true);
                checkbox_serif.setChecked(false);
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);
                editor.putString("Typeface","NORMAL");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.DEFAULT);
                t.setTypeface(android.graphics.Typeface.DEFAULT);
                break;
            case  R.id.linearlayout_serif:
                checkbox_serif.setChecked(true);
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);
                editor.putString("Typeface","SERIF");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.SERIF);
                t.setTypeface(android.graphics.Typeface.SERIF);
                break;

            case  R.id.linearlayout__sans:
                checkbox_sans.setChecked(true);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","CURSIVE");
                editor.apply();
                android.graphics.Typeface typeface= android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/dancing-script.regular.ttf");
                s.setTypeface(typeface);
                t.setTypeface(typeface);

                break;

            case  R.id.linearlayout_Amatic_Bold:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(true);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","Amatic_Bold");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Amatic-Bold.ttf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Amatic-Bold.ttf"));

                break;

            case  R.id.linearlayout_Amatic_Regular:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(true);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","Amatic_Regular");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/AmaticSC-Regular.ttf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/AmaticSC-Regular.ttf"));

                break;
            case  R.id.linearlayout_Capture:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(true);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);
                editor.putString("Typeface","Capture");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Capture_it.ttf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Capture_it.ttf"));

                break;
            case  R.id.linearlayout_Caviar_Dreams_Bold:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(true);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","Caviar_Dreams_Bold");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Caviar_Dreams_Bold.ttf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Caviar_Dreams_Bold.ttf"));

                break;
            case  R.id.linearlayout_Caviar_Dreams:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(true);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","CaviarDreams");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/CaviarDreams.ttf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/CaviarDreams.ttf"));

                break;
            case  R.id.linearlayout_Ostrich:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(true);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","Ostrich");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/ostrich-regular.ttf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/ostrich-regular.ttf"));

                break;
            case  R.id.linearlayout_OstrichSans:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(true);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","OstrichSans");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/OstrichSans-Heavy.otf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/AmaticSC-Regular.ttf"));

                break;
            case  R.id.linearlayout_Pacifico:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(true);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","Pacifico");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Pacifico.ttf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Pacifico.ttf"));

                break;
            case  R.id.linearlayout_PlayfairDisplay_Bold:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(true);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","PlayfairBold");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/PlayfairDisplay-Bold.otf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/PlayfairDisplay-Bold.otf"));

                break;
            case  R.id.linearlayout_PlayfairDisplay_Italic:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(true);
                checkBox_Respective.setChecked(false);

                editor.putString("Typeface","PlayfairItalic");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/PlayfairDisplay-Italic.otf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/PlayfairDisplay-Italic.otf"));

                break;
            case  R.id.linearlayout_Respective:
                checkbox_sans.setChecked(false);
                checkBox_monospace.setChecked(false);
                checkbox_normal.setChecked(false);
                checkbox_serif.setChecked(false);
                checkBox_Amatic_Bold.setChecked(false);
                checkBox_Amatic_Regular.setChecked(false);
                checkBox_Capture.setChecked(false);
                checkBox_Caviar_Dreams.setChecked(false);
                checkBox_Caviar_Dreams_Bold.setChecked(false);
                checkBox_Ostrich.setChecked(false);
                checkBox_OstrichSans.setChecked(false);
                checkBox_Pacifico.setChecked(false);
                checkBox_PlayfairDisplay_Bold.setChecked(false);
                checkBox_PlayfairDisplay_Italic.setChecked(false);
                checkBox_Respective.setChecked(true);

                editor.putString("Typeface","Respective");
                editor.apply();
                s.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Respective_Swashes_Slanted.ttf"));
                t.setTypeface(android.graphics.Typeface.createFromAsset(c.getAssets(),"fonts/Respective_Swashes_Slanted.ttf"));

                break;
        }

    }


}
