package com.example.runningapplication.RecyclerView;

import static com.example.runningapplication.Login.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.DetailRecord;
import com.example.runningapplication.Login;
import com.example.runningapplication.ClassPakage.PreferenceManager;
import com.example.runningapplication.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    Context context;
//    ArrayList<Item_data> items;
    public static int getItemPosition;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

//    public RecyclerAdapter(Context context, ArrayList<Item_data> items) {
//        this.context = context;
//        this.items = items;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("recyclerCheck", "onCreateViewHolder");

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent ,false);//뷰를 리사클러뷰 크기에 맞추기??

//        View view = LayoutInflater.from(context).inflate(R.layout.item,null);   이거랑 무슨차이
//        View view = View.inflate(context, R.layout.item,null);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { //뷰와 데이터 연결
        Log.d("recyclerCheck", "onBindViewHolder====" + position);

        //데이터 담긴 어레이리스트. 생성자를 통해서 받아옴
        //user.items 갹채룰 item이라는 변수에 할당
        ItemData item = user.items.get(position);

        holder.tv_date.setText(item.getTv_date());
        holder.tv_distance.setText(item.getTv_distance());
        holder.tv_time.setText(item.getTv_time());
        holder.tv_place.setText(item.getDialogText());
        holder.editText.setText((item.getDialogText()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //아이템 클릭했을 때.
                Intent intent = new Intent(context, DetailRecord.class);
                intent.putExtra("position", holder.getAdapterPosition());
                getItemPosition = holder.getAdapterPosition();

//                itemNum = position;

                context.startActivity(intent);

            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { //길게눌렀을때 항목 삭제
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                deleteDialog.setTitle("기록을 삭제하시겠습니까?");

                deleteDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //user 객체의 items리스트의 position과 동일한 인덱스값을 갖는 객체 제거
                        user.items.remove(holder.getAdapterPosition());
                        user.totalRunningDistance.remove(holder.getAdapterPosition());
//                        remove(holder.getAdapterPosition());

                        PreferenceManager.setObject(context, Login.ID, user);

                        dialog.dismiss();
                        notifyDataSetChanged(); //새로고침. 변경사항 화면 반영

                    }
                });


                deleteDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        notifyDataSetChanged(); //새로고침. 변경사항 화면 반영

                    }
                });
                deleteDialog.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return (user.items != null ? user.items.size() : 0);
    }


    public void remove(int position) { //버튼 길게 눌렀을 때 삭제
        try {
            user.items.remove(position);

            notifyItemRemoved(position); //새로고침 리스트뷰를 지우고 새로고침을 해줘야 추가 삭제 등이 이루어질 수 있음
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder { //뷰홀더 만들기

        TextView tv_date;
        TextView tv_time;
        TextView tv_distance;
        TextView tv_place;

        EditText editText;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_date = itemView.findViewById(R.id.tv_date);
            this.tv_time = itemView.findViewById(R.id.tv_time);
            this.tv_distance = itemView.findViewById(R.id.tv_distance);
            this.tv_place = itemView.findViewById(R.id.tv_place);

            this.editText = new EditText(context);

            Log.d("recyclerCheck", "ViewHolder 기본생성자");
        }
    }
}
