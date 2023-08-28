package com.project.pill_so_good.camera.analyze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.project.pill_so_good.dialog.LoadingDialog;
import com.project.pill_so_good.layout.ResultActivity;
import com.project.pill_so_good.member.domain.Division;
import com.project.pill_so_good.member.dto.UserInfo;
import com.project.pill_so_good.member.memberInfo.UserInfoService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PillAnalyzeUtil {

    private final PillCodeController pillCodeController;
    private final UserInfoService userInfoService;


    public PillAnalyzeUtil() {
        this.pillCodeController = PillCodeRequester.getApiService();
        this.userInfoService = new UserInfoService();
    }

    public void pillImageAnalyze(Activity activity, File imageFile) {
        RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("image/*"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
        Call<ResponseBody> call = pillCodeController.detectImage(imagePart);

        LoadingDialog loadingDialog = new LoadingDialog(activity);
        loadingDialog.showLoadingDialog();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            Log.i("MainActivity", "body");
                            loadingDialog.dismissLoadingDialog();

                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            String medicineCode = jsonObject.getString("result");
                            String detectImageURL = jsonObject.getString("url");

                            UserInfo userInfo = userInfoService.getUserInfo(activity);

                            Log.i("MainActivity", "Name : " + userInfo.getName() + "// UserId : " + userInfo.getUserId());

                            Intent intent = new Intent(activity, ResultActivity.class);
                            intent.putExtra("medicineCode", medicineCode);
                            intent.putExtra("age", userInfo.getAge());
                            intent.putExtra("dbKey", userInfo.getDivision());
                            intent.putExtra("detect_image_url", detectImageURL);

                            activity.startActivity(intent);
                        } catch (IOException | JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loadingDialog.dismissLoadingDialog();
                Toast.makeText(activity, "서버와의 통신이 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
