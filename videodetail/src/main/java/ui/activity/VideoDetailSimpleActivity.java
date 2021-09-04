package ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl;
import com.tencent.liteav.demo.superplayer.model.utils.SystemUtils;
import com.tencent.rtmp.TXLiveConstants;
import com.wdcs.callback.JsonCallback;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.model.DataDTO;
import com.wdcs.model.VideoDetailModel;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.NumberFormatTool;
import com.wdcs.utils.SPUtils;
import com.wdcs.utils.ScreenUtils;
import com.wdcs.utils.ToastUtils;
import com.wdcs.videodetail.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import widget.CircleImageView;
import widget.LoadingView;
import widget.SpannableTextView;

import static com.tencent.liteav.demo.superplayer.SuperPlayerView.instance;
import static com.wdcs.constants.Constants.BLUE_V;
import static com.wdcs.constants.Constants.success_code;
import static utils.NetworkUtil.setDataWifiState;

public class VideoDetailSimpleActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private SuperPlayerView playerView;
    private RelativeLayout agreeNowifiPlay;
    private TextView continuePlay;
    private TextView publisherName;
    private ImageView officialCertificationImg;
    private CircleImageView publisherHeadimg;
    private SpannableTextView foldText;
    private TextView expendText;
    private LinearLayout introduceLin;
    private LoadingView videoLoadingProgress;
    private String contentId = "";
    private List<DataDTO> mDatas;
    private LinearLayout superplayerIvFullscreen;
    private String TAG = "VideoDetailSimpleActivity";
    private RelativeLayout rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SystemUtils.setNavbarColor(this, R.color.black);
        setContentView(R.layout.activity_video_detail_simple);
        initView();
    }

    private void initView() {
        rootview = findViewById(R.id.rootview);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        playerView = SuperPlayerView.getInstance(this, getWindow().getDecorView(), true);
        agreeNowifiPlay = findViewById(R.id.agree_nowifi_play);
        continuePlay = findViewById(R.id.continue_play);
        continuePlay.setOnClickListener(this);
        publisherName = findViewById(R.id.publisher_name);
        officialCertificationImg = findViewById(R.id.official_certification_img);
        publisherHeadimg = findViewById(R.id.publisher_headimg);
        foldText = findViewById(R.id.fold_text);
        expendText = findViewById(R.id.expend_text);
        introduceLin = findViewById(R.id.introduce_lin);
        videoLoadingProgress = findViewById(R.id.video_loading_progress);
        superplayerIvFullscreen = findViewById(R.id.superplayer_iv_fullscreen);
        contentId = getIntent().getStringExtra("contentId");
        mDatas = new ArrayList<>();

        /**
         * 监听播放器播放窗口变化回调
         */
        playerView.playModeCallBack = new SuperPlayerView.PlayModeCallBack() {

            @Override
            public void getPlayMode(SuperPlayerDef.PlayerMode playerMode) {
                if (playerMode.equals(SuperPlayerDef.PlayerMode.FULLSCREEN)) {
                    introduceLin.setVisibility(View.GONE);
                } else if (playerMode.equals(SuperPlayerDef.PlayerMode.WINDOW)) {
                    introduceLin.setVisibility(View.VISIBLE);
                }
            }
        };
        if (SPUtils.isVisibleNoWifiView(this)) {
            agreeNowifiPlay.setVisibility(View.VISIBLE);
        } else {
            agreeNowifiPlay.setVisibility(View.GONE);
            getOneVideo(contentId);
        }
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
            SPUtils.getInstance().put(Constants.AGREE_NETWORK, "1");
            for (int i = 0; i < mDatas.size(); i++) {
                mDatas.get(i).setWifi(true);
            }
            agreeNowifiPlay.setVisibility(View.GONE);
            getOneVideo(contentId);
        }
    }

    private void addPlayView() {
        if (null != playerView && null != playerView.getParent()) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
        }
        publisherName.setText(mDatas.get(0).getCreatorNickname());
        if (TextUtils.equals(mDatas.get(0).getCreatorCertMark(), BLUE_V)) {
            officialCertificationImg.setVisibility(View.VISIBLE);
        } else {
            officialCertificationImg.setVisibility(View.GONE);
        }
        Glide.with(this)
                .load(mDatas.get(0).getCreatorHead())
                .into(publisherHeadimg);
        getFoldText(mDatas.get(0));

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams bottomLp = (RelativeLayout.LayoutParams) introduceLin.getLayoutParams();
        String videoType = videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(0).getWidth())),
                Integer.parseInt(NumberFormatTool.getNumStr(mDatas.get(0).getHeight())));
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
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
        } else if (videoType.equals("0")) {
            superplayerIvFullscreen.setVisibility(View.GONE);
            playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            playerView.setOrientation(false);
        } else {
            superplayerIvFullscreen.setVisibility(View.VISIBLE);
            playerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            playerView.setOrientation(true);
        }
        if (null != playerView.mWindowPlayer && null != playerView.mWindowPlayer.mLayoutBottom && null != playerView.mWindowPlayer.mLayoutBottom.getParent()) {
            ((ViewGroup) playerView.mWindowPlayer.mLayoutBottom.getParent()).removeView(playerView.mWindowPlayer.mLayoutBottom);
        }
        introduceLin.addView(playerView.mWindowPlayer.mLayoutBottom, 0);
        introduceLin.setLayoutParams(bottomLp);
        introduceLin.setVisibility(View.VISIBLE);
        RelativeLayout item = new RelativeLayout(this);
        RelativeLayout.LayoutParams itemLp = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        item.addView(playerView, itemLp);
        playerView.setTag(0);
        playerView.setBackgroundColor(getResources().getColor(R.color.black));
        rootview.addView(item, 0, lp);
        play(mDatas.get(0).getPlayUrl(), mDatas.get(0).getTitle());
    }

    private void getFoldText(DataDTO item) {
        String topicNameStr;
        if (TextUtils.isEmpty(item.getBelongTopicName()) || null == item.getBelongTopicName()) {
            topicNameStr = "";
        } else {
            topicNameStr = "#" + item.getBelongTopicName() + "  ";
        }
        String brief = topicNameStr + item.getBrief();

        final SpannableStringBuilder foldTextBuilder = new SpannableStringBuilder(brief);
        //单独设置字体大小
        AbsoluteSizeSpan foldTextSizeSpan = new AbsoluteSizeSpan(ButtonSpan.dip2px(16));
        foldTextBuilder.setSpan(foldTextSizeSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //单独设置点击事件
        ClickableSpan foldTextClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ToastUtils.showShort("点击了话题");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint paint) {
                paint.setColor(getResources().getColor(R.color.white));
                paint.setUnderlineText(false);
            }
        };
        foldTextBuilder.setSpan(foldTextClickableSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //同时设置字体颜色、点击事件
        ClickableSpan foldTextBuilderClickableSpan = new ClickableSpan() {

            @Override
            public void onClick(@NonNull View widget) {
                if (foldText.getVisibility() == View.VISIBLE) {
                    foldText.setVisibility(View.GONE);
                    expendText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint paint) {
                paint.setColor(getResources().getColor(R.color.p80_opacity_white));
                paint.setUnderlineText(false);

            }
        };
        foldTextBuilder.setSpan(foldTextBuilderClickableSpan, topicNameStr.length(), topicNameStr.length() + item.getBrief().length(), Spanned.SPAN_COMPOSING);
        //不设置不生效
        foldText.setMovementMethod(LinkMovementMethod.getInstance());
        foldText.setText(foldTextBuilder);
        //去掉点击后文字的背景色 (不去掉会有默认背景色)
        foldText.setHighlightColor(Color.parseColor("#00000000"));

        foldText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                TextView tv = (TextView) v;
                CharSequence text = tv.getText();
                if (text instanceof SpannableString) {
                    if (action == MotionEvent.ACTION_UP) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();

                        x -= tv.getTotalPaddingLeft();
                        y -= tv.getTotalPaddingTop();

                        x += tv.getScrollX();
                        y += tv.getScrollY();

                        Layout layout = tv.getLayout();
                        int line = layout.getLineForVertical(y);
                        int off = layout.getOffsetForHorizontal(line, x);

                        ClickableSpan[] link = ((SpannableString) text).getSpans(off, off, ClickableSpan.class);
                        if (link.length != 0) {
                            link[0].onClick(tv);
                        } else {
                            //do textview click event
                            return false;
                        }
                    }
                }

                return true;
            }
        });


        /**
         * 展开的
         */
        final SpannableStringBuilder expendTextBuilder = new SpannableStringBuilder(brief);
        //单独设置字体大小
        AbsoluteSizeSpan expendTextSizeSpan = new AbsoluteSizeSpan(ButtonSpan.dip2px(16));
        expendTextBuilder.setSpan(expendTextSizeSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //单独设置点击事件
        ClickableSpan expendTextClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ToastUtils.showShort("点击了话题");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint paint) {
                paint.setColor(getResources().getColor(R.color.white));
                paint.setUnderlineText(false);
            }
        };
        expendTextBuilder.setSpan(expendTextClickableSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //同时设置字体颜色、点击事件
        ClickableSpan expendTextBuilderClickableSpan = new ClickableSpan() {

            @Override
            public void onClick(@NonNull View widget) {
                if (expendText.getVisibility() == View.VISIBLE) {
                    expendText.setVisibility(View.GONE);
                    foldText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint paint) {
                paint.setColor(getResources().getColor(R.color.p80_opacity_white));
                paint.setUnderlineText(false);

            }
        };

        expendTextBuilder.setSpan(expendTextBuilderClickableSpan, topicNameStr.length(), topicNameStr.length() + item.getBrief().length(), Spanned.SPAN_COMPOSING);
        //不设置不生效
        expendText.setMovementMethod(LinkMovementMethod.getInstance());
        expendText.setText(expendTextBuilder);
        //去掉点击后文字的背景色 (不去掉回有默认背景色)
        expendText.setHighlightColor(Color.parseColor("#00000000"));


        expendText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                TextView tv = (TextView) v;
                CharSequence text = tv.getText();
                if (text instanceof SpannableString) {
                    if (action == MotionEvent.ACTION_UP) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();

                        x -= tv.getTotalPaddingLeft();
                        y -= tv.getTotalPaddingTop();

                        x += tv.getScrollX();
                        y += tv.getScrollY();

                        Layout layout = tv.getLayout();
                        int line = layout.getLineForVertical(y);
                        int off = layout.getOffsetForHorizontal(line, x);

                        ClickableSpan[] link = ((SpannableString) text).getSpans(off, off, ClickableSpan.class);
                        if (link.length != 0) {
                            link[0].onClick(tv);
                        } else {
                            //do textview click event
                            return false;
                        }
                    }
                }

                return true;
            }
        });

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
    private void getOneVideo(String contentId) {
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
                                    setDataWifiState(mDatas, VideoDetailSimpleActivity.this);
                                    addPlayView();
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
        if (playerView != null) {
            playerView.mSuperPlayer.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SuperPlayerImpl.mCurrentPlayVideoURL = null;
        if (instance != null) {
            instance.stopPlay();
            instance.release();
            instance.mSuperPlayer.destroy();
            instance = null;
        }
        OkGo.getInstance().cancelAll();
    }
}