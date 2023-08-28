package com.project.pill_so_good.pill.dto;

public class AnalyzeResultDto {

    private final String pillCode;
    private final int age;
    private final String dbKey;
    private final String detectImageUrl;

    public AnalyzeResultDto(String pillCode, int age, String dbKey, String detectImageUrl) {
        this.pillCode = pillCode;
        this.age = age;
        this.dbKey = dbKey;
        this.detectImageUrl = detectImageUrl;
    }

    public boolean isCorrectPillCode() {
        return pillCode.matches("[0-9]+");
    }

    public String getPillCode() {
        return pillCode;
    }

    public int getAge() {
        return age;
    }

    public String getDbKey() {
        return dbKey;
    }

    public String getDetectImageUrl() {
        return detectImageUrl;
    }
}
