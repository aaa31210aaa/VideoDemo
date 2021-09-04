package com.wdcs.model;

import androidx.annotation.Keep;

@Keep
public class TokenModel {


    private Integer code;
    private Boolean success;
    private String message;
    private String detail;
    private DataDTO data;
    private String time;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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
        private String token;
        private LoginSysUserVoDTO loginSysUserVo;
        private String gdyToken;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public LoginSysUserVoDTO getLoginSysUserVo() {
            return loginSysUserVo;
        }

        public void setLoginSysUserVo(LoginSysUserVoDTO loginSysUserVo) {
            this.loginSysUserVo = loginSysUserVo;
        }

        public String getGdyToken() {
            return gdyToken;
        }

        public void setGdyToken(String gdyToken) {
            this.gdyToken = gdyToken;
        }

        @Keep
        public static class LoginSysUserVoDTO {
            private String id;
            private String username;
            private String nickname;
            private String head;
            private String gender;
            private String state;
            private String permissionCodes;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getPermissionCodes() {
                return permissionCodes;
            }

            public void setPermissionCodes(String permissionCodes) {
                this.permissionCodes = permissionCodes;
            }
        }
    }
}
