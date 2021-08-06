package com.wdcs.utils;

import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.model.DataDTO;
import com.wdcs.model.ShareInfo;

public class ShareUtils {
    public static void toShare(DataDTO item, String platform) {
        VideoInteractiveParam param = VideoInteractiveParam.getInstance();
        ShareInfo shareInfo = ShareInfo.getInstance(item.getShareUrl(), item.getShareImageUrl(),
                item.getShareBrief(), item.getShareTitle(), platform);
        try {
            param.shared(shareInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
