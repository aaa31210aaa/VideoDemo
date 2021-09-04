package config.http;


import com.wdcs.model.VideoCommonModel.DataDTO;
import com.wdcs.utils.PersonInfoManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import base.BaseResponse;
import config.datasource.MineHttpDataSource;
import config.http.service.MineApiService;
import io.reactivex.Observable;
import model.bean.TopicRequestBody;
import model.bean.VideoBean;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by tanggongwen on 2019/9/17.
 */
public class MineHttpDataSourceImpl implements MineHttpDataSource {
    private MineApiService apiService;
    private volatile static MineHttpDataSourceImpl INSTANCE = null;

    public static MineHttpDataSourceImpl getInstance(MineApiService apiService) {
        if (INSTANCE == null) {
            synchronized (MineHttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MineHttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private MineHttpDataSourceImpl(MineApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<BaseResponse<DataDTO>> getTopic(TopicRequestBody body) {
        return apiService.getTopicData(PersonInfoManager.getInstance().getTransformationToken(),body);
    }


    @Override
    public Observable<BaseResponse<String>> uploadFile(List<File> files, String isPublic, String generateCoverImage) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("video/mp4"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            partList.add(body);
        }
        return apiService.uploadFile(partList,isPublic ,generateCoverImage);
    }
}
