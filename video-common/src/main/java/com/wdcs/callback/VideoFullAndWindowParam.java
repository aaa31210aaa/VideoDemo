package com.wdcs.callback;

import android.util.Log;

import com.wdcs.model.ShowHomeFragmentModel;

public class VideoFullAndWindowParam {
    public VideoFullAndWindowCallBack videoFullAndWindowCallBack;
    public static VideoFullAndWindowParam videoFullAndWindowParam;

    private VideoFullAndWindowParam() {
    }

    public static VideoFullAndWindowParam getInstance() {
        if (videoFullAndWindowParam == null) {
            synchronized (VideoInteractiveParam.class) {
                if (videoFullAndWindowParam == null) {
                    videoFullAndWindowParam = new VideoFullAndWindowParam();
                }
            }
        }
        return videoFullAndWindowParam;
    }

    public void setVideoFullAndWindowCallBack(VideoFullAndWindowCallBack callBack) {
        this.videoFullAndWindowCallBack = callBack;
    }

    public void VideoFullAndWindow(int state) throws Exception {
        if (null == videoFullAndWindowCallBack) {
            Log.e("VideoFullAndWindow", "获取失败，请重试");
        } else {
            videoFullAndWindowCallBack.videoFullAndWindowState(state);
        }
    }

}
