package callback;

import model.ShareInfo;
import utils.ToastUtils;


/**
 * 参数交互类
 */
public class VideoInteractiveParam {
    public VideoParamCallBack callBack;
    public static VideoInteractiveParam param;


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
}
