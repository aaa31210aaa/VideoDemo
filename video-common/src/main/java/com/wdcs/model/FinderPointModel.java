package com.wdcs.model;

import android.text.TextUtils;

import androidx.annotation.Keep;

import java.util.List;

/**
 * finer埋点模型
 */
@Keep
public class FinderPointModel {
    /**
     * 内容ID
     */
    private String content_id;
    /**
     * 是否完播
     */
    private String isFinish;
    /**
     * 内容名称
     */
    private String content_name;
    /**
     * 内容来源
     */
    private String content_source;
    /**
     * 内容第三方ID
     */
    private String third_ID;
    /**
     * 内容关键词
     */
    private List<String> content_key;
    /**
     * 内容标签
     */
    private List<String> content_list;
    /**
     * 内容分类
     */
    private String content_classify;
    /**
     * 内容创建时间
     */
    private String create_time;
    /**
     * 内容发布时间
     */
    private String publish_time;
    /**
     * 内容类别
     */
    private String content_type;
    /**
     * 分享位置
     */
    private String transmit_location;
    /**
     * 入口来源
     */
    private String module_source;
    /**
     * 是否为重播
     */
    private String is_renew;
    /**
     * 播放时长
     */
    private String play_duration;
    /**
     *创作者ID
     */
    private String creator_id;
    /**
     * 位置序号
     */
    private String rank_num;
    /**
     * 按钮名称
     */
    private String button_name;

    /**
     * 作品简介
     */
    private String works_brief;

    /**
     * 作品时长
     */
    private String works_duration;

    /**
     * 作品大小
     */
    private String works_size;

    /**
     * 用户ID  点击谁的头像，记录谁的用户ID
     */
    private String user_id;

    /**
     * 是否关注
     */
    private String is_notice;

    /**
     * 倍速
     */
    private String speed_n;

    public String getContent_id() {
        if (TextUtils.isEmpty(content_id)) {
            return "";
        }
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getContent_name() {
        if (TextUtils.isEmpty(content_name)) {
            return "";
        }
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    public String getContent_source() {
        if (TextUtils.isEmpty(content_source)) {
            return "";
        }
        return content_source;
    }

    public void setContent_source(String content_source) {
        this.content_source = content_source;
    }

    public String getThird_ID() {
        if (TextUtils.isEmpty(third_ID)) {
            return "";
        }
        return third_ID;
    }

    public void setThird_ID(String third_ID) {
        this.third_ID = third_ID;
    }

//    public String getContent_key() {
//        if (TextUtils.isEmpty(content_key)) {
//            return "";
//        }
//        return content_key;
//    }
//
//    public void setContent_key(String content_key) {
//        this.content_key = content_key;
//    }
//
//    public String getContent_list() {
//        if (TextUtils.isEmpty(content_list)) {
//            return "";
//        }
//        return content_list;
//    }
//
//    public void setContent_list(String content_list) {
//        this.content_list = content_list;
//    }


    public List<String> getContent_key() {
        return content_key;
    }

    public void setContent_key(List<String> content_key) {
        this.content_key = content_key;
    }

    public List<String> getContent_list() {
        return content_list;
    }

    public void setContent_list(List<String> content_list) {
        this.content_list = content_list;
    }

    public String getContent_classify() {
        if (TextUtils.isEmpty(content_classify)) {
            return "";
        }
        return content_classify;
    }

    public void setContent_classify(String content_classify) {
        this.content_classify = content_classify;
    }

    public String getCreate_time() {
        if (TextUtils.isEmpty(create_time)) {
            return "";
        }
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPublish_time() {
        if (TextUtils.isEmpty(publish_time)) {
            return "";
        }
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getContent_type() {
        if (TextUtils.isEmpty(content_type)) {
            return "";
        }
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getTransmit_location() {
        if (TextUtils.isEmpty(transmit_location)) {
            return "";
        }
        return transmit_location;
    }

    public void setTransmit_location(String transmit_location) {
        this.transmit_location = transmit_location;
    }

    public String getModule_source() {
        if (TextUtils.isEmpty(module_source)) {
            return "";
        }
        return module_source;
    }

    public void setModule_source(String module_source) {
        this.module_source = module_source;
    }

    public String getIs_renew() {
        if (TextUtils.isEmpty(is_renew)) {
            return "";
        }
        return is_renew;
    }

    public void setIs_renew(String is_renew) {
        this.is_renew = is_renew;
    }

    public String getPlay_duration() {
        if (TextUtils.isEmpty(play_duration)) {
            return "";
        }
        return play_duration;
    }

    public void setPlay_duration(String play_duration) {
        this.play_duration = play_duration;
    }

    public String getCreator_id() {
        if (TextUtils.isEmpty(creator_id)) {
            return "";
        }
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getRank_num() {
        if (TextUtils.isEmpty(rank_num)) {
            return "";
        }
        return rank_num;
    }

    public void setRank_num(String rank_num) {
        this.rank_num = rank_num;
    }

    public String getButton_name() {
        if (TextUtils.isEmpty(button_name)) {
            return "";
        }
        return button_name;
    }

    public void setButton_name(String button_name) {
        this.button_name = button_name;
    }

    public String getWorks_brief() {
        if (TextUtils.isEmpty(works_brief)) {
            return "";
        }
        return works_brief;
    }

    public void setWorks_brief(String works_brief) {
        this.works_brief = works_brief;
    }

    public String getWorks_duration() {
        if (TextUtils.isEmpty(works_duration)) {
            return "";
        }
        return works_duration;
    }

    public void setWorks_duration(String works_duration) {
        this.works_duration = works_duration;
    }

    public String getWorks_size() {
        if (TextUtils.isEmpty(works_size)) {
            return "";
        }
        return works_size;
    }

    public void setWorks_size(String works_size) {
        this.works_size = works_size;
    }

    public String getUser_id() {
        if (TextUtils.isEmpty(user_id)) {
            return "";
        }
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIs_notice() {
        if (TextUtils.isEmpty(is_notice)) {
            return "";
        }
        return is_notice;
    }

    public void setIs_notice(String is_notice) {
        this.is_notice = is_notice;
    }

    public String getSpeed_n() {
        if (TextUtils.isEmpty(speed_n)) {
            return "";
        }
        return speed_n;
    }

    public void setSpeed_n(String speed_n) {
        this.speed_n = speed_n;
    }
}
