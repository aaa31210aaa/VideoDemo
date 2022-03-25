package com.wdcs.model;


import java.util.List;

public class UploadVideoModel {

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

    public String isSuccess() {
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
        private String id;
        private String type;
        private String source;
        private String onShelve;
        private String title;
        private String brief;
        private String keywords;
        private String classification;
        private String content;
        private String contentUrl;
        private String externalUrl;
        private String imagesUrl;
        private String playUrl;
        private String playDuration;
        private String orientation;
        private String liveStatus;
        private String liveStartTime;
        private String issuerId;
        private String listStyle;
        private String enableVoice;
        private String reporterName;
        private String issuerName;
        private String issuerImageUrl;
        private String startTime;
        private String endTime;
        private String disableComment;
        private String status;
        private String audit1Time;
        private String audit2Time;
        private String audit3Time;
        private String audit1User;
        private String audit2User;
        private String audit3User;
        private String appId;
        private String departmentId;
        private String orgId;
        private String version;
        private String createTime;
        private String updateTime;
        private String createBy;
        private String updateBy;
        private String deleted;
        private String thumbnailUrl;
        private String label;
        private String attachUrl;
        private String back1;
        private String back2;
        private String back3;
        private String rejectReason;
        private String datapool;
        private String zhcsNewsid;
        private String tags;
        private ExtendDTO extend;
        private String isExternal;
        private String thirdPartyId;
        private String hidden;
        private String hasSubset;
        private String recommendWay;
        private String subType;
        private String code;
        private String belongActivityId;
        private String belongTopicId;
        private String width;
        private String height;
        private String thirdPartyCode;
        private String crowdPackage;
        private List<PanelContentsDTO> panelContents;
        private String multiPictureItems;
        private String panelContentId;
        private String isTop;
        private String skipContentDetection;
        private String sort;
        private String recommendedContent;
        private String relatedContents;
        private String pid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getOnShelve() {
            return onShelve;
        }

        public void setOnShelve(String onShelve) {
            this.onShelve = onShelve;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getClassification() {
            return classification;
        }

        public void setClassification(String classification) {
            this.classification = classification;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getExternalUrl() {
            return externalUrl;
        }

        public void setExternalUrl(String externalUrl) {
            this.externalUrl = externalUrl;
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

        public String getOrientation() {
            return orientation;
        }

        public void setOrientation(String orientation) {
            this.orientation = orientation;
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

        public String getEnableVoice() {
            return enableVoice;
        }

        public void setEnableVoice(String enableVoice) {
            this.enableVoice = enableVoice;
        }

        public String getReporterName() {
            return reporterName;
        }

        public void setReporterName(String reporterName) {
            this.reporterName = reporterName;
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

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getDisableComment() {
            return disableComment;
        }

        public void setDisableComment(String disableComment) {
            this.disableComment = disableComment;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAudit1Time() {
            return audit1Time;
        }

        public void setAudit1Time(String audit1Time) {
            this.audit1Time = audit1Time;
        }

        public String getAudit2Time() {
            return audit2Time;
        }

        public void setAudit2Time(String audit2Time) {
            this.audit2Time = audit2Time;
        }

        public String getAudit3Time() {
            return audit3Time;
        }

        public void setAudit3Time(String audit3Time) {
            this.audit3Time = audit3Time;
        }

        public String getAudit1User() {
            return audit1User;
        }

        public void setAudit1User(String audit1User) {
            this.audit1User = audit1User;
        }

        public String getAudit2User() {
            return audit2User;
        }

        public void setAudit2User(String audit2User) {
            this.audit2User = audit2User;
        }

        public String getAudit3User() {
            return audit3User;
        }

        public void setAudit3User(String audit3User) {
            this.audit3User = audit3User;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getDeleted() {
            return deleted;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getAttachUrl() {
            return attachUrl;
        }

        public void setAttachUrl(String attachUrl) {
            this.attachUrl = attachUrl;
        }

        public String getBack1() {
            return back1;
        }

        public void setBack1(String back1) {
            this.back1 = back1;
        }

        public String getBack2() {
            return back2;
        }

        public void setBack2(String back2) {
            this.back2 = back2;
        }

        public String getBack3() {
            return back3;
        }

        public void setBack3(String back3) {
            this.back3 = back3;
        }

        public String getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }

        public String getDatapool() {
            return datapool;
        }

        public void setDatapool(String datapool) {
            this.datapool = datapool;
        }

        public String getZhcsNewsid() {
            return zhcsNewsid;
        }

        public void setZhcsNewsid(String zhcsNewsid) {
            this.zhcsNewsid = zhcsNewsid;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public ExtendDTO getExtend() {
            return extend;
        }

        public void setExtend(ExtendDTO extend) {
            this.extend = extend;
        }

        public String getIsExternal() {
            return isExternal;
        }

        public void setIsExternal(String isExternal) {
            this.isExternal = isExternal;
        }

        public String getThirdPartyId() {
            return thirdPartyId;
        }

        public void setThirdPartyId(String thirdPartyId) {
            this.thirdPartyId = thirdPartyId;
        }

        public String getHidden() {
            return hidden;
        }

        public void setHidden(String hidden) {
            this.hidden = hidden;
        }

        public String getHasSubset() {
            return hasSubset;
        }

        public void setHasSubset(String hasSubset) {
            this.hasSubset = hasSubset;
        }

        public String getRecommendWay() {
            return recommendWay;
        }

        public void setRecommendWay(String recommendWay) {
            this.recommendWay = recommendWay;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getBelongActivityId() {
            return belongActivityId;
        }

        public void setBelongActivityId(String belongActivityId) {
            this.belongActivityId = belongActivityId;
        }

        public String getBelongTopicId() {
            return belongTopicId;
        }

        public void setBelongTopicId(String belongTopicId) {
            this.belongTopicId = belongTopicId;
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

        public String getThirdPartyCode() {
            return thirdPartyCode;
        }

        public void setThirdPartyCode(String thirdPartyCode) {
            this.thirdPartyCode = thirdPartyCode;
        }

        public String getCrowdPackage() {
            return crowdPackage;
        }

        public void setCrowdPackage(String crowdPackage) {
            this.crowdPackage = crowdPackage;
        }

        public List<PanelContentsDTO> getPanelContents() {
            return panelContents;
        }

        public void setPanelContents(List<PanelContentsDTO> panelContents) {
            this.panelContents = panelContents;
        }

        public String getMultiPictureItems() {
            return multiPictureItems;
        }

        public void setMultiPictureItems(String multiPictureItems) {
            this.multiPictureItems = multiPictureItems;
        }

        public String getPanelContentId() {
            return panelContentId;
        }

        public void setPanelContentId(String panelContentId) {
            this.panelContentId = panelContentId;
        }

        public String getIsTop() {
            return isTop;
        }

        public void setIsTop(String isTop) {
            this.isTop = isTop;
        }

        public String getSkipContentDetection() {
            return skipContentDetection;
        }

        public void setSkipContentDetection(String skipContentDetection) {
            this.skipContentDetection = skipContentDetection;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getRecommendedContent() {
            return recommendedContent;
        }

        public void setRecommendedContent(String recommendedContent) {
            this.recommendedContent = recommendedContent;
        }

        public String getRelatedContents() {
            return relatedContents;
        }

        public void setRelatedContents(String relatedContents) {
            this.relatedContents = relatedContents;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public static class ExtendDTO {
        }

        public static class PanelContentsDTO {
            private String id;
            private String panelId;
            private String categoryId;
            private String contentId;
            private String startTime;
            private String endTime;
            private String sort;
            private String onShelve;
            private String categoryName;
            private String panelName;
            private String version;
            private String createTime;
            private String updateTime;
            private String createBy;
            private String updateBy;
            private String isTop;
            private long vernier;
            private String endTimeVernier;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPanelId() {
                return panelId;
            }

            public void setPanelId(String panelId) {
                this.panelId = panelId;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getContentId() {
                return contentId;
            }

            public void setContentId(String contentId) {
                this.contentId = contentId;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public String getOnShelve() {
                return onShelve;
            }

            public void setOnShelve(String onShelve) {
                this.onShelve = onShelve;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getPanelName() {
                return panelName;
            }

            public void setPanelName(String panelName) {
                this.panelName = panelName;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(String updateBy) {
                this.updateBy = updateBy;
            }

            public String getIsTop() {
                return isTop;
            }

            public void setIsTop(String isTop) {
                this.isTop = isTop;
            }

            public long getVernier() {
                return vernier;
            }

            public void setVernier(long vernier) {
                this.vernier = vernier;
            }

            public String getEndTimeVernier() {
                return endTimeVernier;
            }

            public void setEndTimeVernier(String endTimeVernier) {
                this.endTimeVernier = endTimeVernier;
            }
        }
    }
}
