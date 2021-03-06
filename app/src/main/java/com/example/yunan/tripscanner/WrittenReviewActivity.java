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

public class WrittenReviewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchTask mSearchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_hosted_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("내가 쓴 리뷰 목록");
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

        @Override
        protected Review doInBackground(Void... params) {
            //attempt authentication against a network service.

            CommunicationManager communication = new CommunicationManager();
            ObjectMapper mapper = new ObjectMapper();



            //내가 쓴 리뷰 목록들 불러오기
            searchResult = communication.GET("/api/v1/reviews/written");

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
