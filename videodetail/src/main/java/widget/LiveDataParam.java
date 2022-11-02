package widget;

import androidx.lifecycle.MutableLiveData;

import com.wdcs.callback.VideoInteractiveParam;


public class LiveDataParam {
    public static LiveDataParam param;
    public MutableLiveData<Integer> homeTabIndex = new MutableLiveData<>();

    private LiveDataParam() {
    }

    public static LiveDataParam getInstance() {
        if (param == null) {
            synchronized (VideoInteractiveParam.class) {
                if (param == null) {
                    param = new LiveDataParam();
                }
            }
        }
        return param;
    }

    public void setHomeTabIndex(Integer index){
        homeTabIndex.setValue(index);
    }
}
