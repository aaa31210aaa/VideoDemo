package com.wdcs.model;

import java.util.List;

public class VideoCommonModel {

    private String code;
    private String success;
    private String message;
    private String detail;
    private DataDTO data;
    private String time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class DataDTO {
        private String total;
        private List<RecordsDTO> records;
        private String pageIndex;
        private String pageSize;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<RecordsDTO> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsDTO> records) {
            this.records = records;
        }

        public String getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(String pageIndex) {
            this.pageIndex = pageIndex;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public static class RecordsDTO {
            private String shareTitle;
            private String shareUrl;
            private String shareImageUrl;
            private String shareBrief;
            private String timeDif;
            private String issueTimeStamp;
            private String startTime;
            private String id;
            private String readCount;
            private String commentCountShow;
            private String likeCountShow;
            private String favorCountShow;
            private String viewCountShow;
            private String type;
            private String title;
            private String thumbnailUrl;
            private String brief;
            private String detailUrl;
            private String externalUrl;
            private String isExternal;
            private String playUrl;
            private String source;
            private String keywords;
            private String tags;
            private String classification;
            private String imagesUrl;
            private String playDuration;
            private String status;
            private String liveStatus;
            private String liveStartTime;
            private String issuerId;
            private String listStyle;
            private String issuerName;
            private String issuerImageUrl;
            private String disableComment;
            private String label;
            private String orientation;
            private String whetherLike;
            private String whetherFavor;
            private String whetherFollow;
            private String pId;
            private String isTop;
            private String leftTag;
            private ExtendDTO extend;
            private String vernier;
            private String advert;
            private String newsId;
            private String belongActivityId;
            private String belongActivityName;
            private String belongTopicId;
            private String belongTopicName;
            private String width;
            private String height;
            private String creatorUsername;
            private String creatorNickname;
            private String creatorHead;
            private String creatorGender;
            private String creatorCertMark;
            private String creatorCertDomain;
            private String rejectReason;
            private String thirdPartyId;
            private String createBy;
            private String thirdPartyCode;
            private String pid;

            public String getShareTitle() {
                return shareTitle;
            }

            public void setShareTitle(String shareTitle) {
                this.shareTitle = shareTitle;
            }

            public String getShareUrl() {
                return shareUrl;
            }

            public void setShareUrl(String shareUrl) {
                this.shareUrl = shareUrl;
            }

            public String getShareImageUrl() {
                return shareImageUrl;
            }

            public void setShareImageUrl(String shareImageUrl) {
                this.shareImageUrl = shareImageUrl;
            }

            public String getShareBrief() {
                return shareBrief;
            }

            public void setShareBrief(String shareBrief) {
                this.shareBrief = shareBrief;
            }

            public String getTimeDif() {
                return timeDif;
            }

            public void setTimeDif(String timeDif) {
                this.timeDif = timeDif;
            }

            public String getIssueTimeStamp() {
                return issueTimeStamp;
            }

            public void setIssueTimeStamp(String issueTimeStamp) {
                this.issueTimeStamp = issueTimeStamp;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getReadCount() {
                return readCount;
            }

            public void setReadCount(String readCount) {
                this.readCount = readCount;
            }

            public String getCommentCountShow() {
                return commentCountShow;
            }

            public void setCommentCountShow(String commentCountShow) {
                this.commentCountShow = commentCountShow;
            }

            public String getLikeCountShow() {
                return likeCountShow;
            }

            public void setLikeCountShow(String likeCountShow) {
                this.likeCountShow = likeCountShow;
            }

            public String getFavorCountShow() {
                return favorCountShow;
            }

            public void setFavorCountShow(String favorCountShow) {
                this.favorCountShow = favorCountShow;
            }

            public String getViewCountShow() {
                return viewCountShow;
            }

            public void setViewCountShow(String viewCountShow) {
                this.viewCountShow = viewCountShow;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getThumbnailUrl() {
                return thumbnailUrl;
            }

            public void setThumbnailUrl(String thumbnailUrl) {
                this.thumbnailUrl = thumbnailUrl;
            }

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getDetailUrl() {
                return detailUrl;
            }

            public void setDetailUrl(String detailUrl) {
                this.detailUrl = detailUrl;
            }

            public String getExternalUrl() {
                return externalUrl;
            }

            public void setExternalUrl(String externalUrl) {
                this.externalUrl = externalUrl;
            }

            public String getIsExternal() {
                return isExternal;
            }

            public void setIsExternal(String isExternal) {
                this.isExternal = isExternal;
            }

            public String getPlayUrl() {
                return playUrl;
            }

            public void setPlayUrl(String playUrl) {
                this.playUrl = playUrl;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            public String getClassification() {
                return classification;
            }

            public void setClassification(String classification) {
                this.classification = classification;
            }

            public String getImagesUrl() {
                return imagesUrl;
            }

            public void setImagesUrl(String imagesUrl) {
                this.imagesUrl = imagesUrl;
            }

            public String getPlayDuration() {
                return playDuration;
            }

            public void setPlayDuration(String playDuration) {
                this.playDuration = playDuration;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getLiveStatus() {
                return liveStatus;
            }

            public void setLiveStatus(String liveStatus) {
                this.liveStatus = liveStatus;
            }

            public String getLiveStartTime() {
                return liveStartTime;
            }

            public void setLiveStartTime(String liveStartTime) {
                this.liveStartTime = liveStartTime;
            }

            public String getIssuerId() {
                return issuerId;
            }

            public void setIssuerId(String issuerId) {
                this.issuerId = issuerId;
            }

            public String getListStyle() {
                return listStyle;
            }

            public void setListStyle(String listStyle) {
                this.listStyle = listStyle;
            }

            public String getIssuerName() {
                return issuerName;
            }

            public void setIssuerName(String issuerName) {
                this.issuerName = issuerName;
            }

            public String getIssuerImageUrl() {
                return issuerImageUrl;
            }

            public void setIssuerImageUrl(String issuerImageUrl) {
                this.issuerImageUrl = issuerImageUrl;
            }

            public String getDisableComment() {
                return disableComment;
            }

            public void setDisableComment(String disableComment) {
                this.disableComment = disableComment;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getOrientation() {
                return orientation;
            }

            public void setOrientation(String orientation) {
                this.orientation = orientation;
            }

            public String getWhetherLike() {
                return whetherLike;
            }

            public void setWhetherLike(String whetherLike) {
                this.whetherLike = whetherLike;
            }

            public String getWhetherFavor() {
                return whetherFavor;
            }

            public void setWhetherFavor(String whetherFavor) {
                this.whetherFavor = whetherFavor;
            }

            public String getWhetherFollow() {
                return whetherFollow;
            }

            public void setWhetherFollow(String whetherFollow) {
                this.whetherFollow = whetherFollow;
            }

            public String getPId() {
                return pId;
            }

            public void setPId(String pId) {
                this.pId = pId;
            }

            public String getIsTop() {
                return isTop;
            }

            public void setIsTop(String isTop) {
                this.isTop = isTop;
            }

            public String getLeftTag() {
                return leftTag;
            }

            public void setLeftTag(String leftTag) {
                this.leftTag = leftTag;
            }

            public ExtendDTO getExtend() {
                return extend;
            }

            public void setExtend(ExtendDTO extend) {
                this.extend = extend;
            }

            public Object getVernier() {
                return vernier;
            }

            public void setVernier(String vernier) {
                this.vernier = vernier;
            }

            public String getAdvert() {
                return advert;
            }

            public void setAdvert(String advert) {
                this.advert = advert;
            }

            public String getNewsId() {
                return newsId;
            }

            public void setNewsId(String newsId) {
                this.newsId = newsId;
            }

            public String getBelongActivityId() {
                return belongActivityId;
            }

            public void setBelongActivityId(String belongActivityId) {
                this.belongActivityId = belongActivityId;
            }

            public String getBelongActivityName() {
                return belongActivityName;
            }

            public void setBelongActivityName(String belongActivityName) {
                this.belongActivityName = belongActivityName;
            }

            public String getBelongTopicId() {
                return belongTopicId;
            }

            public void setBelongTopicId(String belongTopicId) {
                this.belongTopicId = belongTopicId;
            }

            public String getBelongTopicName() {
                return belongTopicName;
            }

            public void setBelongTopicName(String belongTopicName) {
                this.belongTopicName = belongTopicName;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }

            public String getCreatorUsername() {
                return creatorUsername;
            }

            public void setCreatorUsername(String creatorUsername) {
                this.creatorUsername = creatorUsername;
            }

            public String getCreatorNickname() {
                return creatorNickname;
            }

            public void setCreatorNickname(String creatorNickname) {
                this.creatorNickname = creatorNickname;
            }

            public String getCreatorHead() {
                return creatorHead;
            }

            public void setCreatorHead(String creatorHead) {
                this.creatorHead = creatorHead;
            }

            public String getCreatorGender() {
                return creatorGender;
            }

            public void setCreatorGender(String creatorGender) {
                this.creatorGender = creatorGender;
            }

            public Object getCreatorCertMark() {
                return creatorCertMark;
            }

            public void setCreatorCertMark(String creatorCertMark) {
                this.creatorCertMark = creatorCertMark;
            }

            public String getCreatorCertDomain() {
                return creatorCertDomain;
            }

            public void setCreatorCertDomain(String creatorCertDomain) {
                this.creatorCertDomain = creatorCertDomain;
            }

            public String getRejectReason() {
                return rejectReason;
            }

            public void setRejectReason(String rejectReason) {
                this.rejectReason = rejectReason;
            }

            public String getThirdPartyId() {
                return thirdPartyId;
            }

            public void setThirdPartyId(String thirdPartyId) {
                this.thirdPartyId = thirdPartyId;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getThirdPartyCode() {
                return thirdPartyCode;
            }

            public void setThirdPartyCode(String thirdPartyCode) {
                this.thirdPartyCode = thirdPartyCode;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public static class ExtendDTO {
            }
        }
    }
}
