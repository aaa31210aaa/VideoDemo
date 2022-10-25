package adpter;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.wdcs.model.VideoChannelModel;

import java.util.ArrayList;
import java.util.List;

import ui.fragment.LiveFragment;
import ui.fragment.TestFragment;
import ui.fragment.VideoDetailFragment;
import ui.fragment.VideoHomeFragment;
import ui.fragment.XkshFragment;

public class TestViewPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public TestViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
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

    public void addItems(@Nullable List<String> channelBeanList, String contentId, int toCurrentTab, String category_name, String module_source) {
        titleList.clear();
        fragmentList.clear();
        titleList.addAll(channelBeanList);
        for (int i = 0; i < channelBeanList.size() ; i++) {
            if (i == 2) {
                VideoHomeFragment videoHomeFragment = new VideoHomeFragment();
                fragmentList.add(videoHomeFragment.newInstance(contentId, toCurrentTab, category_name, module_source));
            } else {
                TestFragment testFragment = new TestFragment();
                fragmentList.add(testFragment);
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
