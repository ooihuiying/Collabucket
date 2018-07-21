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

public class FileProjectCompleted extends AppCompatActivity {


    private Button mBtnDone;
    private EditText mProjectTitle;
    private EditText mAdditionalPayment;

    DatabaseReference mUserDatabase;
    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_project_completed);

        mProjectTitle = findViewById(R.id.editTextPay);
        mAdditionalPayment = findViewById(R.id.editTextAdditionalPay);
        mBtnDone = findViewById(R.id.buttonDone);

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the project partner id
                //Need to do datachange in order to obtain the values from the key
                //Only project owner can fill up this form for this to work

                final String title = mProjectTitle.getText().toString().trim(); //get from textbox
                final String additionalPay = mAdditionalPayment.getText().toString().trim(); //get from textbox

                // check if quotation is empty
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(FileProjectCompleted.this, "Enter project title", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference mgetDatabase = FirebaseDatabase.getInstance().getReference();

                mgetDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Projects")
                        .child(title)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String partner_id = dataSnapshot.child("Partner").getValue().toString();

                                //after obtaining partner's id, then create a new branch called CompletedProjects -> Same level as Users
                                database = FirebaseDatabase.getInstance().getReference()
                                        .child("CompletedProjects")
                                        .child(partner_id)
                                        .child(title);


                                HashMap<String, String> map = new HashMap<>();
                                map.put("ProjectTitle", title);
                                map.put("AdditionalPay", additionalPay);
                                map.put("OwnerID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                map.put("SenderID",partner_id);

                                database.setValue(map);

                                ////Now need to update project status for owner
                                DatabaseReference updateOwnerProjectStatus =  FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("Projects")
                                        .child(title)
                                        .child("ProjectStatus");
                                updateOwnerProjectStatus.setValue("Completed");

                                ////Now need to update project status for partner/freelancer
                                DatabaseReference updatePartnerProjectStatus =  FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(partner_id)
                                        .child("Projects")
                                        .child(title)
                                        .child("ProjectStatus");
                                updatePartnerProjectStatus.setValue("Completed");

                                ///////////////////////////////////////////////////////////////////////////////////////////////////
                                Toast.makeText(FileProjectCompleted.this, "File for project completion is successful!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(FileProjectCompleted.this, MainActivity.class);
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
