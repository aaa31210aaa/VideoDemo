package adpter;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wdcs.videodetail.demo.R;

import java.util.List;
@Keep
public class VideoDetailKeyWordAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public VideoDetailKeyWordAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.keyword_tv, item);
    }
}
