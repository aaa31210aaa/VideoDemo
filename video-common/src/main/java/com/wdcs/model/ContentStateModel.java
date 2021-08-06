package com.wdcs.model;

import androidx.annotation.Keep;

@Keep
public class ContentStateModel {

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

    public Object getDetail() {
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

    @Keep
    public static class DataDTO {
        private String id;
        private String commentCountShow;
        private String likeCountShow;
        private String favorCountShow;
        private String viewCountShow;
        private String playCountShow;
        private String contentId;
        private String whetherLike;
        private String whetherFavor;

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

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
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
    }
}
