package com.msw.mesapp.activity.home.quality;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.fragment.quality.FragmentTestReleaseProcess1;
import com.msw.mesapp.activity.fragment.quality.FragmentTestReleaseProcess2;
import com.msw.mesapp.activity.fragment.quality.FragmentTestReleaseProcess3;
import com.msw.mesapp.activity.fragment.quality.FragmentTestReleaseProcess4;
import com.msw.mesapp.ui.widget.DecoratorViewPager;
import com.msw.mesapp.ui.widget.TitlePopup;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.StatusBarUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestReleaseProcessActivity extends AppCompatActivity {

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

    private final String[] mTitles = {"预混", "粉碎粒度","碳酸锂","扣电"};
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_release_process);
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
        title.setText("未发布");
//        Animation scaleAnimation=new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f  );
//        scaleAnimation.setDuration(2000);//设置动画持续时间为2秒
//        title.startAnimation(scaleAnimation);
        add.setImageResource(R.mipmap.icon_list);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titlePopup.show(view);
            }
        });
    }
    public void initData() {
    }
    public void initView() {
        initTitle();
        initPopup();
        initSlidingTabLayout();
    }
    public void initSlidingTabLayout() {
        fragmentList.add(new FragmentTestReleaseProcess1());
        fragmentList.add(new FragmentTestReleaseProcess2());
        fragmentList.add(new FragmentTestReleaseProcess3());
        fragmentList.add(new FragmentTestReleaseProcess4());
        viewPager.setNestedpParent((ViewGroup)viewPager.getParent());//将 viewpager 的父view传递到viewpager里面 ,解决滑动冲突
        viewPager.setAdapter(new TestReleaseProcessActivity.mPageAdapter(this.getSupportFragmentManager()));
        slidingTabLayout.setTextsize(13);
        slidingTabLayout.setTextAllCaps(true);
        slidingTabLayout.setViewPager(viewPager, mTitles);
    }
    //初始化标题列表
    public void initPopup() {
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //给标题栏弹窗添加子类
        titlePopup.addAction(new TitlePopup.ActionItem(this, "已发布", R.drawable.mm_title_btn_keyboard_normal));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(TitlePopup.ActionItem item, int position) {
                //ActivityUtil.toastShow(TestReleaseProcessActivity.this, "点击了" + item.mTitle.toString());
                switch (position){
                    case 0:
                        ActivityUtil.switchTo(TestReleaseProcessActivity.this,TestReleaseProcessedActivity.class);
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
