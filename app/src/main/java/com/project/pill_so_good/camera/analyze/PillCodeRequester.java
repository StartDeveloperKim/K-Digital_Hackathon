package com.project.pill_so_good.camera.analyze;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PillCodeRequester {

    private static final String BASE_URL = "http://3.37.228.188:5000/"; // 배포하면 URL 변경필요

    public static PillCodeController getApiService() {
        return getInstance().create(PillCodeController.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
