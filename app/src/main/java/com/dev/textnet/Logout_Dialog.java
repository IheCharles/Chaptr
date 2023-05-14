package com.dev.textnet;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Dell on 2017/12/07.
 */

public class Logout_Dialog extends Dialog implements android.view.View.OnClickListener{
    public  Activity activity;
    String UserID;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    LinearLayout linearLayout,LinearLayout_Audio;
    TextView textView,textview_l;
    ImageButton Imagebutton,imageButton_l;
    public Logout_Dialog(Activity a, String UserId) {
        super(a);
        this.activity=a;
        UserID=UserId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.log_out);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar=findViewById(R.id.progressBar_viewdialoglog_out);
        progressBar.setVisibility(View.GONE);
        linearLayout=findViewById(R.id.dialoglogout);
        LinearLayout_Audio=findViewById(R.id.dialogsoung);
        textView=findViewById(R.id.textView_sound);
        textview_l=findViewById(R.id.textView5_l);
        imageButton_l=findViewById(R.id.imagebutton_l);
        Imagebutton=findViewById(R.id.logoutf_sound);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    textView.setText("Disable Audio");
                    Imagebutton.setImageResource(R.drawable.ic_volume_off_black_24dp);
                }else{
                    if (dataSnapshot.getValue(Boolean.class)){
                        textView.setText("Disable Audio");
                        Imagebutton.setImageResource(R.drawable.ic_volume_off_black_24dp);
                    }else{
                        textView.setText("Enable Audio");
                        Imagebutton.setImageResource(R.drawable.ic_volume_up_black_24dp);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        linearLayout.setOnClickListener(this);
        LinearLayout_Audio.setOnClickListener(this);
        textView.setOnClickListener(this);
        textview_l.setOnClickListener(this);
        imageButton_l.setOnClickListener(this);
        Imagebutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logoutf_sound:
                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").setValue(false);
                            textView.setText("Enable Audio");
                            Imagebutton.setImageResource(R.drawable.ic_volume_up_black_24dp);
                        }else{
                            if (dataSnapshot.getValue(Boolean.class)){
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").setValue(false);
                                textView.setText("Enable Audio");
                                Imagebutton.setImageResource(R.drawable.ic_volume_up_black_24dp);
                            }else{
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").setValue(true);
                                textView.setText("Disable Audio");
                                Imagebutton.setImageResource(R.drawable.ic_volume_off_black_24dp);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.imagebutton_l:
                mAuth = FirebaseAuth.getInstance();
                progressBar.setVisibility(View.VISIBLE);
                textview_l.setVisibility(View.INVISIBLE);
                imageButton_l.setVisibility(View.INVISIBLE);

                mAuth.signOut();
                break;
            case R.id.textView5_l:
                mAuth = FirebaseAuth.getInstance();
                progressBar.setVisibility(View.VISIBLE);
                textview_l.setVisibility(View.INVISIBLE);
                imageButton_l.setVisibility(View.INVISIBLE);

                mAuth.signOut();
                break;
            case R.id.dialoglogout:
                mAuth = FirebaseAuth.getInstance();
                progressBar.setVisibility(View.VISIBLE);
                textview_l.setVisibility(View.INVISIBLE);
                imageButton_l.setVisibility(View.INVISIBLE);

                mAuth.signOut();
                break;
            case R.id.dialogsoung:
                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").setValue(false);
                            textView.setText("Enable Audio");
                            Imagebutton.setImageResource(R.drawable.ic_volume_up_black_24dp);
                        }else{
                            if (dataSnapshot.getValue(Boolean.class)){
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").setValue(false);
                                textView.setText("Enable Audio");
                                Imagebutton.setImageResource(R.drawable.ic_volume_up_black_24dp);
                            }else{
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").setValue(true);
                                textView.setText("Disable Audio");
                                Imagebutton.setImageResource(R.drawable.ic_volume_off_black_24dp);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case R.id.textView_sound:
                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()){
                            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").setValue(false);
                            textView.setText("Enable Audio");
                            Imagebutton.setImageResource(R.drawable.ic_volume_up_black_24dp);
                        }else{
                            if (dataSnapshot.getValue(Boolean.class)){
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").setValue(false);
                                textView.setText("Enable Audio");
                                Imagebutton.setImageResource(R.drawable.ic_volume_up_black_24dp);
                            }else{
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("AllowAudio").setValue(true);
                                textView.setText("Disable Audio");
                                Imagebutton.setImageResource(R.drawable.ic_volume_off_black_24dp);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();

    }
}
