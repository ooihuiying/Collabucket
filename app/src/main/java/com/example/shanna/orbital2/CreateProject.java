package com.example.shanna.orbital2;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateProject extends AppCompatActivity {

    private EditText mEditTextTitle;
    private EditText mEditTextProjectSummary;
    private EditText mEditTextQualifications;
    private EditText mEditTextResponsibilities;
    private EditText mEditTextPay;
    private EditText mEditTextDuration;
    private EditText mEditTextDateOfListing;


    private Button mBtnPost;

    private String projectStatus = "Open"; //by default, all projects are set as open
                                            //To be changed to closed and in progress when the deal is closed
                                            //Change to completed when the job is completed
                                            //Search for projects will then need to filter by this
                                            //status so that it will only display projects that are
                                            // current
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        mEditTextTitle = findViewById(R.id.editTextTitle);
        mEditTextProjectSummary = findViewById(R.id.editTextProjectSummary);
        mEditTextQualifications = findViewById(R.id.editTextQualifications);
        mEditTextResponsibilities = findViewById(R.id.editTextResponsibilities);
        mEditTextPay = findViewById(R.id.editTextPay);
        mEditTextDuration = findViewById(R.id.editTextDuration);
        mEditTextDateOfListing = findViewById(R.id.editTextDateOfListing);
        mBtnPost = findViewById(R.id.buttonPost);

        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String title = mEditTextTitle.getText().toString().trim(); //get from textbox
                final String projectSummary = mEditTextProjectSummary.getText().toString().trim(); //get from textbox
                final String qualifications = mEditTextQualifications.getText().toString().trim();
                final String responsibilities = mEditTextResponsibilities.getText().toString().trim();
                final String pay = mEditTextPay.getText().toString().trim(); //get from textbox
                final String duration = mEditTextDuration.getText().toString().trim(); //get from textbox
                final String dateOfListing = mEditTextDateOfListing.getText().toString().trim(); //get from textbox

                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                String current_uid = mCurrentUser.getUid();
                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("Projects").child(title);


                // check if title is empty
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(CreateProject.this, "Enter project title", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if description is empty
                if (TextUtils.isEmpty(projectSummary)) {
                    Toast.makeText(CreateProject.this, "Enter short description about project.", Toast.LENGTH_LONG).show();
                    return;
                }

                // check if project summary is of valid length
                if (projectSummary.length() > 500) {
                    Toast.makeText(CreateProject.this, "You have exceeded the 500 characters limit for project description.", Toast.LENGTH_LONG).show();
                    return;
                }

                // check if qualifications is empty
                if (TextUtils.isEmpty(qualifications)) {
                    Toast.makeText(CreateProject.this, "Enter qualifications and skills required.", Toast.LENGTH_LONG).show();
                    return;
                }

                // check if responsibilities is empty
                if (TextUtils.isEmpty(responsibilities)) {
                    Toast.makeText(CreateProject.this, "Enter responsibilities and duties of the position.", Toast.LENGTH_LONG).show();
                    return;
                }

                // check if phone number is empty
                if (TextUtils.isEmpty(pay)) {
                    Toast.makeText(CreateProject.this, "Enter base quotation.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if password is empty
                if (TextUtils.isEmpty(duration)) {
                    Toast.makeText(CreateProject.this, "Enter duration requirement.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if date of listing is empty
                if (TextUtils.isEmpty(dateOfListing)) {
                    Toast.makeText(CreateProject.this, "Enter today's date.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // project data
                final HashMap<String, Object> projectMap = new HashMap<>();

                projectMap.put("Owner", FirebaseAuth.getInstance().getCurrentUser().getUid());
                projectMap.put("Partner", " ");
                projectMap.put("Title", title);
                projectMap.put("ProjectSummary", projectSummary);
                projectMap.put("ProjectQualifications", qualifications);
                projectMap.put("ProjectResponsibilities", responsibilities);
                projectMap.put("Pay", pay);
                projectMap.put("Duration", duration);
                projectMap.put("BufferWait", "");
                projectMap.put("MaxChanges", "");
                projectMap.put("DateOfRequest", "");
                projectMap.put("DateOfListing", dateOfListing);
                projectMap.put("ProjectStatus", projectStatus);

                mUserDatabase.setValue(projectMap);


                Toast.makeText(CreateProject.this, "Project successfully listed!", Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }
}
