package adpter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wdcs.model.CollectionTypeModel;
import com.wdcs.model.VideoDetailCollectionModel;
import com.wdcs.videodetail.demo.R;

import java.util.List;

public class NewsVideoCollectionRvAdapter extends BaseQuickAdapter<VideoDetailCollectionModel.DataDTO.RecordsDTO, BaseViewHolder> {
    private Context context;

    public NewsVideoCollectionRvAdapter(Context context, int layoutResId, @Nullable List<VideoDetailCollectionModel.DataDTO.RecordsDTO> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, VideoDetailCollectionModel.DataDTO.RecordsDTO item) {
        RelativeLayout videoCollectionItemRl = helper.getView(R.id.video_collection_item_rl);
        TextView newsCollectionItemTv = helper.getView(R.id.video_collection_item_tv);
        newsCollectionItemTv.setText(item.getTitle());
        if (item.isCheck()) {
            newsCollectionItemTv.setTextColor(context.getResources().getColor(R.color.bz_red));
        } else {
            newsCollectionItemTv.setTextColor(context.getResources().getColor(R.color.video_black));
        }

        videoCollectionItemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.newsVideoItemClick(helper.getAdapterPosition());
            }
        });
    }

    public NewsVideoItemClickListener itemClickListener;

    public interface NewsVideoItemClickListener {
        void newsVideoItemClick(int position);
    }

    public void setNewsVideoItemClick(NewsVideoItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
