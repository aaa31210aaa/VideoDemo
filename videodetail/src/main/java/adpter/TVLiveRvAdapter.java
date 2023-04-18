package adpter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wdcs.model.CategoryCompositeModel;
import com.wdcs.model.TVLiveModel;
import com.wdcs.videodetail.demo.R;

import java.util.List;

public class TVLiveRvAdapter extends BaseQuickAdapter<CategoryCompositeModel.DataDTO.ContentsDTO, BaseViewHolder> {
    private Context context;

    public TVLiveRvAdapter(Context context, int layoutResId, @Nullable List<CategoryCompositeModel.DataDTO.ContentsDTO> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryCompositeModel.DataDTO.ContentsDTO item) {
        ImageView imageView = helper.getView(R.id.tv_live_rv_item_img);
        Glide.with(context)
                .load(item.getThumbnailUrl())
                .placeholder(R.drawable.test2)
                .into(imageView);
    }
}
