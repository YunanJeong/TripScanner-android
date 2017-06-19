package com.example.yunan.tripscanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PendingReviewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchTask mSearchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("리뷰 쓰기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Joined Result View
        View resultView = findViewById(R.id.include_pending_result_view);
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


            //리뷰할 목록들 불러오기
            searchResult = communication.GET("/api/v1/reviews/pending");



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

            PendingReviewRecyclerAdapter adapter = new PendingReviewRecyclerAdapter(getApplicationContext(), review);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            mSearchTask = null;
        }

    }

}
