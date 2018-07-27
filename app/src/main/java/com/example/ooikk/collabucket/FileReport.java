package com.example.ooikk.collabucket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FileReport extends AppCompatActivity {

    private Button mBtnTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_report);

        mBtnTerms = findViewById(R.id.button4);

        mBtnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FileReport.this, Terms.class));
            }
        });
    }
}

