package utils;


import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.wdcs.model.ShowHomeFragmentModel;

import ui.fragment.VideoHomeFragment;

public class ShowHomeFragment {
    public static void AddHomeFragment(ShowHomeFragmentModel model) {
        if (model.getActivity() instanceof FragmentActivity) {
            FragmentTransaction transaction = ((FragmentActivity) model.getActivity()).getSupportFragmentManager().beginTransaction();
            VideoHomeFragment videoHomeFragment = VideoHomeFragment.newInstance(
                    model.getContentId(), model.getToCurrentTab(), model.getCategoryName(), model.getModuleSource()
            );
            transaction.add(model.getWidgetId(), videoHomeFragment);
            transaction.commitAllowingStateLoss();
        }
    }
}
