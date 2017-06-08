package com.example.yunan.tripscanner;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yunan on 2017-06-08.
 */

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {

    Context context;
    Review review;

    public ReviewRecyclerAdapter(Context context, Object obj) {
        this.context = context;
        this.review = (Review) obj;
    }

    @Override
    public ReviewRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_review, parent, false);
        return new ReviewRecyclerAdapter.ViewHolder(v);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(final ReviewRecyclerAdapter.ViewHolder holder, int position) {

        final HashMap<String, Object> temp;

        temp = (HashMap<String, Object>) review.getReviews().get(position); //temp가 리뷰 항목 한개
        HashMap<String, Object> writerTemp = (HashMap<String, Object>) temp.get("writer");

        DownloadImageTask downloadImageTask = new DownloadImageTask(writerTemp.get("image_medium").toString(), holder.userImageView);
        downloadImageTask.execute((Void) null);

        holder.userNameTextView.setText(writerTemp.get("name").toString());
        holder.reviewCheckInTextView.setText(temp.get("created_at").toString());
        holder.reviewCheckOutTextView.setText(temp.get("updated_at").toString());
        holder.reviewContentTextView.setText(temp.get("content").toString());
        holder.ratingBar.setRating(Float.parseFloat(temp.get("rate").toString()));

    }

    @Override
    public int getItemCount() {
        return this.review.getReviews().size();
    }
    /** item layout 불러오기 **/
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView reviewCardView;
        ImageView userImageView;
        TextView userNameTextView;
        TextView reviewContentTextView;
        TextView reviewCheckInTextView;
        TextView reviewCheckOutTextView;
        RatingBar ratingBar;

        public ViewHolder(View v) {
            super(v);
            reviewCardView = (CardView)  v.findViewById(R.id.card_view_review);
            userImageView = (ImageView) reviewCardView.findViewById(R.id.review_user_image);
            userNameTextView = (TextView) reviewCardView.findViewById(R.id.review_user_name);
            reviewContentTextView = (TextView) reviewCardView.findViewById(R.id.review_content);
            reviewCheckInTextView = (TextView) reviewCardView.findViewById(R.id.review_check_in);
            reviewCheckOutTextView = (TextView) reviewCardView.findViewById(R.id.review_check_out);
            ratingBar = (RatingBar) reviewCardView.findViewById(R.id.review_rating_bar);

        }
    }


    public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
        ImageView mImageView;
        String mURL;
        public DownloadImageTask(String url, ImageView imageView) {
            this.mImageView = imageView;
            this.mURL = "http:"+ url;
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
}
