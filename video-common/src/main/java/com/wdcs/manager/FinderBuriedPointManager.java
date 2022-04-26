package com.wdcs.manager;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.model.DataDTO;
import com.wdcs.model.FinderPointModel;
import com.wdcs.model.FinderPointVideoPlay;
import com.wdcs.model.VideoCollectionModel;
import com.wdcs.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FinderBuriedPointManager {

    /**
     * finder埋点
     *
     * @param model
     */
    public static void setFinderBuriedPoint(String eventStr, FinderPointModel model) {
        VideoInteractiveParam.getInstance().setFinderPoint(eventStr, model);
    }

    /**
     * 按钮点击finder埋点
     *
     * @param buttonName
     */
    public static void setFinderClick(String buttonName) {
        FinderPointModel model = new FinderPointModel();
        model.setButton_name(buttonName);
        VideoInteractiveParam.getInstance().setFinderPoint(Constants.SHORT_VIDEO_PAGE_CLICK, model);
    }

    public static void setFinderCommon(String eventStr, FinderPointModel model) {
        VideoInteractiveParam.getInstance().setFinderPoint(eventStr, model);
    }

    public static void setFinderLikeFavoriteShare(String eventStr, DataDTO mDataDTO) {
        if (null == mDataDTO) {
            return;
        }
        FinderPointModel model = new FinderPointModel();
        model.setContent_id(mDataDTO.getId() + "");
        model.setContent_name(mDataDTO.getBrief());
        model.setContent_source(mDataDTO.getSource());
        model.setThird_ID(mDataDTO.getThirdPartyId());
//        List<String> keyWords = Arrays.asList(mDataDTO.getKeywords().split(","));
//        List<String> tags = Arrays.asList(mDataDTO.getTags().split(","));
//        model.setContent_key(keyWords);
//        model.setContent_list(tags);
        model.setContent_key(mDataDTO.getKeywordsShow());
        model.setContent_list(mDataDTO.getTagsShow());
        model.setContent_classify(mDataDTO.getClassification());
        model.setCreate_time(mDataDTO.getCreateTime());
        model.setPublish_time(mDataDTO.getStartTime());
        model.setContent_type(mDataDTO.getType());
        if (TextUtils.equals(eventStr, Constants.CONTENT_TRANSMIT)) {
            model.setTransmit_location("底部分享");
        }
        VideoInteractiveParam.getInstance().setFinderPoint(eventStr, model);
    }

    /**
     * 点赞收藏分享finder埋点
     *
     * @param eventStr
     * @param mDataDTO
     */
    public static void setFinderLikeFavoriteShare(String eventStr, VideoCollectionModel.DataDTO.RecordsDTO mDataDTO) {
        if (null == mDataDTO) {
            return;
        }
        FinderPointModel model = new FinderPointModel();
        model.setContent_id(mDataDTO.getId() + "");
        model.setContent_name(mDataDTO.getBrief());
        model.setContent_source(mDataDTO.getSource());
        model.setThird_ID(mDataDTO.getThirdPartyId());
//        List<String> keyWords = Arrays.asList(mDataDTO.getKeywords().split(","));
//        List<String> tags = Arrays.asList(mDataDTO.getTags().split(","));
//        model.setContent_key(keyWords);
//        model.setContent_list(tags);
        model.setContent_key(mDataDTO.getKeywordsShow());
        model.setContent_list(mDataDTO.getTagsShow());
        model.setContent_classify(mDataDTO.getClassification());
        model.setCreate_time(mDataDTO.getCreateTime());
        model.setPublish_time(mDataDTO.getStartTime());
        model.setContent_type(mDataDTO.getType());
        if (TextUtils.equals(eventStr, Constants.CONTENT_TRANSMIT)) {
            model.setTransmit_location("底部分享");
        }
        VideoInteractiveParam.getInstance().setFinderPoint(eventStr, model);
    }

    /**
     * 视频播放倍速finder埋点
     */
    public static void setFinderSpeed(String eventStr, DataDTO mDataDTO, String speed) {
        if (null == mDataDTO) {
            return;
        }
        FinderPointModel model = new FinderPointModel();
        model.setSpeed_n(speed);
        model.setContent_id(mDataDTO.getId() + "");
        model.setContent_name(mDataDTO.getBrief());
        model.setContent_source(mDataDTO.getSource());
        model.setThird_ID(mDataDTO.getThirdPartyId());
//        List<String> keyWords = Arrays.asList(mDataDTO.getKeywords().split(","));
//        List<String> tags = Arrays.asList(mDataDTO.getTags().split(","));
//        model.setContent_key(keyWords);
//        model.setContent_list(tags);
        model.setContent_key(mDataDTO.getKeywordsShow());
        model.setContent_list(mDataDTO.getTagsShow());
        model.setContent_classify(mDataDTO.getClassification());
        model.setCreate_time(mDataDTO.getCreateTime());
        model.setPublish_time(mDataDTO.getStartTime());
        VideoInteractiveParam.getInstance().setFinderPoint(eventStr, model);
    }

    /**
     * 播放时长
     */
    public static void setFinderVideo(String eventStr, String isRenew, DataDTO mDataDTO, long duration) {
        if (null == mDataDTO) {
            return;
        }
        FinderPointModel model = new FinderPointModel();
//        model.setModule_source("");
        if (TextUtils.equals(Constants.CONTENT_VIDEO_PLAY, eventStr)) {
            model.setIs_renew(isRenew);
        }

        if (TextUtils.equals(Constants.CONTENT_VIDEO_DURATION, eventStr)) {
            model.setPlay_duration(duration + "");
        }
        model.setContent_id(mDataDTO.getId() + "");
        model.setContent_name(mDataDTO.getBrief());
        model.setCreator_id(mDataDTO.getCreateBy());
        model.setContent_source(mDataDTO.getSource());
        model.setThird_ID(mDataDTO.getThirdPartyId());
//        List<String> keyWords = Arrays.asList(mDataDTO.getKeywords().split(","));
//        List<String> tags = Arrays.asList(mDataDTO.getTags().split(","));
//        model.setContent_key(keyWords);
//        model.setContent_list(tags);
        model.setContent_key(mDataDTO.getKeywordsShow());
        model.setContent_list(mDataDTO.getTagsShow());
        model.setContent_classify(mDataDTO.getClassification());
        model.setContent_type(mDataDTO.getType());
        model.setCreate_time(mDataDTO.getCreateTime());
        model.setPublish_time(mDataDTO.getStartTime());
        VideoInteractiveParam.getInstance().setFinderPoint(eventStr, model);
        Log.e("finder", "上报finder埋点:" + "事件为" + eventStr);
    }

    /**
     * 开始播放
     */
    public static void setFinderVideoPlay(String eventStr, String isRenew, DataDTO mDataDTO) {
        if (null == mDataDTO) {
            return;
        }
        FinderPointVideoPlay model = new FinderPointVideoPlay();
        model.setIs_renew(isRenew);
        model.setContent_id(mDataDTO.getId() + "");
        model.setContent_name(mDataDTO.getBrief());
        model.setCreator_id(mDataDTO.getCreateBy());
        model.setContent_source(mDataDTO.getSource());
        model.setThird_ID(mDataDTO.getThirdPartyId());
        model.setContent_key(mDataDTO.getKeywordsShow());
        model.setContent_list(mDataDTO.getTagsShow());
        model.setContent_classify(mDataDTO.getClassification());
        model.setContent_type(mDataDTO.getType());
        model.setCreate_time(mDataDTO.getCreateTime());
        model.setPublish_time(mDataDTO.getStartTime());
        VideoInteractiveParam.getInstance().setFinderPoint(eventStr, model);
    }

    /**
     * 开始播放
     */
    public static void setFinderVideoPlay(String eventStr, String isRenew, VideoCollectionModel.DataDTO.RecordsDTO mDataDTO) {
        if (null == mDataDTO) {
            return;
        }
        FinderPointVideoPlay model = new FinderPointVideoPlay();
        model.setIs_renew(isRenew);
        model.setContent_id(mDataDTO.getId() + "");
        model.setContent_name(mDataDTO.getBrief());
        model.setCreator_id(mDataDTO.getCreateBy());
        model.setContent_source(mDataDTO.getSource());
        model.setThird_ID(mDataDTO.getThirdPartyId());
        model.setContent_key(mDataDTO.getKeywordsShow());
        model.setContent_list(mDataDTO.getTagsShow());
        model.setContent_classify(mDataDTO.getClassification());
        model.setContent_type(mDataDTO.getType());
        model.setCreate_time(mDataDTO.getCreateTime());
        model.setPublish_time(mDataDTO.getStartTime());
        VideoInteractiveParam.getInstance().setFinderPoint(eventStr, model);
    }


    public static void setFinderVideo(String eventStr, String isRenew, VideoCollectionModel.DataDTO.RecordsDTO mDataDTO, long duration) {
        if (null == mDataDTO) {
            return;
        }
        FinderPointModel model = new FinderPointModel();
//        model.setModule_source("");
        if (TextUtils.equals(Constants.CONTENT_VIDEO_PLAY, eventStr)) {
            model.setIs_renew(isRenew);
        }

        if (TextUtils.equals(Constants.CONTENT_VIDEO_DURATION, eventStr)) {
            model.setPlay_duration(duration + "");
        }
        model.setContent_id(mDataDTO.getId() + "");
        model.setContent_name(mDataDTO.getBrief());
        model.setCreator_id(mDataDTO.getCreateBy());
        model.setContent_source(mDataDTO.getSource());
        model.setThird_ID(mDataDTO.getThirdPartyId());
//        List<String> keyWords = Arrays.asList(mDataDTO.getKeywords().split(","));
//        List<String> tags = Arrays.asList(mDataDTO.getTags().split(","));
//        model.setContent_key(keyWords);
//        model.setContent_list(tags);
        model.setContent_key(mDataDTO.getKeywordsShow());
        model.setContent_list(mDataDTO.getTagsShow());
        model.setContent_classify(mDataDTO.getClassification());
        model.setContent_type(mDataDTO.getType());
        model.setCreate_time(mDataDTO.getCreateTime());
        model.setPublish_time(mDataDTO.getStartTime());
        VideoInteractiveParam.getInstance().setFinderPoint(eventStr, model);
    }

}
