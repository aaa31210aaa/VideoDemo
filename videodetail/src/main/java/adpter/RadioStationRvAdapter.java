package adpter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wdcs.model.CategoryCompositeModel;
import com.wdcs.model.RadioStationModel;
import com.wdcs.videodetail.demo.R;

import java.util.List;

public class RadioStationRvAdapter extends BaseQuickAdapter<CategoryCompositeModel.DataDTO.ContentsDTO, BaseViewHolder> {
    private Context context;

    public RadioStationRvAdapter(Context context, int layoutResId, @Nullable List<CategoryCompositeModel.DataDTO.ContentsDTO> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryCompositeModel.DataDTO.ContentsDTO item) {
        ImageView imageView = helper.getView(R.id.radio_station_item_img);
        Glide.with(context)
                .load(item.getThumbnailUrl())
                .placeholder(R.drawable.test2)
                .into(imageView);
        TextView textView = helper.getView(R.id.radio_station_item_tv);
        textView.setText(item.getTitle());
    }
}
