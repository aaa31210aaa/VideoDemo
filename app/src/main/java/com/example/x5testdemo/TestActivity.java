package com.example.x5testdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

public class TestActivity extends AppCompatActivity {
    private WindowPlayer superPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_test);
        //mPlayerView 即step1中添加的界面 view
        TXCloudVideoView mView = (TXCloudVideoView) findViewById(R.id.video_view);
        //创建 player 对象
        TXVodPlayer mVodPlayer = new TXVodPlayer(this);
        //关键 player 对象与界面 view
        mVodPlayer.setPlayerView(mView);
        mVodPlayer.startPlay("https://oss.changsha.cn/2021/20210820/V17321732381080_1080.mp4");
    }
}