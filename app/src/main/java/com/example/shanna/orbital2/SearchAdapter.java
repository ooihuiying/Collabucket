//Owner's id is displayed for debugging-> But can remove it

package com.example.shanna.orbital2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;
    ArrayList<String> aboutList;
    ArrayList<String> titleList;
    ArrayList<String> ownerList;

    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView about, title, owner;
        ImageView image;

        public SearchViewHolder(View itemView) {
            super(itemView);
            about = (TextView) itemView.findViewById(R.id.search_about);
            title = (TextView) itemView.findViewById(R.id.search_title);
           // owner = (TextView) itemView.findViewById(R.id.search_owner);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> about, ArrayList<String> title,
                         ArrayList<String> owner) {
        this.context = context;
        this.aboutList = about;
        this.titleList = title;
        this.ownerList = owner;
    }

    //When the Adapter creates its first item, onCreateViewHolder is called.
    // This is what allows the Adapter to reuse a reference to a view instead of
    // re-inflating it. Typically this implementation will just
    // inflate a view and return a ViewHolder object.
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.single_search, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }


    //onBindViewHolder() is called for each and every item and
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.title.setText(titleList.get(position));
        holder.about.setText(aboutList.get(position));
        //holder.owner.setText(ownerList.get(position));

        //if got time, can look into loading images here.

        final String title = titleList.get(position);
        final String ownerId = ownerList.get(position);

        //  Toast.makeText(context,"id is "+ownerId, Toast.LENGTH_LONG).show();
        //  Toast.makeText(context,"Title is "+ title, Toast.LENGTH_LONG).show();

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //  Toast.makeText(context,"Loading ", Toast.LENGTH_LONG).show();
                Intent profileIntent = new Intent(context, ProjectDetails.class);
                //pass user id of the project that the current user clicked
                profileIntent.putExtra("Owner", ownerId);
                //need to pass the title of project too
                profileIntent.putExtra("Title", title);
                context.startActivity(profileIntent);

/*
                final String project_title = getIntent().getStringExtra("Title");
                final String project_owner_id = getIntent().getStringExtra("Owner");
*/
            }
        });


    }

    public int getItemCount() {
        return titleList.size();
    }

}
