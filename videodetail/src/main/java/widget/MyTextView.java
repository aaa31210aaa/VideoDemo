package widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {
    private static String TAG = "MyTextView";

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean linkHit;//内部富文本是否被点击
    private static long lastClickTime;

    private static final long CLICK_DELAY = 500l;


    @Override
    public boolean performClick() {   //最后响应3
        if (linkHit) {
            return true;    //这样textView的OnClick事件不会响应
        }
        return super.performClick();  //调用监听的onClick方法
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {   //textView的OnClick事件响应，首先响应1
        linkHit = false;
        return super.onTouchEvent(event);
    }

    public static class CustomLinkMovementMethod extends LinkMovementMethod { //次之2

        static CustomLinkMovementMethod sInstance;
        //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
        float x1 = 0;
        float x2 = 0;
        float y1 = 0;
        float y2 = 0;

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer,
                                    MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                    }

                    return true;
                } else {
                    Selection.removeSelection(buffer);
                }
            }

            return super.onTouchEvent(widget, buffer, event);
        }

        public static CustomLinkMovementMethod getInstance() {
            if (sInstance == null) {
                sInstance = new CustomLinkMovementMethod();
            }
            return sInstance;
        }
    }


}
