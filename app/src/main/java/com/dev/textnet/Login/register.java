package com.dev.textnet.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.textnet.R;
import com.dev.textnet.Userpro.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Dell on 2017/06/23.
 */

public class register extends AppCompatActivity {


    EditText userregisterEmail, userregisterpassowrd;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mauthStateListener;
    DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        if (Build.VERSION.SDK_INT>=21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.Colo,null));
        }

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



        mAuth.addAuthStateListener(mauthStateListener);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail= userregisterEmail.getText().toString().trim();
                String userpass= userregisterpassowrd.getText().toString().trim();

                if (!TextUtils.isEmpty(useremail) && !TextUtils.isEmpty(userpass)) {
                    createUserAccount();
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
            if (userpass.length()>=7) {
                // Toast.makeText(register.this, "Account Created", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(register.this, UserProfile.class);
                intent.putExtra("Email", useremail);
                intent.putExtra("Pass", userpass);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(register.this, "Password must have more than seven characters", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(register.this, "Incomplete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(register.this,login.class);
        startActivity(intent);
    }
}
