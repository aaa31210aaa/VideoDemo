package ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.wdcs.manager.FinderBuriedPointManager;
import com.wdcs.model.VideoChannelModel;
import com.wdcs.utils.NoScrollViewPager;
import com.wdcs.videodetail.demo.R;

import java.util.ArrayList;
import java.util.List;

import adpter.TestViewPagerAdapter;
import adpter.VideoViewPagerAdapter;
import ui.fragment.VideoHomeFragment;

public class VideoHomeTestActivity extends AppCompatActivity {
    private RelativeLayout videoHomeLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private VideoHomeFragment videoHomeFragment;
    private String contentId;
    private int toCurrentTab;
    private String category_name;
    private String module_source;
    private SlidingTabLayout testTab;
    private NoScrollViewPager testVp;
    private TestViewPagerAdapter testViewPagerAdapter;
    private List<String> tabModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_home_test);
        videoHomeLayout = findViewById(R.id.video_home_layout);
        contentId = getIntent().getStringExtra("contentId");
        toCurrentTab = getIntent().getIntExtra("toCurrentTab", 1);
        category_name = getIntent().getStringExtra("category_name");
        module_source = getIntent().getStringExtra("module_source");
        initTab();
    }

    private void initTab() {
        testTab = findViewById(R.id.test_tab);
        testVp = findViewById(R.id.test_vp);
        testVp.setOffscreenPageLimit(3);
        if (null == testViewPagerAdapter) {
            testViewPagerAdapter = new TestViewPagerAdapter(getSupportFragmentManager());
        }
        testVp.setAdapter(testViewPagerAdapter);
        for (int i = 0; i < 4; i++) {
            tabModels.add("测试"+i);
        }
        testViewPagerAdapter.addItems(tabModels, contentId, toCurrentTab, category_name, module_source);
        String[] titles = tabModels.toArray(new String[tabModels.size()]);
        testTab.setViewPager(testVp, titles);
    }

}