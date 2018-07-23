package com.example.shanna.orbital2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchBar extends AppCompatActivity {

    private EditText mEditTextSearch;

    private DatabaseReference mDatabaseRef;
    private FirebaseUser firebaseUser;
    private ArrayList<String> aboutList;
    private ArrayList<String> titleList;
    private ArrayList<String> ownerList;
    private ArrayList<String> aboutListAll;
    private ArrayList<String> titleListAll;
    private ArrayList<String> ownerListAll;
    private SearchAdapter searchAdapter;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        mEditTextSearch = findViewById(R.id.search_edit_text);
        recyclerView = findViewById(R.id.searchRecyclerView);

        aboutList = new ArrayList<>();
        titleList = new ArrayList<>();
        ownerList = new ArrayList<>();

        aboutListAll = new ArrayList<>();
        titleListAll = new ArrayList<>();
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
                            Iterable<DataSnapshot> innerDataSnapshotAll = snapshotAll.child("Projects").getChildren();

                            for (DataSnapshot innerSnapAll : innerDataSnapshotAll) {
                                if (innerSnapAll.child("ProjectStatus").getValue().toString().equals("Open")) {
                                    aboutListAll.add(innerSnapAll.child("ProjectSummary").getValue().toString());
                                    titleListAll.add(innerSnapAll.child("Title").getValue().toString());
                                    ownerListAll.add(innerSnapAll.child("Owner").getValue().toString());

                                    mAdapter = new SearchAdapter(SearchBar.this, aboutListAll, titleListAll, ownerListAll);
                                    recyclerView.setAdapter(mAdapter);
                                }
                            }
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
                    mAdapter = new SearchAdapter(SearchBar.this, aboutList, titleList, ownerList);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter = new SearchAdapter(SearchBar.this, aboutListAll, titleListAll, ownerListAll);
                    recyclerView.setAdapter(mAdapter);
                    /* clear list every time search bar is emptied
                    aboutList.clear();
                    titleList.clear();
                    ownerList.clear();
                    recyclerView.removeAllViews();
                     //list ALL projects when search bar is empty
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshotAll) {
                                    for (DataSnapshot snapshotAll : dataSnapshotAll.getChildren()) {
                                        Iterable<DataSnapshot> innerDataSnapshotAll = snapshotAll.child("Projects").getChildren();

                                        for (DataSnapshot innerSnapAll : innerDataSnapshotAll) {
                                            if (innerSnapAll.child("ProjectStatus").getValue().toString().equals("Open")
                                                    && !ownerList.contains(innerSnapAll.child("Owner").getValue().toString())) {
                                                aboutList.add(innerSnapAll.child("ProjectSummary").getValue().toString());
                                                titleList.add(innerSnapAll.child("Title").getValue().toString());
                                                ownerList.add(innerSnapAll.child("Owner").getValue().toString());

                                                mAdapter = new SearchAdapter(SearchBar.this, aboutList, titleList, ownerList);
                                                recyclerView.setAdapter(mAdapter);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled (@NonNull DatabaseError databaseError){

                                }
                            });*/
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
                titleList.clear();
                ownerList.clear();
                recyclerView.removeAllViews();

                //final int counter = 0;
                //Toast.makeText(SearchBar.this, s, Toast.LENGTH_SHORT).show();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Iterable<DataSnapshot> innerDataSnapshot = snapshot.child("Projects").getChildren();

                    for (DataSnapshot innerSnap : innerDataSnapshot) {

                        if (innerSnap.child("ProjectStatus").getValue().toString().equals("Open")) {
                            String about = innerSnap.child("ProjectSummary").getValue().toString();
                            String title = innerSnap.child("Title").getValue().toString();
                            String owner = innerSnap.child("Owner").getValue().toString();

                            if (about.toLowerCase().contains(s.toLowerCase()) || title.toLowerCase().contains(s.toLowerCase())) {
                                aboutList.add(about);
                                titleList.add(title);
                                ownerList.add(owner);
                                //Toast.makeText(SearchBar.this, title, Toast.LENGTH_SHORT).show();
                                //innerCounter++;

                                searchAdapter = new SearchAdapter(SearchBar.this, aboutList, titleList, ownerList);
                                recyclerView.setAdapter(searchAdapter);
                            }
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}