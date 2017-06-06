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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yunan on 2017-05-28.
 */

public class MemberChipAdapter extends RecyclerView.Adapter<MemberChipAdapter.ViewHolder>{
    Context context;
    ArrayList<HashMap<String,Object>> membersTemp;

    public MemberChipAdapter(Context context, Object obj) {
        this.context = context;
        this.membersTemp = (ArrayList<HashMap<String,Object>>) obj;
    }

    @Override
    public MemberChipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_chip_view, parent, false);
        return new ViewHolder(v);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String memberName = membersTemp.get(position).get("name").toString();

        holder.memberChipView.setText(memberName);


    }

    @Override
    public int getItemCount() {
        return this.membersTemp.size();
    }
    /** item layout 불러오기 **/
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView memberChipView;

        public ViewHolder(View v) {
            super(v);
            memberChipView = (TextView)  v.findViewById(R.id.detail_member_chip);

        }
    }


}
