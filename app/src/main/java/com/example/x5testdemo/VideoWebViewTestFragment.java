package com.example.x5testdemo;

import static com.just.agentweb.AbsAgentWebSettings.USERAGENT_AGENTWEB;
import static com.just.agentweb.AbsAgentWebSettings.USERAGENT_UC;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.wdcs.utils.NoScrollViewPager;
import com.wdcs.widget.CustomWebView;

import ui.activity.Special5GNewsDetailActivity;

public class VideoWebViewTestFragment extends Fragment {
    private String mParam1;
    private TextView to_news_detail;
    private View view;
    public CustomWebView webView;
    public AgentWeb mAgentWeb;
    private RelativeLayout container;
    public MutableLiveData<WebView> mLiveData;
    public NoScrollViewPager noScrollViewPager;

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

    public void setLiveData(MutableLiveData<WebView> liveData) {
        this.mLiveData = liveData;
    }

    public void setNoScollViewpager(NoScrollViewPager noScrollViewPager){
        this.noScrollViewPager = noScrollViewPager;
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
//        ScreenUtils.fullScreen(getActivity(), R.color.transparent);
        view = inflater.inflate(R.layout.fragment_video_web_view_test, container, false);
        initView();
        to_news_detail = view.findViewById(R.id.to_news_detail);
        to_news_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Special5GNewsDetailActivity.class);
                intent.putExtra("contentId", "319684"); //112676 新闻  //319725  连续剧 //319684
                startActivity(intent);
            }
        });

        return view;
    }


    private void initView() {
        webView = view.findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl(mParam1);
        mLiveData.setValue(webView);
        //如果页面中使用了JavaScript，不加改代码页面不显示。
        // 设置支持javascript
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //设置js可以直接打开窗口，如window.open()，默认为false
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        //是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webView.getSettings().setSupportZoom(true);
        //是否可以缩放，默认true
        webView.getSettings().setBuiltInZoomControls(false);
        // 是否显示缩放按钮，默认false
        webView.getSettings().setUseWideViewPort(true);
        // 设置此属性，可任意比例缩放。大视图模式
        webView.getSettings().setLoadWithOverviewMode(true);
        // 和setUseWideViewPort(true)一起解决网页自适应问题
        webView.getSettings().setAppCacheEnabled(true);
        // 是否使用缓存
        webView.getSettings().setDomStorageEnabled(true);//DOM Storage
        webView.getSettings().setUserAgentString(webView.getSettings()
                .getUserAgentString()
                .concat(USERAGENT_AGENTWEB)
                .concat(USERAGENT_UC)
        );
        //访问网页
        webView.loadUrl(mParam1);
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

}