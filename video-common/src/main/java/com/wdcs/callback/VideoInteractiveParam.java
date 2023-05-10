package com.wdcs.callback;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.wdcs.common.R;
import com.wdcs.http.ApiConstants;
import com.wdcs.model.BuriedPointModel;
import com.wdcs.model.FinderPointModel;
import com.wdcs.model.FinderPointVideoPlay;
import com.wdcs.model.GdyTokenModel;
import com.wdcs.model.ShareInfo;
import com.wdcs.model.TokenModel;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 参数交互类
 */
public class VideoInteractiveParam {
    public VideoParamCallBack callBack;
    public GetGdyTokenCallBack gdyTokenCallBack;
    public VideoFinderPointCallBack videoFinderPointCallBack;
    public ApplicationIsAgreeCallBack applicationIsAgreeCallBack;
    public static VideoInteractiveParam param;
    private String transformationToken;
    private Context context;

    private VideoInteractiveParam() {
    }

    public static VideoInteractiveParam getInstance() {
        if (param == null) {
            synchronized (VideoInteractiveParam.class) {
                if (param == null) {
                    param = new VideoInteractiveParam();
                }
            }
        }
        return param;
    }

    public void setCallBack(VideoParamCallBack callBack) {
        this.callBack = callBack;
    }

    public void setGdyTokenCallBack(GetGdyTokenCallBack callBack) {
        this.gdyTokenCallBack = callBack;
    }

    public void setVideoFinderPointCallBack(VideoFinderPointCallBack finderPointCallBack) {
        this.videoFinderPointCallBack = finderPointCallBack;
    }

    public void setApplicationIsAgreeCallBack(ApplicationIsAgreeCallBack isAgreeCallBack) {
        this.applicationIsAgreeCallBack = isAgreeCallBack;
    }


    /**
     * 传递分享对象
     *
     * @param shareInfo
     * @throws Exception
     */
    public void shared(ShareInfo shareInfo) throws Exception {
        if (callBack == null) {
            Log.e("shared", "获取失败，请重试");
        } else {
            callBack.shared(shareInfo);
        }
    }

    /**
     * 登录
     */
    public void toLogin() throws Exception {
        if (callBack == null) {
            Log.e("toLogin", "获取失败，请重试");
        } else {
            callBack.Login();
        }

    }

    /**
     * 获取code
     *
     * @return
     */
    public String getCode() throws Exception {
        if (callBack == null) {
            Log.e("getCode", "获取失败，请重试");
            return "";
        }
        return callBack.setCode();
    }

    /**
     * 传递跳转url
     */
    public void recommendUrl(String url, ShareInfo shareInfo) throws Exception {
        if (callBack == null) {
            Log.e("recommendUrl", "获取失败，请重试");
        } else {
            callBack.recommedUrl(url, shareInfo);
        }
    }

    /**
     * 传递视频埋点的信息
     */
    public void trackingPoint(BuriedPointModel buriedPointModel) throws Exception {
        if (callBack == null) {
            Log.e("trackingPoint", "获取失败，请重试");
        } else {
            callBack.trackingPoint(buriedPointModel);
        }
    }


    /**
     * 获取设备id
     */
    public String getDeviceId() {
        if (callBack == null) {
            return "";
        } else {
            return callBack.setDeviceId();
        }
    }

    /**
     * 返回token
     *
     * @return
     */
    public void checkLoginStatus() {
        if (gdyTokenCallBack == null) {
            Log.e("checkLoginStatus", "获取失败，请重试");
        } else {
            getUserToken();
        }
    }

    /**
     * 获取Finder埋点数据
     */
    public void setFinderPoint(String eventStr, FinderPointModel model) {
        if (videoFinderPointCallBack == null) {
            Log.e("videoFinderPoint:", "获取失败，请重试");
        } else {
            if (model != null) {
                Gson gson = new Gson();
                String paramJson = gson.toJson(model);
                JSONObject json = null;
                try {
                    json = new JSONObject(paramJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                videoFinderPointCallBack.getFinderPoint(eventStr, json);
            } else {
                videoFinderPointCallBack.getFinderPoint(eventStr, new JSONObject());
            }
        }
    }

    /**
     * 获取Finder埋点数据
     */
    public void setFinderPoint(String eventStr, FinderPointVideoPlay model) {
        if (videoFinderPointCallBack == null) {
            Log.e("videoFinderPoint:", "获取失败，请重试");
        } else {
            if (model != null) {
                Gson gson = new Gson();
                String paramJson = gson.toJson(model);
                JSONObject json = null;
                try {
                    json = new JSONObject(paramJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                videoFinderPointCallBack.getFinderPoint(eventStr, json);
            } else {
                videoFinderPointCallBack.getFinderPoint(eventStr, new JSONObject());
            }
        }
    }

    /**
     * 获取app是否同意了隐私协议
     */
    public String getApplicationIsAgree() {
        if (applicationIsAgreeCallBack == null) {
            Log.e("ApplicationIsAgree", "获取失败,请重试");
        } else {
            return applicationIsAgreeCallBack.setIsAgreePrivacy();
        }
        return "";
    }

    /**
     * 获取fragment实例
     */
    public Fragment getWebViewFragment(Bundle bundle, MutableLiveData<WebView> liveData) {
        if (null == callBack) {
            Log.e("getWebViewFragment", "获取失败,请重试");
        } else {
            return callBack.getWebViewFragment(bundle, liveData);
        }
        return new Fragment();
    }


    /**
     * 使用获取的code去换token
     */
    public void getUserToken() {
        if (PersonInfoManager.getInstance().isRequestToken()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", VideoInteractiveParam.getInstance().getCode());
                jsonObject.put("ignoreGdy", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            OkGo.<TokenModel>post(ApiConstants.getInstance().mycsToken())
                    .tag("userToken")
                    .upJson(jsonObject)
                    .execute(new JsonCallback<TokenModel>(TokenModel.class) {
                        @Override
                        public void onSuccess(Response<TokenModel> response) {
                            if (null == response.body()) {
                                ToastUtils.showShort("系统数据错误，请重试");
                                return;
                            }

                            if (response.body().getCode() == 200) {
                                if (null == response.body().getData()) {
                                    ToastUtils.showShort("系统数据错误，请重试");
                                    return;
                                }
                                try {
                                    PersonInfoManager.getInstance().setToken(VideoInteractiveParam.getInstance().getCode());
                                    PersonInfoManager.getInstance().setUserId(response.body().getData().getLoginSysUserVo().getId());
                                    PersonInfoManager.getInstance().setTgtCode(VideoInteractiveParam.getInstance().getCode());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                transformationToken = response.body().getData().getToken();
                                PersonInfoManager.getInstance().setTransformationToken(transformationToken);
                                getGdyToken(PersonInfoManager.getInstance().getTransformationToken());
                            } else {
                                Log.e("getUserToken", response.body().getMessage());
                            }
                        }

                        @Override
                        public void onError(Response<TokenModel> response) {
                            if (null != response.body()) {
                                Log.e("getUserToken", response.body().getMessage());
                                return;
                            }
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                        }
                    });
        } else {
            getGdyToken(PersonInfoManager.getInstance().getTransformationToken());
        }
    }

    /**
     * 换取广电云token
     * token 融媒token
     */
    private void getGdyToken(String token) {
        JSONObject jsonObject = new JSONObject();
        OkGo.<GdyTokenModel>post(ApiConstants.getInstance().gdyToken())
                .tag("getGdyToken")
                .headers("token", token)
                .upJson(jsonObject)
                .execute(new JsonCallback<GdyTokenModel>(GdyTokenModel.class) {
                    @Override
                    public void onSuccess(Response<GdyTokenModel> response) {
                        try {
                            if (response.body().getCode() == 200) {
                                if (null == response.body().getData()) {
                                    ToastUtils.showShort("系统数据错误，请重试");
                                    return;
                                }
                                PersonInfoManager.getInstance().setGdyToken(response.body().getData());
                                //本地有的时候
                                if (null != gdyTokenCallBack) {
                                    gdyTokenCallBack.checkLoginStatus(PersonInfoManager.getInstance().getGdyToken());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<GdyTokenModel> response) {
                        if (null != response.body()) {
                            Log.e("getGdyToken", response.body().getMessage());
                            return;
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
