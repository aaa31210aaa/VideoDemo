package ui.activity;


import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.PORTRAIT_STR;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wdcs.callback.JsonCallback;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.manager.FinderBuriedPointManager;
import com.wdcs.model.DataDTO;
import com.wdcs.model.FinderPointModel;
import com.wdcs.model.ReadArticleModel;
import com.wdcs.model.TokenModel;
import com.wdcs.model.TopicModel;
import com.wdcs.model.UploadVideoModel;
import com.wdcs.model.VideoDetailModel;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.DataCleanUtils;
import com.wdcs.utils.FileUtils;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.ScreenUtils;
import com.wdcs.utils.ToastUtils;
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.IncapableCause;
import com.zhihu.matisse.internal.entity.Item;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.functions.Consumer;
import model.bean.UploadVideoBean;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "UploadActivity";
    /**
     * 话题标签
     */
    private TagFlowLayout tagFlow;
    /**
     * 上传进度条
     */
    private ProgressBar uploadProgress;

    /**
     * 选择的话题
     */
    private String selectTopicStr;

    private CustomPopWindow uploadVideoWindow;
    private View chooseVideoView;
    private static final String[] permissionsGroup = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_CHOOSE = 0x4001;
    private List<String> imageUrlList = new ArrayList<>();
    private List<File> fileList = new ArrayList<>();
    public String topicSelectId;
    private ImageView uploadBtn;
    private UploadVideoBean uploadVideoBean;
    private UploadVideoBean uploadCoverBean;
    private ImageView uploadVideoCancel;
    private TextView uploadCancelTv;
    private TextView releaseBtn;
    private boolean isSelected;
    private EditText briefIntroduction;
    private List<TopicModel.DataDTO> uploadTagFlow;
    private float worksDuration; //视频的时长
    private long worksSize; //视频大小
    private TextView uploadChooseCover;
    private boolean isUploadVideo; //当前是否上传了视频
    private File videoFile; //当前的视频文件
    private TextView uploadAgreementCancel;
    private TextView uploadAgreementSubmit;
    private boolean isFirst = true;
    private String draftId; //草稿id
    private TextView uploadArticle; //提交草稿
    private String orientation;
    private ReadArticleModel.DataDTO articleData;
    private String uploadTypes = "activity.works";
    private TagAdapter tagAdapter;
    private Toast toast;
    private View toastLayout;
    private TimerTask task;
    private WebView webView;
    private RelativeLayout uploadAgreementRl;
    private TextView showToastUploadText;
    private boolean canSaveDraft = true;
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    private CustomPopWindow showToastPop;
    private View showToastPopView;
    private TextView uploadText;
    private String module_source;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ScreenUtils.fullScreen(this, true);
        ScreenUtils.setStatusBarColor(this, R.color.white);
        initView();
    }

    private void initView() {
        draftId = getIntent().getStringExtra("draftId");
        module_source = getIntent().getStringExtra("module_source");
        if (TextUtils.isEmpty(draftId)) {
            draftId = "";
        }
        uploadAgreementRl = findViewById(R.id.upload_agreement_rl);
        uploadAgreementCancel = findViewById(R.id.upload_agreement_cancel);
        uploadAgreementSubmit = findViewById(R.id.upload_agreement_submit);

        tagFlow = findViewById(R.id.uploadTagFlow);
        uploadTagFlow = new ArrayList<>();
        uploadProgress = findViewById(R.id.upload_progress);
        uploadBtn = findViewById(R.id.upload_btn);
        uploadBtn.setOnClickListener(this);
        noLoginTipsView = View.inflate(this, R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);
        uploadChooseCover = findViewById(R.id.upload_choose_cover);
        chooseVideoView = View.inflate(this, R.layout.mine_layout_imagetype, null);
        chooseVideoView.findViewById(R.id.tvChooseImage).setOnClickListener(this);
        chooseVideoView.findViewById(R.id.tvDismiss).setOnClickListener(this);
        uploadVideoCancel = findViewById(R.id.upload_video_cancel);
        uploadVideoCancel.setOnClickListener(this);
        uploadCancelTv = findViewById(R.id.upload_cancel_tv);
        uploadCancelTv.setOnClickListener(this);
        releaseBtn = findViewById(R.id.release_btn);
        releaseBtn.setOnClickListener(this);
        briefIntroduction = findViewById(R.id.brief_introduction);
        uploadArticle = findViewById(R.id.upload_article);
        uploadArticle.setOnClickListener(this);
        webView = findViewById(R.id.webView);
        showToastPopView = View.inflate(this, R.layout.upload_toast_layout, null);
        uploadText = showToastPopView.findViewById(R.id.upload_text);
        uploadVideoBean = new UploadVideoBean();
        getTopicData();
        FinderBuriedPointManager.setFinderShortVideoStartMake(Constants.SHORT_VIDEO_START_MAKE, module_source);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus && isFirst) {
            // 开启javascript 渲染
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.setVerticalScrollBarEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setWebViewClient(new WebViewClient());
            // 载入内容
            webView.loadUrl("file:///android_asset/ugc.html");
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getUploadAgreement())) {
                uploadAgreementRl.setVisibility(View.VISIBLE);
            }

            /**
             * 协议取消
             */
            uploadAgreementCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            /**
             * 协议确定
             */
            uploadAgreementSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadAgreementRl.setVisibility(View.GONE);
                    PersonInfoManager.getInstance().setUploadAgreement("1");
                }
            });

            isFirst = false;
        }
    }

    /**
     * 读取草稿
     */
    private void readArticle() {
        OkGo.<ReadArticleModel>get(ApiConstants.getInstance().readArticle() + draftId)
                .tag(TAG)
                .execute(new JsonCallback<ReadArticleModel>() {
                    @Override
                    public void onSuccess(Response<ReadArticleModel> response) {
                        if (null != response.body() || null != response.body().getData()) {
                            articleData = response.body().getData();
                            setUploadVideoBean(articleData);
                        }
                    }

                    @Override
                    public void onError(Response<ReadArticleModel> response) {
                        super.onError(response);
                        if (null != response.body()) {
                            ToastUtils.showShort(response.message());
                        }
                    }
                });
    }

    /**
     * 获取草稿设置数据
     *
     * @return
     */
    private void setUploadVideoBean(ReadArticleModel.DataDTO articleData) {
        if (null == articleData) {
            return;
        }
        if (TextUtils.isEmpty(articleData.getBelongTopicId())) {
            topicSelectId = "";
        } else {
            topicSelectId = articleData.getBelongTopicId();
        }

        if (TextUtils.isEmpty(articleData.getWidth())) {
            uploadVideoBean.setWidth("");
        } else {
            uploadVideoBean.setWidth(articleData.getWidth());
        }

        if (TextUtils.isEmpty(articleData.getHeight())) {
            uploadVideoBean.setHeight("");
        } else {
            uploadVideoBean.setHeight(articleData.getHeight());
        }
        if (TextUtils.isEmpty(articleData.getImagesUrl())) {
            uploadVideoBean.setCoverImageUrl("");
        } else {
            uploadVideoBean.setCoverImageUrl(articleData.getImagesUrl());

            if (null != UploadActivity.this && !UploadActivity.this.isFinishing()
                    && !UploadActivity.this.isDestroyed()) {
                Glide.with(UploadActivity.this)
                        .load(uploadVideoBean.getCoverImageUrl())
                        .transform()
                        .into(uploadBtn);
            }
            uploadVideoCancel.setVisibility(View.VISIBLE);
            uploadChooseCover.setVisibility(View.VISIBLE);
            isUploadVideo = true;
        }
        if (TextUtils.isEmpty(articleData.getOrientation())) {
            uploadVideoBean.setOrientation("");
        } else {
            uploadVideoBean.setOrientation(articleData.getOrientation());
            if (TextUtils.equals(PORTRAIT_STR, articleData.getOrientation())) {
                orientation = "2";
            } else {
                orientation = "1";
            }
        }

        if (TextUtils.isEmpty(articleData.getPlayDuration())) {
            uploadVideoBean.setDuration("");
        } else {
            uploadVideoBean.setDuration(articleData.getPlayDuration());
        }

        if (TextUtils.isEmpty(articleData.getPlayUrl())) {
            uploadVideoBean.setUrl("");
        } else {
            uploadVideoBean.setUrl(articleData.getPlayUrl());
        }

        if (TextUtils.isEmpty(articleData.getTitle())) {
            briefIntroduction.setText("");
        } else {
            briefIntroduction.setText(articleData.getTitle());
        }
        briefIntroduction.setSelection(briefIntroduction.getText().length());

        for (int i = 0; i < uploadTagFlow.size(); i++) {
            if (TextUtils.equals(topicSelectId, uploadTagFlow.get(i).getId())) {
                tagAdapter.setSelectedList(i);
            }
        }
    }

    private void showToast(String textStr) {
        if (null == toast) {
            toastLayout = View.inflate(this, R.layout.upload_toast_layout, null);
            showToastUploadText = toastLayout.findViewById(R.id.upload_text);
            showToastUploadText.setText(textStr);
            toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastLayout);
            toast.show();
        } else {
            showToastUploadText.setText(textStr);
            toast.show();
        }
    }

    private void showToastPop() {
        if (null == showToastPop) {
            showToastPop = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(showToastPopView)
                    .setOutsideTouchable(true)
                    .setFocusable(true)
                    .setAnimationStyle(R.style.AnimCenter)
                    .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, Utils.getContext().getResources().getDisplayMetrics().heightPixels)
                    .create()
                    .showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            showToastPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PersonInfoManager.getInstance().isRequestToken()) {
            try {
                getUserToken(VideoInteractiveParam.getInstance().getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用获取的code去换token
     */
    public void getUserToken(String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("ignoreGdy", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<TokenModel>post(ApiConstants.getInstance().mycsToken())
                .tag("userToken")
                .upJson(jsonObject)
                .execute(new JsonCallback<TokenModel>(TokenModel.class) {
                    @Override
                    public void onSuccess(Response<TokenModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        try {
                            if (response.body().getCode() == 200) {
                                if (null == response.body().getData()) {
                                    ToastUtils.showShort(R.string.data_err);
                                    return;
                                }
                                Log.d("mycs_token", "转换成功");
                                try {
                                    PersonInfoManager.getInstance().setToken(VideoInteractiveParam.getInstance().getCode());
                                    PersonInfoManager.getInstance().setUserId(response.body().getData().getLoginSysUserVo().getId());
                                    PersonInfoManager.getInstance().setTgtCode(VideoInteractiveParam.getInstance().getCode());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                PersonInfoManager.getInstance().setTransformationToken(response.body().getData().getToken());
                            } else {
                                ToastUtils.showShort(response.body().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<TokenModel> response) {
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
                            return;
                        }
                        ToastUtils.showShort(R.string.net_err);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    /**
     * 获取话题
     */
    private void getTopicData() {
        OkGo.<TopicModel>get(ApiConstants.getInstance().getTopics())
                .tag(TAG)
                .execute(new JsonCallback<TopicModel>() {
                    @Override
                    public void onSuccess(Response<TopicModel> response) {
                        if (null != response.body()) {
                            uploadTagFlow = response.body().getData();
                            if (null != uploadTagFlow) {
                                setTagFlowData(uploadTagFlow);
                            }

                            if (!TextUtils.isEmpty(draftId)) {
                                //进来读取草稿
                                readArticle();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<TopicModel> response) {
                        super.onError(response);
                        ToastUtils.showShort(response.message());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    /**
     * 获取话题数据
     */
    public void setTagFlowData(final List<TopicModel.DataDTO> list) {
        tagAdapter = new TagAdapter<TopicModel.DataDTO>(list) {
            @Override
            public View getView(FlowLayout parent, int position, TopicModel.DataDTO recordsDTO) {
                TextView tv = (TextView) LayoutInflater.from(UploadActivity.this)
                        .inflate(R.layout.item_tag_layout, parent, false);
                tv.setText("#" + recordsDTO.getTitle());
                return tv;
            }
        };
        tagFlow.setAdapter(tagAdapter);

        tagFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Log.e("onTagClick", list.get(position).getTitle());
                if (isSelected) {
                    topicSelectId = list.get(position).getId() + "";
                    selectTopicStr = list.get(position).getTitle();
                } else {
                    topicSelectId = "";
                    selectTopicStr = "";
                }
                return true;
            }
        });

        tagFlow.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Log.e("choose", selectPosSet.toString());
                if (selectPosSet.size() > 0) {
                    isSelected = true;
                } else {
                    isSelected = false;
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tvChooseImage) {
            chooseVideo(false);
            uploadVideoWindow.dissmiss();
        } else if (id == R.id.tvDismiss) {
            uploadVideoWindow.dissmiss();
        } else if (id == R.id.upload_btn) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                if (isUploadVideo) {
                    chooseVideo(true);
                } else {
                    showChooseVideoPop();
                }
            }

        } else if (id == R.id.upload_video_cancel) {
            uploadVideoCancel.setVisibility(View.GONE);
            uploadBtn.setImageResource(R.drawable.upload_btn);
            uploadVideoBean.setWidth("");
            uploadVideoBean.setHeight("");
            uploadVideoBean.setCoverImageUrl("");
            uploadVideoBean.setDuration("");
            uploadVideoBean.setUrl("");
            uploadCoverBean = null;
            uploadChooseCover.setVisibility(View.GONE);
            isUploadVideo = false;
        } else if (id == R.id.upload_cancel_tv) {
            finish();
        } else if (id == R.id.release_btn) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                if (!canSaveDraft) {
                    ToastUtils.showShort("视频上传中，请稍后");
                    return;
                }

                if (TextUtils.isEmpty(uploadVideoBean.getUrl())) {
                    ToastUtils.showShort("视频还未上传");
                    return;
                }

                if (TextUtils.isEmpty(briefIntroduction.getText().toString())) {
                    ToastUtils.showShort("请填写视频简介");
                    return;
                }
                String url;
                if (TextUtils.isEmpty(draftId)) {
                    url = ApiConstants.getInstance().releaseContent();
                } else {
                    url = ApiConstants.getInstance().updateArticle();
                }
                //发布作品
                releaseContent(url, "1", topicSelectId, uploadVideoBean.getWidth(), uploadVideoBean.getHeight(),
                        uploadVideoBean.getCoverImageUrl(), orientation, uploadVideoBean.getDuration()
                        , uploadVideoBean.getUrl(), briefIntroduction.getText().toString(), uploadTypes, "提交成功");
//                    //保存草稿
//                    updateArticle(topicSelectId, uploadVideoBean.getWidth(), uploadVideoBean.getHeight(),
//                            uploadVideoBean.getCoverImageUrl(), orientation, uploadVideoBean.getDuration()
//                            , uploadVideoBean.getUrl(), briefIntroduction.getText().toString(), uploadTypes, "提交成功");


            }
        } else if (id == R.id.upload_article) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                if (canSaveDraft) {
                    String url;
                    if (TextUtils.isEmpty(draftId)) {
                        url = ApiConstants.getInstance().releaseContent();
                    } else {
                        url = ApiConstants.getInstance().updateArticle();
                    }
                    if (!TextUtils.isEmpty(uploadVideoBean.getUrl()) || !TextUtils.isEmpty(briefIntroduction.getText())) {
                        releaseContent(url, "0", topicSelectId, uploadVideoBean.getWidth(), uploadVideoBean.getHeight(),
                                uploadVideoBean.getCoverImageUrl(), orientation, uploadVideoBean.getDuration()
                                , uploadVideoBean.getUrl(), briefIntroduction.getText().toString(), uploadTypes, "保存成功");
                    } else {
                        showToast("没有内容可保存");
                    }
                } else {
                    ToastUtils.showShort("视频上传中，请稍后");
                }
            }
        } else if (id == R.id.no_login_tips_cancel) {
            if (null != noLoginTipsPop) {
                noLoginTipsPop.dissmiss();
            }
        } else if (id == R.id.no_login_tips_ok) {
            if (null != noLoginTipsPop) {
                noLoginTipsPop.dissmiss();
            }
            try {
                param.toLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件选择框
     */
    private void showChooseVideoPop() {
        //创建并显示popWindow
        uploadVideoWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(chooseVideoView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create()
                .showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }


    /**
     * 选择视频
     */
    private void chooseVideo(final boolean isChooseCover) {
        new RxPermissions(this).request(permissionsGroup).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Set<MimeType> mimeTypes;
                    if (isChooseCover) {
                        mimeTypes = MimeType.ofImage();
                    } else {
                        mimeTypes = MimeType.ofVideo();
                    }
                    Matisse.from(UploadActivity.this)
                            .choose(mimeTypes)
                            .countable(true)//true:选中后显示数字;false:选中后显示对号
                            .maxSelectable(1)//可选的最大数
                            .showSingleMediaType(true)
                            .capture(false)//选择照片时，是否显示拍照
                            .addFilter(new Filter() {
                                @Override
                                protected Set<MimeType> constraintTypes() {
                                    return new HashSet<MimeType>() {{
                                        if (isChooseCover) {
                                            add(MimeType.AVI);
                                            add(MimeType.MKV);
                                            add(MimeType.GIF);
                                            add(MimeType.MP4);
                                        } else {
                                            add(MimeType.PNG);
                                            add(MimeType.JPEG);
                                            add(MimeType.WEBP);
                                        }
                                    }};
                                }

                                @Override
                                public IncapableCause filter(Context context, Item item) {
                                    try {
                                        InputStream inputStream = getContentResolver().openInputStream(item.getContentUri());
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;
                                        BitmapFactory.decodeStream(inputStream, null, options);
                                        int width = options.outWidth;
                                        int height = options.outHeight;

//                                        if (width >= 50)
//                                            return new IncapableCause("宽度超过500px");

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                            })
                            //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                            .captureStrategy(new CaptureStrategy(true, "com.changsha.apps.android.mycs"))
                            .imageEngine(new GlideEngine())//图片加载引擎
                            .forResult(REQUEST_CODE_CHOOSE);//
                } else {
                    ToastUtils.showShort("获取权限失败，请在设置中打开相关权限");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> result = Matisse.obtainResult(data);
            imageUrlList.clear();
            fileList.clear();
            for (Uri imageUri : result) {
                imageUrlList.add(FileUtils.getRealPathFromURI(getApplication(), imageUri));
            }

            for (int i = 0; i < imageUrlList.size(); i++) {
                File file = new File(imageUrlList.get(i));
                try {
                    worksSize = DataCleanUtils.getFolderSize(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fileList.add(file);
            }
            canSaveDraft = false;
            //调用上传接口
            if (isUploadVideo) {
                uploadCover();
            } else {
                uploadVideo();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelAll();
        if (null != showToastPop) {
            showToastPop.dissmiss();
        }


    }

    /**
     * 上传视频
     */
    private void uploadVideo() {
        OkGo.<UploadVideoBean>post(ApiConstants.getInstance().uploadVideo() + "?isPublic=1&generateCoverImage=1")
                .tag(TAG)
                .isMultipart(true)
                .addFileParams("file", fileList)
                .execute(new JsonCallback<UploadVideoBean>() {

                    @Override
                    public void onSuccess(Response<UploadVideoBean> response) {
                        uploadVideoBean = response.body();
                        if (null != UploadActivity.this && !UploadActivity.this.isFinishing()
                                && !UploadActivity.this.isDestroyed()) {
                            Glide.with(UploadActivity.this)
                                    .load(uploadVideoBean.getCoverImageUrl())
                                    .into(uploadBtn);
                        }
                        if (uploadVideoBean.getOrientation().equals(PORTRAIT_STR)) {
                            orientation = "2";
                        } else {
                            orientation = "1";
                        }
                        isUploadVideo = true;
                        uploadProgress.setVisibility(View.GONE);
                        uploadVideoCancel.setVisibility(View.VISIBLE);
                        worksDuration = Float.parseFloat(uploadVideoBean.getDuration());
                        //行为埋点 点击上传作品按钮时触发
                        Log.d("upLoadActivity", "话题：" + selectTopicStr + "---" + "内容id" + "" + "---" + "作品简介" + briefIntroduction.getText().toString()
                                + "---" + "作品时长" + worksDuration + "---" + "作品大小" + worksSize);
                        uploadChooseCover.setVisibility(View.VISIBLE);
                        uploadBtn.setEnabled(true);
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        uploadProgress.setVisibility(View.VISIBLE);
                        uploadProgress.setProgress((int) (progress.fraction * 100));
                        uploadBtn.setImageResource(R.drawable.uploading);
                        uploadBtn.setEnabled(false);
                    }

                    @Override
                    public void onError(Response<UploadVideoBean> response) {
                        super.onError(response);
                        ToastUtils.showShort(response.message());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        canSaveDraft = true;
                    }
                });
    }

    /**
     * 上传封面
     */
    private void uploadCover() {
        OkGo.<UploadVideoBean>post(ApiConstants.getInstance().uploadVideo() + "?isPublic=1&generateCoverImage=1")
                .tag(TAG)
                .isMultipart(true)
                .addFileParams("file", fileList)
                .execute(new JsonCallback<UploadVideoBean>() {

                    @Override
                    public void onSuccess(Response<UploadVideoBean> response) {
                        uploadCoverBean = response.body();
                        if (null != UploadActivity.this && !UploadActivity.this.isFinishing()
                                && !UploadActivity.this.isDestroyed()) {
                            Glide.with(UploadActivity.this)
                                    .load(uploadCoverBean.getUrl())
                                    .into(uploadBtn);
                        }
                        uploadVideoBean.setCoverImageUrl(uploadCoverBean.getUrl());
                        isUploadVideo = true;
                        uploadProgress.setVisibility(View.GONE);
                        uploadVideoCancel.setVisibility(View.VISIBLE);
                        uploadChooseCover.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                    }

                    @Override
                    public void onError(Response<UploadVideoBean> response) {
                        super.onError(response);
                        ToastUtils.showShort(response.message());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        canSaveDraft = true;
                    }
                });
    }


    /**
     * 发布内容
     */
    private void releaseContent(String url, final String ugcUploadWay, String belongTopicId, String width, String height, String imagesUrl,
                                String orientation, String playDuration, String playUrl, String title, String type, final String tipStr) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", draftId);
            jsonObject.put("belongTopicId", belongTopicId);
            jsonObject.put("width", width);
            jsonObject.put("height", height);
            jsonObject.put("imagesUrl", imagesUrl);
            jsonObject.put("thumbnailUrl", imagesUrl);
            jsonObject.put("orientation", orientation);
            jsonObject.put("playDuration", playDuration);
            jsonObject.put("playUrl", playUrl);
            jsonObject.put("title", title);
            jsonObject.put("type", type);
            jsonObject.put("ugcUploadWay", ugcUploadWay);
            jsonObject.put("subType", "video");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<UploadVideoModel>post(url)
                .tag(TAG)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .upJson(jsonObject)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<UploadVideoModel>(UploadVideoModel.class) {
                    @Override
                    public void onSuccess(Response<UploadVideoModel> response) {
                        if (null != response.body()) {
                            //行为埋点 编辑视频发布
                            //selectTopicStr workDuration  worksSize

                            UploadVideoModel bean = response.body();
//                            showToast(tipStr);
                            FinderPointModel model = new FinderPointModel();
                            if (null != bean.getData()) {
                                model.setContent_id(bean.getData().getContentId());
                                model.setContent_name(briefIntroduction.getText().toString());
                                model.setWorks_duration(String.valueOf(worksDuration));
                                model.setWorks_size(worksSize + "");
                                model.setWorks_brief(briefIntroduction.getText().toString());
                                FinderBuriedPointManager.setFinderCommon(Constants.SHORT_VIDEO_SUBMIT, model);

                                uploadText.setText(tipStr);
                                showToastPop();
                                String toDraft = "";
                                if (TextUtils.equals("0", ugcUploadWay)) {
                                    toDraft = "?tabsId=2";
                                }
                                toPersonelCenter(toDraft);
                            } else {
                                if (null != bean.getMessage()) {
                                    ToastUtils.showShort(bean.getMessage());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<UploadVideoModel> response) {
                        super.onError(response);
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    private void toPersonelCenter(final String isToH5Draft) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);//休眠2秒
                    if (Utils.mIsDebug) {
                        param.recommendUrl(Constants.PERSONAL_CENTER + isToH5Draft, null);
                    } else {
                        param.recommendUrl(Constants.PERSONAL_CENTER_ZS + isToH5Draft, null);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }.start();
    }

    /**
     * 没有登录情况下 点击点赞收藏评论 提示登录的提示框
     */
    private void noLoginTipsPop() {
        if (null == noLoginTipsPop) {
            noLoginTipsPop = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(noLoginTipsView)
                    .enableBackgroundDark(true)
                    .setOutsideTouchable(true)
                    .setFocusable(true)
                    .setAnimationStyle(R.style.AnimCenter)
                    .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, Utils.getContext().getResources().getDisplayMetrics().heightPixels)
                    .create()
                    .showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            noLoginTipsPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

}