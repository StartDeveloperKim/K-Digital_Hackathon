package com.project.pill_so_good.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.pill_so_good.R;
import com.project.pill_so_good.member.autoLogin.AutoLoginService;
import com.project.pill_so_good.member.dto.UserInfo;
import com.project.pill_so_good.member.logout.LogoutService;
import com.project.pill_so_good.member.memberInfo.UserInfoService;
import com.project.pill_so_good.mydata.MyDataService;
import com.project.pill_so_good.mydata.dto.PillMyDataDto;
import com.project.pill_so_good.pill.PillInfo;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutBtn;
    private TextView txName, txAge, txGender, txDivision;
    private LogoutService logoutService;
    private MyDataService myDataService;
    private UserInfoService userInfoService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logoutService = new LogoutService();
        myDataService = new MyDataService();
        userInfoService = new UserInfoService();

        setLogoutButton();
//        setUserInfo();

        List<PillMyDataDto> pillInfo = myDataService.findPillInfo(getApplicationContext());
    }

    private void setUserInfo() {
        txName = findViewById(R.id.profile_name);
        txAge = findViewById(R.id.profile_age);
        txGender = findViewById(R.id.profile_gender);
        txDivision = findViewById(R.id.profile_division);

        UserInfo userInfo = userInfoService.getUserInfo(getApplicationContext());
        txName.setText(userInfo.getName());
        txAge.setText(userInfo.getAge());
        txGender.setText(userInfo.getGender());
        txDivision.setText(userInfo.getDivision());

    }

    private void setLogoutButton() {
        logoutBtn = findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutService.logout();
                AutoLoginService.removeLoginInfo(ProfileActivity.this);
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
