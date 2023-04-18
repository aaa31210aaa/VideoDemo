package adpter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wdcs.model.DataDTO;
import com.wdcs.videodetail.demo.R;

import java.util.List;

public class LiveNetRvAdapter extends BaseQuickAdapter<DataDTO, BaseViewHolder> {
    private Context mContext;

    public LiveNetRvAdapter(Context context, int layoutResId, @Nullable List<DataDTO> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DataDTO item) {
        ImageView imageView = helper.getView(R.id.live_item_img);
        ImageView liveState = helper.getView(R.id.item_live_state);
        if (TextUtils.equals(item.getLiveStatus(), "0")) {
            liveState.setImageResource(R.drawable.live_preview);
        } else if (TextUtils.equals(item.getLiveStatus(), "1")) {
            liveState.setImageResource(R.drawable.living);
        } else {
            liveState.setImageResource(R.drawable.live_end);
        }
        TextView textView = helper.getView(R.id.live_net_tilte);
        if (null != mContext && !((Activity) mContext).isFinishing()
                && !((Activity) mContext).isDestroyed()) {
            Glide.with(mContext)
                    .load(item.getThumbnailUrl())
                    .into(imageView);
        }

        textView.setText(item.getTitle());
    }
}
