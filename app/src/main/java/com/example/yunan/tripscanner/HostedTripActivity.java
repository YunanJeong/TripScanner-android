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

public class HostedTripActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchTask mSearchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_hosted_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("내가 생성한 동행 목록");
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

    public class SearchTask extends AsyncTask<Void, Void, Trip> {

        @Override
        protected Trip doInBackground(Void... params) {
            //attempt authentication against a network service.

            CommunicationManager communication = new CommunicationManager();
            String searchResult = communication.GET("/api/v1/trips/owned");

            Trip trip = new Trip();
            ObjectMapper mapper = new ObjectMapper();
            try {
                trip = mapper.readValue(searchResult, Trip.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return trip;
        }

        @Override
        protected void onPostExecute(final Trip trip) {
            mSearchTask = null;

            RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), trip);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            mSearchTask = null;
        }

    }
}