package adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wdcs.constants.Constants;
import com.wdcs.model.CollectionTypeModel;
import com.wdcs.model.VideoDetailCollectionModel;
import com.wdcs.videodetail.demo.R;

import java.util.List;

public class VideoCollectionRvAdapter extends BaseQuickAdapter<CollectionTypeModel.DataDTO.ChildrenDTO, BaseViewHolder> {
    private Context context;

    public VideoCollectionRvAdapter(Context context, int layoutResId, @Nullable List<CollectionTypeModel.DataDTO.ChildrenDTO> data,
                                    String type) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CollectionTypeModel.DataDTO.ChildrenDTO item) {
        RelativeLayout videoCollectionItemRl = helper.getView(R.id.video_collection_item_rl);
        TextView videoCollectionItemTv = helper.getView(R.id.video_collection_item_tv);
        videoCollectionItemTv.setText(item.getIndex() + "");
        if (item.isCheck()) {
            videoCollectionItemTv.setTextColor(context.getResources().getColor(R.color.bz_red));
        } else {
            videoCollectionItemTv.setTextColor(context.getResources().getColor(R.color.video_black));
        }

        videoCollectionItemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClickListener(helper.getAdapterPosition());
            }
        });
    }

    public ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void itemClickListener(int position);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
