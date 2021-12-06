package com.wdcs.callback;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.wdcs.http.ApiConstants;
import com.wdcs.model.BuriedPointModel;
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
    public static VideoInteractiveParam param;
    private String transformationToken;

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

    public void setGdyTokenCallBack(GetGdyTokenCallBack callBack){
        this.gdyTokenCallBack = callBack;
    }


    /**
     * 传递分享对象
     *
     * @param shareInfo
     * @throws Exception
     */
    public void shared(ShareInfo shareInfo) throws Exception {
        if (callBack == null) {
            throw new Exception("获取失败,请重试");
        } else {
            callBack.shared(shareInfo);
        }
    }

    /**
     * 登录
     */
    public void toLogin() throws Exception {
        if (callBack == null) {
            throw new Exception("请求失败,请重试");
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
            throw new Exception("获取失败,请重试");
        } else {
            return callBack.setCode();
        }
    }

    /**
     * 传递跳转url
     */
    public void recommendUrl(String url, ShareInfo shareInfo) throws Exception {
        if (callBack == null) {
            throw new Exception("获取失败,请重试");
        } else {
            callBack.recommedUrl(url, shareInfo);
        }
    }

    /**
     * 传递视频埋点的信息
     */
    public void trackingPoint(BuriedPointModel buriedPointModel) throws Exception {
        if (callBack == null) {
            throw new Exception("获取失败,请重试");
        } else {
            callBack.trackingPoint(buriedPointModel);
        }
    }


    /**
     * 获取设备id
     */
    public String getDeviceId() throws Exception {
        if (callBack == null) {
            throw new Exception("获取失败,请重试");
        } else {
            return callBack.setDeviceId();
        }
    }

    /**
     * 返回token
     *
     * @return
     */
    public void checkLoginStatus() throws Exception{
        if (gdyTokenCallBack == null) {
            throw new Exception("获取失败,请重试");
        } else {
            getUserToken();
        }
    }

    /**
     * 使用获取的code去换token
     */
    public void getUserToken() {
        if (PersonInfoManager.getInstance().isRequestToken()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", VideoInteractiveParam.getInstance().getCode());
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
                                    PersonInfoManager.getInstance().setGdyToken(response.body().getData().getGdyToken());
                                    PersonInfoManager.getInstance().setUserId(response.body().getData().getLoginSysUserVo().getId());
                                    PersonInfoManager.getInstance().setTgtCode(VideoInteractiveParam.getInstance().getCode());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                transformationToken = response.body().getData().getToken();
                                PersonInfoManager.getInstance().setTransformationToken(transformationToken);

                                gdyTokenCallBack.checkLoginStatus(transformationToken);
                            } else {
                                ToastUtils.showShort(response.body().getMessage());
                            }
                        }

                        @Override
                        public void onError(Response<TokenModel> response) {
                            if (null != response.body()) {
                                ToastUtils.showShort(response.body().getMessage());
                                return;
                            }
                            ToastUtils.showShort("请求失败,请检查网络");
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                        }
                    });
        } else {
            //本地有的时候
            gdyTokenCallBack.checkLoginStatus(PersonInfoManager.getInstance().getGdyToken());
        }
    }
}
