package com.wdcs.constants;

public class Constants {
    public static final String success_code = "200";
    public static final String token_error = "401";
    public static final String USER_ID = "user_id";
    public static final String LOCAL_USER_ID = "local_user_id";
    public static final String TYPE_TOKEN = "token";
    public static final String GDY_TOKEN = "gdy_token";
    public static final String TRANSFORMATION_TOKEN = "transformation_token";

    public static final String KEY_USER = "keyuser";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String PUSH_TOKEN = "pushtoken";
    public static final String SHARE_WX = "wx";
    public static final String SHARE_CIRCLE = "circle";
    public static final String SHARE_QQ = "qq";
    public static final String AGREE_NETWORK = "agree_network";
    public static final String CONTENT_TYPE = "视频";
    public static final String WX_STRING = "朋友";
    public static final String CIRCLE_STRING = "朋友圈";
    public static final String QQ_STRING = "qq";
    public static final String PARAME_VIDEO_CHANNEL = "videochannletype";
    public static final String PANELCODE = "panelCode";
    public static final String VIDEOTAG = "videoTag";
    public static final String TRACKINGUPLOAD = "TrackingUpload";
    public static final String BLUE_V = "blue-v";
    public static final String YELLOW_V = "yellow-v";
    public static final String RED_V = "red-v";
    /**
     * 1.排行榜https://uat-h5.zhcs.csbtv.com/act/xksh/#/ranking
     * 2.话题详情https://uat-h5.zhcs.csbtv.com/act/xksh/#/topicDetails?id=215403
     * 3.个人中心https://uat-h5.zhcs.csbtv.com/act/xksh/#/me
     * 4.头像TA人主页https://uat-h5.zhcs.csbtv.com/act/xksh/#/others?id=53901
     * 5.搜索页 https://uat-h5.zhcs.csbtv.com/fuse/news/#/searchPlus
     */
    public static final String RANKING_LIST = "https://uat-h5.zhcs.csbtv.com/act/xksh/#/ranking";
    public static final String TOPIC_DETAILS = "https://uat-h5.zhcs.csbtv.com/act/xksh/#/topicDetails?id=";
    public static final String PERSONAL_CENTER = "https://uat-h5.zhcs.csbtv.com/act/xksh/#/me";
    public static final String HEAD_OTHER = "https://uat-h5.zhcs.csbtv.com/act/xksh/#/others?id=";
    public static final String SEARCHPLUS = "https://uat-h5.zhcs.csbtv.com/fuse/news/#/searchPlus";

    /**
     * ssid 火山需要传入的设备id (暂时先固定填上测试  后续需从万达拿)
     */
    public static final String SSID = "88888888";
    /**
     * 刷新操作标识
     */
    public static final String REFRESH_TYPE = "refresh";
    /**
     * 加载更多操作标识
     */
    public static final String LOADMORE_TYPE = "loadmore";

    /**
     * 上报内容埋点固定传参
     */
    public static final String ENTER_FROM = "click_category";
    public static final String CATEGORY_NAME = "c2402539";
    public static final String PARAMS_FOR_SPECIAL = "content_manager_system";

    /**
     * 上报事件event
     */
    public static final String CMS_CLIENT_SHOW = "cms_client_show"; //露出即上报
    public static final String CMS_VIDEO_PLAY_AUTO = "cms_video_play_auto"; //在内流中滑动播放视频时上报
    public static final String CMS_VIDEO_OVER_AUTO = "cms_video_over_auto"; //滑动播放结束/自动播放结束
    public static final String CMS_VIDEO_PLAY = "cms_video_play"; //播放完成后点击「重播」
    public static final String CMS_VIDEO_OVER = "cms_video_over";  //重播的视频播放结束


}
