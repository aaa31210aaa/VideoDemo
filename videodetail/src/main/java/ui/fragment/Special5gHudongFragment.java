package ui.fragment;

import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.PANELCODE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.wdcs.callback.JsonCallback;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.model.ColumnModel;
import com.wdcs.model.Special5GTabModel;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.utils.NoScrollViewPager;
import com.wdcs.utils.ToastUtils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import adpter.Special5gHudongPagerAdapter;

public class Special5gHudongFragment extends Fragment {
    private View view;
    private List<String> colunmHudongList = new ArrayList<>();
    private SlidingTabLayout special5gHudongtab;
    private NoScrollViewPager special5gHudongVp;
    public Special5gHudongPagerAdapter special5gHudongPagerAdapter;
    private String columnId;
    private List<Special5GTabModel.DataDTO> tabLists = new ArrayList<>();


    public Special5gHudongFragment() {
        // Required empty public constructor
    }

    public static Special5gHudongFragment newInstance(Special5gHudongFragment fragment, VideoChannelModel videoChannelModel) {
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putString("columnId",videoChannelModel.getColumnBean().getColumnId());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            columnId = getArguments().getString("columnId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_special5g_hudong, container, false);
        initView();
        return view;
    }

    private void initView() {
        special5gHudongtab = view.findViewById(R.id.special_5g_hudongtab);
        special5gHudongVp = view.findViewById(R.id.special_5g_hudong_vp);

        initHudongViewPager();
    }

    private void initHudongViewPager() {
        special5gHudongVp.setOffscreenPageLimit(3);
        if (null == special5gHudongPagerAdapter) {
            special5gHudongPagerAdapter = new Special5gHudongPagerAdapter(getActivity().getSupportFragmentManager());
        }
        special5gHudongVp.setAdapter(special5gHudongPagerAdapter);
        getHudongTabData();
    }

    private void getHudongTabData() {
        String deviceId = "";
        try {
            deviceId = param.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkGo.<Special5GTabModel>get(ApiConstants.getInstance().getCategoryData())
                .params("categoryCode", columnId)
                .params("ssid", deviceId)
                .execute(new JsonCallback<Special5GTabModel>() {
                    @Override
                    public void onSuccess(Response<Special5GTabModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (null == response.body().getData()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }
                        if (TextUtils.equals(Constants.success_code, response.body().getCode())) {
                            tabLists.addAll(response.body().getData());
                            initViewPagerData(tabLists);
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onError(Response<Special5GTabModel> response) {
                        super.onError(response);
                        if (null == response.body()) {
                            return;
                        }
                        if (null == response.body().getMessage()) {
                            return;
                        }

                        ToastUtils.showShort(response.body().getMessage());
                    }
                });
    }

    private void initViewPagerData(List<Special5GTabModel.DataDTO> models) {
        special5gHudongPagerAdapter.addItems(models);
        for (Special5GTabModel.DataDTO channelBean : models) {
            colunmHudongList.add(channelBean.getName());
        }
        String[] titles = colunmHudongList.toArray(new String[colunmHudongList.size()]);
        //tab和ViewPager进行关联
        special5gHudongtab.setViewPager(special5gHudongVp, titles);
        special5gHudongtab.setCurrentTab(0);
    }
}