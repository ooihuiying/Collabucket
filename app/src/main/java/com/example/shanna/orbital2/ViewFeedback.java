package com.example.shanna.orbital2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ViewFeedback extends AppCompatActivity {

    private RecyclerView mRatingsList;
    private DatabaseReference mUsersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mRatingsList = (RecyclerView)findViewById(R.id.feedbackRecyclerView);
        mRatingsList.setHasFixedSize(true);
        mRatingsList.setLayoutManager(new LinearLayoutManager(this));
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
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Feedback");

        FirebaseRecyclerOptions<Feedback> options =
                new FirebaseRecyclerOptions.Builder<Feedback>()
                        .setQuery(query, Feedback.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Feedback, FeedbackViewHolder>(options) {
            @Override
            public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_feedback, parent, false);

                return new FeedbackViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(FeedbackViewHolder holder, int position, Feedback model) {
                // Bind the Chat object to the ChatHolder
                holder.setName(getRef(position).getKey());
                holder.setComments(model.getComments());
                // holder.setUserImage(model.getThumb_image());
                holder.setRating(model.getRating());
            }

        };
        mRatingsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public FeedbackViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name){
            TextView userNameView = (TextView) mView.findViewById(R.id.textViewUser);
            String toPost = "Feedback from " + name;
            userNameView.setText(toPost);
        }
        public void setComments(String description){
            TextView userDescription = (TextView) mView.findViewById(R.id.textViewComments);
            userDescription.setText(description);
        }
        //  public void setUserImage(String thumb_image) {
        public void setRating(int rating) {
            RatingBar ratingBar = mView.findViewById(R.id.ratingBarFinal);
            ratingBar.setRating(rating);
        }
    }
}
