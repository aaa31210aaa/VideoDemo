package model.bean;

import androidx.annotation.Keep;

@Keep
public class ActivityRuleBean {

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
    @Keep
    public static class DataDTO {
        private String id;
        private String categoryId;
        private String name;
        private String code;
        private String typeName;
        private String typeCode;
        private ConfigDTO config;
        private String limitCount;
        private String isCategoryPanel;
        private String contents;
        private String subCategories;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public ConfigDTO getConfig() {
            return config;
        }

        public void setConfig(ConfigDTO config) {
            this.config = config;
        }

        public String getLimitCount() {
            return limitCount;
        }

        public void setLimitCount(String limitCount) {
            this.limitCount = limitCount;
        }

        public String getIsCategoryPanel() {
            return isCategoryPanel;
        }

        public void setIsCategoryPanel(String isCategoryPanel) {
            this.isCategoryPanel = isCategoryPanel;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public String getSubCategories() {
            return subCategories;
        }

        public void setSubCategories(String subCategories) {
            this.subCategories = subCategories;
        }
        @Keep
        public static class ConfigDTO {
            private String code;
            private String imageUrl;
            private String name;
            private String typeName;
            private String jumpUrl;
            private String typeCode;
            private String backgroundImageUrl;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getJumpUrl() {
                return jumpUrl;
            }

            public void setJumpUrl(String jumpUrl) {
                this.jumpUrl = jumpUrl;
            }

            public String getTypeCode() {
                return typeCode;
            }

            public void setTypeCode(String typeCode) {
                this.typeCode = typeCode;
            }

            public String getBackgroundImageUrl() {
                return backgroundImageUrl;
            }

            public void setBackgroundImageUrl(String backgroundImageUrl) {
                this.backgroundImageUrl = backgroundImageUrl;
            }
        }
    }
}
