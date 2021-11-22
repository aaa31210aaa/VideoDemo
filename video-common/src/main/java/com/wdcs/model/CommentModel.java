package com.wdcs.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class CommentModel {

    private Integer code;
    private DataDTO data;
    private String detail;
    private String message;
    private Boolean success;
    private String time;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Keep
    public static class DataDTO {
        private Integer pageIndex;
        private Integer pageSize;
        private List<RecordsDTO> records;
        private Integer total;

        public Integer getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(Integer pageIndex) {
            this.pageIndex = pageIndex;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public List<RecordsDTO> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsDTO> records) {
            this.records = records;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        @Keep
        public static class RecordsDTO {
            private List<ChildrenDTO> children;
            private String content;
            private Integer contentId;
            private Integer createBy;
            private String createTime;
            private String editor;
            private String head;
            private Integer id;
            private String nickname;
            private Integer pcommentId;
            private Integer rcommentId;
            private String title;
            private Integer userId;
            private String isTop;
            private String onShelve;

            public List<ChildrenDTO> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenDTO> children) {
                this.children = children;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public Integer getContentId() {
                return contentId;
            }

            public void setContentId(Integer contentId) {
                this.contentId = contentId;
            }

            public Integer getCreateBy() {
                return createBy;
            }

            public void setCreateBy(Integer createBy) {
                this.createBy = createBy;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getEditor() {
                return editor;
            }

            public void setEditor(String editor) {
                this.editor = editor;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public Integer getPcommentId() {
                return pcommentId;
            }

            public void setPcommentId(Integer pcommentId) {
                this.pcommentId = pcommentId;
            }

            public Integer getRcommentId() {
                return rcommentId;
            }

            public void setRcommentId(Integer rcommentId) {
                this.rcommentId = rcommentId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Integer getUserId() {
                return userId;
            }

            public void setUserId(Integer userId) {
                this.userId = userId;
            }

            public String getIsTop() {
                return isTop;
            }

            public void setIsTop(String isTop) {
                this.isTop = isTop;
            }

            public String getOnShelve() {
                return onShelve;
            }

            public void setOnShelve(String onShelve) {
                this.onShelve = onShelve;
            }

            @Keep
            public static class ChildrenDTO {
                private String content;
                private Integer contentId;
                private Integer createBy;
                private String createTime;
                private String editor;
                private String head;
                private Integer id;
                private String nickname;
                private Integer pcommentId;
                private Integer rcommentId;
                private String title;
                private Integer userId;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public Integer getContentId() {
                    return contentId;
                }

                public void setContentId(Integer contentId) {
                    this.contentId = contentId;
                }

                public Integer getCreateBy() {
                    return createBy;
                }

                public void setCreateBy(Integer createBy) {
                    this.createBy = createBy;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public String getEditor() {
                    return editor;
                }

                public void setEditor(String editor) {
                    this.editor = editor;
                }

                public String getHead() {
                    return head;
                }

                public void setHead(String head) {
                    this.head = head;
                }

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public Integer getPcommentId() {
                    return pcommentId;
                }

                public void setPcommentId(Integer pcommentId) {
                    this.pcommentId = pcommentId;
                }

                public Integer getRcommentId() {
                    return rcommentId;
                }

                public void setRcommentId(Integer rcommentId) {
                    this.rcommentId = rcommentId;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public Integer getUserId() {
                    return userId;
                }

                public void setUserId(Integer userId) {
                    this.userId = userId;
                }
            }
        }
    }
}
