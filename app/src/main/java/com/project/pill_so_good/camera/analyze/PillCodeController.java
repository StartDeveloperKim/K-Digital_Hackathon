package com.project.pill_so_good.camera.analyze;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PillCodeController {

    @Multipart
    @POST("detect")
        // TODO :: URL은 서버 환경을 보고 변경이 필요함
    Call<ResponseBody> detectImage(@Part MultipartBody.Part image);
}
