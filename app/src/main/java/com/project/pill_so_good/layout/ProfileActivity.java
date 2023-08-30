package com.project.pill_so_good.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.pill_so_good.R;
import com.project.pill_so_good.member.autoLogin.AutoLoginService;
import com.project.pill_so_good.member.logout.LogoutService;
import com.project.pill_so_good.mydata.MyDataService;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutBtn;
    private LogoutService logoutService;
    private MyDataService myDataService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logoutService = new LogoutService();
        myDataService = new MyDataService();

        setLogoutButton();
        myDataService.findPillInfo(getApplicationContext());
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
