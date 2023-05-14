package com.example.runningapplication;

import static com.example.runningapplication.ClassPakage.MailCheck.emailChek;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.runningapplication.ClassPakage.PreferenceManager;
import com.example.runningapplication.ClassPakage.User;

public class SignUp extends AppCompatActivity {

    private EditText et_id;
    private EditText et_pw;
    private EditText et_pwCheck;
    private Button btn_join;
    private Button btn_cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_id =  findViewById(R.id.et_id);
        et_pw =  findViewById(R.id.et_pw);
        et_pwCheck =  findViewById(R.id.et_pwCheck);
        btn_join =  findViewById(R.id.btn_join);
        btn_cancel =  findViewById(R.id.btn_cancel);

//        SharedPreferences sharedPreferences = getSharedPreferences("meberInfo", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();



        et_id.addTextChangedListener(new TextWatcher() { //텍스트 입력 이벤트를 받는 콜백. 이메일 형식 일치 여부에 따라 텍스트 색 변경
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { //텍스트 변경 전 호출
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { //텍스트 변경 중 호출
                if(emailChek(et_id.getText().toString()) == false) {
                    et_id.setTextColor(Color.RED);

                } else {
                    et_id.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { //텍스트 변경 후 호출되는 메서드
            }
        });


            btn_join.setOnClickListener(new View.OnClickListener() { //회원가입
            @Override
            public void onClick(View v) { //가입버튼

                String ID = et_id.getText().toString();
                String PW = et_pw.getText().toString();
                String PWCHECK = et_pwCheck.getText().toString();

                if(!ID.isEmpty() && emailChek(ID) == true){
                    if(PreferenceManager.getObject(SignUp.this, ID) == null){
                        Log.d("signUpCheck", "아이디 중복 확인");

                        if(PW.equals(PWCHECK) && !PW.isEmpty()){

                            User user = new User();
                            user.password = PW;


                            PreferenceManager.setObject(SignUp.this, ID, user);

                            Toast.makeText(SignUp.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(SignUp.this, "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUp.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUp.this, "아이디를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}