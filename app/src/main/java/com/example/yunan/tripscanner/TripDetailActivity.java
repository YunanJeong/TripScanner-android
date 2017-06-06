package com.example.yunan.tripscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class TripDetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        final HashMap<String,Object> tripTemp = (HashMap<String,Object>)  getIntent().getSerializableExtra("Trip");
        HashMap<String,Object> ownerTemp = (HashMap<String,Object>) tripTemp.get("owner");
        ArrayList<HashMap<String,Object>> membersTemp = (ArrayList<HashMap<String,Object>>) tripTemp.get("members");

        setTitle(tripTemp.get("address").toString());

        ImageView imageView = (ImageView) findViewById(R.id.detail_image);
        DownloadImageTask downloadImageTask = new DownloadImageTask(tripTemp.get("image_medium").toString() , imageView);
        downloadImageTask.execute((Void) null);

        TextView addressTextView = (TextView) findViewById(R.id.detail_address);
        TextView checkInTextView = (TextView) findViewById(R.id.detail_check_in);
        TextView checkOutTextView = (TextView) findViewById(R.id.detail_check_out);
        TextView contentTextView = (TextView) findViewById(R.id.detail_trip_content);
        addressTextView.setText(tripTemp.get("address").toString());
        checkInTextView.setText(tripTemp.get("check_in").toString());
        checkOutTextView.setText(tripTemp.get("check_out").toString());
        contentTextView.setText(tripTemp.get("content").toString());

        Button joinButton = (Button) findViewById(R.id.detail_join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinTripTask joinTripTask = new JoinTripTask(tripTemp.get("id").toString());
                joinTripTask.execute((Void) null);
            }
        });

        ImageView hostImageView = (ImageView) findViewById(R.id.detail_host_image);
        DownloadImageTask downloadHostImageTask = new DownloadImageTask(ownerTemp.get("image_medium").toString(), hostImageView);
        downloadHostImageTask.execute((Void) null);

        TextView hostNameTextView = (TextView) findViewById(R.id.detail_host_name);
        TextView schoolChip = (TextView) findViewById(R.id.detail_host_profile_school);
        TextView jobChip = (TextView) findViewById(R.id.detail_host_profile_job);
        TextView localeChip = (TextView) findViewById(R.id.detail_host_profile_locale);
        TextView countryChip = (TextView) findViewById(R.id.detail_host_profile_country);
        hostNameTextView.setText(ownerTemp.get("name").toString());
        schoolChip.setText(ownerTemp.get("school").toString());
        jobChip.setText(ownerTemp.get("job").toString());
        localeChip.setText(ownerTemp.get("locale").toString());
        countryChip.setText(ownerTemp.get("country").toString());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView memberChipRecyclerView = (RecyclerView) findViewById(R.id.detail_member_chip_recycle) ;
        memberChipRecyclerView.setHasFixedSize(true);
        memberChipRecyclerView.setLayoutManager(linearLayoutManager);
        MemberChipAdapter adapter = new MemberChipAdapter(getApplicationContext(), membersTemp);
        memberChipRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
        ImageView mImageView;
        String mURL;

        public DownloadImageTask(String url, ImageView imageView) {
            this.mImageView = imageView;
            this.mURL = "http:" + url;
        }

        protected Bitmap doInBackground(Void... params) {
            //Get Image from URL
            Bitmap bitmap = null;
            try {
                InputStream inputImageStream = new URL(mURL).openStream();
                bitmap = BitmapFactory.decodeStream(inputImageStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            //input image to imageView in cardView.
            mImageView.setImageBitmap(bitmap);
        }
    }

    public class JoinTripTask extends AsyncTask<Void, Void, Boolean> {

        private String tripId;

        public JoinTripTask(String id) {
            this.tripId = id;
        }

        protected Boolean doInBackground(Void... params) {
            CommunicationManager communication = new CommunicationManager();
            String response = communication.PUT("/api/v1/trips/"+tripId+"/join", null);  //ex /api/v1/trips/3/join
            if(response.contains("error")){
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean success) {
            if(success){
                Toast.makeText (getApplicationContext(), "등록 성공. Joined Trip에서 확인가능.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText (getApplicationContext(), "등록 실패. 재접속 해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
