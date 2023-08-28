package com.project.pill_so_good.pill;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.pill_so_good.pill.dto.AnalyzeResultDto;
import com.project.pill_so_good.pill.policy.PillPolicy;

public class PillInformationService {

    private final FirebaseFirestore db;

    private final GetPillInfoSuccessListener listener;

    public PillInformationService(GetPillInfoSuccessListener getPillInfoSuccessListener) {
        this.db = FirebaseFirestore.getInstance();
        this.listener = getPillInfoSuccessListener;
    }

    public void getPillInfo(AnalyzeResultDto analyzeResultDto, PillPolicy pillPolicy) {
        db.collection(analyzeResultDto.getDbKey())
                .document(analyzeResultDto.getPillCode()).get()
                .addOnSuccessListener(result -> {
                    PillInfo pillInfo = pillPolicy.getPillInfo(result, analyzeResultDto.getAge(), analyzeResultDto.getPillCode());
                    listener.onFirebaseDataParsed(pillInfo);
                })
                .addOnFailureListener(runnable -> {
                    System.out.println(runnable.getMessage());
                });
    }
}
