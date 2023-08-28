package com.project.pill_so_good.member.memberInfo;

import android.content.Context;
import android.content.SharedPreferences;

import com.project.pill_so_good.member.dto.UserInfo;


public class UserInfoService {

    private static final String USER_ID = "user_id";

    private static final String USER_INFO = "user_info";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String GENDER = "gender";
    private static final String DIVISION = "division";


    public void setUserInfo(Context context, String userId, String name, int age, String gender, String division) {
        SharedPreferences preferences = getPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(USER_ID, userId);
        editor.putString(NAME, name);
        editor.putInt(AGE, age);
        editor.putString(GENDER, gender);
        editor.putString(DIVISION, division);

        editor.apply();
    }

    public UserInfo getUserInfo(Context context) {
        SharedPreferences preferences = getPreferences(context);
        return new UserInfo(preferences.getString(USER_ID, ""), preferences.getString(NAME, ""), preferences.getInt(AGE, 0),
                preferences.getString(GENDER, ""), preferences.getString(DIVISION, ""));
    }


    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    }
}
