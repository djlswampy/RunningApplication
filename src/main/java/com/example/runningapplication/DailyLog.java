package com.example.runningapplication;

import static com.example.runningapplication.Login.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.runningapplication.ClassPakage.PreferenceManager;

public class DailyLog extends AppCompatActivity {

    private Button btn_addImage;
    private Button btn_complete;
    private TextView et_content;
    private TextView tv_imageUri;
    private ImageButton ibtn_delete;


    //콜백함수 : 이벤트가 발생했을 때 호출되는 함수?
    //호출한 액티비티에서 돌아올 때 그 액티비티에서 데이터 받아 올 수 있는 통로

//    private final ActivityResultLauncher<Intent> getImageLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//
//                    if(result.getResultCode() == RESULT_OK) {
//                        Intent intent = result.getData();
//                        String str = intent.getData().toString();
//
////                        Toast.makeText(dailyLog.this, str, Toast.LENGTH_SHORT).show();
//
//                    tv_imageUri.setText(str);
//
//
//                    }
//                }
//            }
//    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailylog);

        et_content = findViewById(R.id.et_content);

//        Intent intent_edit = getIntent();
//        String str = intent_edit.getStringExtra("contentForEdit");
//        et_content.setText(str);

        Intent intent = getIntent();
        String str = intent.getStringExtra("date");
        String str1 = intent.getStringExtra("content");

        if(str1 != null){ //전달된 내용이 없다면 실행x(수정 버튼 눌렀을때만 실행)
            et_content.setText(str1);
        }

        tv_imageUri = findViewById(R.id.tv_imageUri);
        Button btn_addImage = findViewById(R.id.btn_addImage);
        btn_addImage.setOnClickListener(new View.OnClickListener() {  //이미지 첨부 버튼
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                getImageLauncher.launch(intent);

            }
        });

        ibtn_delete = findViewById(R.id.ibtn_delete);
        ibtn_delete.setOnClickListener(new View.OnClickListener() { // x버튼
            @Override
            public void onClick(View view) {
                if(tv_imageUri != null){
                    tv_imageUri.setText(null);
                }
            }
        });


        btn_complete = findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //작성 완료 버튼

//                Intent intent = new Intent(DailyLog.this, Calendar.class);

                if (tv_imageUri != null){
                    intent.putExtra("imageUri", tv_imageUri.toString());

                }

                if (et_content != null) {
//                    String str1 = et_content.getText().toString();
//                    intent.putExtra("content", et_content.getText().toString(););
                    user.dailyLogList.put(str, et_content.getText().toString());
                    PreferenceManager.setObject(DailyLog.this, Login.ID, user);

                }

//                startActivity(intent);
                finish();
            }
        });
    }






////콜백함수 : 이벤트가 발생했을 때 호출되는 함수?
////호출한 액티비티에서 돌아올 때 그 액티비티에서 데이터 받아 올 수 있는 통로
//    private final ActivityResultLauncher<Intent> getImageLauncher = registerForActivityResult(
//        new ActivityResultContracts.StartActivityForResult(),
//        new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//
//                if(result.getResultCode() == RESULT_OK) {
//                    Intent intent = result.getData();
//                    Uri uri = intent.getData();
//                    String str = intent.getData().toString();
//
//                    Toast.makeText(dailyLog.this, str, Toast.LENGTH_SHORT).show();
//
//
//
////                    tv_imageUri.setText(str);
//
//
//                }
//            }
//        }
//    );

}