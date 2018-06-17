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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.activity.fragment.CardFragment;
import com.msw.mesapp.activity.home.equipment.InspectMonitorActivity;
import com.msw.mesapp.activity.home.equipment.InspectWorkerActivity;
import com.msw.mesapp.activity.home.equipment.QrManageActivity;
import com.msw.mesapp.activity.home.equipment.RepairApplyActivity;
import com.msw.mesapp.activity.home.equipment.RepairBillActivity;
import com.msw.mesapp.activity.home.equipment.RepairScoreActivity;
import com.msw.mesapp.activity.home.equipment.RepairWorkActivity;
import com.msw.mesapp.activity.home.id_management.IdManagementActivity;
import com.msw.mesapp.activity.home.production_management.CheckScalesManagement.CheckScalesCheckActivity;
import com.msw.mesapp.activity.home.production_management.CheckScalesManagement.CheckScalesMemberActivity;
import com.msw.mesapp.activity.home.production_management.JiaojiebanManagement.JiaoJieBanManagement;
import com.msw.mesapp.activity.home.production_management.ShaiwangManagement.ShaiWangManagement;
import com.msw.mesapp.activity.home.quality.TestCheckActivity;
import com.msw.mesapp.activity.home.quality.TestCheckProcessActivity;
import com.msw.mesapp.activity.home.quality.TestCheckProductActivity;
import com.msw.mesapp.activity.home.quality.TestReleaseActivity;
import com.msw.mesapp.activity.home.quality.TestReleaseProcessActivity;
import com.msw.mesapp.activity.home.warehouse.MaterialInActivity;
import com.msw.mesapp.activity.home.warehouse.MaterialOutActivity;
import com.msw.mesapp.activity.home.warehouse.MaterialOutCheckActivity;
import com.msw.mesapp.activity.home.warehouse.ProductInActivity;
import com.msw.mesapp.activity.home.warehouse.ProductInAddActivityDetail1Scan;
import com.msw.mesapp.activity.home.warehouse.ProductOutActivity;
import com.msw.mesapp.activity.home.warehouse.ProductOutCheckActivity;
import com.msw.mesapp.activity.home.warehouse.SampleInActivity;
import com.msw.mesapp.activity.home.warehouse.SampleOutActivity;
import com.msw.mesapp.activity.me.ModifyPasswordActivity;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.ActivityUtil;
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


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.device_inspection_ll)
    LinearLayout deviceInspectionLl;
    @Bind(R.id.repair_bill_ll)
    LinearLayout repairBillLl;
    @Bind(R.id.repair_apply_ll)
    LinearLayout repairApplyLl;
    @Bind(R.id.qr_management_ll)
    LinearLayout qrManagementLl;
    @Bind(R.id.device_verify_ll)
    LinearLayout deviceVerifyLl;
    @Bind(R.id.receive_repair_order_ll)
    LinearLayout receiveRepairOrderLl;
    @Bind(R.id.repair_comment_ll)
    LinearLayout repairCommentLl;
    @Bind(R.id.product_vertify_ll)
    LinearLayout productVertifyLl;
    @Bind(R.id.material_vertify_ll)
    LinearLayout materialVertifyLl;
    @Bind(R.id.produce_vertify_ll)
    LinearLayout produceVertifyLl;
    @Bind(R.id.product_release_ll)
    LinearLayout productReleaseLl;
    @Bind(R.id.material_release_ll)
    LinearLayout materialReleaseLl;
    @Bind(R.id.produce_release_ll)
    LinearLayout produceReleaseLl;
    @Bind(R.id.material_input_ll)
    LinearLayout materialInputLl;
    @Bind(R.id.material_output_ll)
    LinearLayout materialOutputLl;
    @Bind(R.id.product_input_ll)
    LinearLayout productInputLl;
    @Bind(R.id.product_output_vertify_ll)
    LinearLayout productOutputVertifyLl;
    @Bind(R.id.sample_input_ll)
    LinearLayout sampleInputLl;
    @Bind(R.id.sample_output_ll)
    LinearLayout sampleOutputLl;
    @Bind(R.id.add_vertify_ll)
    LinearLayout addVertifyLl;
    @Bind(R.id.material_output_vertify_ll)
    LinearLayout materialOutputVertifyLl;
    @Bind(R.id.product_output_ll)
    LinearLayout productOutputLl;
    @Bind(R.id.product_check_scale_ll)
    LinearLayout productCheckScaleLl;
    @Bind(R.id.shaiwang_check_ll)
    LinearLayout shaiwangCheckLl;
    @Bind(R.id.jiaojieban_ll)
    LinearLayout jiaojiebanLl;
    @Bind(R.id.id_bind_ll)
    LinearLayout idBindLl;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.check_scale_vertify_ll)
    LinearLayout checkScaleVertifyLl;


    List<CardFragment> fragmentList = new ArrayList<>();
    private String permission_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        permission_code = SharedPreferenceUtils.getString(this, GlobalKey.Permission.SPKEY, "");//获取当前用户的权限码
        Log.i("permission_code", permission_code);
        ActivityManager.getAppManager().addActivity(this); //添加当前Activity到Activity列表中
        initView();
        //定时获取待办事项（5s一次)
        new Timer().schedule(new TimerTask() {
                                 @Override
                                 public void run() {
                                     fragmentList.clear();
                                     initCardView();
                                 }
                             }, 0, 5000
        );
    }

    public void checkPermission() {

        /**
         * 设备管理模块权限控制
         */
        {
            if (!permission_code.contains(GlobalKey.Permission.ProduceInpsect)) {//执行巡检
                deviceInspectionLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.InspectCheck)) {//巡检审核
                deviceVerifyLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.Repair_Bill)) {//维修单据
                repairBillLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.Repair_Recevive)) {//维修接单
                receiveRepairOrderLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.Repair_Apply)) {//维修申请
                repairApplyLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.Repair_Comment)) {//维修评价
                repairCommentLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.QrManagment)) {//条码管理
                qrManagementLl.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * 品质管理模块权限控制
         */
        {
            if (!permission_code.contains(GlobalKey.Permission.ProductCheck)) {//产品审核
                productVertifyLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.MaterialCheck)) {//原料审核
                materialVertifyLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.ProcessCheck)) {//制程审核
                produceVertifyLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.ProductRelease)) {//产品发布
                productReleaseLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.MaterialRelease)) {//原料发布
                materialReleaseLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.ProcessRelease)) {//制程发布
                produceReleaseLl.setVisibility(View.INVISIBLE);
            }
        }

        /***
         * 仓库管理模块权限控制
         */
        {
            if (!permission_code.contains(GlobalKey.Permission.MaterialInput)) {//原料入库
                materialInputLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.MaterialOutput)) {//原料出库执行
                materialOutputLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.MaterialOutputCheck)) {//原料出库审核
                materialOutputVertifyLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.ProductInput)) {//产品入库
                productInputLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.ProductOutput)) {//产品出库执行
                productOutputLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.ProductOutputCheck)) {//产品出库审核
                productOutputVertifyLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.AddProductVertify)) {//新增缴库
                addVertifyLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.SampleInput)) {//样品入库
                sampleInputLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.SampleOutput)) {//样品领取
                sampleOutputLl.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * 生产管理模块的权限控制
         */
        {
            if (!permission_code.contains(GlobalKey.Permission.ProductCheckScale)) {//生产核秤
                productCheckScaleLl.setVisibility(View.INVISIBLE);
            }

            if (!permission_code.contains(GlobalKey.Permission.CheckScaleVertify)) {//生产审核
                checkScaleVertifyLl.setVisibility(View.INVISIBLE);
            }

            if (!permission_code.contains(GlobalKey.Permission.ShaiwangCheck)) {//筛网检查
                shaiwangCheckLl.setVisibility(View.INVISIBLE);
            }
            if (!permission_code.contains(GlobalKey.Permission.JobTransform)) {//岗位交接
                jiaojiebanLl.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * ID 管理模块的权限控制
         */
        if (!permission_code.contains(GlobalKey.Permission.IDManagement)) {//ID管理
            idBindLl.setVisibility(View.INVISIBLE);
        }

    }

    private void initView() {
        String name = "";
        name = SharedPreferenceUtils.getString(HomeActivity.this, GlobalKey.Login.CODE, name);
        ToastUtil.showToast(HomeActivity.this, "欢迎用户：" + name, ToastUtil.Success);
        initNavView();
        initCardView();
        checkPermission();
    }

    private void initNavView() {

        View headerView = navView.getHeaderView(0);
        SketchImageView head = headerView.findViewById(R.id.head);
        TextView id = headerView.findViewById(R.id.tvid);

        head.getOptions()
                .setCacheInDiskDisabled(true)
                .setShaper(new CircleImageShaper())
                .setDecodeGifImage(true) //显示gif
                .setLoadingImage(R.mipmap.ic_autorenew) //加载时的图片
                .setErrorImage(R.mipmap.defualt_head)  //错误时的图片
                .setShapeSize(ShapeSize.byViewFixedSize()) //设置尺寸
                .setDisplayer(new FadeInImageDisplayer()); //显示图片的动画
        head.displayResourceImage(R.mipmap.icon);
        head.setClickRetryOnDisplayErrorEnabled(true);//加载失败时点击重新加载

        String code = "";
        code = SharedPreferenceUtils.getString(HomeActivity.this, GlobalKey.Login.CODE, code);
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

    private void initCardView() {
        String userCode = SharedPreferenceUtils.getString(this, GlobalKey.Login.CODE, "");
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


    @OnClick({R.id.back, R.id.device_inspection_ll, R.id.repair_bill_ll, R.id.repair_apply_ll, R.id.qr_management_ll, R.id.device_verify_ll, R.id.receive_repair_order_ll, R.id.repair_comment_ll, R.id.product_vertify_ll, R.id.material_vertify_ll, R.id.produce_vertify_ll, R.id.product_release_ll, R.id.material_release_ll, R.id.produce_release_ll, R.id.material_input_ll, R.id.material_output_ll, R.id.product_input_ll, R.id.product_output_vertify_ll, R.id.sample_input_ll, R.id.sample_output_ll, R.id.add_vertify_ll, R.id.material_output_vertify_ll, R.id.product_output_ll, R.id.product_check_scale_ll, R.id.shaiwang_check_ll, R.id.jiaojieban_ll, R.id.id_bind_ll, R.id.check_scale_vertify_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.device_inspection_ll:
                ToastUtil.showToast(HomeActivity.this, "执行巡检", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, InspectWorkerActivity.class);
                break;
            case R.id.repair_bill_ll:
                ToastUtil.showToast(HomeActivity.this, "维修单据", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, RepairBillActivity.class);
                break;
            case R.id.repair_apply_ll:
                ToastUtil.showToast(HomeActivity.this, "维修申请", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, RepairApplyActivity.class);
                break;
            case R.id.qr_management_ll:
                ToastUtil.showToast(HomeActivity.this, "条码管理", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, QrManageActivity.class);
                break;
            case R.id.device_verify_ll:
                ToastUtil.showToast(HomeActivity.this, "巡检审核", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, InspectMonitorActivity.class);
                break;
            case R.id.receive_repair_order_ll:
                ToastUtil.showToast(HomeActivity.this, "接维修单", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, RepairWorkActivity.class);
                break;
            case R.id.repair_comment_ll:
                ToastUtil.showToast(HomeActivity.this, "维修评价", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, RepairScoreActivity.class);
                break;
            case R.id.product_vertify_ll:
                ToastUtil.showToast(HomeActivity.this, "产品审核", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestCheckProductActivity.class);
                break;
            case R.id.material_vertify_ll:
                ToastUtil.showToast(HomeActivity.this, "原料审核", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestCheckActivity.class);
                break;
            case R.id.produce_vertify_ll:
                ToastUtil.showToast(HomeActivity.this, "制程审核", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestCheckProcessActivity.class);
                break;
            case R.id.product_release_ll:
                ToastUtil.showToast(HomeActivity.this, "产品发布", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestReleaseActivity.class);
                break;
            case R.id.material_release_ll:
                ToastUtil.showToast(HomeActivity.this, "原料发布", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestReleaseActivity.class);
                break;
            case R.id.produce_release_ll:
                ToastUtil.showToast(HomeActivity.this, "制程发布", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, TestReleaseProcessActivity.class);
                break;
            case R.id.material_input_ll:
                ToastUtil.showToast(HomeActivity.this, "原料入库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, MaterialInActivity.class);
                break;
            case R.id.material_output_ll:
                ToastUtil.showToast(HomeActivity.this, "原料出库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, MaterialOutActivity.class);
                break;
            case R.id.product_input_ll:
                ToastUtil.showToast(HomeActivity.this, "产品入库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, ProductInActivity.class);
                break;
            case R.id.product_output_vertify_ll:
                ToastUtil.showToast(HomeActivity.this, "产品出库审核", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, ProductOutCheckActivity.class);
                break;
            case R.id.sample_input_ll:
                ToastUtil.showToast(HomeActivity.this, "样品入库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, SampleInActivity.class);
                break;
            case R.id.sample_output_ll:
                ToastUtil.showToast(HomeActivity.this, "样品出库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, SampleOutActivity.class);
                break;
            case R.id.add_vertify_ll:
                ToastUtil.showToast(HomeActivity.this, "新增缴库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, ProductInAddActivityDetail1Scan.class);
                break;
            case R.id.material_output_vertify_ll:
                ToastUtil.showToast(HomeActivity.this, "原料出库审核", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, MaterialOutCheckActivity.class);
                break;
            case R.id.product_output_ll:
                ToastUtil.showToast(HomeActivity.this, "产品出库", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, ProductOutActivity.class);
                break;
            case R.id.product_check_scale_ll:
                ToastUtil.showToast(HomeActivity.this, "生产核秤", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, CheckScalesMemberActivity.class);
                break;
            case R.id.check_scale_vertify_ll:
                ToastUtil.showToast(HomeActivity.this, "核秤审核", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, CheckScalesCheckActivity.class);
                break;
            case R.id.shaiwang_check_ll:
                ToastUtil.showToast(HomeActivity.this, "筛网检查", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, ShaiWangManagement.class);
                break;
            case R.id.jiaojieban_ll:
                ToastUtil.showToast(HomeActivity.this, "岗位交接", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, JiaoJieBanManagement.class);
                break;
            case R.id.id_bind_ll:
                ToastUtil.showToast(HomeActivity.this, "ID绑定", ToastUtil.Default);
                ActivityUtil.switchTo(HomeActivity.this, IdManagementActivity.class);
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
                ActivityManager.getAppManager().AppExit();//清除所有的Activity
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
