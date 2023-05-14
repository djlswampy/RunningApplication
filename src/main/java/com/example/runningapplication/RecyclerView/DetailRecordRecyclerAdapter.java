package com.example.runningapplication.RecyclerView;


import static com.example.runningapplication.Login.user;
import static com.example.runningapplication.RecyclerView.RecyclerAdapter.getItemPosition;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.R;

public class DetailRecordRecyclerAdapter extends RecyclerView.Adapter<DetailRecordRecyclerAdapter.MyViewHolder>{
    Context context;

    int setItemPosition = getItemPosition;


    public DetailRecordRecyclerAdapter(Context context) {
        this.context = context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("recyclerCheck", "onCreateViewHolder");

        View view = LayoutInflater.from(context).inflate(R.layout.record_item, parent ,false);//뷰를 리사클러뷰 크기에 맞추기??
        MyViewHolder holder = new MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("recyclerCheck", "onBindViewHolder====" + position);

        DetailRecord_ItemData item = user.recordItemsList_List.get(setItemPosition).get(position); //record에서 선택한 아이텡의 position값이 필요함
        holder.tv_sectionTime.setText(item.getTv_sectionTime());
        holder.tv_sectionDistance.setText(item.getTv_sectionDistance());
        holder.tv_sectionPace.setText(item.getTv_sectionPace());
        holder.tv_sectionNum.setText(position + 1 + "");

//        holder.itemView.setOnClickListener(new View.OnClickListener() { //아이템 클릭시 호출 콜백
//            @Override
//            public void onClick(View v) { //마커 정보창 열기12
//
//
//            }
//        });

    }


    @Override
    public int getItemCount() { // 어댑터가 관리하는 데이터 세트의 아이템 개수를 반환하는 역할

        //삼항연산자
        return (user.recordItemsList_List != null ? user.recordItemsList_List.get(setItemPosition).size() : 0);

    }



    public class MyViewHolder extends RecyclerView.ViewHolder { //뷰홀더 만들기

        TextView tv_sectionDistance;
        TextView tv_sectionTime;
        TextView tv_sectionPace;
        TextView tv_sectionNum;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_sectionDistance = itemView.findViewById(R.id.tv_sectionDistance);
            this.tv_sectionTime = itemView.findViewById(R.id.tv_sectionTime);
            this.tv_sectionPace = itemView.findViewById(R.id.tv_sectionPace);
            this.tv_sectionNum = itemView.findViewById(R.id.tv_sectionNum);


            Log.d("recyclerCheck", "ViewHolder 기본생성자");
        }
    }
}