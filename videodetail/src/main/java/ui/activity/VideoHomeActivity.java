package ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhouwei.library.CustomPopWindow;
import com.flyco.tablayout.SlidingTabLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.contants.Contants;
import com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl;
import com.tencent.liteav.demo.superplayer.model.utils.SystemUtils;
import com.tencent.liteav.demo.superplayer.ui.player.FullScreenPlayer;
import com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer;
import com.tencent.liteav.demo.superplayer.ui.view.PointSeekBar;
import com.wdcs.callback.JsonCallback;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.manager.ContentBuriedPointManager;
import com.wdcs.model.ColumnModel;
import com.wdcs.model.TrackingUploadModel;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.KeyboardUtils;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.ToastUtils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import adpter.VideoViewPagerAdapter;
import model.bean.ActivityRuleBean;
import ui.fragment.VideoDetailFragment;
import ui.fragment.XkshFragment;

import com.wdcs.utils.NoScrollViewPager;

import org.json.JSONObject;

import static com.tencent.liteav.demo.superplayer.SuperPlayerView.instance;
import static com.tencent.liteav.demo.superplayer.SuperPlayerView.mTargetPlayerMode;
import static com.tencent.liteav.demo.superplayer.ui.player.AbsPlayer.formattedTime;
import static com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer.mDuration;
import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.TRACKINGUPLOAD;
import static com.wdcs.constants.Constants.VIDEOTAG;
import static com.wdcs.constants.Constants.success_code;

public class VideoHomeActivity extends AppCompatActivity implements View.OnClickListener {
    public SlidingTabLayout videoTab;
    public NoScrollViewPager videoVp;
    private String[] mTitlesArrays = {"我的小康", "视频", "直播"};
    private VideoViewPagerAdapter adapter;
    private List<VideoChannelModel> videoChannelModels = new ArrayList<>();
    private List<String> colunmList = new ArrayList<>();
    private String panelCode = "";
    private LinearLayout backLin;
    private RelativeLayout videoTitleView;
    private VideoDetailFragment videoDetailFragment;
    private XkshFragment xkshFragment;
    private SuperPlayerView playerView;

    private ImageView searchIcon;
    private ImageView personalCenter;
    private ActivityRuleBean activityRuleBean;
    private boolean isAbbreviation; //当前是否是缩略图
    private TranslateAnimation translateAniLeftShow, translateAniLeftHide;
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SystemUtils.setNavbarColor(this, R.color.video_black);
        setContentView(R.layout.activity_video_main);
        initView();
    }

    private void initView() {
        panelCode = getIntent().getStringExtra("panelCode");
        backLin = findViewById(R.id.back_lin);
        backLin.setOnClickListener(this);
        videoTab = findViewById(R.id.video_tab);
        videoVp = findViewById(R.id.video_vp);
        videoTitleView = findViewById(R.id.video_title_view);
        searchIcon = findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(this);
        personalCenter = findViewById(R.id.personal_center);
        personalCenter.setOnClickListener(this);
        noLoginTipsView = View.inflate(this, R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);

        playerView = SuperPlayerView.getInstance(this, getWindow().getDecorView(), true);
        initViewPager();
        initViewPagerData();
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
                videoVp.setScroll(false);
                videoDetailFragment.videoDetailmanager.setCanScoll(false);
                xkshFragment.xkshManager.setCanScoll(false);
                Log.e("Touch", "onStartTrackingTouch------");
            }

            @Override
            public void onStopTrackingTouch(PointSeekBar seekBar) {
                int curProgress = seekBar.getProgress();
                int maxProgress = seekBar.getMax();
                Log.e("Touch", "onStopTrackingTouch------");
                if (mTargetPlayerMode == SuperPlayerDef.PlayerMode.WINDOW) {
                    videoVp.setScroll(true);
                    videoDetailFragment.videoDetailmanager.setCanScoll(true);
                    xkshFragment.xkshManager.setCanScoll(true);
                } else {
                    videoVp.setScroll(false);
                    videoDetailFragment.videoDetailmanager.setCanScoll(false);
                    xkshFragment.xkshManager.setCanScoll(false);
                }

                switch (playerView.mWindowPlayer.mPlayType) {
                    case VOD:
                        if (curProgress >= 0 && curProgress <= maxProgress) {
                            // 关闭重播按钮
                            playerView.mWindowPlayer.toggleView(playerView.mWindowPlayer.mLayoutReplay, false);
                            float percentage = ((float) curProgress) / maxProgress;
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

        /**
         * 监听播放器播放窗口变化回调
         */
        playerView.playModeCallBack = new SuperPlayerView.PlayModeCallBack() {
            @Override
            public void getPlayMode(SuperPlayerDef.PlayerMode playerMode) {
                LinearLayout videoFragmentFullLin = (LinearLayout) videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.superplayer_iv_fullscreen);
                LinearLayout xkshFullLin = (LinearLayout) xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.superplayer_iv_fullscreen);
                if (playerMode.equals(SuperPlayerDef.PlayerMode.FULLSCREEN)) {
                    videoDetailFragment.videoDetailmanager.setCanScoll(false);
                    videoDetailFragment.refreshLayout.setEnableRefresh(false);
                    xkshFragment.xkshManager.setCanScoll(false);
                    xkshFragment.refreshLayout.setEnableRefresh(false);
                    videoDetailFragment.adapter.setEnableLoadMore(false);
                    xkshFragment.adapter.setEnableLoadMore(false);

                    if (null != videoDetailFragment.popupWindow) {
                        videoDetailFragment.popupWindow.dissmiss();
                    }
                    if (null != xkshFragment.popupWindow) {
                        xkshFragment.popupWindow.dissmiss();
                    }

                    if (null != videoDetailFragment.inputAndSendPop) {
                        videoDetailFragment.inputAndSendPop.dissmiss();
                    }
                    if (null != xkshFragment.inputAndSendPop) {
                        xkshFragment.inputAndSendPop.dissmiss();
                    }

                    if (null != videoDetailFragment.choosePop) {
                        videoDetailFragment.choosePop.dissmiss();
                    }
                    if (null != xkshFragment.choosePop) {
                        xkshFragment.choosePop.dissmiss();
                    }

                    if (null != videoDetailFragment.noLoginTipsPop) {
                        videoDetailFragment.noLoginTipsPop.dissmiss();
                    }
                    if (null != xkshFragment.noLoginTipsPop) {
                        xkshFragment.noLoginTipsPop.dissmiss();
                    }

                    if (null != videoDetailFragment.sharePop) {
                        videoDetailFragment.sharePop.dissmiss();
                    }
                    if (null != xkshFragment.sharePop) {
                        xkshFragment.sharePop.dissmiss();
                    }

                    KeyboardUtils.hideKeyboard(getWindow().getDecorView());
                    videoTab.setVisibility(View.GONE);
                    if (null != videoDetailFragment.videoDetailCommentBtn) {
                        videoDetailFragment.videoDetailCommentBtn.setVisibility(View.GONE);
                    }
                    if (null != xkshFragment.videoDetailCommentBtn) {
                        xkshFragment.videoDetailCommentBtn.setVisibility(View.GONE);
                    }
                    if (null != videoDetailFragment.mVideoTitleView) {
                        videoDetailFragment.mVideoTitleView.setVisibility(View.GONE);
                    }
                    if (null != xkshFragment.mVideoTitleView) {
                        xkshFragment.mVideoTitleView.setVisibility(View.GONE);
                    }
                    videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin).setVisibility(View.GONE);
                    xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.introduce_lin).setVisibility(View.GONE);
                    videoVp.setScroll(false);

                    if (null != videoFragmentFullLin) {
                        videoFragmentFullLin.setVisibility(View.GONE);
                    }

                    if (null != xkshFullLin) {
                        xkshFullLin.setVisibility(View.GONE);
                    }
                    xkshFragment.rankList.setVisibility(View.GONE);
                    if (isAbbreviation) {
                        xkshFragment.activityRuleAbbreviation.setVisibility(View.GONE);
                    } else {
                        xkshFragment.activityRuleImg.setVisibility(View.GONE);
                        xkshFragment.activityToAbbreviation.setVisibility(View.GONE);
                    }

                } else if (playerMode.equals(SuperPlayerDef.PlayerMode.WINDOW)) {
                    videoDetailFragment.videoDetailmanager.setCanScoll(true);
                    videoDetailFragment.refreshLayout.setEnableRefresh(true);
                    xkshFragment.xkshManager.setCanScoll(true);
                    xkshFragment.refreshLayout.setEnableRefresh(true);
                    videoDetailFragment.adapter.setEnableLoadMore(true);
                    xkshFragment.adapter.setEnableLoadMore(true);

                    videoDetailFragment.setLikeCollection(playerView.contentStateModel);
                    xkshFragment.setLikeCollection(playerView.contentStateModel);

                    videoTab.setVisibility(View.VISIBLE);
                    if (null != videoDetailFragment.videoDetailCommentBtn) {
                        videoDetailFragment.videoDetailCommentBtn.setVisibility(View.VISIBLE);
                    }
                    if (null != xkshFragment.videoDetailCommentBtn) {
                        xkshFragment.videoDetailCommentBtn.setVisibility(View.VISIBLE);
                    }
                    if (null != videoDetailFragment.mVideoTitleView) {
                        videoDetailFragment.mVideoTitleView.setVisibility(View.VISIBLE);
                    }
                    if (null != xkshFragment.mVideoTitleView) {
                        xkshFragment.mVideoTitleView.setVisibility(View.VISIBLE);
                    }
                    if (null != videoFragmentFullLin) {
                        videoFragmentFullLin.setVisibility(View.VISIBLE);
                    }
                    if (null != xkshFullLin) {
                        xkshFullLin.setVisibility(View.VISIBLE);
                    }

                    videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin).setVisibility(View.VISIBLE);
                    xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.introduce_lin).setVisibility(View.VISIBLE);
                    videoVp.setScroll(true);
                    xkshFragment.rankList.setVisibility(View.VISIBLE);
                    if (isAbbreviation) {
                        xkshFragment.activityRuleAbbreviation.setVisibility(View.VISIBLE);
                    } else {
                        xkshFragment.activityRuleImg.setVisibility(View.VISIBLE);
                        xkshFragment.activityToAbbreviation.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        playerView.mWindowPlayer.isReplayClick = new WindowPlayer.IsReplayClick() {
            @Override
            public void getReplayClick() {
                if (xkshFragment.mIsVisibleToUser) {
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, xkshFragment.myContentId,"","",Constants.CMS_VIDEO_PLAY),Constants.CMS_VIDEO_PLAY);
                } else if (videoDetailFragment.videoFragmentIsVisibleToUser) {
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, videoDetailFragment.myContentId,"","",Constants.CMS_VIDEO_PLAY),Constants.CMS_VIDEO_PLAY);
                }

            }
        };

        playerView.mFullScreenPlayer.isReplayClick = new FullScreenPlayer.FullIsReplayClick() {
            @Override
            public void getFullReplayClick() {
                if (xkshFragment.mIsVisibleToUser) {
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, xkshFragment.myContentId,"","",Constants.CMS_VIDEO_PLAY),Constants.CMS_VIDEO_PLAY);
                } else if (videoDetailFragment.videoFragmentIsVisibleToUser) {
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, videoDetailFragment.myContentId,"","",Constants.CMS_VIDEO_PLAY),Constants.CMS_VIDEO_PLAY);
                }
            }
        };

        SuperPlayerImpl.readPlayCallBack = new SuperPlayerImpl.ReadPlayCallBack() {
            @Override
            public void ReadPlayCallback() {
                if (xkshFragment.mIsVisibleToUser) {
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, xkshFragment.myContentId,"","",Constants.CMS_VIDEO_PLAY_AUTO),Constants.CMS_VIDEO_PLAY_AUTO);
                } else if (videoDetailFragment.videoFragmentIsVisibleToUser) {
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, videoDetailFragment.myContentId,"","",Constants.CMS_VIDEO_PLAY_AUTO),Constants.CMS_VIDEO_PLAY_AUTO);
                }
            }
        };

        translateAnimation();
    }

    //位移动画
    private void translateAnimation() {
        //向左位移显示动画
        translateAniLeftShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF表示操作自身
                0,//fromXValue表示开始的X轴位置
                Animation.RELATIVE_TO_SELF,
                1,//fromXValue表示结束的X轴位置
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue表示开始的Y轴位置
                Animation.RELATIVE_TO_SELF,
                0);//fromXValue表示结束的Y轴位置
        translateAniLeftShow.setRepeatMode(Animation.REVERSE);
        translateAniLeftShow.setDuration(500);

        //向左位移隐藏动画
        translateAniLeftHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF表示操作自身
                1,//fromXValue表示开始的X轴位置
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue表示结束的X轴位置
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue表示开始的Y轴位置
                Animation.RELATIVE_TO_SELF,
                0);//fromXValue表示结束的Y轴位置
        translateAniLeftHide.setRepeatMode(Animation.REVERSE);
        translateAniLeftHide.setDuration(500);
    }

    private void initViewPager() {
        videoVp.setOffscreenPageLimit(3);
        if (null == adapter) {
            adapter = new VideoViewPagerAdapter(getSupportFragmentManager());
        }
        videoVp.setAdapter(adapter);

        videoVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position && null != videoTab) {
                    videoTab.hideMsg(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViewPagerData() {
        for (int i = 0; i < mTitlesArrays.length; i++) {
            VideoChannelModel model = new VideoChannelModel();
            ColumnModel columnModel = new ColumnModel();
            columnModel.setColumnId(i + "");
            if (i == 0) {
                columnModel.setColumnName("我的小康生活");
                columnModel.setColumnType("0");
                columnModel.setPanelCode("xksh.works");
            } else if (i == 1) {
                columnModel.setColumnName("视频");
                columnModel.setColumnType("1");
                columnModel.setPanelCode("mycs.video.video");
            } else if (i == 2) {
                columnModel.setColumnName("直播");
                columnModel.setColumnType("0");
                columnModel.setPanelCode("mycs.live.livelist");
            }
            model.setColumnBean(columnModel);
            videoChannelModels.add(model);
        }
        adapter.addItems(videoChannelModels, videoTab, videoTitleView, playerView);
        for (VideoChannelModel channelBean : videoChannelModels) {
            colunmList.add(channelBean.getColumnBean().getColumnName());
        }
        String[] titles = colunmList.toArray(new String[colunmList.size()]);
        //tab和ViewPager进行关联
        videoTab.setViewPager(videoVp, titles);
        videoTab.setCurrentTab(1);

        videoDetailFragment = (VideoDetailFragment) adapter.getItem(1);
        xkshFragment = (XkshFragment) adapter.getItem(0);

        getActivityRule();
    }

    /**
     * 获取活动规则
     */
    private void getActivityRule() {
        OkGo.<ActivityRuleBean>get(ApiConstants.getInstance().getActivityRule())
                .tag(VIDEOTAG)
                .params("panelCode", "activity.xksh.link")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<ActivityRuleBean>() {
                    @Override
                    public void onSuccess(Response<ActivityRuleBean> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (response.body().getCode().equals(success_code)) {
                            activityRuleBean = response.body();
                            if (null == activityRuleBean) {
                                return;
                            }
                            videoTab.showMsg(0, "有活动");
                            videoTab.setMsgMargin(0, 0, ButtonSpan.dip2px(10));
                            /**
                             * 设置活动规则图，缩略图
                             */
                            Glide.with(VideoHomeActivity.this)
                                    .load(activityRuleBean.getData().getConfig().getImageUrl())
                                    .into(xkshFragment.activityRuleImg);
                            Glide.with(VideoHomeActivity.this)
                                    .load(activityRuleBean.getData().getConfig().getBackgroundImageUrl())
                                    .into(xkshFragment.activityRuleAbbreviation);
                            /**
                             * 活动规则图点击 跳转活动链接
                             */
                            xkshFragment.activityRuleImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        param.recommendUrl(activityRuleBean.getData().getConfig().getJumpUrl());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            /**
                             * 变成缩略图
                             */
                            xkshFragment.activityToAbbreviation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    xkshFragment.activityRuleImg.setVisibility(View.GONE);
                                    xkshFragment.activityRuleAbbreviation.setVisibility(View.VISIBLE);
                                    isAbbreviation = true;
                                }
                            });

                            /**
                             * 点击展示完整活动图
                             */
                            xkshFragment.activityRuleAbbreviation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    xkshFragment.activityRuleImg.setVisibility(View.VISIBLE);
                                    xkshFragment.activityRuleAbbreviation.setVisibility(View.GONE);
                                    isAbbreviation = false;
                                }
                            });

                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onError(Response<ActivityRuleBean> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back_lin) {
            finish();
        } else if (id == R.id.search_icon) {
            //跳转H5搜索
            try {
                param.recommendUrl(Constants.SEARCHPLUS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.personal_center) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                //跳转H5个人中心
                try {
                    param.recommendUrl(Constants.PERSONAL_CENTER);
                } catch (Exception e) {
                    e.printStackTrace();
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SuperPlayerImpl.mCurrentPlayVideoURL = null;
        if (instance != null) {
            instance.release();
            instance.mSuperPlayer.destroy();
            instance = null;
        }
//        OkGo.getInstance().cancelTag(VIDEOTAG);
    }

    /**
     * 上报埋点
     *
     * @param jsonObject
     */
    public static void uploadBuriedPoint(JSONObject jsonObject, final String trackingType) {
        OkGo.<TrackingUploadModel>post(ApiConstants.getInstance().trackingUpload())
                .tag(TRACKINGUPLOAD)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .upJson(jsonObject)
                .execute(new JsonCallback<TrackingUploadModel>() {
                    @Override
                    public void onSuccess(Response<TrackingUploadModel> response) {
                        Log.e("uploadBuriedPoint", response.body() + "埋点类型===" + trackingType);
                    }

                    @Override
                    public void onError(Response<TrackingUploadModel> response) {
                        super.onError(response);
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }
                        ToastUtils.showShort(response.body().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
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
}