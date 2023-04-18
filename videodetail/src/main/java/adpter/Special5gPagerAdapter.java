package adpter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.model.VideoChannelModel;

import java.util.ArrayList;
import java.util.List;

import ui.fragment.Special5gHudongFragment;
import ui.fragment.Special5gLiveFragment;
import ui.fragment.Special5gVideoFragment;

public class Special5gPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragmentList = new ArrayList<>();
    private List<VideoChannelModel> titleList = new ArrayList<>();
    private String webUrl = "https://uat-h5.zhcs.csbtv.com/sdk/news/#/?c=1";

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public Special5gPagerAdapter(FragmentManager fragmentManager) {
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

    public void addItems(@Nullable List<VideoChannelModel> channelBeanList, SuperPlayerView playerView,
                         String contentId, String categoryName, String requestId) {
        titleList.clear();
        fragmentList.clear();
        titleList.addAll(channelBeanList);
        for (VideoChannelModel videoChannelModel : channelBeanList) {
            String id = videoChannelModel.getColumnBean().getColumnId();
            switch (id) {
                case "5G.house.hudong":
                    Special5gHudongFragment hudongFragment = new Special5gHudongFragment();
                    fragmentList.add(hudongFragment.newInstance(hudongFragment, videoChannelModel));
                    break;
                case "5G.house.yinshi":
                case "5G.house.jishi":
                case "5G.house.xinweng":
                    Bundle bundle = new Bundle();
                    bundle.putString("webUrl", webUrl);
                    Fragment fragment = VideoInteractiveParam.getInstance().getWebViewFragment(bundle);
                    if (null != fragment) {
                        fragmentList.add(fragment);
                    }
                    break;
                case "5G.house.zhibo":
                    Special5gLiveFragment fragment4 = new Special5gLiveFragment();
                    fragmentList.add(fragment4.newInstance(fragment4, videoChannelModel));
                    break;
                case "5G.house.tuijian":
                    Special5gVideoFragment fragment6 = new Special5gVideoFragment();
                    fragment6.setPlayerView(playerView);
                    fragmentList.add(fragment6.newInstance(fragment6, videoChannelModel, contentId, categoryName, requestId));
                    break;

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
