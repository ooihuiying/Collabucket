package com.example.shanna.orbital2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    Context context;
    ArrayList<String> aboutList;
    ArrayList<String> nameList;
    ArrayList<String> iconList;
    ArrayList<String> ownerList;

    class UsersViewHolder extends RecyclerView.ViewHolder {

        TextView about, name;
        ImageView icon;

        public UsersViewHolder(View itemView) {
            super(itemView);
            about = (TextView) itemView.findViewById(R.id.single_description);
            name = (TextView) itemView.findViewById(R.id.single_name);
            icon = (CircleImageView) itemView.findViewById(R.id.single_image);
        }
    }

    public UsersAdapter(Context context, ArrayList<String> about, ArrayList<String> name,
                         ArrayList<String> icon, ArrayList<String> owner) {
        this.context = context;
        this.aboutList = about;
        this.nameList = name;
        this.iconList = icon;
        this.ownerList = owner;
    }

    //When the Adapter creates its first item, onCreateViewHolder is called.
    // This is what allows the Adapter to reuse a reference to a view instead of
    // re-inflating it. Typically this implementation will just
    // inflate a view and return a ViewHolder object.
    public UsersAdapter.UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_users, parent, false);
        return new UsersAdapter.UsersViewHolder(view);
    }


    //onBindViewHolder() is called for each and every item and
    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersViewHolder holder, int position) {
        holder.name.setText(nameList.get(position));
        holder.about.setText(aboutList.get(position));
        Picasso.get().load(iconList.get(position)).placeholder(R.drawable.spaceman_1x).into(holder.icon);

        final String ownerId = ownerList.get(position);

        //  Toast.makeText(context,"id is "+ownerId, Toast.LENGTH_LONG).show();
        //  Toast.makeText(context,"Title is "+ title, Toast.LENGTH_LONG).show();

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //  Toast.makeText(context,"Loading ", Toast.LENGTH_LONG).show();
                Intent profileIntent = new Intent(context, ViewProfile.class);
                //pass user id of the project that the current user clicked
                profileIntent.putExtra("user_id", ownerId);
                context.startActivity(profileIntent);

/*
                final String project_title = getIntent().getStringExtra("Title");
                final String project_owner_id = getIntent().getStringExtra("Owner");
*/
            }
        });


    }

    public int getItemCount() {
        return nameList.size();
    }
}
