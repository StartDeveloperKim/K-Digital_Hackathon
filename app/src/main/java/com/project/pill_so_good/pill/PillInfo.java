package com.project.pill_so_good.pill;

import android.graphics.Color;
import android.widget.TextView;

public class PillInfo {

    private final String code;
    private final String company;
    private final String name;
    private String info;
    private final String dangerInfo;

    private String imageUrl;

    private Boolean isTaken;

    public PillInfo(String code, String company, String name, String info, String dangerInfo) {
        this.code = code;
        this.company = company;
        this.name = name;
        this.info = info;
        this.dangerInfo = dangerInfo;
        this.isTaken = Boolean.FALSE;
    }

    public PillInfo(String code, String company, String name, String info, String dangerInfo, int age) {
        this(code, company, name, info, dangerInfo);
        this.info = setAgeInfo(age, info);
    }

    private String setAgeInfo(int age, String info) {
        String[] splitInfo = info.split("세");
        int cutAge = Integer.parseInt(splitInfo[0]);
        isTaken = checkAge(cutAge, age, splitInfo[1]);
        return makeInfo(info);
    }

    private String makeInfo(String info) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("사용자는 ");
        stringBuilder.append(info);

        if (isTaken) {
            stringBuilder.append("이(가) 아니기에 복용이 가능합니다.");
        }else{
            stringBuilder.append("이기에 복용이 불가능합니다.");
        }
        return stringBuilder.toString();
    }

    private Boolean checkAge(int cutAge, int age, String criteria) {
        return ((criteria.equals("이하")) && (cutAge < age)) ||
                ((criteria.equals("미만")) && (cutAge <= age)) ||
                ((criteria.equals("이상")) && (cutAge > age));
    }

    public void setInfoTextColor(TextView textView) {
        if (isTaken) {
            textView.setTextColor(Color.parseColor("#4CAF50"));
        }
    }

    public String getCode() {
        return code;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getDangerInfo() {
        return dangerInfo;
    }

    public String getImageUrl() {
        return imageUrl;}

    public void setImageUrl(String detectImageUrl) {
        this.imageUrl = detectImageUrl;
    }
}
