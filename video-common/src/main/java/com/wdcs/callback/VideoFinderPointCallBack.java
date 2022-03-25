package com.wdcs.callback;

import org.json.JSONObject;

/**
 * 传递finder埋点数据
 */
public interface VideoFinderPointCallBack {
    void getFinderPoint(String eventStr, JSONObject json);
}
