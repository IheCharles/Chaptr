package com.dev.textnet.Userpro;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.textnet.Login.register;
import com.dev.textnet.MainActivity;
import com.dev.textnet.Progressing;
import com.dev.textnet.R;
import com.dev.textnet.Splash_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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
    DatabaseReference firebaseDatabase;
    StorageReference mstoragereference;
    private int selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);
        if (Build.VERSION.SDK_INT>=21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ResourcesCompat.getColor(getResources(),R.color.Colo,null));
        }
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





    private void SaveProfile() {
        boolean hasDrawable = (imageView.getDrawable() != null);
        if(hasDrawable) {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            selectedColor = calculateAverageColor(bitmap, 1);

            final EditText editText = (EditText) findViewById(R.id.inputusername);
            final EditText editText1 = (EditText) findViewById(R.id.Status_userprofile);


            final String username, userstatus;

            username = editText.getText().toString().trim();
            userstatus = editText1.getText().toString().trim();

            if (!TextUtils.isEmpty(username) && imageHoldUri != null && !TextUtils.isEmpty(userstatus)) {
                final Progressing alert = new Progressing(UserProfile.this);
                alert.show();
                alert.textView1.setText("Saving...");
                String useremail = getIntent().getExtras().getString("Email");
                String userpass = getIntent().getExtras().getString("Pass");

                mAuth.createUserWithEmailAndPassword(useremail, userpass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            alert.dismiss();

                            Toast.makeText(UserProfile.this, "Account Created", Toast.LENGTH_SHORT).show();
                            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                            final StorageReference mstorage = mstoragereference.child("User_profile").child(imageHoldUri.getLastPathSegment());
                            //String ProfilePicUrl = imageHoldUri.getLastPathSegment();
                            mstorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    mstorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri imageUrl) {
                                            firebaseDatabase.child("username").setValue(username);
                                            firebaseDatabase.child("status").setValue(userstatus);
                                            firebaseDatabase.child("userid").setValue(mAuth.getCurrentUser().getUid());
                                            firebaseDatabase.child("imageurl").setValue(imageUrl.toString());
                                            firebaseDatabase.child("PostCount").setValue(0);
                                            firebaseDatabase.child("followerNo").setValue(0);
                                            firebaseDatabase.child("color").setValue(selectedColor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //  alert.dismiss();

                                                    finishAffinity();
                                                    //  restartApp();
                                                    Intent intent= new Intent(UserProfile.this,Splash_Activity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }
                                    });




                                }
                            });

                        } else {
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            if (e != null) {
                                Toast.makeText(UserProfile.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(UserProfile.this, "Account Creation failed", Toast.LENGTH_SHORT).show();
                            alert.dismiss();

                        }
                    }
                });

            } else {
                Toast.makeText(UserProfile.this, "Incomplete", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(UserProfile.this, "Incomplete", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(UserProfile.this,register.class);
        startActivity(intent);
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), Splash_Activity.class);
        int mPendingIntentId = 1;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final ImageView imageView= findViewById(R.id.imageView_profile);
        //SAVE URI FROM GALLERY
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setInitialCropWindowPaddingRatio(0)
                    .setAspectRatio(1, 1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setInitialCropWindowPaddingRatio(0)
                    .setRequestedSize(400,400)
                  //  .setOutputCompressQuality(80)
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


}
