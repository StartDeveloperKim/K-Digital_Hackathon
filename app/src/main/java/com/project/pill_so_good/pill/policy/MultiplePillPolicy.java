package com.project.pill_so_good.pill.policy;

import com.google.firebase.firestore.DocumentSnapshot;
import com.project.pill_so_good.pill.PillInfo;

public class MultiplePillPolicy implements PillPolicy{
    @Override
    public PillInfo getPillInfo(DocumentSnapshot result, int age, String pillCode) {
        return null;
    }
}
