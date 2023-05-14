package com.dev.textnet.Userpro;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
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
import android.support.v4.view.ViewPager;
import com.bumptech.glide.Glide;
import com.dev.textnet.Address_followers;
import com.dev.textnet.All_followers;
import com.dev.textnet.CircleImage;
import com.dev.textnet.CustomImageSizeGlideModule;
import com.dev.textnet.Logout_Dialog;
import com.dev.textnet.MainActivity;
import com.dev.textnet.Progressing;

import com.dev.textnet.Typing;
import com.dev.textnet.UserActivity_3;
import com.dev.textnet.status_dialog;
import com.dev.textnet.useractivity_recyclerview;
import com.dev.textnet.useractivty_fav;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.dev.textnet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import static android.support.v4.graphics.ColorUtils.blendARGB;

/**
 * Created by Dell on 2017/06/23.
 */

public class UserActivity extends AppCompatActivity {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private  RecyclerView recyclerView1;
    static   int TotalItemCount3;
    private LinearLayoutManager linearLayoutManager;
    Boolean thresh= true;
    int count=2;
    static Query circlequery;
     FirebaseRecyclerAdapter<CircleImage,Circleviewholder> firebaseRecyclerAdapter1;
    static int selectedColor;
    static int       Posit2;
    private static final int SELECT_FILE = 2;

    DatabaseReference firebaseDatabase;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    private boolean ProcessUpvote = false;
    static int mRvTopView2;
    static SharedPreferences sharedPreferences2;
    static   int  RGB;
    String RV_TOP_VIEW2="TopView";
    private ViewPager mViewPager;
    FirebaseUser mcurrentuser;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Uri imageHoldUri;
    Boolean blink=false;
    String RV_POS_INDEX2="Index";
    static  Query filter;
    static  int TotalItemCount2;
    static      Query filterQuery;
    private ProgressBar progressBar;
    Boolean Notifiy=false;
    private boolean out=true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useractivity);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Posts");
        final Toolbar toolbar =  findViewById(R.id.toolbar_UserActivity);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        progressBar=findViewById(R.id.progressBaru1);
        final CardView cardview_circleadd=findViewById(R.id.cardview_circleadd);
        ImageButton imagebutton_uadd=findViewById(R.id.cardview_circleadd_buttton);
        final RelativeLayout relativeLayout=findViewById(R.id.relativeview_useractivity);

        mcurrentuser = mAuth.getCurrentUser();
        if (getIntent().getExtras() != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                int statusColor=getIntent().getExtras().getInt("RGB");
                int white = Color.BLACK;
                int resultColor = blendARGB(statusColor, white, 0.1f);
                Window window = this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(resultColor);
                SharedPreferences sharedPreferences=getSharedPreferences("stats",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("statusbar",resultColor);
                editor.apply();
            }
        }else{
            if (Build.VERSION.SDK_INT >= 21){
                SharedPreferences sharedPreferences=getSharedPreferences("stats",Context.MODE_PRIVATE);
               RGB = (sharedPreferences.getInt("statusbar", 0));
                int white = Color.BLACK;
                int resultColor = blendARGB(RGB, white, 0.1f);
                Window window = this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(resultColor);
            }
        }
        SharedPreferences psharedPreferences=getSharedPreferences("param",MODE_PRIVATE);
        SharedPreferences.Editor peditor=psharedPreferences.edit();
        peditor.putString("mauth",mAuth.getCurrentUser().getUid());
        peditor.apply();
         mViewPager = findViewById(R.id.container_activity);
      setupViewPager(mViewPager);
        final TabLayout tabLayout =  findViewById(R.id.tabs_useractivity);
        tabLayout.setupWithViewPager(mViewPager);
      //  Toast.makeText(UserActivity.this,"kk",Toast.LENGTH_SHORT).show();

        recyclerView1=findViewById(R.id.recycleview_new_circleImage) ;
        linearLayoutManager=(new LinearLayoutManager(UserActivity.this,LinearLayoutManager.HORIZONTAL,true));
        linearLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView1.setHasFixedSize(true);

        final CardView cardview = findViewById(R.id.nextcircle);
        ImageButton imageButton=findViewById(R.id.imageButton_nextcircle);


        imagebutton_uadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserActivity.this, Typing.class);
                startActivity(intent);

            }
        });
        cardview_circleadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserActivity.this, Typing.class);
                startActivity(intent);

            }
        });
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int statusColor=getIntent().getExtras().getInt("RGB");
                Intent intent=new Intent(UserActivity.this,All_followers.class);
                intent.putExtra("Color",statusColor);
                startActivity(intent);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override//lit
            public void onClick(View view) {
                int statusColor=getIntent().getExtras().getInt("RGB");

                Intent intent=new Intent(UserActivity.this,All_followers.class);
                intent.putExtra("Color",statusColor);
                startActivity(intent);
            }
        });


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //ma broda
            }
        });
        final ImageView imageView = findViewById(R.id.imageView_useractivity);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //in nigeria right now boiiiiiiiiiiii
                startActivityForResult(intent, SELECT_FILE);
            }
        });



        final KenBurnsView UserImage =  findViewById(R.id.imageView_blur);
        UserImage.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }
            @Override
            public void onTransitionEnd(Transition transition) {
            }
        });
        TextView late=findViewById(R.id.statuc_useractivity);
        late.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("color").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int color=dataSnapshot.getValue(Integer.class);
                        status_dialog status_dialog=new status_dialog(UserActivity.this,mAuth.getCurrentUser().getUid(),color);
                        status_dialog.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //getting user info
        final DatabaseReference getUserInfo= firebaseDatabase.child(mAuth.getCurrentUser().getUid());
      //  final DatabaseReference g=FirebaseDatabase.getInstance().getReference().child("Posts");
        getUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                dataSnapshot.getRef().keepSynced(true);

                //getting status
                String status=dataSnapshot.child("status").getValue(String.class);
                TextView showedittext = findViewById(R.id.statuc_useractivity);
                if (showedittext!=null) {
                    showedittext.setText(status);
                }
                //getting username
                final String username=dataSnapshot.child("username").getValue(String.class);
                TextView textView =  findViewById(R.id.editText_useractivity);
                if (textView!=null) {
                    textView.setText(username);
                }
                //gettting follower number
                if (dataSnapshot.child("followerNo").exists()) {
                    int count = dataSnapshot.child("followerNo").getValue(Integer.class);
                    TextView followercount = findViewById(R.id.textView_followersnumber);
                    if (followercount!=null) {
                        if (count == 0) {
                            followercount.setText(String.valueOf(0));

                        } else {
                            followercount.setText(IntegerShortner(count,0));
                        }
                    }
                }else{
                    TextView followercount = findViewById(R.id.textView_followersnumber);
                    if (followercount!=null) {
                        followercount.setText(String.valueOf(0));
                    }
                }
                //getting following number
                if (dataSnapshot.child("follow").exists()) {
                    long sub = dataSnapshot.child("follow").getChildrenCount();
                    TextView followcount = findViewById(R.id.textView_followingsnumber);

                    if (followcount!=null) {
                        if (sub == 0) {
                            followcount.setText(String.valueOf(0));

                        } else {

                            followcount.setText(IntegerShortner(0, sub));
                        }
                    }
                }else{
                    TextView followcount = findViewById(R.id.textView_followingsnumber);
                    if (followcount!=null) {
                        followcount.setText(String.valueOf(0));
                    }
                }

                //getting number of posts
                if (dataSnapshot.child("PostCount").exists()) {
                    TextView textView1_post = findViewById(R.id.textView_followerbooks);
                    if (textView1_post!=null) {
                        if (dataSnapshot.child("PostCount").getValue(Integer.class)== 0) {
                            textView1_post.setText("0");
                        } else {
                            textView1_post.setText(IntegerShortner(dataSnapshot.child("PostCount").getValue(Integer.class),0));
                        }
                    }
                }else{
                    TextView textView1_post = findViewById(R.id.textView_followerbooks);
                    if (textView1_post!=null) {
                        textView1_post.setText("0");
                    }
                }

                //getting total likes
               /* dataSnapshot.getRef().child("MyPosts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TextView followcount2 = findViewById(R.id.textView_followingsnumber);
                        int Total = 0;
                        long likecount = 0;
                        long totallike = dataSnapshot.getChildrenCount();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (dataSnapshot1.child("hearts").exists()) {
                                likecount = likecount + 1;
                                Total = Total + dataSnapshot1.child("hearts").getValue(Integer.class);
                            }
                            if (likecount == totallike) {
                                followcount2.setText(IntegerShortner(Total, 0));
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });*/

               //setting progressbar color
              //  progressBar color
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && dataSnapshot.child("color").exists()) {
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(dataSnapshot.child("color").getValue(Integer.class)));
                }

                //getting user images
                final String imageUrl = dataSnapshot.child("imageurl").getValue(String.class);
                CustomImageSizeGlideModule.CustomImageSizeModel customImageRequest1 = new CustomImageSizeGlideModule.CustomImageSizeModelFutureStudio(imageUrl);
                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .into(imageView);
                final ImageView UserImage =  findViewById(R.id.imageView_blur);
                if (UserImage!=null) {
                    Picasso.with(getApplicationContext())
                            .load(imageUrl)
                            .resize(400, 200)
                            .centerCrop()
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .transform(new BlurTransformation(UserActivity.this, 50))
                            .into(UserImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    boolean hasDrawable = (UserImage.getDrawable() != null);
                                    if (hasDrawable && out) {
                                        ImageView imageView_blr = findViewById(R.id.imageView_blur);
                                        BitmapDrawable drawable = (BitmapDrawable) imageView_blr.getDrawable();
                                        Bitmap bitmap = drawable.getBitmap();
                                        int selectedColor = calculateAverageColor(bitmap, 2);
                                        progressBar.setVisibility(View.GONE);
                                        CardView cardView_ffp = findViewById(R.id.useactivity_ffp_card);
                                        cardView_ffp.setCardBackgroundColor(selectedColor);
                                        TextView textView = findViewById(R.id.editText_useractivity);
                                        final TextView textView2 = findViewById(R.id.statuc_useractivity);
                                        //ScrollView scrollView=findViewById(R.id.movies);
                                        CardView cardview = findViewById(R.id.nextcircle);
                                        int Trans = Color.TRANSPARENT;
                                        int transmix = blendARGB(selectedColor, Trans, 0.2f);
                                        cardview.setCardBackgroundColor(transmix);
                                        cardview_circleadd.setCardBackgroundColor(selectedColor);
                                        int selecttextcolor = getContrastColor(selectedColor);
                                        textView.setTextColor(selecttextcolor);
                                        textView2.setTextColor(selecttextcolor);
                                        collapsingToolbarLayout = findViewById(R.id.callaping_vp);


                                        textView2.setMovementMethod(new ScrollingMovementMethod());
                                    }
                                }

                                @Override
                                public void onError() {
                                    Picasso.with(getApplicationContext())
                                            .load(imageUrl)
                                            .resize(400, 200)
                                            .centerCrop()
                                            .transform(new BlurTransformation(UserActivity.this, 50))
                                            .into(UserImage, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    boolean hasDrawable = (UserImage.getDrawable() != null);
                                                    if (hasDrawable && out) {
                                                        ImageView imageView_blr = findViewById(R.id.imageView_blur);
                                                        BitmapDrawable drawable = (BitmapDrawable) imageView_blr.getDrawable();
                                                        Bitmap bitmap = drawable.getBitmap();
                                                        // ScrollView scrollView=findViewById(R.id.movies);
                                                        selectedColor = calculateAverageColor(bitmap, 2);
                                                        progressBar.setVisibility(View.GONE);
                                                        TextView textView = findViewById(R.id.editText_useractivity);
                                                        CardView cardview = findViewById(R.id.nextcircle);
                                                        CardView cardView_ffp = findViewById(R.id.useactivity_ffp_card);
                                                        cardView_ffp.setCardBackgroundColor(selectedColor);
                                                        int Trans = Color.TRANSPARENT;
                                                        int transmix = blendARGB(selectedColor, Trans, 0.2f);
                                                        cardview.setCardBackgroundColor(transmix);
                                                        final TextView textView2 = findViewById(R.id.statuc_useractivity);
                                                        int selecttextcolor = getContrastColor(selectedColor);
                                                        textView.setTextColor(selecttextcolor);
                                                        textView2.setTextColor(selecttextcolor);
                                                        cardview_circleadd.setCardBackgroundColor(selectedColor);


                                                        collapsingToolbarLayout = findViewById(R.id.callaping_vp);
                                                        textView2.setMovementMethod(new ScrollingMovementMethod());
                                                        // collapsingToolbarLayout.setContentScrimColor(transmix);
                                                    }
                                                }

                                                @Override
                                                public void onError() {
                                                }
                                            });
                                }
                            });
                }

                //updating following images
                final DatabaseReference imageupdater=dataSnapshot.child("follow").getRef();
                long childcount=dataSnapshot.child("follow").getChildrenCount();

                if (childcount<15){
                    //cardview.setVisibility(View.GONE);
                }
                if (childcount>=1){
                    for (final DataSnapshot snapshot : dataSnapshot.child("follow").getChildren()) {
                        count=count +1;


                        if (count<16){
                            DatabaseReference picupdate=firebaseDatabase.child(snapshot.getKey());
                            picupdate.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String url=dataSnapshot.child("imageurl").getValue(String.class);
                                    int color;
                                    if (dataSnapshot.child("color").exists()) {
                                        color = dataSnapshot.child("color").getValue(Integer.class);
                                        imageupdater.child(snapshot.getKey()).child("color").setValue(color);
                                    }
                                        imageupdater.child(snapshot.getKey()).child("CircleImage").setValue(url);


                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }

                }

                //setting following images
                long Long =   dataSnapshot.child("follow").getChildrenCount();
            //    Toast.makeText(UserActivity.this,String.valueOf(Long),Toast.LENGTH_SHORT).show();
                if (Long==0){
                    relativeLayout.setVisibility(View.GONE);
                }else{
                    circlequery=imageupdater.orderByChild("timestamp").limitToLast(15);
                    firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<CircleImage, Circleviewholder>(
                            CircleImage.class,
                            R.layout.circleimageview,
                            Circleviewholder.class,
                            circlequery
                    ) {

                        @Override
                        protected void populateViewHolder(Circleviewholder viewHolder, CircleImage model, int position) {
                            final String postKey = getRef(position).getKey();
                            viewHolder.setCircleImage(UserActivity.this,model.getCircleImage());
                            viewHolder.setColor(UserActivity.this,model.getColor(),model.getNew());
                            viewHolder.circleview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //to udate list acording to most clicked follower
                                      //  useractivty_fav useractivty_fav=new useractivty_fav();
                                 //   useractivty_fav.firebaseRecyclerAdapter.cleanup();
                                    Intent intent2 = new Intent(UserActivity.this,UserActivity_3.class);
                                    intent2.putExtra("Userprofil",postKey);
                                    intent2.putExtra("Activity","U1");
                                    startActivity(intent2);
                                    //finish();
                                }
                            });
                           /* viewHolder.circleview.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    Bitmap bitmap= blur(getWindow().getDecorView().getRootView());
                                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
                                    byte[] bytes=stream.toByteArray();
                                    Intent intent=new Intent(UserActivity.this,popup.class);
                                    intent.putExtra("Userprofi",postKey);
                                    intent.putExtra("bit",bytes);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.useractivity_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click<br />
        switch (item.getItemId()) {

            case R.id.texttogroup:
                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Address_followers share_dialog=new Address_followers(UserActivity.this,dataSnapshot.child("username").getValue(String.class),dataSnapshot.child("imageurl").getValue(String.class));
                       share_dialog.show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case R.id.settingsu:
                Logout_Dialog alert=new Logout_Dialog(UserActivity.this,mAuth.getCurrentUser().getUid());
                alert.show();/*sitting at home programmign all day like a faggot*/
                    break;
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionPageAdapter3 adapter3 = new SectionPageAdapter3(getSupportFragmentManager());
        adapter3.addFragment(new useractivity_recyclerview(), "Cal");
        adapter3.addFragment(new useractivty_fav(), "Call");
        viewPager.setAdapter(adapter3);
    }
    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
    }
    public int calculateAverageColor(android.graphics.Bitmap bitmap, int pixelSpacing) {
        int R = 0;
        int G = 0;
        int B = 0;
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
    public static class Circleviewholder extends RecyclerView.ViewHolder {
        ImageView circleview;
        CardView cardview_new;
        RelativeLayout relativeLayout;
        public Circleviewholder(View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.retarded);
            cardview_new=itemView.findViewById(R.id.cardview_circleview_alert);
            circleview=itemView.findViewById(R.id.circleimageview);
        }
        public void setCircleImage(final Context ctx, final String image) {
            final ImageView imageView =  itemView.findViewById(R.id.circleimageview);
            Glide.with(itemView.getContext())
                    .load(image)
                    .into(imageView);
        }
        public void setColor(UserActivity userActivity, int color, Boolean aNew) {
           // cardview_new.setCardBackgroundColor(color);
            if (Build.VERSION.SDK_INT >= 21) {
                if (aNew != null) {
                    if (aNew) {
                        cardview_new.setCardBackgroundColor(color);
                    } else {
                        relativeLayout.setVisibility(View.GONE);
                    }
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }else{
                relativeLayout.setVisibility(View.GONE);
            }
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
    private static Bitmap getScreenshot(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.cord_ua));
        if (firebaseRecyclerAdapter1!=null) {
            firebaseRecyclerAdapter1.cleanup();
        }
        recyclerView1.getRecycledViewPool().clear();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        out=false;
        if (firebaseRecyclerAdapter1!=null) {
            firebaseRecyclerAdapter1.cleanup();
        }
        unbindDrawables(findViewById(R.id.cord_ua));
        recyclerView1.getRecycledViewPool().clear();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ImageView imageView= findViewById(R.id.imageView_useractivity);
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();



            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setInitialCropWindowPaddingRatio(0)
                    .setRequestedSize(400,400)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);



            if (resultCode == RESULT_OK) {
                //deleting old image
                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("imageurl").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FirebaseStorage mstoragereference= FirebaseStorage.getInstance().getReference().getStorage();
                        StorageReference photoRef = mstoragereference.getReferenceFromUrl(dataSnapshot.getValue(String.class));
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //adding new image
                                imageHoldUri = result.getUri();
                                imageView.setImageURI(imageHoldUri);
                                Bitmap newDrawable;
                                newDrawable = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                if (newDrawable != null) {
                                    final int  selectedColor = calculateAverageColor(newDrawable, 1);
                                    final Progressing alert=new Progressing(UserActivity.this);
                                    alert.show();
                                    alert.textView1.setText("Saving...");
                                    StorageReference mstoragereference= FirebaseStorage.getInstance().getReference();
                                    final StorageReference mstorage = mstoragereference.child("User_profile").child(UserActivity.this.imageHoldUri.getLastPathSegment());
                                    mstorage.putFile(UserActivity.this.imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                           // final Uri imageUrl = taskSnapshot.getDownloadUrl();

                                            mstorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    //Bitmap hochladen
                                                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("color").setValue(selectedColor);
                                                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("imageurl").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
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
                                                                                        notify.child("action").setValue(dataSnapshot.child("username").getValue(String.class) + "'s profile picture was updated.");
                                                                                        notify.child("note").setValue("");
                                                                                        notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                                                                                        notify.child("imageurl").setValue(dataSnapshot.child("imageurl").getValue(String.class));
                                                                                        notify.child("Seen").setValue(false);
                                                                                        notify.child("Type").setValue("UserActivity");
                                                                                        count = count + 1;
                                                                                        if (count == childcount) {
                                                                                            unbindDrawables(findViewById(R.id.cord_ua));
                                                                                            recyclerView1.getRecycledViewPool().clear();
                                                                                            finish();
                                                                                            startActivity(new Intent(UserActivity.this, MainActivity.class));
                                                                                            alert.dismiss();

                                                                                        }
                                                                                    }
                                                                                }else{
                                                                                    unbindDrawables(findViewById(R.id.cord_ua));
                                                                                    recyclerView1.getRecycledViewPool().clear();
                                                                                    finish();
                                                                                    startActivity(new Intent(UserActivity.this, MainActivity.class));
                                                                                    alert.dismiss();
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
                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    });
                                }
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(UserActivity.this,"Upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       // Toast.makeText(UserActivity.this,"act",Toast.LENGTH_SHORT).show();

        SharedPreferences psharedPreferences=getSharedPreferences("param",MODE_PRIVATE);
        SharedPreferences.Editor peditor=psharedPreferences.edit();
        peditor.putString("mauth",mAuth.getCurrentUser().getUid());
        peditor.apply();

        mViewPager = findViewById(R.id.container_activity);
        setupViewPager(mViewPager);
        ViewPagerSetUp();

    }
    @Override
    protected void onResume() {
        super.onResume();
       // Toast.makeText(UserActivity.this,"res",Toast.LENGTH_SHORT).show();
        SharedPreferences psharedPreferences=getSharedPreferences("param",MODE_PRIVATE);
        SharedPreferences.Editor peditor=psharedPreferences.edit();
        peditor.putString("mauth",mAuth.getCurrentUser().getUid());
        peditor.apply();
      //  mViewPager = findViewById(R.id.container_activity);
      //  setupViewPager(mViewPager);

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
    private void ViewPagerSetUp(){
      //  Toast.makeText(UserActivity.this,"view",Toast.LENGTH_SHORT).show();

        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("color").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final  int blend=blendARGB(dataSnapshot.getValue(Integer.class),Color.WHITE,0.2f);
                final TabLayout tabLayout =  findViewById(R.id.tabs_useractivity);
                if (tabLayout!=null) {

                    int[] image = {
                            R.drawable.ic_person_black_24dp,
                            R.drawable.ic_favorite_border_black_24dp2,
                    };
                    for (int i = 0; i < image.length; i++) {
                        tabLayout.getTabAt(i).setIcon(image[i]);
                        tabLayout.getTabAt(i).getIcon().setColorFilter(blend, PorterDuff.Mode.SRC_IN);
                    }
                    View root = tabLayout.getChildAt(0);
                    if (root instanceof LinearLayout) {
                        ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                        GradientDrawable draw = new GradientDrawable();
                        draw.setColor(ResourcesCompat.getColor(getResources(), R.color.goodgrey, null));
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
                            }
                            if (tabLayout.getSelectedTabPosition() == 0) {
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}