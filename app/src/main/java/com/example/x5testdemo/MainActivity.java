package com.example.x5testdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import callback.VideoInteractiveParam;
import callback.VideoParamCallBack;
import model.ShareInfo;
import ui.VideoDetailActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private EditText panelId;
    private EditText contentId;
    private EditText token;
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
                        return "a79c1166-846b-4ab2-9845-4cd745824f3a";
                    }
                });
            }
        });

        token = findViewById(R.id.token);
        token.setText("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJ3ZWIiLCJpc3MiOiJmdXNlIiwiZXhwIjoxNjI0MzI2ODY4LCJpYXQiOjE2MjM3MjIwNjgsImp0aSI6ImM0NGNjMmEwNThlMDQ5YThiMTRmZTNjY2I3YWRmYTI5IiwidXNlcm5hbWUiOiJ5YW5xaWh1YW5nIn0.H3qZVUYqE85Yh1-hF_F8QFG2jOiVdaZ5leK8RRq_Dng");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoDetailActivity.class);
                intent.putExtra("panelId", panelId.getText().toString());
                intent.putExtra("contentId", contentId.getText().toString());
                intent.putExtra("token", token.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}