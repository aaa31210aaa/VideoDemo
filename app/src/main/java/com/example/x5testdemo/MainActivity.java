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

import com.tencent.rtmp.TXLiveBase;
import com.wdcs.callback.ApplicationIsAgreeCallBack;
import com.wdcs.callback.GetGdyTokenCallBack;
import com.wdcs.callback.VideoFullAndWindowCallBack;
import com.wdcs.callback.VideoFullAndWindowParam;
import com.wdcs.callback.VideoFinderPointCallBack;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.callback.VideoParamCallBack;
import com.wdcs.model.BuriedPointModel;
import com.wdcs.model.ShareInfo;
import com.wdcs.model.ShowHomeFragmentModel;

import org.json.JSONException;
import org.json.JSONObject;

import ui.activity.TgtCodeActivity;
import ui.activity.UploadActivity;
import ui.activity.VideoDetailActivity;
import ui.activity.VideoHomeActivity;
import ui.activity.VideoHomeTestActivity;

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
        String sdkver = TXLiveBase.getSDKVersionStr();
        Log.d("liteavsdk", "liteav sdk version is : " + sdkver);
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
//                Intent intent = new Intent(MainActivity.this, VideoDetailActivity.class);
//                intent.putExtra("panelId", "mycs.video.video");
//                intent.putExtra("classId", "10299204");
//                intent.putExtra("contentId", contentId.getText().toString());
//                intent.putExtra("category_name", "123456");
//                startActivity(intent);

                Intent intent = new Intent(MainActivity.this, VideoHomeTestActivity.class);
                intent.putExtra("contentId", contentId.getText().toString());
                intent.putExtra("category_name", "123456");
                intent.putExtra("module_source", "测试来源");
                startActivity(intent);
            }
        });

        setCode = findViewById(R.id.set_code);

        setCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoInteractiveParam.getInstance().setVideoFinderPointCallBack(new VideoFinderPointCallBack() {
                    @Override
                    public void getFinderPoint(String eventStr, JSONObject json) {
                        try {
                            Log.e("FinderPoint", json.getString("content_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                VideoInteractiveParam.getInstance().setCallBack(new VideoParamCallBack() {
                    @Override
                    public void shared(ShareInfo shareInfo) {

                    }

                    @Override
                    public void Login() {

                    }

                    //https://testmycs.csbtv.com/accountapi/getUserInfoByTgt
                    @Override //e76ea51c-9dc2-4312-a2b5-9bc85ec79198
                    //b3bfd518-7a64-4ff0-a556-d4fbffb0d71f
                    public String setCode() {
                        return "36211043-5525-41c3-af83-784b538acce6";
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
                        Log.e("广电云token:", gdyToken);
                    }
                });

                VideoInteractiveParam.getInstance().setApplicationIsAgreeCallBack(new ApplicationIsAgreeCallBack() {
                    @Override
                    public String setIsAgreePrivacy() {
                        return "1";
                    }
                });

                VideoFullAndWindowParam.getInstance().setVideoFullAndWindowCallBack(new VideoFullAndWindowCallBack() {
                    @Override
                    public void videoFullAndWindowState(int state) {
                        //state 0 是窗口， 1 是全屏
                        Log.e("videoFullAndWindowState", state + "");
                    }
                });
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoHomeActivity.class);
//                intent.putExtra("contentId", contentId.getText().toString());
//                intent.putExtra("category_name", "123456");
//                intent.putExtra("module_source", "测试来源");
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