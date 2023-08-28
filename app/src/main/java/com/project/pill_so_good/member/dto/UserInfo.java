package com.project.pill_so_good.member.dto;

import com.project.pill_so_good.member.domain.Division;
import com.project.pill_so_good.member.domain.Gender;

public class UserInfo {
    private final String userId;

    private final String name;
    private final int age;
    private final String gender;
    private final String division;

    public UserInfo(String userId, String name, int age, String gender, String division) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.division = division;
    }

    public String getUserId() {
        return userId;}

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getDivision() {
        return division;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", division='" + division + '\'' +
                '}';
    }
}
