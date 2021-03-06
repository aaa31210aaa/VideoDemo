package callback;

import model.ShareInfo;

/**
 * 交互参数的接口
 */
public interface VideoParamCallBack {

    /**
     * 获取分享四要素函数
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
}
