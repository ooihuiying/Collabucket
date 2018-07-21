package com.example.shanna.orbital2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Collaborate extends AppCompatActivity {

    private EditText mEditTextPay;
    private EditText mEditTextDuration;
    private EditText mEditTextMaxChanges;
    private EditText mEditTextWait;
    private EditText mEditTextRequestListingDate;
    private Button mBtnCollaborate;

    private FirebaseAuth auth;
    private DatabaseReference mDatabaseClone;
    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mSenderFullName;
    private DatabaseReference mOwnerFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborate_form);

        mEditTextPay = findViewById(R.id.editTextPay);
        mEditTextDuration = findViewById(R.id.editTextDuration);
        mEditTextMaxChanges = findViewById(R.id.editTextMaxChanges);
        mEditTextWait = findViewById(R.id.editTextWait);
        mEditTextRequestListingDate = findViewById(R.id.editTextRequestListingDate);
        mBtnCollaborate = findViewById(R.id.button2);

        //Previous page was ProjectDetails.java
        final String owner_id = getIntent().getStringExtra("Owner");
        final String title = getIntent().getStringExtra("Title");

        auth = FirebaseAuth.getInstance();

        // click to complete collaboration
        mBtnCollaborate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pay = mEditTextPay.getText().toString().trim(); //get from textbox
                final String duration = mEditTextDuration.getText().toString().trim(); //get from textbox
                final String maxChanges = mEditTextMaxChanges.getText().toString().trim(); //get from textbox
                final String wait = mEditTextWait.getText().toString().trim(); //get from textbox
                final String requestDate = mEditTextRequestListingDate.getText().toString().trim();

                // check if quotation is empty
                if (TextUtils.isEmpty(pay)) {
                    Toast.makeText(Collaborate.this, "Enter base quotation", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if phone number is empty
                if (TextUtils.isEmpty(duration)) {
                    Toast.makeText(Collaborate.this, "Enter collaboration duration", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if changes is empty
                if (TextUtils.isEmpty(maxChanges)) {
                    Toast.makeText(Collaborate.this, "Enter max number of changes", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if password is empty
                if (TextUtils.isEmpty(wait)) {
                    Toast.makeText(Collaborate.this, "Enter buffer time for response", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if today's date is empty
                if (TextUtils.isEmpty(requestDate)) {
                    Toast.makeText(Collaborate.this, "Enter today's date", Toast.LENGTH_SHORT).show();
                    return;
                }

                //notification -> To update the "Notifications" branch at firebase
                //This update in "Notifications" branch will trigger the notification to
                //be sent to the project's owner's device
                HashMap<String, Object> notificationMap = new HashMap<>();
                notificationMap.put("Sender", auth.getCurrentUser().getUid());
                notificationMap.put("Type", "CollabRequest");
                notificationMap.put("Time", System.currentTimeMillis());
                notificationMap.put("ProjectTitle", title);


                //notifications
                mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications").child(owner_id).child(title);

                mNotificationDatabase.setValue(notificationMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        //Update AllCollabsRequest branch -> Same level as Users
                        mDatabaseClone = FirebaseDatabase.getInstance().getReference()
                                .child("AllCollabsReq")  //Another branch of the same level as Users
                                .child(owner_id)
                                .child(title + auth.getCurrentUser().getUid());


                        //Get the project sender name
                        mSenderFullName = FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child(auth.getCurrentUser().getUid());

                        //Get the project owner's name
                        mOwnerFullName = FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(owner_id);

                        //Get the value of project owner name
                        mOwnerFullName.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String ownerName = dataSnapshot.child("FullName").getValue().toString();

                                ////////Get the value of the sender's name -> Only onDataChange method can obtain value of key from firebase
                                //We want sender's full name so that we can display on requestFragment for project owner side
                                mSenderFullName.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String senderName = dataSnapshot.child("FullName").getValue().toString();

                                        // Toast.makeText(Collaborate.this, senderName, Toast.LENGTH_LONG).show();

                                        final HashMap<String, Object> collabMapClone = new HashMap<>();

                                        collabMapClone.put("Pay", pay);
                                        collabMapClone.put("Duration", duration);
                                        collabMapClone.put("BufferWait", wait);
                                        collabMapClone.put("MaxChanges", maxChanges);
                                        collabMapClone.put("DateOfRequest", requestDate);
                                        collabMapClone.put("Partner", auth.getCurrentUser().getUid()); //partner is project requester id
                                        collabMapClone.put("SenderFullName", senderName);
                                        collabMapClone.put("OwnerFullName", ownerName);
                                        collabMapClone.put("Title", title);

                                        mDatabaseClone.setValue(collabMapClone);

                                        Toast.makeText(Collaborate.this, "Request for collaboration sent!", Toast.LENGTH_LONG).show();
                                        Intent here = new Intent(Collaborate.this, MainActivity.class);
                                        startActivity(here);
                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }); //update notification branch
            }
        });


    }
}