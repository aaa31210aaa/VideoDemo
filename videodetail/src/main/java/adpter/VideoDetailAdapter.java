package adpter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.manager.BuriedPointModelManager;
import com.wdcs.manager.FinderBuriedPointManager;
import com.wdcs.manager.ViewPagerLayoutManager;
import com.wdcs.model.DataDTO;
import com.wdcs.model.FinderPointModel;
import com.wdcs.model.RecommendModel;
import com.wdcs.utils.AppUtils;
import com.wdcs.utils.ButtonSpan;
import com.wdcs.utils.NumberFormatTool;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.SPUtils;
import com.wdcs.utils.ScreenUtils;
import com.wdcs.utils.ToastUtils;
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import ui.activity.VideoHomeActivity;
import utils.GlideUtil;
import widget.EllipsizeTextView;
import widget.MyTextView;

import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.BLUE_V;
import static com.wdcs.constants.Constants.VIDEOTAG;
import static com.wdcs.constants.Constants.YELLOW_V;
import static com.wdcs.constants.Constants.token_error;
import static com.wdcs.utils.SPUtils.isVisibleNoWifiView;
import static ui.fragment.VideoDetailFragment.videoIsNormal;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoDetailAdapter extends BaseQuickAdapter<DataDTO, BaseViewHolder> {
    private Context mContext;
    private List<DataDTO> mDatas;
    private List<RecommendModel.DataDTO.RecordsDTO> recommendList = new ArrayList<>();
    private boolean mIsAuto;
    private String brief;
    private String spaceStr = "";
    private SuperPlayerView superPlayerView;
    private ToAddPlayerViewClick click;
    private SmartRefreshLayout mRefreshlayout;
    private ViewPagerLayoutManager mVideoDetailmanager;
    private String topicNameStr;
    private boolean isClick = true;

    public VideoDetailAdapter(int layoutResId, @Nullable List<DataDTO> data, Context context,
                              SuperPlayerView playerView, SmartRefreshLayout refreshLayout, ViewPagerLayoutManager videoDetailmanager) {
        super(layoutResId, data);
        this.mContext = context;
        this.mDatas = data;
        this.superPlayerView = playerView;
        this.mRefreshlayout = refreshLayout;
        this.mVideoDetailmanager = videoDetailmanager;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DataDTO item) {
        if (null == item) {
            return;
        }
        RelativeLayout itemRootView = helper.getView(R.id.item_relativelayout);
        LinearLayout introduceLin = helper.getView(R.id.introduce_lin);
        final RelativeLayout noWifiLl = helper.getView(R.id.agree_nowifi_play);
        TextView continuePlay = helper.getView(R.id.continue_play);
        LinearLayout fullLin = helper.getView(R.id.superplayer_iv_fullscreen);
        ImageView publisherHeadimg = helper.getView(R.id.publisher_headimg);
        TextView publisherName = helper.getView(R.id.publisher_name);
        ImageView officialCertificationImg = helper.getView(R.id.official_certification_img);
        TextView watched = helper.getView(R.id.watched);
        final EllipsizeTextView foldTextView = helper.getView(R.id.fold_text);
        final TextView expendText = helper.getView(R.id.expend_text);
        ImageView verticalVideoWdcsLogo = helper.getView(R.id.vertical_video_wdcs_logo);
        ImageView horizontalVideoWdcsLogo = helper.getView(R.id.horizontal_video_wdcs_logo);
        ImageView coverPicture = helper.getView(R.id.cover_picture);
        LinearLayout videoDetailLikes = helper.getView(R.id.video_detail_likes);
        final ImageView videoDetailLikesImage = helper.getView(R.id.video_detail_likes_image);
        final TextView likesNum = helper.getView(R.id.likes_num);
        LinearLayout videoDetailCollection = helper.getView(R.id.video_detail_collection);
        final ImageView videoDetailCollectionImage = helper.getView(R.id.video_detail_collection_image);
        final TextView collectionNum = helper.getView(R.id.collection_num);
        LinearLayout videoDetailCommentLl = helper.getView(R.id.video_detail_comment_ll);
        final TextView commentNum = helper.getView(R.id.comment_num);
        LinearLayout share = helper.getView(R.id.share);
        LinearLayout publishWorks = helper.getView(R.id.publish_works);
        final TextView follow = helper.getView(R.id.follow);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) coverPicture.getLayoutParams();
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((VideoHomeActivity) mContext).getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        double widthPixel = outMetrics.widthPixels;
        double heightPixel = outMetrics.heightPixels;
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
            verticalVideoWdcsLogo.setVisibility(View.VISIBLE);
            horizontalVideoWdcsLogo.setVisibility(View.GONE);

            if (phoneIsNormal()) {
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                layoutParams.setMargins(0, 0, 0, 0);
            } else {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
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

        //非wifi状态下布局是否显示
        if (item.isWifi()) {
            noWifiLl.setVisibility(View.INVISIBLE);
//            //全屏按钮是否显示
//            if (item.isFullBtnIsShow()) {
//                fullLin.setVisibility(View.VISIBLE);
//            } else {
//                fullLin.setVisibility(View.GONE);
//            }
        } else {
            noWifiLl.setVisibility(View.VISIBLE);
        }

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
                mFollowViewClick.followClick(helper.getAdapterPosition());
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
                    FinderPointModel model = new FinderPointModel();
                    model.setUser_id(item.getCreateBy());
                    if (TextUtils.equals("关注", follow.getText().toString())) {
                        model.setIs_notice("否");
                    } else {
                        model.setIs_notice("是");
                    }
                    model.setModule_source("视频播放");
                    FinderBuriedPointManager.setFinderCommon(Constants.CLICK_USER,model);
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

        //观看人数
        watched.setText(NumberFormatTool.formatNum(item.getViewCountShow(), false));

        if (TextUtils.isEmpty(item.getBelongTopicName()) || null == item.getBelongTopicName()) {
            topicNameStr = "";
        } else {
            topicNameStr = "#" + item.getBelongTopicName();
        }

        if (TextUtils.isEmpty(item.getBrief())) {
            brief = item.getTitle();
        } else {
            brief = item.getBrief();
        }

        foldTextView.setText(brief);
        foldTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //行为埋点 button_name 展开简介
                FinderBuriedPointManager.setFinderClick("展开简介");
                if (foldTextView.getVisibility() == View.VISIBLE) {
                    foldTextView.setVisibility(View.GONE);
                    expendText.setVisibility(View.VISIBLE);
                }
            }
        });

        expendText.setText(brief);
        expendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClick) {
                    if (expendText.getVisibility() == View.VISIBLE) {
                        expendText.setVisibility(View.GONE);
                        foldTextView.setVisibility(View.VISIBLE);
                    }
                }
                isClick = true;
            }
        });

        //点赞
        videoDetailLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLikeListener.likeClick(v, item, videoDetailLikesImage, likesNum);
            }
        });

        //收藏
        videoDetailCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollectionListener.collectionClick(v, item, videoDetailCollectionImage, collectionNum);
            }
        });

        //评论
        videoDetailCommentLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentListener.commentClick(v, item, commentNum);
            }
        });

        //转发
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareListener.shareClick(v, item);
            }
        });

        //发布
        publishWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPublishWorksListener.publishWorksClick(v, item);
            }
        });
    }


    public void setTopicClick(boolean isClick) {
        this.isClick = isClick;
    }

    public LikeListener mLikeListener;

    public interface LikeListener {
        void likeClick(View view, DataDTO item, ImageView likeImage, TextView likeNum);
    }

    public void setlikeListener(LikeListener likeListener) {
        this.mLikeListener = likeListener;
    }

    public CollectionListener mCollectionListener;

    public interface CollectionListener {
        void collectionClick(View view, DataDTO item, ImageView collectionImage, TextView collectionNum);
    }

    public void setCollectionListener(CollectionListener collectionListener) {
        this.mCollectionListener = collectionListener;
    }

    public CommentListener mCommentListener;

    public interface CommentListener {
        void commentClick(View view, DataDTO item, TextView commentNum);
    }

    public void setCommentListener(CommentListener commentListener) {
        this.mCommentListener = commentListener;
    }

    public PublishWorksListener mPublishWorksListener;

    public interface PublishWorksListener {
        void publishWorksClick(View view, DataDTO item);
    }

    public void setPublishWorksListener(PublishWorksListener publishWorksListener) {
        this.mPublishWorksListener = publishWorksListener;
    }


    public ShareListener mShareListener;

    public interface ShareListener {
        void shareClick(View view, DataDTO item);
    }

    public void setShareListener(ShareListener shareListener) {
        this.mShareListener = shareListener;
    }

    private FollowViewClick mFollowViewClick;

    public interface FollowViewClick {
        void followClick(int position);
    }

    public void setFollowViewClick(FollowViewClick mFollow) {
        this.mFollowViewClick = mFollow;
    }

    /**
     * 获取首页滚动消息
     */
    public void getViewFlipperData(final List<RecommendModel.DataDTO.RecordsDTO> list,
                                   final ViewFlipper viewFlipper, final DataDTO mItem) {
        if (null == mContext) {
            return;
        }
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
                //获取子View的id
                int mPosition = viewFlipper.getDisplayedChild();
                try {
                    param.recommendUrl(list.get(mPosition).getUrl(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FinderBuriedPointManager.setFinderClick("服务_" + list.get(mPosition).getTitle());
            }
        });
    }

    public interface ToAddPlayerViewClick {
        void clickNoWifi(int position);
    }

    public void setToAddPlayerViewClick(ToAddPlayerViewClick listener) {
        this.click = listener;
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
