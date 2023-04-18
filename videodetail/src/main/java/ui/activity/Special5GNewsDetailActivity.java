package ui.activity;

import static com.tencent.liteav.demo.superplayer.ui.player.AbsPlayer.formattedTime;
import static com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer.mDuration;
import static com.wdcs.constants.Constants.CLICK_INTERVAL_TIME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.contants.Contants;
import com.tencent.liteav.demo.superplayer.model.utils.SystemUtils;
import com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer;
import com.tencent.liteav.demo.superplayer.ui.view.PointSeekBar;
import com.tencent.rtmp.TXLiveConstants;
import com.wdcs.callback.JsonCallback;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.model.CollectionTypeModel;
import com.wdcs.model.DataDTO;
import com.wdcs.model.ShareInfo;
import com.wdcs.model.VideoDetailCollectionModel;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.ScreenUtils;
import com.wdcs.utils.ToastUtils;
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import adpter.NewsVideoCollectionRvAdapter;
import adpter.VideoCollectionRvAdapter;

public class Special5GNewsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout titleBar;
    private RelativeLayout newsDetailPlayview;
    private SuperPlayerView playerView;
    private LinearLayout.LayoutParams playViewParams;
    private RelativeLayout newsDetailBack;
    private RelativeLayout newsDetailShare;
    private int height;
    private Handler handler;
    private TextView newsDetailTitle;
    private TextView introductionText;
    private TextView textTv;
    private RecyclerView collectionRv;
    private VideoCollectionRvAdapter adapter;
    private NewsVideoCollectionRvAdapter newsVideoAdapter;
    private List<CollectionTypeModel.DataDTO.ChildrenDTO> videoComDatas = new ArrayList<>();
    private List<VideoDetailCollectionModel.DataDTO.RecordsDTO> newsVideoDatas = new ArrayList<>();
    private String contentId;
    private String type;
    private int pageIndex = 1;
    private String pageSize = "10";
    private TextView videoCollectionNum;
    private SmartRefreshLayout refreshLayout;
    private String newsCollectionId;
    public CustomPopWindow sharePop;
    private View sharePopView;
    private RelativeLayout sharePopDismiss;
    private RelativeLayout shareWxBtnRl;
    private RelativeLayout shareCircleBtnRl;
    private RelativeLayout shareQqBtnRl;
    private VideoDetailCollectionModel.DataDTO.RecordsDTO recordsDTO;
    private CollectionTypeModel.DataDTO.ChildrenDTO childrenDTO;
    private RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special5g_news_detail);
        initView();
    }

    private void initView() {
        ScreenUtils.setStatusBarColor(this, R.color.white);
        ScreenUtils.StatusBarLightMode(this, true, false);
        contentId = getIntent().getStringExtra("contentId");
        titleBar = findViewById(R.id.title_bar);
        newsDetailBack = findViewById(R.id.news_detail_back);
        newsDetailBack.setOnClickListener(this);
        newsDetailShare = findViewById(R.id.news_detail_share);
        newsDetailShare.setOnClickListener(this);
        newsDetailPlayview = findViewById(R.id.news_detail_playview);
        playerView = new SuperPlayerView(this, getWindow().getDecorView(), false);
        playerView.setStatuDark(true);
        addPlayView();

        /**
         * 监听播放器播放窗口变化回调
         */
        playerView.playModeCallBack = new SuperPlayerView.PlayModeCallBack() {
            @Override
            public void getPlayMode(SuperPlayerDef.PlayerMode playerMode) {
                if (playerMode.equals(SuperPlayerDef.PlayerMode.FULLSCREEN)) {
                    titleBar.setVisibility(View.GONE);
                } else if (playerMode.equals(SuperPlayerDef.PlayerMode.WINDOW)) {
                    titleBar.setVisibility(View.VISIBLE);
                }
            }
        };

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
//                                long duration = (long) (percentage * mDuration);
//                                if (percentage > maxPercent) {
//                                    maxPercent = percentage;
//                                }

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
//                                long duration = (long) (percentage * mDuration);
//                                if (percentage > maxPercent) {
//                                    maxPercent = percentage;
//                                }
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

        //窗口视频点击监听
        if (null != playerView) {
            playerView.mWindowPlayer.setOnDoubleClick(new WindowPlayer.DoubleClick() {
                @Override
                public void onDoubleClick(DataDTO item) {
                    handler.sendEmptyMessageDelayed(1, CLICK_INTERVAL_TIME);
                }
            });
        }

        handler = new Handler() {
            @Override
            public void dispatchMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        //单击
                        playerView.mWindowPlayer.togglePlayState();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        };
        rootView = findViewById(R.id.rootView);
        newsDetailTitle = findViewById(R.id.news_detail_title);
        introductionText = findViewById(R.id.introduction_text);
        videoCollectionNum = findViewById(R.id.video_collection_num);
        textTv = findViewById(R.id.text_tv);
        refreshLayout = findViewById(R.id.refreshLayout);
        collectionRv = findViewById(R.id.collection_rv);
        sharePopView = View.inflate(this, R.layout.special_5g_share_pop_view, null);
        sharePopDismiss = sharePopView.findViewById(R.id.share_pop_dismiss);
        sharePopDismiss.setOnClickListener(this);
        shareWxBtnRl = sharePopView.findViewById(R.id.share_wx_btn_rl);
        shareWxBtnRl.setOnClickListener(this);
        shareCircleBtnRl = sharePopView.findViewById(R.id.share_circle_btn_rl);
        shareCircleBtnRl.setOnClickListener(this);
        shareQqBtnRl = sharePopView.findViewById(R.id.share_qq_btn_rl);
        shareQqBtnRl.setOnClickListener(this);

        collectionRv.setHasFixedSize(true);
        collectionRv.setNestedScrollingEnabled(false);
        initRefreshLayout();
        getVideoCollectionData(contentId);

    }

    private void initRefreshLayout() {
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (newsVideoDatas.size() > 0) {
                    pageIndex++;
                    getCollectionList(newsCollectionId, String.valueOf(pageIndex), pageSize, false);
                }
            }
        });
    }

    private void addPlayView() {
        height = (int) (ScreenUtils.getPhoneWidth(Special5GNewsDetailActivity.this) / Constants.Horizontal_Proportion);
        playViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        playerView.setLayoutParams(playViewParams);
        playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        if (null != playerView && null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }
        newsDetailPlayview.addView(playerView);
    }

    /**
     * 播放视频
     *
     * @param playUrl
     */
    public void play(String playUrl) {
        if (null != playerView) {
            SuperPlayerModel model = new SuperPlayerModel();
            model.url = playUrl;
            playerView.playWithModel(model);
        }
    }

    /**
     * 获取合集详情
     */
    private void getVideoCollectionData(final String contentId) {
        OkGo.<CollectionTypeModel>get(ApiConstants.getInstance().getVideoCollectionUrl() + contentId)
                .execute(new JsonCallback<CollectionTypeModel>() {
                    @Override
                    public void onSuccess(Response<CollectionTypeModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (null == response.body().getData()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (TextUtils.equals(Constants.success_code, response.body().getCode())) {
                            type = response.body().getData().getType();
                            if (TextUtils.equals(Constants.VIDEO_COM, type)) {
                                newsDetailTitle.setText(response.body().getData().getTitle());
                                introductionText.setText(response.body().getData().getBrief());
                                textTv.setText("选集");
                                videoCollectionNum.setVisibility(View.VISIBLE);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(Special5GNewsDetailActivity.this, 5);
                                collectionRv.setLayoutManager(gridLayoutManager);
                                adapter = new VideoCollectionRvAdapter(Special5GNewsDetailActivity.this, R.layout.video_collection_item, videoComDatas, type);
                                //集数
                                int collectionNum = response.body().getData().getChildren().size();
                                videoCollectionNum.setText("共" + collectionNum + "集");
                                for (int i = 0; i < collectionNum; i++) {
                                    if (i == 0) {
                                        response.body().getData().getChildren().get(i).setCheck(true);
                                        childrenDTO = response.body().getData().getChildren().get(i);
                                    } else {
                                        response.body().getData().getChildren().get(i).setCheck(false);
                                    }
                                    response.body().getData().getChildren().get(i).setIndex(i + 1);
                                    videoComDatas.add(response.body().getData().getChildren().get(i));
                                }
                                adapter.setNewData(videoComDatas);
                                collectionRv.setAdapter(adapter);
                                String url = response.body().getData().getChildren().get(0).getPlayUrl();
                                play(url);
                                //选集
                                adapter.setItemClickListener(new VideoCollectionRvAdapter.ItemClickListener() {
                                    @Override
                                    public void itemClickListener(int position) {
                                        for (int i = 0; i < videoComDatas.size(); i++) {
                                            if (i == position) {
                                                videoComDatas.get(i).setCheck(true);
                                            } else {
                                                videoComDatas.get(i).setCheck(false);
                                            }
                                        }
                                        childrenDTO = videoComDatas.get(position);
                                        adapter.notifyDataSetChanged();
                                        playerView.resetPlayer();
                                        play(videoComDatas.get(position).getPlayUrl());
                                    }
                                });
                            } else {
                                newsDetailTitle.setText(response.body().getData().getTitle());
                                introductionText.setText(response.body().getData().getBrief());
                                textTv.setText(response.body().getData().getChildren().get(0).getTitle());
                                videoCollectionNum.setVisibility(View.GONE);
                                newsCollectionId = response.body().getData().getChildren().get(0).getId();
                                getCollectionList(newsCollectionId, String.valueOf(pageIndex), pageSize, true);
                            }
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }

                    }

                    @Override
                    public void onError(Response<CollectionTypeModel> response) {
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
     * 获取合集列表
     */
    private void getCollectionList(String classId, String pageIndex, String pageSize, final boolean isRefresh) {
        OkGo.<VideoDetailCollectionModel>get(ApiConstants.getInstance().getSpecList())
                .params("classId", classId)
                .params("pageIndex", pageIndex)
                .params("pageSize", pageSize)
                .execute(new JsonCallback<VideoDetailCollectionModel>() {
                    @Override
                    public void onSuccess(Response<VideoDetailCollectionModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (null == response.body().getData()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (TextUtils.equals(Constants.success_code, response.body().getCode())) {
                            if (response.body().getData().getRecords().isEmpty()) {
                                refreshLayout.finishLoadMore();
                            } else {
                                String url = response.body().getData().getRecords().get(0).getPlayUrl();
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(Special5GNewsDetailActivity.this, 2);
                                collectionRv.setLayoutManager(gridLayoutManager);
                                newsVideoAdapter = new NewsVideoCollectionRvAdapter(Special5GNewsDetailActivity.this, R.layout.news_collection_item, newsVideoDatas);
                                for (int i = 0; i < response.body().getData().getRecords().size(); i++) {
                                    if (isRefresh) {
                                        if (i == 0) {
                                            response.body().getData().getRecords().get(i).setCheck(true);
                                            recordsDTO = response.body().getData().getRecords().get(i);
                                        } else {
                                            response.body().getData().getRecords().get(i).setCheck(false);
                                        }
                                    } else {
                                        response.body().getData().getRecords().get(i).setCheck(false);
                                    }

                                    response.body().getData().getRecords().get(i).setIndex(i + 1);
                                    newsVideoDatas.add(response.body().getData().getRecords().get(i));
                                }
                                if (isRefresh) {
                                    play(url);
                                }
                                newsVideoAdapter.setNewData(newsVideoDatas);
                                collectionRv.setAdapter(newsVideoAdapter);
                            }


                            //选集
                            newsVideoAdapter.setNewsVideoItemClick(new NewsVideoCollectionRvAdapter.NewsVideoItemClickListener() {
                                @Override
                                public void newsVideoItemClick(int position) {
                                    for (int i = 0; i < newsVideoDatas.size(); i++) {
                                        if (i == position) {
                                            newsVideoDatas.get(i).setCheck(true);
                                        } else {
                                            newsVideoDatas.get(i).setCheck(false);
                                        }
                                    }
                                    recordsDTO = newsVideoDatas.get(position);
                                    newsVideoAdapter.notifyDataSetChanged();
                                    playerView.resetPlayer();
                                    play(newsVideoDatas.get(position).getPlayUrl());
                                }
                            });
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onError(Response<VideoDetailCollectionModel> response) {
                        super.onError(response);
                        if (null == response.body()) {
                            return;
                        }
                        if (null == response.body().getMessage()) {
                            return;
                        }

                        ToastUtils.showShort(response.body().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        refreshLayout.finishLoadMore();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playerView != null) {
            playerView.mSuperPlayer.stop();
            playerView.release();
            playerView.mSuperPlayer.destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerView.mSuperPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playerView.mSuperPlayer.resume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.news_detail_back) {
            finish();
        } else if (id == R.id.news_detail_share) {
            //分享弹窗
            sharePop();
        } else if (id == R.id.share_pop_dismiss) {
            if (null != sharePop) {
                sharePop.dissmiss();
            }
        } else if (id == R.id.share_wx_btn_rl) {
            if (TextUtils.equals(Constants.VIDEO_COM, type)) {
                toShare(childrenDTO, Constants.SHARE_WX);
            } else {
                toShare(recordsDTO, Constants.SHARE_WX);
            }

        } else if (id == R.id.share_circle_btn_rl) {
            if (TextUtils.equals(Constants.VIDEO_COM, type)) {
                toShare(childrenDTO, Constants.SHARE_CIRCLE);
            } else {
                toShare(recordsDTO, Constants.SHARE_CIRCLE);
            }
        } else if (id == R.id.share_qq_btn_rl) {
            if (TextUtils.equals(Constants.VIDEO_COM, type)) {
                toShare(childrenDTO, Constants.SHARE_QQ);
            } else {
                toShare(recordsDTO, Constants.SHARE_QQ);
            }
        }
    }

    public void toShare(VideoDetailCollectionModel.DataDTO.RecordsDTO item, String platform) {
        VideoInteractiveParam param = VideoInteractiveParam.getInstance();
        ShareInfo shareInfo = ShareInfo.getInstance(item.getShareUrl(), item.getShareImageUrl(),
                item.getShareBrief(), item.getShareTitle(), platform);
        try {
            param.shared(shareInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toShare(CollectionTypeModel.DataDTO.ChildrenDTO item, String platform) {
        VideoInteractiveParam param = VideoInteractiveParam.getInstance();
        ShareInfo shareInfo = ShareInfo.getInstance(item.getShareUrl(), item.getShareImageUrl(),
                item.getShareBrief(), item.getShareTitle(), platform);
        try {
            param.shared(shareInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享弹窗
     */
    private void sharePop() {
        sharePop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(sharePopView)
                .setOutsideTouchable(true)
                .enableBackgroundDark(true)
                .setFocusable(true)
                .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, ButtonSpan.dip2px(160))
                .setAnimationStyle(R.style.take_popwindow_anim)
                .create()
                .showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, -50);
        SystemUtils.showNavigationBar(getWindow().getDecorView());
    }
}