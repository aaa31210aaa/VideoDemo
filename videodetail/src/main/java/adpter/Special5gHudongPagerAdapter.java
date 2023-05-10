package adpter;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.MutableLiveData;

import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.model.Special5GTabModel;
import com.wdcs.model.VideoChannelModel;

import java.util.ArrayList;
import java.util.List;

import ui.fragment.Special5gLiveFragment;
import ui.fragment.Special5gVideoFragment;

public class Special5gHudongPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragmentList = new ArrayList<>();
    private List<Special5GTabModel.DataDTO> titleList = new ArrayList<>();

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public Special5gHudongPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position).getName();
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

    public void addItems(@Nullable List<Special5GTabModel.DataDTO> channelBeanList) {
        titleList.clear();
        fragmentList.clear();
        titleList.addAll(channelBeanList);
        for (Special5GTabModel.DataDTO dataDTO : channelBeanList) {
            addItemClickListener.addItemClick(dataDTO.getJumpUrl(),fragmentList);
        }
        notifyDataSetChanged();

    }

    public void remove(@Nullable Special5GTabModel.DataDTO bean) {
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

    public AddItemClickListener addItemClickListener;

    public interface AddItemClickListener {
        void addItemClick(String url,List<Fragment> fragmentList);
    }

    public void setAddItemClickListener(AddItemClickListener listener){
        addItemClickListener = listener;
    }
}
