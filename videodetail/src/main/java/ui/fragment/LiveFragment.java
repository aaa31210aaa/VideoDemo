package ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zhouwei.library.CustomPopWindow;
import com.flyco.tablayout.SlidingTabLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.wdcs.callback.JsonCallback;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.model.DataDTO;
import com.wdcs.model.ShareInfo;
import com.wdcs.model.TokenModel;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.model.VideoDetailModel;
import com.wdcs.utils.PersonInfoManager;
import com.wdcs.utils.ToastUtils;
import com.wdcs.utils.Utils;
import com.wdcs.videodetail.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adpter.LiveRvAdapter;
import widget.CustomLoadMoreView;

import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.PANELCODE;
import static com.wdcs.constants.Constants.VIDEOTAG;
import static com.wdcs.constants.Constants.success_code;
import static utils.NetworkUtil.setDataWifiState;

public class LiveFragment extends Fragment implements View.OnClickListener {
    private RecyclerView liveRv;
    private LiveRvAdapter adapter;
    private List<DataDTO> mDatas;
    private String mPageSize = "20"; //每页视频多少条
    private Bundle args;
    private String panelCode = "";
    private SmartRefreshLayout refreshLayout;
    private boolean isLoadComplate = false;
    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener;
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    private View footview;

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
        args.putString(PANELCODE, videoChannelModel.getColumnBean().getPanelCode());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            panelCode = getArguments().getString(PANELCODE);
        }
    }


    private void initView(View view) {
        liveRv = view.findViewById(R.id.live_rv);
        liveRv.setHasFixedSize(true);
        mDatas = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        liveRv.setLayoutManager(linearLayoutManager);
        noLoginTipsView = View.inflate(getActivity(), R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);
        adapter = new LiveRvAdapter(R.layout.live_rv_item_layout, mDatas, getActivity());
        liveRv.setAdapter(adapter);
        footview = View.inflate(getActivity(),R.layout.footer_view,null);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShareInfo shareInfo = ShareInfo.getInstance(mDatas.get(position).getShareUrl(), mDatas.get(position).getShareImageUrl(),
                mDatas.get(position).getShareBrief(), mDatas.get(position).getShareTitle(), "");

                if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                    noLoginTipsPop();
                } else {
                    try {
                        String url = mDatas.get(position).getDetailUrl();
                        if (url.contains("?")) {
                            param.recommendUrl(url + "&token=" + PersonInfoManager.getInstance().getGdyToken(), shareInfo);
                        } else {
                            param.recommendUrl(url + "?token=" + PersonInfoManager.getInstance().getGdyToken(), shareInfo);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        initSmartRefresh(view);
//        adapter.setLoadMoreView(new CustomLoadMoreView());
        adapter.setPreLoadNumber(2);
        adapter.setOnLoadMoreListener(requestLoadMoreListener, liveRv);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPullDownData(mPageSize, panelCode, "false", Constants.REFRESH_TYPE);
    }

    private void initSmartRefresh(View view) {

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadComplate = false;
//                adapter.setOnLoadMoreListener(requestLoadMoreListener, liveRv);
                getPullDownData(mPageSize, panelCode, "false", Constants.REFRESH_TYPE);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMoreData(mDatas.get(mDatas.size() - 1).getId() + "", panelCode, "true", Constants.LOADMORE_TYPE);
            }
        });

//        requestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                if (!isLoadComplate) {
//                    liveRv.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mDatas.isEmpty()) {
//                                adapter.loadMoreFail();
//                                return;
//                            }
//                            loadMoreData(mDatas.get(mDatas.size() - 1).getId() + "", panelCode, "true", Constants.LOADMORE_TYPE);
//                        }
//                    });
//                }
//            }
//        };
    }

    /**
     * 获取下拉列表数据
     *
     * @param panelCode
     * @param removeFirst
     */
    private void getPullDownData(String pageSize, String panelCode, String removeFirst,String refreshType) {
        mDatas.clear();
        OkGo.<VideoDetailModel>get(ApiConstants.getInstance().getVideoDetailListUrl())
                .tag(VIDEOTAG)
                .params("pageSize", pageSize)
                .params("panelCode", panelCode)
                .params("removeFirst", removeFirst)
                .params("refreshType", refreshType)
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
    private void loadMoreData(String contentId, String panelCode, String removeFirst,String refreshType) {
        OkGo.<VideoDetailModel>get(ApiConstants.getInstance().getVideoDetailListUrl())
                .tag(VIDEOTAG)
                .params("contentId", contentId)
                .params("pageSize", 10)
                .params("panelCode", panelCode)
                .params("removeFirst", removeFirst)
                .params("refreshType", refreshType)
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
//                                adapter.loadMoreEnd();
//                                adapter.setOnLoadMoreListener(requestLoadMoreListener, liveRv);
                                refreshLayout.finishLoadMoreWithNoMoreData();
                                isLoadComplate = true;
                                return;
                            } else {
//                                adapter.setOnLoadMoreListener(requestLoadMoreListener, liveRv);
                                isLoadComplate = false;
                            }
                            mDatas.addAll(response.body().getData());
                            adapter.setNewData(mDatas);
//                            adapter.loadMoreComplete();
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
                        refreshLayout.finishLoadMore();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PersonInfoManager.getInstance().isRequestToken()) {
            try {
                getUserToken(VideoInteractiveParam.getInstance().getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用获取的code去换token
     */
    public void getUserToken(String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<TokenModel>post(ApiConstants.getInstance().mycsToken())
                .tag(VIDEOTAG)
                .upJson(jsonObject)
                .execute(new JsonCallback<TokenModel>(TokenModel.class) {
                    @Override
                    public void onSuccess(Response<TokenModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (response.body().getCode() == 200) {
                            try {
                                PersonInfoManager.getInstance().setToken(VideoInteractiveParam.getInstance().getCode());
                                PersonInfoManager.getInstance().setGdyToken(response.body().getData().getGdyToken());
                                PersonInfoManager.getInstance().setUserId(response.body().getData().getLoginSysUserVo().getId());
                                PersonInfoManager.getInstance().setTgtCode(VideoInteractiveParam.getInstance().getCode());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            PersonInfoManager.getInstance().setTransformationToken(response.body().getData().getToken());
                        } else {
                            ToastUtils.showShort(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onError(Response<TokenModel> response) {
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
     * 没有登录情况下 点击点赞收藏评论 提示登录的提示框
     */
    private void noLoginTipsPop() {
        if (null == noLoginTipsPop) {
            noLoginTipsPop = new CustomPopWindow.PopupWindowBuilder(getActivity())
                    .setView(noLoginTipsView)
                    .enableBackgroundDark(true)
                    .setOutsideTouchable(true)
                    .setFocusable(true)
                    .setAnimationStyle(R.style.AnimCenter)
                    .size(Utils.getContext().getResources().getDisplayMetrics().widthPixels, Utils.getContext().getResources().getDisplayMetrics().heightPixels)
                    .create()
                    .showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            noLoginTipsPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.no_login_tips_cancel) {
            if (null != noLoginTipsPop) {
                noLoginTipsPop.dissmiss();
            }
        } else if (id == R.id.no_login_tips_ok) {
            if (null != noLoginTipsPop) {
                noLoginTipsPop.dissmiss();
            }
            try {
                param.toLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}