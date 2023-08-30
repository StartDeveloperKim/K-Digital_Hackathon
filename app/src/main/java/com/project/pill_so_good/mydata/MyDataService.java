package com.project.pill_so_good.mydata;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.pill_so_good.member.memberInfo.UserInfoService;
import com.project.pill_so_good.mydata.dto.PillMyDataDto;
import com.project.pill_so_good.pill.PillInfo;

import java.util.ArrayList;
import java.util.List;

public class MyDataService {

    private final DatabaseReference databaseReference;

    private final UserInfoService userInfoService;

    public MyDataService() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("MyData");
        this.userInfoService = new UserInfoService();
    }

    public void save(PillInfo pillInfo, Context context) {
        databaseReference.child(userInfoService.getUserInfo(context).getUserId()).child(pillInfo.getCode()).setValue(pillInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showToastMessage(context, "데이터가 추가되었습니다.");
                    }
                }).addOnFailureListener(runnable -> {
                    showToastMessage(context, "데이터 추가에 실패했습니다.");
                });
    }
    
    public List<PillMyDataDto> findPillInfo(Context context) {
        List<PillMyDataDto> result = new ArrayList<>();
//        boolean complete = databaseReference.child(userInfoService.getUserInfo(context).getUserId()).get()
//                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                    @Override
//                    public void onSuccess(DataSnapshot dataSnapshot) {
//                        dataSnapshot.getChildren()
//                                .forEach(pillInfos -> {
//                                    PillMyDataDto pillMyDataDto = pillInfos.getValue(PillMyDataDto.class);
//                                    result.add(pillMyDataDto);
//                                });
//                    }
//                }).isComplete();


        return result;
    }

    private static void showToastMessage(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
