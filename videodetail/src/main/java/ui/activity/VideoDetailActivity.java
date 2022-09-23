package ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
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
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.contants.Contants;
import com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl;
import com.tencent.liteav.demo.superplayer.model.utils.SystemUtils;
import com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer;
import com.tencent.liteav.demo.superplayer.ui.view.PointSeekBar;
import com.tencent.rtmp.TXLiveConstants;
import com.wdcs.callback.JsonCallback;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.manager.BuriedPointModelManager;
import com.wdcs.manager.ContentBuriedPointManager;
import com.wdcs.manager.FinderBuriedPointManager;
import com.wdcs.manager.OnViewPagerListener;
import com.wdcs.manager.ViewPagerLayoutManager;
import com.wdcs.model.CollectionLabelModel;
import com.wdcs.model.CommentLv1Model;
import com.wdcs.model.ContentStateModel;
import com.wdcs.model.DataDTO;
import com.wdcs.model.FinderPointModel;
import com.wdcs.model.RecommendModel;
import com.wdcs.model.ReplyLv2Model;
import com.wdcs.model.ShareInfo;
import com.wdcs.model.TokenModel;
import com.wdcs.model.TrackingUploadModel;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.model.VideoCollectionModel;
import com.wdcs.model.VideoCollectionModel.DataDTO.RecordsDTO;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.DateUtils;
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

import adpter.CommentPopRvAdapter;
import adpter.VideoCollectionAdapter;
import adpter.VideoDetailAdapter;
import model.bean.ActivityRuleBean;
import widget.CollectionClickble;
import widget.CustomLoadMoreView;
import widget.LoadingView;

import com.wdcs.utils.NoScrollViewPager;
import com.wdcs.widget.YALikeAnimationView;

import static android.widget.RelativeLayout.BELOW;
import static com.tencent.liteav.demo.superplayer.SuperPlayerView.mTargetPlayerMode;
import static com.tencent.liteav.demo.superplayer.ui.player.AbsPlayer.formattedTime;
import static com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer.mDuration;
import static com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer.mProgress;
import static com.wdcs.constants.Constants.CLICK_INTERVAL_TIME;
import static com.wdcs.constants.Constants.VIDEOTAG;
import static com.wdcs.constants.Constants.success_code;
import static com.wdcs.constants.Constants.token_error;
import static ui.activity.VideoHomeActivity.maxPercent;
import static ui.activity.VideoHomeActivity.uploadBuriedPoint;
import static utils.NetworkUtil.setDataWifiStates;

public class VideoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView videoDetailRv;
    public VideoCollectionAdapter adapter;
    private CommentPopRvAdapter commentPopRvAdapter;
    //视频列表数据
    public List<RecordsDTO> mDatas = new ArrayList<>();
    //评论列表数据
    private List<MultiItemEntity> mCommentPopRvData;
    private List<CommentLv1Model.DataDTO.RecordsDTO> mCommentPopDtoData;
    public SuperPlayerView playerView;
    private ImageView videoStaticBg;
    private ImageView startPlay;

    //评论列表弹窗
    public CustomPopWindow popupWindow;
    private boolean popupWindowIsShow;

    private View contentView;
    private View chooseContentView;
    private RelativeLayout dismissPop;
    private RecyclerView commentPopRv;
    private TextView commentEdtInput;
    private RelativeLayout collectionBtn;
    private RelativeLayout likesBtn;
    private RelativeLayout commentPopRl;
    private RelativeLayout commentShare;
    //附着在软键盘上的输入弹出窗
    public CustomPopWindow inputAndSendPop;
    private View sendPopContentView;
    private View rootView;
    private EditText edtInput;
    private TextView tvSend;
    private RelativeLayout videoDetailWhiteCommentRl;
    //选择集数弹窗
    public CustomPopWindow choosePop;
    private RecyclerView videoDetailChoosePopRv;

    public ViewPagerLayoutManager videoDetailmanager;
    private ImageView choosePopDismiss;
    public SmartRefreshLayout refreshLayout;
    private String transformationToken = "";
    private String panelCode = "";
    private boolean initialize = true;
    private String mVideoSize = "15"; //每页视频多少条
    private int mPageIndex = 1; //评论列表页数
    private int mPageSize = 10; //评论列表每页多少条
    public String myContentId = ""; //记录当前视频id
    public int currentIndex = 0; //记录当前视频列表的位置
    private TextView commentPopCommentTotal;

    private String videoType; //视频类型
    private VideoInteractiveParam param;
    public String playUrl;
    private String recommendTag = "recommend";
    private boolean isLoadComplate = false;
    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener;
    public View decorView;
    private SoftKeyBoardListener softKeyBoardListener;
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    public CustomPopWindow sharePop;
    private View sharePopView;
    private ImageView shareWxBtn;
    private ImageView shareCircleBtn;
    private ImageView shareQqBtn;
    public RecordsDTO mDataDTO;
    private List<RecommendModel.DataDTO.RecordsDTO> recommondList;
    private ViewGroup rlLp;
    private VideoChannelModel videoChannelModel;

    private VideoChannelModel channelModel;
    private Bundle args;
    //    private RelativeLayout.LayoutParams lp;
    private LoadingView loadingProgress;
    private boolean isFollow; //是否关注
    public double pointPercent;// 每一次记录的节点播放百分比
    //    private long everyOneDuration; //每一次记录需要上报的播放时长 用来分段上报埋点
    private long lsDuration = 0; //每一次上报临时保存的播放时长
    private boolean isCheckState; //是否请求了检查点赞收藏状态的接口
    private TextView zxpl;
    private String negativeScreenContentId;
    private DataDTO negativeScreenDto;
    private View footerView;
    public ActivityRuleBean activityRuleBean;
    public ImageView activityRuleImg; //活动规则图
    public ImageView activityRuleAbbreviation;
    public ImageView activityToAbbreviation; //变为缩略图按钮
    public boolean isAbbreviation; //当前是否是缩略图
    public long videoOldSystemTime;
    public long videoReportTime;
    private String mCategoryName = "";
    private boolean isReply = false;
    private String replyId;
    private List<CollectionLabelModel.DataDTO.ListDTO> collectionList;
    private List<String> collectionTvList;
    private String classId;
    private RelativeLayout backRl;
    private ImageView back;
    private int videoPageIndex = 1;
    private RelativeLayout.LayoutParams playViewParams;
    private String className;
    private boolean isShow = true;
    private TextView collectionNum; //收藏数
    private View commentEmptyView;
    private TextView commentListTips;
    private YALikeAnimationView loveIcon;
    private long beforeClickTime = 0;
    private boolean likeIsRequesting;
    private Handler handler;
    private TextView commentTitle;
    private List<String> collectionStrList;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        SystemUtils.setNavbarColor(this, R.color.video_black);
        decorView = getWindow().getDecorView();
        setContentView(R.layout.activity_video_detail);
        initView();
        if (!TextUtils.isEmpty(classId)) {
//            getPullDownData(mVideoSize, panelCode, "false", Constants.REFRESH_TYPE);
            getCollectionList(classId, String.valueOf(videoPageIndex), String.valueOf(mVideoSize));
        } else if (!TextUtils.isEmpty(myContentId)) {
            getOneVideo(myContentId);
        }
    }

    private void initView() {
        playerView = new SuperPlayerView(this, getWindow().getDecorView(), true);
        classId = getIntent().getStringExtra("classId");
        panelCode = getIntent().getStringExtra("panelId");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("contentId"))) {
            myContentId = getIntent().getStringExtra("contentId");
        }
        className = getIntent().getStringExtra("className");
        if (TextUtils.isEmpty(className)) {
            className = "";
        } else {
            className = "  " + className;
        }
        backRl = findViewById(R.id.back_rl);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        param = VideoInteractiveParam.getInstance();
        collectionNum = findViewById(R.id.collection_num);
        mDataDTO = new RecordsDTO();
        recommondList = new ArrayList<>();
        loadingProgress = findViewById(R.id.video_loading_progress);
        loadingProgress.setVisibility(View.VISIBLE);
        videoDetailRv = findViewById(R.id.video_detail_rv);
        videoDetailRv.setHasFixedSize(true);
        videoDetailmanager = new ViewPagerLayoutManager(VideoDetailActivity.this);
        videoDetailRv.setLayoutManager(videoDetailmanager);
        footerView = View.inflate(VideoDetailActivity.this, R.layout.footer_view, null);
        commentEmptyView = View.inflate(this, R.layout.comment_list_empty, null);
        commentListTips = commentEmptyView.findViewById(R.id.comment_list_tips);
        loveIcon = findViewById(R.id.love_icon);
        setSoftKeyBoardListener();

        /**
         * 监听播放器播放窗口变化回调
         */
        playerView.playModeCallBack = new SuperPlayerView.PlayModeCallBack() {
            @Override
            public void getPlayMode(SuperPlayerDef.PlayerMode playerMode) {
                LinearLayout videoFragmentFullLin = (LinearLayout) adapter.getViewByPosition(currentIndex, R.id.superplayer_iv_fullscreen);
                if (playerMode.equals(SuperPlayerDef.PlayerMode.FULLSCREEN)) {
                    videoDetailmanager.setCanScoll(false);
                    refreshLayout.setEnableRefresh(false);
                    adapter.setEnableLoadMore(false);
                    if (null != popupWindow) {
                        popupWindow.dissmiss();
                    }

                    if (null != inputAndSendPop) {
                        inputAndSendPop.dissmiss();
                    }

                    if (null != choosePop) {
                        choosePop.dissmiss();
                    }

                    if (null != noLoginTipsPop) {
                        noLoginTipsPop.dissmiss();
                    }

                    if (null != sharePop) {
                        sharePop.dissmiss();
                    }

                    if (null != adapter.getViewByPosition(currentIndex, R.id.introduce_lin)) {
                        adapter.getViewByPosition(currentIndex, R.id.introduce_lin).setVisibility(View.GONE);
                    }

                    if (null != videoFragmentFullLin) {
                        videoFragmentFullLin.setVisibility(View.GONE);
                    }

                    if (null != adapter.getViewByPosition(currentIndex, R.id.horizontal_video_wdcs_logo)) {
                        adapter.getViewByPosition(currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.GONE);
                    }

                    if (null != adapter.getViewByPosition(currentIndex, R.id.cover_picture)) {
                        adapter.getViewByPosition(currentIndex, R.id.cover_picture).setVisibility(View.GONE);
                    }

                    if (null != adapter.getViewByPosition(currentIndex, R.id.user_todo)) {
                        adapter.getViewByPosition(currentIndex, R.id.user_todo).setVisibility(View.GONE);
                    }

                    if (null != adapter.getViewByPosition(currentIndex, R.id.video_item_bottom)) {
                        adapter.getViewByPosition(currentIndex, R.id.video_item_bottom).setVisibility(View.GONE);
                    }

                    if (null != playerView.mWindowPlayer.mLayoutBottom) {
                        playerView.mWindowPlayer.mLayoutBottom.setVisibility(View.GONE);
                    }

                    backRl.setVisibility(View.GONE);

                    KeyboardUtils.hideKeyboard(getWindow().getDecorView());
                } else if (playerMode.equals(SuperPlayerDef.PlayerMode.WINDOW)) {
                    videoDetailmanager.setCanScoll(true);
                    if (!TextUtils.isEmpty(classId)) {
                        refreshLayout.setEnableRefresh(true);
                    }
                    adapter.setEnableLoadMore(true);
                    setLikeCollection(playerView.contentStateModel);
                    if (null != videoFragmentFullLin) {
                        videoFragmentFullLin.setVisibility(View.VISIBLE);
                    }

                    if (null != adapter.getViewByPosition(currentIndex, R.id.introduce_lin)) {
                        adapter.getViewByPosition(currentIndex, R.id.introduce_lin).setVisibility(View.VISIBLE);
                    }
                    if (null != adapter.getViewByPosition(currentIndex, R.id.horizontal_video_wdcs_logo)) {
                        adapter.getViewByPosition(currentIndex, R.id.horizontal_video_wdcs_logo).setVisibility(View.VISIBLE);
                    }

                    if (null != adapter.getViewByPosition(currentIndex, R.id.cover_picture)) {
                        adapter.getViewByPosition(currentIndex, R.id.cover_picture).setVisibility(View.VISIBLE);
                    }

                    if (null != adapter.getViewByPosition(currentIndex, R.id.user_todo)) {
                        adapter.getViewByPosition(currentIndex, R.id.user_todo).setVisibility(View.VISIBLE);
                    }

                    if (null != adapter.getViewByPosition(currentIndex, R.id.video_item_bottom)) {
                        adapter.getViewByPosition(currentIndex, R.id.video_item_bottom).setVisibility(View.VISIBLE);
                    }

                    if (null != playerView.mWindowPlayer.mLayoutBottom) {
                        playerView.mWindowPlayer.mLayoutBottom.setVisibility(View.VISIBLE);
                    }

                    backRl.setVisibility(View.VISIBLE);
                }
            }
        };

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
                videoDetailmanager.setCanScoll(false);
            }

            @Override
            public void onStopTrackingTouch(PointSeekBar seekBar) {
                int curProgress = seekBar.getProgress();
                int maxProgress = seekBar.getMax();
                if (mTargetPlayerMode == SuperPlayerDef.PlayerMode.WINDOW) {
                    videoDetailmanager.setCanScoll(true);
                } else {
                    videoDetailmanager.setCanScoll(false);
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


        //开始播放回调
        SuperPlayerImpl.setReadPlayCallBack(new SuperPlayerImpl.ReadPlayCallBack() {
            @Override
            public void ReadPlayCallback() {
                String isRenew = "";
                if (null == playerView.buriedPointModel.getXksh_renew() || TextUtils.equals("false", playerView.buriedPointModel.getXksh_renew())) {
//                      //不为重播
                    isRenew = "否";
                    videoOldSystemTime = DateUtils.getTimeCurrent();
                    String event;
                    event = Constants.CMS_VIDEO_PLAY;
                    if (null != mDataDTO && !TextUtils.isEmpty(mDataDTO.getVolcCategory())) {
                        uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoDetailActivity.this, mDataDTO.getThirdPartyId(), "", "", event, mDataDTO.getVolcCategory(), mDataDTO.getRequestId()), event);
                    }
                } else {
                    isRenew = "是";
                }

                FinderBuriedPointManager.setFinderVideoPlay(Constants.CONTENT_VIDEO_PLAY, isRenew, mDataDTO);
            }
        });

        //自动播放/拖动进度条 播放结束回调
        SuperPlayerImpl.setDetailAutoPlayOverCallBack(new SuperPlayerImpl.DetailAutoPlayOverCallBack() {
            @Override
            public void DetailAutoPlayOverCallBack() {
                if (isShow) {
                    playerView.mSuperPlayer.reStart();
                }
            }
        });


        videoDetailmanager.setOnViewPagerListener(new OnViewPagerListener() {


            @Override
            public void onInitComplete() {
                if (initialize) {
                    return;
                }
                initialize = true;

                if (mDatas.isEmpty()) {
                    return;
                }
                mDataDTO = mDatas.get(0);
                if (null != adapter.getViewByPosition(0, R.id.superplayer_iv_fullscreen)) {
                    if (TextUtils.equals("2", videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(0).getWidth())),
                            Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(0).getHeight()))))) {
                        adapter.getViewByPosition(0, R.id.superplayer_iv_fullscreen).setVisibility(View.VISIBLE);
                    } else {
                        adapter.getViewByPosition(0, R.id.superplayer_iv_fullscreen).setVisibility(View.GONE);
                    }
                }

//                playerView.mWindowPlayer.setDataDTO(mDataDTO, mDataDTO);
                playerView.mWindowPlayer.setViewpager((NoScrollViewPager) VideoDetailActivity.this.findViewById(R.id.video_vp));
                playerView.mWindowPlayer.setIsTurnPages(false);
                playerView.mWindowPlayer.setManager(videoDetailmanager);
                playerView.mFullScreenPlayer.setRecordsDTO(mDataDTO);
                myContentId = String.valueOf(mDatas.get(0).getId());
                addPageViews(myContentId);
                OkGo.getInstance().cancelTag("contentState");
                getContentState(myContentId);
//                getThematicCollection(myContentId);
//                setCollection();

                SuperPlayerImpl.mCurrentPlayVideoURL = mDatas.get(0).getPlayUrl();
                currentIndex = 0;
                mPageIndex = 1;
                if (mDatas.get(0).getDisableComment()) {
                    commentListTips.setText("当前页面评论功能已关闭");
                    commentPopRl.setEnabled(false);
                    commentEdtInput.setHint("当前页面评论功能已关闭");
                } else {
                    commentListTips.setText("暂无任何评论，快来抢沙发吧！");
                    commentPopRl.setEnabled(true);
                    commentEdtInput.setHint("写评论...");
                }
                commentPopRvAdapter.setEmptyView(commentEmptyView);
                getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), true);
                videoType = mDatas.get(0).getType();
                rlLp = (ViewGroup) videoDetailmanager.findViewByPosition(0);
                OkGo.getInstance().cancelTag(recommendTag);
                //获取推荐列表
                getRecommend(myContentId, 0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
            }

            @Override
            public void onPageSelected(final int position, boolean isBottom) {
                if (null == playerView) {
                    return;
                }

                if (null != playerView.getTag() && position == (int) playerView.getTag()) {
                    return;
                }

                //避免越界
                if (mDatas.isEmpty()) {
                    return;
                }

                if (null == mDatas.get(position)) {
                    return;
                }
                //露出 即上报
//              ContentBuriedPointManager.setContentBuriedPoint();
                playerView.mWindowPlayer.hide();
                if (!TextUtils.isEmpty(mDataDTO.getVolcCategory())) {
                    if (mDuration != 0 && mProgress != 0) {
                        //上报埋点
                        long evePlayTime = Math.abs(mProgress - lsDuration);
                        double currentPercent = (evePlayTime * 1.0 / mDuration);
                        double uploadPercent = 0;
                        if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
//                      //不为重播
                            if (currentPercent > maxPercent) {
                                uploadPercent = currentPercent;
                                maxPercent = currentPercent;
                            } else {
                                uploadPercent = maxPercent;
                            }
                        } else {
                            uploadPercent = 1;
                        }
                        videoReportTime = DateUtils.getTimeCurrent() - videoOldSystemTime;
                        BigDecimal two = new BigDecimal(uploadPercent);
                        double pointPercentTwo = two.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                        String event;
                        event = Constants.CMS_VIDEO_OVER;
                        uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoDetailActivity.this, mDataDTO.getThirdPartyId(), String.valueOf(videoReportTime), String.valueOf(Math.floor(pointPercentTwo * 100)), event, mDataDTO.getVolcCategory(), mDataDTO.getRequestId()), event);
//                        DebugLogUtils.DebugLog("埋点事件：" + event + "播放时长:" + videoReportTime + "---" + "播放百分比:" + pointPercentTwo);
                        Log.e("video_md", "埋点事件：" + event + "播放时长:" + videoReportTime + "---" + "播放百分比:" + pointPercentTwo);
                    }
                }

                String isFinish;
                if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
                    isFinish = "否";
                } else {
                    isFinish = "是";
                }
                FinderBuriedPointManager.setFinderVideo(Constants.CONTENT_VIDEO_DURATION, "", mDataDTO, videoReportTime, isFinish);

                mDataDTO = mDatas.get(position);
                if (null != adapter.getViewByPosition(position, R.id.superplayer_iv_fullscreen)) {
                    if (TextUtils.equals("2", videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(position).getWidth())),
                            Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(position).getHeight()))))) {
                        adapter.getViewByPosition(position, R.id.superplayer_iv_fullscreen).setVisibility(View.VISIBLE);
                    } else {
                        adapter.getViewByPosition(position, R.id.superplayer_iv_fullscreen).setVisibility(View.GONE);
                    }
                }

//                DebugLogUtils.DebugLog(mDataDTO.isFullBtnIsShow() + "状态" + "---视频宽：" + mDataDTO.getWidth() + "视频高:" + mDataDTO.getHeight() + "视频类型---" +
//                        videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDataDTO.getWidth())),
//                                Integer.parseInt(NumberFormatTool.getNumStr(mDataDTO.getHeight()))));


                //滑动下一条或者上一条视频
                playerView.mWindowPlayer.setRecordDuration(0);
                lsDuration = 0;
                maxPercent = 0;
                SuperPlayerImpl.mCurrentPlayVideoURL = mDatas.get(position).getPlayUrl();
                playUrl = mDatas.get(position).getPlayUrl();
//                playerView.mWindowPlayer.setDataDTO(mDataDTO, mDatas.get(currentIndex));
                playerView.mFullScreenPlayer.setRecordsDTO(mDataDTO);
                playerView.mWindowPlayer.setIsTurnPages(true);
                currentIndex = position;
//                choosePopDatas.clear();
                reset();
                myContentId = String.valueOf(mDatas.get(position).getId());
                //重置重播标识
                if (null != playerView && null != playerView.buriedPointModel) {
                    playerView.buriedPointModel.setIs_renew("false");
                }
                addPageViews(myContentId);
                videoType = mDatas.get(position).getType();
                mPageIndex = 1;
                if (mDatas.get(position).getDisableComment()) {
                    commentListTips.setText("当前页面评论功能已关闭");
                    commentPopRl.setEnabled(false);
                    commentEdtInput.setHint("当前页面评论功能已关闭");
                } else {
                    commentListTips.setText("暂无任何评论，快来抢沙发吧！");
                    commentPopRl.setEnabled(true);
                    commentEdtInput.setHint("写评论...");
                }
                commentPopRvAdapter.setEmptyView(commentEmptyView);
                getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), true);
                getContentState(myContentId);
//                getThematicCollection(myContentId);
                setCollection();
                rlLp = (ViewGroup) videoDetailmanager.findViewByPosition(position);
                OkGo.getInstance().cancelTag(recommendTag);
                getRecommend(myContentId, position);

                if (!"1".equals(playerView.mFullScreenPlayer.strSpeed)) {
                    playerView.mFullScreenPlayer.mVodMoreView.mCallback.onSpeedChange(1.0f);
                    playerView.mFullScreenPlayer.superplayerSpeed.setText("倍速");
                    playerView.mFullScreenPlayer.mRbSpeed1.setChecked(true);
                }
            }
        });

        initSmartRefresh();

        contentView = View.inflate(VideoDetailActivity.this, R.layout.fragment_video_comment_pop, null);
        sendPopContentView = View.inflate(VideoDetailActivity.this, R.layout.layout_input_window, null);
        commentPopCommentTotal = contentView.findViewById(R.id.comment_pop_comment_total);
        edtInput = sendPopContentView.findViewById(R.id.edtInput);
        tvSend = sendPopContentView.findViewById(R.id.tvSend);
        commentTitle = contentView.findViewById(R.id.comment_title);
        commentTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(VideoDetailActivity.this, TgtCodeActivity.class));
                return true;
            }
        });

        noLoginTipsView = View.inflate(VideoDetailActivity.this, R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);

        sharePopView = View.inflate(VideoDetailActivity.this, R.layout.share_pop_view, null);
        shareWxBtn = sharePopView.findViewById(R.id.share_wx_btn);
        shareWxBtn.setOnClickListener(this);
        shareCircleBtn = sharePopView.findViewById(R.id.share_circle_btn);
        shareCircleBtn.setOnClickListener(this);
        shareQqBtn = sharePopView.findViewById(R.id.share_qq_btn);
        shareQqBtn.setOnClickListener(this);

        activityRuleImg = findViewById(R.id.activity_rule_img);
        activityRuleImg.setOnClickListener(this);
        activityToAbbreviation = findViewById(R.id.activity_to_abbreviation);
        activityToAbbreviation.setOnClickListener(this);
        activityRuleAbbreviation = findViewById(R.id.activity_rule_abbreviation);
        activityRuleAbbreviation.setOnClickListener(this);

        /**
         * 发送评论
         */
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInput.getText())) {
                    Toast.makeText(VideoDetailActivity.this, "请输入评论", Toast.LENGTH_SHORT).show();
                } else {
                    if (isReply) {
                        toReply(replyId);
                    } else {
                        toComment(edtInput.getText().toString(), myContentId);
                    }
                    edtInput.setText("");
                }
            }
        });

        rootView = findViewById(R.id.root);

        dismissPop = contentView.findViewById(R.id.dismiss_pop);
        dismissPop.setOnClickListener(this);
        commentPopRv = contentView.findViewById(R.id.comment_pop_rv);
        commentEdtInput = contentView.findViewById(R.id.comment_edtInput);
        commentPopRl = contentView.findViewById(R.id.comment_pop_rl);
        commentPopRl.setOnClickListener(this);
        initCommentPopRv();
        adapter = new VideoCollectionAdapter(R.layout.video_fragment_item, mDatas, VideoDetailActivity.this,
                playerView, refreshLayout, videoDetailmanager, className);
        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setPreLoadNumber(2);
        adapter.openLoadAnimation();

        handler = new Handler() {
            @Override
            public void dispatchMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        //单击
                        playerView.mWindowPlayer.togglePlayState();
                        break;
                    case 2:
                        //双击
                        if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                            noLoginTipsPop();
                        } else {
                            loveIcon.startAnimation();
                            if (TextUtils.equals("false", playerView.contentStateModel.getWhetherLike()) && !likeIsRequesting) {
                                likeIsRequesting = true;
                                ImageView likeImage = (ImageView) adapter.getViewByPosition(currentIndex, R.id.video_detail_likes_image);
                                TextView likeNum = (TextView) adapter.getViewByPosition(currentIndex, R.id.likes_num);
                                addOrCancelLike(myContentId, videoType, likeImage, likeNum);
                            }
                        }
                        break;
                }
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final BaseQuickAdapter adapter, View view, int position) {
                if (System.currentTimeMillis() - beforeClickTime < CLICK_INTERVAL_TIME) {
                    handler.removeMessages(1);
                    handler.sendEmptyMessage(2);
                } else {
                    if (!handler.hasMessages(2) && !handler.hasMessages(1))
                        handler.sendEmptyMessageDelayed(1, CLICK_INTERVAL_TIME);
                }
                beforeClickTime = System.currentTimeMillis();
            }
        });
        adapter.setOnLoadMoreListener(requestLoadMoreListener, videoDetailRv);

        /**
         * 无wifi 继续播放点击
         */
        adapter.setToAddPlayerViewClick(new VideoDetailAdapter.ToAddPlayerViewClick() {
            @Override
            public void clickNoWifi(int position) {
                SPUtils.getInstance().put(Constants.AGREE_NETWORK, "1");
                for (int i = 0; i < mDatas.size(); i++) {
                    if (null != mDatas.get(i)) {
                        mDatas.get(i).setWifi(true);
                    }
                }
                for (int i = 0; i < mDatas.size(); i++) {
                    if (null != mDatas.get(i)) {
                        mDatas.get(i).setWifi(true);
                    }
                }
                addPlayView(position);
                adapter.notifyDataSetChanged();
            }
        });

        //点赞
        adapter.setlikeListener(new VideoCollectionAdapter.LikeListener() {
            @Override
            public void likeClick(View view, RecordsDTO item, ImageView likeImage, TextView likeNum) {
                if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                    noLoginTipsPop();
                } else {
                    addOrCancelLike(myContentId, videoType, likeImage, likeNum);
                }
            }
        });

        //收藏
        adapter.setCollectionListener(new VideoCollectionAdapter.CollectionListener() {
            @Override
            public void collectionClick(View view, RecordsDTO item, ImageView collectionImage, TextView collectionNum) {
                if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                    noLoginTipsPop();
                } else {
                    addOrCancelFavor(myContentId, videoType, collectionImage, collectionNum);
                }
            }
        });

        //评论
        adapter.setCommentListener(new VideoCollectionAdapter.CommentListener() {
            @Override
            public void commentClick(View view, RecordsDTO item, TextView commentNum) {
                FinderBuriedPointManager.setFinderClick("评论");
                showCommentPopWindow();
            }
        });

        //转发
        adapter.setShareListener(new VideoCollectionAdapter.ShareListener() {
            @Override
            public void shareClick(View view, RecordsDTO item) {
                FinderBuriedPointManager.setFinderLikeFavoriteShare(Constants.CONTENT_TRANSMIT, mDataDTO);
                FinderBuriedPointManager.setFinderClick("分享");
                sharePop();
            }
        });

        //发布
        adapter.setPublishWorksListener(new VideoCollectionAdapter.PublishWorksListener() {
            @Override
            public void publishWorksClick(View view, RecordsDTO item) {
                //行为埋点 点击视频发布
                if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                    try {
                        noLoginTipsPop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(VideoDetailActivity.this, UploadActivity.class);
                    intent.putExtra("module_source", "视频详情");
                    startActivity(intent);
                }
            }
        });


        /**
         * 关注按钮
         */
        adapter.setFollowViewClick(new VideoCollectionAdapter.FollowViewClick() {
            @Override
            public void followClick(int position) {
                if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                    noLoginTipsPop();
                } else {
                    if (isFollow) {
                        //调用取消关注接口
                        cancelFollow(mDatas.get(position).getCreateBy());
                    } else {
                        //调用关注接口
                        toFollow(mDatas.get(position).getCreateBy());
                    }
                }
            }
        });

        //窗口视频双击点击监听
        //窗口视频双击点击监听
        if (null != playerView) {
            playerView.mWindowPlayer.setOnDoubleClick(new WindowPlayer.DoubleClick() {
                @Override
                public void onDoubleClick(DataDTO item) {
                    if (System.currentTimeMillis() - beforeClickTime < CLICK_INTERVAL_TIME) {
                        handler.removeMessages(1);
                        handler.sendEmptyMessage(2);
                    } else {
                        if (!handler.hasMessages(2) && !handler.hasMessages(1))
                            handler.sendEmptyMessageDelayed(1, CLICK_INTERVAL_TIME);
                    }
                    beforeClickTime = System.currentTimeMillis();
                }
            });
        }

        videoDetailRv.setAdapter(adapter);
    }


    /**
     * 关注
     *
     * @param targetUserId
     */
    private void toFollow(String targetUserId) {
        OkGo.<TrackingUploadModel>post(ApiConstants.getInstance().toFollow() + targetUserId)
                .tag(VIDEOTAG)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .execute(new JsonCallback<TrackingUploadModel>() {
                    @Override
                    public void onSuccess(Response<TrackingUploadModel> response) {
                        try {
                            if (200 == response.body().getCode()) {
                                FinderPointModel model = new FinderPointModel();
                                model.setUser_id(mDataDTO.getCreateBy());
                                FinderBuriedPointManager.setFinderCommon(Constants.NOTICE_USER, model);
                                setFollowView("true");
                                //行为埋点 关注用户 关注的用户id mDataDTO.getCreateBy()
                            } else {
                                ToastUtils.showShort(response.body().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<TrackingUploadModel> response) {
                        super.onError(response);
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
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
     * 设置关注
     */
    private void setFollowView(String whetherFollow) {
        /**
         * 设置关注
         */
        TextView followText = (TextView) adapter.getViewByPosition(currentIndex, R.id.follow);
        String localUserId = PersonInfoManager.getInstance().getUserId();
        String userId = mDataDTO.getCreateBy();

        if (null == followText) {
            return;
        }
        if (TextUtils.equals(whetherFollow, "true")) {
            //已关注
            isFollow = true;
            followText.setText("已关注");
            followText.setBackgroundResource(R.drawable.followed_bg);
        } else {
            //未关注
            isFollow = false;
            followText.setText("关注");
            followText.setBackgroundResource(R.drawable.follow_bg);
        }
    }


    /**
     * 取消关注
     *
     * @param targetUserId
     */
    private void cancelFollow(String targetUserId) {
        OkGo.<TrackingUploadModel>post(ApiConstants.getInstance().cancelFollow() + targetUserId)
                .tag(VIDEOTAG)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .execute(new JsonCallback<TrackingUploadModel>() {
                    @Override
                    public void onSuccess(Response<TrackingUploadModel> response) {
                        if (200 == response.body().getCode()) {
                            setFollowView("false");
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onError(Response<TrackingUploadModel> response) {
                        super.onError(response);
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
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
     * 获取单条视频详情
     */
    private void getOneVideo(final String contentId) {
        mDatas.clear();
        OkGo.<String>get(ApiConstants.getInstance().getVideoDetailUrl() + contentId)
                .tag(VIDEOTAG)
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
                                    RecordsDTO dataDTO = JSON.parseObject(json, RecordsDTO.class);
                                    if (null == dataDTO) {
                                        return;
                                    }
                                    initialize = false;
                                    mDatas.add(dataDTO);
                                    setDataWifiStates(mDatas, VideoDetailActivity.this);
                                    adapter.setNewData(mDatas);
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
                        adapter.loadMoreEnd();
                        refreshLayout.setEnableRefresh(false);
                        getThematicCollection(myContentId);
                    }
                });
    }

    /**
     * 获取专题视频列表
     */
    private void getCollectionList(String collectionId, String mPageIndex, String mPageSize) {
        if (null != playerView && null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }
        mDatas.clear();
        OkGo.<VideoCollectionModel>get(ApiConstants.getInstance().getSpecList())
                .tag(VIDEOTAG)
                .params("classId", collectionId)
                .params("pageIndex", mPageIndex)
                .params("pageSize", mPageSize)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<VideoCollectionModel>(VideoCollectionModel.class) {

                    @Override
                    public void onSuccess(Response<VideoCollectionModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        try {
                            if (response.body().getCode().equals(success_code)) {
                                if (null == response.body().getData() && response.body().getData().getRecords().size() == 0) {
                                    ToastUtils.showShort(R.string.data_err);
                                    return;
                                }

                                mDatas.addAll(response.body().getData().getRecords());
                                for (int i = 0; i < mDatas.size(); i++) {
                                    String videoType = videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(i).getWidth())),
                                            Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(i).getHeight())));
                                    mDatas.get(i).setLogoType(videoType);
                                }

                                setDataWifiStates(mDatas, VideoDetailActivity.this);
                                adapter.setNewData(mDatas);
                                if (mDatas.size() > 0) {
                                    initialize = false;
                                }
                                //                            videoDetailCommentBtn.setVisibility(View.VISIBLE);
                            } else {
                                //                            videoDetailCommentBtn.setVisibility(View.GONE);
                            }
                            if (null != refreshLayout) {
                                refreshLayout.finishRefresh();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<VideoCollectionModel> response) {
                        super.onError(response);
                        if (null != response.body()) {
//                            ToastUtils.showShort(response.body().getMessage());
                            return;
                        }
                        ToastUtils.showShort(R.string.net_err);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        loadingProgress.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 在视频playview位置上添加各种view
     *
     * @param position
     */
    public void addPlayView(final int position) {
        if (null == playerView) {
            return;
        }
        if (null != playerView.mWindowPlayer && null != playerView.mWindowPlayer.mLayoutBottom && null != playerView.mWindowPlayer.mLayoutBottom.getParent()) {
            ((ViewGroup) playerView.mWindowPlayer.mLayoutBottom.getParent()).removeView(playerView.mWindowPlayer.mLayoutBottom);
        }

        if (null != playerView && null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }
        RelativeLayout itemRelativelayout = (RelativeLayout) adapter.getViewByPosition(position, R.id.item_relativelayout);

        String videoType = videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(position).getWidth())),
                Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(position).getHeight())));
        if (TextUtils.equals("0", videoType)) {
            double percent = Double.parseDouble(mDatas.get(position).getWidth()) / Double.parseDouble(mDatas.get(position).getHeight());
            double mHeight;
            mHeight = getResources().getDisplayMetrics().widthPixels / percent;
            playViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) mHeight);
            playViewParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            playViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            playViewParams.setMargins(0, 0, 0, 0);
            playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
            playerView.setOrientation(false);
            if (null != itemRelativelayout) {
                itemRelativelayout.addView(playerView.mWindowPlayer.mLayoutBottom);
            }
        } else if (TextUtils.equals("1", videoType)) {
            int height = (int) (ScreenUtils.getPhoneWidth(VideoDetailActivity.this) / Constants.Portrait_Proportion);
            playViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            playViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            if (phoneIsNormal()) {
                playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
                playerView.setOrientation(false);
            } else {
                playViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                playerView.setOrientation(false);
            }
            if (null != itemRelativelayout) {
                itemRelativelayout.addView(playerView.mWindowPlayer.mLayoutBottom);
            }
        } else {
            int height = (int) (ScreenUtils.getPhoneWidth(VideoDetailActivity.this) / Constants.Horizontal_Proportion);
            playViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            playViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            playerView.setOrientation(true);
            if (null != itemRelativelayout) {
                itemRelativelayout.addView(playerView.mWindowPlayer.mLayoutBottom);
            }
        }
        playerView.setLayoutParams(playViewParams);
        playerView.setTag(position);
        if (rlLp != null) {
            rlLp.addView(playerView, 1);
            //露出即上报
            if (!TextUtils.isEmpty(mDataDTO.getVolcCategory())) {
                uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoDetailActivity.this, mDataDTO.getThirdPartyId(), "", "", Constants.CMS_CLIENT_SHOW, mDataDTO.getVolcCategory(), mDataDTO.getRequestId()), Constants.CMS_CLIENT_SHOW);
            }
            play(mDatas.get(position).getPlayUrl(), mDatas.get(position).getTitle());
        }
    }


    /**
     * 视频是否是16：9
     * 0 :  竖版视频非16：9
     * 1 ：  竖版视频16：9
     * 2 ：  横板视频
     */
    public static String videoIsNormal(int videoWidth, int videoHeight) {
        if (videoWidth == 0 || videoHeight == 0) {
            return "2";
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
        int phoneWidth = ScreenUtils.getPhoneWidth(VideoDetailActivity.this);
        int phoneHeight = ScreenUtils.getPhoneHeight(VideoDetailActivity.this);
        if (phoneHeight * 9 == phoneWidth * 16) {
            return true;
        } else {
            return false;
        }
    }

    private void initSmartRefresh() {
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (playerView.mSuperPlayer.getPlayerState() == SuperPlayerDef.PlayerState.PLAYING) {
                    playerView.mSuperPlayer.pause();
                }
                isLoadComplate = false;
                adapter.setOnLoadMoreListener(requestLoadMoreListener, videoDetailRv);
//                getPullDownData(mVideoSize, panelCode, "false", Constants.REFRESH_TYPE);
                videoPageIndex = 1;
                getCollectionList(classId, String.valueOf(videoPageIndex), String.valueOf(mVideoSize));
                //重置重播标识
                if (null != playerView && null != playerView.buriedPointModel) {
                    playerView.buriedPointModel.setIs_renew("false");
                }
            }
        });

        requestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadComplate) {
                    videoDetailRv.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mDatas.isEmpty()) {
                                adapter.loadMoreFail();
                                return;
                            }
                            videoPageIndex++;
                            loadMoreData(classId, String.valueOf(videoPageIndex), mVideoSize);
                        }
                    });
                }
            }
        };
    }


    /**
     * 初始化评论列表
     */
    private void initCommentPopRv() {
        mCommentPopRvData = new ArrayList<>();
        mCommentPopDtoData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VideoDetailActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentPopRv.setLayoutManager(linearLayoutManager);
        commentPopRvAdapter = new CommentPopRvAdapter(mCommentPopRvData, VideoDetailActivity.this);
        commentPopRvAdapter.bindToRecyclerView(commentPopRv);
        commentPopRvAdapter.setEmptyView(R.layout.comment_list_empty);
        commentPopRvAdapter.expandAll();
        commentPopRv.setAdapter(commentPopRvAdapter);
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
     * 评论列表弹出框
     */
    private void showCommentPopWindow() {
        if (null == popupWindow) {
            //创建并显示popWindow
            popupWindow = new CustomPopWindow.PopupWindowBuilder(VideoDetailActivity.this)
                    .setView(contentView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                    .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, Utils.getContext().getResources().getDisplayMetrics().heightPixels - ButtonSpan.dip2px(200))
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .create()
                    .showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        } else {
            popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
//        SystemUtils.hideBottomUIMenuForPopupWindow(popupWindow);
        popupWindow.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                SystemUtils.hideSystemUI(decorView);
            }
        });

    }

    /**
     * 选集弹窗
     */
    public void showChoosePop() {
        if (null == choosePop) {
            //创建并显示popWindow
            choosePop = new CustomPopWindow.PopupWindowBuilder(VideoDetailActivity.this)
                    .setView(chooseContentView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .create()
                    .showAtLocation(VideoDetailActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            choosePop.showAtLocation(VideoDetailActivity.this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }

        choosePop.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                SystemUtils.hideSystemUI(decorView);
            }
        });
    }

    /**
     * 分享弹窗
     */
    private void sharePop() {
        if (null == sharePop) {
            sharePop = new CustomPopWindow.PopupWindowBuilder(VideoDetailActivity.this)
                    .setView(sharePopView)
                    .setOutsideTouchable(true)
                    .setFocusable(true)
                    .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, ButtonSpan.dip2px(150))
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .create()
                    .showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        } else {
            sharePop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
    }

    public void toShare(RecordsDTO item, String platform) {
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
     * 播放视频
     *
     * @param playUrl
     */
    public void play(String playUrl, String title) {
        if (null != playerView) {
            SuperPlayerModel model = new SuperPlayerModel();
            model.url = playUrl;
            model.title = title;
            model.contentId = myContentId;
            playerView.playWithModel(model);
        }
    }

    public void stop() {
        if (null != playerView && null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }
        playerView.stopPlay();
    }

    public void reset() {
        if (null != playerView && null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }

        if (null != playerView) {
            playerView.resetPlayer();
        }
    }

    /**
     * 获取更多数据
     */
    private void loadMoreData(String collectionId, String mPageIndex, String mPageSize) {
        OkGo.<VideoCollectionModel>get(ApiConstants.getInstance().getSpecList())
                .tag(VIDEOTAG)
                .params("classId", collectionId)
                .params("pageIndex", mPageIndex)
                .params("pageSize", mPageSize)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<VideoCollectionModel>(VideoCollectionModel.class) {

                    @Override
                    public void onSuccess(Response<VideoCollectionModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        try {
                            if (response.body().getCode().equals(success_code)) {
                                if (null == response.body().getData() && response.body().getData().getRecords().size() == 0) {
                                    ToastUtils.showShort(R.string.data_err);
                                    return;
                                }

                                if (response.body().getData().getRecords().size() == 0) {
                                    adapter.loadMoreEnd();
                                    adapter.setOnLoadMoreListener(null, videoDetailRv);
                                    if (null != footerView && null != footerView.getParent()) {
                                        ((ViewGroup) footerView.getParent()).removeView(footerView);
                                    }
                                    adapter.addFooterView(footerView);
                                    isLoadComplate = true;
                                } else {
                                    adapter.setOnLoadMoreListener(requestLoadMoreListener, videoDetailRv);
                                    isLoadComplate = false;
                                    mDatas.addAll(response.body().getData().getRecords());
                                    for (int i = 0; i < mDatas.size(); i++) {
                                        String videoType = videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(i).getWidth())),
                                                Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(i).getHeight())));
                                        mDatas.get(i).setLogoType(videoType);
                                    }

                                    setDataWifiStates(mDatas, VideoDetailActivity.this);
                                }
                            }
                            if (null != refreshLayout) {
                                refreshLayout.finishRefresh();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<VideoCollectionModel> response) {
                        super.onError(response);
                        if (null != response.body()) {
//                            ToastUtils.showShort(response.body().getMessage());
                            return;
                        }
                        ToastUtils.showShort(R.string.net_err);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        loadingProgress.setVisibility(View.GONE);
                    }
                });
    }


    /**
     * 获取评论列表
     */
    public void getCommentList(String pageIndex, String pageSize, final boolean isRefresh) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("contentId", myContentId);
            jsonObject.put("pageIndex", pageIndex);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("pcommentId", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<CommentLv1Model>post(ApiConstants.getInstance().getCommentWithReply())
                .tag(VIDEOTAG)
                .upJson(jsonObject)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<CommentLv1Model>(CommentLv1Model.class) {
                    @Override
                    public void onSuccess(Response<CommentLv1Model> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        try {
                            if (response.body().getCode().equals("200")) {
                                if (null == response.body().getData()) {
                                    ToastUtils.showShort(R.string.data_err);
                                    return;
                                }

                                if (isRefresh) {
                                    mCommentPopRvData.clear();
                                    mCommentPopDtoData.clear();
                                }

                                //评论集合
                                List<CommentLv1Model.DataDTO.RecordsDTO> lv1List = response.body().getData().getRecords();
                                for (int i = 0; i < lv1List.size(); i++) {
                                    CommentLv1Model.DataDTO.RecordsDTO lv1Model = lv1List.get(i);
                                    lv1Model.setPosition(i);
                                    lv1Model.setShow(true);
                                    List<ReplyLv2Model.ReplyListDTO> lv2List = lv1Model.getReply().getReplyList();
                                    for (int j = 0; j < lv2List.size(); j++) {
                                        ReplyLv2Model.ReplyListDTO lv2Model = lv2List.get(j);
                                        lv2Model.setPosition(j);
                                        lv2Model.setParentPosition(i);
                                        lv1Model.addSubItem(lv2Model);
                                    }
                                    mCommentPopRvData.add(lv1Model);
                                }

                                mCommentPopDtoData.addAll(lv1List);
                                commentPopRvAdapter.setContentId(myContentId);
                                commentPopRvAdapter.setSrc(mCommentPopRvData);
                                commentPopRvAdapter.setNewData(mCommentPopRvData);

                                //第一级评论点击
                                commentPopRvAdapter.setLv1CommentClick(new CommentPopRvAdapter.Lv1CommentClick() {
                                    @Override
                                    public void Lv1Comment(String id, String replyName) {
                                        toSetHint(id, replyName);
                                    }
                                });

                                //第一级评论第一条回复点击
                                commentPopRvAdapter.setLv1No1Click(new CommentPopRvAdapter.Lv1No1Click() {
                                    @Override
                                    public void lv1No1Click(String id, String replyName) {
                                        toSetHint(id, replyName);
                                    }
                                });

                                //第一级评论第二条回复点击
                                commentPopRvAdapter.setLv1No2Click(new CommentPopRvAdapter.Lv1No2Click() {
                                    @Override
                                    public void lv1No2Click(String id, String replyName) {
                                        toSetHint(id, replyName);
                                    }
                                });

                                //第二级回复点击
                                commentPopRvAdapter.setLv2ReplyClick(new CommentPopRvAdapter.Lv2ReplyClick() {
                                    @Override
                                    public void Lv2ReplyClick(String id, String replyName) {
                                        toSetHint(id, replyName);
                                    }
                                });


                                TextView commentNum = (TextView) adapter.getViewByPosition(currentIndex, R.id.comment_num);
                                if (mCommentPopDtoData.isEmpty()) {
                                    if (null != commentNum) {
                                        commentNum.setText("评论");
                                    }
                                    commentPopCommentTotal.setText("(0)");
                                } else {
                                    if (null != commentNum) {
                                        commentNum.setText(response.body().getData().getTotal());
                                    }
                                    commentPopCommentTotal.setText("(" + response.body().getData().getTotal() + ")");
                                }

                                if (response.body().getData().getRecords().size() == 0) {
                                    commentPopRvAdapter.loadMoreEnd();
                                } else {
                                    commentPopRvAdapter.loadMoreComplete();
                                }

                            } else {
                                commentPopRvAdapter.loadMoreFail();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Response<CommentLv1Model> response) {
                        commentPopRvAdapter.loadMoreFail();
                    }
                });
    }

    private void toSetHint(String id, String replyName) {
        if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
            try {
                noLoginTipsPop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            KeyboardUtils.toggleSoftInput(getWindow().getDecorView());
            showInputEdittextAndSend();
            edtInput.setHint("回复@" + replyName);
            isReply = true;
            replyId = id;
        }
    }

    /**
     * 评论
     */
    private void toComment(String content, String contentId) {
        FinderBuriedPointManager.setFinderLikeFavoriteShare(Constants.CONTENT_COMMENT, mDataDTO);
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
                                    String jsonString = BuriedPointModelManager.getVideoComment(myContentId, mDatas.get(currentIndex).getTitle(), "", "",
                                            "", "", mDatas.get(currentIndex).getIssueTimeStamp(), Constants.CONTENT_TYPE);
                                    Log.e("埋点", "埋点：评论---" + jsonString);
                                }

                                ToastUtils.showShort("评论已提交，请等待审核通过！");
                                if (null != inputAndSendPop) {
                                    inputAndSendPop.dissmiss();
                                }
                                KeyboardUtils.hideKeyboard(VideoDetailActivity.this.getWindow().getDecorView());
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
     * 回复
     */
    private void toReply(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("reply", edtInput.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(ApiConstants.getInstance().addUserReply())
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
                                ToastUtils.showShort("回复已提交，请等待审核通过！");
                                if (null != inputAndSendPop) {
                                    inputAndSendPop.dissmiss();
                                }
                                mPageIndex = 1;
                                KeyboardUtils.hideKeyboard(VideoDetailActivity.this.getWindow().getDecorView());
                                getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), true);
                            } else if (code.equals(token_error)) {
                                try {
                                    param.toLogin();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (null != mJsonObject.getString("message")) {
                                    ToastUtils.showShort(mJsonObject.getString("message"));
                                } else {
                                    ToastUtils.showShort("回复失败");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showShort("回复失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 获取收藏点赞状态
     */
    public void getContentState(String contentId) {
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

                        try {
                            if (response.body().getCode().equals(success_code)) {
                                if (null == response.body().getData()) {
                                    ToastUtils.showShort(R.string.data_err);
                                    return;
                                }

                                playerView.contentStateModel = response.body().getData();
                                if (null != playerView.contentStateModel) {
                                    setLikeCollection(playerView.contentStateModel);
                                    playerView.setContentStateModel(myContentId, videoType);
                                }
                            } else {
                                ToastUtils.showShort(response.body().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    /**
     * 获取专题合集标签
     */
    public void getThematicCollection(String contentId) {
        String belongTopicId = mDataDTO.getBelongTopicId();
        if (TextUtils.isEmpty(belongTopicId)) {
            belongTopicId = "0";
        }
        OkGo.<CollectionLabelModel>get(ApiConstants.getInstance().getCollectToVideo() + contentId + "/" + belongTopicId)
                .tag(VIDEOTAG)
                .headers("token", PersonInfoManager.getInstance().getTransformationToken())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<CollectionLabelModel>(CollectionLabelModel.class) {
                    @Override
                    public void onSuccess(Response<CollectionLabelModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        try {
                            if (response.body().getCode().equals(success_code)) {
                                if (null == response.body().getData()) {
                                    return;
                                }
                                topicName = response.body().getData().getTopicName();
                                collectionList = new ArrayList<>();
                                collectionTvList = new ArrayList<>();
                                collectionStrList = new ArrayList<>();
                                String collectionStr = "";
                                if (null == response.body().getData().getList()) {
                                    List<CollectionLabelModel.DataDTO.ListDTO> listDTO = new ArrayList<>();
                                    collectionList.addAll(listDTO);
                                } else {
                                    collectionList.addAll(response.body().getData().getList());
                                }
                                if (!TextUtils.isEmpty(topicName)) {
                                    CollectionLabelModel.DataDTO.ListDTO listDTO = new CollectionLabelModel.DataDTO.ListDTO();
                                    listDTO.setTitle("#" + topicName);
                                    listDTO.setId("");
                                    collectionList.add(listDTO);
                                }

                                for (int i = 0; i < collectionList.size(); i++) {
                                    collectionStr = collectionStr + collectionList.get(i).getTitle();
                                    collectionStrList.add(collectionList.get(i).getTitle());
                                    if (collectionList.size() == 1) {
                                        collectionTvList.add("  " + collectionList.get(i).getTitle());
                                    } else {
                                        if (i == 0) {
                                            collectionTvList.add("  " + collectionList.get(i).getTitle() + "｜");
                                        } else {
                                            if (i == collectionList.size() - 1) {
                                                collectionTvList.add(collectionList.get(i).getTitle());
                                            } else {
                                                collectionTvList.add(collectionList.get(i).getTitle() + "｜");
                                            }
                                        }
                                    }
                                }
                                TextView foldTextView = (TextView) adapter.getViewByPosition(currentIndex, R.id.fold_text);
                                TextView expendTextView = (TextView) adapter.getViewByPosition(currentIndex, R.id.expend_text);
                                String brief = "";
                                String spaceStr = "";
                                RecordsDTO item = adapter.getItem(currentIndex);
                                if (null == item) {
                                    return;
                                }
                                if (TextUtils.isEmpty(adapter.getItem(currentIndex).getBrief())) {
                                    brief = item.getTitle();
                                } else {
                                    brief = item.getBrief();
                                }
                                SpannableStringBuilder builder = new SpannableStringBuilder();
                                if (collectionList.isEmpty()) {
                                    return;
                                } else {
                                    for (int i = 0; i < collectionList.size(); i++) {
                                        ImageSpan imgSpan = null;
                                        if (!TextUtils.isEmpty(collectionList.get(i).getId())) {
                                            imgSpan = new ImageSpan(VideoDetailActivity.this,
                                                    R.drawable.collection_image,
                                                    ImageSpan.ALIGN_CENTER);
                                        }

                                        final String str = collectionTvList.get(i);
                                        final String strChun = collectionStrList.get(i);
                                        SpannableString sp = new SpannableString(str);
                                        if (i == 0 && !TextUtils.isEmpty(collectionList.get(i).getId())) {
                                            sp.setSpan(imgSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        }
                                        final String classId = String.valueOf(collectionList.get(i).getId());
                                        /**
                                         * 每一个合集标签点击事件
                                         */
                                        sp.setSpan(new CollectionClickble(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
//                                                //合集标签点击事件
//                                                if (TextUtils.isEmpty(classId) && str.contains("#")) {
//                                                    adapter.setTopicClick(false);
//                                                    Log.e("topic", "点击的话题");
//                                                } else {
//                                                    adapter.setTopicClick(true);
//                                                    Intent intent = new Intent(VideoDetailActivity.this, VideoDetailActivity.class);
//                                                    intent.putExtra("classId", classId);
//                                                    intent.putExtra("className", strChun.trim());
//                                                    startActivity(intent);
//                                                    FinderBuriedPointManager.setFinderClick("集合_" + strChun);
//                                                }
                                            }
                                        }, VideoDetailActivity.this), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        if (i == collectionList.size() - 1) {
                                            builder.append(sp);
                                            builder.append("  " + brief);
                                        } else {
                                            builder.append(sp);
                                        }
                                    }
                                    if (null != foldTextView && null != expendTextView) {
                                        foldTextView.setMovementMethod(LinkMovementMethod.getInstance());
                                        foldTextView.setText(builder);
                                        expendTextView.setMovementMethod(LinkMovementMethod.getInstance());
                                        expendTextView.setText(builder);
                                    }
                                }
                            } else {
                                ToastUtils.showShort(response.body().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<CollectionLabelModel> response) {
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
                    }
                });
    }

    /**
     * 设置合集标签
     */
    public void setCollection() {
        RecordsDTO item = adapter.getItem(currentIndex);
        String brief;
        TextView foldTextView = (TextView) adapter.getViewByPosition(currentIndex, R.id.fold_text);
        TextView expendTextView = (TextView) adapter.getViewByPosition(currentIndex, R.id.expend_text);
        if (null == item) {
            return;
        }
        if (TextUtils.isEmpty(adapter.getItem(currentIndex).getBrief())) {
            brief = item.getTitle();
        } else {
            brief = item.getBrief();
        }

        if (TextUtils.isEmpty(className)) {
            foldTextView.setText(brief);
            expendTextView.setText(brief);
        } else {
            SpannableString sp = new SpannableString(className);
            ImageSpan imgSpan = new ImageSpan(VideoDetailActivity.this,
                    R.drawable.collection_image,
                    ImageSpan.ALIGN_CENTER);
            sp.setSpan(imgSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new ForegroundColorSpan(Color.WHITE), 0, className.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(sp);
            builder.append(" " + brief);
            foldTextView.setText(builder);
            expendTextView.setText(builder);
        }


    }

    public void setLikeCollection(ContentStateModel.DataDTO contentStateModel) {
        ImageView collectionImage = (ImageView) adapter.getViewByPosition(currentIndex, R.id.video_detail_collection_image);
        TextView collectionNum = (TextView) adapter.getViewByPosition(currentIndex, R.id.collection_num);
        ImageView likeImage = (ImageView) adapter.getViewByPosition(currentIndex, R.id.video_detail_likes_image);
        TextView likeNum = (TextView) adapter.getViewByPosition(currentIndex, R.id.likes_num);

        if (null != collectionImage) {
            if (contentStateModel.getWhetherFavor().equals("true")) {
                collectionImage.setImageResource(R.drawable.collection);
            } else {
                collectionImage.setImageResource(R.drawable.collection_icon);
            }
        }

        if (null != collectionNum) {
            collectionNum.setText(NumberFormatTool.formatNum(Long.parseLong(NumberFormatTool.getNumStr(contentStateModel.getFavorCountShow())), false));
        }

        if (null != likeImage) {
            if (contentStateModel.getWhetherLike().equals("true")) {
                likeImage.setImageResource(R.drawable.favourite_select);
            } else {
                likeImage.setImageResource(R.drawable.favourite);
            }
        }

        if (null != likeNum) {
            if (contentStateModel.getLikeCountShow().equals("0")) {
                likeNum.setText("赞");
            } else {
                likeNum.setText(NumberFormatTool.formatNum(Long.parseLong(NumberFormatTool.getNumStr(contentStateModel.getLikeCountShow())), false));
            }
        }

        TextView followView = (TextView) adapter.getViewByPosition(currentIndex, R.id.follow);
        if (null != followView) {
            if (contentStateModel.getWhetherFollow().equals("true")) {
                followView.setBackgroundResource(R.drawable.followed_bg);
                followView.setText("已关注");
                isFollow = true;
            } else {
                adapter.getViewByPosition(currentIndex, R.id.follow).setBackgroundResource(R.drawable.follow_bg);
                followView.setText("关注");
                isFollow = false;
            }
        }
    }

    /**
     * 收藏/取消收藏
     */
    private void addOrCancelFavor(String contentId, String type, final ImageView collectionImage, final TextView collectionNum) {
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

                        if (null == playerView) {
                            return;
                        }

                        try {
                            JSONObject json = new JSONObject(response.body());
                            if (json.get("code").toString().equals(success_code)) {
                                if (!mDatas.isEmpty()) {
                                    String jsonString = BuriedPointModelManager.getLikeAndFavorBuriedPointData(myContentId, mDatas.get(currentIndex).getTitle(),
                                            "", "", "", "", mDatas.get(currentIndex).getIssueTimeStamp(),
                                            Constants.CONTENT_TYPE);
                                    Log.e("埋点", "埋点：收藏---" + jsonString);
                                }

                                if (json.get("data").toString().equals("1")) {
                                    int num;
                                    num = Integer.parseInt(NumberFormatTool.getNumStr(collectionNum.getText().toString()));
                                    num++;
                                    collectionNum.setText(NumberFormatTool.formatNum(num, false));
                                    collectionImage.setImageResource(R.drawable.collection);
                                    FinderBuriedPointManager.setFinderLikeFavoriteShare(Constants.CONTENT_FAVORITE, mDataDTO);
                                    playerView.contentStateModel.setWhetherFavor("true");
                                } else {
                                    int num;
                                    num = Integer.parseInt(NumberFormatTool.getNumStr(collectionNum.getText().toString()));
                                    if (num > 0) {
                                        num--;
                                    }
                                    collectionNum.setText(NumberFormatTool.formatNum(num, false));
                                    collectionImage.setImageResource(R.drawable.collection_icon);
                                    playerView.contentStateModel.setWhetherFavor("false");
                                }
                                if (null != playerView.contentStateModel) {
                                    playerView.setContentStateModel(myContentId, videoType);
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
    private void addOrCancelLike(String targetId, String type, final ImageView likeImage, final TextView likeNum) {
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

                        if (null == playerView) {
                            return;
                        }

                        try {
                            JSONObject json = new JSONObject(response.body());
                            if (null != json && json.get("code").toString().equals("200")) {
                                if (!mDatas.isEmpty()) {
                                    String jsonString = BuriedPointModelManager.getLikeAndFavorBuriedPointData(myContentId, mDatas.get(currentIndex).getTitle(),
                                            "", "", "", "", mDatas.get(currentIndex).getIssueTimeStamp(),
                                            Constants.CONTENT_TYPE);
                                    Log.e("埋点", "埋点：点赞---" + jsonString);
                                }

                                if (json.get("data").toString().equals("1")) {
                                    int num = 0;
                                    if (null != likeImage) {
                                        likeImage.setImageResource(R.drawable.favourite_select);
                                    }

                                    if (null != likeNum) {
                                        num = Integer.parseInt(NumberFormatTool.getNumStr(likeNum.getText().toString()));
                                        num++;
                                        likeNum.setText(NumberFormatTool.formatNum(num, false));
                                    }
                                    FinderBuriedPointManager.setFinderLikeFavoriteShare(Constants.CONTENT_LIKE, mDataDTO);
                                    mDataDTO.setWhetherLike(true);
                                    playerView.contentStateModel.setWhetherLike("true");
                                    playerView.contentStateModel.setLikeCountShow(NumberFormatTool.formatNum(num, false).toString());
                                } else {
                                    int num = 0;
                                    if (null != likeImage) {
                                        likeImage.setImageResource(R.drawable.favourite);
                                    }
                                    if (null != likeNum) {
                                        num = Integer.parseInt(NumberFormatTool.getNumStr(likeNum.getText().toString()));
                                        if (num > 0) {
                                            num--;
                                        }
                                        if (num == 0) {
                                            likeNum.setText("赞");
                                        } else {
                                            likeNum.setText(NumberFormatTool.formatNum(num, false));
                                        }
                                    }
                                    playerView.contentStateModel.setWhetherLike("false");
                                    playerView.contentStateModel.setLikeCountShow(NumberFormatTool.formatNum(num, false).toString());
                                }
                                if (null != playerView.contentStateModel) {
                                    playerView.setContentStateModel(myContentId, videoType);
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

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        likeIsRequesting = false;
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
                                transformationToken = response.body().getData().getToken();
                                PersonInfoManager.getInstance().setTransformationToken(transformationToken);
                                if (!TextUtils.isEmpty(myContentId)) {
                                    getContentState(myContentId);
                                }
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

    @Override
    public void onResume() {
        super.onResume();
        if (playerView != null && !SPUtils.isVisibleNoWifiView(this)) {
            if (playerView.homeVideoIsLoad) {
                playerView.mSuperPlayer.resume();
            } else {
                playerView.mSuperPlayer.reStart();
            }
        }
        videoOldSystemTime = DateUtils.getTimeCurrent();
        if (!TextUtils.isEmpty(myContentId)) {
            getContentState(myContentId);
        }

        if (PersonInfoManager.getInstance().isRequestToken()) {
            try {
                getUserToken(VideoInteractiveParam.getInstance().getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (null != playerView && playerView.mSuperPlayer.getPlayerMode() == SuperPlayerDef.PlayerMode.FULLSCREEN) {
            SystemUtils.hideSystemUI(decorView);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (playerView == null) {
            return;
        }

        playerView.mSuperPlayer.pause();
        if (null == mDataDTO) {
            return;
        }
        if (!TextUtils.isEmpty(mDataDTO.getVolcCategory())) {
            if (playerView.mWindowPlayer.mCurrentPlayState != SuperPlayerDef.PlayerState.END) {

                if (mProgress != 0 && mDuration != 0) {
                    /**
                     * 上报内容埋点 视频播放时长
                     */
                    long evePlayTime = Math.abs(mProgress - lsDuration);
                    double currentPercent = (evePlayTime * 1.0 / mDuration);
                    double uploadPercent = 0;
                    if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
//                      //不为重播
                        if (currentPercent > maxPercent) {
                            uploadPercent = currentPercent;
                            maxPercent = currentPercent;
                        } else {
                            uploadPercent = maxPercent;
                        }
                    } else {
                        uploadPercent = 1;
                    }
                    videoReportTime = DateUtils.getTimeCurrent() - videoOldSystemTime;
                    BigDecimal two = new BigDecimal(uploadPercent);
                    double pointPercentTwo = two.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                    lsDuration = mProgress;
                    String event;
                    event = Constants.CMS_VIDEO_OVER;
                    //上报埋点
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(VideoDetailActivity.this, mDataDTO.getThirdPartyId(), String.valueOf(videoReportTime), String.valueOf(Math.floor(pointPercentTwo * 100)), event, mDataDTO.getVolcCategory(), mDataDTO.getRequestId()), event);
                    Log.e("video_md", "埋点事件：" + event + "播放时长:" + videoReportTime + "---" + "播放百分比:" + pointPercentTwo);
                }
            }
        }

        String isFinish;
        if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
            isFinish = "否";
        } else {
            isFinish = "是";
        }
        FinderBuriedPointManager.setFinderVideo(Constants.CONTENT_VIDEO_DURATION, "", mDataDTO, videoReportTime, isFinish);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isShow = false;
        if (playerView != null) {
            playerView.mSuperPlayer.stop();
            playerView.release();
            playerView.mSuperPlayer.destroy();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        } else if (id == R.id.dismiss_pop) {
            if (popupWindow != null) {
                popupWindow.dissmiss();
            }
        } else if (id == R.id.comment_pop_rl) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                try {
                    noLoginTipsPop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                edtInput.setHint("留下你的精彩评论");
                isReply = false;
                KeyboardUtils.toggleSoftInput(VideoDetailActivity.this.getWindow().getDecorView());
                showInputEdittextAndSend();
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
        } else if (id == R.id.share || id == R.id.comment_share) {
            if (mDatas.isEmpty()) {
                return;
            }
            FinderBuriedPointManager.setFinderLikeFavoriteShare(Constants.CONTENT_TRANSMIT, mDataDTO);
//            FinderBuriedPointManager.setFinderClick("分享");
            sharePop();
        } else if (id == R.id.share_wx_btn) {
            if (mDatas.isEmpty()) {
                return;
            }
            toShare(mDataDTO, Constants.SHARE_WX);
        } else if (id == R.id.share_circle_btn) {
            if (mDatas.isEmpty()) {
                return;
            }
            toShare(mDataDTO, Constants.SHARE_CIRCLE);
        } else if (id == R.id.share_qq_btn) {
            if (mDatas.isEmpty()) {
                return;
            }
            toShare(mDataDTO, Constants.SHARE_QQ);
        }
    }


    /**
     * 弹出发送评论弹出窗
     */
    private void showInputEdittextAndSend() {
        //创建并显示popWindow
        if (null == inputAndSendPop) {
            inputAndSendPop = new CustomPopWindow.PopupWindowBuilder(VideoDetailActivity.this)
                    .setView(sendPopContentView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .create()
                    .showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        } else {
            inputAndSendPop.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
        edtInput.setFocusable(true);
        edtInput.setFocusableInTouchMode(true);
        edtInput.requestFocus();

    }

    /**
     * 没有登录情况下 点击点赞收藏评论 提示登录的提示框
     */
    private void noLoginTipsPop() {
        if (null == noLoginTipsPop) {
            noLoginTipsPop = new CustomPopWindow.PopupWindowBuilder(VideoDetailActivity.this)
                    .setView(noLoginTipsView)
                    .enableBackgroundDark(true)
                    .setOutsideTouchable(true)
                    .setFocusable(true)
                    .setAnimationStyle(R.style.AnimCenter)
                    .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, Utils.getContext().getResources().getDisplayMetrics().heightPixels)
                    .create()
                    .showAtLocation(decorView, Gravity.CENTER, 0, 0);
        } else {
            noLoginTipsPop.showAtLocation(decorView, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 添加软键盘监听
     */
    private void setSoftKeyBoardListener() {
        softKeyBoardListener = new SoftKeyBoardListener(VideoDetailActivity.this);
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
                        try {
                            if (response.body().getCode().equals("200")) {
                                recommondList.clear();
                                recommondList.addAll(response.body().getData().getRecords());

                                ViewFlipper viewFlipper = (ViewFlipper) adapter.getViewByPosition(position, R.id.video_flipper);

                                if (null == viewFlipper) {
                                    return;
                                }

                                if (null != adapter.getItem(position) && adapter.getItem(position).isClosed()) {
                                    viewFlipper.setVisibility(View.GONE);
                                    return;
                                }

                                if (recommondList.size() > 1) {
                                    viewFlipper.setVisibility(View.VISIBLE);
                                    viewFlipper.startFlipping();
                                    viewFlipper.setAutoStart(true);
                                } else if (recommondList.size() == 1) {
                                    viewFlipper.setVisibility(View.VISIBLE);
                                    viewFlipper.setAutoStart(false);
                                } else if (recommondList.size() == 0) {
                                    viewFlipper.setVisibility(View.GONE);
                                }
                                adapter.getViewFlipperData(recommondList, viewFlipper, mDatas.get(position));
                            } else {
                                ToastUtils.showShort(response.body().getMessage());
                            }
                            if (SPUtils.isVisibleNoWifiView(VideoDetailActivity.this)) {
                                SPUtils.getInstance().put(Constants.AGREE_NETWORK, "0");
                            } else {
                                addPlayView(position);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
                        if (SPUtils.isVisibleNoWifiView(VideoDetailActivity.this)) {
                            SPUtils.getInstance().put(Constants.AGREE_NETWORK, "0");
                        } else {
                            addPlayView(position);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        loadingProgress.setVisibility(View.GONE);
                    }
                });
    }


}