package com.example.x5testdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.callback.VideoParamCallBack;
import com.wdcs.model.BuriedPointModel;
import com.wdcs.model.ShareInfo;

import ui.activity.VideoDetailActivity;
import ui.activity.VideoDetailSimpleActivity;
import ui.activity.VideoMainActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private EditText panelId;
    private EditText contentId;
    private TextView fxsys;
    private TextView setCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        panelId = findViewById(R.id.panelid);
        panelId.setText("48662");
        contentId = findViewById(R.id.contentid);
        fxsys = findViewById(R.id.fxsys);

        fxsys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoDetailSimpleActivity.class);
                intent.putExtra("contentId", "93459");
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
                    @Override
                    public String setCode() {
                        return "9bcf9f3c-91ba-4d4d-b233-fb8f98582105";
                    }

                    @Override
                    public void recommedUrl(String url) {

                    }

                    @Override
                    public void buriedPoint(BuriedPointModel buriedPointModel) {

                    }
                });
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoMainActivity.class);
                intent.putExtra("panelId", panelId.getText().toString());
                intent.putExtra("contentId", contentId.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}