package com.project.pill_so_good.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.pill_so_good.R;
import com.project.pill_so_good.member.autoLogin.AutoLoginService;
import com.project.pill_so_good.member.login.LoginService;

public class LoginActivity extends AppCompatActivity {


    private LoginService loginService;
    private EditText etEmail, etPassword;
    private Button loginBtn, signUpBtn;

    private CheckBox autoLoginCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginService = new LoginService();

        etEmail = findViewById(R.id.login_email);
        etPassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.signup_btn);
        autoLoginCheck = findViewById(R.id.auto_login_checkbox);

        setLoginBtn();
        setSignUpBtn();
    }


    private void setSignUpBtn() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setLoginBtn() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    loginService.login(email, password, LoginActivity.this, autoLoginCheck.isChecked());
                }
            }
        });
    }
}
