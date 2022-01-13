package com.wdcs.utils;

import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.model.DataDTO;
import com.wdcs.model.VideoCollectionModel.DataDTO.RecordsDTO;
import com.wdcs.model.ShareInfo;
import com.wdcs.model.VideoCollectionModel;

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

    public static void toShare(RecordsDTO item, String platform) {
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
