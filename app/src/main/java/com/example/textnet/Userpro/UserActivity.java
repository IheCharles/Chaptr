package com.example.textnet.Userpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.textnet.MainActivity;
import com.example.textnet.R;
import com.example.textnet.Typing;

/**
 * Created by Dell on 2017/06/23.
 */

public class UserActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useractivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_UserActivity);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                Intent intent2= new Intent(UserActivity.this,MainActivity.class);
                startActivity(intent2);
            }
            });

        ImageView imageView= (ImageView)findViewById(R.id.imageView_useractivity);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(UserActivity.this,EditableProfile.class);
                startActivity(intent2);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.useractivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click<br />
        switch (item.getItemId()) {


            case R.id.add: {
                Intent intent= new Intent(this, Typing.class);
                startActivity(intent);
                finish();

            }
            default: {
                return super.onOptionsItemSelected(item);
            }

        }

    }

}
