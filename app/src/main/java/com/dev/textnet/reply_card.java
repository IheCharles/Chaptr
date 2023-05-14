package com.dev.textnet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

/**
 * Created by Dell on 2018/01/08.
 */

public class reply_card extends Dialog implements android.view.View.OnClickListener {
    private ImageView circleimage;
    private EditText editText;
    FirebaseAuth mAuth;
    private String Name;

    private CardView cardview_com;
    private String ImageUrl;
    private ProgressBar progressBar;

    int scrolly;
    private ImageButton imageButton_com;
    private  String PostKey;
    private TextView textView;
    private TextView textView2;
    DatabaseReference firebaseDatabase;
    private String UserId;
    Activity a;
    private DatabaseReference databaseReference;
    private  String Commenters_name;
    private String Imageurl_Commented;
    private String Note;
    private String action;
        String p;


    public reply_card(String pk,FragmentActivity activity, String userId, String username, String imageurl, String imageurl1, String name, String note,String ac) {
        super(activity);
        UserId=userId;
        Name=username;
        a=activity;
        p=pk;
        ImageUrl=imageurl1;
        Imageurl_Commented=imageurl;
        Commenters_name=name;
        Note=note;
        action=ac;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.reply_card);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Posts");
        firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        circleimage=findViewById(R.id.RC_imageView);
        imageButton_com=findViewById(R.id.RC_imagebutton);
        textView=findViewById(R.id.RC_textView1);
        textView2=findViewById(R.id.RC_textView2);
        editText=findViewById(R.id.RC_edittext);
        imageButton_com.setOnClickListener(this);
        textView.setText(action);
        textView2.setText(Note);
        progressBar=findViewById(R.id.progressBar_sharehelp);
        progressBar.setVisibility(View.INVISIBLE);
        textView2.setMovementMethod(new ScrollingMovementMethod());

        Glide.with(a).load(ImageUrl).into(circleimage);
        KeyboardVisibilityEvent.setEventListener(
                a,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                       if (!isOpen){
                           textView2.setText(Note);
                           textView2.scrollTo(0,scrolly);
                       }else{
                           scrolly=textView2.getVerticalScrollbarPosition();
                           if (Note.length() > 150) {
                               textView2.scrollTo(0,0);
                               String d = Note.substring(0, 150) + "...";
                               textView2.setText(d);
                           }
                       }
                    }
                });


        circleimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2= new Intent(a,UserActivity_3.class);
                intent2.putExtra("Userprofil",UserId);
                intent2.putExtra("Activity","U3");
                a.startActivity(intent2);

            }
        });



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


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.RC_imagebutton:
                if (!TextUtils.isEmpty(editText.getText())) {
                    imageButton_com.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    DatabaseReference notify = firebaseDatabase.child(UserId).child("Notifications").push();
                    notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                    notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                    notify.child("action").setValue(Name + " Replied to " + "'" + Note + "..." + "'");
                    notify.child("note").setValue(editText.getText().toString());
                    notify.child("Type").setValue("ReplyPopup");
                    notify.child("Seen").setValue(false);
                    notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                    notify.child("imageurl").setValue(Imageurl_Commented).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Notifications").child(p).removeValue();
                            dismiss();
                        }
                    });
                }else{
                    Toast.makeText(a,":(",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
