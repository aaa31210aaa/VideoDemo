package ui.activity;

import static android.widget.RelativeLayout.BELOW;
import static com.tencent.liteav.demo.superplayer.SuperPlayerView.mTargetPlayerMode;
import static com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl.autoPlayOver5gCallBack;
import static com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl.readPlay5GCallBack;
import static com.tencent.liteav.demo.superplayer.ui.player.AbsPlayer.formattedTime;
import static com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer.mDuration;
import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.SPECIAL5G_ISPAUSE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.contants.Contants;
import com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl;
import com.tencent.liteav.demo.superplayer.model.utils.SystemUtils;
import com.tencent.liteav.demo.superplayer.ui.view.PointSeekBar;
import com.wdcs.callback.JsonCallback;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.manager.ContentBuriedPointManager;
import com.wdcs.manager.FinderBuriedPointManager;
import com.wdcs.model.ColumnModel;
import com.wdcs.model.Special5GTabModel;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.utils.DateUtils;
import com.wdcs.utils.KeyboardUtils;
import com.wdcs.utils.NetworkUtil;
import com.wdcs.utils.NoScrollViewPager;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.SPUtils;
import com.wdcs.utils.ScreenUtils;
import com.wdcs.utils.ToastUtils;
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.Special5gPagerAdapter;
import ui.fragment.Special5gHudongFragment;
import ui.fragment.Special5gVideoFragment;
import widget.NetBroadcastReceiver;

public class SpecialArea5GActivity extends AppCompatActivity implements View.OnClickListener {
    public LinearLayout searchBar;
    public CommonTabLayout special5gTab;
    public NoScrollViewPager special5gVp;
    public Special5gPagerAdapter special5gPagerAdapter;
    private ArrayList<VideoChannelModel> videoChannelModels = new ArrayList<>();
    private List<String> colunmList = new ArrayList<>();

    private ImageView area5gSearch;
    public SuperPlayerView playerView;
    public String contentId;
    private String categoryName;
    private String requestId;
    private String module_source;
    private RelativeLayout topZzc;
    public static double special5gMaxPercent = 0;
    private RelativeLayout special5gBack;
    private RelativeLayout special5gPersonalCenter;
    private boolean isDestroyed = false;
    private NetBroadcastReceiver netWorkStateReceiver;
    private Special5gVideoFragment special5gSixFragment;
    private Handler mHandler = new Handler();
    private TranslateAnimation translateAniLeftShow, translateAniLeftHide;
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    private ImageView special5gBackImg;
    private ImageView special5gSearchImg;
    private ImageView personalCenterImg;
    private RelativeLayout special5gTabRl;
    private int toCurrentTab;
    private RelativeLayout.LayoutParams layoutParams;
    private List<Special5GTabModel.DataDTO> tabLists = new ArrayList<>();
    public static List<WebView> webViewList = new ArrayList<>();
    private int tabPosition;
    public static Map<Fragment, WebView> fragmentWebViewMap = new HashMap<>();
    public static WebView currentWebView;
    private int defaultTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_area5g);
        initView();
        initViewPager();

        if (NetworkUtil.isWifi(this)) {
            SPUtils.getInstance().put("net_state", "0");
        } else {
            SPUtils.getInstance().put("net_state", "1");
        }
        netWorkStateReceiver = NetBroadcastReceiver.getInstance();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        videoProgressMonitor();
        playerWindowMonitor();
        playerStartMonitor();
        playerOverMonitor();
        translateAnimation();
    }


    private void initView() {
        searchBar = findViewById(R.id.search_bar);
        special5gTab = findViewById(R.id.special_5g_tab);

        special5gTabRl = findViewById(R.id.special_5g_tab_rl);
        special5gBackImg = findViewById(R.id.special_5g_back_img);
        special5gSearchImg = findViewById(R.id.special_5g_search_img);
        personalCenterImg = findViewById(R.id.personal_center_img);
        special5gVp = findViewById(R.id.special_5g_vp);
        area5gSearch = findViewById(R.id.area5g_search);
        topZzc = findViewById(R.id.top_zzc);
        special5gBack = findViewById(R.id.special_5g_back);
        special5gPersonalCenter = findViewById(R.id.special_5g_personal_center);
        noLoginTipsView = View.inflate(this, R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);
        playerView = new SuperPlayerView(this, getWindow().getDecorView(), true);
        playerView.setStatuDark(false);
        area5gSearch.setOnClickListener(this);
        special5gBack.setOnClickListener(this);
        special5gPersonalCenter.setOnClickListener(this);


        contentId = getIntent().getStringExtra("contentId");
        categoryName = getIntent().getStringExtra("category_name");
        requestId = getIntent().getStringExtra("requestId");
        module_source = getIntent().getStringExtra("module_source");
        if (null == module_source) {
            module_source = "";
        }
        layoutParams = (RelativeLayout.LayoutParams) findViewById(R.id.special_5g_vp).getLayoutParams();
//        setStatuBar(false);

    }

    private void initViewPager() {
        if (null == special5gPagerAdapter) {
            special5gPagerAdapter = new Special5gPagerAdapter(getSupportFragmentManager());
        }
        special5gVp.setAdapter(special5gPagerAdapter);

        special5gPagerAdapter.setAddItemClickListener(new Special5gPagerAdapter.AddItemClickListener() {
            @Override
            public void addItemClick(String url, List<Fragment> fragmentList) {
                Bundle bundle = new Bundle();
                bundle.putString("webUrl", url);
                MutableLiveData<WebView> webLiveData = new MutableLiveData<>();
                final Fragment fragment = VideoInteractiveParam.getInstance().getWebViewFragment(bundle, webLiveData);

                webLiveData.observe(SpecialArea5GActivity.this, new Observer<WebView>() {
                    @Override
                    public void onChanged(final WebView webView) {
                        fragmentWebViewMap.put(fragment, webView);
                    }
                });

                if (null != fragment) {
                    fragmentList.add(fragment);
                }
            }
        });

        special5gVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageSelected(int position) {
                special5gTab.setCurrentTab(position);
                if ("5G.house.tuijian".equals(tabLists.get(position).getCode())) {
                    //黑背景
                    setStatuBar(false);
                    playerView.setOrientation(true);
                    tabPosition = position;
                } else {
                    //白背景
                    setStatuBar(true);
                    playerView.setOrientation(false);
                    tabPosition = position;
                }

                Fragment fragment = special5gPagerAdapter.fragmentList.get(position);
                if (null != fragment) {
                    currentWebView = fragmentWebViewMap.get(fragment);
                }

                if (fragment instanceof Special5gHudongFragment) {
                    String columnId = fragment.getArguments().getString("columnId");
                    if (columnId.equals("5G.house.hudong")) {
                        ((Special5gHudongFragment) fragment).getCurrentWebView();
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getTabData();
    }

    private void getTabData() {
        String deviceId = "";
        try {
            deviceId = param.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkGo.<Special5GTabModel>get(ApiConstants.getInstance().getCategoryData())
                .params("categoryCode", "5G.channel.hose")
                .params("ssid", deviceId)
                .execute(new JsonCallback<Special5GTabModel>() {
                    @Override
                    public void onSuccess(Response<Special5GTabModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (null == response.body().getData()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }
                        if (TextUtils.equals(Constants.success_code, response.body().getCode())) {
                            tabLists.addAll(response.body().getData());
                            initViewPagerData(tabLists);
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onError(Response<Special5GTabModel> response) {
                        super.onError(response);
                        if (null == response.body()) {
                            return;
                        }
                        if (null == response.body().getMessage()) {
                            return;
                        }

                        ToastUtils.showShort(response.body().getMessage());
                    }
                });
    }

    /**
     * 根据是否是视频tab，来设置statusbar
     *
     * @param isVideo
     */
    private void setStatuBar(boolean isVideo) {
        if (isVideo) {
            topZzc.setVisibility(View.GONE);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.special_5g_tab_rl);
            special5gTab.setIndicatorColor(getResources().getColor(R.color.bz_red));
            special5gTab.setTextSelectColor(getResources().getColor(R.color.video_black));
            special5gTab.setTextUnselectColor(getResources().getColor(R.color.p60_opacity_black));
            special5gTabRl.setBackgroundColor(getResources().getColor(R.color.white));
            searchBar.setBackgroundColor(getResources().getColor(R.color.white));
            area5gSearch.setImageResource(R.drawable.blackbg_special_area5g_search_bg);
            special5gBackImg.setImageResource(R.drawable.black_back);
            special5gSearchImg.setImageResource(R.drawable.black_search);
            personalCenterImg.setImageResource(R.drawable.black_personal_center);
            ScreenUtils.setStatusBarColor(this, getResources().getColor(R.color.white), true);
            ScreenUtils.StatusBarLightMode(SpecialArea5GActivity.this, true);
            SystemUtils.setNavbarColor(this, R.color.white);
        } else {
            topZzc.setVisibility(View.VISIBLE);
            layoutParams.removeRule(BELOW);
            special5gTab.setIndicatorColor(getResources().getColor(R.color.white));
            special5gTab.setTextSelectColor(getResources().getColor(R.color.white));
            special5gTab.setTextUnselectColor(getResources().getColor(R.color.p60_opacity_white));
            special5gTabRl.setBackgroundColor(getResources().getColor(R.color.transparent));
            searchBar.setBackgroundColor(getResources().getColor(R.color.transparent));
            area5gSearch.setImageResource(R.drawable.whitebg_special_area5g_search_bg);
            special5gBackImg.setImageResource(R.drawable.video_back);
            special5gSearchImg.setImageResource(R.drawable.video_search);
            personalCenterImg.setImageResource(R.drawable.video_user);
            ScreenUtils.fullScreen(this, getResources().getColor(R.color.transparent));
            SystemUtils.setNavbarColor(this, R.color.video_black);
        }

    }

    private void initViewPagerData(List<Special5GTabModel.DataDTO> models) {
        special5gVp.setOffscreenPageLimit(models.size());
        try {
            if (models.size() == 1) {
                special5gTab.setTextSelectColor(getResources().getColor(R.color.white));
                special5gTab.setTextSizeSelect(5);
            }

            for (int i = 0; i < models.size(); i++) {
                VideoChannelModel model = new VideoChannelModel();
                ColumnModel columnModel = new ColumnModel();
                columnModel.setColumnId(models.get(i).getCode());
                columnModel.setColumnName(models.get(i).getName());
                columnModel.setPanelCode(models.get(i).getCode());
                if ("5G.house.tuijian".equals(models.get(i).getCode())) {
                    defaultTabIndex = i;
                    columnModel.setSelecticon(R.drawable.tab_icon_white);
                } else {
                    columnModel.setSelecticon(R.drawable.tab_icon_red);
                }
                columnModel.setSkipUrl(models.get(i).getJumpUrl());
                columnModel.setUnselecticon(R.drawable.tab_icon_white);
                model.setColumnBean(columnModel);
                videoChannelModels.add(model);
            }
            toCurrentTab = getIntent().getIntExtra("setCurrentTab", defaultTabIndex);
            special5gPagerAdapter.addItems(videoChannelModels, playerView, contentId, categoryName, requestId, defaultTabIndex);
            for (VideoChannelModel channelBean : videoChannelModels) {
                colunmList.add(channelBean.getColumnBean().getColumnName());
            }
            String[] titles = colunmList.toArray(new String[colunmList.size()]);
            //tab和ViewPager进行关联
//            special5gTab.setViewPager(special5gVp, titles);
            special5gTab.setTabData(videoChannelModels);
            setCurrentPosition(toCurrentTab);
            special5gTab.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    special5gVp.setCurrentItem(position);
                }

                @Override
                public void onTabReselect(int position) {

                }
            });


            special5gSixFragment = (Special5gVideoFragment) special5gPagerAdapter.getItem(defaultTabIndex);
            if (null != special5gSixFragment) {
                special5gSixFragment.setCommentPopIsVisible(new Special5gVideoFragment.CommentPopIsVisible() {
                    @Override
                    public void commentPopIsVisible(boolean isVisible) {
                        if (isVisible) {
                            topZzc.setVisibility(View.GONE);
                        } else {
                            topZzc.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentPosition(int position) {
        if (null != special5gTab && null != special5gVp) {
            special5gTab.setCurrentTab(position);
            special5gVp.setCurrentItem(position);
        }
    }

    /**
     * 视频进度条监听
     */
    private void videoProgressMonitor() {
        //全屏进度条监听
        if (null != playerView && null != playerView.mFullScreenPlayer) {
            playerView.mFullScreenPlayer.mSeekBarProgress.setOnSeekBarChangeListener(new PointSeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(PointSeekBar seekBar, int progress, boolean fromUser) {
                    if (null == playerView) {
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
                                if (percentage > special5gMaxPercent) {
                                    special5gMaxPercent = percentage;
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

                    if (special5gTab.getCurrentTab() != 5 && SPECIAL5G_ISPAUSE) {
                        playerView.mSuperPlayer.pause();
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
                    special5gVp.setScroll(false);
                    special5gSixFragment.videoDetailmanager.setCanScoll(false);
                }

                @Override
                public void onStopTrackingTouch(PointSeekBar seekBar) {
                    int curProgress = seekBar.getProgress();
                    int maxProgress = seekBar.getMax();
                    if (mTargetPlayerMode == SuperPlayerDef.PlayerMode.WINDOW) {
                        special5gVp.setScroll(true);
                        special5gSixFragment.videoDetailmanager.setCanScoll(true);
                    } else {
                        special5gVp.setScroll(false);
                        special5gSixFragment.videoDetailmanager.setCanScoll(false);
                    }

                    switch (playerView.mWindowPlayer.mPlayType) {
                        case VOD:
                            if (curProgress >= 0 && curProgress <= maxProgress) {
                                // 关闭重播按钮
                                playerView.mWindowPlayer.toggleView(playerView.mWindowPlayer.mLayoutReplay, false);
                                float percentage = ((float) curProgress) / maxProgress;
                                long duration = (long) (percentage * mDuration);
                                if (percentage > special5gMaxPercent) {
                                    special5gMaxPercent = percentage;
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


    }

    /**
     * 监听播放器播放窗口变化回调
     */
    private void playerWindowMonitor() {
        playerView.playModeCallBack = new SuperPlayerView.PlayModeCallBack() {
            @Override
            public void getPlayMode(SuperPlayerDef.PlayerMode playerMode) {
                LinearLayout videoFragmentFullLin = (LinearLayout) special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.superplayer_iv_fullscreen);
                if (playerMode.equals(SuperPlayerDef.PlayerMode.FULLSCREEN)) {
                    special5gSixFragment.videoDetailmanager.setCanScoll(false);
                    special5gSixFragment.refreshLayout.setEnableRefresh(false);
                    special5gSixFragment.adapter.setEnableLoadMore(false);
                    if (null != special5gSixFragment.popupWindow) {
                        special5gSixFragment.popupWindow.dissmiss();
                    }

                    if (null != special5gSixFragment.inputAndSendPop) {
                        special5gSixFragment.inputAndSendPop.dissmiss();
                    }

                    if (null != special5gSixFragment.noLoginTipsPop) {
                        special5gSixFragment.noLoginTipsPop.dissmiss();
                    }

                    if (null != special5gSixFragment.sharePop) {
                        special5gSixFragment.sharePop.dissmiss();
                    }
//                        if (null != videoDetailFragment.videoDetailCommentBtn) {
//                            videoDetailFragment.videoDetailCommentBtn.setVisibility(View.GONE);
//                        }

                    if (null != searchBar) {
                        searchBar.setVisibility(View.GONE);
                    }
                    if (null != special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.introduce_lin)) {
                        special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.introduce_lin).setVisibility(View.GONE);
                    }

                    if (null != videoFragmentFullLin) {
                        videoFragmentFullLin.setVisibility(View.GONE);
                    }

                    if (special5gSixFragment.isAbbreviation) {
                        if (null != special5gSixFragment.activityRuleAbbreviation) {
                            special5gSixFragment.activityRuleAbbreviation.setVisibility(View.GONE);
                        }
                    } else {
                        if (null != special5gSixFragment.activityRuleImg) {
                            special5gSixFragment.activityRuleImg.setVisibility(View.GONE);
                        }
                        if (null != special5gSixFragment.activityToAbbreviation) {
                            special5gSixFragment.activityToAbbreviation.setVisibility(View.GONE);
                        }
                    }

                    if (null != special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.horizontal_video_wdcs_logo)) {
                        special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.GONE);
                    }

                    if (null != special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.cover_picture)) {
                        special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.cover_picture).setVisibility(View.GONE);
                    }

                    if (null != special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.video_item_bottom)) {
                        special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.video_item_bottom).setVisibility(View.GONE);
                    }

                    if (null != playerView.mWindowPlayer.mLayoutBottom) {
                        playerView.mWindowPlayer.mLayoutBottom.setVisibility(View.GONE);
                    }


                    KeyboardUtils.hideKeyboard(getWindow().getDecorView());
                    special5gTab.setVisibility(View.GONE);
                    special5gVp.setScroll(false);


                } else if (playerMode.equals(SuperPlayerDef.PlayerMode.WINDOW)) {
                    special5gSixFragment.videoDetailmanager.setCanScoll(true);
                    special5gSixFragment.refreshLayout.setEnableRefresh(true);
                    special5gSixFragment.adapter.setEnableLoadMore(true);
                    special5gSixFragment.setLikeCollection(playerView.contentStateModel);
//                        if (null != special5gSixFragment.videoDetailCommentBtn) {
//                            special5gSixFragment.videoDetailCommentBtn.setVisibility(View.VISIBLE);
//                        }
                    if (null != videoFragmentFullLin) {
                        videoFragmentFullLin.setVisibility(View.VISIBLE);
                    }

                    if (null != special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.introduce_lin)) {
                        special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.introduce_lin).setVisibility(View.VISIBLE);
                    }

                    if (special5gSixFragment.isAbbreviation) {
                        if (null != special5gSixFragment.activityRuleAbbreviation) {
                            special5gSixFragment.activityRuleAbbreviation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (null != special5gSixFragment.activityRuleBean && null != special5gSixFragment.activityRuleBean.getData() && null != special5gSixFragment.activityRuleBean.getData().getConfig().getJumpUrl()
                                && !TextUtils.isEmpty(special5gSixFragment.activityRuleBean.getData().getConfig().getJumpUrl())) {
                            if (null != special5gSixFragment.activityRuleImg) {
                                special5gSixFragment.activityRuleImg.setVisibility(View.VISIBLE);
                            }
                            if (null != special5gSixFragment.activityToAbbreviation) {
                                special5gSixFragment.activityToAbbreviation.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    if (null != special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.horizontal_video_wdcs_logo)) {
                        special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.VISIBLE);
                    }

                    if (null != special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.cover_picture)) {
                        special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.cover_picture).setVisibility(View.VISIBLE);
                    }

                    if (null != special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.video_item_bottom)) {
                        special5gSixFragment.adapter.getViewByPosition(special5gSixFragment.currentIndex, R.id.video_item_bottom).setVisibility(View.VISIBLE);
                    }

                    if (null != playerView.mWindowPlayer.mLayoutBottom) {
                        playerView.mWindowPlayer.mLayoutBottom.setVisibility(View.VISIBLE);
                    }

                    if (null != searchBar) {
                        searchBar.setVisibility(View.VISIBLE);
                    }
                    special5gTab.setVisibility(View.VISIBLE);
                    special5gVp.setScroll(true);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != special5gSixFragment && null != special5gSixFragment.playViewParams) {
                                special5gSixFragment.playViewParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                                special5gSixFragment.playViewParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
                                special5gSixFragment.playViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                                special5gSixFragment.playerView.setLayoutParams(special5gSixFragment.playViewParams);
                            }

                        }
                    }, 100);
                }
            }
        };
    }

    /**
     * 开始播放回调
     */
    private void playerStartMonitor() {
        SuperPlayerImpl.setReadPlay5GCallBack(new SuperPlayerImpl.ReadPlay5GCallBack() {
            @Override
            public void ReadPlay5GCallBack() {
                try {
                    if (null != special5gSixFragment) {
                        if (SPECIAL5G_ISPAUSE) {
                            playerView.mSuperPlayer.pause();
                        }
                        Log.e("ssss", "测试ReadPlayCallback生命周期---" + SPECIAL5G_ISPAUSE);
                        String isRenew = "";
                        if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
                            //                    //不为重播
                            special5gSixFragment.videoOldSystemTime = DateUtils.getTimeCurrent();

                            String event;
                            if (TextUtils.equals(special5gSixFragment.mDataDTO.getIsAutoReportEvent(), "1")) {
                                event = Constants.CMS_VIDEO_PLAY;
                            } else {
                                event = Constants.CMS_VIDEO_PLAY_AUTO;
                            }
                            if (null != special5gSixFragment.mDataDTO && !TextUtils.isEmpty(special5gSixFragment.mDataDTO.getVolcCategory())) {
                                FinderBuriedPointManager.uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(SpecialArea5GActivity.this, special5gSixFragment.mDataDTO.getThirdPartyId(), "", "",
                                        event, special5gSixFragment.mDataDTO.getVolcCategory(), special5gSixFragment.mDataDTO.getRequestId()), event);
                            }

                            isRenew = "否";
                        } else {
                            isRenew = "是";
                        }
                        //Finder 埋点 视频开始播放
                        FinderBuriedPointManager.setFinderVideoPlay(Constants.CONTENT_VIDEO_PLAY, isRenew, special5gSixFragment.mDataDTO, module_source);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 自动播放/拖动进度条 播放结束回调
     */
    private void playerOverMonitor() {
        SuperPlayerImpl.setAutoPlayOver5GCallBack(new SuperPlayerImpl.AutoPlayOver5GCallBack() {
            @Override
            public void AutoPlayOver5GCallBack() {
                if (!SPECIAL5G_ISPAUSE && null != playerView) {
                    Log.e("yqh_yqh", "重播地址：" + playerView.mCurrentPlayVideoURL);
                    playerView.mSuperPlayer.reStart();
                }
            }
        });
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.area5g_search) {
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
        } else if (id == R.id.special_5g_back) {
            if (null != currentWebView && currentWebView.canGoBack()) {
                currentWebView.goBack();
            } else {
                finish();
            }

        } else if (id == R.id.special_5g_personal_center) {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != currentWebView && currentWebView.canGoBack()) {
                currentWebView.goBack();
                return false;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            destroy();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy();
    }

    private void destroy() {
        if (isDestroyed) {
            return;
        }
        // 回收资源
        if (playerView != null) {
            playerView.release();
            playerView.mSuperPlayer.destroy();
            playerView = null;
        }
        special5gMaxPercent = 0;
        SPECIAL5G_ISPAUSE = false;
        unregisterReceiver(netWorkStateReceiver);
        FinderBuriedPointManager.setFinderClick("页面关闭");
        mHandler.removeCallbacksAndMessages(null);
        autoPlayOver5gCallBack = null;
        SuperPlayerImpl.setAutoPlayOver5GCallBack(autoPlayOver5gCallBack);
        readPlay5GCallBack = null;
        SuperPlayerImpl.setReadPlay5GCallBack(readPlay5GCallBack);
        isDestroyed = true;
        webViewList.clear();
        fragmentWebViewMap.clear();
        currentWebView = null;
    }
}