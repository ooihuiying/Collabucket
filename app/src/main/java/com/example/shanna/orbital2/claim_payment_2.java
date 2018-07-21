package com.example.shanna.orbital2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class claim_payment_2 extends AppCompatActivity {

    private Button mBtnDone;
    private TextView mProjectPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_payment_2);

        mProjectPay = findViewById(R.id.TextViewPay);
        mBtnDone = findViewById(R.id.buttonDone);


        //From claimPayment.java
        String pay = getIntent().getStringExtra("Pay");
       // Toast.makeText(claim_payment_2.this, "pay is" + pay, Toast.LENGTH_LONG).show();

        mProjectPay.setText(pay);

        mBtnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(claim_payment_2.this, "Payment will be transferred to your account!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(claim_payment_2.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
