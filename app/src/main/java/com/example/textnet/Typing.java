package com.example.textnet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.textnet.Userpro.EditableProfile;
import com.example.textnet.Userpro.UserActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by Dell on 2017/06/25.
 */

public class Typing extends AppCompatActivity {
    private static final int SELECT_FILE = 2;
    Uri imageHoldUri = null;
    private DatabaseReference mdatabase;
    private ProgressDialog mprogressdialog;
    private StorageReference mstroragereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typing);

        mstroragereference= FirebaseStorage.getInstance().getReference();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Posts");

        final EditText Title= (EditText)findViewById(R.id.editText_typing);
        final EditText story= (EditText)findViewById(R.id.editTextstory_typing);


        mprogressdialog=new ProgressDialog(this);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_typing);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String heading=Title.getText().toString();
                String body = story.getText().toString();
                if (TextUtils.isEmpty(heading) && TextUtils.isEmpty(body)) {
                    Intent intent2 = new Intent(Typing.this, UserActivity.class);
                    startActivity(intent2);
                    finish();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Typing.this);
                    builder.setTitle("Current work will be lost");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent2 = new Intent(Typing.this, UserActivity.class);
                            startActivity(intent2);
                            finish();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }
            }
        });

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.navigationbottombar);


       ImageView CardImage=(ImageView) findViewById(R.id.imageView_typing);
        CardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_typing, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click<br />
        switch (item.getItemId()) {


            case R.id.Post: {
                StartPosting();
            }
            default: {
                return super.onOptionsItemSelected(item);
            }

        }

    }

    private void StartPosting() {
        mprogressdialog.setTitle("Posting...");
        mprogressdialog.show();
        final EditText Title= (EditText)findViewById(R.id.editText_typing);
        final EditText story= (EditText)findViewById(R.id.editTextstory_typing);
        String heading=Title.getText().toString().trim();
        String body = story.getText().toString();

        if (!TextUtils.isEmpty(heading) && !TextUtils.isEmpty(body) && imageHoldUri != null){

            StorageReference filepath=mstroragereference.child("Post_image").child(imageHoldUri.getLastPathSegment());

            filepath.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri imageUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newpost=mdatabase.push();

                    newpost.child("Title").setValue(Title);
                    newpost.child("Story").setValue(story);
                    newpost.child("Image").setValue(imageUrl.toString());
                    mprogressdialog.dismiss();

                    Intent intent = new Intent(Typing.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final ImageView imageView= (ImageView)findViewById(R.id.imageView_typing);
        //SAVE URI FROM GALLERY
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setInitialCropWindowPaddingRatio(0)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                imageView.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
