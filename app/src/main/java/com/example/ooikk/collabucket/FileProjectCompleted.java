package com.example.ooikk.collabucket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView mProjectTitle;
    private EditText mAdditionalPayment;

    DatabaseReference mUserDatabase;
    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_project_completed);

        mProjectTitle = findViewById(R.id.textViewTitle);
        mAdditionalPayment = findViewById(R.id.editTextAdditionalPay);
        mBtnDone = findViewById(R.id.buttonDone);

        final String project_title = getIntent().getStringExtra("project_title");

        mProjectTitle.setText(project_title);

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the project partner id
                //Need to do datachange in order to obtain the values from the key
                //Only project owner can fill up this form for this to work

                final String additionalPay = mAdditionalPayment.getText().toString().trim(); //get from textbox

                // check if quotation is empty
                if (TextUtils.isEmpty(additionalPay)) {
                    Toast.makeText(FileProjectCompleted.this, "Please enter additional pay amount.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference mgetDatabase = FirebaseDatabase.getInstance().getReference();


               /* mgetDatabase.child("ProjectsListed").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(project_title)*/

               /*
                mgetDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Projects")
                        .child(project_title)

                        */
               mgetDatabase.child("SuccessfulCollaborations")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(project_title+FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String partner_id = dataSnapshot.child("Partner").getValue().toString();
                                Toast.makeText(FileProjectCompleted.this, "Partner id is " + partner_id, Toast.LENGTH_SHORT).show();
                                //after obtaining partner's id, then create a new branch called CompletedProjects -> Same level as Users
                                database = FirebaseDatabase.getInstance().getReference()
                                        .child("CompletedProjects")
                                        .child(partner_id)
                                        .child(project_title);


                                HashMap<String, String> map = new HashMap<>();
                                map.put("ProjectTitle", project_title);
                                map.put("AdditionalPay", additionalPay);
                                map.put("OwnerID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                map.put("SenderID",partner_id);

                                database.setValue(map);

                                ////Now need to update project status for partner/freelancer
       ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                //This doesn't get updated even though the code below is to update freelancer's project details.
                                DatabaseReference updatePartnerProjectStatus =  FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(partner_id)
                                        .child("Projects")
                                        .child(project_title)
                                        .child("ProjectStatus");
                                updatePartnerProjectStatus.setValue("Completed");

                                ////Now need to update project status for owner

                                DatabaseReference updateOwnerProjectStatus =  FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("Projects")
                                        .child(project_title)
                                        .child("ProjectStatus");
                                updateOwnerProjectStatus.setValue("Completed");
                                //////////////////////////////////////////////////////////////////////////////v//////////////////////

                                //Update for successful collaboration -> Try
                                DatabaseReference upup = FirebaseDatabase.getInstance().getReference()
                                            .child("SuccessfulCollaborations")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(project_title+FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("ProjectStatus");
                                upup.setValue("Completed");


                                //Update for successful collaboration -> Try
                                DatabaseReference upup2 = FirebaseDatabase.getInstance().getReference()
                                            .child("SuccessfulCollaborations")
                                            .child(partner_id)
                                            .child(project_title+partner_id)
                                            .child("ProjectStatus");
                                upup2.setValue("Completed");


                                /*
                                DatabaseReference updateOwnerProjectStatus =  FirebaseDatabase.getInstance().getReference()
                                        .child("ProjectsListed").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(project_title)
                                        .child("ProjectStatus");
                                updateOwnerProjectStatus.setValue("Completed");


                                ////Now need to update project status for partner/freelancer
                                DatabaseReference updatePartnerProjectStatus =  FirebaseDatabase.getInstance().getReference()
                                        .child("ProjectsListed").child(partner_id)
                                        .child(project_title)
                                        .child("ProjectStatus");
                                updatePartnerProjectStatus.setValue("Completed");
                                 */
                                ///////////////////////////////////////////////////////////////////////////////////////////////////
                                Toast.makeText(FileProjectCompleted.this, "Congratulations! Project is successfully completed. :)", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(FileProjectCompleted.this, FeedbackForm.class);
                                intent.putExtra("user_id", partner_id);
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
