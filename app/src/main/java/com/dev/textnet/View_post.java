package com.dev.textnet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kobakei.ratethisapp.RateThisApp;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;



import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Objects;

import static android.support.v4.graphics.ColorUtils.blendARGB;


/**
 * Created by Dell on 2017/06/30.
 */

public class View_post extends Activity {

    ProgressBar progressBar;
    CardView cardView;
    FirebaseAuth mAuth;
    String myimage;
    int maxVolume = 50;
    int selectedColor;
    Boolean soundcheck=false;
    TextView textView1;
    TextView textView;
    private AdView mAdView;
    DatabaseReference databaseReference;
    DatabaseReference firebaseDatabase;
    Boolean clicked=false;
    NestedScrollView scrollView;
    String commentnamesend;
    LinearLayout linearLayout;
    ImageButton imagebuttonlove;
    Boolean goOffline=false;
    Boolean getGoOffline2=false;
    ImageButton imageButtoncomment;
     RecyclerView recyclerView;
    private GestureDetector gestureDetector;
    String PostKey;
    MediaPlayer mediaplayer = new MediaPlayer();
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
     static String translatedText="f";
        Boolean mCancel=false;
        ViewModel mViewModel;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.view_post);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mAuth = FirebaseAuth.getInstance();
       /*linearLayout=(LinearLayout)findViewById(R.id.linear);*/
         progressBar=findViewById(R.id.progressBar2);
         cardView=findViewById(R.id.cardview_viewpost);
         textView=findViewById(R.id.editText_view_post);
         textView1=findViewById(R.id.Textview_view_post);
        final CardView cardauthor=findViewById(R.id.viewpost_cardauthor);

        final TextView textview_author=findViewById(R.id.textView6_77);

        imagebuttonlove=findViewById(R.id.imageButtonlove);
  //  scrollView=findViewById(R.id.nested);//i knew you'd come in handy
        final CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.main_collapsing);
        final AppBarLayout appBarLayout=findViewById(R.id.main_appbar_viewpost);
        final CoordinatorLayout coordinatorLayout=findViewById(R.id.viewpost_lay);
        final ImageButton imagebuttonshare=findViewById(R.id.imageButton_share);
        final ImageButton imagebutton_comment=findViewById(R.id.imageButton_comments);
        final NestedScrollView nestedScrollView=findViewById(R.id.nested);
        final TextView delete_notice=findViewById(R.id.textView_deletenotice);
       /* final ImageView imageviewauthorimage=(ImageView)findViewById(R.id.imageview_authorimage);
        final TextView textView_authorsname=(TextView)findViewById(R.id.textView_authorsname);*/

       cardView.setPreventCornerOverlap(false);
        cardauthor.setVisibility(View.INVISIBLE);
        imagebuttonshare.setVisibility(View.INVISIBLE);
        cardView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        imagebuttonlove.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        delete_notice.setVisibility(View.GONE);
        imagebutton_comment.setVisibility(View.INVISIBLE);

        textView1.setTextIsSelectable(true);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Posts");
        firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Users");


       /* appBarLayout.setOnTouchListener(new OnSlidingTouchListener(appBarLayout.getContext()){
            @Override
            public boolean onSlideDown() {
                onBackPressed();
                overridePendingTransition(R.anim.no_change,R.anim.slide_down);
                return true;
            }
        });*/





           PostKey=getIntent().getExtras().getString("Userprofile");


        appBarLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getActionMasked()==MotionEvent.ACTION_MOVE){




                    onBackPressed();
                    overridePendingTransition(R.anim.no_change,R.anim.slide_down);


                    //    Toast.makeText(View_post.this,String.valueOf(nestedScrollView.computeVerticalScrollOffset()),Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });




      /*  button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textView_com.getText().toString().isEmpty()) {
                    int time= java.util.Calendar.DAY_OF_YEAR;
                    Date currentTime = Calendar.getInstance().getTime();
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    mprogressDialog.setTitle("Saving comment...");
                    mprogressDialog.setCanceledOnTouchOutside(false);
                    mprogressDialog.show();
                    DatabaseReference comment=databaseReference.child(PostKey).child("Comments").push();
                    comment.child("Comment").setValue(textView_com.getText().toString());
                    comment.child("user").setValue(mAuth.getCurrentUser().getUid());
                    comment.child("username").setValue(commentnamesend);
                    comment.child("time").setValue(mydate);
                    comment.child("imageurl").setValue(myimage)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                mprogressDialog.dismiss();
                            }else{
                                mprogressDialog.dismiss();
                                Toast.makeText(View_post.this,"error",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }else{
                    Toast.makeText(View_post.this, "???", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


      /*  name.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id=dataSnapshot.getValue(String.class);

                DatabaseReference username=FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("username");
                DatabaseReference userimage=FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("imageurl");
                username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String usename=dataSnapshot.getValue(String.class);
                        textView_authorsname.setText(usename);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                userimage.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String image=dataSnapshot.getValue(String.class);
                    Glide.with(View_post.this).load(image).into(imageviewauthorimage);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        imagebutton_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            FirebaseDatabase.getInstance().goOnline();
                            databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.child("UserId").getValue(String.class).equals(mAuth.getCurrentUser().getUid())) {
                                        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                                Comment_dialog comment_dialog = new Comment_dialog(View_post.this, dataSnapshot1.child("username").getValue(String.class), dataSnapshot1.child("imageurl").getValue(String.class), PostKey, dataSnapshot.child("title").getValue(String.class));
                                                comment_dialog.show();
                                                comment_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialogInterface) {
                                                        FirebaseDatabase.getInstance().goOffline();
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
                        }else{
                            Toast.makeText(View_post.this,"You need to be logged in to make comment on posts!",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });







            DatabaseReference allpostitems = databaseReference.child(PostKey);
            allpostitems.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {


                        firebaseDatabase.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("Position").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    if (dataSnapshot.getChildrenCount()==10) {

                                        RateThisApp.showRateDialog(View_post.this);
                                        RateThisApp.Config config = new RateThisApp.Config();
                                        config.setTitle(R.string.Rate);
                                        config.setMessage(R.string.meat);
                                        RateThisApp.init(config);
                                        RateThisApp.setCallback(new RateThisApp.Callback() {
                                            @Override
                                            public void onYesClicked() {
                                              //  Toast.makeText(View_post.this, "Yes event", Toast.LENGTH_SHORT).show();
                                                mediaplayer.release();

                                            }

                                            @Override
                                            public void onNoClicked() {
                                               // Toast.makeText(View_post.this, "No event", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelClicked() {
                                               // Toast.makeText(View_post.this, "Cancel event", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    }
                                }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                       /* SeekBar seekBar = findViewById(R.id.seekBar1);
                        final ImageButton imageButton_dropdown=findViewById(R.id.imageButton_dropdown);
                        final LinearLayout linearLayout_dropdown=findViewById(R.id.linearlayout_dropdown);
                        RelativeLayout relativeLayout_holder=findViewById(R.id.relativeLayout_seekholder);
                       // relativeLayout_holder.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                        imageButton_dropdown.setVisibility(View.GONE);*/

                        firebaseDatabase.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("AllowAudio").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                               // Toast.makeText(View_post.this,"at all",Toast.LENGTH_SHORT).show();
                                if (!dataSnapshot1.exists()) {
                                   // Toast.makeText(View_post.this,"allowaudio ref doesnt exit",Toast.LENGTH_SHORT).show();
                                        dataSnapshot.child("audio").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                   // Toast.makeText(View_post.this,"audio ref exist",Toast.LENGTH_SHORT).show();
                                                    mediaplayer = new MediaPlayer();
                                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                                    final ImageButton imageButton_dropdown=findViewById(R.id.imageButton_dropdown);


                                                    imageButton_dropdown.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(final View view) {
                                                        final LinearLayout linearLayout_dropdown=findViewById(R.id.linearlayout_dropdown);
                                                            if (linearLayout_dropdown.getVisibility()==View.GONE) {

                                                                linearLayout_dropdown.setVisibility(View.VISIBLE);
                                                                imageButton_dropdown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                ImageButton imagebutton_time=findViewById(R.id.imageButton_time);
                                                                ImageButton imagebutton_count=findViewById(R.id.imageButton_wordcount);
                                                                if (selectedColor !=0) {
                                                                    int blackmix = blendARGB(selectedColor, Color.BLACK, 0.3f);
                                                                    Drawable drawable = imageButton_dropdown.getDrawable();
                                                                    Drawable md = drawable.mutate();
                                                                    md = DrawableCompat.wrap(md);
                                                                    DrawableCompat.setTint(md, blackmix);
                                                                    DrawableCompat.setTintMode(md, PorterDuff.Mode.SRC_IN);

                                                                    Drawable drawablet = imagebutton_time.getDrawable();
                                                                    Drawable mdt = drawablet.mutate();
                                                                    mdt = DrawableCompat.wrap(mdt);
                                                                    DrawableCompat.setTint(mdt, blackmix);
                                                                    DrawableCompat.setTintMode(mdt, PorterDuff.Mode.SRC_IN);

                                                                    Drawable drawablec = imagebutton_count.getDrawable();
                                                                    Drawable mdc = drawablec.mutate();
                                                                    mdc = DrawableCompat.wrap(mdc);
                                                                    DrawableCompat.setTint(mdc, blackmix);
                                                                    DrawableCompat.setTintMode(mdc, PorterDuff.Mode.SRC_IN);
                                                                   // linearLayout_divider.setBackgroundColor(blackmix);
                                                                }
                                                                TextView story = findViewById(R.id.Textview_view_post);
                                                                TextView wordcount=findViewById(R.id.textView_wordcount);
                                                                TextView readtime=findViewById(R.id.textView_time);
                                                                wordcount.setText(String.valueOf(story.getText().toString().trim().split("\\s+").length));
                                                                int minutes=story.getText().toString().trim().split("\\s+").length/200;

                                                                    readtime.setText(minutes+":00");



                                                            }else{

                                                                linearLayout_dropdown.setVisibility(View.GONE);
                                                                imageButton_dropdown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                                                if (selectedColor !=0){
                                                                    int blackmix = blendARGB(selectedColor, Color.BLACK, 0.3f);
                                                                    Drawable drawable = imageButton_dropdown.getDrawable();
                                                                    Drawable md = drawable.mutate();
                                                                    md = DrawableCompat.wrap(md);
                                                                    DrawableCompat.setTint(md, blackmix);
                                                                    DrawableCompat.setTintMode(md, PorterDuff.Mode.SRC_IN);
                                                                }

                                                            }
                                                        }
                                                    });
                                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                                    // Create a storage reference from our app
                                                    StorageReference storageRef = storage.getReferenceFromUrl(dataSnapshot.getValue(String.class));
                                                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            try {
                                                                // Download url of file
                                                                mediaplayer = new MediaPlayer();
                                                                final String url = uri.toString();
                                                                mediaplayer.setDataSource(url);
                                                                // wait for media player to get prepare
                                                                mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                                    @Override
                                                                    public void onPrepared(MediaPlayer dmediaPlayer) {
                                                                        if (View_post.this.isFinishing()) {
                                                                            dmediaPlayer.release();
                                                                            //nullify your MediaPlayer reference
                                                                            mediaplayer = null;
                                                                        } else {
                                                                            dmediaPlayer.start();
                                                                            dmediaPlayer.setLooping(true);
                                                                            initControls();
                                                                        }

                                                                    }

                                                                });
                                                                mediaplayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                                                    @Override
                                                                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                                                        Toast.makeText(View_post.this,"Audio failed to play.",Toast.LENGTH_SHORT).show();
                                                                        return false;
                                                                    }
                                                                });
                                                                mediaplayer.prepareAsync();
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(View_post.this, "Audio failed to play", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });


                                                } else {
                                                    SeekBar seekBar = findViewById(R.id.seekBar1);
                                                    final ImageButton imageButton_dropdown=findViewById(R.id.imageButton_dropdown);
                                                    final LinearLayout linearLayout_dropdown=findViewById(R.id.linearlayout_dropdown);
                                                    RelativeLayout relativeLayout_holder=findViewById(R.id.relativeLayout_seekholder);
                                                    relativeLayout_holder.setVisibility(View.GONE);
                                                    seekBar.setVisibility(View.GONE);
                                                    imageButton_dropdown.setVisibility(View.GONE);


                                                    linearLayout_dropdown.setVisibility(View.VISIBLE);



                                                    TextView story = findViewById(R.id.Textview_view_post);
                                                    TextView wordcount=findViewById(R.id.textView_wordcount);
                                                    TextView readtime=findViewById(R.id.textView_time);
                                                    wordcount.setText(String.valueOf(story.getText().toString().trim().split("\\s+").length));
                                                    int minutes=story.getText().toString().trim().split("\\s+").length/200;
                                                    readtime.setText(minutes+":00");

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                }else{
                                 //   Toast.makeText(View_post.this,"allowaudio ref  exit",Toast.LENGTH_SHORT).show();
                                    if (dataSnapshot1.getValue(Boolean.class)) {
                                      //  Toast.makeText(View_post.this,"allowaudio ref  exit and is true",Toast.LENGTH_SHORT).show();
                                        dataSnapshot.child("audio").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    mediaplayer = new MediaPlayer();
                                                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                    final ImageButton imageButton_dropdown=findViewById(R.id.imageButton_dropdown);


                                                    imageButton_dropdown.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(final View view) {
                                                            final LinearLayout linearLayout_dropdown=findViewById(R.id.linearlayout_dropdown);
                                                            if (linearLayout_dropdown.getVisibility()==View.GONE) {

                                                                linearLayout_dropdown.setVisibility(View.VISIBLE);
                                                                imageButton_dropdown.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                                                ImageButton imagebutton_time=findViewById(R.id.imageButton_time);
                                                                ImageButton imagebutton_count=findViewById(R.id.imageButton_wordcount);
                                                                if (selectedColor !=0) {
                                                                    int blackmix = blendARGB(selectedColor, Color.BLACK, 0.3f);
                                                                    Drawable drawable = imageButton_dropdown.getDrawable();
                                                                    Drawable md = drawable.mutate();
                                                                    md = DrawableCompat.wrap(md);
                                                                    DrawableCompat.setTint(md, blackmix);
                                                                    DrawableCompat.setTintMode(md, PorterDuff.Mode.SRC_IN);

                                                                    Drawable drawablet = imagebutton_time.getDrawable();
                                                                    Drawable mdt = drawablet.mutate();
                                                                    mdt = DrawableCompat.wrap(mdt);
                                                                    DrawableCompat.setTint(mdt, blackmix);
                                                                    DrawableCompat.setTintMode(mdt, PorterDuff.Mode.SRC_IN);

                                                                    Drawable drawablec = imagebutton_count.getDrawable();
                                                                    Drawable mdc = drawablec.mutate();
                                                                    mdc = DrawableCompat.wrap(mdc);
                                                                    DrawableCompat.setTint(mdc, blackmix);
                                                                    DrawableCompat.setTintMode(mdc, PorterDuff.Mode.SRC_IN);
                                                                    // linearLayout_divider.setBackgroundColor(blackmix);
                                                                }
                                                                TextView story = findViewById(R.id.Textview_view_post);
                                                                TextView wordcount=findViewById(R.id.textView_wordcount);
                                                                TextView readtime=findViewById(R.id.textView_time);
                                                                wordcount.setText(String.valueOf(story.getText().toString().trim().split("\\s+").length));
                                                                int minutes=story.getText().toString().trim().split("\\s+").length/200;

                                                                readtime.setText(minutes+":00");



                                                            }else{

                                                                linearLayout_dropdown.setVisibility(View.GONE);
                                                                imageButton_dropdown.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                                                if (selectedColor !=0){
                                                                    int blackmix = blendARGB(selectedColor, Color.BLACK, 0.3f);
                                                                    Drawable drawable = imageButton_dropdown.getDrawable();
                                                                    Drawable md = drawable.mutate();
                                                                    md = DrawableCompat.wrap(md);
                                                                    DrawableCompat.setTint(md, blackmix);
                                                                    DrawableCompat.setTintMode(md, PorterDuff.Mode.SRC_IN);
                                                                }

                                                            }
                                                        }
                                                    });

                                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                                    // Create a storage reference from our app
                                                    StorageReference storageRef = storage.getReferenceFromUrl(Objects.requireNonNull(dataSnapshot.getValue(String.class)));
                                                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            try {
                                                                // Download url of file
                                                                final String url = uri.toString();
                                                                mediaplayer = new MediaPlayer();
                                                                mediaplayer.setDataSource(url);
                                                                // wait for media player to get prepare
                                                                mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                                    @Override
                                                                    public void onPrepared(MediaPlayer dmediaPlayer) {
                                                                        if (View_post.this.isFinishing()) {
                                                                            dmediaPlayer.release();
                                                                            //nullify your MediaPlayer reference
                                                                            mediaplayer = null;
                                                                        } else {
                                                                            dmediaPlayer.start();
                                                                            dmediaPlayer.setLooping(true);
                                                                            initControls();
                                                                        }

                                                                    }

                                                                });
                                                                mediaplayer.prepareAsync();
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(View_post.this, "Audio failed to play", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });


                                                } else {
                                                    SeekBar seekBar = findViewById(R.id.seekBar1);
                                                    final ImageButton imageButton_dropdown=findViewById(R.id.imageButton_dropdown);
                                                    final LinearLayout linearLayout_dropdown=findViewById(R.id.linearlayout_dropdown);
                                                    RelativeLayout relativeLayout_holder=findViewById(R.id.relativeLayout_seekholder);
                                                    relativeLayout_holder.setVisibility(View.GONE);
                                                    seekBar.setVisibility(View.GONE);
                                                    imageButton_dropdown.setVisibility(View.GONE);


                                                    linearLayout_dropdown.setVisibility(View.VISIBLE);



                                                    TextView story = findViewById(R.id.Textview_view_post);
                                                    TextView wordcount=findViewById(R.id.textView_wordcount);
                                                    TextView readtime=findViewById(R.id.textView_time);
                                                    wordcount.setText(String.valueOf(story.getText().toString().trim().split("\\s+").length));
                                                    int minutes=story.getText().toString().trim().split("\\s+").length/200;
                                                    readtime.setText(minutes+":00");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }else{
                                        SeekBar seekBar = findViewById(R.id.seekBar1);
                                        final ImageButton imageButton_dropdown=findViewById(R.id.imageButton_dropdown);
                                        final LinearLayout linearLayout_dropdown=findViewById(R.id.linearlayout_dropdown);
                                        RelativeLayout relativeLayout_holder=findViewById(R.id.relativeLayout_seekholder);
                                        relativeLayout_holder.setVisibility(View.GONE);
                                        seekBar.setVisibility(View.GONE);
                                        imageButton_dropdown.setVisibility(View.GONE);


                                        linearLayout_dropdown.setVisibility(View.VISIBLE);



                                        TextView story = findViewById(R.id.Textview_view_post);
                                        TextView wordcount=findViewById(R.id.textView_wordcount);
                                        TextView readtime=findViewById(R.id.textView_time);
                                        wordcount.setText(String.valueOf(story.getText().toString().trim().split("\\s+").length));
                                        int minutes=story.getText().toString().trim().split("\\s+").length/200;
                                        readtime.setText(minutes+":00");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        dataSnapshot.getRef().keepSynced(true);
                        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                if (dataSnapshot1.exists()) {
                                    final DatabaseReference viewnumber = databaseReference.child(PostKey).child("views");
                                    final DatabaseReference user = firebaseDatabase.child(mAuth.getCurrentUser().getUid());
                                    final DatabaseReference viewid = user.child("ViewId");


                                    //checking if following and make fales
                                    if (!Objects.equals(dataSnapshot.child("UserId").getValue(String.class), mAuth.getCurrentUser().getUid())) {

                                        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                                if (dataSnapshot.child(Objects.requireNonNull(dataSnapshot.child("UserId").getValue(String.class))).exists()) {
                                                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").child(Objects.requireNonNull(dataSnapshot.child("UserId").getValue(String.class))).child("New").setValue(false);

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }

                                    //get number of views
                                    viewid.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                long totalviewcount = dataSnapshot.getChildrenCount();
                                                long viewcount = 0;
                                                Boolean addview = true;
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    viewcount = viewcount + 1;
                                                    if (Objects.equals(dataSnapshot1.getKey(), PostKey)) {
                                                        addview = false;
                                                    }
                                                }
                                                if (addview && viewcount == totalviewcount) {
                                                    viewid.child(PostKey).setValue(PostKey);
                                                    viewnumber.runTransaction(new Transaction.Handler() {
                                                        @NonNull
                                                        @Override
                                                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                            if (mutableData.getValue() == null) {
                                                                mutableData.setValue(1);
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
                                                }
                                            } else {
                                                viewid.child(PostKey).setValue(PostKey);
                                                viewnumber.runTransaction(new Transaction.Handler() {
                                                    @NonNull
                                                    @Override
                                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                        if (mutableData.getValue() == null) {
                                                            mutableData.setValue(1);
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
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    //check if liked
                                    user.child("Favorite").child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                imagebuttonlove.setImageResource(R.drawable.ic_favorite_like_24dp2);
                                                int blende = blendARGB(selectedColor, Color.BLACK, 0.5f);
                                                Drawable drawablelove = imagebuttonlove.getDrawable();
                                                Drawable md1 = drawablelove.mutate();
                                                md1 = DrawableCompat.wrap(md1);
                                                DrawableCompat.setTint(md1, blende);
                                                DrawableCompat.setTintMode(md1, PorterDuff.Mode.SRC_IN);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    mAdView = findViewById(R.id.adView);
                                    AdRequest adRequest = new AdRequest.Builder().build();
                                    mAdView.loadAd(adRequest);

                                }else{
                                    mAdView = findViewById(R.id.adView);
                                    AdRequest adRequest = new AdRequest.Builder().build();
                                    mAdView.loadAd(adRequest);
                                    final DatabaseReference viewnumber = databaseReference.child(PostKey).child("views");
                                    final DatabaseReference user = firebaseDatabase.child(mAuth.getCurrentUser().getUid());
                                    final DatabaseReference viewid = user.child("ViewId");
                                    viewid.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                long totalviewcount = dataSnapshot.getChildrenCount();
                                                long viewcount = 0;
                                                Boolean addview = true;
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    viewcount = viewcount + 1;
                                                    if (Objects.equals(dataSnapshot1.getKey(), PostKey)) {
                                                        addview = false;
                                                    }
                                                }
                                                if (addview && viewcount == totalviewcount) {
                                                    viewid.child(PostKey).setValue(PostKey);
                                                    viewnumber.runTransaction(new Transaction.Handler() {
                                                        @NonNull
                                                        @Override
                                                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                            if (mutableData.getValue() == null) {
                                                                mutableData.setValue(1);
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
                                                }
                                            } else {
                                                viewid.child(PostKey).setValue(PostKey);
                                                viewnumber.runTransaction(new Transaction.Handler() {
                                                    @NonNull
                                                    @Override
                                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                                        if (mutableData.getValue() == null) {
                                                            mutableData.setValue(1);
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
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        final ImageView imageviewcircle = findViewById(R.id.circleimage_viewpost);
                        TextView Title = findViewById(R.id.editText_view_post);
                        TextView story = findViewById(R.id.Textview_view_post);
                        String title1 = dataSnapshot.child("title").getValue(String.class);
                        String story1 = dataSnapshot.child("Story").getValue(String.class);
                      //  Title.setMovementMethod(LinkMovementMethod.getInstance());
                        Title.setText(title1);
                        story.setText(story1);
                        if (dataSnapshot.child("Typeface").exists()) {
                            String texttype = dataSnapshot.child("Typeface").getValue(String.class);
                            if (texttype.equals("MONOSPACE")) {
                                story.setTypeface(android.graphics.Typeface.MONOSPACE);
                                Title.setTypeface(android.graphics.Typeface.MONOSPACE);
                            }
                            if (texttype.equals("NORMAL")) {
                                story.setTypeface(android.graphics.Typeface.DEFAULT);
                                Title.setTypeface(android.graphics.Typeface.DEFAULT);
                            }
                            if (texttype.equals("SERIF")) {
                                story.setTypeface(android.graphics.Typeface.SERIF);
                                Title.setTypeface(android.graphics.Typeface.SERIF);
                            }
                            if (texttype.equals("CURSIVE")) {
                                android.graphics.Typeface typeface= android.graphics.Typeface.createFromAsset(getAssets(),"fonts/dancing-script.regular.ttf");
                                story.setTypeface(typeface);
                                Title.setTypeface(typeface);
                            }
                            if (texttype.equals("Amatic_Bold")) {
                                story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Amatic-Bold.ttf"));
                                Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Amatic-Bold.ttf"));
                            }
                            if (texttype.equals("Amatic_Regular")) {
                                story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/AmaticSC-Regular.ttf"));
                                Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/AmaticSC-Regular.ttf"));
                            }
                            if (texttype.equals("Capture")) {
                                story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Capture_it.ttf"));
                                Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Capture_it.ttf"));
                            }
                            if (texttype.equals("Caviar_Dreams_Bold")) {
                                story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Caviar_Dreams_Bold.ttf"));
                                Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Caviar_Dreams_Bold.ttf"));
                            }
                            if (texttype.equals("CaviarDreams")) {
                                story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf"));
                                Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf"));
                            }
                            if (texttype.equals("Ostrich")) {
                                story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/ostrich-regular.ttf"));
                                Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/ostrich-regular.ttf"));
                            }
                            if (texttype.equals("OstrichSans")) {
                                story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/OstrichSans-Heavy.otf"));
                                Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/OstrichSans-Heavy.otf"));
                            }
                            if (texttype.equals("Pacifico")) {
                                story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf"));
                                Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf"));
                            }
                            if (texttype.equals("PlayfairBold")) {
                                story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/PlayfairDisplay-Bold.otf"));
                                Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/PlayfairDisplay-Bold.otf"));
                            }
                                if (texttype.equals("PlayfairItalic")) {
                                    story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/PlayfairDisplay-Italic.otf"));
                                    Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/PlayfairDisplay-Italic.otf"));
                                }
                                if (texttype.equals("Respective")) {
                                    story.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Respective_Swashes_Slanted.ttf"));
                                    Title.setTypeface(android.graphics.Typeface.createFromAsset(getAssets(),"fonts/Respective_Swashes_Slanted.ttf"));
                                }
                        }
                        if (dataSnapshot.child("TextGravity").exists()){
                            String gravity=dataSnapshot.child("TextGravity").getValue(String.class);
                            if (gravity.equals("End")){
                                story.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                            }
                            if (gravity.equals("Center")){
                                story.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            }
                            if (gravity.equals("Start")){
                                story.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            }
                        }
                        if (dataSnapshot.child("TextSize").exists()) {
                            int textsize = dataSnapshot.child("TextSize").getValue(Integer.class);
                            story.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);
                        }
                        //setting writters image
                        String authorId = dataSnapshot.child("UserId").getValue(String.class);
                        firebaseDatabase.child(authorId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String imageurl = dataSnapshot.child("imageurl").getValue(String.class);
                                String name = dataSnapshot.child("username").getValue(String.class);
                                textview_author.setText(name);
                                Glide.with(getApplicationContext())
                                        .load(imageurl)
                                        .into(imageviewcircle);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        //setting post image
                        final String imageUrl = dataSnapshot.child("image").getValue(String.class);
                        final ImageView imageView = findViewById(R.id.imageView_view_post);
                        Picasso.with(View_post.this)
                                .load(imageUrl)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        boolean hasDrawable = (imageView.getDrawable() != null);
                                        if (hasDrawable) {
                                            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                                            Bitmap bitmap = drawable.getBitmap();
                                            selectedColor = calculateAverageColor(bitmap, 1);
                                            progressBar.setVisibility(View.GONE);
                                            int Inversecolor = getContrastColor(selectedColor);
                                            textView.setTextColor(Inversecolor);
                                            textView1.setTextColor(Inversecolor);
                                            cardView.setVisibility(View.VISIBLE);
                                            textView.setVisibility(View.VISIBLE);
                                            imagebuttonshare.setVisibility(View.VISIBLE);
                                            imagebuttonlove.setVisibility(View.VISIBLE);
                                            imagebutton_comment.setVisibility(View.VISIBLE);
                                            cardauthor.setVisibility(View.VISIBLE);
                                            textView1.setVisibility(View.VISIBLE);


                                            firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Position").child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                        nestedScrollView.scrollBy(0,dataSnapshot.getValue(Integer.class));
                                                      //  Toast.makeText(View_post.this,String.valueOf(dataSnapshot.getValue(Integer.class)),Toast.LENGTH_LONG).show();

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            int white = Color.WHITE;
                                            int black = Color.BLACK;
                                            int resultColor = blendARGB(selectedColor, white, 0.1f);
                                            int resultColor1 = blendARGB(selectedColor, white, 0.5f);
                                            int resultColor3 = blendARGB(selectedColor, white, 0.3f);
                                            int Trans = Color.TRANSPARENT;
                                            int resultt = blendARGB(selectedColor, Trans, 0.2f);
                                            int resultt2 = blendARGB(selectedColor, Trans, 0.6f);
                                            textView.setBackgroundColor(resultt);
                                            //  toolbar.setBackgroundColor(resultt2);
                                           SeekBar seekBar=findViewById(R.id.seekBar1);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                seekBar.setThumbTintList(ColorStateList.valueOf(resultColor));
                                                seekBar.setProgressBackgroundTintList(ColorStateList.valueOf(resultColor));
                                                seekBar.setProgressTintList(ColorStateList.valueOf(resultColor));
                                            }
                                            cardauthor.setCardBackgroundColor(resultColor);
                                            coordinatorLayout.setBackgroundColor(resultColor1);
                                            cardView.setCardBackgroundColor(resultColor);
                                    /*linearLayout.setBackgroundColor(resultColor1);*/
                                    /*scrollView.setBackgroundColor(resultColor1);*/
                                            if (Build.VERSION.SDK_INT >= 21) {
                                                Window window = View_post.this.getWindow();
                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                int white1 = Color.BLACK;
                                                int resultColor2 = blendARGB(selectedColor, white1, 0.1f);
                                                window.setStatusBarColor(resultColor2);
                                                appBarLayout.setBackgroundColor(resultColor2);
                                                collapsingToolbarLayout.setStatusBarScrimColor(resultColor2);
                                            }
                                             ImageButton imageButton_dropdown=findViewById(R.id.imageButton_dropdown);

                                            int blackmix1 = blendARGB(selectedColor, Color.BLACK, 0.3f);
                                            Drawable drawabled = imageButton_dropdown.getDrawable();
                                            Drawable mdd = drawabled.mutate();
                                            mdd = DrawableCompat.wrap(mdd);
                                            DrawableCompat.setTint(mdd, blackmix1);
                                            DrawableCompat.setTintMode(mdd, PorterDuff.Mode.SRC_IN);
                                            Drawable drawablelove = imagebuttonlove.getDrawable();
                                            Drawable md1 = drawablelove.mutate();
                                            md1 = DrawableCompat.wrap(md1);
                                            DrawableCompat.setTint(md1, blackmix1);
                                            DrawableCompat.setTintMode(md1, PorterDuff.Mode.SRC_IN);
                                            Drawable drawableview = imagebutton_comment.getDrawable();
                                            Drawable md2 = drawableview.mutate();
                                            md2 = DrawableCompat.wrap(md2);
                                            DrawableCompat.setTint(md2, blackmix1);
                                            DrawableCompat.setTintMode(md2, PorterDuff.Mode.SRC_IN);
                                            Drawable drawableshare = imagebuttonshare.getDrawable();
                                            Drawable md_share = drawableshare.mutate();
                                            md_share = DrawableCompat.wrap(md_share);
                                            DrawableCompat.setTint(md_share, blackmix1);
                                            DrawableCompat.setTintMode(md_share, PorterDuff.Mode.SRC_IN);


                                            ImageButton imagebutton_time=findViewById(R.id.imageButton_time);
                                            ImageButton imagebutton_count=findViewById(R.id.imageButton_wordcount);

                                            int blackmix = blendARGB(selectedColor, Color.BLACK, 0.3f);

                                            TextView wordcount=findViewById(R.id.textView_wordcount);
                                            TextView readtime=findViewById(R.id.textView_time);
                                            wordcount.setTextColor(blackmix);
                                            readtime.setTextColor(blackmix);
                                            TextView stati=findViewById(R.id.textView_wordcount_static);
                                            TextView stati2=findViewById(R.id.textView_time_static);
                                            stati.setTextColor(blackmix);
                                            stati2.setTextColor(blackmix);
                                            Drawable drawablet = imagebutton_time.getDrawable();
                                            Drawable mdt = drawablet.mutate();
                                            mdt = DrawableCompat.wrap(mdt);
                                            DrawableCompat.setTint(mdt, blackmix);
                                            DrawableCompat.setTintMode(mdt, PorterDuff.Mode.SRC_IN);

                                            Drawable drawablec = imagebutton_count.getDrawable();
                                            Drawable mdc = drawablec.mutate();
                                            mdc = DrawableCompat.wrap(mdc);
                                            DrawableCompat.setTint(mdc, blackmix);
                                            DrawableCompat.setTintMode(mdc, PorterDuff.Mode.SRC_IN);
                                            // linearLayout_divider.setBackgroundColor(blackmix);

                                            }
                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(View_post.this)
                                                .load(imageUrl)
                                                .into(imageView, new Callback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        boolean hasDrawable = (imageView.getDrawable() != null);
                                                        if (hasDrawable) {
                                                            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                                                            Bitmap bitmap = drawable.getBitmap();
                                                            selectedColor = calculateAverageColor(bitmap, 1);
                                                            progressBar.setVisibility(View.GONE);
                                                            int Inversecolor = getContrastColor(selectedColor);
                                                            textView.setTextColor(Inversecolor);
                                                            textView1.setTextColor(Inversecolor);
                                                            cardView.setVisibility(View.VISIBLE);
                                                            textView.setVisibility(View.VISIBLE);
                                                            imagebutton_comment.setVisibility(View.VISIBLE);
                                                            imagebuttonlove.setVisibility(View.VISIBLE);
                                                            textView1.setVisibility(View.VISIBLE);
                                                            imagebuttonshare.setVisibility(View.VISIBLE);
                                                            cardauthor.setVisibility(View.VISIBLE);
                                                            int white = Color.WHITE;
                                                            int black = Color.BLACK;
                                                            int Trans = Color.TRANSPARENT;
                                                            int resultt = blendARGB(selectedColor, Trans, 0.2f);
                                                            textView.setBackgroundColor(resultt);

                                                            firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Position").child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()){
                                                                        nestedScrollView.scrollBy(0,dataSnapshot.getValue(Integer.class));
                                                                        //Toast.makeText(View_post.this,String.valueOf(dataSnapshot.getValue(Integer.class)),Toast.LENGTH_LONG).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                            int resultColor = blendARGB(selectedColor, white, 0.1f);
                                                            int resultColor1 = blendARGB(selectedColor, white, 0.5f);
                                                            int resultColor3 = blendARGB(selectedColor, black, 0.3f);
                                                            cardView.setCardBackgroundColor(resultColor);
                                                            SeekBar seekBar=findViewById(R.id.seekBar1);

                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                seekBar.setThumbTintList(ColorStateList.valueOf(resultColor));
                                                                seekBar.setProgressBackgroundTintList(ColorStateList.valueOf(resultColor));
                                                                seekBar.setProgressTintList(ColorStateList.valueOf(resultColor));
                                                            }
                                                            cardauthor.setCardBackgroundColor(resultColor);
                                                            int resultt2 = blendARGB(selectedColor, Trans, 0.6f);
                                                            //    toolbar.setBackgroundColor(resultt2);
                                                    /*linearLayout.setBackgroundColor(resultColor1);*/
                                                    /*scrollView.setBackgroundColor(resultColor1);*/
                                                            coordinatorLayout.setBackgroundColor(resultColor1);
                                                            if (Build.VERSION.SDK_INT >= 21) {
                                                                Window window = View_post.this.getWindow();
                                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                                window.setStatusBarColor(resultColor);
                                                                appBarLayout.setBackgroundColor(resultColor);
                                                                collapsingToolbarLayout.setStatusBarScrimColor(resultColor);
                                                            }
                                                            int blackmix1 = blendARGB(selectedColor, Color.BLACK, 0.3f);
                                                            ImageButton imageButton_dropdown=findViewById(R.id.imageButton_dropdown);


                                                            Drawable drawabled = imageButton_dropdown.getDrawable();
                                                            Drawable mdd = drawabled.mutate();
                                                            mdd = DrawableCompat.wrap(mdd);
                                                            DrawableCompat.setTint(mdd, blackmix1);
                                                            DrawableCompat.setTintMode(mdd, PorterDuff.Mode.SRC_IN);
                                                            Drawable drawablelove = imagebuttonlove.getDrawable();
                                                            Drawable md1 = drawablelove.mutate();
                                                            md1 = DrawableCompat.wrap(md1);
                                                            DrawableCompat.setTint(md1, blackmix1);
                                                            DrawableCompat.setTintMode(md1, PorterDuff.Mode.SRC_IN);
                                                            Drawable drawableview = imagebutton_comment.getDrawable();
                                                            Drawable md2 = drawableview.mutate();
                                                            md2 = DrawableCompat.wrap(md2);
                                                            DrawableCompat.setTint(md2, blackmix1);
                                                            DrawableCompat.setTintMode(md2, PorterDuff.Mode.SRC_IN);
                                                            Drawable drawableshare = imagebuttonshare.getDrawable();
                                                            Drawable md_share = drawableshare.mutate();
                                                            md_share = DrawableCompat.wrap(md_share);
                                                            DrawableCompat.setTint(md_share, blackmix1);
                                                            DrawableCompat.setTintMode(md_share, PorterDuff.Mode.SRC_IN);

                                                            ImageButton imagebutton_time=findViewById(R.id.imageButton_time);
                                                            ImageButton imagebutton_count=findViewById(R.id.imageButton_wordcount);

                                                            int blackmix = blendARGB(selectedColor, Color.BLACK, 0.3f);
                                                            TextView wordcount=findViewById(R.id.textView_wordcount);
                                                            TextView readtime=findViewById(R.id.textView_time);
                                                            wordcount.setTextColor(blackmix);
                                                            readtime.setTextColor(blackmix);
                                                            TextView stati=findViewById(R.id.textView_wordcount_static);
                                                            TextView stati2=findViewById(R.id.textView_time_static);
                                                            stati.setTextColor(blackmix);
                                                            stati2.setTextColor(blackmix);
                                                            Drawable drawablet = imagebutton_time.getDrawable();
                                                            Drawable mdt = drawablet.mutate();
                                                            mdt = DrawableCompat.wrap(mdt);
                                                            DrawableCompat.setTint(mdt, blackmix);
                                                            DrawableCompat.setTintMode(mdt, PorterDuff.Mode.SRC_IN);

                                                            Drawable drawablec = imagebutton_count.getDrawable();
                                                            Drawable mdc = drawablec.mutate();
                                                            mdc = DrawableCompat.wrap(mdc);
                                                            DrawableCompat.setTint(mdc, blackmix);
                                                            DrawableCompat.setTintMode(mdc, PorterDuff.Mode.SRC_IN);
                                                            // linearLayout_divider.setBackgroundColor(blackmix);


                                                        }
                                                    }

                                                    @Override
                                                    public void onError() {
                                                    }
                                                });
                                    }
                                });

                      //  FirebaseDatabase.getInstance().goOffline();
                    }else{
                        cardauthor.setVisibility(View.INVISIBLE);
                        imagebuttonshare.setVisibility(View.INVISIBLE);
                        cardView.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                        imagebuttonlove.setVisibility(View.INVISIBLE);
                        textView1.setVisibility(View.INVISIBLE);
                        delete_notice.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        imagebutton_comment.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });




        //do if shared
        imagebuttonshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshotq) {
                        if (dataSnapshotq.exists()) {
                            FirebaseDatabase.getInstance().goOnline();

                            databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot1) {
                                    if (!dataSnapshot1.child("UserId").getValue(String.class).equals(mAuth.getCurrentUser().getUid())) {
                                        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Share_dialog share_dialog = new Share_dialog(View_post.this, dataSnapshot.child("username").getValue(String.class), dataSnapshot.child("imageurl").getValue(String.class), PostKey);
                                                share_dialog.show();
                                                share_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialogInterface) {
                                                        FirebaseDatabase.getInstance().goOffline();
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
                        } else {
                            Toast.makeText(View_post.this, "You need to be logged in to share a post!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });
      imagebuttonlove.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshotq) {
                      if (dataSnapshotq.exists()){
                          FirebaseDatabase.getInstance().goOnline();
                          databaseReference.child(PostKey).child("UserId").addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(DataSnapshot dataSnapshot) {
                                  if (!dataSnapshot.getValue(String.class).equals(mAuth.getCurrentUser().getUid())) {
                                      if (!clicked) {
                                          clicked = true;
                                          imagebuttonlove.setImageResource(R.drawable.ic_favorite_like_24dp2);
                                          imagebuttonlove.setImageResource(R.drawable.ic_favorite_like_24dp2);
                                          int blende = blendARGB(selectedColor, Color.BLACK, 0.5f);
                                          Drawable drawablelove = imagebuttonlove.getDrawable();
                                          Drawable md1 = drawablelove.mutate();
                                          md1 = DrawableCompat.wrap(md1);
                                          DrawableCompat.setTint(md1, blende);
                                          DrawableCompat.setTintMode(md1, PorterDuff.Mode.SRC_IN);
                                          final DatabaseReference user = firebaseDatabase.child(mAuth.getCurrentUser().getUid());
                                          final DatabaseReference checklike = databaseReference.child(PostKey).child("hearts");
                                          final DatabaseReference loop = user.child("Favorite");
                                          user.child("Favorite").child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(DataSnapshot dataSnapshot) {
                                                  if (dataSnapshot.exists()) {
                                                      Toast.makeText(View_post.this, "You can't unlike a post", Toast.LENGTH_SHORT).show();
                                                  } else {
                                                      checklike.runTransaction(new Transaction.Handler() {
                                                          @Override
                                                          public Transaction.Result doTransaction(MutableData mutableData) {
                                                              if (mutableData.getValue() == null) {
                                                                  mutableData.setValue(1);
                                                              } else {
                                                                  int count = mutableData.getValue(Integer.class);
                                                                  if (count == 999) {
                                                                      databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                          @Override
                                                                          public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                              firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                  @Override
                                                                                  public void onDataChange(DataSnapshot dataSnapshot1) {
                                                                                      DatabaseReference notify = firebaseDatabase.child(dataSnapshot.child("UserId").getValue(String.class)).child("Notifications").push();
                                                                                      notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                                                      notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("action").setValue(dataSnapshot1.child("username").getValue(String.class) + " and 1000 others liked your story!");
                                                                                      notify.child("note").setValue("");
                                                                                      notify.child("Type").setValue("UserActivity");
                                                                                      notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("Seen").setValue(false);
                                                                                      notify.child("imageurl").setValue(dataSnapshot1.child("imageurl").getValue(String.class));
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
                                                                  if (count == 9999) {
                                                                      databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                          @Override
                                                                          public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                              firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                  @Override
                                                                                  public void onDataChange(DataSnapshot dataSnapshot1) {
                                                                                      DatabaseReference notify = firebaseDatabase.child(dataSnapshot.child("UserId").getValue(String.class)).child("Notifications").push();
                                                                                      notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                                                      notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("action").setValue(dataSnapshot1.child("username").getValue(String.class) + " and 10000 others liked your story!");
                                                                                      notify.child("note").setValue("");
                                                                                      notify.child("Type").setValue("UserActivity");
                                                                                      notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("Seen").setValue(false);
                                                                                      notify.child("imageurl").setValue(dataSnapshot1.child("imageurl").getValue(String.class));
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
                                                                  if (count == 99999) {
                                                                      databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                          @Override
                                                                          public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                              firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                  @Override
                                                                                  public void onDataChange(DataSnapshot dataSnapshot1) {
                                                                                      DatabaseReference notify = firebaseDatabase.child(dataSnapshot.child("UserId").getValue(String.class)).child("Notifications").push();
                                                                                      notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                                                      notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("action").setValue(dataSnapshot1.child("username").getValue(String.class) + " and 100000 others liked your story!");
                                                                                      notify.child("note").setValue("");
                                                                                      notify.child("Type").setValue("UserActivity");
                                                                                      notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("Seen").setValue(false);
                                                                                      notify.child("imageurl").setValue(dataSnapshot1.child("imageurl").getValue(String.class));
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
                                                                  if (count == 999999) {
                                                                      databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                          @Override
                                                                          public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                              firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                  @Override
                                                                                  public void onDataChange(DataSnapshot dataSnapshot1) {
                                                                                      DatabaseReference notify = firebaseDatabase.child(dataSnapshot.child("UserId").getValue(String.class)).child("Notifications").push();
                                                                                      notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                                                      notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("action").setValue(dataSnapshot1.child("username").getValue(String.class) + " and 1000000 others liked your story!");
                                                                                      notify.child("note").setValue("");
                                                                                      notify.child("Type").setValue("UserActivity");
                                                                                      notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("Seen").setValue(false);
                                                                                      notify.child("imageurl").setValue(dataSnapshot1.child("imageurl").getValue(String.class));
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
                                                                  if (count == 9999999) {
                                                                      databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                          @Override
                                                                          public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                              firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                  @Override
                                                                                  public void onDataChange(DataSnapshot dataSnapshot1) {
                                                                                      DatabaseReference notify = firebaseDatabase.child(dataSnapshot.child("UserId").getValue(String.class)).child("Notifications").push();
                                                                                      notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                                                      notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("action").setValue(dataSnapshot1.child("username").getValue(String.class) + " and 10000000 others liked your story!");
                                                                                      notify.child("note").setValue("");
                                                                                      notify.child("Type").setValue("UserActivity");
                                                                                      notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("Seen").setValue(false);
                                                                                      notify.child("imageurl").setValue(dataSnapshot1.child("imageurl").getValue(String.class));
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
                                                                  if (count == 99999999) {
                                                                      databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                          @Override
                                                                          public void onDataChange(final DataSnapshot dataSnapshot) {
                                                                              firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                  @Override
                                                                                  public void onDataChange(DataSnapshot dataSnapshot1) {
                                                                                      DatabaseReference notify = firebaseDatabase.child(dataSnapshot.child("UserId").getValue(String.class)).child("Notifications").push();
                                                                                      notify.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                                                      notify.child("UserId").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("action").setValue(dataSnapshot1.child("username").getValue(String.class) + " and 100000000 others liked your story!");
                                                                                      notify.child("note").setValue("");
                                                                                      notify.child("Type").setValue("UserActivity");
                                                                                      notify.child("key").setValue(mAuth.getCurrentUser().getUid());
                                                                                      notify.child("Seen").setValue(false);
                                                                                      notify.child("imageurl").setValue(dataSnapshot1.child("imageurl").getValue(String.class));
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
                                                                  mutableData.setValue(count + 1);
                                                              }
                                                              return Transaction.success(mutableData);

                                                          }

                                                          @Override
                                                          public void onComplete(final DatabaseError databaseError, boolean b,
                                                                                 DataSnapshot dataSnapshot) {
                                                              LayoutInflater inflater = getLayoutInflater();
                                                              View view = inflater.inflate(R.layout.heart,
                                                                      (ViewGroup) findViewById(R.id.relativeLayout1));

                                                              Toast toast = new Toast(View_post.this);
                                                              toast.setView(view);
                                                              toast.show();
                                                              databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                  @Override
                                                                  public void onDataChange(DataSnapshot dataSnapshot) {
                                                                      DatabaseReference store = loop.child(PostKey);
                                                                      store.child("timestamp").setValue(ServerValue.TIMESTAMP);
                                                                      store.child("image").setValue(dataSnapshot.child("image").getValue(String.class));
                                                                      store.child("Story").setValue(dataSnapshot.child("Story").getValue(String.class));
                                                                      store.child("UserId").setValue(dataSnapshot.child("UserId").getValue(String.class));
                                                                      store.child("imageicon").setValue(dataSnapshot.child("imageicon").getValue(String.class));
                                                                      store.child("postid").setValue(dataSnapshot.child("postid").getValue(String.class));
                                                                      store.child("textcolor").setValue(dataSnapshot.child("textcolor").getValue(Integer.class));
                                                                      store.child("title").setValue(dataSnapshot.child("title").getValue(String.class));
                                                                      store.child("username").setValue(dataSnapshot.child("username").getValue(String.class));
                                                                      store.child("time").setValue(dataSnapshot.child("time").getValue(String.class));
                                                                      store.child("Typeface").setValue(dataSnapshot.child("Typeface").getValue(String.class));
                                                                      store.child("TextGravity").setValue(dataSnapshot.child("TextGravity").getValue(String.class));
                                                                      FirebaseDatabase.getInstance().goOffline();
                                                                  }

                                                                  @Override
                                                                  public void onCancelled(DatabaseError databaseError) {

                                                                  }
                                                              });
                                                          }
                                                      });


                                                  }
                                              }

                                              @Override
                                              public void onCancelled(DatabaseError databaseError) {

                                              }
                                          });
                                      }
                                  } else {
                                      Toast.makeText(View_post.this, "You can't upvote your own post", Toast.LENGTH_SHORT).show();
                                  }
                              }

                              @Override
                              public void onCancelled(DatabaseError databaseError) {
                              }
                          });
                      }else{
                          Toast.makeText(View_post.this,"You need to be logged in to like a post!",Toast.LENGTH_SHORT).show();

                      }
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });

          }
      });

        cardauthor.setPreventCornerOverlap(false);

        cardauthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaplayer.isPlaying()){
                    mediaplayer.stop();
                }
                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            FirebaseDatabase.getInstance().goOnline();
                            Intent intent = new Intent(View_post.this, UserActivity_2.class);
                            intent.putExtra("Userprofile", PostKey);
                            intent.putExtra("ID", mAuth.getCurrentUser().getUid());
                            startActivity(intent);
                        }else {
                            FirebaseDatabase.getInstance().goOnline();
                            Intent intent = new Intent(View_post.this, UserActivity_2.class);
                            intent.putExtra("Userprofile", PostKey);
                            // intent.putExtra("ID", mAuth.getCurrentUser().getUid());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });





    }

    public void cancel(){
        mCancel = true;
    }

    private void commentbutton() {

        /*LinearLayoutManager mLayoutManager=(new LinearLayoutManager(View_post.this));
        recyclerView.setLayoutManager(mLayoutManager);
      String PostKey=getIntent().getExtras().getString("Userprofile");
        DatabaseReference commentdatabase=FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey).child("Comments");
        final FirebaseRecyclerAdapter<coment,Commentviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<coment, Commentviewholder>(

                coment.class,
                R.layout.comment_model,
                View_post.Commentviewholder.class,
                commentdatabase
        ) {

            protected void populateViewHolder(final Commentviewholder viewHolder, final coment model, int position) {

                final String postKey = getRef(position).getKey();
                viewHolder.setUser(model.getUsername());
                viewHolder.setComment(model.getComment());
                viewHolder.setTime(model.getTime());
                viewHolder.setImageurl(model.getImageurl());




            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.setAdapter(firebaseRecyclerAdapter);

            }
        });*/


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

    @Override
    protected void onDestroy() {
        super.onDestroy();

            mediaplayer.release();

        final  ImageView imageView=(ImageView)findViewById(R.id.imageView_view_post);
       unbindDrawables(imageView);

        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseDatabase.getInstance().goOnline();


    //   mediaplayer.reset();
        mediaplayer.release();
        final NestedScrollView nestedScrollView=findViewById(R.id.nested);
        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Position").child(PostKey).setValue(nestedScrollView.computeVerticalScrollOffset());


        final  ImageView imageView=(ImageView)findViewById(R.id.imageView_view_post);
        unbindDrawables(imageView);
        overridePendingTransition(R.anim.no_change,R.anim.slide_down);
    }

    @Override
    protected void onStop() {
        super.onStop();
       mediaplayer.release();
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
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
    }


    private void initControls()
    {
        try
        {
            volumeSeekbar = findViewById(R.id.seekBar1);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static class Commentviewholder extends RecyclerView.ViewHolder {

        View mView;
        TextView commentname;
        TextView commenit;
        ImageView commentimage;
        TextView times;



        public Commentviewholder(View itemView) {
            super(itemView);
            mView = itemView;
         commentname=(TextView)mView.findViewById(R.id.textView_commentusername);
            times=(TextView)mView.findViewById(R.id.textView_commenttime);
            commenit =(TextView)mView.findViewById(R.id.coment);
            commentimage=(ImageView)mView.findViewById(R.id.imageview_commentimage);

        }

        public void setComment(String comment) {
            commenit.setText(comment);
        } /* only business studies left i think i got 4 As friday 10 november 2017/correction 2,i fcked up real bad tuesday December 5th,/* Hey future me, if you'er reading this, it means this app succeedd, also learn how to spell suceessed,seriously!*/

        public void setTime(String time) {
            times.setText(time);
        }

        public void setImageurl(String imageurl) {
            Glide.with(mView.getContext()).load(imageurl).into(commentimage);
        }

        public void setUser(String user) {
            commentname.setText(user);
        }
    }
    /* else {
                                          checklike.runTransaction(new Transaction.Handler() {
                                              @Override
                                              public Transaction.Result doTransaction(MutableData mutableData) {
                                                  if (mutableData.getValue() == null) {
                                                      mutableData.setValue(1);
                                                  } else {
                                                      int count = mutableData.getValue(Integer.class);
                                                      mutableData.setValue(count + 1);
                                                  }
                                                  return Transaction.success(mutableData);
                                              }

                                              @Override
                                              public void onComplete(DatabaseError databaseError, boolean b,
                                                                     DataSnapshot dataSnapshot) {
                                                  LayoutInflater inflater = getLayoutInflater();
                                                  View view = inflater.inflate(R.layout.heart,
                                                          (ViewGroup) findViewById(R.id.relativeLayout1));

                                                  Toast toast = new Toast(View_post.this);
                                                  toast.setView(view);
                                                  toast.show();
                                                  imagebuttonlove.setImageResource(R.drawable.ic_favorite_like_24dp2);
                                                  int blende = blendARGB(selectedColor, Color.BLACK, 0.5f);
                                                  Drawable drawablelove = imagebuttonlove.getDrawable();
                                                  Drawable md1 = drawablelove.mutate();
                                                  md1 = DrawableCompat.wrap(md1);
                                                  DrawableCompat.setTint(md1, blende);
                                                  DrawableCompat.setTintMode(md1, PorterDuff.Mode.SRC_IN);
                                                  databaseReference.child(PostKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                          DatabaseReference store = loop.child(PostKey);
                                                          store.child("image").setValue(dataSnapshot.child("image").getValue(String.class));
                                                          store.child("Story").setValue(dataSnapshot.child("Story").getValue(String.class));
                                                          store.child("UserId").setValue(dataSnapshot.child("UserId").getValue(String.class));
                                                          store.child("imageicon").setValue(dataSnapshot.child("imageicon").getValue(String.class));
                                                          store.child("postid").setValue(dataSnapshot.child("postid").getValue(String.class));
                                                          store.child("textcolor").setValue(dataSnapshot.child("textcolor").getValue(Integer.class));
                                                          store.child("title").setValue(dataSnapshot.child("title").getValue(String.class));
                                                          store.child("username").setValue(dataSnapshot.child("username").getValue(String.class)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                              @Override
                                                              public void onSuccess(Void aVoid) {
                                                                  FirebaseDatabase.getInstance().goOffline();
                                                              }
                                                          });
                                                      }

                                                      @Override
                                                      public void onCancelled(DatabaseError databaseError) {
                                                      }
                                                  });
                                              }
                                          });


                                      }*/
}


