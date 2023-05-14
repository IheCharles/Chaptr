package com.dev.textnet;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.support.v4.graphics.ColorUtils.blendARGB;

/**
 * Created by Dell on 2017/09/16.
 */

public class comments extends AppCompatActivity {
    DatabaseReference databaseReference;
    StorageReference mstoragereference;
    DatabaseReference postdatabase;
    FirebaseAuth mAuth;
    TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        mstoragereference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        postdatabase =FirebaseDatabase.getInstance().getReference().child("Posts");
        CardView cardView_scroll=findViewById(R.id.comment_cardviewsscoll);
        final DatabaseReference storageReference = databaseReference.child(mAuth.getCurrentUser().getUid()).child("imageurl");
        final RecyclerView recyclerView=findViewById(R.id.recyclerview_commentscom);

        LinearLayoutManager mLayoutManager=(new LinearLayoutManager(comments.this));
        recyclerView.setLayoutManager(mLayoutManager);
        String PostKey=getIntent().getExtras().getString("U");
        int selectedColor=getIntent().getExtras().getInt("color");

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = comments.this.getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int white1=Color.BLACK;
            int resultColor2 =  blendARGB(selectedColor,white1,0.1f);
            window.setStatusBarColor(resultColor2);

            cardView_scroll.setPadding(6,10,6,10);

        }

        DatabaseReference commentdatabase=FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey).child("Comments");
        final FirebaseRecyclerAdapter<coment,View_post.Commentviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<coment, View_post.Commentviewholder>(

                coment.class,
                R.layout.comment_model,
                View_post.Commentviewholder.class,
                commentdatabase
        ) {

            protected void populateViewHolder(final View_post.Commentviewholder viewHolder, final coment model, int position) {

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
        });




           /* posttext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!edittext.getText().toString().isEmpty()) {
                       DatabaseReference comment=postdatabase.child(PostKey).child("Comments").child(mAuth.getCurrentUser().getUid());
                    }else{
                        Toast.makeText(comments.this, "???", Toast.LENGTH_SHORT).show();
                    }
                }
            });*/


        databaseReference.child(mAuth.getCurrentUser().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.commentmenu, menu);
        return true;
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




    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return a < 0.5 ? Color.BLACK : Color.WHITE;
    }

}
