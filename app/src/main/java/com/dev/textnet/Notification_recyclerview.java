package com.dev.textnet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Dell on 2018/01/05.
 */

public class Notification_recyclerview extends Fragment {
    FirebaseRecyclerAdapter<Notification_Card_model,Notification_recyclerview.Postviewholder> firebaseRecyclerAdapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private DatabaseReference databaseReference;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth mAuth;
    int preTotal=0;
    int ItemCount;
    int TotalItemCount;
    int firstItem;
    private  int prevTotal=0;
    static int count=3;
    int visthresh=3;
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
    LinearLayoutManager mLayoutManager;
    private int startingPageIndex = 0;
    private String param;
    SwipeRefreshLayout swipeRefreshLayout;
    int color;
    private boolean vis=false;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.notification_rec,container,false);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth= FirebaseAuth.getInstance();
        recyclerView=view.findViewById(R.id.recycleview_not);
        recyclerView.setHasFixedSize(true);
         mLayoutManager = (new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,true));
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
         swipeRefreshLayout=view.findViewById(R.id.notification_swipe);


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Notifications").orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                long longd = dataSnapshot.getChildrenCount();
                                //     Toast.makeText(getActivity(),String.valueOf(recyclerView.getChildCount()),Toast.LENGTH_SHORT).show();
                                long cont = 0;
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot1.child("Seen").getValue(Boolean.class)) {
                                        dataSnapshot1.getRef().removeValue();
                                    } else {
                                        dataSnapshot1.getRef().child("Seen").setValue(true);
                                    }
                                    cont = cont + 1;
                                    if (longd == cont) {
                                        Load();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }

            });
            FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("color").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        swipeRefreshLayout.setColorSchemeColors(dataSnapshot.getValue(Integer.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSndapshot) {
                if (dataSndapshot.exists()) {
                    Load();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth= FirebaseAuth.getInstance();
       firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSndapshot) {
               if (dataSndapshot.exists()) {
                   if (isVisibleToUser) {
                       Load2();

                   } else {
                       if (getActivity() != null) {
                           if (vis) {
                               vis = false;
                               firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Notifications").orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshot) {
                                       //     Toast.makeText(getActivity(),String.valueOf(recyclerView.getChildCount()),Toast.LENGTH_SHORT).show();
                                       for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                           if (dataSnapshot1.child("Seen").getValue(Boolean.class)) {
                                               dataSnapshot1.getRef().removeValue();

                                           }
                                       }
                                   }

                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {
                                   }
                               });
                           }
                       }
                   }
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


    }

    private void Load2() {
        vis=true;
        Load();
    }

    private void Load() {
        if (swipeRefreshLayout!=null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        final DatabaseReference notification_databaseref=firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Notifications");

        notification_databaseref.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              //  Toast.makeText(getActivity(),String.valueOf(dataSnapshot.getChildrenCount()),Toast.LENGTH_SHORT).show();
                Query filter;
                long l=0;
               for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                   l=l+1;

                   if (l==dataSnapshot.getChildrenCount()){
                       filter=notification_databaseref.orderByChild("timestamp").endAt(snapshot.child("timestamp").getValue(Long.class));
                       firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notification_Card_model, Notification_recyclerview.Postviewholder>(
                               Notification_Card_model.class,
                               R.layout.notification_card,
                               Notification_recyclerview.Postviewholder.class,
                               filter
                       ) {

                           protected void populateViewHolder(final Notification_recyclerview.Postviewholder viewHolder, Notification_Card_model model, final int position) {


                               viewHolder.setImage(getActivity(),model.getImageurl());
                               viewHolder.setText(getActivity(),model.getAction(),model.getNote());
                               final String postKey = getRef(position).getKey();


                               viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(final View view) {
                                       firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("Notifications").child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(final DataSnapshot dataSnapshot) {
                                               String direction=dataSnapshot.child("Type").getValue(String.class);


                                               if (dataSnapshot.child("Type").exists()) {
                                                   if (direction.equals("UserActivity")) {
                                                       Intent useractivityintent = new Intent(getActivity(), UserActivity_3.class);
                                                       useractivityintent.putExtra("Userprofil", dataSnapshot.child("key").getValue(String.class));
                                                       useractivityintent.putExtra("Activity","U1");
                                                       startActivity(useractivityintent);
                                                       dataSnapshot.getRef().removeValue();
                                                   }

                                                   if (direction.equals("Post")) {
                                                       Intent viewpostintent = new Intent(getActivity(), View_post.class);
                                                       viewpostintent.putExtra("Userprofile", dataSnapshot.child("key").getValue(String.class));
                                                       startActivity(viewpostintent);
                                                       dataSnapshot.getRef().removeValue();
                                                       //  Toast.makeText(getActivity(),dataSnapshot.child("Key").getValue(String.class),Toast.LENGTH_SHORT).show();

                                                   }

                                                   if (direction.equals("ReplyPopup")) {
                                                       firebaseDatabase.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(DataSnapshot dataSnapshot_u) {
                                                               reply_card reply_card = new reply_card(postKey,getActivity(), dataSnapshot.child("UserId").getValue(String.class), dataSnapshot_u.child("username").getValue(String.class), dataSnapshot_u.child("imageurl").getValue(String.class), dataSnapshot.child("imageurl").getValue(String.class), dataSnapshot.child("Name").getValue(String.class),dataSnapshot.child("note").getValue(String.class),dataSnapshot.child("action").getValue(String.class));
                                                               reply_card.show();
                                                           }

                                                           @Override
                                                           public void onCancelled(DatabaseError databaseError) {

                                                           }
                                                       });

                                                   }
                                               }else {
                                                   Toast.makeText(getActivity(),postKey,Toast.LENGTH_SHORT).show();
                                               }
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
                           }
                       });
                   }
               }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }



    public static class Postviewholder extends RecyclerView.ViewHolder {


        View mView;
        LinearLayout linearLayout;
        ImageView imageicon;
        TextView NC_1;
        LinearLayout gr;
        TextView NC_2;
        public Postviewholder(View itemView) {
            super(itemView);
            mView = itemView;
            linearLayout=mView.findViewById(R.id.NC_linearlayout);
            NC_1=mView.findViewById(R.id.NC_textview1);
            gr=mView.findViewById(R.id.gr);
            NC_2=mView.findViewById(R.id.NC_textview2);
            imageicon=mView.findViewById(R.id.NC_imageView);
        }

        public void setImage(FragmentActivity activity, String imageurl) {
            Glide.with(activity).load(imageurl)
                    .skipMemoryCache(true)
                    .into(imageicon);
        }

        public void setText(FragmentActivity activity, String action, String edittext) {
            NC_1.setText(action);
            if (edittext!=null) {
                if (edittext.length() == 0) {
                    NC_2.setVisibility(View.GONE);
                    gr.setGravity(Gravity.CENTER_VERTICAL);
                } else {
                    if (edittext.length() > 100) {
                        edittext = edittext.substring(0, 100) + "...";
                        NC_2.setText(edittext);

                    } else {
                        NC_2.setText(edittext);
                    }
                }
            }else{
                NC_2.setVisibility(View.GONE);
                gr.setGravity(Gravity.CENTER_VERTICAL);
            }
        }
    }
}
