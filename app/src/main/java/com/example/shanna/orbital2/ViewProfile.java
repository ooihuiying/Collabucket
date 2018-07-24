package com.example.shanna.orbital2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile extends AppCompatActivity {

    //For the user_clients to lead to the correct ViewProfile
    private TextView mDisplayID;


    private DatabaseReference mDatabase;

    //Android layout
    private CircleImageView mProfileDisplayImage;
    private TextView mProfileName;
    private TextView mProfileLocation;
    private TextView mProfileProfession;
    private TextView mUserType;
    private TextView mProfileDescription;
    private TextView mProfileWebsite;
    private TextView mProfilePhoneNum;
    private TextView mProfileEmail;
    private TextView mProfileEducation;
    private TextView mProfileWork;

    private Button mViewProjects;
    private Button mViewFeedbacks;

    //Create storage reference in firebase
    private StorageReference mStorageRef;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        //For the user_clients to lead to the correct ViewProfile
        final String user_id = getIntent().getStringExtra("user_id");

        mProfileDisplayImage = (CircleImageView)findViewById(R.id.profileAvatar);
        mProfileName =  findViewById(R.id.profileFullName);
        mProfileProfession =  findViewById(R.id.profileProfession);
        mUserType = findViewById(R.id.profileUserType);
        mProfileDescription =  findViewById(R.id.profileAboutMe);
        mProfileLocation =  findViewById(R.id.profileLocation);
        mProfilePhoneNum =  findViewById(R.id.profilePhoneNumber);
        mProfileEmail =  findViewById(R.id.profileEmail);
        mProfileWebsite =  findViewById(R.id.profileWebsite);
        mProfileEducation = findViewById(R.id.Education);
        mProfileWork = findViewById(R.id.WorkExperience);
        mViewProjects = findViewById(R.id.buttonViewProjects);
        mViewFeedbacks = findViewById(R.id.buttonFeedbacks);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Image -> Reference to Firebase storage root
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //first set the value inside the text boxes to contain information that was set previously
        mDatabase.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //For image, we need to use picasso library
                String image = dataSnapshot.child("Image").getValue().toString();

                if(!image.equals("default")) {
                    Picasso.get().load(image).placeholder(R.drawable.spaceman_1x).into(mProfileDisplayImage);
                }

                mProfileName.setText(dataSnapshot.child("FullName").getValue().toString());
                mProfileProfession.setText(dataSnapshot.child("Profession").getValue().toString());
                mUserType.setText(dataSnapshot.child("UserType").getValue().toString());
                mProfileDescription.setText(dataSnapshot.child("Description").getValue().toString());
                mProfileLocation.setText(dataSnapshot.child("Location").getValue().toString());
                mProfilePhoneNum.setText(dataSnapshot.child("PhoneNum").getValue().toString());
                mProfileEmail.setText(dataSnapshot.child("Email").getValue().toString());
                mProfileWebsite.setText(dataSnapshot.child("Website").getValue().toString());
                mProfileEducation.setText(dataSnapshot.child("Education").getValue().toString());
                mProfileWork.setText(dataSnapshot.child("WorkExperience").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mViewFeedbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewProfile.this, ViewFeedback.class));
            }
        });

        //When user click on chat -> Trigger chat activity
        mViewProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //by right should lead to Chat.java. But currently Chat.java not done, so lead to Main
                Intent intent = new Intent(ViewProfile.this, Users_ProjectsList.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

    }
}