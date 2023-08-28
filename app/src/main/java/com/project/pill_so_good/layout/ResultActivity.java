package com.project.pill_so_good.layout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.pill_so_good.R;
import com.project.pill_so_good.pill.GetPillInfoSuccessListener;
import com.project.pill_so_good.pill.PillInfo;
import com.project.pill_so_good.pill.PillInformationService;
import com.project.pill_so_good.pill.dto.AnalyzeResultDto;
import com.project.pill_so_good.pill.policy.PillPolicyFactory;
import com.project.pill_so_good.s3.S3ImageController;
import com.project.pill_so_good.s3.S3ImageDownloader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity implements GetPillInfoSuccessListener {

    private ImageView resultImageView;
    private TextView nameTv, companyTv, codeTv, infoTv, dangerInfoTv;
    private Button addDataButton;
    private S3ImageController s3ImageController;
    private PillInformationService pillInformationService;

    private static final String MEDICINE_CODE = "medicineCode";
    private static final String AGE = "age";
    private static final String DB_KEY = "dbKey";
    private static final String DETECT_IMAGE_URL = "detect_image_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        s3ImageController = S3ImageDownloader.getApiService();
        pillInformationService = new PillInformationService(this);

        setTextView();
        setAddButton();

        AnalyzeResultDto analyzeResultDto = getAnalyzeResultDto(getIntent().getExtras());
        if (analyzeResultDto.isCorrectPillCode()) {
            pillInformationService.getPillInfo(analyzeResultDto, PillPolicyFactory.getPillPolicyInstance(analyzeResultDto.getDbKey()));
            setDetectImageView(analyzeResultDto.getDetectImageUrl());
        }else{
            addDataButton.setEnabled(false);
        }
    }

    private void setAddButton() {
        addDataButton = findViewById(R.id.addData_btn);
        addDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 파이어베이스 RealTime 데이터베이스에 사용자 UID를 기준으로 값을 CRUD 할 수 있는 코드를 작성하자.
            }
        });
    }

    private void setTextView() {
        nameTv = findViewById(R.id.pill_name);
        companyTv = findViewById(R.id.company);
        codeTv = findViewById(R.id.code);
        infoTv = findViewById(R.id.pill_info);
        dangerInfoTv = findViewById(R.id.danger_info);

        addDataButton = findViewById(R.id.addData_btn);
    }

    private AnalyzeResultDto getAnalyzeResultDto(Bundle bundle) {
        return new AnalyzeResultDto(bundle.getString(MEDICINE_CODE), bundle.getInt(AGE), bundle.getString(DB_KEY), bundle.getString(DETECT_IMAGE_URL));
    }

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

    @Override
    public void onFirebaseDataParsed(PillInfo pillinfo) {
        setTextView(nameTv, pillinfo.getName());
        setTextView(companyTv, pillinfo.getCompany());
        setTextView(companyTv, pillinfo.getCode());
        setTextView(infoTv, pillinfo.getInfo());
        setTextView(dangerInfoTv, pillinfo.getDangerInfo());
    }

    private void setTextView(TextView view, String message) {
        view.setText(message);
    }
}
