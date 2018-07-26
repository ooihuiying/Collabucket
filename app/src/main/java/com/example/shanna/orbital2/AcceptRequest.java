package com.example.shanna.orbital2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.app.PendingIntent.getActivity;

public class AcceptRequest extends AppCompatActivity {

    private TextView mPay;
    private TextView mDuration;
    private TextView mBufferWait;
    private TextView mMaxChanges;
    private TextView mDateOfRequest;
    private TextView mTitle;

    private TextView mBtnProfileView;
    private Button mBtnReject;
    private Button mBtnAccept;

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseClone;
    private DatabaseReference mRequesterDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);

        //From requestFragment
        mPay = findViewById(R.id.textViewPay);
        mDuration = findViewById(R.id.textViewDuration);
        mBufferWait = findViewById(R.id.textViewWait);
        mMaxChanges = findViewById(R.id.textViewChanges);
        mDateOfRequest = findViewById(R.id.textViewFormSubmitDate);
        mTitle = findViewById(R.id.editTextPay);

        mBtnProfileView=findViewById(R.id.buttonViewRequesterProfile);
        mBtnReject = findViewById(R.id.buttonReject);
        mBtnAccept = findViewById(R.id.buttonAccept);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String owner_id = current_user.getUid();

        final String pay = getIntent().getStringExtra("pay");
        final String duration = getIntent().getStringExtra("duration");
        final String bufferWait = getIntent().getStringExtra("bufferWait");
        final String maxChanges = getIntent().getStringExtra("maxChanges");
        final String requestDate = getIntent().getStringExtra("requestDate");
        final String partnerID = getIntent().getStringExtra("partnerID");  //requester
        final String senderFullName = getIntent().getStringExtra("senderFullName");
        final String project_title = getIntent().getStringExtra("projectTitle");


       // Toast.makeText(AcceptRequest.this, "sender id is ->" + senderID, Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // display agreement details by reading from the AllCollabsReq branch
        mDatabase.child("AllCollabsReq").child(owner_id)
                .child(project_title + partnerID)
                //.addValueEventListener(new ValueEventListener() {
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mPay.setText(dataSnapshot.child("Pay").getValue().toString());
                        mDuration.setText(dataSnapshot.child("Duration").getValue().toString());
                        mBufferWait.setText(dataSnapshot.child("BufferWait").getValue().toString());
                        mMaxChanges.setText(dataSnapshot.child("MaxChanges").getValue().toString());
                        mDateOfRequest.setText(dataSnapshot.child("DateOfRequest").getValue().toString());
                        mTitle.setText(dataSnapshot.child("Title").getValue().toString());

                        mDatabase.child("AllCollabsReq").child(owner_id).child(project_title+partnerID).removeEventListener(this);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        //bring user to see the requester's profile
        mBtnProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AcceptRequest.this, ViewProfile.class);
                intent.putExtra("user_id", partnerID);
                startActivity(intent);
            }
        });



        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //1) Make a new branch under successfulCollaborations -> Same level as Users.
                //partner id is project requester id
                mDatabaseClone = FirebaseDatabase.getInstance().getReference().child("SuccessfulCollaborationsNotifications").child(partnerID)
                        .child(project_title + partnerID);

                ////////////////1a) Do for SuccessfulCollaborationsNotifications
                final HashMap<String, Object> collabMapClone = new HashMap<>();
                collabMapClone.put("Pay", pay);
                collabMapClone.put("Duration", duration);
                collabMapClone.put("BufferWait", bufferWait);
                collabMapClone.put("MaxChanges", maxChanges);
                collabMapClone.put("DateOfRequest", requestDate);
                collabMapClone.put("Partner", FirebaseAuth.getInstance().getCurrentUser().getUid());
                collabMapClone.put("SenderFullName", senderFullName);
                collabMapClone.put("Title", project_title);

                //Get owner full name
                final DatabaseReference name = FirebaseDatabase.getInstance().getReference();
                name.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //this is project owner id
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String ownerName = dataSnapshot.child("FullName").getValue().toString();
                                                    collabMapClone.put("OwnerFullName", ownerName);
                                                    mDatabaseClone.setValue(collabMapClone);
                                                    name.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeEventListener(this);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });



                ///1b)update the branch called SuccessfulCollaboration -> Same level as Users
                /// We have successfulcollaboration-> Owner and succesfulcollaborations-> partner
                /// so that collab fragment can be seen for both users

                //Get owner full name
                final DatabaseReference name2 = FirebaseDatabase.getInstance().getReference();
                name2.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        //.addValueEventListener(new ValueEventListener() {
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    final DatabaseReference mDatabaseCollabOwner = FirebaseDatabase.getInstance().getReference().child("SuccessfulCollaborations").child(owner_id)
                                        .child(project_title+owner_id);
                                    final DatabaseReference mDatabaseCollabPartner = FirebaseDatabase.getInstance().getReference().child("SuccessfulCollaborations").child(partnerID)
                                        .child(project_title+partnerID);

                                    String ownerFullName = dataSnapshot.child("FullName").getValue().toString();

                                    final HashMap<String, Object> collabMap = new HashMap<>();
                                    collabMap.put("Pay", pay);
                                    collabMap.put("Duration", duration);
                                    collabMap.put("BufferWait", bufferWait);
                                    collabMap.put("MaxChanges", maxChanges);
                                    collabMap.put("DateOfRequest", requestDate);
                                    collabMap.put("Partner", partnerID);
                                    collabMap.put("SenderFullName", senderFullName);
                                    collabMap.put("Title", project_title);
                                    collabMap.put("OwnerID", owner_id);
                                    collabMap.put("ProjectStatus", "closed");
                                    collabMap.put("OwnerFullName", ownerFullName);

                                    //get project summary, responsibilities, qualifications, date of listing
                                    final DatabaseReference mmm = FirebaseDatabase.getInstance().getReference();
                                    mmm.child("Users").child(owner_id)
                                        .child("Projects")
                                        .child(project_title)
                                        .addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            String projectSummary = dataSnapshot.child("ProjectSummary").getValue().toString();
                                            String projectQualifications = dataSnapshot.child("ProjectQualifications").getValue().toString();
                                            String projectResponsibilities = dataSnapshot.child("ProjectResponsibilities").getValue().toString();
                                            String projectDateOfListing = dataSnapshot.child("DateOfListing").getValue().toString();

                                            collabMap.put("ProjectSummary", projectSummary);
                                            collabMap.put("ProjectResponsibilities", projectResponsibilities);
                                            collabMap.put("ProjectQualifications", projectQualifications);
                                            collabMap.put("DateOfListing", projectDateOfListing);

                                            mDatabaseCollabOwner.setValue(collabMap);
                                            mDatabaseCollabPartner.setValue(collabMap);


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {}


                                    }); //end of inner datachange for project summary, responsibilities, qualifications, date of listing

                                 } //end of outer datachange for project owner's full name


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                //3) DOESN"T WORK -> THE APP CRASHES
                // delete the request from the request page of the project's owner
                //by deleting the branch from "AllCollabsReq" so that the request fragment
                //cannot find the branch and it will be empty.

                /*
                DatabaseReference mDeleteRequest = FirebaseDatabase.getInstance().getReference().child("AllCollabsReq")
                        .child(owner_id)
                        .child(project_title+partnerID);

                mDeleteRequest.removeValue();
                */

                //4) update for requester side so that it gets displayed under his/her projects branch

                //Need to first obtain Projectsummary, qualifications, responsibilities, dateoflisting from project owner's branch
                //Need to do datachange in order to obtain the values from the key

                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                DatabaseReference mgetDatabase = FirebaseDatabase.getInstance().getReference();

               mgetDatabase.child("Users").child(owner_id)
                        .child("Projects")
                        .child(project_title)
                        .addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String projectSummary = dataSnapshot.child("ProjectSummary").getValue().toString();
                                String projectQualifications = dataSnapshot.child("ProjectQualifications").getValue().toString();
                                String projectResponsibilities = dataSnapshot.child("ProjectResponsibilities").getValue().toString();
                                String projectDateOfListing = dataSnapshot.child("DateOfListing").getValue().toString();


                                mRequesterDatabase = FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(partnerID)
                                        .child("Projects")
                                        .child(project_title);

                                 /*+++++++++++++++++++++
                                mRequesterDatabase = FirebaseDatabase.getInstance().getReference().child("ProjectsListed")
                                        .child(partnerID)
                                        .child(project_title);
                                   +++++++++++++++++++++++++*/

                                final HashMap<String, Object> collabMapRequester = new HashMap<>();

                                collabMapRequester.put("Pay", pay);
                                collabMapRequester.put("Duration", duration);
                                collabMapRequester.put("BufferWait", bufferWait);
                                collabMapRequester.put("MaxChanges", maxChanges);
                                collabMapRequester.put("DateOfRequest", requestDate);
                                collabMapRequester.put("Owner", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                collabMapRequester.put("Partner", partnerID);
                                collabMapRequester.put("Title", project_title);
                                collabMapRequester.put("ProjectStatus", "Closed.");
                                collabMapRequester.put("ProjectSummary", projectSummary);
                                collabMapRequester.put("ProjectQualifications", projectQualifications);
                                collabMapRequester.put("ProjectResponsibilities", projectResponsibilities);
                                collabMapRequester.put("DateOfListing", projectDateOfListing);

                                mRequesterDatabase.setValue(collabMapRequester);




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                // update projects for owner -> Not done. Commented line 280 because the updating of owner's
                                // project willl lead to the freelancer emulator to showing an empty Collab form.
                                //Not sure why...just take note that the project owner's project branch is not updated
                                //Only freelancer side is
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                           //    DatabaseReference ownerDatabase = FirebaseDatabase.getInstance().getReference()
                           //             .child("Users")
                            //            .child(owner_id)
                            //            .child("Projects")
                            //            .child(project_title);



                               // ownerDatabase.setValue(collabMapRequester);


/*
                                DatabaseReference ownerDatabase = FirebaseDatabase.getInstance().getReference()
                                        .child("ProjectsListed")
                                        .child(owner_id)
                                        .child(project_title);

                                ownerDatabase.setValue(collabMapRequester);
*/


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

               Intent intent = new Intent(AcceptRequest.this, make_payment.class);
               intent.putExtra("SenderID", partnerID);
               intent.putExtra("project_title", project_title);

               startActivity(intent);
               finish();

            } //end onclick
        }); //end button

        //Update the rejected collabs
        //Then lead user to the main page
        mBtnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Update a new branch called rejectCollabReq -> Same level as Users
                DatabaseReference rejectBtn = FirebaseDatabase.getInstance().getReference().child("RejectedCollabReq").child(partnerID).child(project_title+partnerID);

                HashMap<String, String> map = new HashMap<>();

                map.put("RejectedSenderID", partnerID);
                map.put("RejectedSenderFullName", senderFullName);
                map.put("ProjectOwnerID", owner_id);
                map.put("ProjectTitle", project_title);

                rejectBtn.setValue(map);
                Toast.makeText(AcceptRequest.this, "Rejection for collaboration sent!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(AcceptRequest.this, MainActivity.class);
                startActivity(intent);
                finish();



            }
        });

    }

}