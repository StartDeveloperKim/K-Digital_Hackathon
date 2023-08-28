package com.project.pill_so_good.pill;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.pill_so_good.pill.policy.PillPolicy;

public class PillInformationService {

    private final FirebaseFirestore db;

    private final GetPillInfoSuccessListener listener;

    public PillInformationService(GetPillInfoSuccessListener getPillInfoSuccessListener) {
        this.db = FirebaseFirestore.getInstance();
        this.listener = getPillInfoSuccessListener;
    }

    public void getPillInfo(String code, String collectionName, int age, PillPolicy pillPolicy) {
        db.collection(collectionName)
                .document(code).get()
                .addOnSuccessListener(result -> {
                    PillInfo pillInfo = pillPolicy.getPillInfo(result, age, code);
                    listener.onFirebaseDataParsed(pillInfo);
                });
    }
}
