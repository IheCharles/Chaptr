package com.example.textnet.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textnet.MainActivity;
import com.example.textnet.R;
import com.example.textnet.Userpro.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Dell on 2017/06/23.
 */

public class register extends AppCompatActivity {


    EditText userregisterEmail, userregisterpassowrd;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mauthStateListener;
    DatabaseReference mdatabase;
    ProgressDialog mprogressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mdatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        userregisterEmail= (EditText)findViewById(R.id.registeremail);
        userregisterpassowrd = (EditText)findViewById(R.id.registerpassword);

        mAuth= FirebaseAuth.getInstance();
        mauthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {




            }
        };

        TextView textView=  (TextView)findViewById(R.id.registertext);

        mprogressDialog= new ProgressDialog(this);

        mAuth.addAuthStateListener(mauthStateListener);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail= userregisterEmail.getText().toString().trim();
                String userpass= userregisterpassowrd.getText().toString().trim();

                if (!TextUtils.isEmpty(useremail) && !TextUtils.isEmpty(userpass)) {
                    createUserAccount();
                    mprogressDialog.setTitle("Creating Account.....");
                    mprogressDialog.show();
                }else{
                    Toast.makeText(register.this, "You didn't fill in all the fields", Toast.LENGTH_SHORT).show();
                }


            }

        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mauthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mauthStateListener);
    }

    private void createUserAccount() {


       String useremail= userregisterEmail.getText().toString().trim();
        String userpass= userregisterpassowrd.getText().toString().trim();

        if (!TextUtils.isEmpty(useremail) && !TextUtils.isEmpty(userpass)){

            mAuth.createUserWithEmailAndPassword(useremail,userpass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(register.this, "Account Created", Toast.LENGTH_SHORT).show();
                        mprogressDialog.dismiss();
                        Intent intent= new Intent(register.this,UserProfile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(register.this, "Account Creation failed", Toast.LENGTH_SHORT).show();
                        mprogressDialog.dismiss();
                    }

                }
            });
        }else{
            Toast.makeText(register.this, "You didn't fill in all the fields", Toast.LENGTH_SHORT).show();
        }
    }


    public void onBackPressed() {
       Intent intent= new Intent(register.this,login.class);
       startActivity(intent);
       finish();
    }
}
