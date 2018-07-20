package com.msw.mesapp.activity.home.quality;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.fragment.quality.FragmentTestReleaseed;
import com.msw.mesapp.activity.fragment.quality.FragmentTestReleaseing;
import com.msw.mesapp.ui.widget.DecoratorViewPager;
import com.msw.mesapp.ui.widget.TitlePopup;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.StatusBarUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestReleaseActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @Bind(R.id.viewPager)
    DecoratorViewPager viewPager;
    TitlePopup titlePopup;

    private final String[] mTitles = {"未发布", "已发布"};
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    String titile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_release);
        ButterKnife.bind(this);
        titile = getIntent().getExtras().get("1").toString();
        initData();
        initView();
    }

    public void initData() {

    }

    public void initView() {
        initTitle();
        initPopup();
        initSlidingTabLayout();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText(titile);
        add.setImageResource(R.mipmap.icon_list);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titlePopup.show(view);
            }
        });
    }

    public void initSlidingTabLayout() {
        fragmentList.add(new FragmentTestReleaseing());
        fragmentList.add(new FragmentTestReleaseed());
        viewPager.setNestedpParent((ViewGroup) viewPager.getParent());//将 viewpager 的父view传递到viewpager里面 ,解决滑动冲突
        viewPager.setAdapter(new TestReleaseActivity.mPageAdapter(this.getSupportFragmentManager()));
        slidingTabLayout.setViewPager(viewPager, mTitles);
    }

    //初始化标题列表
    public void initPopup() {
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //给标题栏弹窗添加子类
        titlePopup.addAction(new TitlePopup.ActionItem(this, "碳酸锂", R.drawable.mm_title_btn_keyboard_normal));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(TitlePopup.ActionItem item, int position) {
                switch (position) {
                    case 0:
                        ActivityUtil.switchTo(TestReleaseActivity.this, TestRelease2Activity.class);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public class mPageAdapter extends FragmentPagerAdapter {

        public mPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
