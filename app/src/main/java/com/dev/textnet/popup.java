package com.dev.textnet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import static android.support.v4.graphics.ColorUtils.blendARGB;

/**
 * Created by Dell on 2017/08/26.
 */

public class popup extends Activity {
    DatabaseReference firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mcurrentuser;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mcurrentuser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        final ImageView UserImage= (ImageView) findViewById(R.id.imageView_popupblur);
        final ImageView  imageView = (ImageView) findViewById(R.id.imageView_popup);




        byte[] bitma=getIntent().getByteArrayExtra("bit");
        Bitmap bitmap= BitmapFactory.decodeByteArray(bitma,0,bitma.length);

        ImageView bla=(ImageView)findViewById(R.id.bla);
        bla.setImageBitmap(bitmap);

        bla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        final String PostKey=getIntent().getExtras().getString("Userprofi");

        DatabaseReference storageReferenc = firebaseDatabase.child(PostKey).child("status");
     final   TextView textView=(TextView)findViewById(R.id.textView_pop);
        storageReferenc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ugh=dataSnapshot.getValue(String.class);
                textView.setText(ugh);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference title=firebaseDatabase.child(PostKey).child("username");
        title.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dong =dataSnapshot.getValue(String.class);
                TextView textView1=(TextView)findViewById(R.id.textView_popcat);
                textView1.setText(dong);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference storageReference = firebaseDatabase.child(PostKey).child("imageurl");
        storageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String imageUrl = dataSnapshot.getValue(String.class);
                firebaseDatabase.child(mAuth.getCurrentUser().getUid()).child("follow").child(PostKey).child("CircleImage").setValue(imageUrl);


                Glide.with(getApplicationContext())
                        .load(imageUrl)
                        .into(imageView);



                Picasso.with(popup.this)
                        .load(imageUrl)
                        .resize(200, 100)

                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(UserImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                boolean hasDrawable = (imageView.getDrawable() != null);
                                if (hasDrawable) {
                                    final ImageView imageView_blr= (ImageView) findViewById(R.id.imageView_popupblur);
                                    BitmapDrawable drawable = (BitmapDrawable) imageView_blr.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();

                                    int selectedColor = calculateAverageColor(bitmap, 1);
                                    int selecttextcolor = getContrastColor(selectedColor);

                                    int white = Color.WHITE;
                                    int resultColor = blendARGB(selectedColor, white, 0.2f);
                                    CardView cardView=(CardView)findViewById(R.id.irart);
                                    cardView.setCardBackgroundColor(selectedColor);


                                }

                            }

                            @Override
                            public void onError() {
                                Picasso.with(popup.this)
                                        .load(imageUrl)
                                        .resize(200, 100)
                                        .into(UserImage, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                boolean hasDrawable = (imageView.getDrawable() != null);
                                                if (hasDrawable) {
                                                    final ImageView imageView_blr= (ImageView) findViewById(R.id.imageView_popupblur);
                                                    BitmapDrawable drawable = (BitmapDrawable) imageView_blr.getDrawable();
                                                    Bitmap bitmap = drawable.getBitmap();
                                                    int selectedColor = calculateAverageColor(bitmap, 1);

                                                    int selecttextcolor = getContrastColor(selectedColor);

                                                    int white = Color.BLACK;
                                                    int resultColor = blendARGB(selectedColor, white, 0.2f);

                                                    CardView cardView=(CardView)findViewById(R.id.irart);
                                                    cardView.setCardBackgroundColor(selectedColor);


                                                }
                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });

                            }
                        });

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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


    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
    }


}
