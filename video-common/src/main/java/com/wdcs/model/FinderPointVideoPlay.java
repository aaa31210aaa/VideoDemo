package com.wdcs.model;

import androidx.annotation.Keep;

import java.util.List;
@Keep
public class FinderPointVideoPlay {
    private String module_source;
    private String is_renew;
    private String content_id;
    private String content_name;
    private String creator_id;
    private String content_source;
    private String third_ID;
    private List<String> content_key;
    private List<String> content_list;
    private String content_classify;
    private String content_type;
    private String create_time;
    private String publish_time;

    public String getModule_source() {
        return module_source;
    }

    public void setModule_source(String module_source) {
        this.module_source = module_source;
    }

    public String getIs_renew() {
        return is_renew;
    }

    public void setIs_renew(String is_renew) {
        this.is_renew = is_renew;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getContent_source() {
        return content_source;
    }

    public void setContent_source(String content_source) {
        this.content_source = content_source;
    }

    public String getThird_ID() {
        return third_ID;
    }

    public void setThird_ID(String third_ID) {
        this.third_ID = third_ID;
    }

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
        return content_classify;
    }

    public void setContent_classify(String content_classify) {
        this.content_classify = content_classify;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }
}
