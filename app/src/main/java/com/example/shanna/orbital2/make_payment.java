package com.example.shanna.orbital2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class make_payment extends AppCompatActivity {

    private Button mBtnTerms;
    private TextView mTextViewprojectTitle;
    private TextView mTextViewamount;
    private Button mBtnpayNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        //From AcceptRequest accept button
        final String RequestSenderID = getIntent().getStringExtra("SenderID");
        final String project_title = getIntent().getStringExtra("project_title");

        mBtnTerms = findViewById(R.id.button4);
        mTextViewprojectTitle= findViewById(R.id.textViewTitle);
        mTextViewamount = findViewById(R.id.editTextPay);
        mBtnpayNow = findViewById(R.id.btnPay);

        mTextViewprojectTitle.setText(project_title);

        //If user click on Terms and Condition button
        mBtnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(make_payment.this, Terms.class));
            }
        });

        //if user clicks payNow button
        mBtnpayNow.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final String amount = mTextViewamount.getText().toString().trim();

                // check if quotation is empty
                if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(make_payment.this, "Enter payment", Toast.LENGTH_LONG).show();
                    return;
                }



                DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference()
                        .child("Payment")
                        .child(RequestSenderID)
                        .child(project_title);

                HashMap<String, String> map = new HashMap<>();
                map.put("Title", project_title);
                map.put("Amount", amount);
                map.put("PaymentMadeBy", FirebaseAuth.getInstance().getCurrentUser().getUid());

                mUserDatabase.setValue(map);

                Toast.makeText(make_payment.this, "Thank you for making the payment. We wish you all the best in your project!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(make_payment.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


}
