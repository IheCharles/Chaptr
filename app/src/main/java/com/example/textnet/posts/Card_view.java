package com.example.textnet.posts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.textnet.Cards;
import com.example.textnet.MainActivity;
import com.example.textnet.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Dell on 2017/06/21.
 */

public class Card_view extends Fragment {

private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.card_view,container,false);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Posts");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview_new);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Cards,Postviewholder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cards, Postviewholder>(

                Cards.class,
                R.layout.card_view,
                Postviewholder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(Postviewholder viewHolder, Cards model, int position) {


                viewHolder.setTitle(model.getTitle());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public  static class Postviewholder extends RecyclerView.ViewHolder{

        View mView;

        public Postviewholder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setTitle(String title){
            TextView cardtitle= (TextView) mView.findViewById(R.id.cardTitle);
            cardtitle.setText(title);

        }


    }


}
