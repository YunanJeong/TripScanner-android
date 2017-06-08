package com.example.yunan.tripscanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class OwnedReviewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchTask mSearchTask;
    private String mUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_hosted_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("리뷰 목록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Joined Result View
        View resultView = findViewById(R.id.include_joined_hosted_view);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) resultView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSearchTask = new SearchTask();
        mSearchTask.execute((Void) null);
    }

    public class SearchTask extends AsyncTask<Void, Void, Review> {

        String searchResult = null;
        String hostInfo = (String) getIntent().getSerializableExtra("userId");
        String hostName = (String) getIntent().getSerializableExtra("name");

        @Override
        protected Review doInBackground(Void... params) {
            //attempt authentication against a network service.

            CommunicationManager communication = new CommunicationManager();
            ObjectMapper mapper = new ObjectMapper();


            if(hostInfo == null){
                //현재 로그인한 유저 정보 받아오기
                String userInfo = communication.GET("/api/v1/users/me");

                User user = new User();
                try {
                    user = mapper.readValue(userInfo, User.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //툴바제목에 유저이름 달아주기
                mUserName = user.getUser().get("name").toString();
                //리뷰 당한 목록들 불러오기
                searchResult = communication.GET("/api/v1/users/"+ user.getUser().get("id").toString() +"/reviews/owned");
            }
            else{
                //툴바제목에 호스트이름 달아주기
                mUserName = hostName;
                //리뷰 당한 목록들 불러오기
                searchResult = communication.GET("/api/v1/users/"+ hostInfo +"/reviews/owned");
            }



            Review review = new Review();
            try {
                review = mapper.readValue(searchResult, Review.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return review;
        }

        @Override
        protected void onPostExecute(final Review review) {
            mSearchTask = null;

            //툴바제목에 유저이름 달아주기
            getSupportActionBar().setTitle(mUserName+"님이 받은 리뷰목록");

            ReviewRecyclerAdapter adapter = new ReviewRecyclerAdapter(getApplicationContext(), review);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            mSearchTask = null;
        }

    }


}
