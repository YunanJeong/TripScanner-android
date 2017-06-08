package com.example.yunan.tripscanner;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends BaseNaviActivity {

    private DownloadUserInfoTask mDownloadUserInfoTask;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TripScanner");

        /*requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                200);*/
        //setSupportActionBar(toolbar);
        mDownloadUserInfoTask = new DownloadUserInfoTask();
        mDownloadUserInfoTask.execute((Void) null);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        super.onNavigationItemSelected(item);
        return true;
    }

    public class DownloadUserInfoTask extends AsyncTask<Void, Void, User> {


        DownloadUserInfoTask() {

        }



        @Override
        protected User doInBackground(Void... params) {
            //attempt authentication against a network service.

            CommunicationManager communication = new CommunicationManager();
            String result = communication.GET("/api/v1/users/me");

            User user = new User();
            ObjectMapper mapper = new ObjectMapper();
            try {
                user = mapper.readValue(result, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Get Image from URL
            bitmap = null;
            try {
                InputStream inputImageStream = new URL("http:"+user.getUser().get("image_medium").toString()).openStream();
                bitmap = BitmapFactory.decodeStream(inputImageStream);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return user;
        }

        @Override
        protected void onPostExecute(final User user) {
            mDownloadUserInfoTask = null;

            TextView navUserNameView = (TextView) findViewById(R.id.nav_user_name);
            TextView navEmailView = (TextView) findViewById(R.id.nav_user_email);
            navUserNameView.setText(user.getUser().get("name").toString());
            navEmailView.setText(user.getUser().get("email").toString());



            ImageView navUserImageView = (ImageView) findViewById(R.id.nav_user_image);
            //이미지 라운딩 처리
            navUserImageView.setBackground(new ShapeDrawable(new OvalShape()));
            navUserImageView.setClipToOutline(true);
            //이미지 삽입
            navUserImageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onCancelled() {
            mDownloadUserInfoTask = null;
        }

    }


}
