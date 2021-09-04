package ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
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
import com.wdcs.manager.OnViewPagerListener;
import com.wdcs.manager.ViewPagerLayoutManager;
import com.wdcs.model.ColumnModel;
import com.wdcs.model.ContentStateModel;
import com.wdcs.model.DataDTO;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.KeyboardUtils;
import com.wdcs.utils.NumberFormatTool;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.ToastUtils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import adpter.VideoViewPagerAdapter;
import ui.fragment.VideoDetailFragment;
import ui.fragment.XkshFragment;

import com.wdcs.utils.NoScrollViewPager;

import static com.tencent.liteav.demo.superplayer.SuperPlayerView.instance;
import static com.tencent.liteav.demo.superplayer.SuperPlayerView.mTargetPlayerMode;
import static com.tencent.liteav.demo.superplayer.ui.player.AbsPlayer.formattedTime;
import static com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer.mDuration;
import static com.wdcs.callback.VideoInteractiveParam.param;

public class VideoMainActivity extends AppCompatActivity implements View.OnClickListener {
    public SlidingTabLayout videoTab;
    public NoScrollViewPager videoVp;
    private String[] mTitlesArrays = {"我的小康", "视频", "直播"};
    private VideoViewPagerAdapter adapter;
    private List<VideoChannelModel> videoChannelModels = new ArrayList<>();
    private List<String> colunmList = new ArrayList<>();
    private String panelId = "";
    private LinearLayout backLin;
    private RelativeLayout videoTitleView;
    private VideoDetailFragment videoDetailFragment;
    private XkshFragment xkshFragment;
    private SuperPlayerView playerView;
    private ImageView rankList;
    private ImageView searchIcon;
    private ImageView personalCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SystemUtils.setNavbarColor(this, R.color.black);
        setContentView(R.layout.activity_video_main);
        initView();
    }

    private void initView() {
        panelId = getIntent().getStringExtra("panelId");
        backLin = findViewById(R.id.back_lin);
        backLin.setOnClickListener(this);
        videoTab = findViewById(R.id.video_tab);
        videoVp = findViewById(R.id.video_vp);
        videoTitleView = findViewById(R.id.video_title_view);
        rankList = findViewById(R.id.rank_list);
        rankList.setOnClickListener(this);
        searchIcon = findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(this);
        personalCenter = findViewById(R.id.personal_center);
        personalCenter.setOnClickListener(this);
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
                    videoDetailFragment.videoDetailCommentBtn.setVisibility(View.GONE);
                    xkshFragment.videoDetailCommentBtn.setVisibility(View.GONE);
                    videoDetailFragment.mVideoTitleView.setVisibility(View.GONE);
                    xkshFragment.mVideoTitleView.setVisibility(View.GONE);
                    videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin).setVisibility(View.GONE);
                    xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.introduce_lin).setVisibility(View.GONE);
                    videoVp.setScroll(false);
                    videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.superplayer_iv_fullscreen)
                            .setVisibility(View.GONE);
                    xkshFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.superplayer_iv_fullscreen)
                            .setVisibility(View.GONE);
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
                    videoDetailFragment.videoDetailCommentBtn.setVisibility(View.VISIBLE);
                    xkshFragment.videoDetailCommentBtn.setVisibility(View.VISIBLE);
                    videoDetailFragment.mVideoTitleView.setVisibility(View.VISIBLE);
                    xkshFragment.mVideoTitleView.setVisibility(View.VISIBLE);
                    videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.introduce_lin).setVisibility(View.VISIBLE);
                    xkshFragment.adapter.getViewByPosition(xkshFragment.currentIndex, R.id.introduce_lin).setVisibility(View.VISIBLE);
                    videoVp.setScroll(true);
                    videoDetailFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.superplayer_iv_fullscreen)
                            .setVisibility(View.VISIBLE);
                    xkshFragment.adapter.getViewByPosition(videoDetailFragment.currentIndex, R.id.superplayer_iv_fullscreen)
                            .setVisibility(View.VISIBLE);
                }
            }
        };
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
                columnModel.setPanelId("124501");
            } else if (i == 1) {
                columnModel.setColumnName("视频");
                columnModel.setColumnType("1");
                columnModel.setPanelId("48662");
            } else if (i == 2) {
                columnModel.setColumnName("直播");
                columnModel.setColumnType("0");
                columnModel.setPanelId("48757");
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

        videoTab.showMsg(0, "有活动");
        videoTab.setMsgMargin(0, 0, ButtonSpan.dip2px(10));
        videoDetailFragment = (VideoDetailFragment) adapter.getItem(1);
        xkshFragment = (XkshFragment) adapter.getItem(0);
    }

    /**
     * 获取活动规则
     */
    private void getPanelInfo() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back_lin) {
            finish();
        } else if (id == R.id.rank_list) {
            //跳转H5排行榜
            try {
                param.recommendUrl(Constants.RANKING_LIST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.search_icon) {
            //跳转H5搜索
            try {
//                param.recommendUrl(Constants.);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.personal_center) {
            //跳转H5个人中心
            try {
                param.recommendUrl(Constants.PERSONAL_CENTER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();


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
        OkGo.getInstance().cancelAll();
    }

}