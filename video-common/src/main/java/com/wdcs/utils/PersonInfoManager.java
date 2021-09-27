package com.wdcs.utils;


import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;

import java.util.HashMap;
import java.util.Map;


public class PersonInfoManager {
    private static PersonInfoManager instance;
    private Map headMap;

    public static PersonInfoManager getInstance() {
        if (instance == null) {
            synchronized (PersonInfoManager.class) {
                if (instance == null) {
                    instance = new PersonInfoManager();
                }
            }
        }
        return instance;
    }

    public Map getHeadMap() {
        headMap = new HashMap();
        headMap.put("deviceId", getANDROID_ID());
        return headMap;
    }

    public String getANDROID_ID() {
        String ANDROID_ID = Settings.System.getString(Utils.getContext().getContentResolver(), Settings.System.ANDROID_ID);
        if (TextUtils.isEmpty(ANDROID_ID)) {
            return SPUtils.getInstance().getString(Constants.PUSH_TOKEN, "");
        }
        return ANDROID_ID;
    }

    /**
     * 保存用户token
     *
     * @param token
     */
    public void setToken(String token) {
        SPUtils.getInstance().put(Constants.TYPE_TOKEN, token);
    }

    /**
     * 获取用户token
     *
     * @return
     */
    public String getToken() {
        return SPUtils.getInstance().getString(Constants.TYPE_TOKEN, "");
    }

    /**
     * 获取用户id
     */
    public String getUserId() {
        return SPUtils.getInstance().getString(Constants.LOCAL_USER_ID, "");
    }

    /**
     * 保存用户id
     */
    public void setUserId(String userId) {
        SPUtils.getInstance().put(Constants.LOCAL_USER_ID, userId);
    }

    /**
     * 获取光电云token
     */
    public String getGdyToken() {
        return SPUtils.getInstance().getString(Constants.GDY_TOKEN, "");
    }

    /**
     * 保存
     */
    public void setGdyToken(String userId) {
        SPUtils.getInstance().put(Constants.GDY_TOKEN, userId);
    }

    public void setTgtCode(String tgt) {
        SPUtils.getInstance().put(Constants.TGT_CODE, tgt);
    }

    public String getTgtCode(){
        return SPUtils.getInstance().getString(Constants.TGT_CODE, "");
    }

    /**
     * 保存转换后的用户token
     *
     * @param transformationToken
     */
    public void setTransformationToken(String transformationToken) {
        SPUtils.getInstance().put(Constants.TRANSFORMATION_TOKEN, transformationToken);
    }

    /**
     * 获取转换后的用户token
     * return
     */
    public String getTransformationToken() {
        return SPUtils.getInstance().getString(Constants.TRANSFORMATION_TOKEN, "");
    }

    /**
     * 清空本地token
     */
    public void clearToken() {
        PersonInfoManager.getInstance().setToken("");
        PersonInfoManager.getInstance().setTransformationToken("");
        PersonInfoManager.getInstance().setTgtCode("");
        PersonInfoManager.getInstance().setUserId("");
        PersonInfoManager.getInstance().setGdyToken("");
    }

    /**
     * 判断是否要去请求转换token的接口
     *
     * @return
     */
    public boolean isRequestToken() {
        try {
            if (!TextUtils.isEmpty(VideoInteractiveParam.getInstance().getCode())) {//获取的token不为空
                if (!TextUtils.isEmpty(PersonInfoManager.getInstance().getTgtCode())) { //本地token不为空
                    if (TextUtils.equals(PersonInfoManager.getInstance().getTgtCode(),
                            VideoInteractiveParam.getInstance().getCode())) {
                        Log.e("YQH_Token", "我的长沙已登录_数智已登");
                        return false;
                    } else {
                        Log.e("YQH_Token", "获取到的tgt和本地tgt不一致,我的长沙切换了用户_重登数智融媒");
                        clearToken();
                        return true;
                    }
                } else {
                    Log.e("YQH_Token", "本地tgt为空，相当于第一次登录，需要请求");
                    return true;
                }

            } else {
                //获取的tgt为空，没有登录，不需要请求  点击点赞收藏等则会跳转登录
                clearToken();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
