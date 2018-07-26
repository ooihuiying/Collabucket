package com.example.shanna.orbital2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    private RecyclerView mSenderList;
    String current_id;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_request_fragment, container, false);
       // View view = inflater.inflate(R.layout.activity_users__projects_list, container, false);

        mSenderList = (RecyclerView) view.findViewById(R.id.Request_listView);
       // mSenderList = (RecyclerView) view.findViewById(R.id.projects_list);
        mSenderList.setHasFixedSize(true);
        current_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mSenderList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        startListening();
    }

    public void startListening(){

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("AllCollabsReq")
                .child(current_id);

        FirebaseRecyclerOptions<AllCollabsReq> options =
                new FirebaseRecyclerOptions.Builder<AllCollabsReq>()
                        .setQuery(query, AllCollabsReq.class)
                        .build();

       // Toast.makeText(getContext(), "id is " + current_id, Toast.LENGTH_SHORT).show();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<AllCollabsReq, RequestFragment.ReqViewHolder>(options) {
            @Override
            public RequestFragment.ReqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_collab_req_list, parent, false);
                   //     .inflate(R.layout.users_project, parent, false);


                return new RequestFragment.ReqViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ReqViewHolder holder, int position, AllCollabsReq model) {
                // Bind the Chat object to the ChatHolder

                holder.setTitle(model.getTitle());
                holder.setSenderFullName(model.getSenderFullName());

                final String pay = model.getPay();
                final String duration = model.getDuration();
                final String wait = model.getBufferWait();
                final String maxChanges = model.getMaxChanges();
                final String requestDate = model.getDateOfRequest();
                final String partner = model.getPartner();
                final String senderFullName = model.getSenderFullName();
                final String project_title = model.getTitle();

              //  Toast.makeText(getContext(),"For debugging, partner id is"+ partner, Toast.LENGTH_LONG).show();

                holder.mView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent = new Intent(getContext(), AcceptRequest.class);
                        profileIntent.putExtra("pay", pay);
                        profileIntent.putExtra("duration", duration);
                        profileIntent.putExtra("bufferWait", wait);
                        profileIntent.putExtra("maxChanges", maxChanges);
                        profileIntent.putExtra("requestDate", requestDate);
                        profileIntent.putExtra("partnerID", partner); //project requester id
                        profileIntent.putExtra("senderFullName", senderFullName);
                        profileIntent.putExtra("projectTitle", project_title);
                        startActivity(profileIntent);
                    }
                });




            }

        };
        mSenderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ReqViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public ReqViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView titleView = mView.findViewById(R.id.ProjectTitle);
           // TextView titleView = (TextView) mView.findViewById(R.id.textViewTitle);
            titleView.setText("PROJECT TITLE: " + title);
        }
        public void setSenderFullName(String sender){
            TextView aboutView = mView.findViewById(R.id.SenderName);
            //TextView aboutView = (TextView) mView.findViewById(R.id.textViewProjectSummary);
            aboutView.setText("YOU HAVE A REQUEST FROM: " + sender +"\n");
        }
    }

}



