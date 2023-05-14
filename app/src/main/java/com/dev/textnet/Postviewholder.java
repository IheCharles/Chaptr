package com.dev.textnet;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.support.v4.graphics.ColorUtils.blendARGB;

/**
 * Created by Dell on 2017/07/22.
 */

  class Postviewholder extends RecyclerView.ViewHolder {

    View mView;
    ImageView upvote;
    ImageView imageicon;
    ImageView post_Image;
    private DatabaseReference mDatabaseUpvote;

    private FirebaseAuth mAuth;


    public Postviewholder(View itemView) {
        super(itemView);
        mView = itemView;

        imageicon = (ImageView) mView.findViewById(R.id.Post_imiageicon);
        post_Image = (ImageView) mView.findViewById(R.id.CardImage);
        upvote = (ImageView) mView.findViewById(R.id.upvotec);
        mDatabaseUpvote = FirebaseDatabase.getInstance().getReference().child("Upvote");
        mAuth = FirebaseAuth.getInstance();
    }




    public void setImage(final Context ctx, final String image) {
        final ImageView imageView = (ImageView) mView.findViewById(R.id.CardImage);
        Glide.with(ctx)
                .load(image)
                .into(imageView);
    }


    public void setTitle(String title) {

        TextView cardtitle = (TextView) mView.findViewById(R.id.cardTitle);
        cardtitle.setText(title);


    }


    public void setUsername(String username) {
        TextView post_username = (TextView) mView.findViewById(R.id.post_username);
        post_username.setText(username);
    }

    public void setimage_icon(final Context ctx, final String imageicon) {
        final ImageView image_icon = (ImageView) mView.findViewById(R.id.Post_imiageicon);
        Glide.with(ctx)
                .load(imageicon)
                .into(image_icon);
    }

    public void setTextColor(int TextColor) {
        CardView cardView= (CardView)mView.findViewById(R.id.Cardview_card);
        TextView post_username = (TextView) mView.findViewById(R.id.post_username);
        TextView cardtitle = (TextView) mView.findViewById(R.id.cardTitle);
        int textcolr=getContrastColor(TextColor);
        int white= Color.WHITE;
        int resultColor =  blendARGB(TextColor,white,0.1f);
        cardView.setCardBackgroundColor(resultColor);
        cardtitle.setTextColor(textcolr);
        post_username.setTextColor(textcolr);
    }
    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
    }

}
