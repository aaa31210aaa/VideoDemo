package http;

public class ApiConstants {
    public String baseUrl;
    public String loginBaseUrl;

    private static ApiConstants instance;

    public static ApiConstants getInstance() {
        if (instance == null) {
            synchronized (ApiConstants.class) {
                if (instance == null) {
                    instance = new ApiConstants();
                }
            }
        }
        return instance;
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLoginBaseUrl() {
        return loginBaseUrl;
    }

    public void setLoginBaseUrl(String loginBaseUrl) {
        this.loginBaseUrl = loginBaseUrl;
    }

    /**
     * 获取视频下拉列表
     */
    public String getVideoDetailListUrl() {
        return getBaseUrl() + "client/video/queryVideoPullDownList";
    }

    /**
     * 获取随机推荐视频列表
     */
    public String getVideDetailRandomListUrl() {
        return getBaseUrl() + "client/video/queryRandomVideoList";
    }

    /**
     * 获取视频详情
     */
    public String getVideoDetailUrl() {
        return getBaseUrl() + "client/video/getVideoDetails/";
    }

    /**
     * 获取视频合集
     */
    public String getVideoCollectionUrl() {
        return getBaseUrl() + "client/video/getVideoCollect/";
    }

    /**
     * 获取视频评论列表
     */
    public String getCommentListUrl() {
        return getBaseUrl() + "client/comment/getCommentByContent";
    }

    /**
     * 添加评论
     */
    public String addComment() {
        return getBaseUrl() + "client/comment/add";
    }

    /**
     * 查询统计数据
     */
    public String queryStatsData() {
        return getBaseUrl() + "client/contentStats/queryStatsData";
    }

    /**
     * 收藏/取消收藏
     */
    public String addOrCancelFavor() {
        return getBaseUrl() + "client/favor/addOrCancelFavor";
    }

    /**
     * 点赞/取消点赞
     */
    public String addOrCancelLike() {
        return getBaseUrl() + "client/like/likeOrCancel";
    }

    /**
     * 获取token值
     */
    public String getToken() {
        return getLoginBaseUrl() + "login/mycs";
    }

    /**
     * 我的长沙token登录
     */
    public String mycsToken() {
        return getLoginBaseUrl() + "login/mycs/token";
    }

    /**
     * 浏览量+1接口
     */
    public String addViews() {
        return getBaseUrl() + "client/contentStats/view/count/";
    }
}
