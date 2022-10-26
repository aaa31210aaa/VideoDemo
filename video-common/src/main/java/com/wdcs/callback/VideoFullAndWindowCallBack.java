package com.wdcs.callback;


public interface VideoFullAndWindowCallBack {
    /**
     * 获取视频全屏 窗口 切换的状态值
     * @param state 1 为全屏， 0为窗口
     */
    void videoFullAndWindowState(int state);
}
