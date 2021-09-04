package model;

import androidx.annotation.NonNull;


import com.wdcs.model.VideoCommonModel.DataDTO;

import java.io.File;
import java.util.List;

import base.BaseModel;
import base.BaseResponse;
import config.datasource.MineHttpDataSource;
import config.datasource.MineLocalDataSource;
import io.reactivex.Observable;
import model.bean.TopicRequestBody;
import model.bean.VideoBean;
import okhttp3.MultipartBody;

/**
 * MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据
 * Created by tanggongwen on 2019/9/17.
 */
public class MineModel extends BaseModel implements MineHttpDataSource, MineLocalDataSource {
    private volatile static MineModel INSTANCE = null;
    private final MineHttpDataSource mHttpDataSource;

    private final MineLocalDataSource mLocalDataSource;

    private MineModel(@NonNull MineHttpDataSource httpDataSource,
                      @NonNull MineLocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static MineModel getInstance(MineHttpDataSource httpDataSource,
                                                                       MineLocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (MineModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MineModel(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Observable<BaseResponse<DataDTO>> getTopic(TopicRequestBody body) {
        return mHttpDataSource.getTopic(body);
    }

    @Override
    public Observable<BaseResponse<String>> uploadFile(List<File> files,String isPublic, String generateCoverImage) {
        return mHttpDataSource.uploadFile(files,isPublic,generateCoverImage);
    }


}
