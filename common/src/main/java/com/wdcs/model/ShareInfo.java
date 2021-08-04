package com.wdcs.model;

/**
 * 分享四要素
 */
public class ShareInfo {
    public String shareH5, shareImageUrl, shareBrief, shareTitle, platform;

    public static ShareInfo getInstance(String shareH5, String shareImageUrl, String shareBrief, String shareTitle, String platform) {
        return new ShareInfo(shareH5, shareImageUrl, shareBrief, shareTitle, platform);
    }

    public ShareInfo(String shareH5, String shareImageUrl, String shareBrief, String shareTitle, String platform) {
        this.shareH5 = shareH5;
        this.shareImageUrl = shareImageUrl;
        this.shareBrief = shareBrief;
        this.shareTitle = shareTitle;
        this.platform = platform;
    }
}
