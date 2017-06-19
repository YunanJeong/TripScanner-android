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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by yunan on 2017-06-14.
 */

public class PendingReviewRecyclerAdapter extends RecyclerView.Adapter<PendingReviewRecyclerAdapter.ViewHolder> {

    Context context;
    Review review;
    String ownerId;
    public PendingReviewRecyclerAdapter(Context context, Object obj) {
        this.context = context;
        this.review = (Review) obj;
    }

    @Override
    public PendingReviewRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_pending_review, parent, false);
        return new PendingReviewRecyclerAdapter.ViewHolder(v);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(final PendingReviewRecyclerAdapter.ViewHolder holder, int position) {

        final HashMap<String, Object> temp;

        temp = (HashMap<String, Object>) review.getReviews().get(position); //temp가 리뷰 항목 한개
        final HashMap<String, Object> ownerTemp = (HashMap<String, Object>) temp.get("owner");

        DownloadImageTask downloadImageTask = new DownloadImageTask(ownerTemp.get("image_medium").toString(), holder.userImageView);
        downloadImageTask.execute((Void) null);

        holder.userNameTextView.setText(ownerTemp.get("name").toString());
        holder.reviewCheckInTextView.setText(temp.get("created_at").toString());
        holder.reviewCheckOutTextView.setText(temp.get("updated_at").toString());

        holder.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText (context, "리뷰가 제출되었습니다.", Toast.LENGTH_SHORT).show();
                ownerId = ownerTemp.get("id").toString();

                Review review = new Review();
                review.getReview().put("rate","");
                review.getReview().put("content","");

            }
        });


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
        EditText reviewContentTextView;
        TextView reviewCheckInTextView;
        TextView reviewCheckOutTextView;
        RatingBar ratingBar;
        Button submitButton;

        public ViewHolder(View v) {
            super(v);
            reviewCardView = (CardView)  v.findViewById(R.id.card_view_pending_review);
            userImageView = (ImageView) reviewCardView.findViewById(R.id.pend_review_user_image);
            userNameTextView = (TextView) reviewCardView.findViewById(R.id.pend_review_user_name);
            reviewContentTextView = (EditText) reviewCardView.findViewById(R.id.pend_review_content);
            reviewCheckInTextView = (TextView) reviewCardView.findViewById(R.id.pend_review_check_in);
            reviewCheckOutTextView = (TextView) reviewCardView.findViewById(R.id.pend_review_check_out);
            ratingBar = (RatingBar) reviewCardView.findViewById(R.id.pend_review_rating_bar);
            submitButton = (Button) reviewCardView.findViewById(R.id.pend_review_submit_button);

        }
    }


    public class ReviewWriteTask extends AsyncTask<Void, Void, Boolean> {
        Review mReview;
        public ReviewWriteTask(Review review) {
            this.mReview = review;
        }

        protected Boolean doInBackground(Void... params) {

            CommunicationManager communication = new CommunicationManager();
            communication.PUT("/api/v1/reviews/"+ownerId, review);

            return true;
        }

        protected void onPostExecute(Boolean success) {
            if(success){

            }
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