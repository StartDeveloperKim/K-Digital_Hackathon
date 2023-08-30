package com.project.pill_so_good.mydata.dto;

public class PillMyDataDto {

    private String code;
    private String company;
    private String dangerInfo;
    private String imageUrl;
    private String info;
    private String name;

    public PillMyDataDto() {

    }

    public PillMyDataDto(String code, String company, String dangerInfo, String imageUrl, String info, String name) {
        this.code = code;
        this.company = company;
        this.dangerInfo = dangerInfo;
        this.imageUrl = imageUrl;
        this.info = info;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getCompany() {
        return company;
    }

    public String getDangerInfo() {
        return dangerInfo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }
}
