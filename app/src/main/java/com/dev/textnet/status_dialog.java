package com.dev.textnet;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Dell on 2018/01/12.
 */

public class status_dialog extends Dialog implements android.view.View.OnClickListener {
    DatabaseReference firebaseDatabase;
    FirebaseAuth mAuth;
     ProgressBar progressBar;
    String UserId;
    Activity c;
    TextView textView1;
    int selectcolor;
     EditText sd;
     TextView textView;
    public status_dialog(Activity activity,String user,int color) {
        super(activity);
        selectcolor=color;
        UserId=user;
        c=activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.status);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
         sd =  findViewById(R.id.millhouse);

        progressBar=findViewById(R.id.progressBar3_status);
        progressBar.setVisibility(View.GONE);

        final CardView cardView=(CardView)findViewById(R.id.bark);
        cardView.setCardBackgroundColor(selectcolor);


        textView=findViewById(R.id.save);
      int contr=  getContrastColor(selectcolor);


         textView1=findViewById(R.id.cancel);
        textView1.setOnClickListener(this);
        textView.setOnClickListener(this);
        textView.setTextColor(contr);
        textView1.setTextColor(contr);
        sd.setTextColor(contr);
        DatabaseReference storageReferenc = firebaseDatabase.child(UserId).child("status");
        //shots poping up the Ar (p-p-p-POP NIGGA!!!!!!)
        storageReferenc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ugh = dataSnapshot.getValue(String.class);
                sd.setText(ugh);
                sd.setSelection(sd.getText().length());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.cancel:
                sd.getText().clear();
                dismiss();
                break;
            case R.id.save:
                if (!TextUtils.isEmpty(sd.getText())) {
                    sd.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);

                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("status").setValue(sd.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //bitch if there's a problem we gonna gumball

                            if (task.isSuccessful()) {
                                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot) {
                                        dataSnapshot.child("follower").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                                if (dataSnapshot2.exists()) {
                                                    long count = 0;
                                                    long childcount = dataSnapshot2.getChildrenCount();
                                                    for (DataSnapshot snapshot : dataSnapshot2.getChildren()) {
                                                        DatabaseReference notify = firebaseDatabase.child(snapshot.getKey()).child("Notifications").push();
                                                        notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                        notify.child("UserId").setValue(null);
                                                        notify.child("action").setValue(dataSnapshot.child("username").getValue(String.class) + "'s status was updated.");
                                                        notify.child("note").setValue("");
                                                        notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                                                        notify.child("imageurl").setValue(dataSnapshot.child("imageurl").getValue(String.class));
                                                        notify.child("Seen").setValue(false);
                                                        notify.child("Type").setValue("UserActivity");
                                                        count = count + 1;
                                                        if (count == childcount) {
                                                            TextView showedittext = c.findViewById(R.id.statuc_useractivity);
                                                            showedittext.setText(sd.getText().toString().trim());
                                                            dismiss();
                                                        }
                                                    }
                                                } else {
                                                    TextView showedittext = c.findViewById(R.id.statuc_useractivity);
                                                    showedittext.setText(sd.getText().toString().trim());
                                                    dismiss();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                            }
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    sd.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                    textView.setVisibility(View.VISIBLE);
                                    textView1.setVisibility(View.VISIBLE);
                                    Toast.makeText(c, "failed to update status", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }else{
                    Toast.makeText(c,":(",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
    }

}
