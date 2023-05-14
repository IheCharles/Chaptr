package com.dev.textnet;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Dell on 2017/11/29.
 */

public class ViewDialog extends  Dialog implements android.view.View.OnClickListener  {
  public LinearLayout linearLayoutedit;
    LinearLayout linearLayoutdelete;
   public  Activity c;
    String PK;
    FirebaseAuth mAuth;
    DatabaseReference connectedRef;
    ValueEventListener valueEventListener;
    ProgressBar progressbar;
    FirebaseRecyclerAdapter<Cards, useractivity_recyclerview.Postviewholder> firebaseRecyclerAdapter;

    public ViewDialog(Activity a, String postkey, FirebaseRecyclerAdapter<Cards, useractivity_recyclerview.Postviewholder> firebase) {
        super(a);
        this.c=a;
        PK=postkey;
        firebaseRecyclerAdapter=firebase;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.viewholderpopup);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        linearLayoutedit=findViewById(R.id.dialogedit);
        progressbar=findViewById(R.id.progressBar_viewdialo);
      progressbar.setVisibility(View.GONE);
        linearLayoutdelete=findViewById(R.id.linearlayout_del);
        linearLayoutedit.setOnClickListener(this);
        linearLayoutdelete.setOnClickListener(this);
        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialogedit:
                Intent intent = new Intent(c, Typing.class);
                intent.putExtra("PostKey",PK);
                dismiss();
                c.startActivity(intent);
                break;
            case  R.id.linearlayout_del:


                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        linearLayoutdelete.setVisibility(View.INVISIBLE);
                        linearLayoutedit.setVisibility(View.INVISIBLE);
                        progressbar.setVisibility(View.VISIBLE);
                        valueEventListener=connectedRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean connected = dataSnapshot.getValue(Boolean.class);
                                if (connected) {


                                    mAuth = FirebaseAuth.getInstance();
                                   // FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("MyPosts").child(PK).removeValue();
                                    FirebaseDatabase.getInstance().getReference().child("Posts").child(PK).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {

                                            FirebaseStorage mstoragereference= FirebaseStorage.getInstance().getReference().getStorage();
                                            StorageReference photoRef = mstoragereference.getReferenceFromUrl(dataSnapshot.child("image").getValue(String.class));
                                            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    FirebaseDatabase.getInstance().getReference().child("Posts").child(PK).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("PostCount").runTransaction(new Transaction.Handler() {
                                                                @Override
                                                                public Transaction.Result doTransaction(MutableData mutableData) {
                                                                    if (mutableData.getValue() == null) {
                                                                        mutableData.setValue(0);
                                                                    } else {
                                                                        int count = mutableData.getValue(Integer.class);
                                                                        mutableData.setValue(count - 1);
                                                                    }
                                                                    return Transaction.success(mutableData);
                                                                }
                                                                @Override
                                                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                                                }
                                                            });
                                                            firebaseRecyclerAdapter.notifyDataSetChanged();
                                                            dismiss();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }else{
                                    new CountDownTimer(30000,1000){
                                        @Override
                                        public void onTick(long l) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            // Toast.makeText(Splash_Activity.this,"logged out",Toast.LENGTH_SHORT).show();
                                            Toast.makeText(c,"no network connection",Toast.LENGTH_SHORT).show();
                                            dismiss();


                                        }
                                    }.start();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                final AlertDialog alert11 = builder.create();
                alert11.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                        alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                    }
                });
                alert11.show();
                break;
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (valueEventListener!=null) {
            connectedRef.removeEventListener(valueEventListener);
        }
    }
}
