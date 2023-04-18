package com.example.x5testdemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.just.agentweb.AgentWeb;
import com.wdcs.utils.ScreenUtils;

import ui.activity.Special5GNewsDetailActivity;

public class VideoWebViewTestFragment extends BaseAgentWebFragment {
    private String mParam1;
    private TextView to_news_detail;
    private View view;
    private AgentWeb webView;
    public AgentWeb mAgentWeb;
    private RelativeLayout container;

    public VideoWebViewTestFragment() {
        // Required empty public constructor
    }

    public static VideoWebViewTestFragment newInstance(String param1) {
        VideoWebViewTestFragment fragment = new VideoWebViewTestFragment();
        Bundle args = new Bundle();
        args.putString("url", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ScreenUtils.fullScreen(getActivity(), R.color.transparent);
        view = inflater.inflate(R.layout.fragment_video_web_view_test, container, false);
        to_news_detail = view.findViewById(R.id.to_news_detail);
        to_news_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Special5GNewsDetailActivity.class);
                intent.putExtra("contentId","112676"); //112676 新闻  //319725  视频
                startActivity(intent);
            }
        });


        return view;
    }

    private void initBridge() {


    }

    @Override
    protected void setTitle(WebView view, String title) {
        super.setTitle(view, title);
//        if (!TextUtils.isEmpty(title)) {
//            if (title.length() > 10) {
//                title = title.substring(0, 10).concat("...");
//            }
//        }
//        mTitleTextView.setText(title);
    }

    @NonNull
    @Override
    protected ViewGroup getAgentWebParent() {
        return container = view.findViewById(R.id.container);
    }

    @Nullable
    @Override
    protected String getUrl() {
        return mParam1;
    }
}