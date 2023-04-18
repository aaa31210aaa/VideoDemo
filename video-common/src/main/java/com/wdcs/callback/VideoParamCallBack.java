package com.wdcs.callback;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdcs.model.BuriedPointModel;
import com.wdcs.model.ShareInfo;

/**
 * 交互参数的接口
 */
public interface VideoParamCallBack {

    /**
     * 获取分享四要素函数
     *
     * @param shareInfo
     */
    void shared(ShareInfo shareInfo);

    /**
     * 登录
     */
    void Login();

    /**
     * 获取token值
     */
    String setCode();

    /**
     * 获取推荐跳转的url
     */
    void recommedUrl(@NonNull String url, @Nullable ShareInfo shareInfo);

    /**
     * 获取视频埋点信息
     */
    void trackingPoint(BuriedPointModel buriedPointModel);

    /**
     * 获取设备id
     */
    String setDeviceId();

    /**
     * 获取fragment对象
     * @param bundle
     * @return
     */
    Fragment getWebViewFragment(Bundle bundle);

}
