package com.wdcs.callback;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.wdcs.http.ApiConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomePageInteractive {
    public static HomePageInteractive param;

    public static HomePageInteractive getInstance() {
        if (param == null) {
            synchronized (HomePageInteractive.class) {
                if (param == null) {
                    param = new HomePageInteractive();
                }
            }
        }
        return param;
    }

    /**
     * 获取首页顶图（负一屏）
     */
    public MutableLiveData<String> getHomeTopicImage(Map<String, String> mapParams) {
        MutableLiveData<String> homeTopicImageJson = new MutableLiveData<>();
        OkGo.<String>get(ApiConstants.getInstance().getCategoryCompositeData())
                .tag("TopicImage")
                .params(mapParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        homeTopicImageJson.setValue(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        homeTopicImageJson.setValue(null);
                    }
                });
        return homeTopicImageJson;
    }

    /**
     * 获取今日要闻
     */
    public MutableLiveData<String> getHomeTodayNews(Map<String, String> mapParams) {
        MutableLiveData<String> homeTodayNewsJson = new MutableLiveData<>();
        OkGo.<String>get(ApiConstants.getInstance().getCategoryCompositeData())
                .tag("TodayNews")
                .params(mapParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        homeTodayNewsJson.setValue(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        homeTodayNewsJson.setValue(null);
                    }
                });
        return homeTodayNewsJson;
    }

    /**
     * 首页栏目获取
     */
    public MutableLiveData<String> getHomeColumn(Map<String, String> mapParams) {
        MutableLiveData<String> homeColumnJson = new MutableLiveData<>();
        OkGo.<String>get(ApiConstants.getInstance().getCategoryData())
                .tag("HomeColumn")
                .params(mapParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        homeColumnJson.setValue(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        homeColumnJson.setValue(null);
                    }
                });
        return homeColumnJson;
    }

    /**
     * 获取栏目内容
     */
    public MutableLiveData<String> getHomeColumnContent(Map<String, String> mapParams) {
        MutableLiveData<String> homeColumnContentJson = new MutableLiveData<>();
        String url;
        if (TextUtils.equals(mapParams.get("refreshType"), "loadmore")) {
            url = ApiConstants.getInstance().getContentDate();
        } else {
            url = ApiConstants.getInstance().getContentData();
        }

        OkGo.<String>get(url)
                .tag("ColumnContent")
                .params(mapParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        homeColumnContentJson.setValue(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        homeColumnContentJson.setValue(null);
                    }
                });
        return homeColumnContentJson;
    }

//    /**
//     * 获取栏目更多内容
//     */
//    public MutableLiveData<String> getHomeColumnLoadMore(Map<String, String> mapParams) {
//        MutableLiveData<String> homeColumnLoadMoreJson = new MutableLiveData<>();
//        OkGo.<String>get(ApiConstants.getInstance().getContentDate())
//                .tag("ColumnLoadMore")
//                .params(mapParams)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        homeColumnLoadMoreJson.setValue(response.body());
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        homeColumnLoadMoreJson.setValue(null);
//                    }
//                });
//        return homeColumnLoadMoreJson;
//    }
}
