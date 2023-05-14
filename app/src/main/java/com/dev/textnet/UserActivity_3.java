package com.dev.textnet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;

import static android.support.v4.graphics.ColorUtils.blendARGB;

/**
 * Created by Dell on 2017/08/22.
 */

public class UserActivity_3 extends AppCompatActivity {
    FirebaseUser mcurrentuser;                                                                   /*   :(    */
    static  int count=2;
    static Query filter;
    static DatabaseReference databaseReference;
    static DatabaseReference reference;
    DatabaseReference firebaseDatabase;
    private RecyclerView recyclerView1;                         /*       :)        */
    static  int TotalItemCount2;
    static   int TotalItemCount3;
     FirebaseRecyclerAdapter<CircleImage,UserActivity_3.Circleviewholder> firebaseRecyclerAdapter1;
    int TotalItemCount;
    static DatabaseReference follow;
    Boolean thresh= true;
    int selectedColor;
    static String ID;
    String UserId;
    FirebaseAuth mAuth;
    private Boolean out=true;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ViewPager mViewPager;
    String PostKey;


    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useractivity_2);
        mAuth = FirebaseAuth.getInstance();
        final Toolbar toolbar =  findViewById(R.id.toolbar_UserActivity2);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences psharedPreferences=getSharedPreferences("param",MODE_PRIVATE);
                SharedPreferences.Editor peditor=psharedPreferences.edit();
                peditor.clear();
                peditor.apply();
        onBackPressed();
            }
        });


        mLayoutManager=(new LinearLayoutManager(UserActivity_3.this));

        final ProgressBar progressBar =  findViewById(R.id.progressBaru2);


        recyclerView1 =  findViewById(R.id.recycleview_new_circleImage2);
        linearLayoutManager=(new LinearLayoutManager(UserActivity_3.this,LinearLayoutManager.HORIZONTAL,true));
        recyclerView1.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView1.setHasFixedSize(true);
        mcurrentuser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        final ImageView imageView= (ImageView)findViewById(R.id.imageView_useractivity2);
        PostKey=getIntent().getExtras().getString("Userprofil");
        final String Actvty=getIntent().getExtras().getString("Activity");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
            final CardView cardview_add=findViewById(R.id.useactivity_ffp_card22);

        final ImageButton followingboolean=findViewById(R.id.imageButton_useractivtyadd);
        final CardView  followbooleancard=findViewById(R.id.cardview_adder);
        final ProgressBar progressBar_following=findViewById(R.id.progressBaru_follow);
        progressBar_following.setVisibility(View.GONE);
       /* followbooleancard.setCardBackgroundColor(selectedColor);
        Drawable drawablelove=followingboolean.getDrawable();
        Drawable md1=drawablelove.mutate();
        md1= DrawableCompat.wrap(md1);
        DrawableCompat.setTint(md1,selectedColor);
        DrawableCompat.setTintMode(md1, PorterDuff.Mode.SRC_IN);*/

        followbooleancard.setPreventCornerOverlap(false);
        if (Build.VERSION.SDK_INT >= 21) {
            RelativeLayout relativeLayout1=findViewById(R.id.circleview_rel);
            relativeLayout1.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.circleborder2,null));
        }

        followingboolean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSwdnapshot) {
                      if (dataSwdnapshot.exists()) {
                          progressBar_following.setVisibility(View.VISIBLE);
                          followingboolean.setVisibility(View.GONE);
                          DatabaseReference storageRefer = databaseReference.child(PostKey).child("userid");
                          storageRefer.addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(DataSnapshot dataSnapshot) {
                                  final String UserId = dataSnapshot.getValue(String.class);
                                  final FirebaseAuth mAuth;
                                  mAuth = FirebaseAuth.getInstance();
                                  final DatabaseReference subscribed = firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").child(UserId);
                                  final Query checker = firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").child(UserId);
                                  checker.addListenerForSingleValueEvent(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                          if (dataSnapshot.exists()) {
                                                /*dont care*/
                                              dataSnapshot.getRef().removeValue();
                                              final DatabaseReference subscribed = firebaseDatabase.child(UserId).child("follower");
                                              subscribed.child(mAuth.getCurrentUser().getUid()).removeValue();
                                              firebaseDatabase.child(UserId).child("followerNo").runTransaction(new Transaction.Handler() {
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
                                              firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("feed").orderByChild("UserId").equalTo(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(DataSnapshot dataSnapshot) {
                                                      for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                          snapshot.getRef().removeValue();

                                                      }
                                                  }

                                                  @Override
                                                  public void onCancelled(DatabaseError databaseError) {
                                                  }
                                              });

                                              progressBar_following.setVisibility(View.GONE);
                                              followingboolean.setVisibility(View.VISIBLE);
                                              followingboolean.setImageResource(R.drawable.ic_person_add_black_24dp);
                                          } else {
                                              DatabaseReference pic = firebaseDatabase.child(UserId);
                                              pic.addListenerForSingleValueEvent(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(DataSnapshot dataSnapshot) {
                                                      String url = dataSnapshot.child("imageurl").getValue(String.class);
                                                      String name = dataSnapshot.child("username").getValue(String.class);
                                                      String status = dataSnapshot.child("status").getValue(String.class);
                                                      int color = dataSnapshot.child("color").getValue(Integer.class);


                                                      subscribed.child("UserID").setValue(UserId);
                                                      subscribed.child("username").setValue(name);
                                                      subscribed.child("status").setValue(status);
                                                      subscribed.child("CircleImage").setValue(url);
                                                      subscribed.child("New").setValue(false);
                                                      subscribed.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                      subscribed.child("color").setValue(color);


                                                      firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                          @Override
                                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                                              DatabaseReference notify = firebaseDatabase.child(UserId).child("Notifications").push();
                                                              notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                              notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                                                              notify.child("action").setValue(dataSnapshot.child("username").getValue(String.class) + " has started following you");
                                                              notify.child("note").setValue("");
                                                              notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                                                              notify.child("Type").setValue("UserActivity");
                                                              notify.child("imageurl").setValue(dataSnapshot.child("imageurl").getValue(String.class));
                                                              notify.child("Seen").setValue(false);
                                                              notify.child("listener").setValue(true);
                                                          }

                                                          @Override
                                                          public void onCancelled(DatabaseError databaseError) {

                                                          }
                                                      });

                                                      firebaseDatabase.child(UserId).child("follower").child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getUid());
                                                      firebaseDatabase.child(UserId).child("followerNo").runTransaction(new Transaction.Handler() {
                                                          @Override
                                                          public Transaction.Result doTransaction(MutableData mutableData) {
                                                              if (mutableData.getValue() == null) {
                                                                  mutableData.setValue(0);
                                                              } else {
                                                                  int count = mutableData.getValue(Integer.class);
                                                                  mutableData.setValue(count + 1);
                                                              }
                                                              return Transaction.success(mutableData);
                                                          }

                                                          @Override
                                                          public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                                          }
                                                      });
                                                      progressBar_following.setVisibility(View.GONE);
                                                      followingboolean.setVisibility(View.VISIBLE);
                                                      followingboolean.setImageResource(R.drawable.ic_check_black_24dp);
                                                      DatabaseReference username = firebaseDatabase.child(UserId).child("username");
                                                      username.addListenerForSingleValueEvent(new ValueEventListener() {
                                                          @Override
                                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                                              String name = dataSnapshot.getValue(String.class);
                                                              Toast.makeText(UserActivity_3.this, "You are now following " + name, Toast.LENGTH_SHORT).show();
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
                      } else {
                          Toast.makeText(UserActivity_3.this,"You need to be logged in to follow someone!",Toast.LENGTH_SHORT).show();

                      }
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });


            }
        });

        final DatabaseReference storageRefer= databaseReference.child(PostKey);
        storageRefer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                final String UserId=dataSnapshot.child("userid").getValue(String.class);

               /* if (UserId.equals(mAuth.getCurrentUser().getUid())){
                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").child(UserId).removeValue();
                }*/

               firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataS4napshot) {
                       if (dataS4napshot.exists()) {
                           firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if (dataSnapshot.child(UserId).exists()) {
                                       firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").child(UserId).child("New").setValue(false);
                                   }
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {
                               }
                           });


                       //checking if account belong to user

                       // Toast.makeText(UserActivity_3.this,"LOged",Toast.LENGTH_SHORT).show();
                       if (mAuth.getCurrentUser().getUid().equals(UserId)) {
                           followbooleancard.setVisibility(View.GONE);
                           followingboolean.setVisibility(View.GONE);
                           RelativeLayout relativeLayout1 = findViewById(R.id.circleview_rel);
                           relativeLayout1.setVisibility(View.GONE);
                       }

                   } else {


                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });


                //setting follow number
                if (dataSnapshot.child("follow").exists()) {
                    long sub = dataSnapshot.child("follow").getChildrenCount();
                    TextView textView_follow = findViewById(R.id.textView_followingsnumber2);
                    if (sub == 0) {
                        textView_follow.setText("0");
                    } else {
                        textView_follow.setText(IntegerShortner(0,sub));
                    }
                }else{
                    TextView textView_follow = findViewById(R.id.textView_followingsnumber2);
                    textView_follow.setText("0");
                }

                //setting follower number
                if (dataSnapshot.child("followerNo").exists()) {
                    int count = dataSnapshot.child("followerNo").getValue(Integer.class);
                    TextView textView_follower = findViewById(R.id.textView_followersnumber2);
                    if (count == 0) {
                        textView_follower.setText(String.valueOf(0));
                    } else {
                        textView_follower.setText(IntegerShortner(count,0));
                    }
                }else{
                    TextView textView_follower = findViewById(R.id.textView_followersnumber2);
                    textView_follower.setText(String.valueOf(0));
                }

                //getting number of posts
                if (dataSnapshot.child("PostCount").exists()) {
                    TextView textView1_post = findViewById(R.id.textView_followerbooks2);
                    if (dataSnapshot.child("PostCount").getValue(Integer.class)==0) {
                        textView1_post.setText("0");
                    }else {
                        textView1_post.setText(IntegerShortner(dataSnapshot.child("PostCount").getValue(Integer.class),0));
                    }
                }else{
                    TextView textView1_post = findViewById(R.id.textView_followerbooks2);
                    textView1_post.setText("0");
                }



                //setting progressbar color
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(dataSnapshot.child("color").getValue(Integer.class)));
                }
                //setting account owners images
                final  String imageUrl = dataSnapshot.child("imageurl").getValue(String.class);

                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .skipMemoryCache(true)
                        .into(imageView);
                final ImageView UserImage= findViewById(R.id.imageView_blur2);
                Picasso.with(getApplicationContext())
                        .load(imageUrl)
                        .resize(400, 200)
                        .centerCrop()
                        .transform(new BlurTransformation(UserActivity_3.this, 50))
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(UserImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                boolean hasDrawable = (UserImage.getDrawable() != null);
                                if(hasDrawable && out) {
                                    ImageView imageView_blr =  findViewById(R.id.imageView_blur2);
                                    BitmapDrawable drawable = (BitmapDrawable) imageView_blr.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();
                                    selectedColor = calculateAverageColor(bitmap, 1);
                                    TextView text=findViewById(R.id.statuc_useractivity2);
                                    TextView textView= findViewById(R.id.editText_useractivity2);
                                    int selecttextcolor=getContrastColor(selectedColor);
                                    textView.setTextColor(selecttextcolor);
                                    followbooleancard.setCardBackgroundColor(selectedColor);
                                    text.setTextColor(selecttextcolor);
                                    int whit1e = Color.BLACK;
                                    cardview_add.setCardBackgroundColor(selectedColor);
                                    progressBar.setVisibility(View.GONE);
                                    int Trans = Color.TRANSPARENT;
                                    int transmix = blendARGB(selectedColor, Trans, 0.2f);
                                    collapsingToolbarLayout=findViewById(R.id.callaping_vp2);
                                   // collapsingToolbarLayout.setContentScrimColor(transmix);
                                    //setting status
                                    final String status = dataSnapshot.child("status").getValue(String.class);
                                    if (status.length()>200){
                                        String s=status.substring(0,200)+"...";
                                        TextView textView_status = findViewById(R.id.statuc_useractivity2);
                                        textView_status.setText(s);
                                        textView_status.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Status_Preview status_preview=new Status_Preview(UserActivity_3.this,status,dataSnapshot.child("color").getValue(Integer.class),getContrastColor(dataSnapshot.child("color").getValue(Integer.class)));
                                                status_preview.show();
                                            }
                                        });
                                    }else{
                                        TextView textView_status = findViewById(R.id.statuc_useractivity2);
                                        textView_status.setText(status);
                                    }
                                    if (Build.VERSION.SDK_INT >= 21) {
                                        Window window = UserActivity_3.this.getWindow();
                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                        int white = Color.BLACK;
                                        int resultColor = blendARGB(selectedColor, white, 0.3f);
                                        window.setStatusBarColor(resultColor);
                                    }

                                    mViewPager = findViewById(R.id.container_activity2);
                                    setupViewPager(mViewPager);

                                    final TabLayout tabLayout = findViewById(R.id.tabs_useractivity2);
                                    tabLayout.setupWithViewPager(mViewPager);



                                    final int blend = blendARGB(selectedColor, Color.WHITE, 0.2f);

                                    int[] image = {
                                            R.drawable.ic_person_black_24dp,
                                            R.drawable.ic_favorite_border_black_24dp2
                                    };


                                    for (int i = 0; i < image.length; i++) {
                                        tabLayout.getTabAt(i).setIcon(image[i]);
                                        tabLayout.getTabAt(i).getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                    }

                                    View root = tabLayout.getChildAt(0);
                                    if (root instanceof LinearLayout) {
                                        ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                                        GradientDrawable draw = new GradientDrawable();
                                        draw.setColor(ResourcesCompat.getColor(getResources(),R.color.goodgrey,null));
                                        draw.setSize(2, 2);
                                        ((LinearLayout) root).setDividerPadding(10);
                                        ((LinearLayout) root).setDividerDrawable(draw);
                                    }
                                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                        @Override
                                        public void onTabSelected(TabLayout.Tab tab) {
                                            if (tabLayout.getSelectedTabPosition() == 1) {
                                                tab.setIcon(R.drawable.ic_favorite_black_24dp);
                                                tab.getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                            } else {
                                                tab.setIcon(R.drawable.ic_person_black_24dp);
                                                tab.getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                            }
                                        }
                                        @Override
                                        public void onTabUnselected(TabLayout.Tab tab) {
                                            if (tabLayout.getSelectedTabPosition() == 0) {
                                                tab.setIcon(R.drawable.ic_person_outline_black_24dp);
                                                tab.getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                            } else {
                                                tab.setIcon(R.drawable.ic_favorite_border_black_24dp2);
                                                tab.getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                            }
                                        }
                                        @Override
                                        public void onTabReselected(TabLayout.Tab tab) {

                                        }
                                    });

                                }
                            }
                            @Override
                            public void onError() {
                                Picasso.with(UserActivity_3.this)
                                        .load(imageUrl)
                                        .resize(400, 200)
                                        .centerCrop()
                                        .transform(new BlurTransformation(UserActivity_3.this, 50))
                                        .into(UserImage, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                boolean hasDrawable = (UserImage.getDrawable() != null);
                                                if(hasDrawable && out) {
                                                    BitmapDrawable drawable = (BitmapDrawable) UserImage.getDrawable();
                                                    Bitmap bitmap = drawable.getBitmap();
                                                    selectedColor = calculateAverageColor(bitmap, 1);
                                                    TextView text=findViewById(R.id.statuc_useractivity2);
                                                    TextView textView= findViewById(R.id.editText_useractivity2);
                                                    int selecttextcolor=getContrastColor(selectedColor);
                                                    text.setTextColor(selecttextcolor);
                                                    textView.setTextColor(selecttextcolor);

                                                    followbooleancard.setCardBackgroundColor(selectedColor);
                                                    progressBar.setVisibility(View.GONE);
                                                    collapsingToolbarLayout=findViewById(R.id.callaping_vp2);
                                                    cardview_add.setCardBackgroundColor(selectedColor);
                                                   // collapsingToolbarLayout.setContentScrimColor(transmix);
                                                    if (Build.VERSION.SDK_INT >= 21) {
                                                        Window window = UserActivity_3.this.getWindow();
                                                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                        int white = Color.WHITE;
                                                        int resultColor = blendARGB(selectedColor, white, 0.3f);
                                                        window.setStatusBarColor(resultColor);
                                                    }
                                                    //setting status
                                                    final String status = dataSnapshot.child("status").getValue(String.class);
                                                    if (status.length()>200){
                                                        String s=status.substring(0,200)+"...";
                                                        TextView textView_status = findViewById(R.id.statuc_useractivity2);
                                                        textView_status.setText(s);
                                                        textView_status.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                Status_Preview status_preview=new Status_Preview(UserActivity_3.this,status,dataSnapshot.child("color").getValue(Integer.class),getContrastColor(dataSnapshot.child("color").getValue(Integer.class)));
                                                                status_preview.show();
                                                            }
                                                        });
                                                    }else{
                                                        TextView textView_status = findViewById(R.id.statuc_useractivity2);
                                                        textView_status.setText(status);
                                                    }
                                                    mViewPager = findViewById(R.id.container_activity2);
                                                    setupViewPager(mViewPager);

                                                    final TabLayout tabLayout = findViewById(R.id.tabs_useractivity2);
                                                    tabLayout.setupWithViewPager(mViewPager);



                                                    final int blend = blendARGB(selectedColor, Color.WHITE, 0.2f);

                                                    int[] image = {
                                                            R.drawable.ic_person_black_24dp,
                                                            R.drawable.ic_favorite_border_black_24dp2
                                                    };


                                                    for (int i = 0; i < image.length; i++) {
                                                        tabLayout.getTabAt(i).setIcon(image[i]);
                                                        tabLayout.getTabAt(i).getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                                    }

                                                    View root = tabLayout.getChildAt(0);
                                                    if (root instanceof LinearLayout) {
                                                        ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                                                        GradientDrawable draw = new GradientDrawable();
                                                        draw.setColor(getResources().getColor(R.color.goodgrey));
                                                        draw.setSize(2, 2);
                                                        ((LinearLayout) root).setDividerPadding(10);
                                                        ((LinearLayout) root).setDividerDrawable(draw);
                                                    }
                                                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                        @Override
                                                        public void onTabSelected(TabLayout.Tab tab) {
                                                            if (tabLayout.getSelectedTabPosition() == 1) {
                                                                tab.setIcon(R.drawable.ic_favorite_black_24dp);
                                                                tab.getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                                            } else {
                                                                tab.setIcon(R.drawable.ic_person_black_24dp);
                                                                tab.getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                                            }
                                                        }
                                                        @Override
                                                        public void onTabUnselected(TabLayout.Tab tab) {
                                                            if (tabLayout.getSelectedTabPosition() == 0) {
                                                                tab.setIcon(R.drawable.ic_person_outline_black_24dp);
                                                                tab.getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                                            } else {
                                                                tab.setIcon(R.drawable.ic_favorite_border_black_24dp2);
                                                                tab.getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                                                            }
                                                        }
                                                        @Override
                                                        public void onTabReselected(TabLayout.Tab tab) {

                                                        }
                                                    });

                                                }
                                            }
                                            @Override
                                            public void onError() {
                                            }
                                        });
                            }
                        });

                //checking for wether following or not
                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataS3napshot) {
                        if (dataS3napshot.exists()){
                            final Query checker = firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").orderByChild("UserID").equalTo(UserId);
                            checker.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        followingboolean.setImageResource(R.drawable.ic_check_black_24dp);
                                        if (Actvty.equals("U1")) {
                                            final DatabaseReference subscribed = firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").child(PostKey);
                                            DatabaseReference pic = firebaseDatabase.child(PostKey);
                                            pic.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    subscribed.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    } else {
                                        followingboolean.setImageResource(R.drawable.ic_person_add_black_24dp);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }else {
                            followingboolean.setImageResource(R.drawable.ic_person_add_black_24dp);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //setting circlefollowing
                final DatabaseReference bono=dataSnapshot.child("follow").getRef();
                final Query bongo=bono.orderByChild("timestamp").limitToLast(20);
                bongo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long Long=dataSnapshot.getChildrenCount();
                        if (Long==0){
                            final RelativeLayout relativeLayout_circle=findViewById(R.id.relativeview_useractivity2);
                            relativeLayout_circle.setVisibility(View.GONE);
                        }else{
                            recyclerview1(bongo);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                //setting userid in shared preferance
                SharedPreferences psharedPreferences=getSharedPreferences("param",MODE_PRIVATE);
                SharedPreferences.Editor peditor=psharedPreferences.edit();
                peditor.putString("mauth",UserId);
                peditor.apply();

                //setting username
                final  String username = dataSnapshot.child("username").getValue(String.class);
                TextView textView_username= findViewById(R.id.editText_useractivity2);
                textView_username.setText(username);

                //setting status

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        KenBurnsView imageView_blr =  findViewById(R.id.imageView_blur2);
        imageView_blr.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }
            @Override
            public void onTransitionEnd(Transition transition) {
            }
        });


    }
    private void recyclerview1(Query bongo) {
        firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<CircleImage, UserActivity_3.Circleviewholder>(
                CircleImage.class,
                R.layout.circleimageview,
                UserActivity_3.Circleviewholder.class,
                bongo
        ) {
            @Override
            protected void populateViewHolder(UserActivity_3.Circleviewholder viewHolder, CircleImage model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setCircleImage(UserActivity_3.this,model.getCircleImage());
                viewHolder.circleview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2 = new Intent(UserActivity_3.this,UserActivity_3.class);
                        intent2.putExtra("Userprofil",postKey);
                        intent2.putExtra("Activity","U3");
                        startActivity(intent2);//fuck this
                    }
                });
                /*viewHolder.circleview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Bitmap bitmap= blur(getWindow().getDecorView().getRootView());
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
                        final byte[] bytes=stream.toByteArray();
                        Intent intent=new Intent(UserActivity_3.this,popup.class);
                        intent.putExtra("Userprofi",postKey);
                        intent.putExtra("bit",bytes);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return false;
                    }
                });*/
            }
        };
        firebaseRecyclerAdapter1.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView1.setAdapter(firebaseRecyclerAdapter1);
            }
        });
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
    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
    }
    public static class Circleviewholder extends RecyclerView.ViewHolder {
        ImageView circleview;
        CardView cardview_new;
        ImageButton imagebutton_new;
        public Circleviewholder(View itemView) {
            super(itemView);
            cardview_new=itemView.findViewById(R.id.cardview_circleview_alert);
            circleview=itemView.findViewById(R.id.circleimageview);
        }
        public void setCircleImage(final Context ctx, final String image) {
            cardview_new.setVisibility(View.GONE);
            final ImageView imageView =  itemView.findViewById(R.id.circleimageview);
            Glide.with(ctx)
                    .load(image)
                    .skipMemoryCache(true)
                    .into(imageView);
        }
    }

    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;
    public static Bitmap blur(View v) {
        return blur(v.getContext(), getScreenshot(v));
    }
    public static Bitmap blur(Context ctx, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(ctx);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter2 adapter1 = new SectionPageAdapter2(getSupportFragmentManager());
        adapter1.addFragment(new useractivity_recyclerview(), "Call");
        adapter1.addFragment(new useractivty_fav(), "Call");
        viewPager.setAdapter(adapter1);
    }
    private static Bitmap getScreenshot(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            out=false;
        if (firebaseRecyclerAdapter1!=null) {
            firebaseRecyclerAdapter1.cleanup();
        }
        recyclerView1.getRecycledViewPool().clear();
        databaseReference.child(PostKey).child("UserId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    if (!mAuth.getCurrentUser().getUid().equals(dataSnapshot.getValue(String.class))) {
                        unbindDrawables(findViewById(R.id.cord));
                        // Intent inte=new Intent(UserActivity_3.this, UserActivity.class);
                        // startActivity(inte);
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (firebaseRecyclerAdapter1!=null) {
            firebaseRecyclerAdapter1.cleanup();
        }
        recyclerView1.getRecycledViewPool().clear();
        databaseReference.child(PostKey).child("UserId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!mAuth.getCurrentUser().getUid().equals(dataSnapshot.getValue(String.class))) {
                        unbindDrawables(findViewById(R.id.cord));
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void unbindDrawables(View view){
        try{
            if (view.getBackground() !=null)
                view.getBackground().setCallback(null);

            if (view instanceof ImageView){
                ImageView imageView=(ImageView)view;
                imageView.setImageBitmap(null);
            }else if (view instanceof ViewGroup){
                ViewGroup viewGroup=(ViewGroup)view;
                for (int i=0;i<viewGroup.getChildCount();i++)
                    unbindDrawables(viewGroup.getChildAt(i));

                if (!(view instanceof AdapterView))
                    viewGroup.removeAllViews();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
    @Override
    protected void onResume() {
        super.onResume();
        PostKey=getIntent().getExtras().getString("Userprofil");
        databaseReference.child(PostKey).child("userid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences psharedPreferences=getSharedPreferences("param",MODE_PRIVATE);
                SharedPreferences.Editor peditor=psharedPreferences.edit();
                peditor.putString("mauth",dataSnapshot.getValue(String.class));
                peditor.apply();
                mViewPager = findViewById(R.id.container_activity2);
               // setupViewPager(mViewPager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        PostKey=getIntent().getExtras().getString("Userprofil");
        databaseReference.child(PostKey).child("userid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SharedPreferences psharedPreferences=getSharedPreferences("param",MODE_PRIVATE);
                SharedPreferences.Editor peditor=psharedPreferences.edit();
                peditor.putString("mauth",dataSnapshot.getValue(String.class));
                peditor.apply();
                mViewPager = findViewById(R.id.container_activity2);
                setupViewPager(mViewPager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
