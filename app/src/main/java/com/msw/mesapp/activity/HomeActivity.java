package com.msw.mesapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.Attention.Swing;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.popup.BubblePopup;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.home.equipment.InspectActivity;
import com.msw.mesapp.activity.home.equipment.QrManageActivity;
import com.msw.mesapp.activity.home.equipment.RepairActivity;
import com.msw.mesapp.activity.home.equipment.RepairItemActivity;
import com.msw.mesapp.activity.home.quality.TestCheckMainActivity;
import com.msw.mesapp.activity.home.quality.TestReleaseMainActivity;
import com.msw.mesapp.activity.home.warehouse.MaterialInActivity;
import com.msw.mesapp.activity.me.ModifyPasswordActivity;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    @Bind(R.id.output5)
    LinearLayout output5;
    @Bind(R.id.output6)
    LinearLayout output6;
    @Bind(R.id.output7)
    LinearLayout output7;
    @Bind(R.id.output8)
    LinearLayout output8;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtils.setActivityTranslucent(this); //设置全屏d
        StatusBarUtils.setColorForDrawerLayout(this, drawerLayout, R.color.nocolor);

        initData();
        initView();
    }

    private void initView() {
        String name = "";
        name = (String) SPUtil.get(HomeActivity.this, GlobalKey.Login.CODE, name);
        ToastUtil.showToast(HomeActivity.this, "欢迎用户：" + name, ToastUtil.Success);

        device1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "设备巡检");
                ToastUtil.showToast(HomeActivity.this, "设备巡检", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, InspectActivity.class);
            }
        });
        device2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "维修申请");
                ToastUtil.showToast(HomeActivity.this, "维修申请", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, RepairActivity.class);
            }
        });

        device3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "维修单");
                ToastUtil.showToast(HomeActivity.this, "维修单", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, RepairItemActivity.class);
            }
        });
        device4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "条码管理");
                ToastUtil.showToast(HomeActivity.this, "条码管理", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, QrManageActivity.class);
            }
        });

        quality1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "化验审核");
                ToastUtil.showToast(HomeActivity.this, "化验审核", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestCheckMainActivity.class);
            }
        });
        quality2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "化验发布");
                ToastUtil.showToast(HomeActivity.this, "化验发布", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestReleaseMainActivity.class);
            }
        });
        iokun1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "原料入库");
                ToastUtil.showToast(HomeActivity.this, "原料入库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, MaterialInActivity.class);
            }
        });
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

        navView.setItemIconTintList(null); //颜色

        View headerView = navView.getHeaderView(0);
        final SketchImageView head = (SketchImageView) headerView.findViewById(R.id.head);
        TextView id = (TextView) headerView.findViewById(R.id.tvid);

        final String jpgUrl = "http://img2.touxiang.cn/file/20171124/5c8f1c9dd6479d6d434226d1151b81b1.jpg";
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
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View inflate = View.inflate(HomeActivity.this, R.layout.popup_bubble_image, null);
                ImageView iv = ButterKnife.findById(inflate, R.id.iv_bubble);
                TextView tv = ButterKnife.findById(inflate, R.id.tv_bubble);
                //Glide.with(getContext()).load(jpgUrl).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.defualt_head).into(iv);
                tv.setText("好好工作，天天向上~");
                new BubblePopup(HomeActivity.this, inflate).anchorView(head)
                        .bubbleColor(Color.parseColor("#55C34A"))
                        .showAnim(new SlideBottomEnter())
                        .dismissAnim(new SlideBottomExit())
                        .show();
            }
        });
        String code = "";
        code = (String) SPUtil.get(HomeActivity.this, GlobalKey.Login.CODE, code);
        id.setText(code);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_change) {
                    ActivityUtil.switchTo(HomeActivity.this, ModifyPasswordActivity.class);
                } else if (id == R.id.nav_gallery) {

                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_output) {
                    BaseAnimatorSet bas_in = new Swing();
                    BaseAnimatorSet bas_out = new FadeExit();
                    final NormalDialog dialog = new NormalDialog(HomeActivity.this);
                    dialog.content("是否确定注销用户?")//
                            .showAnim(bas_in)//
                            .dismissAnim(bas_out)//
                            .show();
                    dialog.setOnBtnClickL(new OnBtnClickL() {
                                              @Override
                                              public void onBtnClick() {
                                                  dialog.dismiss();
                                              }
                                          }, new OnBtnClickL() {
                                              @Override
                                              public void onBtnClick() {
                                                  SPUtil.put(HomeActivity.this, GlobalKey.Login.CODE, "");
                                                  ActivityUtil.switchTo(HomeActivity.this, LoginActivity.class);
                                                  finish();
                                                  dialog.dismiss();
                                              }
                                          }
                    );
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    List<Fragment> fragmentList = new ArrayList<>();

    private void initCardView() {
        String ss[] = new String[3];
        ss[0] = "巡检任务";
        ss[1] = "发生故障，需要点检";
        ss[2] = "2018-09-12";

        for (int i = 0; i < 5; i++) {
            fragmentList.add(CardFragment.newInstance(ss));
            ss[0] = "巡检任务" + i;
        }
        viewPager.setAdapter(new FrPageAdapter(this.getSupportFragmentManager()));
    }


    public static class CardFragment extends Fragment {
        @Bind(R.id.tv1)
        TextView tv1;
        @Bind(R.id.tvview)
        View tvview;
        @Bind(R.id.tv2)
        TextView tv2;
        @Bind(R.id.tv3)
        TextView tv3;
        String[] ss;

        public static CardFragment newInstance(String[] ss) {
            CardFragment newFragment = new CardFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArray("ss", ss);
            newFragment.setArguments(bundle);
            return newFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //引用创建好的xml布局
            View view = inflater.inflate(R.layout.item_cardview, container, false);
            ButterKnife.bind(this, view);

            String[] sss = new String[3];

            Bundle args = getArguments();
            if (args != null) {
                sss = args.getStringArray("ss");
            }

            tv1.setText(sss[0]);
            tv2.setText(sss[1]);
            tv3.setText(sss[2]);

            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
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
                //Toast.makeText(HomeActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
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
