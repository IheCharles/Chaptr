package com.example.textnet;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.example.textnet.Login.login;
import com.example.textnet.Userpro.UserActivity;
import com.example.textnet.Userpro.UserProfile;
import com.example.textnet.posts.Card_view;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private SectionPageAdapter mSectionsPageAdapter;
private ViewPager mViewPager;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mauthStateListener;
    DatabaseReference mdatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager= (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        // Set up the ViewPager with the sections adapter.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        mAuth= FirebaseAuth.getInstance();
        mauthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user= firebaseAuth.getCurrentUser();

                if (user != null){
                    final String UserId= user.getUid();

                    mdatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(UserId)){




                            }else{


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{

                    Intent intent= new Intent(MainActivity.this,login.class);
                    startActivity(intent);
                }


            }
        };


        mAuth.addAuthStateListener(mauthStateListener);
        mdatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

        ImageView propic = (ImageView)findViewById(R.id.imageview_propicicon);
        propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
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

    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter adapter= new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Card_view(),"New" );
        adapter.addFragment(new Card_view2(), "Subscribed");
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}
