package com.example.yunan.tripscanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class BaseNaviActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private int mCurrentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_navi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("BaseNaviActivity");
        //setSupportActionBar(toolbar);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*// Handle the case that opening item is selected again
        if(id == mCurrentId){
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        mCurrentId = id;*/


        if (id == R.id.nav_search) {
            Intent intent = new Intent(this, ScrollingSearchActivity.class) ;
            startActivity(intent) ;
        } else if (id == R.id.nav_joined_trip) {
            Intent intent = new Intent(this, JoinedTripActivity.class) ;
            startActivity(intent) ;
        } else if (id == R.id.nav_hosted_trip) {
            Intent intent = new Intent(this, HostedTripActivity.class) ;
            startActivity(intent) ;
        } else if (id == R.id.nav_owned_review) {
            Intent intent = new Intent(this, OwnedReviewActivity.class) ;
            startActivity(intent) ;
        } else if (id == R.id.nav_written_review) {
            Intent intent = new Intent(this, WrittenReviewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {
            ProfileManager.getInstance().saveUserEmail("");
            ProfileManager.getInstance().saveUserToken("");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectNewsFeed(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("NewsFeed");

        Fragment fragment = null;
        fragment = new NewsFeedFragment();
        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_blank, fragment);
            ft.commit();
        }
    }
}
