package ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wdcs.callback.VideoInteractiveParam;
import com.wdcs.videodetail.demo.R;

public class Special5gOneFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RelativeLayout fragmentRoot;
    private View view;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Special5gOneFragment() {
    }

    public static Special5gOneFragment newInstance() {
        Special5gOneFragment fragment = new Special5gOneFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_special5g_one, container, false);
        initView();
        return view;
    }

    private void initView() {
        fragmentRoot = view.findViewById(R.id.fragment_root);
        //添加framgnet到根布局
//        Bundle bundle = new Bundle();
//        bundle.putString("webUrl", webUrl);
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        VideoInteractiveParam.getInstance().getWebViewFragment(bundle);
    }
}