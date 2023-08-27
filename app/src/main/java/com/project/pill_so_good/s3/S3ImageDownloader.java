package com.project.pill_so_good.s3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class S3ImageDownloader {

    private static final String BASE_URL = "https://toyprojectworld.s3.ap-northeast-2.amazonaws.com";

    public static S3ImageController getApiService() {
        return getInstance().create(S3ImageController.class);
    }

    private static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
