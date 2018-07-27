package com.example.ooikk.collabucket;

import com.google.firebase.database.FirebaseDatabase;

public class Feedback {
    private Integer Rating;
    private String Comments;

    public Feedback(){

    }

    public Feedback(Integer rating, String comments){
        this.Rating = rating;
        this.Comments = comments;
    }

    public Integer getRating() {
        return Rating;
    }

    public void setRating(Integer rating) {
        this.Rating = rating;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) { this.Comments = comments; }
}
