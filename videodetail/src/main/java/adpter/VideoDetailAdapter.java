package adpter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.wdcs.constants.Constants;
import com.wdcs.manager.ViewPagerLayoutManager;
import com.wdcs.model.DataDTO;
import com.wdcs.model.RecommendModel;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.NumberFormatTool;
import com.wdcs.utils.ScreenUtils;
import com.wdcs.utils.ToastUtils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import widget.SpannableTextView;

import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.BLUE_V;

public class VideoDetailAdapter extends BaseQuickAdapter<DataDTO, BaseViewHolder> {
    private Context mContext;
    private List<DataDTO> mDatas;
    private List<RecommendModel.DataDTO.RecordsDTO> recommendList = new ArrayList<>();
    private boolean mIsAuto;
    private String brief;
    private SuperPlayerView superPlayerView;
    private ToAddPlayerViewClick click;
    private FollowViewClick followViewClick;
    private SmartRefreshLayout mRefreshlayout;
    private RelativeLayout mVideoDetailCommentBtn;
    private ViewPagerLayoutManager mVideoDetailmanager;


    public VideoDetailAdapter(int layoutResId, @Nullable List<DataDTO> data, Context context,
                            SuperPlayerView playerView, SmartRefreshLayout refreshLayout, RelativeLayout videoDetailCommentBtn, ViewPagerLayoutManager videoDetailmanager) {
        super(layoutResId, data);
        this.mContext = context;
        this.mDatas = data;
        this.superPlayerView = playerView;
        this.mRefreshlayout = refreshLayout;
        this.mVideoDetailCommentBtn = videoDetailCommentBtn;
        this.mVideoDetailmanager = videoDetailmanager;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final DataDTO item) {
        RelativeLayout itemRootView = helper.getView(R.id.item_relativelayout);
        LinearLayout introduceLin = helper.getView(R.id.introduce_lin);
        final RelativeLayout noWifiLl = helper.getView(R.id.agree_nowifi_play);
        TextView continuePlay = helper.getView(R.id.continue_play);
        LinearLayout fullLin = helper.getView(R.id.superplayer_iv_fullscreen);
        ImageView publisherHeadimg = helper.getView(R.id.publisher_headimg);
        TextView publisherName = helper.getView(R.id.publisher_name);
        ImageView officialCertificationImg = helper.getView(R.id.official_certification_img);
        TextView watched = helper.getView(R.id.watched);
        final SpannableTextView foldTextView = helper.getView(R.id.fold_text);
        final TextView expendText = helper.getView(R.id.expend_text);

        if (item.isWifi()) {
            noWifiLl.setVisibility(View.INVISIBLE);
        } else {
            noWifiLl.setVisibility(View.VISIBLE);
        }

        //无wifi时继续播放按钮
        continuePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noWifiLl.setVisibility(View.GONE);
                click.clickNoWifi(helper.getAdapterPosition());
            }
        });

        //全屏按钮
        fullLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (superPlayerView.mWindowPlayer.mControllerCallback != null) {
                    superPlayerView.mWindowPlayer.mControllerCallback.onSwitchPlayMode(SuperPlayerDef.PlayerMode.FULLSCREEN);
                }
            }
        });

        Glide.with(mContext)
                .load(item.getCreatorHead())
                .into(publisherHeadimg);
        publisherHeadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转H5头像TA人主页
                try {
                    param.recommendUrl(Constants.HEAD_OTHER + item.getCreateBy());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        publisherName.setText(item.getCreatorNickname());
        if (TextUtils.equals(item.getCreatorCertMark(), BLUE_V)) {
            officialCertificationImg.setVisibility(View.VISIBLE);
        } else {
            officialCertificationImg.setVisibility(View.GONE);
        }
        watched.setText(NumberFormatTool.formatNum(item.getViewCountShow(), false));
        String topicNameStr;
        if (TextUtils.isEmpty(item.getBelongTopicName()) || null == item.getBelongTopicName()) {
            topicNameStr = "";
        } else {
            topicNameStr = "#" + item.getBelongTopicName()+"  ";
        }
        brief = topicNameStr + item.getBrief();

        final SpannableStringBuilder foldTextBuilder = new SpannableStringBuilder(brief);
        //单独设置字体大小
        AbsoluteSizeSpan foldTextSizeSpan = new AbsoluteSizeSpan(ButtonSpan.dip2px(16));
        foldTextBuilder.setSpan(foldTextSizeSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //单独设置点击事件
        ClickableSpan foldTextClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //跳转H5话题详情
                try {
                    param.recommendUrl(Constants.TOPIC_DETAILS + item.getBelongTopicId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint paint) {
                paint.setColor(mContext.getResources().getColor(R.color.white));
                paint.setUnderlineText(false);
            }
        };
        foldTextBuilder.setSpan(foldTextClickableSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //同时设置字体颜色、点击事件
        ClickableSpan foldTextBuilderClickableSpan = new ClickableSpan() {

            @Override
            public void onClick(@NonNull View widget) {
                if (foldTextView.getVisibility() == View.VISIBLE) {
                    foldTextView.setVisibility(View.GONE);
                    expendText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint paint) {
                paint.setColor(mContext.getResources().getColor(R.color.p80_opacity_white));
                paint.setUnderlineText(false);

            }
        };
        foldTextBuilder.setSpan(foldTextBuilderClickableSpan, topicNameStr.length(), topicNameStr.length() + item.getBrief().length(), Spanned.SPAN_COMPOSING);
        //不设置不生效
        foldTextView.setMovementMethod(LinkMovementMethod.getInstance());
        foldTextView.setText(foldTextBuilder);
        //去掉点击后文字的背景色 (不去掉会有默认背景色)
        foldTextView.setHighlightColor(Color.parseColor("#00000000"));

        foldTextView.setOnTouchListener(new View.OnTouchListener() {
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
                paint.setColor(mContext.getResources().getColor(R.color.white));
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
                    foldTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint paint) {
                paint.setColor(mContext.getResources().getColor(R.color.p80_opacity_white));
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


        /**
         * 推荐服务
         */
        ViewFlipper viewFlipper = helper.getView(R.id.video_flipper);

        if (item.isRecommendVisible() && !item.isClosed()) {
            viewFlipper.setVisibility(View.VISIBLE);
        } else {
            viewFlipper.setVisibility(View.GONE);
        }

        getViewFlipperData(recommendList, viewFlipper, item);
    }

    /**
     * 获取首页滚动消息
     */
    private void getViewFlipperData(final List<RecommendModel.DataDTO.RecordsDTO> list,
                                    final ViewFlipper viewFlipper, final DataDTO mItem) {
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i).getTitle();
            View view = View.inflate(mContext, R.layout.customer_viewflipper_item, null);
            ImageView viewFlipperIcon = view.findViewById(R.id.view_flipper_icon);
            Glide.with(mContext)
                    .load(list.get(i).getThumbnailUrl())
                    .into(viewFlipperIcon);
            TextView textView = view.findViewById(R.id.view_flipper_content);
            textView.setTextColor(mContext.getResources().getColor(R.color.white));
            textView.setText(item);
            ImageView cancel = view.findViewById(R.id.view_flipper_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewFlipper.stopFlipping();
                    viewFlipper.setVisibility(View.GONE);
                    mItem.setClosed(true);
                }
            });
            viewFlipper.addView(view);
        }

        //viewFlipper的点击事件
        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取子View的id
                int mPosition = viewFlipper.getDisplayedChild();
                Log.e("yqh", "子View的id:" + mPosition);
                try {
                    param.recommendUrl(list.get(mPosition).getUrl());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        if (mIsAuto) {
            viewFlipper.startFlipping();
            viewFlipper.setAutoStart(true);
        } else {
            viewFlipper.setAutoStart(false);
        }
    }

    public void setRecommendList(List<RecommendModel.DataDTO.RecordsDTO> mRecommendList,
                                 boolean isAuto) {
        this.recommendList = mRecommendList;
        this.mIsAuto = isAuto;
    }

    public interface ToAddPlayerViewClick {
        void clickNoWifi(int position);
    }
    public void setToAddPlayerViewClick(ToAddPlayerViewClick listener){
        this.click = listener;
    }

    public interface FollowViewClick {
        void followClick(int position);
    }

    public void setFollowViewClick(FollowViewClick mFollow){
        this.followViewClick = mFollow;
    }

    /**
     * 手机是否为16：9
     *
     * @return
     */
    private boolean phoneIsNormal(Context context) {
        int phoneWidth = ScreenUtils.getPhoneWidth(context);
        int phoneHeight = ScreenUtils.getPhoneHeight(context);
        if (phoneHeight * 9 == phoneWidth * 16) {
            return true;
        } else {
            return false;
        }
    }

//    public void addPlayView(Context context, DataDTO item, LinearLayout introduceLin, LinearLayout fullLin,
//                            SmartRefreshLayout refreshLayout, RelativeLayout videoDetailCommentBtn,ViewPagerLayoutManager viewPagerLayoutManager
//                            ,int position, RelativeLayout itemRoot) {
//        if (null != superPlayerView && null != superPlayerView.getParent()) {
//            ((ViewGroup) superPlayerView.getParent()).removeView(superPlayerView);
//        }
//        if (null != superPlayerView.mWindowPlayer && null != superPlayerView.mWindowPlayer.mLayoutBottom && null != superPlayerView.mWindowPlayer.mLayoutBottom.getParent()) {
//            ((ViewGroup) superPlayerView.mWindowPlayer.mLayoutBottom.getParent()).removeView(superPlayerView.mWindowPlayer.mLayoutBottom);
//        }
//        //子布局最外层params
//        RelativeLayout.LayoutParams rootParams = (RelativeLayout.LayoutParams) itemRoot.getLayoutParams();
//        //子布局底部布局 params
//        RelativeLayout.LayoutParams bottomLp = (RelativeLayout.LayoutParams) introduceLin.getLayoutParams();
//
//        if (item.getVideoType().equals("1")) { //竖版视频16:9
//            if (phoneIsNormal(context)) { //手机也是16:9
//                fullLin.setVisibility(View.GONE); //隐藏全屏按钮
//                bottomLp.setMargins(ButtonSpan.dip2px(10), 0, ButtonSpan.dip2px(10), ButtonSpan.dip2px(90));
//                rootParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//                ((ViewGroup) refreshLayout).setLayoutParams(rootParams);
//                superPlayerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
//                superPlayerView.setOrientation(false);
//            } else {
//                fullLin.setVisibility(View.GONE);
//                bottomLp.setMargins(ButtonSpan.dip2px(10), 0, ButtonSpan.dip2px(10), ButtonSpan.dip2px(10));
//                rootParams.addRule(RelativeLayout.ABOVE, videoDetailCommentBtn.getId());
//                ((ViewGroup) refreshLayout).setLayoutParams(rootParams);
//                superPlayerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
//                superPlayerView.setOrientation(false);
//            }
//        } else if (item.getVideoType().equals("0")) {
//            fullLin.setVisibility(View.GONE);
//            bottomLp.setMargins(ButtonSpan.dip2px(10), 0, ButtonSpan.dip2px(10), ButtonSpan.dip2px(10));
//            rootParams.addRule(RelativeLayout.ABOVE, videoDetailCommentBtn.getId());
//            ((ViewGroup) refreshLayout).setLayoutParams(rootParams);
//            superPlayerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
//            superPlayerView.setOrientation(false);
//        } else {
//            fullLin.setVisibility(View.VISIBLE);
//            bottomLp.setMargins(ButtonSpan.dip2px(10), 0, ButtonSpan.dip2px(10), ButtonSpan.dip2px(90));
//            rootParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            ((ViewGroup) refreshLayout).setLayoutParams(rootParams);
//            superPlayerView.mSuperPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
//            superPlayerView.setOrientation(true);
//        }
//        introduceLin.addView(superPlayerView.mWindowPlayer.mLayoutBottom, 0);
//        introduceLin.setLayoutParams(bottomLp);
//
//        RelativeLayout.LayoutParams itemLp = new RelativeLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        RelativeLayout itemRl = new RelativeLayout(context);
//        superPlayerView.setBackgroundColor(context.getResources().getColor(R.color.black));
//        ViewGroup rlLp = (ViewGroup) viewPagerLayoutManager.findViewByPosition(position);
//        itemRl.addView(superPlayerView, itemLp);
//        if (rlLp != null) {
//            rlLp.addView(itemRl, 0, rootParams);
//            //露出即上报
//            uploadBuriedPoint(ContentBuriedPointManager.setContentBuriedPoint(context, String.valueOf(item.getId()), "", "", Constants.CMS_CLIENT_SHOW),Constants.CMS_CLIENT_SHOW);
//            play(item.getPlayUrl(), item.getTitle(), String.valueOf(item.getId()));
//        }
//    }

    /**
     * 播放视频
     *
     * @param playUrl
     */
    public void play(String playUrl, String title, String contentId) {
        if (null != superPlayerView) {
            SuperPlayerModel model = new SuperPlayerModel();
            model.url = playUrl;
            model.title = title;
            model.contentId = contentId;
            superPlayerView.playWithModel(model);
        }
    }
}
