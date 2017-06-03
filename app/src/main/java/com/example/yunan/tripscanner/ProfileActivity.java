package com.example.yunan.tripscanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private DownloadProfileInfoTask mDownloadProfileInfoTask;
    private TextView mJob;
    private TextView mSchool;
    private TextView mMobileNum;
    private TextView mBirth;
    private TextView mLocale;
    private TextView mCountry;
    private TextView mIntroduction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("프로필");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                startActivity(intent); //액티비티 이동
            }
        });

        mJob = (TextView) findViewById(R.id.id_job);
        mSchool = (TextView) findViewById(R.id.id_school);
        mMobileNum = (TextView) findViewById(R.id.id_mobile_num);
        mBirth = (TextView) findViewById(R.id.id_birth);
        mLocale = (TextView) findViewById(R.id.id_locale);
        mCountry = (TextView) findViewById(R.id.id_country);
        mIntroduction = (TextView) findViewById(R.id.id_introduction);

        mDownloadProfileInfoTask = new DownloadProfileInfoTask();
        mDownloadProfileInfoTask.execute((Void)null);


    }



    public class DownloadProfileInfoTask extends AsyncTask<Void, Void, User> {
        DownloadProfileInfoTask() {

        }


        @Override
        protected User doInBackground(Void... params) {
            //attempt authentication against a network service.
            String response = "";



            CommunicationManager communication = new CommunicationManager();
            response = communication.GET("http://huy.dlinkddns.com/api/v1/users/me");

            if(response.contains("error")){
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            User user = new User();
            try {
                user = mapper.readValue(response, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return user;
        }

        @Override
        protected void onPostExecute(final User user) {
            mDownloadProfileInfoTask = null;
            if (user != null) {
                mJob.setText(user.getUser().get("job").toString());
                mSchool.setText(user.getUser().get("school").toString());
                mMobileNum.setText(user.getUser().get("mobile_number").toString());
                mBirth.setText(user.getUser().get("date_of_birth").toString());
                mLocale.setText(user.getUser().get("locale").toString());
                mCountry.setText(user.getUser().get("country").toString());
                mIntroduction.setText(user.getUser().get("introduction").toString());
            } else {

            }
        }
    }


}
