package com.dev.textnet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import com.dev.textnet.Login.login;
import com.dev.textnet.Userpro.UserActivity;
import com.dev.textnet.posts.Card_view;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import static android.support.v4.graphics.ColorUtils.blendARGB;

public class MainActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    DatabaseReference firebaseDatabase;
    StorageReference mstoragereference;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mcurrentuser;
    Uri imageHoldUri = null;

    public MenuItem prevMenuItem;
    static int resultColor;
    private RecyclerView recyclerView1;
    private HPLinearLayoutManager linearLayoutManager;
    int selectedColor;
    Query query;
    CardView cardView;
    FirebaseAuth.AuthStateListener mAuthListener;
    Boolean Notifiy=false;


    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView=findViewById(R.id.bottomnav);
        SharedPreferences sharedPreferencees=getSharedPreferences("Key",Context.MODE_PRIVATE);
       // Toast.makeText(MainActivity.this,sharedPreferencees.getString("Log","poo" ),Toast.LENGTH_SHORT).show();


        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    mcurrentuser = mAuth.getCurrentUser();
                    firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).keepSynced(true);

                    mViewPager =  findViewById(R.id.container);
                    setupViewPager(mViewPager);
                    final Toolbar toolbar = findViewById(R.id.toolbar_main);
                    setSupportActionBar(toolbar);
                    // getSupportActionBar().setTitle(null);


                    //mstoragereference = FirebaseStorage.getInstance().getReference();
                    AppBarLayout appBarLayout=findViewById(R.id.appbar);
                    SharedPreferences sharedPreferences=getSharedPreferences("Key",Context.MODE_PRIVATE);
                    int  RGB = (sharedPreferences.getInt("TopRGB", 0));
                    appBarLayout.setBackgroundColor(RGB);
                    toolbar.setBackgroundColor(RGB);

                    // CoordinatorLayout.LayoutParams layoutparam=(CoordinatorLayout.LayoutParams)bottomNavigationView.getLayoutParams();
                    //layoutparam.setBehavior(new BottomNavigationViewBehavior());

                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                // Toast.makeText(UserActivity.this,"fa",Toast.LENGTH_SHORT).show();
                                dataSnapshot.getRef().removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    final ImageButton imageButton=findViewById(R.id.cardview2_cardview_bbb);
                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("color").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.getValue(Integer.class)!=null) {
                                selectedColor = dataSnapshot.getValue(Integer.class);


                                int white = Color.BLACK;
                                resultColor = blendARGB(selectedColor, white, 0.5f);
                                toolbar.setBackgroundColor(resultColor);
                                int Trans = Color.TRANSPARENT;
                                int resultColor_2 = blendARGB(selectedColor, white, 0.2f);
                                ColorStateList csl = ColorStateList.valueOf(resultColor_2);
                                bottomNavigationView.setItemIconTintList(csl);
                                bottomNavigationView.setItemTextColor(csl);

                                if (Build.VERSION.SDK_INT >= 21) {
                                    int whit = Color.BLACK;
                                    int resultColor = blendARGB(selectedColor, whit, 0.6f);
                                    Window window = MainActivity.this.getWindow();
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                    window.setStatusBarColor(resultColor);
                                }
                                Drawable drawable2 = imageButton.getDrawable();
                                Drawable md2 = drawable2.mutate();
                                md2 = DrawableCompat.wrap(md2);
                                DrawableCompat.setTint(md2, selectedColor);
                                DrawableCompat.setTintMode(md2, PorterDuff.Mode.SRC_IN);

                                imageButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(MainActivity.this, All_followers.class);
                                        intent.putExtra("Color", selectedColor);
                                        startActivity(intent);
                                    }
                                });


                                AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
                                appBarLayout.setBackgroundColor(resultColor);
                                SharedPreferences sharedPreferences = getSharedPreferences("Key", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("TopRGB", resultColor);
                                editor.apply();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Notifications").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (mViewPager.getCurrentItem()!=2) {
                                Notifiy = true;
                                timer1();
                            }

                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        }
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    DatabaseReference storageReference= firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("imageurl");
                    storageReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String imageUrl = dataSnapshot.getValue(String.class);

                            final ImageView propic = (ImageView) findViewById(R.id.imageview_propicicon);

                            Glide.with(getApplicationContext()).load(imageUrl).into(propic);
               /* Picasso.with(getApplicationContext()).load(imageUrl).into(propic, new Callback() {


                    @Override
                    public void onSuccess() {
                        boolean hasDrawable = (propic.getDrawable() != null);
                        if (hasDrawable) {
                            BitmapDrawable drawable = (BitmapDrawable) propic.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            int selectedColor = calculateAverageColor(bitmap, 1);
                            Toast.makeText(MainActivity.this,"ppp",Toast.LENGTH_SHORT).show();
                            firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("color").setValue(selectedColor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MainActivity.this,"ppp",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });*/
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });


                }else{
                    // User is signed out
                    final ImageView propic = findViewById(R.id.imageview_propicicon);
                    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
                    propic.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_person_black_24dp_g,null));
                    mViewPager =  findViewById(R.id.container);
                    setupViewPager(mViewPager);
                    final Toolbar toolbar = findViewById(R.id.toolbar_main);
                    setSupportActionBar(toolbar);
                    if (Build.VERSION.SDK_INT>=21) {
                        Window window = MainActivity.this.getWindow();
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.Pale_Yellow,null));
                    }
                    toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.Pale_Orange,null));
                    ColorStateList csl = ColorStateList.valueOf(ResourcesCompat.getColor(getResources(),R.color.Pale_Orange,null));
                    bottomNavigationView.setItemIconTintList(csl);
                    bottomNavigationView.setItemTextColor(csl);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*if (firebaseDatabase.child(mAuth.getCurrentUser().getUid())) {
          LogedIn=false;
        }else{
            LogedIn=true;
        }*/



        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");


        recyclerView1 =  findViewById(R.id.    card_view2_circle);



        linearLayoutManager =(new HPLinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));

        linearLayoutManager.setStackFromEnd(true);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(linearLayoutManager);


        final ImageButton imageButton=findViewById(R.id.cardview2_cardview_bbb);
       //  query= firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow");




        final ImageView propic = (ImageView) findViewById(R.id.imageview_propicicon);




      /*  mauthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    final String UserId = user.getUid();
                    firebaseDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(UserId)) {
                            } else {
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    Intent intent = new Intent(MainActivity.this, login.class);
                    startActivity(intent);
                }
            }
        };


        mAuth.addAuthStateListener(mauthStateListener);*/




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.following:
                      firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                              if (dataSnapshot.exists()){
                                  mViewPager.setCurrentItem(0);
                              }
                          }

                          @Override
                          public void onCancelled(DatabaseError databaseError) {

                          }
                      });


                        break;
                    case R.id.home:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.notifyee:
                        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    mViewPager.setCurrentItem(2);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        break;
                }
                return false;
            }
        });


        /*firebaseDatabase.child(mAuth.getCurrentUser().getUid()).keepSynced(true);


        CoordinatorLayout.LayoutParams layoutparam=(CoordinatorLayout.LayoutParams)bottomNavigationView.getLayoutParams();
        layoutparam.setBehavior(new BottomNavigationViewBehavior());

        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   // Toast.makeText(UserActivity.this,"fa",Toast.LENGTH_SHORT).show();
                    dataSnapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/

        propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User is signed in
                            int jimmy=Color.BLACK;
                            resultColor =  blendARGB(selectedColor,jimmy,0.4f);
                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            intent.putExtra("RGB",resultColor);
                            startActivity(intent);
                            //finish();

                        } else {
                            // User is signed out
                            // Toast.makeText(MainActivity.this,"log",Toast.LENGTH_SHORT).show();

                            //propic.setImageResource(R.drawable.ic_action_name);
                            Intent intent = new Intent(MainActivity.this, login.class);
                            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


     //  Query update= mDatabase.orderByChild("UserId").equalTo(mcurrentuser.getUid());
// Set up the ViewPager with the sections adapter.





        /*firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Notifications").orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int scat = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        scat = scat + 1;
                            //snapshot.getRef().removeValue();
                        if (snapshot.child("Seen").exists()) {
                            if (!snapshot.child("Seen").getValue(Boolean.class)) {
                                Notifiy = true;
                                timer1();
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/




        RelativeLayout relativeLayout=findViewById(R.id.relativeview_main);
        relativeLayout.setVisibility(View.GONE);


      /*firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){
                    firebaseDatabase.child(snapshot.getKey()).child("follow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snpsht : dataSnapshot.getChildren()){
                                dataSnapshot.getRef().child(snpsht.getKey()).child("New").setValue(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                Toast.makeText(MainActivity.this,"ddd",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


    /*firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                snapshot.getRef().removeValue();
            }
            Toast.makeText(MainActivity.this,"ddd",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("feed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

       /*firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follower").addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               if (dataSnapshot.child("listener").exists()){
                   LayoutInflater inflater = getLayoutInflater();
                   View view = inflater.inflate(R.layout.plus_one,
                           (ViewGroup) findViewById(R.id.relativeLayout_plus_one));
                   int blende = blendARGB(selectedColor,Color.TRANSPARENT,0.5f);
                   ImageButton like=view.findViewById(R.id.relativeLayout_plus_one);
                   Drawable drawablelove = like.getDrawable();
                   Drawable md1 = drawablelove.mutate();
                   md1 = DrawableCompat.wrap(md1);
                   DrawableCompat.setTint(md1, blende);
                   DrawableCompat.setTintMode(md1, PorterDuff.Mode.SRC_IN);
                   Toast toast = new Toast(MainActivity.this);
                   toast.setView(view);
                   toast.setDuration(Toast.LENGTH_SHORT);
                   toast.show();
               }
           }
           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {
           }
           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {
           }
           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {
           }
           @Override
           public void onCancelled(DatabaseError databaseError) {
           }
       });*/

        mViewPager =  findViewById(R.id.container);
        setupViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position==1){

                    RelativeLayout relativeLayout=findViewById(R.id.relativeview_main);
                    relativeLayout.setVisibility(View.GONE);
                    bottomNavigationView.getMenu().getItem(position).setIcon(R.drawable.ic_home_black_24dp);
                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_people_black_24dp2);
                    if (!Notifiy) {
                        bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_notifications_none_black_24dp);
                    }                }
                if (position==0){
                   // RelativeLayout relativeLayout=findViewById(R.id.relativeview_main);
                    //relativeLayout.setVisibility(View.GONE);
                    bottomNavigationView.getMenu().getItem(position).setIcon(R.drawable.ic_people_black_24dp);
                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                    bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_home_black_24dp2);
                    if (!Notifiy) {
                        bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_notifications_none_black_24dp);
                    }
                }
                if (position==2 ){

                        RelativeLayout relativeLayout = findViewById(R.id.relativeview_main);
                        relativeLayout.setVisibility(View.GONE);
                        Notifiy = false;
                        bottomNavigationView.getMenu().getItem(position).setIcon(R.drawable.ic_notifications_black_24dp);
                        bottomNavigationView.getMenu().getItem(position).setChecked(true);
                        bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_people_black_24dp2);
                        bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_home_black_24dp2);
                        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Notifications").orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.child("Seen").exists()) {
                                        if (!snapshot.child("Seen").getValue(Boolean.class)) {

                                            firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Notifications").child(snapshot.getKey()).child("Seen").setValue(true);
                                        } else {
                                            snapshot.getRef().removeValue();
                                        }
                                    } else {
                                        snapshot.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                }else{

                       // Toast.makeText(MainActivity.this, "You need to login.", Toast.LENGTH_SHORT).show();

                }


                //prevMenuItem=bottomNavigationView.getMenu().getItem(position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }


    private void timer1() {
        new CountDownTimer(600,600){
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_notifications_active_black_24dp);

                if (Notifiy) {
                    timer2();
                }else{
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_notifications_black_24dp);
                }
            }
        }.start();
    }

    private void timer2() {
        new CountDownTimer(600,600){
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_notifications_none_black_24dp);
                if (Notifiy) {
                    timer1();
                }else {
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_notifications_black_24dp);
                }
            }
        }.start();
    }


    private void setupViewPager(ViewPager viewPager){

        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        Fragment follwoer=new Card_view2();
        Fragment home=new Card_view();
        Fragment notify=new Notification_recyclerview();
        adapter.addFragment(follwoer, "Subscribed");
        adapter.addFragment(home, "New");
        adapter.addFragment(notify, "Notifications");
        viewPager.setAdapter(adapter);
viewPager.setCurrentItem(1);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

            final MenuItem searchItem = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            // Configure the search info and add any event listeners
            searchView.setQueryHint("Username");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Toast like print
                    //Toast.makeText(MainActivity.this,query,Toast.LENGTH_SHORT).show();
                    if (!searchView.isIconified()) {
                        searchView.setIconified(true);
                    }

                    Intent intent = new Intent(MainActivity.this, SearchResult.class);
                    intent.putExtra("Query", query.trim());
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                    return false;
                }
            });

            return super.onCreateOptionsMenu(menu);

    }

    public int calculateAverageColor(android.graphics.Bitmap bitmap, int pixelSpacing) {
        int R = 0; int G = 0; int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i += pixelSpacing) {
            int color = pixels[i];
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return Color.rgb(R / n, G / n, B / n);
    }


    public static int getComplimentColor(int color) {
        // get existing colors
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);

        // find compliments
        red = (~red) & 0xff;
        blue = (~blue) & 0xff;
        green = (~green) & 0xff;

        return Color.argb(alpha, red, green, blue);
    }


    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // if you find this you can go fuck yourself/im in nigeria motherfucker
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
    }

   private static  String IntegerShortner(int integer,long Long){
       if (integer>0){
           if (integer>999 && integer<10000){
               return  Float.valueOf((float) integer /1000).toString().substring(0,3)+"K";
           }else  if (integer>9999 && integer<100000){
               return (Float.valueOf((float) integer /1000).toString().substring(0,4)+"K");
           }else if (integer>99999 && integer<1000000){
               return (Float.valueOf((float) integer /1000).toString().substring(0,5)+"K");
           }else if (integer>999999 && integer<10000000 || integer>100000000){
               return (Float.valueOf((float) integer /1000000).toString().substring(0,3)+"M");
           }else if (integer>9999999 && integer<100000000){
               return (Float.valueOf((float) integer /1000000).toString().substring(0,4)+"M");
           }else {
               return String.valueOf(integer);
           }
       }else {
           if (Long>999 && Long<10000){
               return  Float.valueOf((float) Long /1000).toString().substring(0,3)+"K";
           }else  if (Long>9999 && Long<100000){
               return (Float.valueOf((float) Long /1000).toString().substring(0,4)+"K");
           }else if (Long>99999 && Long<1000000){
               return (Float.valueOf((float) Long /1000).toString().substring(0,5)+"K");
           }else if (Long>999999 && Long<10000000 || Long>100000000){
               return (Float.valueOf((float) Long /1000000).toString().substring(0,3)+"M");
           }else if (Long>9999999 && Long<100000000){
               return (Float.valueOf((float) Long /1000000).toString().substring(0,4)+"M");
           }else {
               return String.valueOf(Long);
           }
       }
   }


}


