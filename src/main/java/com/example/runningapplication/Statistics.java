package com.example.runningapplication;

import static com.example.runningapplication.Login.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;

import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import java.text.DecimalFormat;

public class Statistics extends AppCompatActivity  {
    TextView tv_total;
    float total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        tv_total = findViewById(R.id.tv_total);

        for (int i = 0; i < user.totalRunningDistance.size(); i++) {
            total += user.totalRunningDistance.get(i);
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String num = decimalFormat.format(total);

        tv_total.setText(num);







    }


}