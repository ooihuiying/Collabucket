package com.example.ooikk.collabucket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Users_ProjectsList extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUserProjectList;
    private DatabaseReference mUsersDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users__projects_list);

        //For the user_clients to lead to the correct ViewProfile
        final String user_id = getIntent().getStringExtra("user_id");
        userId = user_id;

        mToolbar = (Toolbar) findViewById(R.id.toolbar_projects);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Projects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Projects");


        //+++++++++++++++++++
        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("ProjectsListed").child(user_id);

        //+++++++++++++++++++++++++++++++++++++++


        mUserProjectList = (RecyclerView)findViewById(R.id.projects_list);
        mUserProjectList.setHasFixedSize(true);
        mUserProjectList.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();
        startListening();

    }
    public void startListening(){


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(userId)
                .child("Projects");

        //+++++++++++++++++++++++++++++++++++
        /*
        Query query = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("ProjectsListed")
                                    .child(userId);
        //+++++++++++++++++++++++++++++++++++++*/
        FirebaseRecyclerOptions<Projects> options =
                new FirebaseRecyclerOptions.Builder<Projects>()
                        .setQuery(query, Projects.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Projects, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_project, parent, false);

                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final UserViewHolder holder, int position, Projects model) {
                // Bind the Chat object to the ChatHolder
                holder.setTitle(model.getTitle());
                holder.setProjectSummary(model.getProjectSummary());
                //holder.setUserImage(model.getThumb_image());

                final String owner_id = model.getOwner();
                final String title = model.getTitle(); //project title

               // Toast.makeText(Users_ProjectsList.this,"For debugging: User id is " + owner_id + " position is "+position, Toast.LENGTH_LONG).show();
                holder.mView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // if owner is clicking on their own project, display OwnProjectDetails class
                        if (owner_id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Intent intent = new Intent(Users_ProjectsList.this, OwnProjectDetails.class);
                            //pass user id of the project that the current user clicked
                            intent.putExtra("Owner", owner_id);
                            //need to pass the title of project too
                            intent.putExtra("Title", title);
                            startActivity(intent);
                        } else { // else will display ProjectDetails class
                            Intent profileIntent = new Intent(Users_ProjectsList.this, ProjectDetails.class);
                            //pass user id of the project that the current user clicked
                            profileIntent.putExtra("Owner", owner_id);
                            //need to pass the title of project too
                            profileIntent.putExtra("Title", title);
                            startActivity(profileIntent);
                        }
                    }
                });
            }

        };
        mUserProjectList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView titleView = (TextView) mView.findViewById(R.id.editTextPay);
            titleView.setText(title);
        }
        public void setProjectSummary(String projectSummary){
            TextView aboutView = (TextView) mView.findViewById(R.id.textViewProjectSummary);
            aboutView.setText(projectSummary);
        }
    }
}