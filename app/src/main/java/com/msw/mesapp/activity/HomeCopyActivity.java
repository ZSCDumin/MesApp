package com.msw.mesapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.fragment.FragmentHome;
import com.msw.mesapp.activity.fragment.FragmentMe;
import com.msw.mesapp.activity.fragment.FragmentPlan;
import com.msw.mesapp.activity.fragment.TabEntity;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeCopyActivity extends AppCompatActivity {
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private CommonTabLayout mTabLayout;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    //tab的标题、选中图标、未选中图标
    private String[] mTitles = {"主页", "待办事项", "个人中心"};
    private int[] mIconSelectIds = {R.mipmap.tab_unselect_home, R.mipmap.tab_unselect_plan, R.mipmap.tab_unselect_me};
    private int[] mIconUnselectIds = {R.mipmap.tab_select_home, R.mipmap.tab_select_plan, R.mipmap.tab_select_me};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private GestureDetector gesture; //手势识别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtils.setActivityTranslucent(this); //设置全屏

        mTabLayout = (CommonTabLayout) findViewById(R.id.commonTabLayout);

        initData();
        initView();
    }

    private void initView() {
        String name = "";
        name = (String) SPUtil.get(HomeCopyActivity.this, GlobalKey.Login.CODE, name);
        ToastUtil.showToast(HomeCopyActivity.this, "欢迎用户：" + name, ToastUtil.Success);

        //给tab设置数据和关联的fragment
        mTabLayout.setTabData(mTabEntities, this, R.id.fl_change, mFragments);
        //设置红点
        mTabLayout.showDot(1);



    }

    private void initData() {
        ActivityManager.getAppManager().addActivity(this);

        mFragments.add(new FragmentHome());
        mFragments.add(new FragmentPlan());
        mFragments.add(new FragmentMe());

        //设置tab的标题、选中图标、未选中图标
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                //Toast.makeText(HomeActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                ToastUtil.showToast(HomeCopyActivity.this, "再按一次退出程序", ToastUtil.Info);
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}
