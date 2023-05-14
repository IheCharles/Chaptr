package com.dev.textnet.posts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.textnet.Cards;
import com.dev.textnet.CustomImageSizeGlideModule;
import com.dev.textnet.HPLinearLayoutManager;
import com.dev.textnet.Not_Availble;
import com.dev.textnet.R;
import com.dev.textnet.UserActivity_2;
import com.dev.textnet.View_post;
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
 * Created by Dell on 2017/06/21.
 */

public class Card_view extends Fragment  {
    private HPLinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    private boolean ProcessUpvote=false;
    private FirebaseAuth mAuth;
    private String REC;


    String ChildKey;
    int preTotal=0;
    int ItemCount;
    Boolean connect=false;
    int TotalItemCount;
    int firstItem;
    private  int prevTotal=0;
    static int count=40;
    int visthresh=2;
    static int Posit;
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
    static SharedPreferences sharedPreferences;
    static SharedPreferences sharedPreferences2;
    int  counter=0;
    static Query query;
    private int startingPageIndex = 0;
    SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Boolean Stick=true;
    Boolean LoggedIn=false;
   FirebaseRecyclerAdapter<Cards,Card_view.Postviewholder>  firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<Cards,Card_view.Postviewholder>  firebaseRecyclerAdapter_tablet;

    private boolean Break=true;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.card_view,container,false);


        mDatabase= FirebaseDatabase.getInstance().getReference().child("Posts");

        mAuth=FirebaseAuth.getInstance();
        recyclerView =  view.findViewById(R.id.recycleview_new);


        recyclerView.setHasFixedSize(true);

        RelativeLayout relativeLayout=getActivity().findViewById(R.id.relativeview_main);
        relativeLayout.setVisibility(View.GONE);
        mLayoutManager=(new HPLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
     //   recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setLayoutManager(mLayoutManager);


        swipeRefreshLayout=view.findViewById(R.id.swiper);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firstload();
            }

        });
      /*  DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                  Toast.makeText(getActivity(),"poll",Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("not connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });*/




        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("color").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (isAdded()) {
                        swipeRefreshLayout.setColorSchemeColors(dataSnapshot.getValue(Integer.class));
                    }
                }else{
                    if (isAdded()) {
                        swipeRefreshLayout.setColorSchemeColors(ResourcesCompat.getColor(getResources(), R.color.Pale_Orange, null));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



      //  query = mDatabase.limitToLast(count);

      //  final Query key=mDatabase.limitToLast(count);

      /*  recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                key.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap :dataSnapshot.getChildren()){

                            counter=counter+1;
                            if (counter==1){

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });*/

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }


        firstload();






      /*  if (!connect) {
            DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = dataSnapshot.getValue(Boolean.class);
                    if (connected) {

                        firstload();
                        connect=true;

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/


       /* DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.e("Check","connected");
                } else {
                    Log.e("Check","Disconnected");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });*/







        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstItem=mLayoutManager.findLastVisibleItemPosition();
                ItemCount=recyclerView.getChildCount();
                TotalItemCount=mLayoutManager.getItemCount();
                int lastVisibleItemPosition = 0;
                if (mLayoutManager instanceof LinearLayoutManager) {
                    lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
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
                    //currentPage++;
                    Posit2 = mLayoutManager.findFirstVisibleItemPosition();
                    View startView2 = recyclerView.getChildAt(0);
                    mRvTopView2 = (startView2 == null) ? 0 : (startView2.getTop() - recyclerView.getPaddingTop());
                    sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor2=sharedPreferences2.edit();
                    editor2.putInt(RV_POS_INDEX2,Posit2);
                    editor2.putInt(RV_TOP_VIEW2,mRvTopView2);
                    editor2.apply();
                    loading=true;
                    Load(ChildKey);


                }
            }
        });


        return view;
    }

    private void Loadtab(String childKey) {
        count=count+30;
        //  Toast.makeText(getActivity(),"F", Toast.LENGTH_SHORT).show();

        query=mDatabase.orderByChild("postid").limitToLast(count).endAt(childKey);



        firebaseRecyclerAdapter  = new FirebaseRecyclerAdapter<Cards, Card_view.Postviewholder>(

                Cards.class,
                R.layout.cards_tablet,

                Card_view.Postviewholder.class,
                query


        ) {

            @Override
            public Cards getItem(int position) {
                return super.getItem(getItemCount() -1-position);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(getItemCount() -1-position);
            }

            @Override
            public DatabaseReference getRef(int position) {
                return super.getRef(getItemCount() -1-position);
            }


            @Override
            protected void populateViewHolder(final Card_view.Postviewholder viewHolder, Cards model, final int position) {


                final String postKey=getRef(position).getKey();

                viewHolder.setUsername(model.getUsername()); /* im getting so close im gong to be fucking rich bitch, if not im killing my self*/
                viewHolder.setImage(getActivity(),model.getImage());
                viewHolder.setimage_icon(getActivity(),model.getImageicon());
                viewHolder.setTitle(getActivity(),model.getTitle(),model.getTypeface(),model.getStory());
                viewHolder.setTextColor(model.getTextcolor());
                viewHolder.setHearts(model.getHearts());
                viewHolder.setViews(model.getViews());
                viewHolder.setTime(model.getTime());

                viewHolder.imageButton.setVisibility(View.GONE);

                viewHolder.post_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent2= new Intent(getActivity(),View_post.class);
                        intent2.putExtra("Userprofile",postKey);
                        startActivity(intent2);

                    }
                });

                viewHolder.imageicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnaapshot) {
                                        if (dataSnaapshot.exists()){
                                            if (dataSnapshot.exists()) {
                                                Intent intent2 = new Intent(getActivity(), UserActivity_2.class);
                                                intent2.putExtra("Userprofile", postKey);
                                                intent2.putExtra("ID", mAuth.getCurrentUser().getUid());
                                                startActivity(intent2);
                                            } else {
                                                Intent intent2 = new Intent(getActivity(), Not_Availble.class);
                                                startActivity(intent2);
                                            }
                                        }else {
                                            if (dataSnapshot.exists()) {
                                                Intent intent2 = new Intent(getActivity(), UserActivity_2.class);
                                                intent2.putExtra("Userprofile", postKey);
                                                //   intent2.putExtra("ID", mAuth.getCurrentUser().getUid());
                                                startActivity(intent2);
                                            } else {
                                                Intent intent2 = new Intent(getActivity(), Not_Availble.class);
                                                startActivity(intent2);
                                            }
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
                });





            }

        };


        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.notifyDataSetChanged();
            }
        });


        if (sharedPreferences2 != null) {

            SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getActivity());

            Posit2 = (preferences2.getInt(RV_POS_INDEX2, 0));
            mRvTopView2 = (preferences2.getInt(RV_TOP_VIEW2, 0));
            firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                public void onItemRangeInserted(int positionStart, int itemCount) {

                    mLayoutManager.scrollToPositionWithOffset(Posit2, mRvTopView2);
                }

            });


        }


    }

    private void tabletLoad() {
        // Toast.makeText(getActivity(),"poll",Toast.LENGTH_SHORT).show();
        Posit = mLayoutManager.findFirstVisibleItemPosition();
        View startView = recyclerView.getChildAt(0);
        mRvTopView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());
        sharedPreferences  =PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(RV_POS_INDEX,Posit);
        editor.putInt(RV_TOP_VIEW,mRvTopView);
        editor.apply();

        query = mDatabase.orderByChild("postid").limitToLast(40);
        query.keepSynced(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int c=0;
                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    c=c+1;
                    if (c==dataSnapshot.getChildrenCount()){
                        ChildKey= dataSnapshot1.getKey();
                        /*if (getActivity()!=null) {
                            Toast.makeText(getActivity(), dataSnapshot1.child("title").getValue(String.class), Toast.LENGTH_SHORT).show();
                        }*/

                        query = mDatabase.orderByChild("postid").limitToLast(20).endAt(ChildKey);
                        firebaseRecyclerAdapter  = new FirebaseRecyclerAdapter<Cards, Card_view.Postviewholder>(
                                Cards.class,
                                R.layout.cards_tablet,
                                Card_view.Postviewholder.class,
                                query

                        ) {
                            @Override
                            public Cards getItem(int position) {
                                return super.getItem(getItemCount() -1-position);
                            }

                            @Override
                            public int getItemCount() {
                                return super.getItemCount();
                            }
                            @Override
                            public long getItemId(int position) {
                                return super.getItemId(getItemCount() -1-position);
                            }

                            @Override
                            public DatabaseReference getRef(int position) {
                                return super.getRef(getItemCount() -1-position);
                            }


                            @Override
                            protected void populateViewHolder(final Card_view.Postviewholder viewHolder, Cards model, final int position) {





                                final String postKey=getRef(position).getKey();

                                viewHolder.setUsername(model.getUsername());
                                viewHolder.setImage(getActivity(),model.getImage());
                                viewHolder.setimage_icon(getActivity(),model.getImageicon());
                                viewHolder.setTitle(getActivity(),model.getTitle(),model.getTypeface(),model.getStory());
                                viewHolder.setTextColor(model.getTextcolor());
                                viewHolder.setHearts(model.getHearts());
                                viewHolder.setViews(model.getViews());
                                viewHolder.setTime(model.getTime());



                                viewHolder.post_Image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent2= new Intent(getActivity(),View_post.class);
                                        intent2.putExtra("Userprofile",postKey);
                                        startActivity(intent2);

                                    }
                                });

                                viewHolder.imageicon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapsho3t) {
                                                        if (dataSnapsho3t.exists()){
                                                            if (dataSnapshot.exists()) {
                                                                Intent intent2 = new Intent(getActivity(), UserActivity_2.class);
                                                                intent2.putExtra("Userprofile", postKey);
                                                                intent2.putExtra("ID", mAuth.getCurrentUser().getUid());
                                                                startActivity(intent2);
                                                            } else {
                                                                Intent intent2 = new Intent(getActivity(), Not_Availble.class);
                                                                startActivity(intent2);
                                                            }
                                                        }else{
                                                            if (dataSnapshot.exists()) {
                                                                Intent intent2 = new Intent(getActivity(), UserActivity_2.class);
                                                                intent2.putExtra("Userprofile", postKey);
                                                                // intent2.putExtra("ID", mAuth.getCurrentUser().getUid());
                                                                startActivity(intent2);
                                                            } else {
                                                                Intent intent2 = new Intent(getActivity(), Not_Availble.class);
                                                                startActivity(intent2);
                                                            }
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
                                });



                                viewHolder.imageButton.setVisibility(View.GONE);

                            }

                        };

                        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                recyclerView.setAdapter(firebaseRecyclerAdapter);
                                firebaseRecyclerAdapter.notifyDataSetChanged();
                            }
                        });



                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Stick=false;
        swipeRefreshLayout.setRefreshing(false);
        previousTotalItemCount=0;
        //  Load(ChildKey);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Posit = mLayoutManager.findFirstVisibleItemPosition();
        View startView = recyclerView.getChildAt(0);

        mRvTopView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());


        sharedPreferences  =PreferenceManager.getDefaultSharedPreferences(getActivity());

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putInt(RV_POS_INDEX,Posit);
        editor.putInt(RV_TOP_VIEW,mRvTopView);
        editor.apply();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Posit = mLayoutManager.findFirstVisibleItemPosition();
        View startView = recyclerView.getChildAt(0);
        mRvTopView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());
        sharedPreferences  =PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(RV_POS_INDEX,Posit);
        editor.putInt(RV_TOP_VIEW,mRvTopView);
        editor.apply();

    }



    private void firstload(){
       // Toast.makeText(getActivity(),"poll",Toast.LENGTH_SHORT).show();
        Posit = mLayoutManager.findFirstVisibleItemPosition();
        View startView = recyclerView.getChildAt(0);
        mRvTopView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());
        sharedPreferences  =PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(RV_POS_INDEX,Posit);
        editor.putInt(RV_TOP_VIEW,mRvTopView);
        editor.apply();

        query = mDatabase.orderByChild("postid").limitToLast(40);
        query.keepSynced(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int c=0;
                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    c=c+1;
                    if (c==dataSnapshot.getChildrenCount()){
                         ChildKey= dataSnapshot1.getKey();
                        /*if (getActivity()!=null) {
                            Toast.makeText(getActivity(), dataSnapshot1.child("title").getValue(String.class), Toast.LENGTH_SHORT).show();
                        }*/

                        query = mDatabase.orderByChild("postid").limitToLast(20).endAt(ChildKey);
                        firebaseRecyclerAdapter  = new FirebaseRecyclerAdapter<Cards, Card_view.Postviewholder>(
                                Cards.class,
                                R.layout.cards,
                                Card_view.Postviewholder.class,
                                query

                        ) {
                            @Override
                            public Cards getItem(int position) {
                                return super.getItem(getItemCount() -1-position);
                            }

                            @Override
                            public int getItemCount() {
                                return super.getItemCount();
                            }
                            @Override
                            public long getItemId(int position) {
                                return super.getItemId(getItemCount() -1-position);
                            }

                            @Override
                            public DatabaseReference getRef(int position) {
                                return super.getRef(getItemCount() -1-position);
                            }


                            @Override
                            protected void populateViewHolder(final Card_view.Postviewholder viewHolder, Cards model, final int position) {





                                final String postKey=getRef(position).getKey();

                                viewHolder.setUsername(model.getUsername());
                                viewHolder.setImage(getActivity(),model.getImage());
                                viewHolder.setimage_icon(getActivity(),model.getImageicon());
                                viewHolder.setTitle(getActivity(),model.getTitle(),model.getTypeface(),model.getStory());
                                viewHolder.setTextColor(model.getTextcolor());
                                viewHolder.setHearts(model.getHearts());
                                viewHolder.setViews(model.getViews());
                                viewHolder.setTime(model.getTime());



                                viewHolder.post_Image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent2= new Intent(getActivity(),View_post.class);
                                        intent2.putExtra("Userprofile",postKey);
                                        startActivity(intent2);

                                    }
                                });

                                viewHolder.imageicon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapsho3t) {
                                                        if (dataSnapsho3t.exists()){
                                                            if (dataSnapshot.exists()) {
                                                                Intent intent2 = new Intent(getActivity(), UserActivity_2.class);
                                                                intent2.putExtra("Userprofile", postKey);
                                                                intent2.putExtra("ID", mAuth.getCurrentUser().getUid());
                                                                startActivity(intent2);
                                                            } else {
                                                                Intent intent2 = new Intent(getActivity(), Not_Availble.class);
                                                                startActivity(intent2);
                                                            }
                                                        }else{
                                                            if (dataSnapshot.exists()) {
                                                                Intent intent2 = new Intent(getActivity(), UserActivity_2.class);
                                                                intent2.putExtra("Userprofile", postKey);
                                                                // intent2.putExtra("ID", mAuth.getCurrentUser().getUid());
                                                                startActivity(intent2);
                                                            } else {
                                                                Intent intent2 = new Intent(getActivity(), Not_Availble.class);
                                                                startActivity(intent2);
                                                            }
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
                                });



                                viewHolder.imageButton.setVisibility(View.GONE);

                            }

                        };

                        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            public void onItemRangeInserted(int positionStart, int itemCount) {
                                recyclerView.setAdapter(firebaseRecyclerAdapter);

                                firebaseRecyclerAdapter.notifyDataSetChanged();
                            }
                        });



                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Stick=false;
        swipeRefreshLayout.setRefreshing(false);
        previousTotalItemCount=0;
      //  Load(ChildKey);

    }



    private void Load(String childKey) {
        count=count+30;
      //  Toast.makeText(getActivity(),"F", Toast.LENGTH_SHORT).show();

        query=mDatabase.orderByChild("postid").limitToLast(count).endAt(childKey);



        firebaseRecyclerAdapter  = new FirebaseRecyclerAdapter<Cards, Card_view.Postviewholder>(

                Cards.class,
                R.layout.cards,

                Card_view.Postviewholder.class,
                query


        ) {

            @Override
            public Cards getItem(int position) {
                return super.getItem(getItemCount() -1-position);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(getItemCount() -1-position);
            }

            @Override
            public DatabaseReference getRef(int position) {
                return super.getRef(getItemCount() -1-position);
            }


            @Override
            protected void populateViewHolder(final Card_view.Postviewholder viewHolder, Cards model, final int position) {


                final String postKey=getRef(position).getKey();

                viewHolder.setUsername(model.getUsername()); /* im getting so close im gong to be fucking rich bitch, if not im killing my self*/
                viewHolder.setImage(getActivity(),model.getImage());
                viewHolder.setimage_icon(getActivity(),model.getImageicon());
                viewHolder.setTitle(getActivity(),model.getTitle(),model.getTypeface(),model.getStory());
                viewHolder.setTextColor(model.getTextcolor());
                viewHolder.setHearts(model.getHearts());
                viewHolder.setViews(model.getViews());
                viewHolder.setTime(model.getTime());

                viewHolder.imageButton.setVisibility(View.GONE);

                viewHolder.post_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent2= new Intent(getActivity(),View_post.class);
                        intent2.putExtra("Userprofile",postKey);
                        startActivity(intent2);

                    }
                });

                viewHolder.imageicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnaapshot) {
                                        if (dataSnaapshot.exists()){
                                            if (dataSnapshot.exists()) {
                                                Intent intent2 = new Intent(getActivity(), UserActivity_2.class);
                                                intent2.putExtra("Userprofile", postKey);
                                                intent2.putExtra("ID", mAuth.getCurrentUser().getUid());
                                                startActivity(intent2);
                                            } else {
                                                Intent intent2 = new Intent(getActivity(), Not_Availble.class);
                                                startActivity(intent2);
                                            }
                                        }else {
                                            if (dataSnapshot.exists()) {
                                                Intent intent2 = new Intent(getActivity(), UserActivity_2.class);
                                                intent2.putExtra("Userprofile", postKey);
                                                //   intent2.putExtra("ID", mAuth.getCurrentUser().getUid());
                                                startActivity(intent2);
                                            } else {
                                                Intent intent2 = new Intent(getActivity(), Not_Availble.class);
                                                startActivity(intent2);
                                            }
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
                });





            }

        };


        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.notifyDataSetChanged();
            }
        });


        if (sharedPreferences2 != null) {

            SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getActivity());

            Posit2 = (preferences2.getInt(RV_POS_INDEX2, 0));
            mRvTopView2 = (preferences2.getInt(RV_TOP_VIEW2, 0));
            firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                public void onItemRangeInserted(int positionStart, int itemCount) {

                    mLayoutManager.scrollToPositionWithOffset(Posit2, mRvTopView2);
                }

            });


        }



    }



    public    static class Postviewholder extends RecyclerView.ViewHolder {

        View mView;
        ImageView upvote;
        ImageView imageicon;
        ImageView post_Image;
        private DatabaseReference mDatabaseUpvote;
      ImageButton imageButton;
      ImageButton imageButton2;
        private FirebaseAuth mAuth;
      TextView upvotecounter;
      TextView eyecount;

        public Postviewholder(View itemView) {
            super(itemView);
            mView = itemView;
             imageButton=mView.findViewById(R.id.imageButtondelete);
            imageButton2=mView.findViewById(R.id.imageButton_views);
            imageicon =  mView.findViewById(R.id.Post_imiageicon);
            post_Image =  mView.findViewById(R.id.CardImage);
            upvotecounter=mView.findViewById(R.id.upvote_counter);
            eyecount=mView.findViewById(R.id.textView_eyescounter);
            upvote =  mView.findViewById(R.id.upvotec);

        }

      public    void setImage(final Context ctx, final String image) {
          CustomImageSizeGlideModule.CustomImageSizeModel customImageRequest1 = new CustomImageSizeGlideModule.CustomImageSizeModelFutureStudio(image);
            final ImageView imageView = mView.findViewById(R.id.CardImage);
            Glide.with(ctx)
                    .load(image)

                    .into(imageView);

        }
        public void setTitle(Context ctx,String title,String texttype,String story) {
            if (story != null && mView.findViewById(R.id.cardPreview)!=null){
                TextView preview=mView.findViewById(R.id.cardPreview);
                preview.setText(story);
                if (texttype.equals("MONOSPACE")) {
                    preview.setTypeface(android.graphics.Typeface.MONOSPACE);
                }
                if (texttype.equals("NORMAL")) {
                    preview.setTypeface(android.graphics.Typeface.DEFAULT);
                }
                if (texttype.equals("SERIF")) {
                    preview.setTypeface(android.graphics.Typeface.SERIF);
                }
                if (texttype.equals("CURSIVE")) {
                    android.graphics.Typeface typeface = android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/dancing-script.regular.ttf");
                    preview.setTypeface(typeface);
                }
                if (texttype.equals("Amatic_Bold")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Amatic-Bold.ttf"));
                }
                if (texttype.equals("Amatic_Regular")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/AmaticSC-Regular.ttf"));
                }
                if (texttype.equals("Capture")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Capture_it.ttf"));
                }
                if (texttype.equals("Caviar_Dreams_Bold")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Caviar_Dreams_Bold.ttf"));
                }
                if (texttype.equals("CaviarDreams")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/CaviarDreams.ttf"));
                }
                if (texttype.equals("Ostrich")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/ostrich-regular.ttf"));
                }
                if (texttype.equals("OstrichSans")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/OstrichSans-Heavy.otf"));
                }
                if (texttype.equals("Pacifico")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Pacifico.ttf"));
                }
                if (texttype.equals("PlayfairBold")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/PlayfairDisplay-Bold.otf"));
                }
                if (texttype.equals("PlayfairItalic")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/PlayfairDisplay-Italic.otf"));
                }
                if (texttype.equals("Respective")) {
                    preview.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Respective_Swashes_Slanted.ttf"));
                }
            }
            TextView Title =  mView.findViewById(R.id.cardTitle);
            Title.setText(title);
            if (texttype!=null) {
                if (texttype.equals("MONOSPACE")) {
                    Title.setTypeface(android.graphics.Typeface.MONOSPACE);
                }
                if (texttype.equals("NORMAL")) {
                    Title.setTypeface(android.graphics.Typeface.DEFAULT);
                }
                if (texttype.equals("SERIF")) {
                    Title.setTypeface(android.graphics.Typeface.SERIF);
                }
                if (texttype.equals("CURSIVE")) {
                    android.graphics.Typeface typeface = android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/dancing-script.regular.ttf");
                    Title.setTypeface(typeface);
                }
                if (texttype.equals("Amatic_Bold")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Amatic-Bold.ttf"));
                }
                if (texttype.equals("Amatic_Regular")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/AmaticSC-Regular.ttf"));
                }
                if (texttype.equals("Capture")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Capture_it.ttf"));
                }
                if (texttype.equals("Caviar_Dreams_Bold")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Caviar_Dreams_Bold.ttf"));
                }
                if (texttype.equals("CaviarDreams")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/CaviarDreams.ttf"));
                }
                if (texttype.equals("Ostrich")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/ostrich-regular.ttf"));
                }
                if (texttype.equals("OstrichSans")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/OstrichSans-Heavy.otf"));
                }
                if (texttype.equals("Pacifico")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Pacifico.ttf"));
                }
                if (texttype.equals("PlayfairBold")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/PlayfairDisplay-Bold.otf"));
                }
                if (texttype.equals("PlayfairItalic")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/PlayfairDisplay-Italic.otf"));
                }
                if (texttype.equals("Respective")) {
                    Title.setTypeface(android.graphics.Typeface.createFromAsset(ctx.getAssets(), "fonts/Respective_Swashes_Slanted.ttf"));
                }
            }
        }
        public void setUsername(String username) {
            TextView post_username = mView.findViewById(R.id.post_username);
            post_username.setText(username);
        }
      public   void setimage_icon(final Context ctx, final String imagedicon) {


            Glide.with(mView.getContext())
                    .load(imagedicon)

                    .into(imageicon);
        }
      public  void setTextColor(int TextColor) {
            CardView cardView= mView.findViewById(R.id.Cardview_card);

            TextView post_username =  mView.findViewById(R.id.post_username);
            TextView cardtitle =  mView.findViewById(R.id.cardTitle);
          TextView date=mView.findViewById(R.id.textView_postdate);
            int textcolr=getContrastColor(TextColor);

          if (textcolr==Color.BLACK) {
              int blackmix = blendARGB(TextColor, Color.BLACK, 0.5f);
              Drawable drawable = upvote.getDrawable();
              Drawable md = drawable.mutate();
              md = DrawableCompat.wrap(md);
              DrawableCompat.setTint(md, blackmix);
              DrawableCompat.setTintMode(md, PorterDuff.Mode.SRC_IN);
              Drawable drawable1 = imageButton2.getDrawable();
              Drawable md1 = drawable1.mutate();
              md1 = DrawableCompat.wrap(md1);
              DrawableCompat.setTint(md1, blackmix);
              DrawableCompat.setTintMode(md1, PorterDuff.Mode.SRC_IN);
              upvotecounter.setTextColor(blackmix);
              eyecount.setTextColor(blackmix);
              post_username.setTextColor(blackmix);

             /* upvote.setImageResource(R.drawable.ic_favorite_like_24dp);
              Drawable up=upvote.getDrawable();
              Drawable md2=up.mutate();
              md2= DrawableCompat.wrap(md2);
              DrawableCompat.setTint(md2,blackmix);
              DrawableCompat.setTintMode(md2, PorterDuff.Mode.SRC_IN);*/
          }else{
              int blackmix = blendARGB(TextColor, Color.WHITE, 0.6f);
              Drawable drawable = upvote.getDrawable();
              Drawable md = drawable.mutate();
              md = DrawableCompat.wrap(md);
              DrawableCompat.setTint(md, blackmix);
              DrawableCompat.setTintMode(md, PorterDuff.Mode.SRC_IN);
              Drawable drawable1 = imageButton2.getDrawable();
              Drawable md1 = drawable1.mutate();
              md1 = DrawableCompat.wrap(md1);
              DrawableCompat.setTint(md1, blackmix);
              DrawableCompat.setTintMode(md1, PorterDuff.Mode.SRC_IN);
              upvotecounter.setTextColor(blackmix);
              eyecount.setTextColor(blackmix);
              post_username.setTextColor(blackmix);
          }

          int white=Color.WHITE;
          int Trans=Color.TRANSPARENT;
          int resultColor1 =  blendARGB(TextColor,Trans,0.2f);
            int resultColor =  blendARGB(TextColor,white,0.1f);
          int timecolor=blendARGB(textcolr,resultColor,0.5f);
          int titlecol=blendARGB(textcolr,resultColor,0.25f);
          cardtitle.setBackgroundColor(resultColor1);
            cardView.setCardBackgroundColor(resultColor);
            cardtitle.setTextColor(titlecol);
          date.setTextColor(timecolor);
          if (mView.findViewById(R.id.cardview_tabletcolor)!=null){
              TextView dot=mView.findViewById(R.id.textview_textdot);
              CardView cardView2= mView.findViewById(R.id.cardview_tabletcolor);
              cardView2.setCardBackgroundColor(resultColor);
              dot.setTextColor(titlecol);
              TextView t=mView.findViewById(R.id.cardPreview);
              t.setTextColor(titlecol);
          }

        }


      public void setHearts(int hearts) {

          if (hearts<1){
              upvotecounter.setText("0");
          }else{
              upvotecounter.setText(IntegerShortner(hearts));
          }
      }


      public void setViews(int views) {
          if (views<1) {
              eyecount.setText("0");
          }else{
              eyecount.setText(IntegerShortner(views));
          }
      }

      public void setTime(String time) {
          if (time!=null) {
              TextView textView = mView.findViewById(R.id.textView_postdate);
              textView.setText(time);
          }
      }



    }

    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
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
    private static  String IntegerShortner(int integer){
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
        }else{
            return String.valueOf(integer);
        }
    }
}
