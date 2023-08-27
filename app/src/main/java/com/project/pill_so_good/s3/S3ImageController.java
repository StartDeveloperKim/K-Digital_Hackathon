package com.project.pill_so_good.s3;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface S3ImageController {
    @GET("/{image}")
    Call<ResponseBody> getDetectImage(@Path("image") String imageUrl);
}
