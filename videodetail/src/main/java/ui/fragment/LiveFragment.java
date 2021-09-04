package ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wdcs.callback.JsonCallback;
import com.wdcs.http.ApiConstants;
import com.wdcs.model.DataDTO;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.model.VideoDetailModel;
import com.wdcs.utils.ToastUtils;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import adpter.LiveRvAdapter;

import static com.wdcs.constants.Constants.PANELID;
import static com.wdcs.constants.Constants.VIDEOTAG;
import static com.wdcs.constants.Constants.success_code;

public class LiveFragment extends Fragment {
    private RecyclerView liveRv;
    private LiveRvAdapter adapter;
    private List<DataDTO> mDatas;
    private String mPageSize = "20"; //每页视频多少条
    private Bundle args;
    private String panelId = "";
    private SmartRefreshLayout refreshLayout;
    private boolean isLoadComplate = false;
    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener;

    public LiveFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        initView(view);
        return view;
    }

    public LiveFragment newInstance(LiveFragment fragment, VideoChannelModel videoChannelModel) {
        args = new Bundle();
        args.putString(PANELID, videoChannelModel.getColumnBean().getPanelId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            panelId = getArguments().getString(PANELID);
        }
    }


    private void initView(View view) {
        liveRv = view.findViewById(R.id.live_rv);
        liveRv.setHasFixedSize(true);
        mDatas = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        liveRv.setLayoutManager(linearLayoutManager);
        adapter = new LiveRvAdapter(R.layout.live_rv_item_layout, mDatas, getActivity());
        liveRv.setAdapter(adapter);
        initSmartRefresh(view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPullDownData(mPageSize, panelId, "false");
    }

    private void initSmartRefresh(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadComplate = false;
                adapter.setOnLoadMoreListener(requestLoadMoreListener, liveRv);
                getPullDownData(mPageSize, panelId, "false");
            }
        });
        adapter.setPreLoadNumber(2);
        requestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadComplate) {
                    liveRv.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mDatas.isEmpty()) {
                                adapter.loadMoreFail();
                                return;
                            }

                            loadMoreData(mDatas.get(mDatas.size() - 1).getId() + "", mPageSize, panelId, "true");
                        }
                    });
                }
            }
        };
        adapter.setOnLoadMoreListener(requestLoadMoreListener, liveRv);
    }

    /**
     * 获取下拉列表数据
     *
     * @param panelId
     * @param removeFirst
     */
    private void getPullDownData(String pageSize, String panelId, String removeFirst) {
        mDatas.clear();
        OkGo.<VideoDetailModel>get(ApiConstants.getInstance().getVideoDetailListUrl())
                .tag(VIDEOTAG)
                .params("pageSize", pageSize)
                .params("panelId", panelId)
                .params("removeFirst", removeFirst)
                .params("type", "")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<VideoDetailModel>(VideoDetailModel.class) {
                    @Override
                    public void onSuccess(Response<VideoDetailModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (response.body().getCode().equals(success_code)) {
                            if (null == response.body().getData()) {
                                ToastUtils.showShort(R.string.data_err);
                                return;
                            }

                            mDatas.addAll(response.body().getData());
                            adapter.setNewData(mDatas);
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }
                        if (null != refreshLayout) {
                            refreshLayout.finishRefresh();
                        }
                    }

                    @Override
                    public void onError(Response<VideoDetailModel> response) {
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
                            return;
                        }
                        ToastUtils.showShort(R.string.net_err);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 获取更多数据
     */
    private void loadMoreData(String contentId, String mPageSize, String panelId, String removeFirst) {
        OkGo.<VideoDetailModel>get(ApiConstants.getInstance().getVideoDetailListUrl())
                .tag(VIDEOTAG)
                .params("contentId", contentId)
                .params("pageSize", mPageSize)
                .params("panelId", panelId)
                .params("removeFirst", removeFirst)
                .params("type", "")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new JsonCallback<VideoDetailModel>(VideoDetailModel.class) {
                    @Override
                    public void onSuccess(Response<VideoDetailModel> response) {
                        if (null == response.body()) {
                            isLoadComplate = true;
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (response.body().getCode().equals(success_code)) {
                            if (null == response.body().getData()) {
                                isLoadComplate = true;
                                ToastUtils.showShort(R.string.data_err);
                                return;
                            }

                            if (response.body().getData().size() == 0) {
                                Log.e("loadMoreData", "没有更多视频了");
                                adapter.loadMoreEnd();
//                                adapter.setOnLoadMoreListener(null, liveRv);
                                isLoadComplate = true;
                                return;
                            } else {
//                                adapter.setOnLoadMoreListener(requestLoadMoreListener, liveRv);
                                isLoadComplate = false;
                            }
                            mDatas.addAll(response.body().getData());
                            adapter.setNewData(mDatas);
                            adapter.loadMoreComplete();
                        } else {
                            adapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onError(Response<VideoDetailModel> response) {
                        if (null != response.body()) {
                            ToastUtils.showShort(response.body().getMessage());
                            return;
                        }
                        ToastUtils.showShort(R.string.net_err);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        refreshLayout.setEnableRefresh(true);
                    }
                });
    }
}