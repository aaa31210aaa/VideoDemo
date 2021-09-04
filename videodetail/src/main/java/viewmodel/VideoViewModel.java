package viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.wdcs.model.VideoCommonModel;
import com.wdcs.model.VideoCommonModel.DataDTO.RecordsDTO;


import java.io.File;
import java.util.List;

import base.BaseResponse;
import base.BaseViewModel;
import binding.command.BindingAction;
import binding.command.BindingCommand;
import bus.event.SingleLiveEvent;
import io.reactivex.functions.Consumer;
import model.MineModel;
import model.bean.TopicRequestBody;
import utils.RxUtils;

public class VideoViewModel extends BaseViewModel<MineModel> {

    public VideoViewModel(@NonNull Application application, MineModel model) {
        super(application, model);
    }

    /**
     * 话题标签
     */
    public SingleLiveEvent<List<RecordsDTO>> uploadTagFlow = new SingleLiveEvent<>();

//    /**
//     * 简介
//     */
//    public ObservableField<String> briefIntroduction = new ObservableField<>();

    /**
     * 获取话题
     */
    public void getTopicData() {
        TopicRequestBody topicRequestBody = new TopicRequestBody();
        topicRequestBody.setActivityCode("xksh");
        topicRequestBody.setType("activity.topic");
        model.getTopic(topicRequestBody)
                .compose(RxUtils.schedulersTransformer())
                .doOnSubscribe(VideoViewModel.this)
                .subscribe(new Consumer<BaseResponse<VideoCommonModel.DataDTO>>() {
                    @Override
                    public void accept(BaseResponse<VideoCommonModel.DataDTO> dataDTOBaseResponse) throws Exception {
//                        tagDatas.add(dataDTOBaseResponse.getResult());
                        uploadTagFlow.setValue(dataDTOBaseResponse.getResult().getRecords());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("getTopic", throwable.getMessage());
                    }
                });
    }

    /**
     * 上传视频
     */
    public void uploadVideo(List<File> files, String isPublic, String generateCoverImage) {
        model.uploadFile(files, isPublic, generateCoverImage)
                .compose(RxUtils.schedulersTransformer())
                .doOnSubscribe(VideoViewModel.this)
                .subscribe(new Consumer<BaseResponse<String>>() {
                    @Override
                    public void accept(BaseResponse<String> videoBeanBaseResponse) throws Exception {
//                        Log.e("uploadVideo",videoBeanBaseResponse.getMessage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


//        model.uploadFile(files, isPublic, generateCoverImage)
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//
//                    }
//                });


    }




}
