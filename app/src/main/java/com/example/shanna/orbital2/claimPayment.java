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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class claimPayment extends AppCompatActivity {
    private Button mBtnDone;
    private EditText mProjectTitle;

    DatabaseReference mUserDatabase;
    DatabaseReference database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_payment);

        mProjectTitle = findViewById(R.id.editTextTitle);
        mBtnDone = findViewById(R.id.buttonDone);

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = mProjectTitle.getText().toString().trim(); //get from textbox

                //Toast.makeText(claimPayment.this, title, Toast.LENGTH_SHORT).show();

                // check if quotation is empty
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(claimPayment.this, "Enter project title", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference mgetDatabase = FirebaseDatabase.getInstance().getReference()
                                            .child("SuccessfulCollaborations")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(title+FirebaseAuth.getInstance().getCurrentUser().getUid());

               // Toast.makeText(claimPayment.this, title+FirebaseAuth.getInstance().getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();

                        mgetDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String pay = dataSnapshot.child("Pay").getValue().toString();
                                //Toast.makeText(claimPayment.this, pay, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(claimPayment.this, claim_payment_2.class);
                                intent.putExtra("Pay", pay);
                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
    }
}
