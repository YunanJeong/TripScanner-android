package com.example.yunan.tripscanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ProfileEditActivity extends AppCompatActivity {

    private ProfileSaveTask mProfileSaveTask;
    private EditText mJob;
    private EditText mSchool;
    private EditText mMobileNum;
    private EditText mBirth;
    private EditText mLocale;
    private EditText mCountry;
    private EditText mIntroduction;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        mJob = (EditText) findViewById(R.id.editText_Job) ;
        mSchool = (EditText) findViewById(R.id.editText_School) ;
        mMobileNum = (EditText) findViewById(R.id.editText_Phone) ;
        mBirth = (EditText) findViewById(R.id.editText_Birth) ;
        mLocale = (EditText) findViewById(R.id.editText_Location) ;
        mCountry = (EditText) findViewById(R.id.editText_Country) ;
        mIntroduction = (EditText) findViewById(R.id.editText_Introduce) ;
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_Gender);

        User originalUserInfo = (User) getIntent().getSerializableExtra("User");
        mJob.setText(originalUserInfo.getUser().get("job").toString());
        mSchool.setText(originalUserInfo.getUser().get("school").toString());
        mMobileNum.setText(originalUserInfo.getUser().get("mobile_number").toString());
        mBirth.setText(originalUserInfo.getUser().get("date_of_birth").toString());
        mLocale.setText(originalUserInfo.getUser().get("locale").toString());
        mCountry.setText(originalUserInfo.getUser().get("country").toString());
        mIntroduction.setText(originalUserInfo.getUser().get("introduction").toString());
        if(originalUserInfo.getUser().get("gender").toString().contains("male")){
            mRadioGroup.check(R.id.radio_Male);
        }
        else{
            mRadioGroup.check(R.id.radio_Female);
        }



        Button saveButton = (Button) findViewById(R.id.button_Save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.getUser().put("date_of_birth", mBirth.getText().toString());
                user.getUser().put("mobile_number",mMobileNum.getText().toString());
                user.getUser().put("locale",mLocale.getText().toString());
                user.getUser().put("country",mCountry.getText().toString());
                user.getUser().put("introduction",mIntroduction.getText().toString());
                user.getUser().put("school",mSchool.getText().toString());
                user.getUser().put("job",mJob.getText().toString());
                int checkedGenderButtonId = mRadioGroup.getCheckedRadioButtonId();
                if(checkedGenderButtonId == -1){
                    //No item selected
                }
                else{
                    if(checkedGenderButtonId == R.id.radio_Male){
                        user.getUser().put("gender","male");
                    }
                    else{
                        user.getUser().put("gender","female");
                    }
                }



                mProfileSaveTask = new ProfileSaveTask(user);
                mProfileSaveTask.execute((Void) null);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public class ProfileSaveTask extends AsyncTask<Void, Void, Boolean>{

        private User changedUserInfo;

        ProfileSaveTask(User user) {
            changedUserInfo = user;
        }



        @Override
        protected Boolean doInBackground(Void... params) {
            //attempt authentication against a network service.
            String response = "";



            CommunicationManager communication = new CommunicationManager();


            response = communication.PUT("/api/v1/users/me", changedUserInfo);

            if(response.contains("error")){
                return false;
            }

            /*ObjectMapper mapper = new ObjectMapper();
            try {
                user = mapper.readValue(response, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }*/


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mProfileSaveTask = null;

            if (success) {
                Toast.makeText (getApplicationContext(), "수정 성공. 다시 켜서 확인 가능.", Toast.LENGTH_SHORT).show();
                finish();
            } else {

            }
        }
    }


}
