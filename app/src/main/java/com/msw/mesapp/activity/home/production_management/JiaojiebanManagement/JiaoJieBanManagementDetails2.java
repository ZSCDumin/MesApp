package com.msw.mesapp.activity.home.production_management.JiaojiebanManagement;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.fragment.production_management.FragmentJiaojieban1;
import com.msw.mesapp.activity.fragment.production_management.FragmentJiaojieban2;
import com.msw.mesapp.activity.fragment.production_management.FragmentJiaojieban3;
import com.msw.mesapp.activity.fragment.production_management.FragmentJiaojieban4;
import com.msw.mesapp.activity.fragment.production_management.FragmentJiaojieban5;
import com.msw.mesapp.activity.fragment.production_management.FragmentJiaojieban6;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.ui.widget.DecoratorViewPager;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JiaoJieBanManagementDetails2 extends AppCompatActivity {

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

    private final String[] mTitles = {"工具交接", "记录交接", "设备交接", "6S交接", "任务交接", "安全环保"};
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private String code = "";
    private String name = "";
    private String handoverHeaderCode = "";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiao_jie_ban_management_details2);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        name = getIntent().getExtras().get("name").toString();
        handoverHeaderCode = getIntent().getExtras().get("handoverHeaderCode").toString();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(JiaoJieBanManagementDetails2.this);
                builder.setTitle("确定要取消本次操作吗？").setIcon(R.mipmap.alter).setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //级联删除
                        //finish
                        EasyHttp.post(GlobalApi.ProductManagement.Jiaojieban.deleteByCode)
                            .params(GlobalApi.ProductManagement.Jiaojieban.code, handoverHeaderCode)
                            .execute(new SimpleCallBack<String>() {
                                         @Override
                                         public void onError(ApiException e) {
                                             ToastUtil.showToast(JiaoJieBanManagementDetails2.this, "删除失败", ToastUtil.Error);
                                         }

                                         @Override
                                         public void onSuccess(String s) {
                                             try {
                                                 JSONObject jsonObject = new JSONObject(s);
                                                 String message = jsonObject.optString("message");
                                                 ToastUtil.showToast(JiaoJieBanManagementDetails2.this, message, ToastUtil.Success);
                                                 handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
                                             } catch (JSONException e) {
                                                 e.printStackTrace();
                                             }
                                         }
                                     }
                            );

                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
        title.setText(name);
        add.setVisibility(View.INVISIBLE);
    }

    public void initSlidingTabLayout() {
        fragmentList.add(new FragmentJiaojieban1());//工具交接
        fragmentList.add(new FragmentJiaojieban2());//记录交接
        fragmentList.add(new FragmentJiaojieban3());//设备交接
        fragmentList.add(new FragmentJiaojieban4());//6S交接
        fragmentList.add(new FragmentJiaojieban5());//任务交接
        fragmentList.add(new FragmentJiaojieban6());//安全环保
        viewPager.setNestedpParent((ViewGroup) viewPager.getParent());//将 viewpager 的父view传递到viewpager里面 ,解决滑动冲突
        viewPager.setAdapter(new JiaoJieBanManagementDetails2.mPageAdapter(this.getSupportFragmentManager()));
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
