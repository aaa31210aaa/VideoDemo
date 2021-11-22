package ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.flyco.tablayout.listener.OnTabSelectListener;
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
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import adpter.VideoViewPagerAdapter;
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
    private VideoViewPagerAdapter videoViewPagerAdapter;
    private List<VideoChannelModel> videoChannelModels = new ArrayList<>();
    private List<String> colunmList = new ArrayList<>();
    private LinearLayout backLin;
    private RelativeLayout videoTitleView;
    public VideoDetailFragment videoDetailFragment;
    public XkshFragment xkshFragment;
    private SuperPlayerView playerView;

    private ImageView searchIcon;
    private ImageView personalCenter;

    private TranslateAnimation translateAniLeftShow, translateAniLeftHide;
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    public String contentId;
    private int toCurrentTab;
    private String lsCotentnId;
    public static double maxPercent = 0; //记录最大百分比
    public static long lsDuration = 0; //每一次上报临时保存的播放时长

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SystemUtils.setNavbarColor(this, R.color.video_black);
        setContentView(R.layout.activity_video_main);
        initView();
    }

    private void initView() {
        contentId = getIntent().getStringExtra("contentId");
        toCurrentTab = getIntent().getIntExtra("setCurrentTab", 1);
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

        //全屏进度条监听
        if (null != playerView && null != playerView.mFullScreenPlayer) {
            playerView.mFullScreenPlayer.mSeekBarProgress.setOnSeekBarChangeListener(new PointSeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(PointSeekBar seekBar, int progress, boolean fromUser) {

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
        }

        if (null != playerView && null != playerView.mWindowPlayer) {
            //窗口进度条
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
        }

        /**
         * 监听播放器播放窗口变化回调
         */
        playerView.playModeCallBack = new SuperPlayerView.PlayModeCallBack() {
            @Override
            public void getPlayMode(SuperPlayerDef.PlayerMode playerMode) {
                LinearLayout videoFragmentFullLin = (LinearLayout) videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.superplayer_iv_fullscreen);
                LinearLayout xkshFullLin = (LinearLayout) xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.superplayer_iv_fullscreen);
                if (playerMode.equals(SuperPlayerDef.PlayerMode.FULLSCREEN)) {
                    if (videoDetailFragment.videoFragmentIsVisibleToUser) {
                        videoDetailFragment.videoDetailmanager.setCanScoll(false);
                        videoDetailFragment.refreshLayout.setEnableRefresh(false);
                        videoDetailFragment.adapter.setEnableLoadMore(false);
                        if (null != videoDetailFragment.popupWindow) {
                            videoDetailFragment.popupWindow.dissmiss();
                        }

                        if (null != videoDetailFragment.inputAndSendPop) {
                            videoDetailFragment.inputAndSendPop.dissmiss();
                        }

                        if (null != videoDetailFragment.choosePop) {
                            videoDetailFragment.choosePop.dissmiss();
                        }

                        if (null != videoDetailFragment.noLoginTipsPop) {
                            videoDetailFragment.noLoginTipsPop.dissmiss();
                        }

                        if (null != videoDetailFragment.sharePop) {
                            videoDetailFragment.sharePop.dissmiss();
                        }
                        if (null != videoDetailFragment.videoDetailCommentBtn) {
                            videoDetailFragment.videoDetailCommentBtn.setVisibility(View.GONE);
                        }

                        if (null != videoTitleView) {
                            videoTitleView.setVisibility(View.GONE);
                        }
                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin).setVisibility(View.GONE);
                        }

                        if (null != videoFragmentFullLin) {
                            videoFragmentFullLin.setVisibility(View.GONE);
                        }

                        videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.GONE);
                        videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex,R.id.cover_picture).setVisibility(View.GONE);
                    } else if (xkshFragment.mIsVisibleToUser) {
                        xkshFragment.xkshManager.setCanScoll(false);
                        xkshFragment.refreshLayout.setEnableRefresh(false);
                        xkshFragment.adapter.setEnableLoadMore(false);

                        if (null != xkshFragment.popupWindow) {
                            xkshFragment.popupWindow.dissmiss();
                        }


                        if (null != xkshFragment.inputAndSendPop) {
                            xkshFragment.inputAndSendPop.dissmiss();
                        }

                        if (null != xkshFragment.choosePop) {
                            xkshFragment.choosePop.dissmiss();
                        }

                        if (null != xkshFragment.noLoginTipsPop) {
                            xkshFragment.noLoginTipsPop.dissmiss();
                        }

                        if (null != xkshFragment.sharePop) {
                            xkshFragment.sharePop.dissmiss();
                        }

                        if (null != xkshFragment.videoDetailCommentBtn) {
                            xkshFragment.videoDetailCommentBtn.setVisibility(View.GONE);
                        }

                        if (null != videoTitleView) {
                            videoTitleView.setVisibility(View.GONE);
                        }
                        if (null != xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.introduce_lin)) {
                            xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.introduce_lin).setVisibility(View.GONE);
                        }

                        if (null != xkshFullLin) {
                            xkshFullLin.setVisibility(View.GONE);
                        }
                        if (null != xkshFragment.rankList) {
                            xkshFragment.rankList.setVisibility(View.GONE);
                        }

                        if (xkshFragment.isAbbreviation) {
                            if (null != xkshFragment.activityRuleAbbreviation) {
                                xkshFragment.activityRuleAbbreviation.setVisibility(View.GONE);
                            }
                        } else {
                            if (null != xkshFragment.activityRuleImg) {
                                xkshFragment.activityRuleImg.setVisibility(View.GONE);
                            }
                            if (null != xkshFragment.activityToAbbreviation) {
                                xkshFragment.activityToAbbreviation.setVisibility(View.GONE);
                            }
                        }
                        xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.GONE);
                        xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex,R.id.cover_picture).setVisibility(View.GONE);
                    }

                    KeyboardUtils.hideKeyboard(getWindow().getDecorView());
                    videoTab.setVisibility(View.GONE);
                    videoVp.setScroll(false);


                } else if (playerMode.equals(SuperPlayerDef.PlayerMode.WINDOW)) {
                    if (videoDetailFragment.videoFragmentIsVisibleToUser) {
                        videoDetailFragment.videoDetailmanager.setCanScoll(true);
                        videoDetailFragment.refreshLayout.setEnableRefresh(true);
                        videoDetailFragment.adapter.setEnableLoadMore(true);
                        videoDetailFragment.setLikeCollection(playerView.contentStateModel);
                        if (null != videoDetailFragment.videoDetailCommentBtn) {
                            videoDetailFragment.videoDetailCommentBtn.setVisibility(View.VISIBLE);
                        }
                        if (null != videoFragmentFullLin) {
                            videoFragmentFullLin.setVisibility(View.VISIBLE);
                        }

                        if (null != videoTitleView) {
                            videoTitleView.setVisibility(View.VISIBLE);
                        }
                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin).setVisibility(View.VISIBLE);
                        }
                        videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.VISIBLE);
                        videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex,R.id.cover_picture).setVisibility(View.VISIBLE);
                    } else if (xkshFragment.mIsVisibleToUser) {
                        xkshFragment.xkshManager.setCanScoll(true);
                        xkshFragment.refreshLayout.setEnableRefresh(true);
                        xkshFragment.adapter.setEnableLoadMore(true);
                        xkshFragment.setLikeCollection(playerView.contentStateModel);
                        if (null != xkshFragment.videoDetailCommentBtn) {
                            xkshFragment.videoDetailCommentBtn.setVisibility(View.VISIBLE);
                        }

                        if (null != xkshFullLin) {
                            xkshFullLin.setVisibility(View.VISIBLE);
                        }

                        if (null != xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.introduce_lin)) {
                            xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.introduce_lin).setVisibility(View.VISIBLE);
                        }
                        if (null != xkshFragment.rankList) {
                            xkshFragment.rankList.setVisibility(View.VISIBLE);
                        }

                        if (xkshFragment.isAbbreviation) {
                            if (null != xkshFragment.activityRuleAbbreviation) {
                                xkshFragment.activityRuleAbbreviation.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (null != xkshFragment.activityRuleBean.getData().getConfig().getJumpUrl()
                                    && !TextUtils.isEmpty(xkshFragment.activityRuleBean.getData().getConfig().getJumpUrl())) {
                                if (null != xkshFragment.activityRuleImg) {
                                    xkshFragment.activityRuleImg.setVisibility(View.VISIBLE);
                                }
                                if (null != xkshFragment.activityToAbbreviation) {
                                    xkshFragment.activityToAbbreviation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.VISIBLE);
                        xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex,R.id.cover_picture).setVisibility(View.VISIBLE);
                    }
                    if (null != videoTitleView) {
                        videoTitleView.setVisibility(View.VISIBLE);
                    }
                    videoTab.setVisibility(View.VISIBLE);
                    videoVp.setScroll(true);
                }
            }
        };

//        //窗口重播按钮
//        playerView.mWindowPlayer.setIsReplayClick(new WindowPlayer.IsReplayClick() {
//            @Override
//            public void getReplayClick() {
//                if (xkshFragment.mIsVisibleToUser) {
//                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, xkshFragment.mDataDTO.getThirdPartyId(), "", "", Constants.CMS_VIDEO_PLAY), Constants.CMS_VIDEO_PLAY);
//                } else if (videoDetailFragment.videoFragmentIsVisibleToUser) {
//                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, videoDetailFragment.mDataDTO.getThirdPartyId(), "", "", Constants.CMS_VIDEO_PLAY), Constants.CMS_VIDEO_PLAY);
//                }
//            }
//        });

//        //全屏重播按钮
//        playerView.mFullScreenPlayer.setFullIsReplayClick(new FullScreenPlayer.FullIsReplayClick() {
//            @Override
//            public void getFullReplayClick() {
//                if (xkshFragment.mIsVisibleToUser) {
//                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, xkshFragment.mDataDTO.getThirdPartyId(), "", "", Constants.CMS_VIDEO_PLAY), Constants.CMS_VIDEO_PLAY);
//                } else if (videoDetailFragment.videoFragmentIsVisibleToUser) {
//                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, videoDetailFragment.mDataDTO.getThirdPartyId(), "", "", Constants.CMS_VIDEO_PLAY), Constants.CMS_VIDEO_PLAY);
//                }
//            }
//        });

        //开始播放回调
        SuperPlayerImpl.setReadPlayCallBack(new SuperPlayerImpl.ReadPlayCallBack() {
            @Override
            public void ReadPlayCallback() {
                if (xkshFragment.mIsVisibleToUser) {
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, xkshFragment.mDataDTO.getThirdPartyId(), "", "", Constants.CMS_VIDEO_PLAY_AUTO), Constants.CMS_VIDEO_PLAY_AUTO);
                } else if (videoDetailFragment.videoFragmentIsVisibleToUser) {
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, videoDetailFragment.mDataDTO.getThirdPartyId(), "", "", Constants.CMS_VIDEO_PLAY_AUTO), Constants.CMS_VIDEO_PLAY_AUTO);
                }
            }
        });

        //自动播放/拖动进度条 播放结束回调
        SuperPlayerImpl.setAutoPlayOverCallBack(new SuperPlayerImpl.AutoPlayOverCallBack() {
            @Override
            public void AutoPlayOverCallBack() {

                final String event;
                if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
                    //不为重播
                    event = Constants.CMS_VIDEO_OVER_AUTO;
                    playerView.mWindowPlayer.setRecordDuration(mDuration);
                } else {
                    //重播
                    event = Constants.CMS_VIDEO_OVER;
                }

                //拖动/自动播放结束上报埋点
                try {
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoHomeActivity.this, lsCotentnId, String.valueOf(mDuration * 1000), "100", event), event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                maxPercent = 100;


                if (videoDetailFragment.videoFragmentIsVisibleToUser) {
                    lsCotentnId = videoDetailFragment.mDataDTO.getThirdPartyId();
                } else if (xkshFragment.mIsVisibleToUser) {
                    lsCotentnId = xkshFragment.mDataDTO.getThirdPartyId();
                }
                playerView.mSuperPlayer.reStart();
            }
        });

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
        if (null == videoViewPagerAdapter) {
            videoViewPagerAdapter = new VideoViewPagerAdapter(getSupportFragmentManager());
        }
        videoVp.setAdapter(videoViewPagerAdapter);

        videoVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position && null != videoTab) {
                    videoTab.hideMsg(position);
                    playerView.setOrientation(true);
                    //滑动切换到小康生活  事件名：short_video_home_click
                    //属性名：button_name
                } else if (1 == position) {
                    playerView.setOrientation(true);
                    //滑动切换到小康生活  事件名：short_video_home_click
                    //属性名：button_name
                } else if (2 == position) {
                    playerView.mSuperPlayer.pause();
                    //切换到直播的时候  不允许旋转
                    playerView.setOrientation(false);
                    //滑动切换到小康生活  事件名：short_video_home_click
                    //属性名：button_name
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
        videoViewPagerAdapter.addItems(videoChannelModels, videoTab, playerView, contentId);
        for (VideoChannelModel channelBean : videoChannelModels) {
            colunmList.add(channelBean.getColumnBean().getColumnName());
        }
        String[] titles = colunmList.toArray(new String[colunmList.size()]);
        //tab和ViewPager进行关联
        videoTab.setViewPager(videoVp, titles);
        videoTab.setCurrentTab(toCurrentTab);
        videoTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (0 == position) {
                    //点击切换到小康生活  事件名：short_video_home_click
                    //属性名：button_name
                } else if (1 == position) {
                    //点击切换到视频 事件名：short_video_home_click
                    //属性名：button_name
                } else {
                    //点击切换到视频首页  事件名：short_video_home_click
                    //属性名：button_name
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        videoDetailFragment = (VideoDetailFragment) videoViewPagerAdapter.getItem(1);
        xkshFragment = (XkshFragment) videoViewPagerAdapter.getItem(0);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back_lin) {
            finish();
        } else if (id == R.id.search_icon) {
            //跳转H5搜索
            try {
                if (Utils.mIsDebug) {
                    param.recommendUrl(Constants.SEARCHPLUS, null);
                } else {
                    param.recommendUrl(Constants.SEARCHPLUS_ZS, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.personal_center) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                //跳转H5个人中心
                try {
                    if (Utils.mIsDebug) {
                        param.recommendUrl(Constants.PERSONAL_CENTER, null);
                    } else {
                        param.recommendUrl(Constants.PERSONAL_CENTER_ZS, null);
                    }
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
//            instance = null;
            OkGo.getInstance().cancelAll();
        }
        maxPercent = 0;
        lsDuration = 0;
//        OkGo.getInstance().cancelTag(VIDEOTAG);
    }

    /**
     * 上报埋点
     *
     * @param jsonObject
     */
    public static void uploadBuriedPoint(JSONObject jsonObject, final String trackingType) {
        if (null == jsonObject) {
            return;
        }
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