package com.example.ooikk.collabucket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserGuide extends AppCompatActivity {

    public Button mButtonBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        mButtonBegin = findViewById(R.id.ButtonBegin);

        mButtonBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Signup successful, go to user guide
                startActivity(new Intent(UserGuide.this, MainActivity.class));
                // End the activity
                finish();
            }
        });
    }
}
