package com.example.ooikk.collabucket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Rating extends AppCompatActivity {

    private Button mFinish;
    private ImageView mProfilePicture;
    private TextView mInstructions;
    private RatingBar mRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        mFinish = findViewById(R.id.btnFinish);
        mProfilePicture = findViewById(R.id.imageViewPP);
        mInstructions = findViewById(R.id.textViewInstructions);
        mRating = findViewById(R.id.ratingBar);

        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                //store new rating, recalculate average;
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Rating.this, Profile.class);
                startActivity(intent);
            }
        });
    }
}
