package com.msw.mesapp.activity.home.equipment;

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
import com.msw.mesapp.activity.fragment.equipment.FragmentRepairScoreed;
import com.msw.mesapp.activity.fragment.equipment.FragmentRepairScoreing;
import com.msw.mesapp.activity.fragment.equipment.FragmentRepairWorked;
import com.msw.mesapp.activity.fragment.equipment.FragmentRepairWorking;
import com.msw.mesapp.ui.widget.DecoratorViewPager;
import com.msw.mesapp.utils.StatusBarUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepairBillActivity extends AppCompatActivity {
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

    private final String[] mTitles = {"待维修", "已维修", "已接单", "已评价"};

    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_bill);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        initTitle();
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
        title.setText("维修单据");
        add.setVisibility(View.INVISIBLE);
    }

    public void initSlidingTabLayout() {
        fragmentList.add(new FragmentRepairWorking());//未维修
        fragmentList.add(new FragmentRepairWorked());//已维修
        fragmentList.add(new FragmentRepairScoreing());//已接单
        fragmentList.add(new FragmentRepairScoreed());//已评价
        viewPager.setNestedpParent((ViewGroup) viewPager.getParent());//将 viewpager 的父view传递到viewpager里面 ,解决滑动冲突
        viewPager.setAdapter(new RepairBillActivity.mPageAdapter(this.getSupportFragmentManager()));
        slidingTabLayout.setViewPager(viewPager, mTitles);
        viewPager.setClickable(false);
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
