package com.example.runningapplication;

import static com.example.runningapplication.Login.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class CalendarMemo extends AppCompatActivity {

    private Button btn_write, btn_edit, btn_delete;
    private ImageView iv_photo;
    private TextView tv_content;
    private CalendarView calendar;
    private ScrollView scrollView;
    public String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = findViewById(R.id.calendar);
        scrollView = findViewById(R.id.scrollView);
        btn_write = findViewById(R.id.btn_write);
        tv_content = findViewById(R.id.tv_content);
        iv_photo = findViewById(R.id.iv_photo);
        btn_delete = findViewById(R.id.btn_delete);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
//        Date today = new Date();
//        date = dateFormat.format(today);

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        date = new SimpleDateFormat("yyyy-MM-dd").format(today);

        if(user.dailyLogList.get(date) != null){ //캘린더 액티비티 시작할 때 오늘 날짜 메모 텍스트 입력
            tv_content.setText(user.dailyLogList.get(date));
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month); //달 0부터 시작
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date day = cal.getTime();
                date = new SimpleDateFormat("yyyy-MM-dd").format(day);

                if(user.dailyLogList.get(date) != null){
                    tv_content.setText(user.dailyLogList.get(date)) ;
                } else {
                    tv_content.setText("");
                }
                BtnChange();
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //작성하기

                Intent intent = new Intent(CalendarMemo.this, DailyLog.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() { //삭제버튼
            @Override
            public void onClick(View v) {
                tv_content.setText("");
                btn_delete.setVisibility(View.INVISIBLE);
                btn_write.setVisibility(View.VISIBLE);
            }
        });

//        Intent intent = getIntent();
////        String str = intent.getStringExtra("imageUri");
////        Toast.makeText(calendar.this, str, Toast.LENGTH_SHORT).show();
////        Uri uri = Uri.parse(str);
////        iv_photo.setImageURI(uri);
//
//
//        String str = intent.getStringExtra("content");
//        tv_content.setText(str);
//        user.dailyLogList.put(date, str);


        btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //수정버튼

                if(tv_content.getText().toString().isEmpty() == false){ //textView에 내용 없으면 null이 아니라 ""
                    Intent intent1 = new Intent(CalendarMemo.this, DailyLog.class);
                    String str = user.dailyLogList.get(date);
                    intent1.putExtra("date", date);
                    intent1.putExtra("content", str);
                    startActivity(intent1);

                } else {
                    Toast.makeText(CalendarMemo.this, "수정할 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_content.setText(user.dailyLogList.get(date));
        BtnChange();
    }


    public void BtnChange() {
        if(!tv_content.getText().equals("")){
            btn_delete.setVisibility(View.VISIBLE);
            btn_write.setVisibility(View.INVISIBLE);
        } else {
            btn_delete.setVisibility(View.INVISIBLE);
            btn_write.setVisibility(View.VISIBLE);
        }
    }
}