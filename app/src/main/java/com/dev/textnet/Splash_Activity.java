package com.dev.textnet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Dell on 2017/12/24.
 */

public class Splash_Activity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       // Toast.makeText(Splash_Activity.this,String.valueOf(Color.YELLOW),Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = Splash_Activity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        }
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                   new CountDownTimer(1000,1000){
                        @Override
                        public void onTick(long l) {
                        }
                        @Override
                        public void onFinish() {
                            //Toast.makeText(Splash_Activity.this,"logged in",Toast.LENGTH_SHORT).show();
                            //ca-app-pub-7130924913011088~6703299876
                            //ca-app-pub-7130924913011088/7508636150
                            MobileAds.initialize(Splash_Activity.this, "ca-app-pub-7130924913011088~6703299876");
                            Intent intent= new Intent(Splash_Activity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }.start();
                   //startActivity(new Intent(Splash_Activity.this,MainActivity.class));

                } else {
                    // User is signed out
                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            mAuth.signInAnonymously()
                                    .addOnCompleteListener(Splash_Activity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // Log.d(TAG, "OnComplete : " +task.isSuccessful());
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();



                                                MobileAds.initialize(Splash_Activity.this, "ca-app-pub-7130924913011088~6703299876");
                                                // Toast.makeText(Splash_Activity.this,"logged out",Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Splash_Activity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }


                                        }
                                    });
                        }

                    }.start();
                }




            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
