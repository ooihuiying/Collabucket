package com.example.shanna.orbital2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class CollabView extends AppCompatActivity {
    private DatabaseReference mDatabase;

    //Android layout
    private CircleImageView mProfileDisplayImage;
    private TextView mTitle;
    private TextView mStatus;
    private TextView mOwner;
    private TextView mPartner;
    private TextView mDateOfListing;
    private TextView mDateOfRequest;

    private TextView mSummary;
    private TextView mQualifications;
    private TextView mResponsibilities;

    private TextView mFileReport;
    private TextView mBufferWait;
    private TextView mDuration;
    private TextView mMaxChanges;
    private TextView mPay;
    private Button mBack;
    private Button mFeedback;

    //Create storage reference in firebase
    private StorageReference mStorageRef;

    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_view);

        final String current_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String project_title = getIntent().getStringExtra("project_title");
        final String sender_id = getIntent().getStringExtra("SenderID");
        final String sender_name = getIntent().getStringExtra("SenderName");
        final String owner_name = getIntent().getStringExtra("OwnerName");

        mTitle = findViewById(R.id.titleHeader);
        mStatus = findViewById(R.id.textViewProjectStatus);
        mOwner = findViewById(R.id.textViewOwner);
        mPartner = findViewById(R.id.textViewPartner);
        mDateOfListing = findViewById(R.id.textViewDateOfListing);
        mDateOfRequest = findViewById(R.id.textViewDateOfRequest);
        mSummary = findViewById(R.id.textViewProjectSummary);
        mResponsibilities = findViewById(R.id.textViewResponsibilities);
        mQualifications = findViewById(R.id.textViewQualifications);
        mDuration = findViewById(R.id.textViewDuration);
        mPay = findViewById(R.id.textViewPay);
        mMaxChanges = findViewById(R.id.textViewMaxChange);
        mBufferWait = findViewById(R.id.textViewBufferWait);
        mBack = findViewById(R.id.BackBtn);
        mFeedback = findViewById(R.id.FeedbackBtn);
        mFileReport = findViewById(R.id.fileReport);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Image -> Reference to Firebase storage root
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //first set the value inside the text boxes to contain information that was set previously
        mDatabase.child("Users").child(sender_id).child("Projects").child(project_title)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mTitle.setText(project_title);
                        mStatus.setText(dataSnapshot.child("ProjectStatus").getValue().toString());
                        mOwner.setText(owner_name);
                        mPartner.setText(sender_name);
                        mDateOfListing.setText(dataSnapshot.child("DateOfListing").getValue().toString());
                        mDateOfRequest.setText(dataSnapshot.child("DateOfRequest").getValue().toString());
                        mSummary.setText(dataSnapshot.child("ProjectSummary").getValue().toString());
                        mResponsibilities.setText(dataSnapshot.child("ProjectResponsibilities").getValue().toString());
                        mQualifications.setText(dataSnapshot.child("ProjectQualifications").getValue().toString());
                        mDuration.setText(dataSnapshot.child("Duration").getValue().toString());
                        mPay.setText(dataSnapshot.child("Pay").getValue().toString());
                        mMaxChanges.setText(dataSnapshot.child("MaxChanges").getValue().toString());
                        mBufferWait.setText(dataSnapshot.child("BufferWait").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        // if status Completed, can leave feedback
        mFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStatus.getText().equals("Completed")) {
                    Intent intent = new Intent(CollabView.this, FeedbackForm.class);
                    intent.putExtra("user_id", sender_id);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CollabView.this, "Project not complete!", Toast.LENGTH_LONG).show();
                }
            }
        });


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mFileReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CollabView.this, FileReport.class));
            }
        });
    }
}
