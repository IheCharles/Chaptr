package com.dev.textnet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
 * Created by Dell on 2018/02/21.
 */

public class SearchResult extends AppCompatActivity {
    DatabaseReference firebaseDatabase;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<SeachModel,SearchResult.Postviewholder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchtitle);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        final String query=getIntent().getExtras().getString("Query");
        recyclerView =  findViewById(R.id.recycleview_searchresult);
        recyclerView.setHasFixedSize(true);
        HPLinearLayoutManager mLayoutManager = (new HPLinearLayoutManager(SearchResult.this, LinearLayoutManager.VERTICAL, false));
        //   recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setLayoutManager(mLayoutManager);
        final Toolbar toolbar = findViewById(R.id.toolbar_main_searchtitle);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(null);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseRecyclerAdapter.cleanup();
                recyclerView.getRecycledViewPool().clear();
                onBackPressed();
                //ma broda
            }
        });
        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("color").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                int resultColor = blendARGB(dataSnapshot.getValue(Integer.class), Color.BLACK, 0.5f);
                toolbar.setBackgroundColor(resultColor);
                if (Build.VERSION.SDK_INT >= 21) {
                    int whit = Color.BLACK;
                    int resultColor1 = blendARGB(dataSnapshot.getValue(Integer.class), whit, 0.6f);
                    Window window = SearchResult.this.getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(resultColor1);
                }
            }else{
                toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.Pale_Orange,null));
                if (Build.VERSION.SDK_INT >= 21) {
                    int whit = Color.BLACK;
                   // int resultColor1 = blendARGB(dataSnapshot.getValue(Integer.class), whit, 0.6f);
                    Window window = SearchResult.this.getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.Pale_Yellow,null));
                }
            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // final TextView textview=findViewById(R.id.textView_searchtitle);
       Load(query);

    }

    private void Load(String query) {
        Query query1=firebaseDatabase.orderByChild("username").equalTo(query);



        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SeachModel, SearchResult.Postviewholder>(
                SeachModel.class,
                R.layout.search_model,
                SearchResult.Postviewholder.class,
                query1
        ) {


            protected void populateViewHolder(final SearchResult.Postviewholder viewHolder, SeachModel model, final int position) {



                final String postKey = getRef(position).getKey();
                viewHolder.setCircleImage(SearchResult.this,model.getImageurl());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setFollowerNo(model.getFollowerNo());
                viewHolder.setPostCount(model.getPostCount());

                viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(SearchResult.this, UserActivity_3.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        searchView.setIconified(false);
     //   String query=getIntent().getExtras().getString("Query");

        searchView.setQueryHint("Username");
     //   searchView.setQuery(query,true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                // Toast like print
             //   Toast.makeText(SearchResult.this,"submit",Toast.LENGTH_SHORT).show();
                Load(query);
                searchView.setIconified(false);
                searchView.clearFocus();

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
    public static class Postviewholder extends RecyclerView.ViewHolder {
        View mView;
        ImageView imageicon;
        ImageView image;
        RelativeLayout relativeLayout;
        TextView followNo;
        TextView Postcount;
        TextView status;
        public Postviewholder(View itemView) {
            super(itemView);
            mView = itemView;
            relativeLayout=mView.findViewById(R.id.worst);
            followNo=mView.findViewById(R.id.textView_searchmodel_follower);
            Postcount=mView.findViewById(R.id.textView_searchmodel_posts);
            status=mView.findViewById(R.id.textView_searchmodel_status);

        }


        public void setUsername(String username) {
            TextView textView=mView.findViewById(R.id.textView_allfollowers_model);
            textView.setText(username);
        }

        public void setCircleImage(Context ctx, String circleImage) {
            image=mView.findViewById(R.id.imageview_allfollowers_model);
            Glide.with(ctx).load(circleImage).into(image);
        }


        public void setStatus(String stats) {
            status=mView.findViewById(R.id.textView_searchmodel_status);
            status.setText(stats);
        }

        public void setFollowerNo(int followerNo) {
            followNo=mView.findViewById(R.id.textView_searchmodel_follower);
            followNo.setText(String.valueOf(followerNo));
        }

        public void setPostCount(int postCount) {
            Postcount=mView.findViewById(R.id.textView_searchmodel_posts);
            Postcount.setText(String.valueOf(postCount));
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
}
