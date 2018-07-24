//This one


package com.example.shanna.orbital2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    private TabLayout mTabLayout;
    private FrameLayout frameLayout;
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private EditText mEditTextSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // search bar
        mEditTextSearch = findViewById(R.id.editTextSearch);
        mEditTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchBar.class));
            }
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Check if user is logged in
        // If user is not logged in, direct user to login page. Else, will stay at homepage.xml
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); //prevent activities from stacking up: once prev activity no needed, finish()
        }

        //The 5 lines below is for toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //3 lines below is for the 3 horizontal lines for sliding drawer
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.profile:
                                mDrawerLayout.closeDrawers();
                                Intent intent = new Intent(MainActivity.this, ViewProfile.class);
                                intent.putExtra("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                startActivity(intent);
                                break;
                            case R.id.settings: //for them to change their account details
                                mDrawerLayout.closeDrawers();
                                startActivity(new Intent(MainActivity.this, Profile.class));
                                break;
                            case R.id.Users_Clients:
                                mDrawerLayout.closeDrawers();
                                startActivity(new Intent(MainActivity.this, Users_Clients.class));
                                break;
                            case R.id.Report:
                                mDrawerLayout.closeDrawers();
                                startActivity(new Intent(MainActivity.this, FileReport.class));
                                break;
                            case R.id.UserGuide:
                                mDrawerLayout.closeDrawers();
                                startActivity(new Intent(MainActivity.this, UserGuide.class));
                                break;
                            case R.id.logout:
                                mDrawerLayout.closeDrawers();
                                userLogout();
                                break;

                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });


        //Tabs -> Collabs, Requests

        mTabLayout=(TabLayout)findViewById(R.id.tabLayout);
        frameLayout=(FrameLayout)findViewById(R.id.content_frame);

        fragment = new CollabsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new CollabsFragment();
                        break;
                    case 1:
                        fragment = new RequestFragment();
                        break;
                    default:
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.new_listing):
                startActivity(new Intent(MainActivity.this, CreateProject.class));
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Signout User and return user to LoginActivity
    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent2);
        finish();
    }




}