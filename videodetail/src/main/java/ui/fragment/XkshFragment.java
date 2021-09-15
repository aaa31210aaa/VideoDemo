package ui.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zhouwei.library.CustomPopWindow;
import com.flyco.tablayout.SlidingTabLayout;
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
import com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl;
import com.tencent.liteav.demo.superplayer.model.utils.SystemUtils;
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
import com.wdcs.model.ShareInfo;
import com.wdcs.model.TokenModel;
import com.wdcs.model.TrackingUploadModel;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.model.VideoDetailModel;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.KeyboardUtils;
import com.wdcs.utils.NetworkUtil;
import com.wdcs.utils.NoScrollViewPager;
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
import adpter.XkshVideoAdapter;
import ui.activity.UploadActivity;
import ui.activity.VideoHomeActivity;
import widget.LoadingView;

import com.wdcs.manager.OnViewPagerListener;
import com.wdcs.manager.ViewPagerLayoutManager;

import static android.widget.RelativeLayout.BELOW;
import static com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer.mDuration;
import static com.wdcs.constants.Constants.PANELCODE;
import static com.wdcs.constants.Constants.VIDEOTAG;
import static com.wdcs.constants.Constants.success_code;
import static com.wdcs.constants.Constants.token_error;
import static ui.activity.VideoHomeActivity.uploadBuriedPoint;
import static utils.NetworkUtil.setDataWifiState;

public class XkshFragment extends Fragment implements View.OnClickListener {
    private RecyclerView videoDetailRv;
    public XkshVideoAdapter adapter;
    private CommentPopRvAdapter commentPopRvAdapter;
    //视频列表数据
    public List<DataDTO> mDatas = new ArrayList<>();
    //评论列表数据
    private List<CommentModel.DataDTO.RecordsDTO> mCommentPopRvData;
    private List<CommentModel.DataDTO> mCommentPopDtoData;
    private SuperPlayerView playerView;
    private ImageView videoStaticBg;
    private ImageView startPlay;

    public RelativeLayout videoDetailCommentBtn;
    //评论列表弹窗
    public CustomPopWindow popupWindow;
    private LinearLayout videoDetailCollection;
    private LinearLayout videoDetailLikes;

    private View contentView;
    private View chooseContentView;
    private RelativeLayout dismissPop;
    private RecyclerView commentPopRv;
    private TextView commentEdtInput;
    private RelativeLayout commentPopRl;
    //附着在软键盘上的输入弹出窗
    public CustomPopWindow inputAndSendPop;
    private View sendPopContentView;
    private View rootView;
    private LinearLayout edtParent;
    private EditText edtInput;
    private TextView tvSend;
    private RelativeLayout videoDetailWhiteCommentRl;
    //选择集数弹窗
    public CustomPopWindow choosePop;

    public ViewPagerLayoutManager xkshManager;
    private ImageView choosePopDismiss;
    public SmartRefreshLayout refreshLayout;
    private String transformationToken = "";
    private String panelCode = "";
    private RelativeLayout commentListEmptyRl;
    private String recordContentId;//记录的内容id
    private boolean initialize = true;
    private int mVideoSize = 20; //每页视频多少条
    private int mPageIndex = 1; //评论列表页数
    private int mPageSize = 10; //评论列表每页多少条
    public String myContentId = ""; //记录当前视频id
    public int currentIndex = 0; //记录当前视频列表的位置
    private TextView commentTotal;
    private TextView commentPopCommentTotal;
    private ImageView videoDetailCollectionImage; //收藏图标

    private ImageView videoDetailLikesImage; //点赞图标
    private TextView likesNum; //点赞数
    private TextView collectionNum; //收藏数
    private String videoType; //视频类型
    private VideoInteractiveParam param;
    public String playUrl;
    private TextView commentEdittext;
    private String videoTag = "videoTag";
    private String recommendTag = "recommend";
    private boolean isLoadComplate = false;
    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener;
    public View decorView;
    private SoftKeyBoardListener softKeyBoardListener;
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    private LinearLayout share;
    public CustomPopWindow sharePop;
    private View sharePopView;
    private ImageView shareWxBtn;
    private ImageView shareCircleBtn;
    private ImageView shareQqBtn;
    public DataDTO mDataDTO;
    private List<RecommendModel.DataDTO.RecordsDTO> recommondList;
    private ViewGroup rlLp;
    private VideoChannelModel videoChannelModel;

    private VideoChannelModel channelModel;
    private Bundle args;
    public boolean mIsVisibleToUser;
    private SlidingTabLayout mVideoTab;
    private LinearLayout shotAlike;
    private LoadingView loadingProgress;
    private RelativeLayout.LayoutParams lp;
    private boolean isFollow; //是否关注
    public ImageView rankList; //排行榜
    public ImageView activityRuleImg; //活动规则图
    public ImageView activityRuleAbbreviation;
    public ImageView activityToAbbreviation; //变为缩略图按钮
    public LinearLayout fullLin;
    public double pointPercent;                         // 每一次记录的节点播放百分比
    private long everyOneDuration; //每一次记录需要上报的播放时长 用来分段上报埋点
    private long lsDuration = 0; //每一次上报临时保存的播放时长

    public XkshFragment(SlidingTabLayout videoTab, SuperPlayerView mPlayerView) {
        this.mVideoTab = videoTab;
        this.playerView = mPlayerView;
    }

    public XkshFragment newInstance(XkshFragment fragment, VideoChannelModel videoChannelModel) {
        args = new Bundle();
        args.putString(PANELCODE, videoChannelModel.getColumnBean().getPanelCode());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            panelCode = getArguments().getString(PANELCODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = inflater.inflate(R.layout.fragment_xksh, container, false);
        decorView = getActivity().getWindow().getDecorView();
        initView(view);
        getPullDownData(String.valueOf(mVideoSize), panelCode, "false");
        return view;
    }

    private void initView(View view) {
        param = VideoInteractiveParam.getInstance();
        share = view.findViewById(R.id.share);
        share.setOnClickListener(this);
        mDataDTO = new DataDTO();
        recommondList = new ArrayList<>();
        loadingProgress = view.findViewById(R.id.xksh_loading_progress);
        loadingProgress.setVisibility(View.VISIBLE);
        commentEdittext = view.findViewById(R.id.comment_edittext);
        commentListEmptyRl = view.findViewById(R.id.comment_list_empty_rl);
        videoDetailRv = view.findViewById(R.id.video_detail_rv);
        videoDetailRv.setHasFixedSize(true);
        videoDetailCollectionImage = view.findViewById(R.id.video_detail_collection_image);
        videoDetailLikesImage = view.findViewById(R.id.video_detail_likes_image);
        likesNum = view.findViewById(R.id.likes_num);
        collectionNum = view.findViewById(R.id.collection_num);
        shotAlike = view.findViewById(R.id.shot_alike);
        shotAlike.setOnClickListener(this);
        rankList = view.findViewById(R.id.rank_list);
        rankList.setOnClickListener(this);
        activityRuleImg = view.findViewById(R.id.activity_rule_img);
        activityRuleImg.setOnClickListener(this);

        //如果活动没有跳转URL 隐藏
            activityRuleImg.setVisibility(View.GONE);

        activityToAbbreviation = view.findViewById(R.id.activity_to_abbreviation);
        activityToAbbreviation.setOnClickListener(this);
        activityRuleAbbreviation = view.findViewById(R.id.activity_rule_abbreviation);
        activityRuleAbbreviation.setOnClickListener(this);
        xkshManager = new ViewPagerLayoutManager(getActivity());
        videoDetailRv.setLayoutManager(xkshManager);

        setSoftKeyBoardListener();
        xkshManager.setOnViewPagerListener(new OnViewPagerListener() {


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
//                playerView = SuperPlayerView.getInstance(getActivity(), decorView);
                playerView.mWindowPlayer.setDataDTO(mDataDTO, mDataDTO);
                playerView.mWindowPlayer.setViewpager((NoScrollViewPager) getActivity().findViewById(R.id.video_vp));
                playerView.mWindowPlayer.setIsTurnPages(false);
                playerView.mWindowPlayer.setManager(xkshManager);
                playerView.mFullScreenPlayer.setDataDTO(mDataDTO);
                myContentId = String.valueOf(mDatas.get(0).getId());
                addPageViews(myContentId);
                OkGo.getInstance().cancelTag("contentState");
                getContentState(myContentId);
                SuperPlayerImpl.mCurrentPlayVideoURL = mDatas.get(0).getPlayUrl();

                mPageIndex = 1;
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
                getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), true);
                videoType = mDatas.get(0).getType();
                Log.e("T8000", "onInitComplete");
                rlLp = (ViewGroup) xkshManager.findViewByPosition(0);
                OkGo.getInstance().cancelTag(recommendTag);
                //获取推荐列表
                if (mIsVisibleToUser) {
                    getRecommend(myContentId, 0);
                }
//                initChoosePop();
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e("T8000", "onPageRelease: " + isNext + ", " + position);
            }

            @Override
            public void onPageSelected(final int position, boolean isBottom) {
                if (null == playerView) {
                    return;
                }

                if (null != playerView.getTag() && position == (int) playerView.getTag()) {
                    return;
                }
                playerView.mWindowPlayer.hide();
                mDataDTO = mDatas.get(position);

                if (mDuration != 0 && playerView.mWindowPlayer.mProgress != 0) {
                    //上报埋点
                    everyOneDuration = playerView.mWindowPlayer.mProgress;
                    pointPercent = (double) everyOneDuration / mDuration;
                    BigDecimal two = new BigDecimal(pointPercent);
                    double pointPercentTwo = two.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                    uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(getActivity(), mDataDTO.getThirdPartyId(), String.valueOf(everyOneDuration*1000), String.valueOf(Math.floor(pointPercentTwo*100)), Constants.CMS_VIDEO_OVER_AUTO), Constants.CMS_VIDEO_OVER_AUTO);
                }

                //滑动下一条或者上一条视频
                playerView.mWindowPlayer.setRecordDuration(0);
                lsDuration = 0;
                SuperPlayerImpl.mCurrentPlayVideoURL = mDatas.get(position).getPlayUrl();
                playUrl = mDatas.get(position).getPlayUrl();
                playerView.mWindowPlayer.setDataDTO(mDataDTO, mDatas.get(currentIndex));
                playerView.mFullScreenPlayer.setDataDTO(mDataDTO);
                playerView.mWindowPlayer.setIsTurnPages(true);
                playerView.mFullScreenPlayer.setIsTurnPages(true);
                currentIndex = position;
                reset();
                myContentId = String.valueOf(mDatas.get(position).getId());
                //重置重播标识
                playerView.buriedPointModel.setIs_renew("false");
                addPageViews(myContentId);
                videoType = mDatas.get(position).getType();
                mPageIndex = 1;
                if (mDatas.get(position).getDisableComment()) {
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
                getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), true);
                getContentState(myContentId);
                rlLp = (ViewGroup) xkshManager.findViewByPosition(position);
                OkGo.getInstance().cancelTag(recommendTag);
                getRecommend(myContentId, position);

                if (!"1".equals(playerView.mFullScreenPlayer.strSpeed)) {
                    playerView.mFullScreenPlayer.mVodMoreView.mCallback.onSpeedChange(1.0f);
                    playerView.mFullScreenPlayer.superplayerSpeed.setText("倍速");
                    playerView.mFullScreenPlayer.mRbSpeed1.setChecked(true);
                }
            }
        });

        initSmartRefresh(view);
        commentTotal = view.findViewById(R.id.comment_total);
        videoDetailCollection = view.findViewById(R.id.video_detail_collection);
        videoDetailCollection.setOnClickListener(this);
        videoDetailLikes = view.findViewById(R.id.video_detail_likes);
        videoDetailLikes.setOnClickListener(this);

        contentView = View.inflate(getActivity(), R.layout.fragment_video_comment_pop, null);
        sendPopContentView = View.inflate(getActivity(), R.layout.layout_input_window, null);
        commentPopCommentTotal = contentView.findViewById(R.id.comment_pop_comment_total);
        edtParent = sendPopContentView.findViewById(R.id.edt_parent);
        edtInput = sendPopContentView.findViewById(R.id.edtInput);
        tvSend = sendPopContentView.findViewById(R.id.tvSend);

        noLoginTipsView = View.inflate(getActivity(), R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);

        sharePopView = View.inflate(getActivity(), R.layout.share_pop_view, null);
        shareWxBtn = sharePopView.findViewById(R.id.share_wx_btn);
        shareWxBtn.setOnClickListener(this);
        shareCircleBtn = sharePopView.findViewById(R.id.share_circle_btn);
        shareCircleBtn.setOnClickListener(this);
        shareQqBtn = sharePopView.findViewById(R.id.share_qq_btn);
        shareQqBtn.setOnClickListener(this);

        /**
         * 发送评论
         */
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInput.getText())) {
                    Toast.makeText(getActivity(), "请输入评论", Toast.LENGTH_SHORT).show();
                } else {
                    toComment(edtInput.getText().toString(), myContentId);
                }
            }
        });

        rootView = view.findViewById(R.id.root);

        dismissPop = contentView.findViewById(R.id.dismiss_pop);
        dismissPop.setOnClickListener(this);
        commentPopRv = contentView.findViewById(R.id.comment_pop_rv);
        commentEdtInput = contentView.findViewById(R.id.comment_edtInput);
        commentPopRl = contentView.findViewById(R.id.comment_pop_rl);
        commentPopRl.setOnClickListener(this);
        initCommentPopRv();
        videoDetailCommentBtn = view.findViewById(R.id.video_detail_comment_btn);
        videoDetailCommentBtn.setOnClickListener(this);
        videoDetailWhiteCommentRl = view.findViewById(R.id.video_detail_white_comment_rl);
        videoDetailWhiteCommentRl.setOnClickListener(this);
        adapter = new XkshVideoAdapter(R.layout.xksh_video_item_layout, mDatas, getActivity(),
                playerView, refreshLayout, videoDetailCommentBtn, xkshManager);
        adapter.setPreLoadNumber(2);
        adapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(requestLoadMoreListener, videoDetailRv);
        /**
         * 无wifi 继续播放点击
         */
        adapter.setToAddPlayerViewClick(new XkshVideoAdapter.ToAddPlayerViewClick() {
            @Override
            public void clickNoWifi(int position) {
                SPUtils.getInstance().put(Constants.AGREE_NETWORK, "1");
                for (int i = 0; i < mDatas.size(); i++) {
                    mDatas.get(i).setWifi(true);
                }

                for (int i = 0; i < ((VideoHomeActivity) getActivity()).videoDetailFragment.mDatas.size(); i++) {
                    ((VideoHomeActivity) getActivity()).videoDetailFragment.mDatas.get(i).setWifi(true);
                }
                addPlayView(position);
                adapter.notifyDataSetChanged();
                ((VideoHomeActivity) getActivity()).videoDetailFragment.adapter.notifyDataSetChanged();
            }
        });

        /**
         * 关注按钮
         */
        adapter.setFollowViewClick(new XkshVideoAdapter.FollowViewClick() {
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

        videoDetailRv.setAdapter(adapter);
    }

    /**
     * 当前fragment是否显示
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisibleToUser = isVisibleToUser;
        if (null == playerView || mDatas.isEmpty()) {
            return;
        }
        if (null == adapter) {
            return;
        }

        if (isVisibleToUser) {
            if (null != playerView && null != playerView.getParent()) {
                ((ViewGroup) playerView.getParent()).removeView(playerView);
            }
            //重置重播标识
            playerView.buriedPointModel.setIs_renew("false");
            addPlayView(currentIndex);
        } else {
            if (mDuration != 0 && playerView.mWindowPlayer.mProgress != 0) {
                //上报埋点
                everyOneDuration = playerView.mWindowPlayer.mProgress;
                pointPercent = (double) everyOneDuration / mDuration;
                BigDecimal two = new BigDecimal(pointPercent);
                double pointPercentTwo = two.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(getActivity(), mDataDTO.getThirdPartyId(), String.valueOf(everyOneDuration*1000), String.valueOf(pointPercentTwo*100), Constants.CMS_VIDEO_OVER_AUTO), Constants.CMS_VIDEO_OVER_AUTO);
            }

            playerView.mSuperPlayer.pause();
            if (null != rlLp) {
                rlLp.removeView(playerView);
            }
            lsDuration = 0;
        }
    }

    /**
     * 在视频playview位置上添加各种view
     *
     * @param position
     */
    public void addPlayView(final int position) {
        if (SPUtils.isVisibleNoWifiView(getActivity())) {
            return;
        }

        if (null != playerView.mWindowPlayer && null != playerView.mWindowPlayer.mLayoutBottom && null != playerView.mWindowPlayer.mLayoutBottom.getParent()) {
            ((ViewGroup) playerView.mWindowPlayer.mLayoutBottom.getParent()).removeView(playerView.mWindowPlayer.mLayoutBottom);
        }
        LinearLayout linearLayout = (LinearLayout) adapter.getViewByPosition(currentIndex, R.id.introduce_lin);
        RelativeLayout.LayoutParams mLayoutBottomParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams playViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout itemRelativelayout = (RelativeLayout) adapter.getViewByPosition(position, R.id.item_relativelayout);
        String videoType = videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(position).getWidth())),
                Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(position).getHeight())));
        if (TextUtils.equals("0", videoType)) {
            playViewParams.addRule(RelativeLayout.ABOVE, videoDetailCommentBtn.getId());
            playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            playerView.setOrientation(false);
            if (linearLayout != null) {
                linearLayout.addView(playerView.mWindowPlayer.mLayoutBottom, 0);
            }
        } else if (TextUtils.equals("1", videoType)) {
            if (phoneIsNormal()) {
                playViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
                playerView.setOrientation(false);
            } else {
                playViewParams.addRule(RelativeLayout.ABOVE, videoDetailCommentBtn.getId());
                playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                playerView.setOrientation(false);
            }
            if (linearLayout != null) {
                linearLayout.addView(playerView.mWindowPlayer.mLayoutBottom, 0);
            }
        } else {
            playViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            playerView.setOrientation(true);
            mLayoutBottomParams.addRule(BELOW, playerView.getId());
            mLayoutBottomParams.setMargins(0, (Utils.getContext().getResources().getDisplayMetrics().heightPixels / 2) + ButtonSpan.dip2px(135), 0, 0);
            playerView.mWindowPlayer.mLayoutBottom.setLayoutParams(mLayoutBottomParams);
            if (null != itemRelativelayout) {
                itemRelativelayout.addView(playerView.mWindowPlayer.mLayoutBottom);
            }
        }
        playerView.setLayoutParams(playViewParams);
        playerView.setTag(position);
        playerView.setBackgroundColor(Utils.getContext().getResources().getColor(R.color.video_black));

        if (rlLp != null && mIsVisibleToUser) {
            rlLp.addView(playerView, 0);
            //露出即上报
            uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(getActivity(), mDataDTO.getThirdPartyId(), "", "", Constants.CMS_CLIENT_SHOW), Constants.CMS_CLIENT_SHOW);
            play(mDatas.get(position).getPlayUrl(), mDatas.get(position).getTitle());
        }


    }

    private void initSmartRefresh(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (playerView.mSuperPlayer.getPlayerState() == SuperPlayerDef.PlayerState.PLAYING) {
                    playerView.mSuperPlayer.pause();
                }
                isLoadComplate = false;
                adapter.setOnLoadMoreListener(requestLoadMoreListener, videoDetailRv);
                getPullDownData(String.valueOf(mVideoSize), panelCode, "false");
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
                            loadMoreData(ApiConstants.getInstance().getVideoDetailListUrl(), mDatas.get(mDatas.size() - 1).getId() + "", panelCode, "true");
                        }
                    });
                }
            }
        };
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
        int phoneWidth = ScreenUtils.getPhoneWidth(getActivity());
        int phoneHeight = ScreenUtils.getPhoneHeight(getActivity());
        if (phoneHeight * 9 == phoneWidth * 16) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 初始化评论列表
     */
    private void initCommentPopRv() {
        mCommentPopRvData = new ArrayList<>();
        mCommentPopDtoData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentPopRv.setLayoutManager(linearLayoutManager);
        commentPopRvAdapter = new CommentPopRvAdapter(R.layout.comment_pop_rv_item, mCommentPopRvData, getActivity());
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
     * 评论列表弹出框
     */
    private void showCommentPopWindow() {
        if (null == popupWindow) {
            //创建并显示popWindow
            popupWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                    .setView(contentView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                    .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, Utils.getContext().getResources().getDisplayMetrics().heightPixels - ButtonSpan.dip2px(200))
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .create()
                    .showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        } else {
            popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
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
            choosePop = new CustomPopWindow.PopupWindowBuilder(getActivity())
                    .setView(chooseContentView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .create()
                    .showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            choosePop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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
            sharePop = new CustomPopWindow.PopupWindowBuilder(getActivity())
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

    public void toShare(DataDTO item, String platform) {
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
//        if (null == playUrl || TextUtils.isEmpty(playUrl)) {
//            ToastUtils.showShort("当前播放地址(" + playUrl + "),是一个无效地址");
//            return;
//        }
        if (null != playerView) {
//            videoStaticBg.setVisibility(View.GONE);
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
     * 获取下拉列表数据
     *
     * @param removeFirst
     */
    private void getPullDownData(String pageSize, String panelCode, String removeFirst) {
        if (null != playerView && null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }
        mDatas.clear();
        OkGo.<VideoDetailModel>get(ApiConstants.getInstance().getVideoDetailListUrl())
                .tag(VIDEOTAG)
                .params("pageSize", pageSize)
                .params("panelCode", panelCode)
                .params("removeFirst", removeFirst)
                .params("type", "")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<VideoDetailModel>(VideoDetailModel.class) {
                    @Override
                    public void onSuccess(Response<VideoDetailModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (response.body().getCode().equals(success_code)) {
                            if (null == response.body().getData()) {
                                ToastUtils.showShort(R.string.data_err);
                                return;
                            }

                            mDatas.addAll(response.body().getData());
                            for (int i = 0; i < mDatas.size(); i++) {
                                String videoType = videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(i).getWidth())),
                                        Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(i).getHeight())));
                                if (TextUtils.equals("0", videoType) || TextUtils.equals("1", videoType)) {
                                    mDatas.get(i).setFullBtnIsShow(false);
                                } else {
                                    mDatas.get(i).setFullBtnIsShow(true);
                                }
                            }
                            setDataWifiState(mDatas, getActivity());
                            adapter.setNewData(mDatas);
                            if (mDatas.size() > 0) {
                                initialize = false;
                                recordContentId = String.valueOf(mDatas.get(mDatas.size() - 1).getId());
                            }
                            videoDetailCommentBtn.setVisibility(View.VISIBLE);
                            commentListEmptyRl.setVisibility(View.GONE);
                        } else {
                            videoDetailCommentBtn.setVisibility(View.GONE);
                            commentListEmptyRl.setVisibility(View.VISIBLE);
                        }
                        if (null != refreshLayout) {
                            refreshLayout.finishRefresh();
                        }
//                        if (null != playerView) {
//                            playerView.resetPlayer();
//                        }
                    }

                    @Override
                    public void onError(Response<VideoDetailModel> response) {
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
     * 获取更多数据
     */
    private void loadMoreData(String url, String contentId, String panelCode, String removeFirst) {

        OkGo.<VideoDetailModel>get(url)
                .tag(VIDEOTAG)
                .params("contentId", contentId)
                .params("pageSize", 10)
                .params("panelCode", panelCode)
                .params("removeFirst", removeFirst)
                .params("type", "")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<VideoDetailModel>(VideoDetailModel.class) {
                    @Override
                    public void onSuccess(Response<VideoDetailModel> response) {
                        if (null == response.body()) {
                            isLoadComplate = true;
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (response.body().getCode().equals(success_code)) {
                            if (null == response.body().getData()) {
                                isLoadComplate = true;
                                ToastUtils.showShort(R.string.data_err);
                                return;
                            }

                            if (response.body().getData().size() == 0) {
                                Log.e("loadMoreData", "没有更多视频了");
                                adapter.loadMoreEnd();
                                adapter.setOnLoadMoreListener(null, videoDetailRv);
                                isLoadComplate = true;
                            } else {
                                adapter.setOnLoadMoreListener(requestLoadMoreListener, videoDetailRv);
                                isLoadComplate = false;
                            }
                            mDatas.addAll(response.body().getData());
                            setDataWifiState(mDatas, getActivity());
                            adapter.setNewData(mDatas);
                            recordContentId = String.valueOf(mDatas.get(mDatas.size() - 1).getId());
                            Log.e("loadMoreData", "loadMoreData========" + mDatas.size());
                            adapter.loadMoreComplete();
                        } else {
                            adapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onError(Response<VideoDetailModel> response) {
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
                            return;
                        }
                        ToastUtils.showShort(R.string.net_err);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        refreshLayout.setEnableRefresh(true);
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
    private void toComment(String content, String contentId) {
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
                                KeyboardUtils.hideKeyboard(getActivity().getWindow().getDecorView());
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
    public void getContentState(String contentId) {
        OkGo.<ContentStateModel>get(ApiConstants.getInstance().queryStatsData())
                .tag(VIDEOTAG)
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
                                playerView.setContentStateModel(myContentId, videoType);
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
     * 设置关注
     */
    private void setFollowView(String whetherFollow) {
        /**
         * 设置关注
         */
        TextView followText = (TextView) adapter.getViewByPosition(currentIndex, R.id.follow);
        String localUserId = PersonInfoManager.getInstance().getUserId();
        String userId = mDataDTO.getCreateBy();
        if (TextUtils.isEmpty(localUserId) && TextUtils.isEmpty(userId)) {
            followText.setVisibility(View.VISIBLE);
        } else {
            if (TextUtils.equals(localUserId, userId)) {
                //如果发布者是本人  不显示关注按钮
                followText.setVisibility(View.GONE);
            } else {
                followText.setVisibility(View.VISIBLE);
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
        }
    }


    /**
     * 收藏/取消收藏
     */
    private void addOrCancelFavor(String contentId, String type) {
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
                                    videoDetailCollectionImage.setImageResource(R.drawable.collection);
                                    playerView.contentStateModel.setWhetherFavor("true");
                                } else {
                                    int num;
                                    num = Integer.parseInt(NumberFormatTool.getNumStr(collectionNum.getText().toString()));
                                    if (num > 0) {
                                        num--;
                                    }
                                    collectionNum.setText(NumberFormatTool.formatNum(num, false));
                                    videoDetailCollectionImage.setImageResource(R.drawable.collection_icon);
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
                                    String jsonString = BuriedPointModelManager.getLikeAndFavorBuriedPointData(myContentId, mDatas.get(currentIndex).getTitle(),
                                            "", "", "", "", mDatas.get(currentIndex).getIssueTimeStamp(),
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

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsVisibleToUser) {
            return;
        }
        if (playerView != null) {
            playerView.mSuperPlayer.resume();
        }

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

    /**
     * 使用获取的code去换token
     */
    private void getUserToken(String token) {
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

    @Override
    public void onPause() {
        super.onPause();
        if (playerView == null) {
            return;
        }

        if (!mIsVisibleToUser) {
            return;
        }

        if (playerView.mWindowPlayer.mCurrentPlayState != SuperPlayerDef.PlayerState.END) {
            /**
             * 上报内容埋点 视频播放时长
             * 如果 当前播放到的时长进度 小于 上一次存的时长进度 （用户往回拖动进度条，上报用户看到最长的视频时长进度）
             */
            if (playerView.mWindowPlayer.mProgress < playerView.mWindowPlayer.getRecordDuration()) {
                everyOneDuration = playerView.mWindowPlayer.getRecordDuration();
            } else {
                everyOneDuration = playerView.mWindowPlayer.mProgress - lsDuration;
            }

            //当前节点播放的时长/总视频时长 = 这一次观看的视频进度百分比
            if (mDuration != 0) {
                /**
                 * 上报内容埋点 视频播放时长
                 * 如果 当前播放到的时长进度 小于 上一次存的时长进度  那么不上报（用户往回拖动进度条，只上报用户看到最长的视频时长进度）
                 */
                String event;
                if (null == playerView.buriedPointModel.getIs_renew() || TextUtils.equals("false", playerView.buriedPointModel.getIs_renew())) {
                    //不为重播
                    event = Constants.CMS_VIDEO_OVER_AUTO;
                } else {
                    //重播
                    event = Constants.CMS_VIDEO_OVER;
                }

                pointPercent = (double) everyOneDuration / mDuration;
                BigDecimal two = new BigDecimal(pointPercent);
                double pointPercentTwo = two.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                lsDuration = everyOneDuration;

                //上报埋点
                uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(getActivity(), mDataDTO.getThirdPartyId(), String.valueOf(everyOneDuration * 1000), String.valueOf(Math.floor(pointPercentTwo * 100)), event), event);
            }
        }
        playerView.mSuperPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.video_detail_collection) {//收藏
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                addOrCancelFavor(myContentId, videoType);
            }

        } else if (id == R.id.video_detail_likes) {//点赞
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                addOrCancelLike(myContentId, videoType);
            }
        } else if (id == R.id.dismiss_pop) {
            if (popupWindow != null) {
                popupWindow.dissmiss();
            }
        } else if (id == R.id.video_detail_comment_btn) {
            showCommentPopWindow();
        } else if (id == R.id.comment_pop_rl) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                try {
                    noLoginTipsPop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                KeyboardUtils.toggleSoftInput(getActivity().getWindow().getDecorView());
                showInputEdittextAndSend();
            }
        } else if (id == R.id.video_detail_white_comment_rl) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                try {
                    noLoginTipsPop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                KeyboardUtils.toggleSoftInput(getActivity().getWindow().getDecorView());
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
            String jsonString = BuriedPointModelManager.getShareClick(myContentId, mDatas.get(currentIndex).getTitle(), "",
                    "", "", "", mDatas.get(currentIndex).getIssueTimeStamp(), Constants.CONTENT_TYPE, "");
            Log.e("埋点", "埋点：分享按钮---" + jsonString);
            sharePop();
        } else if (id == R.id.share_wx_btn) {
            if (mDatas.isEmpty()) {
                return;
            }
            String jsonString = BuriedPointModelManager.getShareType(myContentId, mDatas.get(currentIndex).getTitle(), "",
                    "", "", "", mDatas.get(currentIndex).getIssueTimeStamp(), Constants.CONTENT_TYPE, Constants.WX_STRING);
            Log.e("埋点", "埋点：分享到微信朋友---" + jsonString);
            toShare(mDataDTO, Constants.SHARE_WX);
        } else if (id == R.id.share_circle_btn) {
            if (mDatas.isEmpty()) {
                return;
            }
            String jsonString = BuriedPointModelManager.getShareType(myContentId, mDatas.get(currentIndex).getTitle(), "",
                    "", "", "", mDatas.get(currentIndex).getIssueTimeStamp(), Constants.CONTENT_TYPE, Constants.CIRCLE_STRING);
            Log.e("埋点", "埋点：分享到微信朋友圈---" + jsonString);
            toShare(mDataDTO, Constants.SHARE_CIRCLE);
        } else if (id == R.id.share_qq_btn) {
            if (mDatas.isEmpty()) {
                return;
            }
            String jsonString = BuriedPointModelManager.getShareType(myContentId, mDatas.get(currentIndex).getTitle(), "",
                    "", "", "", mDatas.get(currentIndex).getIssueTimeStamp(), Constants.CONTENT_TYPE, Constants.QQ_STRING);
            Log.e("埋点", "埋点：分享到QQ---" + jsonString);
            toShare(mDataDTO, Constants.SHARE_QQ);
        } else if (id == R.id.shot_alike) {
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                try {
                    noLoginTipsPop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                startActivity(new Intent(getActivity(), UploadActivity.class));
            }
        } else if (id == R.id.rank_list) {
            //跳转H5排行榜
            try {
                if (Utils.mIsDebug) {
                    param.recommendUrl(Constants.RANKING_LIST, null);
                } else {
                    param.recommendUrl(Constants.RANKING_LIST_ZS, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 弹出发送评论弹出窗
     */
    private void showInputEdittextAndSend() {
        //创建并显示popWindow
        if (null == inputAndSendPop) {
            inputAndSendPop = new CustomPopWindow.PopupWindowBuilder(getActivity())
                    .setView(sendPopContentView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, ButtonSpan.dip2px(50))
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
            noLoginTipsPop = new CustomPopWindow.PopupWindowBuilder(getActivity())
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
        softKeyBoardListener = new SoftKeyBoardListener(getActivity());
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
                        if (response.body().getCode().equals("200")) {
                            recommondList.clear();
                            recommondList.addAll(response.body().getData().getRecords());
                            if (recommondList.size() > 1) {
                                adapter.setRecommendList(recommondList, true);
                                mDatas.get(position).setRecommendVisible(true);
                            } else if (recommondList.size() == 1) {
                                adapter.setRecommendList(recommondList, false);
                                mDatas.get(position).setRecommendVisible(true);
                            } else if (recommondList.size() == 0) {
                                adapter.setRecommendList(recommondList, false);
                                mDatas.get(position).setRecommendVisible(false);
                            }
                            adapter.notifyDataSetChanged();
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
                        if (SPUtils.isVisibleNoWifiView(getActivity())) {
                            SPUtils.getInstance().put(Constants.AGREE_NETWORK, "0");
                        }
                        loadingProgress.setVisibility(View.GONE);
                        addPlayView(position);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SuperPlayerImpl.mCurrentPlayVideoURL = null;
        if (playerView != null) {
            playerView.stopPlay();
            playerView.release();
            playerView.mSuperPlayer.destroy();
        }
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
                        if (200 == response.body().getCode()) {
                            setFollowView("true");
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
}