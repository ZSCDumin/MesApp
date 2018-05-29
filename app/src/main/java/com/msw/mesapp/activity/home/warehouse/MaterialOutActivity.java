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
import com.msw.mesapp.activity.fragment.warehouse.FragmentMaterialOut1;
import com.msw.mesapp.activity.fragment.warehouse.FragmentMaterialOut2;
import com.msw.mesapp.activity.fragment.warehouse.FragmentProductOut1;
import com.msw.mesapp.activity.fragment.warehouse.FragmentProductOut2;
import com.msw.mesapp.ui.widget.DecoratorViewPager;
import com.msw.mesapp.utils.StatusBarUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 出库执行
 */
public class MaterialOutActivity extends AppCompatActivity {

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

    private final String[] mTitles = {"待出库", "已出库"};
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_out);
        ButterKnife.bind(this);
        initView();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {//处理返回按钮
            @Override
            public void onClick(View view) {
                finish();//结束当前的Activity
            }
        });
        title.setText("原料出库");
        add.setVisibility(View.INVISIBLE);//隐藏此按钮
    }

    public void initView() {
        initTitle();
        initSlidingTabLayout();
    }

    public void initSlidingTabLayout() {
        fragmentList.add(new FragmentProductOut1()); //产品未出库
        fragmentList.add(new FragmentProductOut2()); //产品已出库
        viewPager.setNestedpParent((ViewGroup) viewPager.getParent());//将 viewpager 的父view传递到viewpager里面 ,解决滑动冲突
        viewPager.setAdapter(new MaterialOutActivity.mPageAdapter(this.getSupportFragmentManager()));
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
