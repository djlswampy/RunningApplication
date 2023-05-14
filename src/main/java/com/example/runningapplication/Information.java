package com.example.runningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


        Button btn_eventSchedule = findViewById(R.id.btn_eventSchedule);  //대회정보 사이트
        btn_eventSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.marathon.pe.kr/races/races00.html")); //Uri.parse() : 문자열을 uri로
                startActivity(intent);

            }
        });


    }
}