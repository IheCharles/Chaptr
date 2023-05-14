package com.dev.textnet.Login;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.textnet.Progressing;

import com.dev.textnet.Splash_Activity;
import com.dev.textnet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



/**
 * Created by Dell on 2017/06/22.
 */

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email,password;
    DatabaseReference mdatabase;
    ProgressDialog mprogressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth= FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
    //    mAuth = FirebaseAuth.getInstance();


        if (Build.VERSION.SDK_INT>=21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.Colo,null));
        }

       /* mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in

                    Intent intent= new Intent(login.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {

                    // User is signed out
                    Log.d("log out", "onAuthStateChanged:signed_out");
                }



            }
        };*/
       // mAuth.addAuthStateListener(mAuthListener);

      TextView textView=  (TextView)findViewById(R.id.logintext);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Progressing alert=new Progressing(login.this);
                alert.show();
                alert.textView1.setText("Please wait...");

                final String EMAIL= email.getText().toString().trim();
               final  String PASSWORD =password.getText().toString().trim();

                DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            if (!EMAIL.equals("") && !password.equals("")) {

                                mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {

                                            if (login.this.isFinishing()) {
                                                alert.dismiss();
                                            }
                                          //restartApp();
                                            finishAffinity();
                                            restartApp();
                                            Intent intent= new Intent(login.this,Splash_Activity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("DontLog","F");
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                            if (!login.this.isFinishing()) {
                                                alert.dismiss();
                                            }

                                        }

                                    }
                                });
                                mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                        if (!login.this.isFinishing()) {
                                            alert.dismiss();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(login.this, "You didn't fill in all the fields", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            System.out.println("not connected");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Listener was cancelled");
                    }
                });


            }



        });

        Button button= findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(login.this,register.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), Splash_Activity.class);
        int mPendingIntentId = 1;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

}
