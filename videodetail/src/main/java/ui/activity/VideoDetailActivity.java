package ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.contants.Contants;
import com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl;
import com.tencent.liteav.demo.superplayer.model.utils.SystemUtils;
import com.tencent.liteav.demo.superplayer.ui.player.FullScreenPlayer;
import com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer;
import com.tencent.liteav.demo.superplayer.ui.view.PointSeekBar;
import com.tencent.rtmp.TXLiveConstants;
import com.wdcs.callback.JsonCallback;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.manager.BuriedPointModelManager;
import com.wdcs.manager.ContentBuriedPointManager;
import com.wdcs.model.CommentModel;
import com.wdcs.model.ContentStateModel;
import com.wdcs.model.DataDTO;
import com.wdcs.model.RecommendModel;
import com.wdcs.model.TokenModel;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.KeyboardUtils;
import com.wdcs.utils.NumberFormatTool;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.SPUtils;
import com.wdcs.utils.ScreenUtils;
import com.wdcs.utils.SoftKeyBoardListener;
import com.wdcs.utils.ToastUtils;
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import adpter.VideoDetailCommentPopRvAdapter;
import utils.GlideUtil;
import widget.LoadingView;

import static android.widget.RelativeLayout.BELOW;
import static com.tencent.liteav.demo.superplayer.ui.player.AbsPlayer.formattedTime;
import static com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer.mDuration;
import static com.wdcs.constants.Constants.BLUE_V;
import static com.wdcs.constants.Constants.VIDEOTAG;
import static com.wdcs.constants.Constants.YELLOW_V;
import static com.wdcs.constants.Constants.success_code;
import static com.wdcs.constants.Constants.token_error;
import static com.wdcs.utils.ShareUtils.toShare;
import static ui.activity.VideoHomeActivity.lsDuration;
import static ui.activity.VideoHomeActivity.maxPercent;
import static ui.activity.VideoHomeActivity.uploadBuriedPoint;
import static utils.NetworkUtil.setDataWifiState;

public class VideoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private SuperPlayerView playerView;
    private RelativeLayout agreeNowifiPlay;
    private TextView continuePlay;
    private TextView publisherName;
    private ImageView officialCertificationImg;
    private ImageView publisherHeadimg;
    private TextView foldText;
    private TextView huati;
    private TextView expendText;
    private LinearLayout introduceLin;
    private LoadingView videoLoadingProgress;
    private String contentId = "";
    private List<DataDTO> mDatas;
    private LinearLayout superplayerIvFullscreen;
    private String TAG = "VideoDetailSimpleActivity";
    private RelativeLayout rootview;
    public double pointPercent;                         // 每一次记录的节点播放百分比
    private long everyOneDuration; //每一次记录需要上报的播放时长 用来分段上报埋点
    private String isPreview; //判断是否是预览版页面UI
    private RelativeLayout videoDetailCommentBtn;

    private TextView commentTotal;
    private TextView commentPopCommentTotal;
    private ImageView videoDetailCollectionImage; //收藏图标
    private ImageView videoDetailLikesImage; //点赞图标
    private TextView likesNum; //点赞数
    private TextView collectionNum; //收藏数
    private LinearLayout videoDetailCollection;
    private LinearLayout videoDetailLikes;
    private LinearLayout share;

    private View contentView;
    private RelativeLayout dismissPop;
    private RecyclerView commentPopRv;
    private TextView commentEdtInput;
    private RelativeLayout commentPopRl;
    //附着在软键盘上的输入弹出窗
    public CustomPopWindow inputAndSendPop;
    private View sendPopContentView;
    private LinearLayout edtParent;
    private EditText edtInput;
    private TextView tvSend;
    private RelativeLayout videoDetailWhiteCommentRl;
    private TextView commentEdittext;
    private int mPageIndex = 1; //评论列表页数
    private int mPageSize = 10; //评论列表每页多少条
    //评论列表数据
    private List<CommentModel.DataDTO.RecordsDTO> mCommentPopRvData;
    private List<CommentModel.DataDTO> mCommentPopDtoData;
    private VideoDetailCommentPopRvAdapter commentPopRvAdapter;
    private VideoInteractiveParam param;
    private String transformationToken = "";
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    public CustomPopWindow popupWindow;
    public CustomPopWindow sharePop;
    private View sharePopView;
    private ImageView shareWxBtn;
    private ImageView shareCircleBtn;
    private ImageView shareQqBtn;
    private String recommendTag = "recommend";
    private List<RecommendModel.DataDTO.RecordsDTO> recommondList;
    private ViewFlipper videoFlipper;
    private boolean isFirst = true;
    private RelativeLayout backLl;
    private SoftKeyBoardListener softKeyBoardListener;
    private String spaceStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SystemUtils.setNavbarColor(this, R.color.video_black);
        setContentView(R.layout.activity_video_detail);
        initView();
        if (SPUtils.isVisibleNoWifiView(this)) {
            agreeNowifiPlay.setVisibility(View.VISIBLE);
        } else {
            agreeNowifiPlay.setVisibility(View.GONE);
        }
        getOneVideo(contentId);
    }

    private void initView() {
        param = VideoInteractiveParam.getInstance();
        isPreview = getIntent().getStringExtra("isPreview");
        contentId = getIntent().getStringExtra("contentId");
        backLl = findViewById(R.id.back_ll);
        videoDetailCommentBtn = findViewById(R.id.video_detail_comment_btn);
        videoDetailCommentBtn.setOnClickListener(this);
        if (null != isPreview && !TextUtils.isEmpty(isPreview)) {
            videoDetailCommentBtn.setVisibility(View.GONE);
        }
        commentTotal = findViewById(R.id.comment_total);
        videoDetailWhiteCommentRl = findViewById(R.id.video_detail_white_comment_rl);
        videoDetailWhiteCommentRl.setOnClickListener(this);
        commentEdittext = findViewById(R.id.comment_edittext);

        videoDetailCollection = findViewById(R.id.video_detail_collection);
        videoDetailCollection.setOnClickListener(this);
        videoDetailLikes = findViewById(R.id.video_detail_likes);
        videoDetailLikes.setOnClickListener(this);
        share = findViewById(R.id.share);
        share.setOnClickListener(this);
        setSoftKeyBoardListener();
        videoDetailCollectionImage = findViewById(R.id.video_detail_collection_image);
        videoDetailLikesImage = findViewById(R.id.video_detail_likes_image);
        likesNum = findViewById(R.id.likes_num);
        collectionNum = findViewById(R.id.collection_num);

        contentView = View.inflate(this, R.layout.fragment_video_comment_pop, null);
        sendPopContentView = View.inflate(this, R.layout.layout_input_window, null);
        commentPopCommentTotal = contentView.findViewById(R.id.comment_pop_comment_total);
        edtParent = sendPopContentView.findViewById(R.id.edt_parent);
        edtInput = sendPopContentView.findViewById(R.id.edtInput);
        tvSend = sendPopContentView.findViewById(R.id.tvSend);
        /**
         * 发送评论
         */
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInput.getText())) {
                    ToastUtils.showShort("请输入评论");
                } else {
                    toComment(edtInput.getText().toString(), contentId);
                }
            }
        });
        getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), true);
        noLoginTipsView = View.inflate(this, R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);
        dismissPop = contentView.findViewById(R.id.dismiss_pop);
        dismissPop.setOnClickListener(this);
        commentPopRv = contentView.findViewById(R.id.comment_pop_rv);
        commentEdtInput = contentView.findViewById(R.id.comment_edtInput);
        commentPopRl = contentView.findViewById(R.id.comment_pop_rl);
        commentPopRl.setOnClickListener(this);
        sharePopView = View.inflate(this, R.layout.share_pop_view, null);
        shareWxBtn = sharePopView.findViewById(R.id.share_wx_btn);
        shareWxBtn.setOnClickListener(this);
        shareCircleBtn = sharePopView.findViewById(R.id.share_circle_btn);
        shareCircleBtn.setOnClickListener(this);
        shareQqBtn = sharePopView.findViewById(R.id.share_qq_btn);
        shareQqBtn.setOnClickListener(this);

        rootview = findViewById(R.id.rootview);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        playerView = new SuperPlayerView(this, getWindow().getDecorView(), true);
        playerView.mWindowPlayer.setVideoDetailReportDuration(0);
        SuperPlayerImpl.mCurrentPlayVideoURL = null;
        agreeNowifiPlay = findViewById(R.id.agree_nowifi_play);
        continuePlay = findViewById(R.id.continue_play);
        continuePlay.setOnClickListener(this);
        publisherName = findViewById(R.id.publisher_name);
        officialCertificationImg = findViewById(R.id.official_certification_img);
        publisherHeadimg = findViewById(R.id.publisher_headimg);
        publisherHeadimg.setOnClickListener(this);
        foldText = findViewById(R.id.fold_text);
        foldText.setOnClickListener(this);
        expendText = findViewById(R.id.expend_text);
        expendText.setOnClickListener(this);
        huati = findViewById(R.id.huati);
        introduceLin = findViewById(R.id.introduce_lin);
        videoLoadingProgress = findViewById(R.id.video_loading_progress);
        superplayerIvFullscreen = findViewById(R.id.superplayer_iv_fullscreen);
        superplayerIvFullscreen.setOnClickListener(this);
        videoFlipper = findViewById(R.id.video_flipper);
        mDatas = new ArrayList<>();
        recommondList = new ArrayList<>();
        initCommentPopRv();

        playerView.mWindowPlayer.mSeekBarProgress.setOnSeekBarChangeListener(new PointSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(PointSeekBar seekBar, int progress, boolean fromUser) {
                if (null == playerView) {
                    return;
                }
                if (null == playerView.mWindowPlayer) {
                    return;
                }
                if (playerView.mWindowPlayer.mGestureVideoProgressLayout != null && fromUser) {
                    playerView.mWindowPlayer.mGestureVideoProgressLayout.show();
                    float percentage = ((float) progress) / seekBar.getMax();
                    float currentTime = (mDuration * percentage);
                    playerView.mWindowPlayer.mGestureVideoProgressLayout.setTimeText(formattedTime((long) currentTime) + " / " + formattedTime((long) mDuration));
                    playerView.mWindowPlayer.mGestureVideoProgressLayout.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(PointSeekBar seekBar) {
                if (null == playerView) {
                    return;
                }
                if (null == playerView.mWindowPlayer) {
                    return;
                }
                playerView.mWindowPlayer.removeCallbacks(playerView.mWindowPlayer.mHideViewRunnable);
            }

            @Override
            public void onStopTrackingTouch(PointSeekBar seekBar) {
                int curProgress = seekBar.getProgress();
                int maxProgress = seekBar.getMax();

                switch (playerView.mWindowPlayer.mPlayType) {
                    case VOD:
                        if (curProgress >= 0 && curProgress <= maxProgress) {
                            // 关闭重播按钮
                            playerView.mWindowPlayer.toggleView(playerView.mWindowPlayer.mLayoutReplay, false);
                            float percentage = ((float) curProgress) / maxProgress;
                            long duration = (long) (percentage * mDuration);
                            lsDuration = duration;
                            if (percentage > maxPercent) {
                                maxPercent = percentage;
                            }
                            int position = (int) (mDuration * percentage);
                            if (playerView.mWindowPlayer.mControllerCallback != null) {
                                playerView.mWindowPlayer.mControllerCallback.onSeekTo(position);
                                playerView.mWindowPlayer.mControllerCallback.onResume();
                            }
                        }
                        break;
                }
                playerView.mWindowPlayer.postDelayed(playerView.mWindowPlayer.mHideViewRunnable, Contants.delayMillis);
            }
        });

        //全屏进度条监听
        playerView.mFullScreenPlayer.mSeekBarProgress.setOnSeekBarChangeListener(new PointSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(PointSeekBar seekBar, int progress, boolean fromUser) {
                if (null == playerView) {
                    return;
                }
                if (null == playerView.mFullScreenPlayer) {
                    return;
                }

                if (playerView.mFullScreenPlayer.mGestureVideoProgressLayout != null && fromUser) {
                    playerView.mFullScreenPlayer.mGestureVideoProgressLayout.show();
                    float percentage = ((float) progress) / seekBar.getMax();
                    float currentTime = (mDuration * percentage);
                    playerView.mFullScreenPlayer.mGestureVideoProgressLayout.setTimeText(formattedTime((long) currentTime) + " / " + formattedTime((long) mDuration));
                    playerView.mFullScreenPlayer.mGestureVideoProgressLayout.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(PointSeekBar seekBar) {
                if (null == playerView) {
                    return;
                }
                if (null == playerView.mFullScreenPlayer) {
                    return;
                }
                playerView.mFullScreenPlayer.removeCallbacks(playerView.mFullScreenPlayer.mHideViewRunnable);
            }

            @Override
            public void onStopTrackingTouch(PointSeekBar seekBar) {
                int curProgress = seekBar.getProgress();
                int maxProgress = seekBar.getMax();

                switch (playerView.mFullScreenPlayer.mPlayType) {
                    case VOD:
                        if (curProgress >= 0 && curProgress <= maxProgress) {
                            // 关闭重播按钮
                            playerView.mFullScreenPlayer.toggleView(playerView.mFullScreenPlayer.mLayoutReplay, false);
                            float percentage = ((float) curProgress) / maxProgress;
                            long duration = (long) (percentage * mDuration);
                            lsDuration = duration;
                            if (percentage > maxPercent) {
                                maxPercent = percentage;
                            }
                            int position = (int) (mDuration * percentage);

                            if (playerView.mFullScreenPlayer.mControllerCallback != null) {
                                playerView.mFullScreenPlayer.mControllerCallback.onSeekTo(position);
                                playerView.mFullScreenPlayer.mControllerCallback.onResume();
                            }
                        }
                        break;
                }
                playerView.mFullScreenPlayer.postDelayed(playerView.mFullScreenPlayer.mHideViewRunnable, Contants.delayMillis);
            }
        });


        /**
         * 监听播放器播放窗口变化回调
         */
        playerView.playModeCallBack = new SuperPlayerView.PlayModeCallBack() {

            @Override
            public void getPlayMode(SuperPlayerDef.PlayerMode playerMode) {
                if (playerMode.equals(SuperPlayerDef.PlayerMode.FULLSCREEN)) {
                    introduceLin.setVisibility(View.GONE);
                    superplayerIvFullscreen.setVisibility(View.GONE);
                    backLl.setVisibility(View.GONE);
                    videoDetailCommentBtn.setVisibility(View.GONE);
                } else if (playerMode.equals(SuperPlayerDef.PlayerMode.WINDOW)) {
                    introduceLin.setVisibility(View.VISIBLE);
                    superplayerIvFullscreen.setVisibility(View.VISIBLE);
                    backLl.setVisibility(View.VISIBLE);
                    if (null == isPreview && TextUtils.isEmpty(isPreview)) {
                        videoDetailCommentBtn.setVisibility(View.VISIBLE);
                    }
                    setLikeCollection(playerView.contentStateModel);
                }
            }
        };


        playerView.mWindowPlayer.isReplayClick = new WindowPlayer.IsReplayClick() {
            @Override
            public void getReplayClick() {
                uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoDetailActivity.this, mDatas.get(0).getThirdPartyId(), "", "", Constants.CMS_VIDEO_PLAY), Constants.CMS_VIDEO_PLAY);
            }
        };

        playerView.mFullScreenPlayer.isReplayClick = new FullScreenPlayer.FullIsReplayClick() {
            @Override
            public void getFullReplayClick() {
                uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoDetailActivity.this, mDatas.get(0).getThirdPartyId(), "", "", Constants.CMS_VIDEO_PLAY), Constants.CMS_VIDEO_PLAY);
            }
        };

        SuperPlayerImpl.readPlayCallBack = new SuperPlayerImpl.ReadPlayCallBack() {
            @Override
            public void ReadPlayCallback() {
                uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoDetailActivity.this, mDatas.get(0).getThirdPartyId(), "", "", Constants.CMS_VIDEO_PLAY_AUTO), Constants.CMS_VIDEO_PLAY_AUTO);
            }
        };

        SuperPlayerImpl.setAutoPlayOverCallBack(new SuperPlayerImpl.AutoPlayOverCallBack() {
            @Override
            public void AutoPlayOverCallBack() {
                final String event;
                if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
                    //不为重播
                    event = Constants.CMS_VIDEO_OVER_AUTO;
                } else {
                    //重播
                    event = Constants.CMS_VIDEO_OVER;
                }
                //拖动/自动播放结束上报埋点
                uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoDetailActivity.this, mDatas.get(0).getThirdPartyId(), String.valueOf(mDuration * 1000), "100", event), event);
            }
        });
    }


    /**
     * 添加软键盘监听
     */
    private void setSoftKeyBoardListener() {
        softKeyBoardListener = new SoftKeyBoardListener(this);
        //软键盘状态监听
        softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //软键盘已经显示，做逻辑
                Log.e("yqh", "软键盘已经显示,做逻辑");
            }

            @Override
            public void keyBoardHide(int height) {
                //软键盘已经隐藏,做逻辑
//                SystemUtils.hideSystemUI(decorView);
                if (null != inputAndSendPop) {
                    inputAndSendPop.getPopupWindow().dismiss();
                }
                Log.e("yqh", "软键盘已经隐藏,做逻辑");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            if (playerView != null) {
                playerView.mSuperPlayer.resume();
            }

            if (!TextUtils.isEmpty(contentId)) {
                getContentState(contentId);
            }
        }
        isFirst = false;


        if (PersonInfoManager.getInstance().isRequestToken()) {
            try {
                getUserToken(VideoInteractiveParam.getInstance().getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (null != playerView && playerView.mSuperPlayer.getPlayerMode() == SuperPlayerDef.PlayerMode.FULLSCREEN) {
            SystemUtils.hideSystemUI(getWindow().getDecorView());
        }
    }

    /**
     * 使用获取的code去换token
     */
    public void getUserToken(String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
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

                        if (response.body().getCode() == 200) {
                            if (null == response.body().getData()) {
                                ToastUtils.showShort(R.string.data_err);
                                return;
                            }
                            Log.d("mycs_token", "转换成功");
                            try {
                                PersonInfoManager.getInstance().setToken(VideoInteractiveParam.getInstance().getCode());
                                PersonInfoManager.getInstance().setGdyToken(response.body().getData().getGdyToken());
                                PersonInfoManager.getInstance().setUserId(response.body().getData().getLoginSysUserVo().getId());
                                PersonInfoManager.getInstance().setTgtCode(VideoInteractiveParam.getInstance().getCode());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            transformationToken = response.body().getData().getToken();
                            PersonInfoManager.getInstance().setTransformationToken(transformationToken);
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
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
     * 初始化评论列表
     */
    private void initCommentPopRv() {
        mCommentPopRvData = new ArrayList<>();
        mCommentPopDtoData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentPopRv.setLayoutManager(linearLayoutManager);
        commentPopRvAdapter = new VideoDetailCommentPopRvAdapter(R.layout.comment_pop_rv_item, mCommentPopRvData, this);
        commentPopRvAdapter.bindToRecyclerView(commentPopRv);
        commentPopRvAdapter.setEmptyView(R.layout.comment_list_empty);
        commentPopRv.setAdapter(commentPopRvAdapter);
        commentPopRvAdapter.notifyDataSetChanged();
        commentPopRvAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                commentPopRv.post(new Runnable() {
                    @Override
                    public void run() {
                        mPageIndex++;
                        getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), false);
                    }
                });
            }
        }, commentPopRv);
    }

    /**
     * 获取推荐列表数据
     */
    private void getRecommend(String contentId, final int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("current", "1");
            jsonObject.put("pageSize", "999");
            jsonObject.put("contentId", contentId + "");
            jsonObject.put("pageIndex", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<RecommendModel>post(ApiConstants.getInstance().recommendList())
                .tag(recommendTag)
                .upJson(jsonObject)
                .execute(new JsonCallback<RecommendModel>() {
                    @Override
                    public void onSuccess(Response<RecommendModel> response) {
                        if (null == response.body().getData()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }
                        if (response.body().getCode().equals("200")) {
                            recommondList.clear();
                            recommondList.addAll(response.body().getData().getRecords());


                            if (null == videoFlipper) {
                                return;
                            }

                            if (mDatas.get(position).isClosed()) {
                                videoFlipper.setVisibility(View.GONE);
                                return;
                            }

                            if (recommondList.size() > 1) {
                                videoFlipper.setVisibility(View.VISIBLE);
                                videoFlipper.startFlipping();
                                videoFlipper.setAutoStart(true);
                            } else if (recommondList.size() == 1) {
                                videoFlipper.setVisibility(View.VISIBLE);
                                videoFlipper.setAutoStart(false);
                            } else if (recommondList.size() == 0) {
                                videoFlipper.setVisibility(View.GONE);
                            }
                            getViewFlipperData(recommondList, videoFlipper, mDatas.get(position));
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }

                    }

                    @Override
                    public void onError(Response<RecommendModel> response) {
                        super.onError(response);
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
                            return;
                        }
                        ToastUtils.showShort(R.string.net_err);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (SPUtils.isVisibleNoWifiView(VideoDetailActivity.this)) {
                            SPUtils.getInstance().put(Constants.AGREE_NETWORK, "0");
                        }
                    }
                });
    }

    /**
     * 获取首页滚动消息
     */
    public void getViewFlipperData(final List<RecommendModel.DataDTO.RecordsDTO> list,
                                   final ViewFlipper viewFlipper, final DataDTO mItem) {
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i).getTitle();
            View view = View.inflate(this, R.layout.customer_viewflipper_item, null);
            ImageView viewFlipperIcon = view.findViewById(R.id.view_flipper_icon);
            if (null != VideoDetailActivity.this && !VideoDetailActivity.this.isFinishing() && !VideoDetailActivity.this.isDestroyed()) {
                Glide.with(this)
                        .load(list.get(i).getThumbnailUrl())
                        .into(viewFlipperIcon);
            }

            TextView textView = view.findViewById(R.id.view_flipper_content);
            textView.setTextColor(this.getResources().getColor(R.color.white));
            textView.setText(item);
            ImageView cancel = view.findViewById(R.id.view_flipper_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewFlipper.stopFlipping();
                    viewFlipper.setVisibility(View.GONE);
                    mItem.setClosed(true);
                }
            });
            viewFlipper.addView(view);
        }

        //viewFlipper的点击事件
        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取子View的id
                int mPosition = viewFlipper.getDisplayedChild();
                Log.e("yqh", "子View的id:" + mPosition);
                try {
                    param.recommendUrl(list.get(mPosition).getUrl(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取评论列表
     */
    public void getCommentList(String pageIndex, String pageSize, final boolean isRefresh) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contentId", contentId);
            jsonObject.put("pageIndex", pageIndex);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("pcommentId", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<CommentModel>post(ApiConstants.getInstance().getCommentListUrl())
                .tag(VIDEOTAG)
                .upJson(jsonObject)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<CommentModel>(CommentModel.class) {
                    @Override
                    public void onSuccess(Response<CommentModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (response.body().getCode() == 200) {
                            if (null == response.body().getData()) {
                                ToastUtils.showShort(R.string.data_err);
                                return;
                            }

                            if (isRefresh) {
                                mCommentPopRvData.clear();
                                mCommentPopDtoData.clear();
                            }
                            mCommentPopRvData.addAll(response.body().getData().getRecords());
                            mCommentPopDtoData.add(response.body().getData());
                            commentPopRvAdapter.setNewData(mCommentPopRvData);
                            if (mCommentPopDtoData.isEmpty()) {
                                commentTotal.setText("(0)");
                                commentPopCommentTotal.setText("(0)");
                            } else {
                                commentTotal.setText("(" + mCommentPopDtoData.get(0).getTotal() + ")");
                                commentPopCommentTotal.setText("(" + mCommentPopDtoData.get(0).getTotal() + ")");
                            }

                            if (response.body().getData().getRecords().size() == 0) {
                                commentPopRvAdapter.loadMoreEnd();
                            } else {
                                commentPopRvAdapter.loadMoreComplete();
                            }

                        } else {
                            commentPopRvAdapter.loadMoreFail();
                        }

                    }

                    @Override
                    public void onError(Response<CommentModel> response) {
                        commentPopRvAdapter.loadMoreFail();
                    }
                });
    }

    /**
     * 评论
     */
    private void toComment(String content, final String contentId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contentId", contentId);
            jsonObject.put("content", content);
            jsonObject.put("title", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(ApiConstants.getInstance().addComment())
                .tag(VIDEOTAG)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .upJson(jsonObject)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }
                        try {
                            JSONObject mJsonObject = new JSONObject(response.body());
                            String code = mJsonObject.get("code").toString();

                            if (code.equals(success_code)) {
                                if (!mDatas.isEmpty()) {
                                    String jsonString = BuriedPointModelManager.getVideoComment(contentId, mDatas.get(0).getTitle(), "", "",
                                            "", "", mDatas.get(0).getIssueTimeStamp(), Constants.CONTENT_TYPE);
                                    Log.e("埋点", "埋点：评论---" + jsonString);
                                }

                                ToastUtils.showShort("评论已提交，请等待审核通过！");
                                if (null != inputAndSendPop) {
                                    inputAndSendPop.dissmiss();
                                }
                                KeyboardUtils.hideKeyboard(getWindow().getDecorView());
                                mPageIndex = 1;
                                getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), true);
                            } else if (code.equals(token_error)) {
                                Log.e("addComment", "无token 去跳登录");
                                try {
                                    param.toLogin();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (null != mJsonObject.getString("message")) {
                                    ToastUtils.showShort(mJsonObject.getString("message"));
                                } else {
                                    ToastUtils.showShort("评论失败");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShort("评论失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showShort("评论失败");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        edtInput.setText("");
                    }
                });
    }

    /**
     * 获取收藏点赞状态
     */
    public void getContentState(final String contentId) {
        OkGo.<ContentStateModel>get(ApiConstants.getInstance().queryStatsData())
                .tag("contentState")
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .params("contentId", contentId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<ContentStateModel>(ContentStateModel.class) {
                    @Override
                    public void onSuccess(Response<ContentStateModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (response.body().getCode().equals(success_code)) {
                            if (null == response.body().getData()) {
                                ToastUtils.showShort(R.string.data_err);
                                return;
                            }

                            playerView.contentStateModel = response.body().getData();
                            if (null != playerView.contentStateModel) {
                                setLikeCollection(playerView.contentStateModel);
                                if (!mDatas.isEmpty()) {
                                    playerView.setContentStateModel(contentId, mDatas.get(0).getType());
                                }
                            }
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onError(Response<ContentStateModel> response) {
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
                            return;
                        }
                        ToastUtils.showShort(R.string.net_err);
                    }
                });
    }

    public void setLikeCollection(ContentStateModel.DataDTO contentStateModel) {
        if (contentStateModel.getWhetherFavor().equals("true")) {
            videoDetailCollectionImage.setImageResource(R.drawable.collection);
        } else {
            videoDetailCollectionImage.setImageResource(R.drawable.collection_icon);
        }

        collectionNum.setText(NumberFormatTool.formatNum(Long.parseLong(NumberFormatTool.getNumStr(contentStateModel.getFavorCountShow())), false));

        if (contentStateModel.getWhetherLike().equals("true")) {
            videoDetailLikesImage.setImageResource(R.drawable.favourite_select);
        } else {
            videoDetailLikesImage.setImageResource(R.drawable.favourite);
        }

        if (contentStateModel.getLikeCountShow().equals("0")) {
            likesNum.setText("赞");
        } else {
            likesNum.setText(NumberFormatTool.formatNum(Long.parseLong(NumberFormatTool.getNumStr(contentStateModel.getLikeCountShow())), false));
        }
    }

    /**
     * 收藏/取消收藏
     */
    private void addOrCancelFavor(final String contentId, String type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contentId", contentId);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(ApiConstants.getInstance().addOrCancelFavor())
                .tag(VIDEOTAG)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .upJson(jsonObject)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }
                        try {
                            JSONObject json = new JSONObject(response.body());
                            if (json.get("code").toString().equals(success_code)) {
                                if (!mDatas.isEmpty()) {
                                    String jsonString = BuriedPointModelManager.getLikeAndFavorBuriedPointData(contentId, mDatas.get(0).getTitle(),
                                            "", "", "", "", mDatas.get(0).getIssueTimeStamp(),
                                            Constants.CONTENT_TYPE);
                                    Log.e("埋点", "埋点：收藏---" + jsonString);
                                }

                                if (json.get("data").toString().equals("1")) {
                                    int num;
                                    num = Integer.parseInt(NumberFormatTool.getNumStr(collectionNum.getText().toString()));
                                    num++;
                                    collectionNum.setText(NumberFormatTool.formatNum(num, false));
                                    videoDetailCollectionImage.setImageResource(R.drawable.collection);
                                    playerView.contentStateModel.setWhetherFavor("true");
                                    playerView.contentStateModel.setFavorCountShow(NumberFormatTool.formatNum(num, false).toString());
                                } else {
                                    int num;
                                    num = Integer.parseInt(NumberFormatTool.getNumStr(collectionNum.getText().toString()));
                                    if (num > 0) {
                                        num--;
                                    }
                                    collectionNum.setText(NumberFormatTool.formatNum(num, false));
                                    videoDetailCollectionImage.setImageResource(R.drawable.collection_icon);
                                    playerView.contentStateModel.setWhetherFavor("false");
                                    playerView.contentStateModel.setFavorCountShow(NumberFormatTool.formatNum(num, false).toString());
                                }
                                if (null != playerView.contentStateModel) {
                                    if (!mDatas.isEmpty()) {
                                        playerView.setContentStateModel(contentId, mDatas.get(0).getType());
                                    }
                                }
                            } else if (json.get("code").toString().equals(token_error)) {
                                Log.e("addOrCancelFavor", "无token 去跳登录");
                                try {
                                    param.toLogin();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (null != json.get("message").toString()) {
                                    ToastUtils.showShort(json.get("message").toString());
                                } else {
                                    ToastUtils.showShort("收藏失败");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShort("收藏失败");
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("收藏失败");
                    }
                });
    }

    /**
     * 点赞/取消点赞
     */
    private void addOrCancelLike(String targetId, String type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("targetId", targetId);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(ApiConstants.getInstance().addOrCancelLike())
                .tag(VIDEOTAG)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        try {
                            JSONObject json = new JSONObject(response.body());
                            if (null != json && json.get("code").toString().equals("200")) {
                                if (!mDatas.isEmpty()) {
                                    String jsonString = BuriedPointModelManager.getLikeAndFavorBuriedPointData(contentId, mDatas.get(0).getTitle(),
                                            "", "", "", "", mDatas.get(0).getIssueTimeStamp(),
                                            Constants.CONTENT_TYPE);
                                    Log.e("埋点", "埋点：点赞---" + jsonString);
                                }

                                if (json.get("data").toString().equals("1")) {
                                    int num;
                                    videoDetailLikesImage.setImageResource(R.drawable.favourite_select);
                                    num = Integer.parseInt(NumberFormatTool.getNumStr(likesNum.getText().toString()));
                                    num++;
                                    likesNum.setText(NumberFormatTool.formatNum(num, false));
                                    playerView.contentStateModel.setWhetherLike("true");
                                    playerView.contentStateModel.setLikeCountShow(NumberFormatTool.formatNum(num, false).toString());
                                } else {
                                    int num;
                                    videoDetailLikesImage.setImageResource(R.drawable.favourite);
                                    num = Integer.parseInt(NumberFormatTool.getNumStr(likesNum.getText().toString()));
                                    if (num > 0) {
                                        num--;
                                    }
                                    if (num == 0) {
                                        likesNum.setText("赞");
                                    } else {
                                        likesNum.setText(NumberFormatTool.formatNum(num, false));
                                    }
                                    playerView.contentStateModel.setWhetherLike("false");
                                    playerView.contentStateModel.setLikeCountShow(NumberFormatTool.formatNum(num, false).toString());
                                }
                                if (null != playerView.contentStateModel) {
                                    if (!mDatas.isEmpty()) {
                                        playerView.setContentStateModel(contentId, mDatas.get(0).getType());
                                    }
                                }
                            } else if (json.get("code").toString().equals(token_error)) {
                                Log.e("addOrCancelLike", "无token,跳转登录");
                                try {
                                    param.toLogin();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (null != json.get("message").toString()) {
                                    ToastUtils.showShort(json.get("message").toString());
                                } else {
                                    ToastUtils.showShort("点赞失败");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShort("点赞失败");
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        ToastUtils.showShort("点赞失败");
                    }
                });
    }

    /**
     * 浏览量+1
     */
    private void addPageViews(String contentId) {
        OkGo.<String>post(ApiConstants.getInstance().addViews() + contentId)
                .tag(VIDEOTAG)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            Log.e("yqh", jsonObject.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            ToastUtils.showShort(jsonObject.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        } else if (id == R.id.fold_text) {
            if (foldText.getVisibility() == View.VISIBLE) {
                foldText.setVisibility(View.GONE);
                expendText.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.expend_text) {
            if (expendText.getVisibility() == View.VISIBLE) {
                expendText.setVisibility(View.GONE);
                foldText.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.continue_play) {
            if (mDatas.isEmpty()) {
                return;
            }
            SPUtils.getInstance().put(Constants.AGREE_NETWORK, "1");
            for (int i = 0; i < mDatas.size(); i++) {
                mDatas.get(i).setWifi(true);
            }
            agreeNowifiPlay.setVisibility(View.GONE);
            getOneVideo(contentId);
        } else if (id == R.id.superplayer_iv_fullscreen) {
            playerView.mWindowPlayer.mControllerCallback.onSwitchPlayMode(SuperPlayerDef.PlayerMode.FULLSCREEN);
        } else if (id == R.id.video_detail_comment_btn) {
            showCommentPopWindow();
        } else if (id == R.id.video_detail_white_comment_rl) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                try {
                    noLoginTipsPop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                KeyboardUtils.toggleSoftInput(getWindow().getDecorView());
                showInputEdittextAndSend();
            }
        } else if (id == R.id.video_detail_collection) {
            if (mDatas.isEmpty()) {
                return;
            }
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                addOrCancelFavor(contentId, mDatas.get(0).getType());
            }
        } else if (id == R.id.video_detail_likes) {
            if (mDatas.isEmpty()) {
                return;
            }

            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                addOrCancelLike(contentId, mDatas.get(0).getType());
            }
        } else if (id == R.id.share) {
            if (mDatas.isEmpty()) {
                return;
            }
            String jsonString = BuriedPointModelManager.getShareClick(contentId, mDatas.get(0).getTitle(), "",
                    "", "", "", mDatas.get(0).getIssueTimeStamp(), Constants.CONTENT_TYPE, "");
            Log.e("埋点", "埋点：分享按钮---" + jsonString);
            sharePop();
        } else if (id == R.id.share_wx_btn) {
            if (mDatas.isEmpty()) {
                return;
            }
            String jsonString = BuriedPointModelManager.getShareType(contentId, mDatas.get(0).getTitle(), "",
                    "", "", "", mDatas.get(0).getIssueTimeStamp(), Constants.CONTENT_TYPE, Constants.WX_STRING);
            Log.e("埋点", "埋点：分享到微信朋友---" + jsonString);
            toShare(mDatas.get(0), Constants.SHARE_WX);
        } else if (id == R.id.share_circle_btn) {
            if (mDatas.isEmpty()) {
                return;
            }
            String jsonString = BuriedPointModelManager.getShareType(contentId, mDatas.get(0).getTitle(), "",
                    "", "", "", mDatas.get(0).getIssueTimeStamp(), Constants.CONTENT_TYPE, Constants.CIRCLE_STRING);
            Log.e("埋点", "埋点：分享到微信朋友圈---" + jsonString);
            toShare(mDatas.get(0), Constants.SHARE_CIRCLE);
        } else if (id == R.id.share_qq_btn) {
            if (mDatas.isEmpty()) {
                return;
            }
            String jsonString = BuriedPointModelManager.getShareType(contentId, mDatas.get(0).getTitle(), "",
                    "", "", "", mDatas.get(0).getIssueTimeStamp(), Constants.CONTENT_TYPE, Constants.QQ_STRING);
            Log.e("埋点", "埋点：分享到QQ---" + jsonString);
            toShare(mDatas.get(0), Constants.SHARE_QQ);
        } else if (id == R.id.dismiss_pop) {
            if (popupWindow != null) {
                popupWindow.dissmiss();
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
        } else if (id == R.id.comment_pop_rl) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                try {
                    noLoginTipsPop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                KeyboardUtils.toggleSoftInput(getWindow().getDecorView());
                showInputEdittextAndSend();
            }
        } else if (id == R.id.publisher_headimg) {
            if (TextUtils.isEmpty(mDatas.get(0).getIssuerId())) {
                return;
            }
            //跳转H5头像TA人主页
            try {
                if (Utils.mIsDebug) {
                    param.recommendUrl(Constants.HEAD_OTHER + mDatas.get(0).getCreateBy(), null);
                } else {
                    param.recommendUrl(Constants.HEAD_OTHER_ZS + mDatas.get(0).getCreateBy(), null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分享弹窗
     */
    private void sharePop() {
        if (null == sharePop) {
            sharePop = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(sharePopView)
                    .setOutsideTouchable(true)
                    .setFocusable(true)
                    .size(getResources().getDisplayMetrics().widthPixels, ButtonSpan.dip2px(150))
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .create()
                    .showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        } else {
            sharePop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 弹出发送评论弹出窗
     */
    private void showInputEdittextAndSend() {
        //创建并显示popWindow
        if (null == inputAndSendPop) {
            inputAndSendPop = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(sendPopContentView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .size(getResources().getDisplayMetrics().widthPixels, ButtonSpan.dip2px(50))
                    .create()
                    .showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        } else {
            inputAndSendPop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
        edtInput.setFocusable(true);
        edtInput.setFocusableInTouchMode(true);
        edtInput.requestFocus();

    }


    /**
     * 评论列表弹出框
     */
    private void showCommentPopWindow() {
        if (null == popupWindow) {
            //创建并显示popWindow
            popupWindow = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(contentView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                    .size(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels - ButtonSpan.dip2px(200))
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .create()
                    .showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        } else {
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
        popupWindow.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

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
                    .size(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels)
                    .create()
                    .showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            noLoginTipsPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }


    private void addPlayView() {
        if (null == playerView) {
            return;
        }
        if (null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }

        if (TextUtils.isEmpty(mDatas.get(0).getCreatorCertMark())) {
            officialCertificationImg.setVisibility(View.GONE);
        } else {
            officialCertificationImg.setVisibility(View.VISIBLE);
            if (TextUtils.equals(mDatas.get(0).getCreatorCertMark(), BLUE_V)) {
                officialCertificationImg.setImageResource(R.drawable.official_certification);
            } else if (TextUtils.equals(mDatas.get(0).getCreatorCertMark(), YELLOW_V)) {
                officialCertificationImg.setImageResource(R.drawable.yellow_v);
            }
        }

        if (null != VideoDetailActivity.this && !VideoDetailActivity.this.isFinishing() && !VideoDetailActivity.this.isDestroyed()) {
            GlideUtil.displayCircle(publisherHeadimg, mDatas.get(0).getIssuerImageUrl(), true, this);
            //            Glide.with(this)
//                    .load(mDatas.get(0).getIssuerImageUrl())
//                    .into(publisherHeadimg);
            publisherName.setText(mDatas.get(0).getIssuerName());
        }

        getFoldText(mDatas.get(0));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams bottomLp = (LinearLayout.LayoutParams) introduceLin.getLayoutParams();
        RelativeLayout.LayoutParams mLayoutBottomParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String videoType = videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(0).getWidth())),
                Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(0).getHeight())));
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        if (null != playerView.mWindowPlayer && null != playerView.mWindowPlayer.mLayoutBottom && null != playerView.mWindowPlayer.mLayoutBottom.getParent()) {
            ((ViewGroup) playerView.mWindowPlayer.mLayoutBottom.getParent()).removeView(playerView.mWindowPlayer.mLayoutBottom);
        }
        if (videoType.equals("1")) {
            if (phoneIsNormal()) {
                superplayerIvFullscreen.setVisibility(View.GONE);
                playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
                playerView.setOrientation(false);
            } else {
                superplayerIvFullscreen.setVisibility(View.GONE);
                playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                playerView.setOrientation(false);
            }
            if (introduceLin != null) {
                introduceLin.addView(playerView.mWindowPlayer.mLayoutBottom, 0);
            }
        } else if (videoType.equals("0")) {
            superplayerIvFullscreen.setVisibility(View.GONE);
            playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            playerView.setOrientation(false);
            if (introduceLin != null) {
                introduceLin.addView(playerView.mWindowPlayer.mLayoutBottom, 0);
            }
        } else {
            superplayerIvFullscreen.setVisibility(View.VISIBLE);
            playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            playerView.setOrientation(true);
            mLayoutBottomParams.addRule(BELOW, playerView.getId());
            mLayoutBottomParams.setMargins(0, (getResources().getDisplayMetrics().heightPixels / 2) + ButtonSpan.dip2px(120), 0, 0);
            playerView.mWindowPlayer.mLayoutBottom.setLayoutParams(mLayoutBottomParams);
            rootview.addView(playerView.mWindowPlayer.mLayoutBottom);
        }

        introduceLin.setLayoutParams(bottomLp);
        introduceLin.setVisibility(View.VISIBLE);
        RelativeLayout item = new RelativeLayout(this);
        RelativeLayout.LayoutParams itemLp = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        item.addView(playerView, itemLp);
        playerView.setTag(0);
        playerView.setBackgroundColor(getResources().getColor(R.color.video_black));
        rootview.addView(item, 0, lp);
        play(mDatas.get(0).getPlayUrl(), mDatas.get(0).getTitle());
    }

    private void getFoldText(final DataDTO item) {
        String topicNameStr;
        String brief;
        if (TextUtils.isEmpty(item.getBelongTopicName()) || null == item.getBelongTopicName()) {
            topicNameStr = "";
        } else {
            topicNameStr = "#" + item.getBelongTopicName();
        }
        huati.setText(ButtonSpan.subStrByLen(topicNameStr, 24));
        huati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Utils.mIsDebug) {
                        param.recommendUrl(Constants.TOPIC_DETAILS + item.getBelongTopicId(), null);
                    } else {
                        param.recommendUrl(Constants.TOPIC_DETAILS_ZS + item.getBelongTopicId(), null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (TextUtils.isEmpty(item.getBrief())) {
            brief = item.getTitle();
        } else {
            brief = item.getBrief();
        }

        if (TextUtils.isEmpty(brief)) {
            foldText.setVisibility(View.GONE);
        } else {
            foldText.setVisibility(View.VISIBLE);
        }

        if (huati.getText().length() != 0) {
            int num;
            if (huati.getText().length() > 12) {
                num = 12 + 3;
            } else {
                num = huati.getText().length() + 1;
            }

            for (int i = 0; i < num; i++) {
                spaceStr = spaceStr + "\u3000";
                item.setSpaceStr(spaceStr);
            }
            spaceStr = "";
        }

        foldText.setText(item.getSpaceStr() + brief);
        expendText.setText(item.getSpaceStr() + brief);
    }

    /**
     * 播放视频
     *
     * @param playUrl
     */
    public void play(String playUrl, String title) {
        if (null != playerView) {
            SuperPlayerModel model = new SuperPlayerModel();
            model.url = playUrl;
            model.title = title;
            model.contentId = contentId;
            playerView.playWithModel(model);
        }
    }

    /**
     * 视频是否是16：9
     * 0 :  竖版视频非16：9
     * 1 ：  竖版视频16：9
     * 2 ：  横板视频
     */
    private String videoIsNormal(int videoWidth, int videoHeight) {
        if (videoWidth == 0 && videoHeight == 0) {
            return "0";
        }

        if (videoWidth > videoHeight) {
            //横板
            if (videoWidth * 9 == videoHeight * 16) {
                return "2";
            } else {
                return "0";
            }
        } else {
            //竖版
            if (videoHeight * 9 == videoWidth * 16) {
                return "1";
            } else {
                return "0";
            }
        }
    }

    /**
     * 手机是否为16：9
     *
     * @return
     */
    private boolean phoneIsNormal() {
        int phoneWidth = ScreenUtils.getPhoneWidth(this);
        int phoneHeight = ScreenUtils.getPhoneHeight(this);
        if (phoneHeight * 9 == phoneWidth * 16) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取单条视频详情
     */
    private void getOneVideo(final String contentId) {
        videoLoadingProgress.setVisibility(View.VISIBLE);
        OkGo.<String>get(ApiConstants.getInstance().getVideoDetailUrl() + contentId)
                .tag(TAG)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if (jsonObject.get("code").toString().equals(success_code)) {
                                    String json = jsonObject.optJSONObject("data").toString();
                                    if (null == json || TextUtils.isEmpty(json)) {
                                        ToastUtils.showShort(R.string.data_err);
                                        return;
                                    }
                                    DataDTO dataDTO = JSON.parseObject(json, DataDTO.class);
                                    mDatas.add(dataDTO);
                                    if (mDatas.isEmpty()) {
                                        return;
                                    }
                                    setDataWifiState(mDatas, VideoDetailActivity.this);
                                    if (!SPUtils.isVisibleNoWifiView(VideoDetailActivity.this)) {
                                        addPlayView();
                                    }

                                    if (mDatas.get(0).getDisableComment()) {
                                        videoDetailWhiteCommentRl.setEnabled(false);
                                        commentPopRl.setEnabled(false);
                                        commentEdittext.setText("评论关闭");
                                        commentEdtInput.setHint("评论关闭");
                                    } else {
                                        videoDetailWhiteCommentRl.setEnabled(true);
                                        commentPopRl.setEnabled(true);
                                        commentEdittext.setText("写评论...");
                                        commentEdtInput.setHint("写评论...");
                                    }
                                    getContentState(contentId);
                                    getRecommend(contentId, 0);
                                    playerView.mFullScreenPlayer.setDataDTO(mDatas.get(0));
                                } else {
                                    ToastUtils.showShort(jsonObject.get("message").toString());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        try {
                            if (null == response.body()) {
                                ToastUtils.showShort(R.string.net_err);
                            } else {
                                JSONObject jsonObject = new JSONObject(response.body());
                                ToastUtils.showShort(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        videoLoadingProgress.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (playerView == null) {
            return;
        }

        if (playerView.mWindowPlayer.mCurrentPlayState != SuperPlayerDef.PlayerState.END) {
            everyOneDuration = playerView.mWindowPlayer.mProgress - lsDuration;

            //当前节点播放的时长/总视频时长 = 这一次观看的视频进度百分比
            if (mDuration != 0) {
                String event;
                if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
                    //不为重播
                    event = Constants.CMS_VIDEO_OVER_AUTO;
                } else {
                    //重播
                    event = Constants.CMS_VIDEO_OVER;
                }
                double currentPercent = ((double) playerView.mWindowPlayer.mProgress / mDuration);
                double uploadPercent = 0;
                if (((double) playerView.mWindowPlayer.mProgress / mDuration) > maxPercent) {
                    uploadPercent = currentPercent;
                } else {
                    uploadPercent = maxPercent;
                }
                BigDecimal two = new BigDecimal(uploadPercent);
                double pointPercentTwo = two.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                lsDuration = playerView.mWindowPlayer.mProgress;

                //上报埋点
                uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(this, mDatas.get(0).getThirdPartyId(), String.valueOf(everyOneDuration * 1000), String.valueOf(Math.floor(pointPercentTwo * 100)), event), event);
            }
        }
        playerView.mSuperPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SuperPlayerImpl.mCurrentPlayVideoURL = null;
        if (playerView != null) {
            playerView.release();
            playerView.stopPlay();
            playerView.mSuperPlayer.destroy();
            playerView = null;
        }
        maxPercent = 0;
        lsDuration = 0;
    }
}