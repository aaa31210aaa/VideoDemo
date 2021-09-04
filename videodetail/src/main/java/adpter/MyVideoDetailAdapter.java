package adpter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.model.DataDTO;
import com.wdcs.model.RecommendModel;
import com.wdcs.utils.DateUtils;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.SPUtils;
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ui.activity.VideoDetailActivity;
import widget.OverLineTextView;

import static com.wdcs.callback.VideoInteractiveParam.param;

@Keep
public class MyVideoDetailAdapter extends BaseQuickAdapter<DataDTO, BaseViewHolder> {
    private Context mContext;
    private RecyclerView videoDetailKeyWordRv;
    private VideoDetailKeyWordAdapter keyWordAdapter;
    private List<String> keyWordDatas;
    private List<DataDTO> mDatas;
    private List<RecommendModel.DataDTO.RecordsDTO> recommendList = new ArrayList<>();
    private boolean mIsAuto;
    private boolean isExpend;

    public MyVideoDetailAdapter(int layoutResId, @Nullable List<DataDTO> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
        this.mDatas = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DataDTO item) {
        TextView titleText = helper.getView(R.id.video_detail_title);
        titleText.setText(item.getTitle());
        titleText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    if (Utils.mIsDebug){
                        Log.e("tgt码：", VideoInteractiveParam.getInstance().getCode() + "-----"
                                + PersonInfoManager.getInstance().getToken());
                        System.out.println(VideoInteractiveParam.getInstance().getCode() + "-----"
                                + PersonInfoManager.getInstance().getToken());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        helper.setText(R.id.video_detail_from_media, item.getSource());
        helper.setText(R.id.video_detail_date, DateUtils.utc2Local(item.getStartTime()));
        final RelativeLayout noWifiLl = helper.getView(R.id.agree_nowifi_play);
        TextView noWifiText = helper.getView(R.id.no_wifi_text);
        TextView continuePlay = helper.getView(R.id.continue_play);
        noWifiText.setText(R.string.no_wifi);

        if (item.isWifi()) {
            noWifiLl.setVisibility(View.INVISIBLE);
        } else {
            noWifiLl.setVisibility(View.VISIBLE);
        }


        continuePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.getInstance().put(Constants.AGREE_NETWORK, "1");
                for (int i = 0; i < mDatas.size(); i++) {
                    mDatas.get(i).setWifi(true);
                }
                ((VideoDetailActivity) mContext).addPlayView(helper.getAdapterPosition());
                notifyDataSetChanged();
            }
        });


        RecyclerView videoDetailKeyWordRv = helper.getView(R.id.video_detail_keyword);
        if (null != item.getKeywords() | !TextUtils.isEmpty(item.getKeywords())) {
            keyWordDatas = Arrays.asList(item.getKeywords().split(","));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            videoDetailKeyWordRv.setLayoutManager(linearLayoutManager);
            keyWordAdapter = new VideoDetailKeyWordAdapter(R.layout.keyword_layout_item, keyWordDatas);
            keyWordAdapter.bindToRecyclerView(videoDetailKeyWordRv);
            videoDetailKeyWordRv.setAdapter(keyWordAdapter);
        }

        final OverLineTextView foldTextView = helper.getView(R.id.fold_text);
        foldTextView.setText(item.getBrief());

        /**
         * 判断获取的文本是否超过限制行数
         */
        foldTextView.setOnOverLineChangerListener(new OverLineTextView.OnOverLineChangerListener() {
            @Override
            public void onOverLine(boolean inOverLine) {
                if (inOverLine) {
                    foldTextView.setEnabled(true);
                } else {
                    foldTextView.setEnabled(false);
                }
            }
        });

        foldTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpend) {
                    foldTextView.setMaxLines(3);
                    isExpend = false;
                } else {
                    foldTextView.setMaxLines(5);
                    isExpend = true;
                }
            }
        });

        TextView videoDetailItemChooseBtn = helper.getView(R.id.video_detail_item_choose_btn);
        if (null == item.getPid() || TextUtils.isEmpty(String.valueOf(item.getPid()))) {
            videoDetailItemChooseBtn.setVisibility(View.INVISIBLE);
        } else {
            videoDetailItemChooseBtn.setVisibility(View.VISIBLE);
        }
        /**
         * 选择集数弹窗
         */
        videoDetailItemChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((VideoDetailActivity) mContext).getVideoCollection(String.valueOf(item.getPid()));
            }
        });

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
    private void getViewFlipperData(final List<RecommendModel.DataDTO.RecordsDTO> list, final ViewFlipper viewFlipper, final DataDTO mItem) {
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

    public void setRecommendList(List<RecommendModel.DataDTO.RecordsDTO> mRecommendList, boolean isAuto) {
        this.recommendList = mRecommendList;
        this.mIsAuto = isAuto;
    }
}
