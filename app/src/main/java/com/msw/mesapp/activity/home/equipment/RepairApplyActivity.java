package com.msw.mesapp.activity.home.equipment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.dev.BarcodeAPI;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepairApplyActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.et1)
    MaterialEditText et1;
    @Bind(R.id.et2)
    MaterialEditText et2;
    @Bind(R.id.et3)
    MaterialEditText et3;
    @Bind(R.id.et4)
    EditText et4;
    @Bind(R.id.bt)
    Button bt;

    private String t1 = "";
    private String t2 = "";
    private String t3 = "";
    private String t4 = "";
    private String id = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BarcodeAPI.BARCODE_READ:
                    String s = (String) msg.obj;
                    s = s.replace('\n',' '); s = s.replace('\r',' ');
                    String[] splitstr = s.split("-");
                    et1.setText(splitstr[0]);
                    et2.setText(splitstr[1]);
                    et3.setText(splitstr[2]);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_apply);
        ButterKnife.bind(this);

        initData();
        initView();

    }


    private void initData() {
        id = (String) SPUtil.get(RepairApplyActivity.this, GlobalKey.Login.CODE, id);
    }

    public void initPermission(){
        String permission_code = (String) SPUtil.get(RepairApplyActivity.this, GlobalKey.permiss.SPKEY, new String(""));
        String[] split_pc = permission_code.split("-");
        int t = 0;
        for(int i = 0;i<split_pc.length;i++){
            if(split_pc[i].equals(GlobalKey.permiss.Repair_Report) ) t++;
        }
        if(t==0){
            finish();
            ToastUtil.showToast(RepairApplyActivity.this,"权限不足！",ToastUtil.Error);
        }
    }

    public void initTitle(){
        initPermission();

        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        title.setText("维修申请");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final boolean[] Sflag = {true};
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Sflag[0]){
                    BarcodeAPI.getInstance().setLights(true);
                    BarcodeAPI.getInstance().scan();
                }else{
                    BarcodeAPI.getInstance().setLights(false);
                    BarcodeAPI.getInstance().stopScan();
                }
            }
        });
    }
    private void initView() {
        //et1.setText("001");
        //et2.setText("3");
        //et3.setText("001");

        BarcodeAPI.getInstance().open();
        BarcodeAPI.getInstance().setScannerType(20);// 设置扫描头类型(10:5110; 20: N3680 40:MJ-2000)
        int ver= Build.VERSION.SDK_INT;
        BarcodeAPI.getInstance().m_handler = mHandler;
        BarcodeAPI.getInstance().setEncoding("gbk");
        BarcodeAPI.getInstance().setScanMode(true);//打开连扫

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1 = et1.getText().toString();
                t2 = et2.getText().toString();
                t3 = et3.getText().toString();
                t4 = et4.getText().toString();
                //if(t1.length() == 0) { et1.setError("输入不能为空"); return;}
                //if(t2.length() == 0) { et2.setError("输入不能为空"); return;}
                //if(t3.length() == 0) { et3.setError("输入不能为空"); return;}
                if(t4.length() == 0) { et4.setError("输入不能为空"); return;}

                IProgressDialog mProgressDialog = new IProgressDialog() {
                    @Override
                    public Dialog getDialog() {
                        ProgressDialog dialog = new ProgressDialog(RepairApplyActivity.this);
                        dialog.setMessage(GlobalApi.ProgressDialog.UpData);
                        return dialog;
                    }
                };
                EasyHttp.post(GlobalApi.Repair.UpErr.PATH)
                        .params(GlobalApi.Repair.UpErr.department_code, t1) //部门
                        .params(GlobalApi.Repair.UpErr.eqArchive_code, t2) //设备名称
                        .params(GlobalApi.Repair.UpErr.productLine_code, t3) //生产线
                        .params(GlobalApi.Repair.UpErr.applicationDescription, t4) //故障描述
                        .params(GlobalApi.Repair.UpErr.applicationPerson_code, id) //申请人id
                        .sign(true)
                        .timeStamp(true)//本次请求是否携带时间戳
                        .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                            @Override
                            public void onSuccess(String loginModel) {
                                int code = 1;
                                String message = "出错";

                                try {
                                    JSONObject jsonObject = JSON.parseObject(loginModel);
                                    code = (int) jsonObject.get("code");
                                    message = (String) jsonObject.get("message");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (code == 0) {
                                    ToastUtil.showToast(RepairApplyActivity.this, message, ToastUtil.Success);
                                    finish();
                                    ActivityUtil.switchTo(RepairApplyActivity.this, RepairApplyActivity.class);
                                } else {
                                    ToastUtil.showToast(RepairApplyActivity.this, message, ToastUtil.Error);
                                }
                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                ToastUtil.showToast(RepairApplyActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                            }
                        });
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        BarcodeAPI.getInstance().m_handler = mHandler;
    }

    @Override
    public void onDestroy() {
        BarcodeAPI.getInstance().m_handler = null;
        BarcodeAPI.getInstance().close();
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_INFO:
            case KeyEvent.KEYCODE_MUTE:
                if (event.getRepeatCount() == 0) {
                    BarcodeAPI.getInstance().scan();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
