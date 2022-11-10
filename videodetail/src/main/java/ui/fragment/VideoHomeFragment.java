package ui.fragment;

import static com.tencent.liteav.demo.superplayer.SuperPlayerView.instance;
import static com.tencent.liteav.demo.superplayer.SuperPlayerView.mTargetPlayerMode;
import static com.tencent.liteav.demo.superplayer.ui.player.AbsPlayer.formattedTime;
import static com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer.mDuration;
import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.CATEGORYNAME;
import static com.wdcs.constants.Constants.CONTENTID;
import static com.wdcs.constants.Constants.MODULE_SOURCE;
import static com.wdcs.constants.Constants.PANELCODE;
import static com.wdcs.constants.Constants.TABNAME;
import static com.wdcs.constants.Constants.TOCURRENTTAB;
import static com.wdcs.constants.Constants.VIDEOTAG;
import static com.wdcs.constants.Constants.success_code;

import static ui.activity.VideoHomeActivity.uploadBuriedPoint;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.contants.Contants;
import com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl;
import com.tencent.liteav.demo.superplayer.ui.view.PointSeekBar;
import com.wdcs.callback.JsonCallback;
import com.wdcs.callback.VideoFullAndWindowParam;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.manager.ContentBuriedPointManager;
import com.wdcs.manager.FinderBuriedPointManager;
import com.wdcs.model.CategoryModel;
import com.wdcs.model.ColumnModel;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.utils.DateUtils;
import com.wdcs.utils.KeyboardUtils;
import com.wdcs.utils.NetworkUtil;
import com.wdcs.utils.NoScrollViewPager;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.SPUtils;
import com.wdcs.utils.ToastUtils;
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import adpter.VideoViewPagerAdapter;
import ui.activity.VideoHomeActivity;
import widget.LiveDataParam;
import widget.NetBroadcastReceiver;

public class VideoHomeFragment extends Fragment implements View.OnClickListener {

    private View view;

    public SlidingTabLayout videoTab;
    public NoScrollViewPager videoVp;
    private String[] mTitlesArrays;
    private VideoViewPagerAdapter videoViewPagerAdapter;
    private List<VideoChannelModel> videoChannelModels = new ArrayList<>();
    private List<String> colunmList = new ArrayList<>();
    private LinearLayout backLin;
    private RelativeLayout videoTitleView;
    public VideoDetailFragment videoDetailFragment;
    public XkshFragment xkshFragment;
    public static SuperPlayerView playerView;

    private ImageView searchIcon;
    private ImageView personalCenter;

    private TranslateAnimation translateAniLeftShow, translateAniLeftHide;
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    public String contentId;
    private int toCurrentTab = 1;
    private String lsCotentnId;
    public static double maxPercent = 0; //记录最大百分比
    public static long lsDuration = 0; //每一次上报临时保存的播放时长
    private NetBroadcastReceiver netWorkStateReceiver;
    private String categoryName;
    public static boolean isPause;
    private List<CategoryModel.DataDTO> categoryModelList = new ArrayList<>();
    private boolean toFirst = true;
    public String module_source;
    private static Bundle args;
    //    private TextView test;
//    public int videoHomeFragmentVisible; //0 不显示， 1 显示
    private Handler mHandler = new Handler();
    private RelativeLayout topZzc;

    public VideoHomeFragment() {

    }

    public static VideoHomeFragment newInstance(String contentId, int toCurrentTab, String category_name, String module_source) {
        VideoHomeFragment fragment = new VideoHomeFragment();
        args = new Bundle();
        args.putString(CONTENTID, contentId);
        args.putInt(TOCURRENTTAB, toCurrentTab);
        if (!TextUtils.isEmpty(category_name)) {
            args.putString(CATEGORYNAME, category_name);
        }
        if (!TextUtils.isEmpty(module_source)) {
            args.putString(MODULE_SOURCE, module_source);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contentId = getArguments().getString(CONTENTID);
            toCurrentTab = getArguments().getInt(TOCURRENTTAB);
            categoryName = getArguments().getString(CATEGORYNAME);
            module_source = getArguments().getString(MODULE_SOURCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_home, container, false);
        initView();
        return view;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            videoHomeFragmentVisible = 1;
//        } else {
//            videoHomeFragmentVisible = 0;
//        }
//        if (null != videoDetailFragment && !initSetVisible) {
//            videoDetailFragment.setVideoDetailFragmentVisible(videoHomeFragmentVisible);
//        }
//
//        if (null != xkshFragment && !initSetVisible) {
//            xkshFragment.setXkshFragmentVisible(videoHomeFragmentVisible);
//        }
//    }

    private void initView() {
        topZzc = view.findViewById(R.id.top_zzc);
        videoTab = view.findViewById(R.id.video_tab);
        videoVp = view.findViewById(R.id.video_vp);
        videoTitleView = view.findViewById(R.id.video_title_view);
        searchIcon = view.findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(this);
        personalCenter = view.findViewById(R.id.personal_center);
        personalCenter.setOnClickListener(this);
        noLoginTipsView = View.inflate(getActivity(), R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);
        playerView = SuperPlayerView.getInstance(getActivity(), getActivity().getWindow().getDecorView(), true);
//        test = view.findViewById(R.id.test);
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f,0.3f);
//                valueAnimator.setDuration(1000l);
//
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        Float rotate = (Float) animation.getAnimatedValue();
//                        playerView.setScaleX(rotate);
//                        playerView.setScaleY(rotate);
//                    }
//                });
//                valueAnimator.start();
//            }
//        });

        initViewPager();
        getCategoryData();
        if (NetworkUtil.isWifi(getActivity())) {
            SPUtils.getInstance().put("net_state", "0");
        } else {
            SPUtils.getInstance().put("net_state", "1");
        }

        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(netWorkStateReceiver, filter);

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
//                        if (null != videoDetailFragment.videoDetailCommentBtn) {
//                            videoDetailFragment.videoDetailCommentBtn.setVisibility(View.GONE);
//                        }

                        if (null != videoTitleView) {
                            videoTitleView.setVisibility(View.GONE);
                        }
                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin).setVisibility(View.GONE);
                        }

                        if (null != videoFragmentFullLin) {
                            videoFragmentFullLin.setVisibility(View.GONE);
                        }

                        if (videoDetailFragment.isAbbreviation) {
                            if (null != videoDetailFragment.activityRuleAbbreviation) {
                                videoDetailFragment.activityRuleAbbreviation.setVisibility(View.GONE);
                            }
                        } else {
                            if (null != videoDetailFragment.activityRuleImg) {
                                videoDetailFragment.activityRuleImg.setVisibility(View.GONE);
                            }
                            if (null != videoDetailFragment.activityToAbbreviation) {
                                videoDetailFragment.activityToAbbreviation.setVisibility(View.GONE);
                            }
                        }

                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.horizontal_video_wdcs_logo)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.GONE);
                        }

                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.cover_picture)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.cover_picture).setVisibility(View.GONE);
                        }

                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.video_item_bottom)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.video_item_bottom).setVisibility(View.GONE);
                        }

                        if (null != playerView.mWindowPlayer.mLayoutBottom) {
                            playerView.mWindowPlayer.mLayoutBottom.setVisibility(View.GONE);
                        }

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

                        if (null != xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.horizontal_video_wdcs_logo)) {
                            xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.GONE);
                        }

                        if (null != xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.cover_picture)) {
                            xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.cover_picture).setVisibility(View.GONE);
                        }

                        if (null != xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.video_item_bottom)) {
                            xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.video_item_bottom).setVisibility(View.GONE);
                        }

                        if (null != playerView.mWindowPlayer.mLayoutBottom) {
                            playerView.mWindowPlayer.mLayoutBottom.setVisibility(View.GONE);
                        }
                    }

                    KeyboardUtils.hideKeyboard(getActivity().getWindow().getDecorView());
                    videoTab.setVisibility(View.GONE);
                    videoVp.setScroll(false);
//                    videoDetailFragment.mCallback.setEnabled(true);
//                    getActivity().万达提供的方法隐藏tab
                    try {
                        VideoFullAndWindowParam.getInstance().VideoFullAndWindow(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (playerMode.equals(SuperPlayerDef.PlayerMode.WINDOW)) {
                    if (videoDetailFragment.videoFragmentIsVisibleToUser) {
                        videoDetailFragment.videoDetailmanager.setCanScoll(true);
                        videoDetailFragment.refreshLayout.setEnableRefresh(true);
                        videoDetailFragment.adapter.setEnableLoadMore(true);
                        videoDetailFragment.setLikeCollection(playerView.contentStateModel);
//                        if (null != videoDetailFragment.videoDetailCommentBtn) {
//                            videoDetailFragment.videoDetailCommentBtn.setVisibility(View.VISIBLE);
//                        }
                        if (null != videoFragmentFullLin) {
                            videoFragmentFullLin.setVisibility(View.VISIBLE);
                        }

                        if (null != videoTitleView) {
                            videoTitleView.setVisibility(View.VISIBLE);
                        }
                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin).setVisibility(View.VISIBLE);
                        }

                        if (videoDetailFragment.isAbbreviation) {
                            if (null != videoDetailFragment.activityRuleAbbreviation) {
                                videoDetailFragment.activityRuleAbbreviation.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (null != videoDetailFragment.activityRuleBean && null != videoDetailFragment.activityRuleBean.getData() && null != videoDetailFragment.activityRuleBean.getData().getConfig().getJumpUrl()
                                    && !TextUtils.isEmpty(videoDetailFragment.activityRuleBean.getData().getConfig().getJumpUrl())) {
                                if (null != videoDetailFragment.activityRuleImg) {
                                    videoDetailFragment.activityRuleImg.setVisibility(View.VISIBLE);
                                }
                                if (null != videoDetailFragment.activityToAbbreviation) {
                                    videoDetailFragment.activityToAbbreviation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.horizontal_video_wdcs_logo)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.VISIBLE);
                        }

                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.cover_picture)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.cover_picture).setVisibility(View.VISIBLE);
                        }

                        if (null != videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.video_item_bottom)) {
                            videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.video_item_bottom).setVisibility(View.VISIBLE);
                        }

                        if (null != playerView.mWindowPlayer.mLayoutBottom) {
                            playerView.mWindowPlayer.mLayoutBottom.setVisibility(View.VISIBLE);
                        }

                    } else if (xkshFragment.mIsVisibleToUser) {
                        xkshFragment.xkshManager.setCanScoll(true);
                        xkshFragment.refreshLayout.setEnableRefresh(true);
                        xkshFragment.adapter.setEnableLoadMore(true);
                        xkshFragment.setLikeCollection(playerView.contentStateModel);

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
                            if (null != xkshFragment.activityRuleBean && null != xkshFragment.activityRuleBean.getData() && null != xkshFragment.activityRuleBean.getData().getConfig().getJumpUrl()
                                    && !TextUtils.isEmpty(xkshFragment.activityRuleBean.getData().getConfig().getJumpUrl())) {
                                if (null != xkshFragment.activityRuleImg) {
                                    xkshFragment.activityRuleImg.setVisibility(View.VISIBLE);
                                }
                                if (null != xkshFragment.activityToAbbreviation) {
                                    xkshFragment.activityToAbbreviation.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        if (null != xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.horizontal_video_wdcs_logo)) {
                            xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.VISIBLE);
                        }

                        if (null != xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.cover_picture)) {
                            xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.cover_picture).setVisibility(View.VISIBLE);
                        }

                        if (null != xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.video_item_bottom)) {
                            xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.video_item_bottom).setVisibility(View.VISIBLE);
                        }

                        if (null != playerView.mWindowPlayer.mLayoutBottom) {
                            playerView.mWindowPlayer.mLayoutBottom.setVisibility(View.VISIBLE);
                        }

                    }
                    if (null != videoTitleView) {
                        videoTitleView.setVisibility(View.VISIBLE);
                    }
                    videoTab.setVisibility(View.VISIBLE);
                    videoVp.setScroll(true);
//                    videoDetailFragment.mCallback.setEnabled(false);
//                    getActivity().万达提供的方法显示tab
                    try {
                        VideoFullAndWindowParam.getInstance().VideoFullAndWindow(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != videoDetailFragment && null != xkshFragment) {
                                if (null != videoDetailFragment.playViewParams) {
                                    videoDetailFragment.playViewParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                                    videoDetailFragment.playViewParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
                                    videoDetailFragment.playViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                                    videoDetailFragment.playerView.setLayoutParams(videoDetailFragment.playViewParams);
                                }

                                if (null != xkshFragment.playViewParams) {
                                    xkshFragment.playViewParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                                    xkshFragment.playViewParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
                                    xkshFragment.playViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                                    xkshFragment.playerView.setLayoutParams(xkshFragment.playViewParams);
                                }
                            }

                        }
                    }, 100);

                }
            }
        };

        //开始播放回调
        SuperPlayerImpl.setReadPlayCallBack(new SuperPlayerImpl.ReadPlayCallBack() {
            @Override
            public void ReadPlayCallback() {
                if (xkshFragment.mIsVisibleToUser) {
                    String isRenew = "";
                    if (null == playerView.buriedPointModel.getXksh_renew() || TextUtils.equals("false", playerView.buriedPointModel.getXksh_renew())) {
//                    //不为重播
                        xkshFragment.xkshOldSystemTime = DateUtils.getTimeCurrent();
                        String event;
                        if (TextUtils.equals(xkshFragment.mDataDTO.getIsAutoReportEvent(), "1")) {
                            event = Constants.CMS_VIDEO_PLAY;
                        } else {
                            event = Constants.CMS_VIDEO_PLAY_AUTO;
                        }
                        if (null != xkshFragment.mDataDTO && !TextUtils.isEmpty(xkshFragment.mDataDTO.getVolcCategory())) {
                            uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(getActivity(), xkshFragment.mDataDTO.getThirdPartyId(), "", "", event, xkshFragment.mDataDTO.getVolcCategory(), xkshFragment.mDataDTO.getRequestId()), event);
                        }
                        isRenew = "否";
                    } else {
                        isRenew = "是";
                    }
                    //Finder 埋点 视频开始播放
                    FinderBuriedPointManager.setFinderVideoPlay(Constants.CONTENT_VIDEO_PLAY, isRenew, xkshFragment.mDataDTO, module_source);

                } else if (videoDetailFragment.videoFragmentIsVisibleToUser) {
                    String isRenew = "";
                    if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
//                    //不为重播
                        videoDetailFragment.videoOldSystemTime = DateUtils.getTimeCurrent();

                        String event;
                        if (TextUtils.equals(videoDetailFragment.mDataDTO.getIsAutoReportEvent(), "1")) {
                            event = Constants.CMS_VIDEO_PLAY;
                        } else {
                            event = Constants.CMS_VIDEO_PLAY_AUTO;
                        }
                        if (null != videoDetailFragment.mDataDTO || !TextUtils.isEmpty(videoDetailFragment.mDataDTO.getVolcCategory())) {
                            uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(getActivity(), videoDetailFragment.mDataDTO.getThirdPartyId(), "", "", event, videoDetailFragment.mDataDTO.getVolcCategory(), videoDetailFragment.mDataDTO.getRequestId()), event);
                        }

                        isRenew = "否";
                    } else {
                        isRenew = "是";
                    }
                    //Finder 埋点 视频开始播放
                    FinderBuriedPointManager.setFinderVideoPlay(Constants.CONTENT_VIDEO_PLAY, isRenew, videoDetailFragment.mDataDTO, module_source);
                }
            }
        });

        //自动播放/拖动进度条 播放结束回调
        SuperPlayerImpl.setAutoPlayOverCallBack(new SuperPlayerImpl.AutoPlayOverCallBack() {
            @Override
            public void AutoPlayOverCallBack() {
                if (!isPause) {
                    Log.e("yqh_yqh", "重播地址：" + SuperPlayerImpl.mCurrentPlayVideoURL);
                    playerView.mSuperPlayer.reStart();
                }
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
            videoViewPagerAdapter = new VideoViewPagerAdapter(getActivity().getSupportFragmentManager());
        }
        videoVp.setAdapter(videoViewPagerAdapter);

        videoVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (null != playerView && null != playerView.mOrientationHelper) {
                    playerView.mOrientationHelper.disable();
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position && null != videoTab) {
                    videoTab.hideMsg(position);
                    //滑动切换到小康生活  事件名：short_video_home_click
                    //属性名：button_name
                    FinderBuriedPointManager.setFinderClick("tab_" + mTitlesArrays[position]);
                } else if (1 == position) {
                    videoTab.hideMsg(position);
                    if (toFirst) {
                        toFirst = false;
                        return;
                    }
                    //滑动切换到小康生活  事件名：short_video_home_click
                    //属性名：button_name
                    FinderBuriedPointManager.setFinderClick("tab_" + mTitlesArrays[position]);
                } else if (2 == position) {
                    playerView.mSuperPlayer.pause();
                    //切换到直播的时候  不允许旋转
                    playerView.setOrientation(false);
                    //滑动切换到小康生活  事件名：short_video_home_click
                    //属性名：button_name
                    FinderBuriedPointManager.setFinderClick("tab_" + mTitlesArrays[position]);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                playerView.mOrientationHelper.enable();
            }
        });
    }


    private void initViewPagerData() {
        for (int i = 0; i < mTitlesArrays.length; i++) {
            VideoChannelModel model = new VideoChannelModel();
            ColumnModel columnModel = new ColumnModel();
            columnModel.setColumnId(i + "");
            if (i == 0) {
                columnModel.setColumnName(mTitlesArrays[0]);
                columnModel.setPanelCode("xksh.works");
            } else if (i == 1) {
                columnModel.setColumnName("视频");
                columnModel.setPanelCode("mycs.video.video");
            } else if (i == 2) {
                columnModel.setColumnName("直播");
                columnModel.setPanelCode("mycs.live.livelist");
            }
            model.setColumnBean(columnModel);
            videoChannelModels.add(model);
        }
        videoViewPagerAdapter.addItems(videoChannelModels, contentId, categoryName, playerView, toCurrentTab, true);
        colunmList.clear();
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

        videoDetailFragment.setCommentPopIsVisible(new VideoDetailFragment.CommentPopIsVisible() {
            @Override
            public void commentPopIsVisible(boolean isVisible) {
                if (isVisible) {
                    topZzc.setVisibility(View.GONE);
                } else {
                    topZzc.setVisibility(View.VISIBLE);
                }
            }
        });

        videoDetailFragment.setNoWifiClickListener(new VideoDetailFragment.NoWifiListener() {
            @Override
            public void noWifiClickListener() {
                for (int i = 0; i < xkshFragment.mDatas.size(); i++) {
                    if (null != xkshFragment.mDatas.get(i)) {
                        xkshFragment.mDatas.get(i).setWifi(true);
                    }
                }
                xkshFragment.adapter.notifyDataSetChanged();
            }
        });

        xkshFragment.setCommentPopIsVisible(new XkshFragment.CommentPopIsVisible() {
            @Override
            public void commentPopIsVisible(boolean isVisible) {
                if (isVisible) {
                    topZzc.setVisibility(View.GONE);
                } else {
                    topZzc.setVisibility(View.VISIBLE);
                }
            }
        });

        xkshFragment.setNoWifiClickListener(new XkshFragment.NoWifiListener() {
            @Override
            public void noWifiClickListener() {
                for (int i = 0; i < videoDetailFragment.mDatas.size(); i++) {
                    if (null != videoDetailFragment.mDatas.get(i)) {
                        videoDetailFragment.mDatas.get(i).setWifi(true);
                    }
                }
                videoDetailFragment.adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取标题
     */
    private void getCategoryData() {
        OkGo.<CategoryModel>get(ApiConstants.getInstance().getCategoryData())
                .tag(VIDEOTAG)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .params("categoryCode", "mycs.video")
                .execute(new JsonCallback<CategoryModel>(CategoryModel.class) {

                    @Override
                    public void onSuccess(Response<CategoryModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (success_code.equals(response.body().getCode())) {
                            if (null == response.body().getData()) {
                                ToastUtils.showShort(R.string.data_err);
                                return;
                            }

                            categoryModelList.addAll(response.body().getData());
                            if (categoryModelList.isEmpty()) {
                                return;
                            }

                            mTitlesArrays = new String[3];

                            for (int i = 0; i < categoryModelList.size(); i++) {
                                if (TextUtils.equals(categoryModelList.get(i).getCode(), "mycs.xksh")) {
                                    mTitlesArrays[0] = categoryModelList.get(i).getName();
//                                    mTitlesArrays[0] = "我的小康生活";
                                }
                            }
                            mTitlesArrays[1] = "视频";
                            mTitlesArrays[2] = "直播";
                            initViewPagerData();

                            LiveDataParam.getInstance().homeTabIndex.observe(getActivity(), new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer index) {
                                    if (null != videoDetailFragment && null != xkshFragment) {
                                        if (index != 1) {
                                            videoDetailFragment.playerView.setOrientation(false);
                                            xkshFragment.playerView.setOrientation(false);
                                        } else {
                                            videoDetailFragment.playerView.setOrientation(true);
                                            xkshFragment.playerView.setOrientation(true);
                                        }
                                        videoDetailFragment.setVideoDetailFragmentVisible(index);
                                        xkshFragment.setXkshFragmentVisible(index);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Response<CategoryModel> response) {
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
                        videoTitleView.setVisibility(View.VISIBLE);
                    }
                });

    }


    /**
     * 没有登录情况下 点击点赞收藏评论 提示登录的提示框
     */
    private void noLoginTipsPop() {
        if (null == noLoginTipsPop) {
            noLoginTipsPop = new CustomPopWindow.PopupWindowBuilder(getActivity())
                    .setView(noLoginTipsView)
                    .enableBackgroundDark(true)
                    .setOutsideTouchable(true)
                    .setFocusable(true)
                    .setAnimationStyle(R.style.AnimCenter)
                    .size(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels)
                    .create()
                    .showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            noLoginTipsPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.search_icon) {
            //跳转H5搜索
            try {
                if (Utils.mIsDebug) {
                    param.recommendUrl(Constants.SEARCHPLUS, null);
                } else {
                    param.recommendUrl(Constants.SEARCHPLUS_ZS, null);
                }
                FinderBuriedPointManager.setFinderClick("搜索");
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
                    FinderBuriedPointManager.setFinderClick("视频个人中心");
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
    public void onDestroy() {
        super.onDestroy();
        if (instance != null) {
            instance.release();
            instance.mSuperPlayer.destroy();
            instance = null;
        }
        OkGo.getInstance().cancelAll();
        maxPercent = 0;
        lsDuration = 0;
        getActivity().unregisterReceiver(netWorkStateReceiver);
        FinderBuriedPointManager.setFinderClick("页面关闭");
//        OkGo.getInstance().cancelTag(VIDEOTAG);
        mHandler.removeCallbacksAndMessages(null);
    }

    public static boolean VideoFullToWindow() {
        try {
            if (null == playerView || null == playerView.mWindowPlayer.mControllerCallback) {
                return false;
            }

            if (playerView.mSuperPlayer.getPlayerMode() == SuperPlayerDef.PlayerMode.FULLSCREEN) {
                playerView.mWindowPlayer.mControllerCallback.onSwitchPlayMode(SuperPlayerDef.PlayerMode.WINDOW);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}