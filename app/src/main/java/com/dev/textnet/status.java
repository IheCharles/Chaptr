package com.dev.textnet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.textnet.Userpro.UserActivity;
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
 * Created by Dell on 2017/08/29.
 */

public class status extends Activity {
    DatabaseReference firebaseDatabase;
    FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.status);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        final EditText sd = (EditText) findViewById(R.id.millhouse);

        final String PostKey = getIntent().getExtras().getString("Userprof");
        final ProgressBar progressBar=findViewById(R.id.progressBar3_status);
        progressBar.setVisibility(View.GONE);

        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.reltivestat);
        final int bed = getIntent().getExtras().getInt("inhuman");
        final TextView textView=(TextView)findViewById(R.id.save);

        final TextView textView1=(TextView)findViewById(R.id.cancel);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FirebaseDatabase.getInstance().goOffline();
                onBackPressed();

            }
        });




        final byte[] bitma=getIntent().getByteArrayExtra("bit");
        final Bitmap bitmap= BitmapFactory.decodeByteArray(bitma,0,bitma.length);




        final CardView cardView=(CardView)findViewById(R.id.bark);
        cardView.setCardBackgroundColor(bed);



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sd.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);

                firebaseDatabase.child(PostKey).child("status").setValue(sd.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //bitch if there's a problem we gonna gumball

                        if (task.isSuccessful()){
                            firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    dataSnapshot.child("follower").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot2) {
                                            long count=0;
                                            long childcount=dataSnapshot2.getChildrenCount();
                                            for (DataSnapshot snapshot :dataSnapshot2.getChildren()){
                                                DatabaseReference notify= firebaseDatabase.child(snapshot.getKey()).child("Notifications").push();
                                                notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                notify.child("UserId").setValue(null);
                                                notify.child("action").setValue(dataSnapshot.child("username").getValue(String.class) + "'s status was updated.");
                                                notify.child("note").setValue(null);
                                                notify.child("key").setValue(PostKey);
                                                notify.child("imageurl").setValue(dataSnapshot.child("imageurl").getValue(String.class));
                                                notify.child("Seen").setValue(false);
                                                notify.child("Type").setValue("UserActivity");
                                                count=count+1;
                                                if (count==childcount){
                                                    bitmap.recycle();
                                                    FirebaseDatabase.getInstance().goOffline();
                                                    Intent intent =new Intent(status.this,UserActivity.class);
                                                    startActivity(intent);
                                                    if (Build.VERSION.SDK_INT>=21) {
                                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                    }
                                                }
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
                               Toast.makeText(status.this,"failed to update status",Toast.LENGTH_SHORT).show();
                           }
                       });
                    }
                });

            }
        });


        DatabaseReference storageReferenc = firebaseDatabase.child(PostKey).child("status");
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



}
