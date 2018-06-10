package com.msw.mesapp.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.activity.fragment.CardFragment;
import com.msw.mesapp.activity.home.equipment.InspectActivity;
import com.msw.mesapp.activity.home.equipment.QrManageActivity;
import com.msw.mesapp.activity.home.equipment.RepairApplyActivity;
import com.msw.mesapp.activity.home.equipment.RepairReportActivity;
import com.msw.mesapp.activity.home.id_management.IdManagementActivity;
import com.msw.mesapp.activity.home.production_management.CheckScalesManagement.CheckScalesManagement;
import com.msw.mesapp.activity.home.production_management.JiaojiebanManagement.JiaoJieBanManagement;
import com.msw.mesapp.activity.home.production_management.ShaiwangManagement.ShaiWangManagement;
import com.msw.mesapp.activity.home.quality.TestCheckMainActivity;
import com.msw.mesapp.activity.home.quality.TestReleaseMainActivity;
import com.msw.mesapp.activity.home.warehouse.MaterialInActivity;
import com.msw.mesapp.activity.home.warehouse.MaterialOutMainActivity;
import com.msw.mesapp.activity.home.warehouse.ProductInMainActivity;
import com.msw.mesapp.activity.home.warehouse.ProductOutMainActivity;
import com.msw.mesapp.activity.home.warehouse.SampleInActivity;
import com.msw.mesapp.activity.home.warehouse.SampleOutActivity;
import com.msw.mesapp.activity.me.ModifyPasswordActivity;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.SharedPreferenceUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.display.FadeInImageDisplayer;
import me.panpf.sketch.request.ShapeSize;
import me.panpf.sketch.shaper.CircleImageShaper;


public class HomeActivity extends AppCompatActivity {

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.device1)
    LinearLayout device1;
    @Bind(R.id.device2)
    LinearLayout device2;
    @Bind(R.id.device3)
    LinearLayout device3;
    @Bind(R.id.device4)
    LinearLayout device4;
    @Bind(R.id.quality1)
    LinearLayout quality1;
    @Bind(R.id.quality2)
    LinearLayout quality2;
    @Bind(R.id.quality3)
    LinearLayout quality3;
    @Bind(R.id.quality4)
    LinearLayout quality4;
    @Bind(R.id.iokun1)
    LinearLayout iokun1;
    @Bind(R.id.iokun2)
    LinearLayout iokun2;
    @Bind(R.id.iokun3)
    LinearLayout iokun3;
    @Bind(R.id.iokun4)
    LinearLayout iokun4;
    @Bind(R.id.panku1)
    LinearLayout panku1;
    @Bind(R.id.panku2)
    LinearLayout panku2;
    @Bind(R.id.panku3)
    LinearLayout panku3;
    @Bind(R.id.panku4)
    LinearLayout panku4;
    @Bind(R.id.output1)
    LinearLayout output1;
    @Bind(R.id.output2)
    LinearLayout output2;
    @Bind(R.id.output3)
    LinearLayout output3;
    @Bind(R.id.output4)
    LinearLayout output4;

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.idMg1)
    LinearLayout idMg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState = null;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initView();
        new Timer().schedule(new TimerTask() {
                                 @Override
                                 public void run() {
                                     fragmentList.clear();
                                     initCardView();
                                 }
                             }, 0, 5000
        );
    }

    private void initView() {
        String name = "";
        name = (String) SPUtil.get(HomeActivity.this, GlobalKey.Login.CODE, name);

        ToastUtil.showToast(HomeActivity.this, "欢迎用户：" + name, ToastUtil.Success);
        //------------------------------------------------------------------------------------------
        device1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "设备巡检", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, InspectActivity.class);
            }
        });
        device2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "维修单", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, RepairReportActivity.class);
            }
        });
        device3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "维修申请", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, RepairApplyActivity.class);
            }
        });
        device4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "条码管理", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, QrManageActivity.class);
            }
        });
        //------------------------------------------------------------------------------------------
        quality1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "化验审核", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestCheckMainActivity.class);
            }
        });
        quality2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "化验发布", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestReleaseMainActivity.class);
            }
        });
        //------------------------------------------------------------------------------------------
        iokun1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "原料入库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, MaterialInActivity.class);
            }
        });
        iokun2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "成品入库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, ProductInMainActivity.class);
            }
        });
        iokun3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "原料出库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, MaterialOutMainActivity.class);
            }
        });
        iokun4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "成品出库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, ProductOutMainActivity.class);
            }
        });
        panku1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "样品入库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, SampleInActivity.class);
            }
        });
        panku2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "样品出库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, SampleOutActivity.class);
            }
        });

        //------------------------------------------------------------------------------------------
        idMg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HomeActivity.this, "ID管理", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, IdManagementActivity.class);
            }
        });
        //------------------------------------------------------------------------------------------
        initNavView();
        initCardView();
    }

    private void initData() {
        ActivityManager.getAppManager().addActivity(this);
    }

    private void initNavView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        View headerView = navView.getHeaderView(0);
        final SketchImageView head = headerView.findViewById(R.id.head);
        TextView id = headerView.findViewById(R.id.tvid);

        final String jpgUrl = "http://img0.imgtn.bdimg.com/it/u=1931194776,941976534&fm=200&gp=0.jpg";
        head.getOptions()
                .setCacheInDiskDisabled(true)
                .setShaper(new CircleImageShaper())
                .setDecodeGifImage(true) //显示gif
                .setLoadingImage(R.mipmap.ic_autorenew) //加载时的图片
                .setErrorImage(R.mipmap.defualt_head)  //错误时的图片
                .setShapeSize(ShapeSize.byViewFixedSize()) //设置尺寸
                .setDisplayer(new FadeInImageDisplayer()); //显示图片的动画
        head.displayImage(jpgUrl);
        head.setClickRetryOnDisplayErrorEnabled(true);//加载失败时点击重新加载

        String code = "";
        code = (String) SPUtil.get(HomeActivity.this, GlobalKey.Login.CODE, code);
        id.setText(code);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_change) {
                    ActivityUtil.switchTo(HomeActivity.this, ModifyPasswordActivity.class);
                } else if (id == R.id.nav_output) {

                    AlertDialog.Builder build = new AlertDialog.Builder(HomeActivity.this);
                    build.setIcon(R.mipmap.alter);
                    build.setTitle("警告对话框");
                    build.setMessage("您确定要注销此用户吗？");
                    build.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SPUtil.clear(HomeActivity.this);
                            SharedPreferenceUtils.clearPreferences(HomeActivity.this);
                            ActivityUtil.switchTo(HomeActivity.this, LoginActivity.class);
                            finish();
                        }
                    });
                    build.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtil.showToast(HomeActivity.this, "您已取消了该操作！", ToastUtil.Default);
                        }
                    });
                    build.show();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    List<CardFragment> fragmentList = new ArrayList<>();

    private void initCardView() {
        String userCode = (String) SPUtil.get(this, GlobalKey.Login.CODE, "");
        EasyHttp.post(GlobalApi.UndoThingsItems.PATH)
                .params(GlobalApi.UndoThingsItems.STATUS, "0")
                .params(GlobalApi.UndoThingsItems.ADDRESSEECODE, userCode)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray contents = jsonObject.optJSONObject("data").optJSONArray("content");
                            for (int i = 0; i < contents.length(); i++) {
                                JSONObject item = contents.optJSONObject(i);
                                String code = item.optString("code");
                                String title = item.optString("title");
                                String content = item.optString("content"); //获取申请内容
                                String date = item.optString("createTime");
                                String url = item.optString("url");
                                String status = item.optString("status");
                                String ss[] = new String[6];
                                ss[0] = title;
                                ss[1] = content;
                                ss[2] = date;
                                ss[3] = url;
                                ss[4] = status;
                                ss[5] = code;
                                fragmentList.add(CardFragment.newInstance(ss));
                            }
                            viewPager.setAdapter(new FrPageAdapter(HomeActivity.this.getSupportFragmentManager()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(HomeActivity.this, "获取待办事项出错!", ToastUtil.Error);
                    }
                });
    }

    @OnClick({R.id.output1, R.id.output2, R.id.output3, R.id.output4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.output1:
                ToastUtil.showToast(HomeActivity.this, "核秤管理", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, CheckScalesManagement.class);
                break;
            case R.id.output2:
                ToastUtil.showToast(HomeActivity.this, "筛网检查", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, ShaiWangManagement.class);
                break;
            case R.id.output3:
                ToastUtil.showToast(HomeActivity.this, "交接班", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, JiaoJieBanManagement.class);
                break;
            case R.id.output4:
                //暂时为空
                break;
        }
    }


    public class FrPageAdapter extends FragmentPagerAdapter {

        public FrPageAdapter(FragmentManager fm) {
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


    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                ToastUtil.showToast(HomeActivity.this, "再按一次退出程序", ToastUtil.Info);
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
