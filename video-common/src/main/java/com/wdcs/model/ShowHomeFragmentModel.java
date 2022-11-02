package com.wdcs.model;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Keep;

@Keep
public class ShowHomeFragmentModel {
    private Context context; //容器activity
    private int widgetId; //控件id
    private String contentId; //视频id
    private int toCurrentTab = 1; //跳转到第几个tab
    private String categoryName; //类别名称
    private String moduleSource; //模块来源

    public Context getActivity() {
        return context;
    }

    public void setActivity(Context context) {
        this.context = context;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public String getContentId() {
        if (TextUtils.isEmpty(contentId)) {
            return "";
        }
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getToCurrentTab() {
        return toCurrentTab;
    }

    public void setToCurrentTab(int toCurrentTab) {
        this.toCurrentTab = toCurrentTab;
    }

    public String getCategoryName() {
        if (TextUtils.isEmpty(categoryName)) {
            return "";
        }
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getModuleSource() {
        if (TextUtils.isEmpty(moduleSource)) {
            return "";
        }
        return moduleSource;
    }

    public void setModuleSource(String moduleSource) {
        this.moduleSource = moduleSource;
    }
}
