package com.dev.textnet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.textnet.Userpro.allfollowers_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.support.v4.graphics.ColorUtils.blendARGB;

/**
 * Created by Dell on 2017/09/28.
 */

public class All_followers extends AppCompatActivity {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private  RecyclerView recyclerView1;
    DatabaseReference firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<allfollowers_model,All_followers.Postviewholder> firebaseRecyclerAdapter;

    int preTotal=0;
    int ItemCount;
    int TotalItemCount;
    int firstItem;
    private  int prevTotal=0;
    static int count=4;
    int visthresh=4;
    static int Posit;
    static Query circlequery;
    static int       Posit2;
    private Boolean loading=true;
    private String OldestPostId;
    String RV_POS_INDEX="Index";
    String RV_POS_INDEX2="Index";
    private int currentPage = 0;
    String RV_TOP_VIEW="TopView";
    String RV_TOP_VIEW2="TopView";
    private int previousTotalItemCount = 0;
    static int mRvTopView;
    static int mRvTopView2;
    static SharedPreferences sharedPreferences2;

    private int startingPageIndex = 0;

    Boolean Stick=true;
    static String oldPostKey="";
    int loadmore=100;
    String ChildKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewallfollowers);

        recyclerView=findViewById(R.id.viewallfollower_recyclerview);
        recyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mLayoutManager=(new LinearLayoutManager(All_followers.this,LinearLayoutManager.VERTICAL,true));
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        int Colour=getIntent().getExtras().getInt("Color");
        if (Build.VERSION.SDK_INT >= 21){
            int white = Color.BLACK;
            int resultColor = blendARGB(Colour, white, 0.1f);
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(resultColor);

        }
        loadfirst();
        final DatabaseReference imageupdater= firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow");
        imageupdater.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        DatabaseReference picupdate=firebaseDatabase.child(snapshot.getKey());
                        picupdate.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String url=dataSnapshot.child("imageurl").getValue(String.class);
                                //int color=dataSnapshot.child("color").getValue(Integer.class);
                                imageupdater.child(snapshot.getKey()).child("CircleImage").setValue(url);
                               // imageupdater.child(snapshot.getKey()).child("color").setValue(color);
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

        final Toolbar toolbar =findViewById(R.id.toolbar_viewallfollower);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                recyclerView.getRecycledViewPool().clear();
                onBackPressed();
            }
        });
        toolbar.setBackgroundColor(Colour);

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstItem=mLayoutManager.findLastVisibleItemPosition();
                ItemCount=recyclerView.getChildCount();
                TotalItemCount=mLayoutManager.getItemCount();
                int lastVisibleItemPosition = 0;
                if (mLayoutManager instanceof LinearLayoutManager) {
                    lastVisibleItemPosition = ( mLayoutManager).findLastVisibleItemPosition();
                }
                if (TotalItemCount < previousTotalItemCount){
                    currentPage=startingPageIndex;
                    previousTotalItemCount=TotalItemCount;
                    if (TotalItemCount == 0) {
                        loading = true;
                    }
                }
                if (loading && (TotalItemCount > previousTotalItemCount)) {
                    loading = false;
                    previousTotalItemCount = TotalItemCount;
                }
                if (!loading && (lastVisibleItemPosition + visthresh) > TotalItemCount) {
                    currentPage++;
                    Posit2 = mLayoutManager.findFirstVisibleItemPosition();
                    View startView2 = recyclerView.getChildAt(0);
                    mRvTopView2 = (startView2 == null) ? 0 : (startView2.getTop() - recyclerView.getPaddingTop());
                    sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(All_followers.this);
                    SharedPreferences.Editor editor2=sharedPreferences2.edit();
                    editor2.putInt(RV_POS_INDEX2,Posit2);
                    editor2.putInt(RV_TOP_VIEW2,mRvTopView2);
                    editor2.apply();
                    loading=true;
                    load(ChildKey);
                }

            }
        });*/



    }


    public void loadfirst(){
        DatabaseReference follow= firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow");
        Query jew=follow.orderByChild("timestamp");

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<allfollowers_model, All_followers.Postviewholder>(
                allfollowers_model.class,
                R.layout.allfollowers_model,
                All_followers.Postviewholder.class,
                jew
        ) {


            protected void populateViewHolder(final All_followers.Postviewholder viewHolder, allfollowers_model model, final int position) {



                final String postKey = getRef(position).getKey();
                viewHolder.setCircleImage(All_followers.this,model.getCircleImage());
                viewHolder.setUsername(model.getUsername());

                viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(All_followers.this, UserActivity_3.class);
                        intent.putExtra("Userprofil",postKey);
                        intent.putExtra("Activity","U1");
                        startActivity(intent);
                    }
                });


            }
        };
        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.setAdapter(firebaseRecyclerAdapter);

            }
        });
    }
    public void lodad(String childKey){
        DatabaseReference follow= firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow");
        loadmore=loadmore+50;
        Query jew=follow.limitToLast(loadmore).orderByChild("timestamp").endAt(childKey);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<allfollowers_model, All_followers.Postviewholder>(
                allfollowers_model.class,
                R.layout.allfollowers_model,
                All_followers.Postviewholder.class,
                jew
        ) {


            protected void populateViewHolder(final All_followers.Postviewholder viewHolder, allfollowers_model model, final int position) {



                final String postKey = getRef(position).getKey();
                viewHolder.setCircleImage(All_followers.this,model.getCircleImage());
                viewHolder.setUsername(model.getUsername());

                viewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(All_followers.this, UserActivity_3.class);
                        intent.putExtra("Userprofil",postKey);
                        intent.putExtra("Activity","U1");
                        startActivity(intent);
                    }
                });


            }
        };
        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.setAdapter(firebaseRecyclerAdapter);

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.cleanup();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.allfollowing_menu, menu);
        return true;
    }

    public static class Postviewholder extends RecyclerView.ViewHolder {
        View mView;
        ImageView imageicon;
        ImageView image;
        RelativeLayout relativeLayout;
        public Postviewholder(View itemView) {
            super(itemView);
            mView = itemView;
            relativeLayout=mView.findViewById(R.id.worst);

        }


        public void setUsername(String username) {
            TextView textView=mView.findViewById(R.id.textView_allfollowers_model);
            textView.setText(username);
        }

        public void setCircleImage(Context ctx,String circleImage) {
             image=mView.findViewById(R.id.imageview_allfollowers_model);
            Glide.with(ctx).load(circleImage).into(image);
        }


    }
}
