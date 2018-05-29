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
import com.msw.mesapp.activity.fragment.warehouse.FragmentMaterialIn1;
import com.msw.mesapp.activity.fragment.warehouse.FragmentMaterialIn2;
import com.msw.mesapp.activity.fragment.warehouse.FragmentProductIn1;
import com.msw.mesapp.activity.fragment.warehouse.FragmentProductIn2;
import com.msw.mesapp.ui.widget.DecoratorViewPager;
import com.msw.mesapp.utils.StatusBarUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
//成品入库
public class ProductInActivity extends AppCompatActivity {

    @Bind(R.id.bar)
    View bar;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.viewright)
    View viewright;
    @Bind(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @Bind(R.id.viewPager)
    DecoratorViewPager viewPager;

    private final String[] mTitles = {"未入库","已入库"};
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in);
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
        title.setText("成品入库");
        add.setVisibility(View.INVISIBLE);
    }
    public void initData() {
    }
    public void initView() {
        initTitle();
        initSlidingTabLayout();
    }
    public void initSlidingTabLayout() {
        fragmentList.add(new FragmentProductIn1()); //成品待入库
        fragmentList.add(new FragmentProductIn2()); //成品已入库
        viewPager.setNestedpParent((ViewGroup)viewPager.getParent());//将 viewpager 的父view传递到viewpager里面 ,解决滑动冲突
        viewPager.setAdapter(new ProductInActivity.mPageAdapter(this.getSupportFragmentManager()));
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
