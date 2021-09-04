package model.bean;

import androidx.annotation.Keep;

@Keep
public class TopicRequestBody {
    String activityCode;
    String type;

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
