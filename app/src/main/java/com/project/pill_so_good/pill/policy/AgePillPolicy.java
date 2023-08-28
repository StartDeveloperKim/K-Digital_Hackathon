package com.project.pill_so_good.pill.policy;

import com.google.firebase.firestore.DocumentSnapshot;
import com.project.pill_so_good.pill.PillInfo;

public class AgePillPolicy implements PillPolicy{
    @Override
    public PillInfo getPillInfo(DocumentSnapshot result, int age, String pillCode) {
        String name = result.getString("name");
        String company = result.getString("company");
        String info = result.getString("age_limit");
        String dangerInfo = result.getString("info");
        return new PillInfo(pillCode, company, name, info, dangerInfo, age);
    }
}
