package com.project.pill_so_good.layout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.project.pill_so_good.R;
import com.project.pill_so_good.camera.ImageInfo;
import com.project.pill_so_good.camera.Photo;
import com.project.pill_so_good.camera.analyze.PillAnalyzeUtil;
import com.project.pill_so_good.member.memberInfo.UserInfoService;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private UserInfoService userInfoService;

    private ImageButton cameraBtn, galleryBtn, analyzeBtn, profileBtn, map;

    private Photo photo;
    private ImageInfo imageInfo;
    private ActivityResultLauncher<Intent> cameraResultLauncher;
    private ActivityResultLauncher<Intent> galleryResultLauncher;
    private PillAnalyzeUtil pillAnalyzeUtil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInfoService = new UserInfoService();
        photo = new Photo();
        imageInfo = ImageInfo.getInstance();
        pillAnalyzeUtil = new PillAnalyzeUtil();

        setCameraBtn();
        setGalleryBtn();
        setAnalyzeBtn();
        setProfileBtn();
        setMapBtn();
        setCameraResultLauncher();
        setGalleryResultLauncher();
    }
    private void setMapBtn()
    {
        map = findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent를 사용하여 MapActivity를 시작
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setGalleryResultLauncher() {
        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            Uri uri = intent.getData();

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                imageInfo.setBitmap(bitmap);
                                setImageView(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    private void setCameraResultLauncher() {
        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bitmap bitmap = photo.afterTakePicture();
                            setImageView(bitmap);
                            imageInfo.setBitmap(bitmap);
                        }
                    }
                }
        );
    }

    private void setAnalyzeBtn() {
        analyzeBtn = findViewById(R.id.analyze_btn);
        analyzeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageInfo.getBitmap().isPresent()) {
                    pillAnalyzeUtil.pillImageAnalyze(MainActivity.this, imageInfo.convertBitmapToFile(getApplicationContext()));
                } else {
                    showToastMessage("사진을 찍어야 분석이 가능합니다.");
                }
            }
        });
    }

    private void setGalleryBtn() {
        galleryBtn = findViewById(R.id.gallery_btn);
        galleryBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 갤러리 이동 코드
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);
                galleryResultLauncher.launch(intent);
            }
        });
    }

    private void setCameraBtn() {
        cameraBtn = findViewById(R.id.camera_btn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                }else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = photo.createImageFile(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                        } catch (IOException e) {

                        }

                        if (photoFile != null) {
                            Uri photoUri = FileProvider.getUriForFile(MainActivity.this, getPackageName(), photoFile);
                            photo.setPhotoUri(photoUri);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            cameraResultLauncher.launch(intent);
                        }
                    }
                }
            }
        });
    }

    private void setProfileBtn() {
        profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setImageView(Bitmap bitmap) {
        ImageView imageView = findViewById(R.id.camera_btn);
        imageView.setImageBitmap(bitmap);
    }


    private long backPressedTime;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long TIME_INTERVAL = 2000;
        if (backPressedTime + TIME_INTERVAL > currentTime) {
            super.onBackPressed();
            finish();
        }else {
            showToastMessage("뒤로 버튼을 두 번 눌러주세요");
        }

        backPressedTime = currentTime;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                showToastMessage("카메라 권한이 승인됨");
            } else {
                showToastMessage("카메라 권한이 거절 되었습니다.");
            }
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
