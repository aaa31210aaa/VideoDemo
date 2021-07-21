package ui;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.contants.Contants;
import com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl;
import com.tencent.liteav.demo.superplayer.model.utils.SystemUtils;
import com.wdcs.videodetail.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adpter.CommentPopRvAdapter;
import adpter.MyVideoDetailAdapter;
import adpter.VideoDetailPopChooseAdapter;
import callback.JsonCallback;
import callback.VideoInteractiveParam;
import constants.Constants;
import http.ApiConstants;
import manager.PersonInfoManager;
import model.CommentModel;
import model.CommentModel.DataDTO.RecordsDTO;
import model.ContentStateModel;
import model.DataDTO;
import model.ShareInfo;
import model.TokenModel;
import model.VideoCollectionModel;
import model.VideoDetailModel;
import utils.KeyboardUtils;
import utils.NumberFormatTool;
import utils.SPUtils;
import utils.SoftKeyBoardListener;
import utils.ToastUtils;
import widget.LoadingView;

import static constants.Constants.success_code;
import static constants.Constants.token_error;

/**
 * 视频详情页 可滑动查看视频
 */
@Keep
public class VideoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private RecyclerView videoDetailRv;
    public MyVideoDetailAdapter adapter;
    private CommentPopRvAdapter commentPopRvAdapter;
    //视频列表数据
    public List<DataDTO> mDatas;
    //评论列表数据
    private List<RecordsDTO> mCommentPopRvData;
    private List<CommentModel.DataDTO> mCommentPopDtoData;
    private SuperPlayerView playerView;
    private ImageView videoStaticBg;
    private ImageView startPlay;

    public RelativeLayout videoDetailCommentBtn;
    //评论列表弹窗
    private CustomPopWindow popupWindow;
    private boolean popupWindowIsShow;
    private LinearLayout videoDetailCollection;
    private RelativeLayout videoDetailLikes;

    private View contentView;
    private View chooseContentView;
    private RelativeLayout dismissPop;
    private RecyclerView commentPopRv;
    private TextView commentEdtInput;
    private RelativeLayout collectionBtn;
    private RelativeLayout likesBtn;
    private RelativeLayout commentPopRl;
    //附着在软键盘上的输入弹出窗
    private CustomPopWindow inputAndSendPop;
    private View sendPopContentView;
    private View rootView;
    private int visibleWindowDisplayHeight = 0;
    private LinearLayout edtParent;
    private EditText edtInput;
    private TextView tvSend;
    private RelativeLayout videoDetailWhiteCommentRl;
    //选择集数弹窗
    public CustomPopWindow choosePop;
    private RecyclerView videoDetailChoosePopRv;
    private VideoDetailPopChooseAdapter videoDetailPopChooseAdapter;
    private List<DataDTO> choosePopDatas;

    private ViewPagerLayoutManager manager;
    private ImageView choosePopDismiss;
    private RefreshLayout refreshLayout;
    private String token = "";
    private String transformationToken = "";
    private String contentId = "";
    private String panelId = "";
    private RelativeLayout commentListEmptyRl;
    private String recordContentId;//记录的内容id
    private boolean initialize = true;
    private int mVideoSize = 10; //每页视频多少条
    private int mPageIndex = 1; //评论列表页数
    private int mPageSize = 10; //评论列表每页多少条
    public String myContentId = ""; //记录当前视频id
    public int currentIndex = 0; //记录当前视频列表的位置
    private TextView commentTotal;
    private TextView commentPopCommentTotal;
    private ImageView videoDetailCollectionImage; //收藏图标
    private ImageView videoDetailLikesImage; //点赞图标
    private TextView likesNum; //点赞数
    private ContentStateModel.DataDTO contentStateModel;
    private ImageView videoDetailCommentCollectionImage; //评论弹窗收藏图标
    private ImageView videoDetailCommentLikesImage; //评论弹窗点赞图标
    private TextView videoDetailCommentLikesNum; //评论弹窗点赞数
    private String videoType; //视频类型
    private LoadingView loadingProgress;
    private RelativeLayout backLl;
    private VideoInteractiveParam param;
    public String playUrl;
    private TextView commentEdittext;
    private String videoTag = "videoTag";
    private boolean isLoadComplate = false;
    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener;
    public View decorView;
    private SoftKeyBoardListener softKeyBoardListener;
    private CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    private ImageView share;
    private CustomPopWindow sharePop;
    private View sharePopView;
    private ImageView shareWxBtn;
    private ImageView shareCircleBtn;
    private ImageView shareQqBtn;
    private DataDTO mDataDTO;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_video_detail);
        decorView = getWindow().getDecorView();
//        SystemUtils.hideSystemUI(decorView);
        SystemUtils.setNavbarColor(this, R.color.black);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION);
        initView();
        if ((null == panelId || TextUtils.isEmpty(panelId)) &&
                (null == contentId || TextUtils.isEmpty(contentId))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("错误");
            builder.setCancelable(false);
            //设置正面按钮
            builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).show();
        } else if (null == panelId || TextUtils.isEmpty(panelId)) {
            getOneVideo(contentId);
        } else {
            getPullDownData(contentId, "10", panelId, "false");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        SystemUtils.setNavbarColor(this, R.color.white);
    }

    private void initView() {
        token = getIntent().getStringExtra("token");
        panelId = getIntent().getStringExtra("panelId");
        contentId = getIntent().getStringExtra("contentId");
        param = VideoInteractiveParam.getInstance();
//        ScreenUtils.setStatusBarColor(this, R.color.black);
//        PersonInfoManager.getInstance().setTransformationToken(token);
        backLl = findViewById(R.id.back_ll);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        share = findViewById(R.id.share);
        share.setOnClickListener(this);
        mDatas = new ArrayList<>();
        mDataDTO = new DataDTO();
        loadingProgress = findViewById(R.id.loading_progress);
        loadingProgress.setVisibility(View.VISIBLE);
        commentEdittext = findViewById(R.id.comment_edittext);
        commentListEmptyRl = findViewById(R.id.comment_list_empty_rl);
        videoDetailRv = (RecyclerView) findViewById(R.id.video_detail_rv);
        videoDetailRv.setHasFixedSize(true);
        videoDetailCollectionImage = findViewById(R.id.video_detail_collection_image);
        videoDetailLikesImage = findViewById(R.id.video_detail_likes_image);
        likesNum = findViewById(R.id.likes_num);
        manager = new ViewPagerLayoutManager(this);
        videoDetailRv.setLayoutManager(manager);
        setSoftKeyBoardListener();
        manager.setOnViewPagerListener(new OnViewPagerListener() {


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
                myContentId = String.valueOf(mDatas.get(0).getId());
                if (!PersonInfoManager.getInstance().isRequestToken()) {
                    getContentState(myContentId);
                }
                SuperPlayerImpl.mCurrentPlayVideoURL = mDatas.get(0).getPlayUrl();

                mPageIndex = 1;
                if (mDatas.get(0).getDisableComment()) {
                    videoDetailWhiteCommentRl.setEnabled(false);
                    commentPopRl.setEnabled(false);
                    commentEdittext.setText("该内容禁止评论");
                    commentEdtInput.setHint("该内容禁止评论");
                } else {
                    videoDetailWhiteCommentRl.setEnabled(true);
                    commentPopRl.setEnabled(true);
                    commentEdittext.setText("写评论...");
                    commentEdtInput.setHint("写评论...");
                }
                getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), true);
                videoType = mDatas.get(0).getType();
                Log.e("T8000", "onInitComplete");
                playerView = new SuperPlayerView(VideoDetailActivity.this, decorView);
                if (SPUtils.isVisibleNoWifiView(VideoDetailActivity.this)) {
                    SPUtils.getInstance().put(Constants.AGREE_NETWORK, "0");
                } else {
                    addPlayView(0);
                }
                initChoosePop();

                /**
                 * 监听播放器播放窗口变化回调
                 */
                playerView.playModeCallBack = new SuperPlayerView.PlayModeCallBack() {
                    @Override
                    public void getPlayMode(SuperPlayerDef.PlayerMode playerMode) {
                        if (playerMode.equals(SuperPlayerDef.PlayerMode.FULLSCREEN)) {
                            backLl.setVisibility(View.GONE);
                            manager.setCanScoll(false);
                            refreshLayout.setEnableRefresh(false);
                            adapter.setEnableLoadMore(false);
                            videoDetailCommentBtn.setVisibility(View.GONE);
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
                            KeyboardUtils.hideKeyboard(getWindow().getDecorView());
                        } else if (playerMode.equals(SuperPlayerDef.PlayerMode.WINDOW)) {
                            backLl.setVisibility(View.VISIBLE);
                            manager.setCanScoll(true);
                            refreshLayout.setEnableRefresh(true);
                            adapter.setEnableLoadMore(true);
                            videoDetailCommentBtn.setVisibility(View.VISIBLE);
                        }
                    }
                };
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
                mDataDTO = mDatas.get(position);
                SuperPlayerImpl.mCurrentPlayVideoURL = mDatas.get(position).getPlayUrl();
                playUrl = mDatas.get(position).getPlayUrl();
                currentIndex = position;
                choosePopDatas.clear();
                reset();
                myContentId = String.valueOf(mDatas.get(position).getId());
                videoDetailPopChooseAdapter.setContentId(myContentId);
                videoType = mDatas.get(position).getType();
                mPageIndex = 1;
                if (mDatas.get(position).getDisableComment()) {
                    videoDetailWhiteCommentRl.setEnabled(false);
                    commentPopRl.setEnabled(false);
                    commentEdittext.setText("该内容禁止评论");
                    commentEdtInput.setHint("该内容禁止评论");
                } else {
                    videoDetailWhiteCommentRl.setEnabled(true);
                    commentPopRl.setEnabled(true);
                    commentEdittext.setText("写评论...");
                    commentEdtInput.setHint("写评论...");
                }
                getCommentList(String.valueOf(mPageIndex), String.valueOf(mPageSize), true);
                getContentState(myContentId);

                if (SPUtils.isVisibleNoWifiView(VideoDetailActivity.this)) {
                    SPUtils.getInstance().put(Constants.AGREE_NETWORK, "0");
                } else {
                    addPlayView(position);
                }
                if (!"1".equals(playerView.mFullScreenPlayer.strSpeed)) {
                    playerView.mFullScreenPlayer.mVodMoreView.mCallback.onSpeedChange(1.0f);
                    playerView.mFullScreenPlayer.superplayerSpeed.setText("倍速");
                    playerView.mFullScreenPlayer.mRbSpeed1.setChecked(true);
                }
            }
        });

        adapter = new MyVideoDetailAdapter(R.layout.video_detail_item, mDatas, this);
        adapter.openLoadAnimation();
        View footView = View.inflate(this, R.layout.footer_view, null);
        adapter.setFooterView(footView);
        videoDetailRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        initSmartRefresh();
        commentTotal = findViewById(R.id.comment_total);
        videoDetailCollection = findViewById(R.id.video_detail_collection);
        videoDetailCollection.setOnClickListener(this);
        videoDetailLikes = findViewById(R.id.video_detail_likes);
        videoDetailLikes.setOnClickListener(this);

        contentView = View.inflate(this, R.layout.video_detail_comment_pop, null);
        sendPopContentView = View.inflate(this, R.layout.layout_input_window, null);
        commentPopCommentTotal = contentView.findViewById(R.id.comment_pop_comment_total);
        edtParent = sendPopContentView.findViewById(R.id.edt_parent);
        edtInput = sendPopContentView.findViewById(R.id.edtInput);
        tvSend = sendPopContentView.findViewById(R.id.tvSend);

        noLoginTipsView = View.inflate(this, R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);

        sharePopView = View.inflate(this, R.layout.share_pop_view, null);
        shareWxBtn = sharePopView.findViewById(R.id.share_wx_btn);
        shareCircleBtn = sharePopView.findViewById(R.id.share_circle_btn);
        shareQqBtn = sharePopView.findViewById(R.id.share_qq_btn);

        /**
         * 发送评论
         */
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInput.getText())) {
                    Toast.makeText(VideoDetailActivity.this, "请输入评论", Toast.LENGTH_SHORT).show();
                } else {
                    toComment(edtInput.getText().toString(), myContentId);
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
        collectionBtn = contentView.findViewById(R.id.collection_btn);
        collectionBtn.setOnClickListener(this);
        likesBtn = contentView.findViewById(R.id.video_detail_comment_likes_btn);
        likesBtn.setOnClickListener(this);
        videoDetailCommentCollectionImage = contentView.findViewById(R.id.video_detail_comment_collection_image);
        videoDetailCommentLikesImage = contentView.findViewById(R.id.video_detail_comment_likes_image);
        videoDetailCommentLikesNum = contentView.findViewById(R.id.video_detail_comment_likes_num);

        initCommentPopRv();

        videoDetailCommentBtn = findViewById(R.id.video_detail_comment_btn);
        videoDetailCommentBtn.setOnClickListener(this);
        videoDetailWhiteCommentRl = findViewById(R.id.video_detail_white_comment_rl);
        videoDetailWhiteCommentRl.setOnClickListener(this);

    }

    /**
     * 在视频playview位置上添加各种view
     *
     * @param position
     */
    public void addPlayView(final int position) {
        final ViewGroup rlLp = (ViewGroup) manager.findViewByPosition(position);
        final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        videoStaticBg = new ImageView(VideoDetailActivity.this);
//        videoStaticBg.setScaleType(ImageView.ScaleType.FIT_XY);
//        Glide.with(VideoDetailActivity.this).load(mDatas.get(position).getThumbnailUrl()).into(videoStaticBg);
        RelativeLayout.LayoutParams itemLp = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, ButtonSpan.dip2px(200));
        itemLp.setMargins(0, 0, 0, ButtonSpan.dip2px(50));
        RelativeLayout item = new RelativeLayout(VideoDetailActivity.this);
        startPlay = new ImageView(VideoDetailActivity.this);
        startPlay.setImageResource(R.drawable.play_start);
        //外层设置在整个布局中 居中
//        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout startRl = new RelativeLayout(VideoDetailActivity.this);
        RelativeLayout.LayoutParams startlp = new RelativeLayout.LayoutParams(ButtonSpan.dip2px(50),
                ButtonSpan.dip2px(50));
//        videoStaticBg.setTag(position);
        startPlay.setTag(position);
        playerView.setTag(position);
        playerView.setBackgroundColor(getResources().getColor(R.color.black));
        startlp.addRule(RelativeLayout.CENTER_IN_PARENT);
        startRl.addView(startPlay, startlp);
        item.addView(startRl, itemLp);
        //里层rl添加view
//        playerViewRl.addView(videoStaticBg, linLp);
//        playerViewRl.addView(startRl, linLp);
        //外层item view 添加里层的view
//        item.addView(playerViewRl,lp);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        item.addView(playerView, itemLp);
        rlLp.addView(item, lp);
        play(mDatas.get(position).getPlayUrl(), mDatas.get(position).getTitle());
//            }
//        }, 200);
    }

    private void initSmartRefresh() {
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadComplate = false;
                adapter.setOnLoadMoreListener(requestLoadMoreListener, videoDetailRv);
                getRandomData(String.valueOf(mVideoSize));
            }
        });
        adapter.setPreLoadNumber(2);
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
                            if (TextUtils.isEmpty(panelId)) {
                                loadMoreData(ApiConstants.getInstance().getVideDetailRandomListUrl(), mDatas.get(mDatas.size() - 1).getId() + "", panelId, "true");
                            } else {
                                loadMoreData(ApiConstants.getInstance().getVideoDetailListUrl(), mDatas.get(mDatas.size() - 1).getId() + "", panelId, "true");
                            }
                        }
                    });
                }
            }
        };
        adapter.setOnLoadMoreListener(requestLoadMoreListener, videoDetailRv);
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
        commentPopRvAdapter = new CommentPopRvAdapter(R.layout.comment_pop_rv_item, mCommentPopRvData, this);
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

    private void initChoosePop() {
        choosePopDatas = new ArrayList<>();
        chooseContentView = View.inflate(this, R.layout.video_detail_choose_pop, null);
        choosePopDismiss = chooseContentView.findViewById(R.id.choose_pop_dismiss);
        choosePopDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != choosePop) {
                    choosePop.dissmiss();
                }
            }
        });
        videoDetailChoosePopRv = chooseContentView.findViewById(R.id.video_detail_choose_pop_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        videoDetailChoosePopRv.setLayoutManager(gridLayoutManager);

        videoDetailPopChooseAdapter = new VideoDetailPopChooseAdapter(R.layout.video_detail_choose_pop_rv_item,
                this, choosePopDatas, videoDetailChoosePopRv, myContentId, playerView);
        videoDetailChoosePopRv.setAdapter(videoDetailPopChooseAdapter);
        videoDetailPopChooseAdapter.notifyDataSetChanged();

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
            choosePop = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(chooseContentView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .setAnimationStyle(R.style.take_popwindow_anim)
                    .create()
                    .showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            choosePop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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
            sharePop = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(sharePopView)
                    .setOutsideTouchable(false)
                    .setFocusable(true)
                    .size(getResources().getDisplayMetrics().widthPixels, ButtonSpan.dip2px(150))
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
        if (null == playUrl || TextUtils.isEmpty(playUrl)) {
            ToastUtils.showShort("当前播放地址(" + playUrl + "),是一个无效地址");
            return;
        }
        if (null != playerView) {
//            videoStaticBg.setVisibility(View.GONE);
            SuperPlayerModel model = new SuperPlayerModel();
            model.url = playUrl;
            model.title = title;
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
     * 获取单条视频详情
     */
    private void getOneVideo(String contentId) {
        OkGo.<String>get(ApiConstants.getInstance().getVideoDetailUrl() + contentId)
                .tag(videoTag)
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
                                    setDataWifiState(mDatas);
                                    adapter.setNewData(mDatas);
                                    if (!mDatas.isEmpty()) {
                                        initialize = false;
                                    }
                                } else {
                                    ToastUtils.showShort(jsonObject.get("message").toString());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (null != refreshLayout) {
                            refreshLayout.finishRefresh();
                        }
                        if (null != playerView) {
                            playerView.resetPlayer();
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
                        loadingProgress.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 获取下拉列表数据
     *
     * @param contentId
     * @param panelId
     * @param removeFirst
     */
    private void getPullDownData(String contentId, String pageSize, String panelId, String removeFirst) {
        if (null != playerView && null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }
        mDatas.clear();
        OkGo.<VideoDetailModel>get(ApiConstants.getInstance().getVideoDetailListUrl())
                .tag(videoTag)
                .params("contentId", contentId)
                .params("pageSize", pageSize)
                .params("panelId", panelId)
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
                            setDataWifiState(mDatas);
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
                        if (null != playerView) {
                            playerView.resetPlayer();
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
                        loadingProgress.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 获取随机视频列表
     *
     * @param pageSize
     */
    private void getRandomData(String pageSize) {
        if (null != playerView && null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }
        mDatas.clear();
        OkGo.<VideoDetailModel>get(ApiConstants.getInstance().getVideDetailRandomListUrl())
                .tag(videoTag)
                .params("pageSize", pageSize)
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
                            setDataWifiState(mDatas);
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
                        if (null != playerView) {
                            playerView.resetPlayer();
                        }
                    }

                    @Override
                    public void onError(Response<VideoDetailModel> response) {
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
                            return;
                        }
                        ToastUtils.showShort(R.string.net_err);
//                        videoDetailCommentBtn.setVisibility(View.GONE);
//                        commentListEmptyRl.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        reset();
                        loadingProgress.setVisibility(View.GONE);
                    }
                });

    }

    /**
     * 获取更多数据
     */
    private void loadMoreData(String url, String contentId, String panelId, String removeFirst) {

        OkGo.<VideoDetailModel>get(url)
                .tag(videoTag)
                .params("contentId", contentId)
                .params("pageSize", 10)
                .params("panelId", panelId)
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
                            setDataWifiState(mDatas);
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
     * 获取视频合集
     */
    public void getVideoCollection(final String pid) {
        if (choosePopDatas.size() > 0) {
            showChoosePop();
        } else {
            OkGo.<VideoCollectionModel>get(ApiConstants.getInstance().getVideoCollectionUrl() + pid)
                    .tag(videoTag)
                    .execute(new JsonCallback<VideoCollectionModel>(VideoCollectionModel.class) {
                        @Override
                        public void onSuccess(Response<VideoCollectionModel> response) {
                            if (null == response.body()) {
                                ToastUtils.showShort(R.string.data_err);
                                return;
                            }

                            if (response.body().getCode().equals("200")) {
                                if (null == response.body().getDatas()) {
                                    ToastUtils.showShort(R.string.data_err);
                                    return;
                                }
                                choosePopDatas.clear();
                                choosePopDatas.addAll(response.body().getDatas().getChildren());
                                videoDetailPopChooseAdapter.setNewData(choosePopDatas);
                                showChoosePop();
                            } else {
                                ToastUtils.showShort(response.body().getMessage());
                            }
                        }

                        @Override
                        public void onError(Response<VideoCollectionModel> response) {
                            if (null != response.body()) {
                                ToastUtils.showShort(response.body().getMessage());
                                return;
                            }
                            ToastUtils.showShort(R.string.net_err);
                        }
                    });
        }
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
                .tag(videoTag)
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
                .tag(videoTag)
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
    public void getContentState(String contentId) {
        OkGo.<ContentStateModel>get(ApiConstants.getInstance().queryStatsData())
                .tag(videoTag)
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

                            contentStateModel = response.body().getData();
                            if (contentStateModel.getWhetherFavor().equals("true")) {
                                videoDetailCollectionImage.setImageResource(R.drawable.collection);
                                videoDetailCommentCollectionImage.setImageResource(R.drawable.collection);
                            } else {
                                videoDetailCollectionImage.setImageResource(R.drawable.collection_unseletct);
                                videoDetailCommentCollectionImage.setImageResource(R.drawable.collection_unseletct);
                            }

                            if (contentStateModel.getWhetherLike().equals("true")) {
                                videoDetailLikesImage.setImageResource(R.drawable.likes);
                                videoDetailCommentLikesImage.setImageResource(R.drawable.likes);
                                likesNum.setTextColor(getResources().getColor(R.color.bz_red));
                                videoDetailCommentLikesNum.setTextColor(getResources().getColor(R.color.bz_red));
                            } else {
                                videoDetailLikesImage.setImageResource(R.drawable.likes_unselect);
                                videoDetailCommentLikesImage.setImageResource(R.drawable.likes_unselect);
                                likesNum.setTextColor(getResources().getColor(R.color.c9));
                                videoDetailCommentLikesNum.setTextColor(getResources().getColor(R.color.c9));
                            }

                            likesNum.setText(NumberFormatTool.formatNum(Long.parseLong(NumberFormatTool.getNumStr(contentStateModel.getLikeCountShow())), false));
                            videoDetailCommentLikesNum.setText(NumberFormatTool.formatNum(Long.parseLong(NumberFormatTool.getNumStr(contentStateModel.getLikeCountShow())), false));
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
                .tag(videoTag)
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
                                if (json.get("data").toString().equals("1")) {
                                    videoDetailCollectionImage.setImageResource(R.drawable.collection);
                                    videoDetailCommentCollectionImage.setImageResource(R.drawable.collection);
                                } else {
                                    videoDetailCollectionImage.setImageResource(R.drawable.collection_unseletct);
                                    videoDetailCommentCollectionImage.setImageResource(R.drawable.collection_unseletct);
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
                .tag(videoTag)
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
                            if (null != json && json.get("code").toString().equals("200")) {
                                if (json.get("data").toString().equals("1")) {
                                    int num;
                                    videoDetailLikesImage.setImageResource(R.drawable.likes);
                                    videoDetailCommentLikesImage.setImageResource(R.drawable.likes);
                                    likesNum.setTextColor(getResources().getColor(R.color.bz_red));
                                    videoDetailCommentLikesNum.setTextColor(getResources().getColor(R.color.bz_red));
                                    if (TextUtils.isEmpty(likesNum.getText().toString())) {
                                        num = 0;
                                    } else {
                                        num = Integer.parseInt(likesNum.getText().toString());
                                    }

                                    num++;
                                    likesNum.setText(NumberFormatTool.formatNum(num, false));
                                    videoDetailCommentLikesNum.setText(NumberFormatTool.formatNum(num, false));
                                } else {
                                    int num;
                                    videoDetailLikesImage.setImageResource(R.drawable.likes_unselect);
                                    videoDetailCommentLikesImage.setImageResource(R.drawable.likes_unselect);
                                    likesNum.setTextColor(getResources().getColor(R.color.c9));
                                    videoDetailCommentLikesNum.setTextColor(getResources().getColor(R.color.c9));
                                    if (TextUtils.isEmpty(likesNum.getText().toString())) {
                                        num = 0;
                                    } else {
                                        num = Integer.parseInt(likesNum.getText().toString());
                                    }
                                    num--;
                                    likesNum.setText(NumberFormatTool.formatNum(num, false));
                                    videoDetailCommentLikesNum.setText(NumberFormatTool.formatNum(num, false));
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


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (playerView != null) {
            playerView.mSuperPlayer.resume();
        }
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
    private void getUserToken(String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<TokenModel>post(ApiConstants.getInstance().mycsToken())
                .tag(videoTag)
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
                        getContentState(myContentId);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playerView != null) {
            playerView.mSuperPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SuperPlayerImpl.mCurrentPlayVideoURL = null;
        if (playerView != null) {
            playerView.stopPlay();
            playerView.release();
            playerView.mSuperPlayer.destroy();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        } else if (id == R.id.video_detail_collection || id == R.id.collection_btn) {//收藏
            if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                noLoginTipsPop();
            } else {
                addOrCancelFavor(myContentId, videoType);
            }

        } else if (id == R.id.video_detail_likes || id == R.id.video_detail_comment_likes_btn) {//点赞
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
                KeyboardUtils.toggleSoftInput(getWindow().getDecorView());
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
                KeyboardUtils.toggleSoftInput(getWindow().getDecorView());
                showInputEdittextAndSend();
            }
        } else if (id == R.id.no_login_tips_cancel) {
            if (null != noLoginTipsPop) {
                noLoginTipsPop.dissmiss();
            }
        } else if (id == R.id.no_login_tips_ok) {
            try {
                param.toLogin();
                if (null != noLoginTipsPop) {
                    noLoginTipsPop.dissmiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.share) {
            sharePop();
        } else if (id == R.id.share_wx_btn) {
            toShare(mDataDTO, Constants.SHARE_WX);
        } else if (id == R.id.share_circle_btn) {
            toShare(mDataDTO, Constants.SHARE_CIRCLE);
        } else if (id == R.id.share_qq_btn) {
            toShare(mDataDTO, Constants.SHARE_QQ);
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
            noLoginTipsPop = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(noLoginTipsView)
                    .setOutsideTouchable(true)
                    .setFocusable(true)
                    .setAnimationStyle(R.style.AnimCenter)
                    .size(getResources().getDisplayMetrics().widthPixels - ButtonSpan.dip2px(60), ButtonSpan.dip2px(180))
                    .create()
                    .showAtLocation(rootView, Gravity.CENTER, 0, 0);
        } else {
            noLoginTipsPop.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        }
    }

//    /**
//     * 监听布局高度变化
//     *
//     * @param v
//     * @param left
//     * @param top
//     * @param right
//     * @param bottom
//     * @param oldLeft
//     * @param oldTop
//     * @param oldRight
//     * @param oldBottom
//     */
//    @Override
//    public void onLayoutChange(View v, int left, int top, int right, int bottom,
//                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
//        //获取屏幕高度
//        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
//        //阀值设置为屏幕高度的1/3
//        int keyHeight = screenHeight / 3;
//        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
//        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
//        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
//            Log.e("onLayoutChange", "=======软键盘弹起");
//        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
//            Log.e("onLayoutChange", "=======软键盘关闭");
//            if (inputAndSendPop != null) {
//                inputAndSendPop.dissmiss();
//            }
//        }
//    }

    private void setDataWifiState(List<DataDTO> data) {
        if (SPUtils.isVisibleNoWifiView(this)) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setWifi(false);
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setWifi(true);
            }
        }
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
}