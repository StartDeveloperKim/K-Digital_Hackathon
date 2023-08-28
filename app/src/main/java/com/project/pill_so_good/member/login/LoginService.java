package com.project.pill_so_good.member.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.pill_so_good.layout.MainActivity;
import com.project.pill_so_good.member.autoLogin.AutoLoginService;
import com.project.pill_so_good.member.memberInfo.UserInfoService;

public class LoginService {

    private final FirebaseAuth firebaseAuth;
    private final DatabaseReference databaseReference;

    private final UserInfoService userInfoService;

    public LoginService() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Member");
        userInfoService = new UserInfoService();
    }

    public void login(String email, String password, AppCompatActivity activity, Boolean isCheckAutoLogin) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (isCheckAutoLogin)
                        AutoLoginService.setLoginInfo(activity, email, password);

                    setCurrentUserInfoAtLocal(activity);


                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                } else {
                    Toast.makeText(activity, "이메일 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setCurrentUserInfoAtLocal(Context context) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference memberInfo = databaseReference.child(uid);
        memberInfo.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String name = dataSnapshot.child("name").getValue().toString();
                    int age = Integer.parseInt(dataSnapshot.child("age").getValue().toString());
                    String gender = dataSnapshot.child("gender").getValue().toString();
                    String division = dataSnapshot.child("division").getValue().toString();

                    userInfoService.setUserInfo(context, uid, name, age, gender, division);
                }
            }
        });
    }
}


