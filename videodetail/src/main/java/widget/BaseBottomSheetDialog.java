package widget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.BarUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wdcs.videodetail.demo.R;

public abstract class BaseBottomSheetDialog extends BottomSheetDialogFragment {
    private static final String TAG = "base_bottom_sheet_dialog";

    private IBehaviorChanged mBehaviorChanged;


    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void bindView(View v);

    @Override
    public void onStart() {
        super.onStart();
        // 设置软键盘不自动弹出
        //设置 dialog 的宽高
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, getDialog().getWindow().getAttributes().height);
        //设置 dialog 的背景为 null
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        getDialog().getWindow().setDimAmount(0f);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mBehaviorChanged != null)
                    mBehaviorChanged.changedState(null, BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        BarUtils.setStatusBarVisibility(getDialog().getWindow(), false);
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setPeekHeight(0);
            if (mBehaviorChanged != null)
                mBehaviorChanged.changedState(null, BottomSheetBehavior.STATE_EXPANDED);
            behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        dismiss();
                    }
                    if (mBehaviorChanged != null)
                        mBehaviorChanged.changedState(bottomSheet, newState);
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    if (mBehaviorChanged != null)
                        mBehaviorChanged.changedOffset(bottomSheet, slideOffset);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(getCancelOutside());
        View v = inflater.inflate(getLayoutRes(), container, false);
        bindView(v);
        return v;
    }

    public IBehaviorChanged getBehaviorChanged() {
        return mBehaviorChanged;
    }

    public void setBehaviorChanged(IBehaviorChanged behaviorChanged) {
        mBehaviorChanged = behaviorChanged;
    }

    public interface IBehaviorChanged {
        void changedState(View bottomSheet, int state);

        void changedOffset(View bottomSheet, float slideOffset);
    }

    public boolean getCancelOutside() {
        return true;
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, getFragmentTag());
    }

}
