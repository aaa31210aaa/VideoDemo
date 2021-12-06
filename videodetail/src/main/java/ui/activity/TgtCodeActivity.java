package ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.videodetail.demo.R;

public class TgtCodeActivity extends AppCompatActivity {
    private TextView tgt;
    private TextView to_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tgt_code);
        tgt = findViewById(R.id.tgt);
        to_video = findViewById(R.id.to_video);
        to_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TgtCodeActivity.this, VideoDetailActivity.class);
                intent.putExtra("contentId", "737797");
                startActivity(intent);
            }
        });
        try {
            tgt.setText("tgt码:" + VideoInteractiveParam.getInstance().getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}