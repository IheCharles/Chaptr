package com.example.textnet.Userpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textnet.Login.login;
import com.example.textnet.Login.register;
import com.example.textnet.MainActivity;
import com.example.textnet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by Dell on 2017/06/23.
 */

public class UserProfile extends AppCompatActivity{
    private static final int SELECT_FILE = 2;
    Uri imageHoldUri = null;
    ImageView imageView;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mauthStateListener;
    ProgressDialog mprogressDialog;
    DatabaseReference firebaseDatabase;
    StorageReference mstoragereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        final EditText editText = (EditText) findViewById(R.id.inputusername);
        final EditText editText1= (EditText) findViewById(R.id.Status_userprofile);
        imageView = (ImageView) findViewById(R.id.imageView_profile);

        firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth= FirebaseAuth.getInstance();
        mauthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                if (firebaseUser != null){

                    finish();
                    Intent intent= new Intent(UserProfile.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }


            }
        };


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);


            }
        });

        mprogressDialog= new ProgressDialog(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mstoragereference= FirebaseStorage.getInstance().getReference();
        TextView textView=  (TextView)findViewById(R.id.Saveprofile);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username= editText.getText().toString();

                if (!username.equals("")){

                    SaveProfile();

                }else{
                    Toast.makeText(UserProfile.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }




            }



        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final ImageView imageView= (ImageView)findViewById(R.id.imageView_editableprofile);
        //SAVE URI FROM GALLERY
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
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


    private void SaveProfile() {
        final EditText editText = (EditText) findViewById(R.id.inputusername);
        final EditText editText1= (EditText) findViewById(R.id.Status_userprofile);

       final String username,userstatus;

       username= editText.getText().toString().trim();
        userstatus= editText1.getText().toString().trim();

        if (!username.equals("")){



                mprogressDialog.setTitle("Saving...");
                mprogressDialog.show();

            StorageReference mstorage= mstoragereference.child("User_profile").child(imageHoldUri.getLastPathSegment());
                String ProfilePicUrl=imageHoldUri.getLastPathSegment();

                mstorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Uri imageUrl = taskSnapshot.getDownloadUrl();

                        firebaseDatabase.child("username").setValue(username);
                        firebaseDatabase.child("status").setValue(userstatus);
                        firebaseDatabase.child("userid").setValue(mAuth.getCurrentUser().getUid());
                        firebaseDatabase.child("imageurl").setValue(imageUrl.toString());

                        mprogressDialog.dismiss();

                        finish();
                        Intent moveToHome = new Intent(UserProfile.this, MainActivity.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);



                    }
                });



        }else {
            Toast.makeText(UserProfile.this, "Please enter a username", Toast.LENGTH_SHORT).show();
        }

    }

}
