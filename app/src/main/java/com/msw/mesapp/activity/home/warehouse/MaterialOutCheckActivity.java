package com.msw.mesapp.activity.home.warehouse;

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
import com.msw.mesapp.activity.fragment.warehouse.FragmentMaterialOutCheck1;
import com.msw.mesapp.activity.fragment.warehouse.FragmentMaterialOutCheck2;
import com.msw.mesapp.ui.widget.DecoratorViewPager;
import com.msw.mesapp.utils.StatusBarUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 出库审核
 */
public class MaterialOutCheckActivity extends AppCompatActivity {

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

    private final String[] mTitles = {"未审核","已审核"};
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_out);
        ButterKnife.bind(this);
        initData();
        initView();
    }
    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("原料出库");
        add.setVisibility(View.INVISIBLE);
    }
    public void initData() {
    }
    public void initView() {
        initTitle();
        initSlidingTabLayout();
    }
    public void initSlidingTabLayout() {
        fragmentList.add(new FragmentMaterialOutCheck1()); //原料未审核
        fragmentList.add(new FragmentMaterialOutCheck2()); //原料已审核
        viewPager.setNestedpParent((ViewGroup)viewPager.getParent());//将 viewpager 的父view传递到viewpager里面 ,解决滑动冲突
        viewPager.setAdapter(new MaterialOutCheckActivity.mPageAdapter(this.getSupportFragmentManager()));
        slidingTabLayout.setTextsize(14);
        slidingTabLayout.setTextAllCaps(true);
        slidingTabLayout.setViewPager(viewPager, mTitles);
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
