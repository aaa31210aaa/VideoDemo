package adpter;

import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.flyco.tablayout.SlidingTabLayout;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.liteav.demo.superplayer.model.SuperPlayer;
import com.wdcs.model.VideoChannelModel;

import java.util.ArrayList;
import java.util.List;

import ui.fragment.LiveFragment;
import ui.fragment.VideoDetailFragment;
import ui.fragment.XkshFragment;

public class VideoViewPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragmentList = new ArrayList<>();
    private List<VideoChannelModel> titleList = new ArrayList<>();

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public VideoViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position).getColumnBean().getColumnName();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public void clearItems() {
        titleList.clear();
        fragmentList.clear();
        notifyDataSetChanged();
    }

    public void addItems(@Nullable List<VideoChannelModel> channelBeanList, SlidingTabLayout videoTab,
                         SuperPlayerView playerView, String contentId, String categoryName) {
        titleList.clear();
        fragmentList.clear();
        titleList.addAll(channelBeanList);
        for (VideoChannelModel videoChannelModel : channelBeanList) {
            if (TextUtils.equals("2", videoChannelModel.getColumnBean().getColumnId())) {
                LiveFragment fragment = new LiveFragment();
                fragmentList.add(fragment.newInstance(fragment, videoChannelModel));
            } else if (TextUtils.equals("1", videoChannelModel.getColumnBean().getColumnId())) {
                VideoDetailFragment fragment = new VideoDetailFragment(videoTab, playerView, contentId,categoryName);
                fragmentList.add(fragment.newInstance(fragment, videoChannelModel));
            } else {
                XkshFragment fragment = new XkshFragment(videoTab, playerView,categoryName);
                fragmentList.add(fragment.newInstance(fragment, videoChannelModel));
            }
        }
        notifyDataSetChanged();
    }


    public void remove(@Nullable VideoChannelModel bean) {
        int pos = titleList.indexOf(bean);
        titleList.remove(pos);
        fragmentList.remove(pos);
        notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {
        return titleList.get(position).hashCode();
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
