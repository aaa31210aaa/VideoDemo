package ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.videodetail.demo.R;

import com.wdcs.widget.YALikeAnimationView;

public class TgtCodeActivity extends AppCompatActivity {
    private TextView wdTgt;
    private TextView localTgt;
    private TextView gdyTgt;
    private TextView getWdtgt;
    private TextView localToken;
    private YALikeAnimationView likeLayout2;
    private RelativeLayout dianzan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tgt_code);
        wdTgt = findViewById(R.id.wdTgt);
        localTgt = findViewById(R.id.localTgt);
        gdyTgt = findViewById(R.id.gdyTgt);
        getWdtgt = findViewById(R.id.getWdtgt);
        localToken = findViewById(R.id.localToken);
        dianzan = findViewById(R.id.dianzan);
        likeLayout2 = findViewById(R.id.like_love);
        dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeLayout2.startAnimation();
            }
        });

        getWdtgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    wdTgt.setText("从万达获取的tgt："+VideoInteractiveParam.getInstance().getCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            localTgt.setText("本地tgt码:" + PersonInfoManager.getInstance().getTgtCode());
            gdyTgt.setText("广电云tgt：" + PersonInfoManager.getInstance().getGdyToken());
            localToken.setText("数智融媒token：" + PersonInfoManager.getInstance().getTransformationToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}