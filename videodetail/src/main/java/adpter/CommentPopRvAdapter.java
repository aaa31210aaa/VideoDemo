package adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wdcs.videodetail.demo.R;

import java.util.List;

import model.CommentModel.DataDTO.RecordsDTO;
import utils.DateUtils;
import utils.GetTimeAgo;
import widget.CircleImageView;
@Keep
public class CommentPopRvAdapter extends BaseQuickAdapter<RecordsDTO, BaseViewHolder> {
    private Context mContext;

    public CommentPopRvAdapter(int layoutResId, @Nullable List<RecordsDTO> data, Context context) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordsDTO item) {
        CircleImageView commentUserHead = helper.getView(R.id.comment_user_head);
        LinearLayout gmReback = helper.getView(R.id.gm_reback);
        if (null != item.getChildren() && item.getChildren().size() > 0) {
            gmReback.setVisibility(View.VISIBLE);
            if (null == item.getCreateTime() || TextUtils.isEmpty(item.getCreateTime())) {
                helper.setText(R.id.comment_date, "");
            } else {
                helper.setText(R.id.gm_reback_time, GetTimeAgo.getTimeAgo(Long.parseLong(DateUtils.date2TimeStamp(DateUtils.utc2Local(item.getChildren().get(0).getCreateTime()), "yyyy-MM-dd HH:mm:ss"))));
            }

            helper.setText(R.id.gm_reback_text, item.getChildren().get(0).getContent());
        } else {
            gmReback.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load(item.getHead())
                .into(commentUserHead);
        helper.setText(R.id.comment_pop_username, item.getNickname());
        if (null == item.getCreateTime() || TextUtils.isEmpty(item.getCreateTime())) {
            helper.setText(R.id.comment_date, "");
        } else {
            helper.setText(R.id.comment_date, GetTimeAgo.getTimeAgo(Long.parseLong(DateUtils.date2TimeStamp(DateUtils.utc2Local(item.getCreateTime()), "yyyy-MM-dd HH:mm:ss"))));
        }

        helper.setText(R.id.comment_content, item.getContent());
    }
}
