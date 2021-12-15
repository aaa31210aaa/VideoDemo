package adpter;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.wdcs.constants.Constants;
import com.wdcs.manager.ViewPagerLayoutManager;
import com.wdcs.model.DataDTO;
import com.wdcs.model.RecommendModel;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.NumberFormatTool;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.SPUtils;
import com.wdcs.utils.ScreenUtils;
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import ui.activity.VideoHomeActivity;
import utils.GlideUtil;

import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.BLUE_V;
import static com.wdcs.constants.Constants.YELLOW_V;
import static com.wdcs.utils.SPUtils.isVisibleNoWifiView;
import static ui.fragment.VideoDetailFragment.videoIsNormal;
import static ui.fragment.XkshFragment.isFollow;

@Keep
public class XkshVideoAdapter extends BaseQuickAdapter<DataDTO, BaseViewHolder> {
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
    private String spaceStr = "";
    private String topicNameStr;

    public XkshVideoAdapter(int layoutResId, @Nullable List<DataDTO> data, Context context,
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
        TextView follow = helper.getView(R.id.follow);
        ImageView officialCertificationImg = helper.getView(R.id.official_certification_img);
        TextView watched = helper.getView(R.id.watched);
        final TextView foldTextView = helper.getView(R.id.fold_text);
        final TextView expendText = helper.getView(R.id.expend_text);
        TextView huati = helper.getView(R.id.huati);
        ImageView verticalVideoWdcsLogo = helper.getView(R.id.vertical_video_wdcs_logo);
        ImageView horizontalVideoWdcsLogo = helper.getView(R.id.horizontal_video_wdcs_logo);
        ImageView coverPicture = helper.getView(R.id.cover_picture);


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) coverPicture.getLayoutParams();
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((VideoHomeActivity) mContext).getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        double widthPixel = outMetrics.widthPixels;
        if (TextUtils.equals("2", videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(item.getWidth())),
                Integer.parseInt(NumberFormatTool.getNumStr(item.getHeight()))))) {
            //横板标准视频
            verticalVideoWdcsLogo.setVisibility(View.GONE);
            horizontalVideoWdcsLogo.setVisibility(View.VISIBLE);
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            layoutParams.width = (int) widthPixel - 1;
            layoutParams.height = (int) (widthPixel / Constants.Horizontal_Proportion);
            if (null != mContext && !((VideoHomeActivity) mContext).isFinishing()
                    && !((VideoHomeActivity) mContext).isDestroyed()) {
                Glide.with(mContext)
                        .load(item.getImagesUrl())
                        .into(coverPicture);
            }
        } else if (TextUtils.equals("1", videoIsNormal(Integer.parseInt(NumberFormatTool.getNumStr(item.getWidth())),
                Integer.parseInt(NumberFormatTool.getNumStr(item.getHeight()))))) {
            //竖版视频
            if (phoneIsNormal()) {
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            } else {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            verticalVideoWdcsLogo.setVisibility(View.VISIBLE);
            horizontalVideoWdcsLogo.setVisibility(View.GONE);

            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.width = (int) widthPixel - 1;
            layoutParams.height = (int) (widthPixel / Constants.Portrait_Proportion);
            if (null != mContext && !((VideoHomeActivity) mContext).isFinishing()
                    && !((VideoHomeActivity) mContext).isDestroyed()) {
                Glide.with(mContext)
                        .load(item.getImagesUrl())
                        .into(coverPicture);
            }
        } else {
            //非标准视频
            verticalVideoWdcsLogo.setVisibility(View.VISIBLE);
            horizontalVideoWdcsLogo.setVisibility(View.GONE);
            layoutParams.width = (int) widthPixel - 1;
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            layoutParams.setMargins(0, 0, 0, 0);

            double percent = Double.parseDouble(item.getWidth()) / Double.parseDouble(item.getHeight());
            double mHeight;
            mHeight = layoutParams.width / percent;
            layoutParams.height = (int) mHeight;

            if (null != mContext && !((VideoHomeActivity) mContext).isFinishing()
                    && !((VideoHomeActivity) mContext).isDestroyed()) {
                Glide.with(mContext)
                        .load(item.getImagesUrl())
                        .into(coverPicture);
            }
        }
        coverPicture.setLayoutParams(layoutParams);

        if (item.isWifi()) {
            noWifiLl.setVisibility(View.INVISIBLE);
        } else {
            noWifiLl.setVisibility(View.VISIBLE);
        }

//        //全屏按钮是否显示
//        if (item.isFullBtnIsShow()) {
//            fullLin.setVisibility(View.VISIBLE);
//        } else {
//            fullLin.setVisibility(View.GONE);
//        }

        String localUserId = PersonInfoManager.getInstance().getUserId();
        String userId = item.getCreateBy();

        if (TextUtils.isEmpty(item.getIssuerId()) || TextUtils.equals(localUserId, userId)) {
            follow.setVisibility(View.GONE);
        } else {
            follow.setVisibility(View.VISIBLE);
        }


        //无wifi时继续播放按钮
        continuePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noWifiLl.setVisibility(View.GONE);
                click.clickNoWifi(helper.getAdapterPosition());
                if (null != superPlayerView) {
                    superPlayerView.setOrientation(true);
                }
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followViewClick.followClick(helper.getAdapterPosition());
            }
        });

        //全屏按钮
        fullLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisibleNoWifiView(mContext)) {
                    return;
                }

                if (superPlayerView.mWindowPlayer.mControllerCallback != null) {
                    superPlayerView.mWindowPlayer.mControllerCallback.onSwitchPlayMode(SuperPlayerDef.PlayerMode.FULLSCREEN);
                }
            }
        });

        if (null != mContext && !((VideoHomeActivity) mContext).isFinishing()
                && !((VideoHomeActivity) mContext).isDestroyed()) {
            if (null != mContext && !((VideoHomeActivity) mContext).isFinishing()
                    && !((VideoHomeActivity) mContext).isDestroyed()) {
                GlideUtil.displayCircle(publisherHeadimg, item.getIssuerImageUrl(), true, mContext);
//                Glide.with(mContext)
//                        .load(item.getIssuerImageUrl())
//                        .into(publisherHeadimg);
            }
            publisherName.setText(item.getIssuerName());
        }

        publisherHeadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(item.getIssuerId())) {
                    return;
                }
                //跳转H5头像TA人主页
                try {
                    if (Utils.mIsDebug) {
                        param.recommendUrl(Constants.HEAD_OTHER + item.getCreateBy(), null);
                    } else {
                        param.recommendUrl(Constants.HEAD_OTHER_ZS + item.getCreateBy(), null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (TextUtils.isEmpty(item.getCreatorCertMark())) {
            officialCertificationImg.setVisibility(View.GONE);
        } else {
            officialCertificationImg.setVisibility(View.VISIBLE);
            if (TextUtils.equals(item.getCreatorCertMark(), BLUE_V)) {
                officialCertificationImg.setImageResource(R.drawable.official_certification);
            } else if (TextUtils.equals(item.getCreatorCertMark(), YELLOW_V)) {
                officialCertificationImg.setImageResource(R.drawable.yellow_v);
            }
        }

        watched.setText(NumberFormatTool.formatNum(item.getViewCountShow(), false));

        if (TextUtils.isEmpty(item.getBelongTopicName()) || null == item.getBelongTopicName()) {
            topicNameStr = "";
        } else {
            topicNameStr = "#" + item.getBelongTopicName();
        }

        huati.setText(ButtonSpan.subStrByLen(topicNameStr, 24));

        huati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //行为埋点 话题点击
                try {
                    if (Utils.mIsDebug) {
                        param.recommendUrl(Constants.TOPIC_DETAILS + item.getBelongTopicId(), null);
                    } else {
                        param.recommendUrl(Constants.TOPIC_DETAILS_ZS + item.getBelongTopicId(), null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (TextUtils.isEmpty(item.getBrief())) {
            brief = item.getTitle();
        } else {
            brief = item.getBrief();
        }

//        if (TextUtils.isEmpty(brief)) {
//            foldTextView.setVisibility(View.GONE);
//        } else {
//            foldTextView.setVisibility(View.VISIBLE);
//        }

        if (huati.getText().length() != 0) {
            int num;
            if (huati.getText().length() > 12) {
                num = 12 + 3;
            } else {
                num = huati.getText().length() + 1;
            }

            for (int i = 0; i < num; i++) {
                spaceStr = spaceStr + "\u3000";
                item.setSpaceStr(spaceStr);
            }
            spaceStr = "";
        }

        foldTextView.setText(item.getSpaceStr() + brief);
        foldTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //行为埋点 button_name 展开简介
                if (foldTextView.getVisibility() == View.VISIBLE) {
                    foldTextView.setVisibility(View.GONE);
                    expendText.setVisibility(View.VISIBLE);
                }
            }
        });

        expendText.setText(item.getSpaceStr() + brief);
        expendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expendText.getVisibility() == View.VISIBLE) {
                    expendText.setVisibility(View.GONE);
                    foldTextView.setVisibility(View.VISIBLE);
                }
            }
        });


//        final SpannableStringBuilder foldTextBuilder = new SpannableStringBuilder(brief);
//        //单独设置字体大小
//        AbsoluteSizeSpan foldTextSizeSpan = new AbsoluteSizeSpan(ButtonSpan.dip2px(16));
//        foldTextBuilder.setSpan(foldTextSizeSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        //单独设置点击事件
//        ClickableSpan foldTextClickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                //跳转H5话题详情
//                try {
//                    param.recommendUrl(Constants.TOPIC_DETAILS + item.getBelongTopicId());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void updateDrawState(@NonNull TextPaint paint) {
//                paint.setColor(mContext.getResources().getColor(R.color.white));
//                paint.setUnderlineText(false);
//            }
//        };
//        foldTextBuilder.setSpan(foldTextClickableSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        //同时设置字体颜色、点击事件
//        ClickableSpan foldTextBuilderClickableSpan = new ClickableSpan() {
//
//            @Override
//            public void onClick(@NonNull View widget) {
//                if (foldTextView.getVisibility() == View.VISIBLE) {
//                    foldTextView.setVisibility(View.GONE);
//                    expendText.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void updateDrawState(@NonNull TextPaint paint) {
//                paint.setColor(mContext.getResources().getColor(R.color.p80_opacity_white));
//                paint.setUnderlineText(false);
//
//            }
//        };
//        foldTextBuilder.setSpan(foldTextBuilderClickableSpan, topicNameStr.length(), topicNameStr.length() + item.getBrief().length(), Spanned.SPAN_COMPOSING);
//        //不设置不生效
//        foldTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        foldTextView.setText(foldTextBuilder);
//        //去掉点击后文字的背景色 (不去掉会有默认背景色)
//        foldTextView.setHighlightColor(Color.parseColor("#00000000"));
//
//        foldTextView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//
//                TextView tv = (TextView) v;
//                CharSequence text = tv.getText();
//                if (text instanceof SpannableString) {
//                    if (action == MotionEvent.ACTION_UP) {
//                        int x = (int) event.getX();
//                        int y = (int) event.getY();
//
//                        x -= tv.getTotalPaddingLeft();
//                        y -= tv.getTotalPaddingTop();
//
//                        x += tv.getScrollX();
//                        y += tv.getScrollY();
//
//                        Layout layout = tv.getLayout();
//                        int line = layout.getLineForVertical(y);
//                        int off = layout.getOffsetForHorizontal(line, x);
//
//                        ClickableSpan[] link = ((SpannableString) text).getSpans(off, off, ClickableSpan.class);
//                        if (link.length != 0) {
//                            link[0].onClick(tv);
//                        } else {
//                            //do textview click event
//                            return false;
//                        }
//                    }
//                }
//
//                return true;
//            }
//        });
//
//
//        /**
//         * 展开的
//         */
//        final SpannableStringBuilder expendTextBuilder = new SpannableStringBuilder(brief);
//        //单独设置字体大小
//        AbsoluteSizeSpan expendTextSizeSpan = new AbsoluteSizeSpan(ButtonSpan.dip2px(16));
//        expendTextBuilder.setSpan(expendTextSizeSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        //单独设置点击事件
//        ClickableSpan expendTextClickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                ToastUtils.showShort("点击了话题");
//            }
//
//            @Override
//            public void updateDrawState(@NonNull TextPaint paint) {
//                paint.setColor(mContext.getResources().getColor(R.color.white));
//                paint.setUnderlineText(false);
//            }
//        };
//        expendTextBuilder.setSpan(expendTextClickableSpan, 0, topicNameStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        //同时设置字体颜色、点击事件
//        ClickableSpan expendTextBuilderClickableSpan = new ClickableSpan() {
//
//            @Override
//            public void onClick(@NonNull View widget) {
//                if (expendText.getVisibility() == View.VISIBLE) {
//                    expendText.setVisibility(View.GONE);
//                    foldTextView.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void updateDrawState(@NonNull TextPaint paint) {
//                paint.setColor(mContext.getResources().getColor(R.color.p80_opacity_white));
//                paint.setUnderlineText(false);
//
//            }
//        };
//
//        expendTextBuilder.setSpan(expendTextBuilderClickableSpan, topicNameStr.length(), topicNameStr.length() + item.getBrief().length(), Spanned.SPAN_COMPOSING);
//        //不设置不生效
//        expendText.setMovementMethod(LinkMovementMethod.getInstance());
//        expendText.setText(expendTextBuilder);
//        //去掉点击后文字的背景色 (不去掉回有默认背景色)
//        expendText.setHighlightColor(Color.parseColor("#00000000"));
//
//
//        expendText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//
//                TextView tv = (TextView) v;
//                CharSequence text = tv.getText();
//                if (text instanceof SpannableString) {
//                    if (action == MotionEvent.ACTION_UP) {
//                        int x = (int) event.getX();
//                        int y = (int) event.getY();
//
//                        x -= tv.getTotalPaddingLeft();
//                        y -= tv.getTotalPaddingTop();
//
//                        x += tv.getScrollX();
//                        y += tv.getScrollY();
//
//                        Layout layout = tv.getLayout();
//                        int line = layout.getLineForVertical(y);
//                        int off = layout.getOffsetForHorizontal(line, x);
//
//                        ClickableSpan[] link = ((SpannableString) text).getSpans(off, off, ClickableSpan.class);
//                        if (link.length != 0) {
//                            link[0].onClick(tv);
//                        } else {
//                            //do textview click event
//                            return false;
//                        }
//                    }
//                }
//
//                return true;
//            }
//        });


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
            if (null != mContext && !((VideoHomeActivity) mContext).isFinishing()
                    && !((VideoHomeActivity) mContext).isDestroyed()) {
                Glide.with(mContext)
                        .load(list.get(i).getThumbnailUrl())
                        .into(viewFlipperIcon);
            }

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
                //行为埋点 button_name 记录关联框服务名称
                //获取子View的id
                int mPosition = viewFlipper.getDisplayedChild();
                try {
                    param.recommendUrl(list.get(mPosition).getUrl(), null);
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

    public void setToAddPlayerViewClick(ToAddPlayerViewClick listener) {
        this.click = listener;
    }

    public interface FollowViewClick {
        void followClick(int position);
    }

    public void setFollowViewClick(FollowViewClick mFollow) {
        this.followViewClick = mFollow;
    }

    /**
     * 手机是否为16：9
     *
     * @return
     */
    private boolean phoneIsNormal() {
        int phoneWidth = ScreenUtils.getPhoneWidth(mContext);
        int phoneHeight = ScreenUtils.getPhoneHeight(mContext);
        if (phoneHeight * 9 == phoneWidth * 16) {
            return true;
        } else {
            return false;
        }
    }
}
