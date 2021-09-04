package config.datasource;


import com.wdcs.model.VideoCommonModel.DataDTO;


import java.io.File;
import java.util.List;

import base.BaseResponse;
import io.reactivex.Observable;
import model.bean.TopicRequestBody;
import model.bean.VideoBean;

public interface MineHttpDataSource {
    Observable<BaseResponse<DataDTO>> getTopic(TopicRequestBody body);

    Observable<BaseResponse<String>> uploadFile(List<File> files, String isPublic, String generateCoverImage);

}
