package com.example.shanna.orbital2;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import android.util.Log;

public class FeedbackForm extends AppCompatActivity {

    private TextView mFeedbackFor;
    private RatingBar mRating;
    private EditText mFeedback;
    private Button mBtnDone;
    private Button mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        final String other_user = getIntent().getStringExtra("user_id");

        mFeedbackFor = findViewById(R.id.feedbackFor);
        mRating = findViewById(R.id.ratingBarFeedback);
        mFeedback = findViewById(R.id.editTextFeedback);
        mBtnDone = findViewById(R.id.btnFeedback);
        mBtnBack = findViewById(R.id.btnBack);

        FirebaseDatabase.getInstance().getReference().child("Users").child(other_user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFeedbackFor.setText(dataSnapshot.child("FullName").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         final String feedback = mFeedback.getText().toString().trim();
                         final int rating = mRating.getNumStars();

                        final String userLeavingFeedback = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FullName").getValue().toString();

                        final HashMap<String, Object> feedbackMap = new HashMap<>();
                        feedbackMap.put("Rating", rating);
                        feedbackMap.put("Comments", feedback);

                         DatabaseReference thisdata = FirebaseDatabase.getInstance().getReference().child("Users").child(other_user).child("Feedback").child(userLeavingFeedback);
                         thisdata.setValue(feedbackMap);

                       // FirebaseDatabase.getInstance().getReference().child("Users").child(other_user).child("Feedback").child(userLeavingFeedback).setValue(feedbackMap);
                        Toast.makeText(FeedbackForm.this, "Thank you for leaving a feedback! :)", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                finish();
            }
        });
    }

}
