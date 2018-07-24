package com.example.shanna.orbital2;


//Note that load thumb_image doesn't work. Currently loading image instead of thumb_image.

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Users_Clients extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mClientsList;
    private DatabaseReference mUsersDatabase;

    private EditText mEditTextSearch;

    private DatabaseReference mDatabaseRef;
    private FirebaseUser firebaseUser;
    private ArrayList<String> nameList;
    private ArrayList<String> aboutList;
    private ArrayList<String> iconList;
    private ArrayList<String> ownerList;
    private ArrayList<String> aboutListAll;
    private ArrayList<String> nameListAll;
    private ArrayList<String> iconListAll;
    private ArrayList<String> ownerListAll;
    private UsersAdapter usersAdapter;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users__clients);

        mEditTextSearch = findViewById(R.id.searchUsers);
        recyclerView = findViewById(R.id.clients_list);

        aboutList = new ArrayList<>();
        nameList = new ArrayList<>();
        iconList = new ArrayList<>();
        ownerList = new ArrayList<>();

        aboutListAll = new ArrayList<>();
        nameListAll = new ArrayList<>();
        iconListAll = new ArrayList<>();
        ownerListAll = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);

        // use linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        // list ALL projects when search bar is empty
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshotAll) {
                        for (DataSnapshot snapshotAll : dataSnapshotAll.getChildren()) {

                            aboutListAll.add(snapshotAll.child("Description").getValue().toString());
                            nameListAll.add(snapshotAll.child("FullName").getValue().toString());
                            iconListAll.add(snapshotAll.child("Image").getValue().toString());
                            ownerListAll.add(snapshotAll.getKey());

                            mAdapter = new UsersAdapter(Users_Clients.this, aboutListAll, nameListAll, iconListAll, ownerListAll);
                            recyclerView.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onCancelled (@NonNull DatabaseError databaseError){

                    }
                });


        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().isEmpty()) {
                    setUpAdapter(editable.toString());
                    // specify adapter
                    mAdapter = new UsersAdapter(Users_Clients.this, aboutList, nameList, iconList, ownerList);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter = new UsersAdapter(Users_Clients.this, aboutListAll, nameListAll, iconListAll, ownerListAll);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    private void setUpAdapter(final String s) {
        // fill up lists with needed data
        final DatabaseReference mRef;
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear list for every new search (database is async)
                aboutList.clear();
                nameList.clear();
                iconList.clear();
                ownerList.clear();
                recyclerView.removeAllViews();

                //final int counter = 0;
                //Toast.makeText(SearchBar.this, s, Toast.LENGTH_SHORT).show();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String about = snapshot.child("Description").getValue().toString();
                    String name = snapshot.child("FullName").getValue().toString();
                    String icon = snapshot.child("Image").getValue().toString();
                    String owner = snapshot.getKey();

                            if (about.toLowerCase().contains(s.toLowerCase()) || name.toLowerCase().contains(s.toLowerCase())) {
                                aboutList.add(about);
                                nameList.add(name);
                                iconList.add(icon);
                                ownerList.add(owner);
                                //Toast.makeText(SearchBar.this, title, Toast.LENGTH_SHORT).show();
                                //innerCounter++;

                                usersAdapter = new UsersAdapter(Users_Clients.this, aboutList, nameList, iconList, ownerList);
                                recyclerView.setAdapter(usersAdapter);
                            }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
