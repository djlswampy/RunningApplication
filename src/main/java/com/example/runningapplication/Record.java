package com.example.runningapplication;

import static com.example.runningapplication.Login.ID;
import static com.example.runningapplication.Login.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.runningapplication.ClassPakage.PreferenceManager;
import com.example.runningapplication.RecyclerView.RecyclerAdapter;

public class Record extends AppCompatActivity {

    RecyclerView recyclerView;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recyclerView = findViewById(R.id.recycler);
        user = PreferenceManager.getObject(Record.this, ID); //오류해결

//        Intent intent = getIntent();

//        String 이동거리 = intent.getStringExtra("이동거리");
//        String 걸린시간 = intent.getStringExtra("걸린시간");
//        String 날짜 = intent.getStringExtra("날짜");
//
//        Item_data dataModel = new Item_data(이동거리, 걸린시간, 날짜));
//        items.add(dataModel);

        RecyclerAdapter adapter = new RecyclerAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); //방향설정

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);





//        TextView tv_distance = findViewById(R.id.tv_distance);
//        TextView tv_time = findViewById(R.id.tv_time);
//        TextView tv_date = findViewById(R.id.tv_date);
//
//        tv_distance.setText(이동거리);
//        tv_time.setText(걸린시간);
//        tv_date.setText(날짜);

        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent1 = new Intent(record.this, runningStart.class);
//                startActivity(intent1);
                finish();
            }
        });
    }


}