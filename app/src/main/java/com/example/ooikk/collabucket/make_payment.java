package com.example.ooikk.collabucket;

// Youtube tutorial link
// https://www.youtube.com/watch?v=k5lPy_50f0Y

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ooikk.collabucket.ConfigPaypal.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.HashMap;

public class make_payment extends AppCompatActivity {

    private Button mBtnTerms;
    private TextView mTextViewprojectTitle;
    private TextView mTextViewamount;
    private Button mBtnpayNow;

    public static final int PAYPAL_REQUEST_CODE = 7171;

    private  static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onDestroy() {
        stopService(make_payment.this, PayPalService.class);
        super.onDestroy();
    }

    private void stopService(make_payment make_payment, Class<PayPalService> payPalServiceClass) {
    }

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


                //paypal service start
                Intent intent2 = new Intent(make_payment.this, PayPalService.class);
                intent2.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                startService(intent2);

                //Payment paypal
                processPayment();


               // Toast.makeText(make_payment.this, "Thank you for making the payment. We wish you all the best in your project!", Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(make_payment.this, MainActivity.class);
               // startActivity(intent);
              //  finish();
            }
        });


    }

    private void processPayment(){
        final String amount = mTextViewamount.getText().toString().trim();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "SGD",
                "Make payment", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final String amount = mTextViewamount.getText().toString().trim();
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation!=null) {
                    String paymentDetails = confirmation.toJSONObject().toString();

                    startActivity(new Intent(this, PaymentDetails.class)
                            .putExtra("PaymentDetails", paymentDetails)
                            .putExtra("PaymentAmount", amount));

                }
            } else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }



    }

}
