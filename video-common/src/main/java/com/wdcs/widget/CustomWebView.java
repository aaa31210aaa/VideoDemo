package com.wdcs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;



public class CustomWebView extends WebView {
    private boolean isScrollX = false;

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount()== 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isScrollX = false;
                    getParent().getParent()
                            .requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    getParent().getParent()
                            .requestDisallowInterceptTouchEvent(!isScrollX);
                    break;
                default:
                    getParent().getParent()
                            .requestDisallowInterceptTouchEvent(false);
            }
        } else {
            getParent().getParent().
                    requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(event);
    }

    //当webview滚动到边界时执行
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        isScrollX = clampedX;
    }

}
