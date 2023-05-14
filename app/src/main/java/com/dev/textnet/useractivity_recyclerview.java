package com.dev.textnet;

import android.app.Activity;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.graphics.ColorUtils.blendARGB;

/**
 * Created by Dell on 2017/11/13.
 */

public class useractivity_recyclerview extends Fragment{
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
   public FirebaseRecyclerAdapter<Cards,useractivity_recyclerview.Postviewholder> firebaseRecyclerAdapter;
Query filter;
    DatabaseReference firebaseDatabase;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
   Query filterQuery;
    FirebaseUser mcurrentuser;


    int preTotal=0;
    int ItemCount;
    int TotalItemCount;
    int firstItem;
    private  int prevTotal=0;
    static int count=10;
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
    String timestamp;
    private int previousTotalItemCount = 0;
    static int mRvTopView;
    static int mRvTopView2;
    static SharedPreferences sharedPreferences;
    static SharedPreferences sharedPreferences2;

    private int startingPageIndex = 0;
    private String param;
    DatabaseReference mypost;
    LinearLayout linearLayout_wood;


    private FirebaseAuth.AuthStateListener mAuthListener;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
            final View view=inflater.inflate(R.layout.useravity_recyclerviewpager,container,false);
            linearLayout_wood=view.findViewById(R.id.recyerview_post_sad);
            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
            mcurrentuser = mAuth.getCurrentUser();
            recyclerView =  view.findViewById(R.id.recycleview_useractivity);
            recyclerView.setHasFixedSize(true);
            mLayoutManager=(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            recyclerView.setLayoutManager(mLayoutManager);







            SharedPreferences psharedPreferences=getActivity().getSharedPreferences("param",MODE_PRIVATE);
            param=psharedPreferences.getString("mauth",null);
           // mypost=FirebaseDatabase.getInstance().getReference().child("Users").child(param).child("MyPosts");
            RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
            if (animator instanceof SimpleItemAnimator) {
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            }
            linearLayout_wood.setVisibility(View.GONE);


                if (mAuth.getCurrentUser().getUid().equals(param)) {
                    mDatabase.orderByChild("UserId").equalTo(param).limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() == 0) {
                                //Toast.makeText(getActivity(),"3",Toast.LENGTH_SHORT).show();
                                linearLayout_wood.setVisibility(View.VISIBLE);
                            } else {
                                linearLayout_wood.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            firstload();


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
                        currentPage++;
                        Posit2 = mLayoutManager.findFirstVisibleItemPosition();
                        View startView2 = recyclerView.getChildAt(0);


                        mRvTopView2 = (startView2 == null) ? 0 : (startView2.getTop() - recyclerView.getPaddingTop());

                        sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(getActivity());


                        SharedPreferences.Editor editor2=sharedPreferences2.edit();
                        editor2.putInt(RV_POS_INDEX2,Posit2);
                        editor2.putInt(RV_TOP_VIEW2,mRvTopView2);
                        editor2.apply();

                        loading=true;
                        Load();
                    }

                }
            });




            return view;
        }

    private void LoadTab() {
        // Toast.makeText(getActivity(),"load",Toast.LENGTH_SHORT).show();

        count=count+10;
        filterQuery = mDatabase.orderByChild("UserId").equalTo(param).limitToLast(count);

        //filterQuery=mypost.limitToLast(count);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cards, useractivity_recyclerview.Postviewholder>(
                Cards.class,
                R.layout.cards_tablet,
                useractivity_recyclerview.Postviewholder.class,
                filterQuery
        ) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            public Cards getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            public DatabaseReference getRef(int position) {
                return super.getRef(getItemCount() - 1 - position);
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(getItemCount() - 1 - position);
            }


            protected void populateViewHolder(final useractivity_recyclerview.Postviewholder viewHolder, Cards model, final int position) {


                final String postKey = getRef(position).getKey();


                viewHolder.setUsername(model.getUsername());
                viewHolder.setImage(getActivity(), model.getImage());
                viewHolder.setimage_icon(getContext(), model.getImageicon(),param);
                viewHolder.setTitle(getActivity(),model.getTitle(),model.getTypeface(),model.getStory());
                viewHolder.setTextColor(model.getTextcolor());
                viewHolder.setHearts(model.getHearts());
                viewHolder.setViews(model.getViews());
                viewHolder.setTime(model.getTime());


                if (!mAuth.getCurrentUser().getUid().equals(param)) {
                    viewHolder.buttondelete.setVisibility(View.GONE);
                }



                viewHolder.post_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Posit = mLayoutManager.findFirstVisibleItemPosition();
                        View startView = recyclerView.getChildAt(0);

                        mRvTopView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());


                        sharedPreferences  =PreferenceManager.getDefaultSharedPreferences(getActivity());

                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.clear();
                        editor.putInt(RV_POS_INDEX,Posit);
                        editor.putInt(RV_TOP_VIEW,mRvTopView);
                        editor.apply();
                        Intent intent2 = new Intent(getActivity(), View_post.class);
                        intent2.putExtra("Userprofile", postKey);

                        startActivity(intent2);

                    }
                });

                viewHolder.buttondelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ViewDialog alert=new ViewDialog(getActivity(),postKey,firebaseRecyclerAdapter);
                        alert.show();

                    }
                });
            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.setAdapter(firebaseRecyclerAdapter);

            }
        });


        if (sharedPreferences2 != null && getActivity()!=null) {
            if (PreferenceManager.getDefaultSharedPreferences(getActivity())!=null) {
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
    }

    private void tabletLoad() {
        filterQuery = mDatabase.orderByChild("UserId").equalTo(param).limitToLast(10);

        // filterQuery=mypost.limitToLast(2);//
        filterQuery.keepSynced(true);


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cards, useractivity_recyclerview.Postviewholder>(
                Cards.class,
                R.layout.cards_tablet,
                useractivity_recyclerview.Postviewholder.class,
                filterQuery
        ) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            public Cards getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            public DatabaseReference getRef(int position) {
                return super.getRef(getItemCount() - 1 - position);
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(getItemCount() - 1 - position);
            }


            protected void populateViewHolder(final useractivity_recyclerview.Postviewholder viewHolder, Cards model, final int position) {


                final String postKey = getRef(position).getKey();


                viewHolder.setUsername(model.getUsername());
                viewHolder.setImage(getActivity(), model.getImage());
                viewHolder.setimage_icon(getContext(), model.getImageicon(),param);
                viewHolder.setTitle(getActivity(),model.getTitle(),model.getTypeface(),model.getStory());
                viewHolder.setTextColor(model.getTextcolor());
                viewHolder.setHearts(model.getHearts());
                viewHolder.setViews(model.getViews());
                viewHolder.setTime(model.getTime());


                if (!mAuth.getCurrentUser().getUid().equals(param)) {
                    viewHolder.buttondelete.setVisibility(View.GONE);
                }



                viewHolder.post_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Posit = mLayoutManager.findFirstVisibleItemPosition();
                        View startView = recyclerView.getChildAt(0);

                        mRvTopView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());


                        sharedPreferences  =PreferenceManager.getDefaultSharedPreferences(getActivity());

                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.clear();
                        editor.putInt(RV_POS_INDEX,Posit);
                        editor.putInt(RV_TOP_VIEW,mRvTopView);
                        editor.apply();
                        Intent intent2 = new Intent(getActivity(), View_post.class);
                        intent2.putExtra("Userprofile", postKey);

                        startActivity(intent2);

                    }
                });

                viewHolder.buttondelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ViewDialog alert=new ViewDialog(getActivity(),postKey,firebaseRecyclerAdapter);
                        alert.show();

                    }
                });
            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {

                recyclerView.setAdapter(firebaseRecyclerAdapter);

            }
        });


        previousTotalItemCount=0;
        if (sharedPreferences2 != null && getActivity()!=null) {
            if (PreferenceManager.getDefaultSharedPreferences(getActivity())!=null) {
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
        //  filterQuery.removeEventListener((ChildEventListener) filterQuery);
    }


    private void firstload(){
            filterQuery = mDatabase.orderByChild("UserId").equalTo(param).limitToLast(10);

       // filterQuery=mypost.limitToLast(2);//
        filterQuery.keepSynced(true);


            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cards, useractivity_recyclerview.Postviewholder>(
                    Cards.class,
                    R.layout.cards,
                    useractivity_recyclerview.Postviewholder.class,
                    filterQuery
            ) {
                @Override
                public int getItemCount() {
                    return super.getItemCount();
                }

                @Override
                public Cards getItem(int position) {
                    return super.getItem(getItemCount() - 1 - position);
                }

                @Override
                public DatabaseReference getRef(int position) {
                    return super.getRef(getItemCount() - 1 - position);
                }

                @Override
                public long getItemId(int position) {
                    return super.getItemId(getItemCount() - 1 - position);
                }


                protected void populateViewHolder(final useractivity_recyclerview.Postviewholder viewHolder, Cards model, final int position) {


                    final String postKey = getRef(position).getKey();


                    viewHolder.setUsername(model.getUsername());
                    viewHolder.setImage(getActivity(), model.getImage());
                    viewHolder.setimage_icon(getContext(), model.getImageicon(),param);
                    viewHolder.setTitle(getActivity(),model.getTitle(),model.getTypeface(),model.getStory());
                    viewHolder.setTextColor(model.getTextcolor());
                    viewHolder.setHearts(model.getHearts());
                    viewHolder.setViews(model.getViews());
                    viewHolder.setTime(model.getTime());


                        if (!mAuth.getCurrentUser().getUid().equals(param)) {
                            viewHolder.buttondelete.setVisibility(View.GONE);
                        }



                    viewHolder.post_Image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Posit = mLayoutManager.findFirstVisibleItemPosition();
                            View startView = recyclerView.getChildAt(0);

                            mRvTopView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());


                            sharedPreferences  =PreferenceManager.getDefaultSharedPreferences(getActivity());

                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.clear();
                            editor.putInt(RV_POS_INDEX,Posit);
                            editor.putInt(RV_TOP_VIEW,mRvTopView);
                            editor.apply();
                            Intent intent2 = new Intent(getActivity(), View_post.class);
                            intent2.putExtra("Userprofile", postKey);

                            startActivity(intent2);

                        }
                    });

                    viewHolder.buttondelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ViewDialog alert=new ViewDialog(getActivity(),postKey,firebaseRecyclerAdapter);
                            alert.show();

                        }
                    });
                }
            };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {

                recyclerView.setAdapter(firebaseRecyclerAdapter);

            }
        });


        previousTotalItemCount=0;
            if (sharedPreferences2 != null && getActivity()!=null) {
                if (PreferenceManager.getDefaultSharedPreferences(getActivity())!=null) {
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
      //  filterQuery.removeEventListener((ChildEventListener) filterQuery);
        }

    private void Load() {
       // Toast.makeText(getActivity(),"load",Toast.LENGTH_SHORT).show();

        count=count+10;
       filterQuery = mDatabase.orderByChild("UserId").equalTo(param).limitToLast(count);

        //filterQuery=mypost.limitToLast(count);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cards, useractivity_recyclerview.Postviewholder>(
                Cards.class,
                R.layout.cards,
                useractivity_recyclerview.Postviewholder.class,
                filterQuery
        ) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            public Cards getItem(int position) {
                return super.getItem(getItemCount() - 1 - position);
            }

            @Override
            public DatabaseReference getRef(int position) {
                return super.getRef(getItemCount() - 1 - position);
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(getItemCount() - 1 - position);
            }


            protected void populateViewHolder(final useractivity_recyclerview.Postviewholder viewHolder, Cards model, final int position) {


                final String postKey = getRef(position).getKey();


                viewHolder.setUsername(model.getUsername());
                viewHolder.setImage(getActivity(), model.getImage());
                viewHolder.setimage_icon(getContext(), model.getImageicon(),param);
                viewHolder.setTitle(getActivity(),model.getTitle(),model.getTypeface(),model.getStory());
                viewHolder.setTextColor(model.getTextcolor());
                viewHolder.setHearts(model.getHearts());
                viewHolder.setViews(model.getViews());
                viewHolder.setTime(model.getTime());


                    if (!mAuth.getCurrentUser().getUid().equals(param)) {
                        viewHolder.buttondelete.setVisibility(View.GONE);
                    }



                viewHolder.post_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Posit = mLayoutManager.findFirstVisibleItemPosition();
                        View startView = recyclerView.getChildAt(0);

                        mRvTopView = (startView == null) ? 0 : (startView.getTop() - recyclerView.getPaddingTop());


                        sharedPreferences  =PreferenceManager.getDefaultSharedPreferences(getActivity());

                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.clear();
                        editor.putInt(RV_POS_INDEX,Posit);
                        editor.putInt(RV_TOP_VIEW,mRvTopView);
                        editor.apply();
                        Intent intent2 = new Intent(getActivity(), View_post.class);
                        intent2.putExtra("Userprofile", postKey);

                        startActivity(intent2);

                    }
                });

                viewHolder.buttondelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ViewDialog alert=new ViewDialog(getActivity(),postKey,firebaseRecyclerAdapter);
                        alert.show();

                    }
                });
            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recyclerView.setAdapter(firebaseRecyclerAdapter);

            }
        });


        if (sharedPreferences2 != null && getActivity()!=null) {
            if (PreferenceManager.getDefaultSharedPreferences(getActivity())!=null) {
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
    }



    public static class Postviewholder extends RecyclerView.ViewHolder {
        private final ImageButton imageButton2;
        ImageButton imageButton;
        ImageButton buttondelete;
        View mView;
        ImageView upvote;
        ImageView imageicon;
        ImageView post_Image;
        private DatabaseReference mDatabaseUpvote;
        private FirebaseAuth mAuth;
        TextView upvotecounter;
        TextView eyecount;
        public Postviewholder(View itemView) {
            super(itemView);
            mView = itemView;
            buttondelete=mView.findViewById(R.id.imageButtondelete);
            imageButton2=mView.findViewById(R.id.imageButton_views);
            imageicon =  mView.findViewById(R.id.Post_imiageicon);
            post_Image =  mView.findViewById(R.id.CardImage);
            upvote =  mView.findViewById(R.id.upvotec);
            upvotecounter=mView.findViewById(R.id.upvote_counter);
            eyecount=mView.findViewById(R.id.textView_eyescounter);
            mDatabaseUpvote = FirebaseDatabase.getInstance().getReference().child("Upvote");
            mAuth = FirebaseAuth.getInstance();
        }
        public void setImage(final Context ctx, final String image) {
            final ImageView imageView =  mView.findViewById(R.id.CardImage);
            Glide.with(mView.getContext())
                    .load(image)
                    .into(imageView);
        }
        public void setTitle(Context ctx,String title,String texttype,String story) {
            TextView Title =mView.findViewById(R.id.cardTitle);
            Title.setText(title);
            if (story != null && mView.findViewById(R.id.cardPreview)!=null ){
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
            TextView post_username =  mView.findViewById(R.id.post_username);
            post_username.setText(username);
        }
        public void setimage_icon(final Context ctx, final String imageicon, String param) {
           final String image1;

                final ImageView image_icon = mView.findViewById(R.id.Post_imiageicon);

                Glide.with(mView.getContext())
                        .load(imageicon)
                        .into(image_icon);
            }

        public void setTextColor(int TextColor) {
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
                Drawable drawable2 = buttondelete.getDrawable();
                Drawable md2 = drawable2.mutate();
                md2 = DrawableCompat.wrap(md2);
                DrawableCompat.setTint(md2, blackmix);
                DrawableCompat.setTintMode(md2, PorterDuff.Mode.SRC_IN);
                upvotecounter.setTextColor(blackmix);
                eyecount.setTextColor(blackmix);
                post_username.setTextColor(blackmix);
                date.setTextColor(blackmix);
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
                Drawable drawable2 = buttondelete.getDrawable();
                Drawable md2 = drawable2.mutate();
                md2 = DrawableCompat.wrap(md2);
                DrawableCompat.setTint(md2, blackmix);
                DrawableCompat.setTintMode(md2, PorterDuff.Mode.SRC_IN);
                upvotecounter.setTextColor(blackmix);
                eyecount.setTextColor(blackmix);
                post_username.setTextColor(blackmix);
                date.setTextColor(blackmix);
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
                upvotecounter.setText(IntegerShortner(hearts,0));
            }
        }


        public void setViews(int views) {
            if (views<1) {
                eyecount.setText("0");
            }else{
                eyecount.setText(IntegerShortner(views,0));
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

    @Override
    public void onStop() {
        super.onStop();
        if (firebaseRecyclerAdapter!=null) {
            firebaseRecyclerAdapter.cleanup();
        }
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences psharedPreferences=getActivity().getSharedPreferences("param",MODE_PRIVATE);
        param=psharedPreferences.getString("mauth",null);
        linearLayout_wood.setVisibility(View.GONE);
        if (sharedPreferences2 != null) {

            SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getActivity());

            Posit2 = (preferences2.getInt(RV_POS_INDEX2, 0));
            mRvTopView2 = (preferences2.getInt(RV_TOP_VIEW2, 0));
            if (firebaseRecyclerAdapter!=null) {
                firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    public void onItemRangeInserted(int positionStart, int itemCount) {

                        mLayoutManager.scrollToPositionWithOffset(Posit2, mRvTopView2);
                    }

                });
            }


        }
        if (param!=null) {
        //    Load(timestamp);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences psharedPreferences=getActivity().getSharedPreferences("param",MODE_PRIVATE);
        param=psharedPreferences.getString("mauth",null);
        if (sharedPreferences2 != null) {

            SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getActivity());

            Posit2 = (preferences2.getInt(RV_POS_INDEX2, 0));
            mRvTopView2 = (preferences2.getInt(RV_TOP_VIEW2, 0));
            if (firebaseRecyclerAdapter!=null) {
                firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    public void onItemRangeInserted(int positionStart, int itemCount) {

                        mLayoutManager.scrollToPositionWithOffset(Posit2, mRvTopView2);
                    }

                });
            }


        }
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
