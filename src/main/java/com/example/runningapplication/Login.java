package com.example.runningapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.runningapplication.ClassPakage.PreferenceManager;
import com.example.runningapplication.ClassPakage.User;

public class Login extends AppCompatActivity {

    private Button btn_login;
    private Button btn_signUp;
    private Button btn_reset;
    private EditText et_ID;
    private EditText et_PW;

    public static User user;

    public static String ID;
    String PW;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_ID = findViewById(R.id.et_ID);
        et_PW = findViewById(R.id.et_PW);


        btn_login = findViewById(R.id.btn_login);  //로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ID = et_ID.getText().toString();
                PW = et_PW.getText().toString();

//                SharedPreferences sharedPreferences = getSharedPreferences("meberInfo", MODE_PRIVATE);
//                String value = sharedPreferences.getString(ID, "");
                user = PreferenceManager.getObject(Login.this, ID);

                if(user != null && user.password.equals(PW)) {

                    Toast.makeText(Login.this, "로그인 완료", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Login.this, RunningStart.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(Login.this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();

                }

//                Intent intent = new Intent(Login.this, RunningStart.class);
//                startActivity(intent);


            }
        });


        btn_signUp = findViewById(R.id.btn_signUp); //회원가입 버튼
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });


        btn_reset = findViewById(R.id.btn_reset); //SharedPreferences 리셋버튼
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences sharedPreferences = getSharedPreferences("meberInfo", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.apply();
                  PreferenceManager.clear(Login.this);

            }
        });

    }
}