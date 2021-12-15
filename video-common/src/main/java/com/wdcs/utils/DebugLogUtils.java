package com.wdcs.utils;

import static com.wdcs.utils.Utils.mIsDebug;

import android.text.TextUtils;

public class DebugLogUtils {
    public static void DebugLog(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (mIsDebug) {
            ToastUtils.showShort(str);
        }
    }

    public static void DebugLog(Integer str) {
        if (null == str) {
            return;
        }
        if (mIsDebug) {
            ToastUtils.showShort(str);
        }
    }
}
