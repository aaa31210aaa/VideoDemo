package com.example.x5testdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wdcs.callback.GetGdyTokenCallBack;
import com.wdcs.callback.VideoFinderPointCallBack;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.callback.VideoParamCallBack;
import com.wdcs.constants.Constants;
import com.wdcs.model.BuriedPointModel;
import com.wdcs.model.ShareInfo;
import com.wdcs.utils.DebugLogUtils;
import com.wdcs.utils.ToastUtils;

import org.json.JSONObject;

import io.reactivex.functions.Consumer;
import ui.activity.TgtCodeActivity;
import ui.activity.UploadActivity;
import ui.activity.VideoDetailActivity;
import ui.activity.VideoHomeActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private EditText panelCode;
    private EditText contentId;
    private TextView fxsys;
    private TextView setCode;
    private TextView classList;
    private TextView uploadVideoPage;
    private TextView others_home_page;
    private TextView tgt_page;
    private static final String[] permissionsGroup = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        panelCode = findViewById(R.id.panelid);
        panelCode.setText("48662");
        contentId = findViewById(R.id.contentid);
        uploadVideoPage = findViewById(R.id.upload_video_page);
        fxsys = findViewById(R.id.fxsys);
        classList = findViewById(R.id.class_list);
        tgt_page = findViewById(R.id.tgt_page);
        fxsys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoDetailActivity.class);
                intent.putExtra("contentId", contentId.getText().toString());
                startActivity(intent);
            }
        });

        classList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoDetailActivity.class);
                intent.putExtra("panelId", "mycs.video.video");
                intent.putExtra("classId", "10299204");
                intent.putExtra("contentId", contentId.getText().toString());
                intent.putExtra("category_name", "123456");
                startActivity(intent);
            }
        });

        setCode = findViewById(R.id.set_code);

        setCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoInteractiveParam.getInstance().setCallBack(new VideoParamCallBack() {
                    @Override
                    public void shared(ShareInfo shareInfo) {

                    }

                    @Override
                    public void Login() {

                    }

                    //https://testmycs.csbtv.com/accountapi/getUserInfoByTgt
                    @Override //e76ea51c-9dc2-4312-a2b5-9bc85ec79198
                    public String setCode() {
                        return "7c27b1a0-d352-4142-ba03-ba8529f09416";
                    }

                    @Override
                    public void recommedUrl(@NonNull String url, @Nullable ShareInfo shareInfo) {

                    }

                    @Override
                    public void trackingPoint(BuriedPointModel buriedPointModel) {

                    }

                    @Override
                    public String setDeviceId() {
                        return "998877665544332212";
                    }

                });

                VideoInteractiveParam.getInstance().setGdyTokenCallBack(new GetGdyTokenCallBack() {
                    @Override
                    public void checkLoginStatus(String gdyToken) {
                        DebugLogUtils.DebugLog(gdyToken);
                    }
                });

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoHomeActivity.class);
                intent.putExtra("panelId", panelCode.getText().toString());
                intent.putExtra("contentId", contentId.getText().toString());
                intent.putExtra("category_name", "123456");
                startActivity(intent);
            }
        });


        uploadVideoPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                intent.putExtra("draftId", "40054463");
                startActivity(intent);
            }
        });

        tgt_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TgtCodeActivity.class));
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}