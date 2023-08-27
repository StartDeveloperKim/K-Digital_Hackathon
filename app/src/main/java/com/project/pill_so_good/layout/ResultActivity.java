package com.project.pill_so_good.layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.pill_so_good.R;
import com.project.pill_so_good.s3.S3ImageController;
import com.project.pill_so_good.s3.S3ImageDownloader;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    private ImageView resultImageView;
    private S3ImageController s3ImageController;

    private static final String MEDICINE_CODE = "medicineCode";
    private static final String AGE = "age";
    private static final String DB_KEY = "dbKey";
    private static final String DETECT_IMAGE_URL = "detect_image_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        s3ImageController = S3ImageDownloader.getApiService();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        setDetectImageView(extras.getString(DETECT_IMAGE_URL));

    }

    // TODO :: 파이어베이스 DB 구축을 통해 금기정보를 받아와야함
    // TODO :: 알약 API를 통해 알약 정보를 얻어올 수 있어야함.

    private void setDetectImageView(String detectImageURL) {
        resultImageView = findViewById(R.id.result_image);

        Call<ResponseBody> detectImage = s3ImageController.getDetectImage(detectImageURL);
        detectImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseBody body = response.body();
                    Bitmap bitmap = BitmapFactory.decodeStream(body.byteStream());
                    resultImageView.setImageBitmap(bitmap);
                }else{
                    Toast.makeText(getApplicationContext(), "이미지 다운로드 응답이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "이미지 다운로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
