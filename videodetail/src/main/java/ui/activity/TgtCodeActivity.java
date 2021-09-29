package ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.videodetail.demo.R;

public class TgtCodeActivity extends AppCompatActivity {
    private TextView tgt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tgt_code);
        tgt = findViewById(R.id.tgt);
        try {
            tgt.setText("tgt码:" + VideoInteractiveParam.getInstance().getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}