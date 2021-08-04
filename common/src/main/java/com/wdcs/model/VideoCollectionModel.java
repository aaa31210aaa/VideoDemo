package com.wdcs.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class VideoCollectionModel {


    private String code;
    private Boolean success;
    private String message;
    private String detail;
    private Datas data;
    private String time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
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

    public Datas getDatas() {
        return data;
    }

    public void setData(Datas data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @Keep
    public static class Datas {
        private String shareTitle;
        private String shareUrl;
        private String shareImageUrl;
        private String shareBrief;
        private String timeDif;
        private String issueTimeStamp;
        private String startTime;
        private String id;
        private String commentCountShow;
        private String likeCountShow;
        private String favorCountShow;
        private String viewCountShow;
        private String playCountShow;
        private String type;
        private String title;
        private String thumbnailUrl;
        private String brief;
        private String detailUrl;
        private String externalUrl;
        private String source;
        private String keywords;
        private String tags;
        private String classification;
        private String imagesUrl;
        private String playUrl;
        private String playDuration;
        private String liveStatus;
        private String liveStartTime;
        private String issuerId;
        private String listStyle;
        private String issuerName;
        private String issuerImageUrl;
        private Boolean disableComment;
        private String label;
        private String orientation;
        private Boolean whetherLike;
        private Boolean whetherFavor;
        private String pId;
        private List<DataDTO> children;
        private String classList;
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

        public String getPlayCountShow() {
            return playCountShow;
        }

        public void setPlayCountShow(String playCountShow) {
            this.playCountShow = playCountShow;
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

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getPlayDuration() {
            return playDuration;
        }

        public void setPlayDuration(String playDuration) {
            this.playDuration = playDuration;
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

        public Boolean getDisableComment() {
            return disableComment;
        }

        public void setDisableComment(Boolean disableComment) {
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

        public Boolean getWhetherLike() {
            return whetherLike;
        }

        public void setWhetherLike(Boolean whetherLike) {
            this.whetherLike = whetherLike;
        }

        public Boolean getWhetherFavor() {
            return whetherFavor;
        }

        public void setWhetherFavor(Boolean whetherFavor) {
            this.whetherFavor = whetherFavor;
        }

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

//        public List<ChildrenDTO> getChildren() {
//            return children;
//        }
//
//        public void setChildren(List<ChildrenDTO> children) {
//            this.children = children;
//        }

        public String getClassList() {
            return classList;
        }

        public void setClassList(String classList) {
            this.classList = classList;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public List<DataDTO> getChildren() {
            return children;
        }

        public void setChildren(List<DataDTO> children) {
            this.children = children;
        }
        //        public static class ChildrenDTO {
//            private String shareTitle;
//            private String shareUrl;
//            private String shareImageUrl;
//            private String shareBrief;
//            private String timeDif;
//            private String issueTimeStamp;
//            private String startTime;
//            private String id;
//            private String commentCountShow;
//            private String likeCountShow;
//            private String favorCountShow;
//            private String viewCountShow;
//            private String playCountShow;
//            private String type;
//            private String title;
//            private String thumbnailUrl;
//            private String brief;
//            private String detailUrl;
//            private String externalUrl;
//            private String source;
//            private String keywords;
//            private String tags;
//            private String classification;
//            private String imagesUrl;
//            private String playUrl;
//            private String playDuration;
//            private String liveStatus;
//            private String liveStartTime;
//            private String issuerId;
//            private String listStyle;
//            private String issuerName;
//            private String issuerImageUrl;
//            private Boolean disableComment;
//            private String label;
//            private String orientation;
//            private Boolean whetherLike;
//            private Boolean whetherFavor;
//            private String pId;
//            private String pid;
//
//            public String getShareTitle() {
//                return shareTitle;
//            }
//
//            public void setShareTitle(String shareTitle) {
//                this.shareTitle = shareTitle;
//            }
//
//            public String getShareUrl() {
//                return shareUrl;
//            }
//
//            public void setShareUrl(String shareUrl) {
//                this.shareUrl = shareUrl;
//            }
//
//            public String getShareImageUrl() {
//                return shareImageUrl;
//            }
//
//            public void setShareImageUrl(String shareImageUrl) {
//                this.shareImageUrl = shareImageUrl;
//            }
//
//            public String getShareBrief() {
//                return shareBrief;
//            }
//
//            public void setShareBrief(String shareBrief) {
//                this.shareBrief = shareBrief;
//            }
//
//            public String getTimeDif() {
//                return timeDif;
//            }
//
//            public void setTimeDif(String timeDif) {
//                this.timeDif = timeDif;
//            }
//
//            public String getIssueTimeStamp() {
//                return issueTimeStamp;
//            }
//
//            public void setIssueTimeStamp(String issueTimeStamp) {
//                this.issueTimeStamp = issueTimeStamp;
//            }
//
//            public String getStartTime() {
//                return startTime;
//            }
//
//            public void setStartTime(String startTime) {
//                this.startTime = startTime;
//            }
//
//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//
//            public String getCommentCountShow() {
//                return commentCountShow;
//            }
//
//            public void setCommentCountShow(String commentCountShow) {
//                this.commentCountShow = commentCountShow;
//            }
//
//            public String getLikeCountShow() {
//                return likeCountShow;
//            }
//
//            public void setLikeCountShow(String likeCountShow) {
//                this.likeCountShow = likeCountShow;
//            }
//
//            public String getFavorCountShow() {
//                return favorCountShow;
//            }
//
//            public void setFavorCountShow(String favorCountShow) {
//                this.favorCountShow = favorCountShow;
//            }
//
//            public String getViewCountShow() {
//                return viewCountShow;
//            }
//
//            public void setViewCountShow(String viewCountShow) {
//                this.viewCountShow = viewCountShow;
//            }
//
//            public String getPlayCountShow() {
//                return playCountShow;
//            }
//
//            public void setPlayCountShow(String playCountShow) {
//                this.playCountShow = playCountShow;
//            }
//
//            public String getType() {
//                return type;
//            }
//
//            public void setType(String type) {
//                this.type = type;
//            }
//
//            public String getTitle() {
//                return title;
//            }
//
//            public void setTitle(String title) {
//                this.title = title;
//            }
//
//            public String getThumbnailUrl() {
//                return thumbnailUrl;
//            }
//
//            public void setThumbnailUrl(String thumbnailUrl) {
//                this.thumbnailUrl = thumbnailUrl;
//            }
//
//            public String getBrief() {
//                return brief;
//            }
//
//            public void setBrief(String brief) {
//                this.brief = brief;
//            }
//
//            public String getDetailUrl() {
//                return detailUrl;
//            }
//
//            public void setDetailUrl(String detailUrl) {
//                this.detailUrl = detailUrl;
//            }
//
//            public String getExternalUrl() {
//                return externalUrl;
//            }
//
//            public void setExternalUrl(String externalUrl) {
//                this.externalUrl = externalUrl;
//            }
//
//            public String getSource() {
//                return source;
//            }
//
//            public void setSource(String source) {
//                this.source = source;
//            }
//
//            public String getKeywords() {
//                return keywords;
//            }
//
//            public void setKeywords(String keywords) {
//                this.keywords = keywords;
//            }
//
//            public String getTags() {
//                return tags;
//            }
//
//            public void setTags(String tags) {
//                this.tags = tags;
//            }
//
//            public String getClassification() {
//                return classification;
//            }
//
//            public void setClassification(String classification) {
//                this.classification = classification;
//            }
//
//            public String getImagesUrl() {
//                return imagesUrl;
//            }
//
//            public void setImagesUrl(String imagesUrl) {
//                this.imagesUrl = imagesUrl;
//            }
//
//            public String getPlayUrl() {
//                return playUrl;
//            }
//
//            public void setPlayUrl(String playUrl) {
//                this.playUrl = playUrl;
//            }
//
//            public String getPlayDuration() {
//                return playDuration;
//            }
//
//            public void setPlayDuration(String playDuration) {
//                this.playDuration = playDuration;
//            }
//
//            public String getLiveStatus() {
//                return liveStatus;
//            }
//
//            public void setLiveStatus(String liveStatus) {
//                this.liveStatus = liveStatus;
//            }
//
//            public String getLiveStartTime() {
//                return liveStartTime;
//            }
//
//            public void setLiveStartTime(String liveStartTime) {
//                this.liveStartTime = liveStartTime;
//            }
//
//            public String getIssuerId() {
//                return issuerId;
//            }
//
//            public void setIssuerId(String issuerId) {
//                this.issuerId = issuerId;
//            }
//
//            public String getListStyle() {
//                return listStyle;
//            }
//
//            public void setListStyle(String listStyle) {
//                this.listStyle = listStyle;
//            }
//
//            public String getIssuerName() {
//                return issuerName;
//            }
//
//            public void setIssuerName(String issuerName) {
//                this.issuerName = issuerName;
//            }
//
//            public String getIssuerImageUrl() {
//                return issuerImageUrl;
//            }
//
//            public void setIssuerImageUrl(String issuerImageUrl) {
//                this.issuerImageUrl = issuerImageUrl;
//            }
//
//            public Boolean getDisableComment() {
//                return disableComment;
//            }
//
//            public void setDisableComment(Boolean disableComment) {
//                this.disableComment = disableComment;
//            }
//
//            public String getLabel() {
//                return label;
//            }
//
//            public void setLabel(String label) {
//                this.label = label;
//            }
//
//            public String getOrientation() {
//                return orientation;
//            }
//
//            public void setOrientation(String orientation) {
//                this.orientation = orientation;
//            }
//
//            public Boolean getWhetherLike() {
//                return whetherLike;
//            }
//
//            public void setWhetherLike(Boolean whetherLike) {
//                this.whetherLike = whetherLike;
//            }
//
//            public Boolean getWhetherFavor() {
//                return whetherFavor;
//            }
//
//            public void setWhetherFavor(Boolean whetherFavor) {
//                this.whetherFavor = whetherFavor;
//            }
//
//            public String getPId() {
//                return pId;
//            }
//
//            public void setPId(String pId) {
//                this.pId = pId;
//            }
//
//            public String getPid() {
//                return pid;
//            }
//
//            public void setPid(String pid) {
//                this.pid = pid;
//            }
//
//        }
    }
}
