package com.example.ooikk.collabucket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class Profile extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    //Android layout
    private CircleImageView mDisplayImage;
    private EditText mEditTextName;
    private EditText mEditTextLocation;
    private EditText mEditTextProfession;
    private EditText mEditTextUserType;
    private EditText mEditTextDescription;
    private EditText mEditTextWebsite;
    private EditText mEditTextPhoneNum;
    private EditText mEditTextEmail;
    private EditText mEditTextEducation;
    private EditText mEditTextWork;
    private Button mButtonDone;
    private Button mButtonUpload;

    //Image Upload
    private static final int GALLERY_PICK = 1;
    String download_url;

    //Create storage reference in firebase
    private StorageReference mStorageRef;

    //Progress button
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDisplayImage = (CircleImageView)findViewById(R.id.profileAvatar);
        mEditTextName = (EditText) findViewById(R.id.fullName);
        mEditTextLocation = (EditText) findViewById(R.id.Location);
        mEditTextProfession = (EditText) findViewById(R.id.Profession);
        mEditTextUserType = (EditText) findViewById(R.id.UserType);
        mEditTextWebsite = (EditText) findViewById(R.id.Website);
        mEditTextPhoneNum = (EditText) findViewById(R.id.PhoneNumber);
        mEditTextDescription = (EditText) findViewById(R.id.AboutMe);
        mEditTextEmail = (EditText)findViewById(R.id.email);
        mEditTextEducation = (EditText)findViewById(R.id.Education);
        mEditTextWork = (EditText)findViewById(R.id.WorkExperience);
        mButtonDone = (Button)findViewById(R.id.btnDone);
        mButtonUpload = (Button)findViewById(R.id.btnUpload);

        //Image -> Reference to Firebase storage root
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //User data -> Reference to Firebase database root
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        //first set the value inside the text boxes to contain information that was set previously
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //For image, we need to use picasso library

                String image = dataSnapshot.child("Image").getValue().toString();
                if(!image.equals("default")) {
                    Picasso.get().load(image).placeholder(R.drawable.spaceman_1x).into(mDisplayImage);
                }
                mEditTextName.setText(dataSnapshot.child("FullName").getValue().toString());
                mEditTextLocation.setText(dataSnapshot.child("Location").getValue().toString());
                mEditTextProfession.setText(dataSnapshot.child("Profession").getValue().toString());
                mEditTextUserType.setText(dataSnapshot.child("UserType").getValue().toString());
                mEditTextWebsite.setText(dataSnapshot.child("Website").getValue().toString());
                mEditTextPhoneNum.setText(dataSnapshot.child("PhoneNum").getValue().toString());
                mEditTextEmail.setText(dataSnapshot.child("Email").getValue().toString());
                mEditTextDescription.setText(dataSnapshot.child("Description").getValue().toString());
                mEditTextEducation.setText(dataSnapshot.child("Education").getValue().toString());
                mEditTextWork.setText(dataSnapshot.child("WorkExperience").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //To update firebase when the done button is clicked.After update, lead user to profile view.java
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Progress
                mProgress = new ProgressDialog(Profile.this);
                mProgress.setTitle("Saving Changes");
                mProgress.setMessage("Please wait while the changes are being saved");
                mProgress.show();

                mUserDatabase.child("FullName").setValue(mEditTextName.getText().toString());
                mUserDatabase.child("Location").setValue(mEditTextLocation.getText().toString());
                mUserDatabase.child("Profession").setValue(mEditTextProfession.getText().toString());
                mUserDatabase.child("UserType").setValue(mEditTextUserType.getText().toString());
                mUserDatabase.child("Website").setValue(mEditTextWebsite.getText().toString());
                mUserDatabase.child("PhoneNum").setValue(mEditTextPhoneNum.getText().toString());
                mUserDatabase.child("Email").setValue(mEditTextEmail.getText().toString());
                mUserDatabase.child("Description").setValue(mEditTextDescription.getText().toString());
                mUserDatabase.child("Education").setValue(mEditTextEducation.getText().toString());
                mUserDatabase.child("WorkExperience").setValue(mEditTextWork.getText().toString());

                mProgress.setTitle("Done");
                mProgress.dismiss();


                // if update information is successful, go to view Profile
                Intent intent = new Intent(Profile.this, ViewProfile.class);
                intent.putExtra("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
                // End the activity
                finish();

            }

        });


        //This is for when user wants to upload their photos
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                // This is for choosing image from google drive i think
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*"); //define the type->Here we only want to pick images
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE" ), GALLERY_PICK);
            }
        });

    }

    //Enable user to crop the image -> Do this by adding a crop library
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Check request code
        if(requestCode == GALLERY_PICK && resultCode==RESULT_OK){

            Uri imageUri = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                //Progress dialogue
                mProgress = new ProgressDialog(Profile.this);
                mProgress.setTitle("Uploading image...");
                mProgress.setMessage("Please wait while we upload and process the image");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                Uri resultUri = result.getUri(); //uri of cropped image

                //change the icon directly on the update information page
                mDisplayImage.setImageURI(resultUri);

                //Thumbnail
                final File thumb_filePath = new File(resultUri.getPath());
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                //Firebase storage for thumbnail
                final StorageReference thumb_filepath = mStorageRef.child("Avatar").child("thumbs").child(mCurrentUser.getUid()+ ".jpg");

                //Now we should store that image in Firebase storage
                //Below is where we are going to store the file
                StorageReference filepath = mStorageRef.child("Avatar").child(mCurrentUser.getUid()+ ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Profile.this, "Successful in uploading to storage", Toast.LENGTH_LONG).show();

                            //Thumbnail
                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    // final String thumb_downloadUrl = thumb_task.getResult().toString();
                                    final String thumb_downloadUrl = thumb_task.getResult().toString();

                                    //final String thumb_downloadUrl = thumb_task.getResult().getMetadata().toString();
                                    if(thumb_task.isSuccessful()){
                                        //Main: to get the download_url
                                        mStorageRef.child("Avatar").child(mCurrentUser.getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                download_url = uri.toString();

                                                Map update_hashMap = new HashMap();
                                                update_hashMap.put("Image", download_url);
                                                update_hashMap.put("thumb_image", thumb_downloadUrl);

                                                //to update the database image value and thumbnail
                                                mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mProgress.dismiss(); //To remove the progress update or else, it would be up there forever
                                                            Toast.makeText(Profile.this, "Successful in updating database and thumbnail", Toast.LENGTH_LONG).show();
                                                        }else {
                                                            mProgress.dismiss();
                                                            Toast.makeText(Profile.this, "Error in updating database", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(Profile.this, "Error in updating thumbnail", Toast.LENGTH_LONG).show();
                                        mProgress.dismiss();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(Profile.this, "Error in uploading to storage", Toast.LENGTH_LONG).show();
                            mProgress.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}