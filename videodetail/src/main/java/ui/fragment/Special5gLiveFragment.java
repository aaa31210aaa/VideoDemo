package ui.fragment;

import static com.wdcs.callback.VideoInteractiveParam.param;
import static com.wdcs.constants.Constants.PANELCODE;
import static com.wdcs.constants.Constants.VIDEOTAG;
import static com.wdcs.constants.Constants.success_code;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.wdcs.callback.JsonCallback;
import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.constants.Constants;
import com.wdcs.http.ApiConstants;
import com.wdcs.model.CategoryCompositeModel;
import com.wdcs.model.DataDTO;
import com.wdcs.model.GdyTokenModel;
import com.wdcs.model.RadioStationModel;
import com.wdcs.model.ShareInfo;
import com.wdcs.model.TVLiveModel;
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

import adpter.LiveNetRvAdapter;
import adpter.RadioStationRvAdapter;
import adpter.TVLiveRvAdapter;

public class Special5gLiveFragment extends Fragment implements View.OnClickListener {
    private View view;
    private String panelCode;
    private RecyclerView tvLiveRv;
    private RecyclerView radioStationRv;
    private RecyclerView liveNetRv;
    private TVLiveRvAdapter tvLiveRvAdapter;
    private RadioStationRvAdapter radioStationRvAdapter;
    private LiveNetRvAdapter liveNetRvAdapter;
    private List<CategoryCompositeModel.DataDTO.ContentsDTO> tvLiveDatas = new ArrayList<>();
    private List<CategoryCompositeModel.DataDTO.ContentsDTO> radioStationDatas = new ArrayList<>();
    private List<DataDTO> liveNetDatas = new ArrayList<>();
    private View special5gHeaderView;
    private String mPageSize = "20";
    private SmartRefreshLayout refreshLayout;
    private boolean isLoadComplate;
    public CustomPopWindow noLoginTipsPop;
    private View noLoginTipsView;
    private TextView noLoginTipsCancel;
    private TextView noLoginTipsOk;
    private List<CategoryCompositeModel.DataDTO.ContentsDTO> mDatas = new ArrayList<>();
    private String liveCode; //网络直播的面板code


    public Special5gLiveFragment() {
    }

    public static Special5gLiveFragment newInstance(Special5gLiveFragment fragment, VideoChannelModel videoChannelModel) {
        Bundle args = new Bundle();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_special5g_four, container, false);
        initView();
        return view;
    }

    private void initView() {
        special5gHeaderView = View.inflate(getActivity(), R.layout.special_5g_header, null);
        tvLiveRv = special5gHeaderView.findViewById(R.id.tv_live_rv);
        radioStationRv = special5gHeaderView.findViewById(R.id.radio_station_rv);
        liveNetRv = view.findViewById(R.id.live_net_rv);
        tvLiveRv.setHasFixedSize(true);
        radioStationRv.setHasFixedSize(true);
        liveNetRv.setHasFixedSize(true);
        GridLayoutManager tvLiveManager = new GridLayoutManager(getActivity(), 4);
        GridLayoutManager radioStationManager = new GridLayoutManager(getActivity(), 4);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tvLiveRv.setLayoutManager(tvLiveManager);
        radioStationRv.setLayoutManager(radioStationManager);
        liveNetRv.setLayoutManager(linearLayoutManager);
        tvLiveRvAdapter = new TVLiveRvAdapter(getActivity(), R.layout.tv_live_rv_item, tvLiveDatas);
        radioStationRvAdapter = new RadioStationRvAdapter(getActivity(), R.layout.radio_station_rv_item, radioStationDatas);
        liveNetRvAdapter = new LiveNetRvAdapter(getActivity(), R.layout.live_net_rv_item, liveNetDatas);

        //电视直播子项点击
        tvLiveRvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                    noLoginTipsPop();
                } else {
                    if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                        noLoginTipsPop();
                    } else {
                        ShareInfo shareInfo = ShareInfo.getInstance(tvLiveDatas.get(position).getShareUrl(), tvLiveDatas.get(position).getShareImageUrl(),
                                tvLiveDatas.get(position).getShareBrief(), tvLiveDatas.get(position).getShareTitle(), "");
                        String url;
                        if (TextUtils.isEmpty(tvLiveDatas.get(position).getExternalUrl())) {
                            url = tvLiveDatas.get(position).getDetailUrl();
                        } else {
                            url = tvLiveDatas.get(position).getExternalUrl();
                        }
                        getGdyToken(PersonInfoManager.getInstance().getTransformationToken(), url, shareInfo);
                    }
                }
            }
        });

        //电台直播子项点击
        radioStationRvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                    noLoginTipsPop();
                } else {
                    ShareInfo shareInfo = ShareInfo.getInstance(radioStationDatas.get(position).getShareUrl(), radioStationDatas.get(position).getShareImageUrl(),
                            radioStationDatas.get(position).getShareBrief(), radioStationDatas.get(position).getShareTitle(), "");
                    String url;
                    if (TextUtils.isEmpty(radioStationDatas.get(position).getExternalUrl())) {
                        url = radioStationDatas.get(position).getDetailUrl();
                    } else {
                        url = radioStationDatas.get(position).getExternalUrl();
                    }
                    getGdyToken(PersonInfoManager.getInstance().getTransformationToken(), url, shareInfo);
                }
            }
        });


        initSmartRefresh();
        liveNetRvAdapter.setPreLoadNumber(2);
        noLoginTipsView = View.inflate(getActivity(), R.layout.no_login_tips, null);
        noLoginTipsCancel = noLoginTipsView.findViewById(R.id.no_login_tips_cancel);
        noLoginTipsOk = noLoginTipsView.findViewById(R.id.no_login_tips_ok);
        noLoginTipsCancel.setOnClickListener(this);
        noLoginTipsOk.setOnClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (liveNetDatas.isEmpty()) {
                getCategoryCompositeData(panelCode);
            }
        }
    }

    private void initSmartRefresh() {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableScrollContentWhenLoaded(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isLoadComplate = false;
                getPullDownData(mPageSize, panelCode, "false", Constants.REFRESH_TYPE);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (liveNetDatas.size() > 0) {
                    loadMoreData(liveNetDatas.get(liveNetDatas.size() - 1).getId() + "", panelCode, "true", Constants.LOADMORE_TYPE);
                }
            }
        });
    }

    /**
     * 获取栏目面板及内容
     */
    public void getCategoryCompositeData(String categoryCode) {
        OkGo.<CategoryCompositeModel>get(ApiConstants.getInstance().getCategoryCompositeData())
                .params("refreshType", "open")
                .params("personalRec", "1")
                .params("categoryCode", categoryCode)
                .params("pageSize", "20")
                .execute(new JsonCallback<CategoryCompositeModel>() {
                    @Override
                    public void onSuccess(Response<CategoryCompositeModel> response) {
                        if (null == response.body()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (null == response.body().getData()) {
                            ToastUtils.showShort(R.string.data_err);
                            return;
                        }

                        if (response.body().getData().size() >= 1) {
                            for (int i = 0; i < response.body().getData().get(0).getContents().size(); i++) {
                                if (i <= 3) {
                                    tvLiveDatas.add(response.body().getData().get(0).getContents().get(i));
                                }
                            }
                            tvLiveRvAdapter.setNewData(tvLiveDatas);
                            tvLiveRv.setAdapter(tvLiveRvAdapter);
                        }

                        if (response.body().getData().size() >= 2) {
                            for (int i = 0; i < response.body().getData().get(1).getContents().size(); i++) {
                                if (i <= 3) {
                                    radioStationDatas.add(response.body().getData().get(1).getContents().get(i));
                                }
                            }

                            radioStationRvAdapter.setNewData(radioStationDatas);
                            radioStationRv.setAdapter(radioStationRvAdapter);
                        }
                        liveNetRvAdapter.addHeaderView(special5gHeaderView);
                        liveNetRv.setAdapter(liveNetRvAdapter);

                        liveNetRvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                if (TextUtils.isEmpty(PersonInfoManager.getInstance().getTransformationToken())) {
                                    noLoginTipsPop();
                                } else {
                                    ShareInfo shareInfo = ShareInfo.getInstance(liveNetDatas.get(position).getShareUrl(), liveNetDatas.get(position).getShareImageUrl(),
                                            liveNetDatas.get(position).getShareBrief(), liveNetDatas.get(position).getShareTitle(), "");
                                    String url;
                                    if (TextUtils.isEmpty(liveNetDatas.get(position).getExternalUrl())) {
                                        url = liveNetDatas.get(position).getDetailUrl();
                                    } else {
                                        url = liveNetDatas.get(position).getExternalUrl();
                                    }
                                    getGdyToken(PersonInfoManager.getInstance().getTransformationToken(), url, shareInfo);
                                }

                            }
                        });
                        if (response.body().getData().size() == 3 && null != response.body().getData().get(2).getCode()) {
                            getPullDownData(mPageSize, response.body().getData().get(2).getCode(), "false", Constants.REFRESH_TYPE);
                        }

                    }

                    @Override
                    public void onError(Response<CategoryCompositeModel> response) {
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

    /**
     * 获取下拉列表数据
     *
     * @param panelCode
     * @param removeFirst
     */
    private void getPullDownData(String pageSize, String panelCode, String removeFirst, String refreshType) {
        liveNetDatas.clear();
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

                            liveNetDatas.addAll(response.body().getData());
                            liveNetRvAdapter.setNewData(liveNetDatas);
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
    private void loadMoreData(String contentId, String panelCode, String removeFirst, String refreshType) {
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
                                refreshLayout.finishLoadMoreWithNoMoreData();
                                isLoadComplate = true;
                                return;
                            } else {
                                isLoadComplate = false;
                            }
                            liveNetDatas.addAll(response.body().getData());
                            liveNetRvAdapter.setNewData(liveNetDatas);
                        } else {
                            liveNetRvAdapter.loadMoreFail();
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
            jsonObject.put("ignoreGdy", 1);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
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

    /**
     * 换取广电云token
     * token 融媒token
     */
    private void getGdyToken(String token, final String url, final ShareInfo shareInfo) {
        JSONObject jsonObject = new JSONObject();
        OkGo.<GdyTokenModel>post(ApiConstants.getInstance().gdyToken())
                .tag("getGdyToken")
                .headers("token", token)
                .upJson(jsonObject)
                .execute(new JsonCallback<GdyTokenModel>(GdyTokenModel.class) {
                    @Override
                    public void onSuccess(Response<GdyTokenModel> response) {
                        try {
                            if (response.body().getCode() == 200) {
                                if (null == response.body().getData()) {
                                    ToastUtils.showShort(R.string.data_err);
                                    return;
                                }
                                PersonInfoManager.getInstance().setGdyToken(response.body().getData());
                                if (liveNetDatas.size() == 0) {
                                    return;
                                }

                                try {
                                    if (url.contains("?")) {
                                        param.recommendUrl(url + "&token=" + PersonInfoManager.getInstance().getGdyToken(), shareInfo);
                                    } else {
                                        param.recommendUrl(url + "?token=" + PersonInfoManager.getInstance().getGdyToken(), shareInfo);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ToastUtils.showShort(response.body().getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<GdyTokenModel> response) {
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
}