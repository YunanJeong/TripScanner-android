package com.example.yunan.tripscanner;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yunan on 2017-05-28.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    Trip trip;

    public RecyclerAdapter(Context context, Object obj) {
        this.context = context;
        if(obj instanceof Trip){
            trip = (Trip)obj;
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final HashMap<String,Object> temp;

        temp = (HashMap<String,Object>)trip.getTrips().get(position);
        HashMap <String, Object> ownerTemp = (HashMap<String, Object>) temp.get("owner");
        holder.addressView.setText(temp.get("address").toString());

        DownloadImageTask downloadImageTask = new DownloadImageTask(temp.get("image_medium").toString(), holder.imageView);
        downloadImageTask.execute((Void) null);

        holder.ownerNameView.setText(ownerTemp.get("name").toString());
        holder.checkInView.setText(temp.get("check_in").toString());
        holder.checkOutView.setText(temp.get("check_out").toString());

        //card item selected
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, TripDetailActivity.class);
                //Send selected item information to TripDetailActivity
                intent.putExtra("Trip",temp);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.trip.getTrips().size();
    }
    /** item layout 불러오기 **/
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView addressView;
        ImageView imageView;
        TextView ownerNameView;
        TextView checkInView;
        TextView checkOutView;


        public ViewHolder(View v) {
            super(v);

            cardView = (CardView) v.findViewById(R.id.card_view);
            addressView = (TextView) cardView.findViewById(R.id.title_address);
            imageView = (ImageView) cardView.findViewById(R.id.image);
            ownerNameView = (TextView) cardView.findViewById(R.id.owner_name);
            checkInView = (TextView) cardView.findViewById(R.id.check_in);
            checkOutView = (TextView) cardView.findViewById(R.id.check_out);

        }
    }

    public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap>{
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
