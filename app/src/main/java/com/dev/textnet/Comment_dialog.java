package com.dev.textnet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by Dell on 2018/01/08.
 */

public class Comment_dialog extends Dialog implements android.view.View.OnClickListener {
    private ImageView circleimage;
    private EditText editText;
    FirebaseAuth mAuth;
    private String Name;
    DatabaseReference firebaseDatabase;
    private DatabaseReference databaseReference;
    private CardView cardview_com;
    private String ImageUrl;
    private ProgressBar progressBar;
    Activity c;
    private ImageButton imageButton_com;
    private  String PostKey;
    private TextView textView;
    private TextView textView_limit;
    private String tit;

    public Comment_dialog(Activity a,String name,String Image,String PK,String title) {
        super(a);
        this.c=a;
        PostKey=PK;
        Name=name;
        ImageUrl=Image;
        tit=title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.share);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().goOnline();
        InputMethodManager imm= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput( InputMethodManager.SHOW_FORCED,0);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Posts");
        firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        circleimage=findViewById(R.id.share_imagecircle);
        editText=findViewById(R.id.share_edittext);
        textView = findViewById(R.id.share_action);
        cardview_com=findViewById(R.id.share_cardviewpost);
        textView_limit=findViewById(R.id.textView_share_limit);
        progressBar=findViewById(R.id.progressBar_share);
        imageButton_com=findViewById(R.id.share_imagebuttonpost);
        imageButton_com.setOnClickListener(this);
        cardview_com.setOnClickListener(this);
        progressBar.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT>=21) {
            textView_limit.setPadding(0, 0, 10, 0);
        }
        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("color").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int blackmix = dataSnapshot.getValue(Integer.class);
                Drawable drawable =imageButton_com.getDrawable();
                Drawable md = drawable.mutate();
                md = DrawableCompat.wrap(md);
                DrawableCompat.setTint(md, blackmix);
                DrawableCompat.setTintMode(md, PorterDuff.Mode.SRC_IN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(blackmix));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    textView_limit.setText(String.valueOf(editText.length()));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textView.setText(Name+ " commented on"+" '" +tit+"'");

        Glide.with(c)
                .load(ImageUrl)
                .into(circleimage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share_cardviewpost:

                    imageButton_com.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth = FirebaseAuth.getInstance();
                    databaseReference.child(PostKey).child("UserId").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userids=dataSnapshot.getValue(String.class);

                            DatabaseReference notify = firebaseDatabase.child(userids).child("Notifications").push();
                            notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                            notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                            notify.child("action").setValue(textView.getText().toString());
                            notify.child("note").setValue(editText.getText().toString());
                            notify.child("Type").setValue("ReplyPopup");
                            notify.child("Name").setValue(Name);
                            notify.child("Seen").setValue(false);
                            notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                            notify.child("imageurl").setValue(ImageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    InputMethodManager imm= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput( InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                                   dismiss();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                break;
            case  R.id.share_imagebuttonpost:

                    imageButton_com.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth = FirebaseAuth.getInstance();
                    databaseReference.child(PostKey).child("UserId").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userids=dataSnapshot.getValue(String.class);
                            final Calendar calendar = Calendar.getInstance();

                            DatabaseReference notify = firebaseDatabase.child(userids).child("Notifications").push();
                            notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                            notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                            notify.child("action").setValue(textView.getText());
                            notify.child("note").setValue(editText.getText().toString());
                            notify.child("Type").setValue("ReplyPopup");
                            notify.child("key").setValue(PostKey);
                            notify.child("Seen").setValue(false);
                            notify.child("time").setValue(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) +"/"+ calendar.get(Calendar.YEAR));
                            notify.child("imageurl").setValue(ImageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    InputMethodManager imm= (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput( InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                                    //FirebaseDatabase.getInstance().goOffline();
                                    dismiss();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                break;
        }
    }


}
