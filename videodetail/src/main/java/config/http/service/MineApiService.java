package config.http.service;



import com.wdcs.model.VideoCommonModel.DataDTO;


import java.util.List;

import base.BaseResponse;
import io.reactivex.Observable;
import model.bean.TopicRequestBody;
import model.bean.VideoBean;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MineApiService {
    //获取话题
    @POST("api/cms/client/content/page")
    Observable<BaseResponse<DataDTO>> getTopicData(@Header("authorization") String head, @Body TopicRequestBody body);

    /**
     * 上传视频
     * @param files
     * @return
     */
    @Multipart
    @POST("api/media/file/upload")
    Observable<BaseResponse<String>> uploadFile(@Part List<MultipartBody.Part> files, @Query("isPublic") String isPublic, @Query("generateCoverImage") String generateCoverImage);
}
