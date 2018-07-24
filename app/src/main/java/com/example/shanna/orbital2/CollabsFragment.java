package com.example.shanna.orbital2;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CollabsFragment extends Fragment {

    private RecyclerView mCollabList;
    String current_id;

    public CollabsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_collabs_fragment, container, false);
        mCollabList = (RecyclerView) view.findViewById(R.id.Collabs_listView);
        mCollabList.setHasFixedSize(true);
        current_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mCollabList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        startListening();
    }

    public void startListening(){

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("SuccessfulCollaborations")
                .child(current_id);

        FirebaseRecyclerOptions<AllCollabsReq> options =
                new FirebaseRecyclerOptions.Builder<AllCollabsReq>()
                        .setQuery(query, AllCollabsReq.class)
                        .build();

        // Toast.makeText(getContext(), "id is " + current_id, Toast.LENGTH_SHORT).show();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<AllCollabsReq, CollabsFragment.CollabViewHolder>(options) {
            @Override
            public CollabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_single_collab, parent, false);
                //     .inflate(R.layout.users_project, parent, false);


                return new CollabViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final CollabViewHolder holder, int position, AllCollabsReq model) {
                // Bind the Chat object to the ChatHolder

                final String project_title = model.getTitle();
                final String SenderID = model.getPartner();
                final String SenderFullName = model.getSenderFullName();
                final String OwnerFullName = model.getOwnerFullName();

                holder.setTitle(model.getTitle());
                holder.setOwner(OwnerFullName);
                holder.setPartner(model.getSenderFullName());

                holder.mView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(getContext(), CollabView.class);
                        profileIntent.putExtra("project_title", project_title);
                        profileIntent.putExtra("SenderID", SenderID);
                        profileIntent.putExtra("SenderName", SenderFullName);
                        profileIntent.putExtra("OwnerName", OwnerFullName);
                        startActivity(profileIntent);
                    }
                });


            }

        };
        mCollabList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class CollabViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public CollabViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView titleView = mView.findViewById(R.id.collab_title);
            titleView.setText("PROJECT TITLE: " + title);
        }
        public void setPartner(String partner){
            TextView partnerView = mView.findViewById(R.id.collab_partner);
            partnerView.setText("PARTNER: " + partner);
        }
        public void setOwner(String owner) {
            TextView dateReqView = mView.findViewById(R.id.collab_owner);
            dateReqView.setText("OWNER: " + owner);
        }
    }

}