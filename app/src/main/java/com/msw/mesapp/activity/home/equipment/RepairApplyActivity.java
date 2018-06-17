package com.msw.mesapp.activity.home.equipment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
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
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.describe_et)
    EditText describeEt;
    @Bind(R.id.equipment_code_tv)
    TextView equipmentCodeTv;
    @Bind(R.id.department_code_tv)
    TextView departmentCodeTv;
    @Bind(R.id.product_line_code_tv)
    TextView productLineCodeTv;

    private String equipment_code = "";
    private String department_code = "";
    private String product_line_code = "";
    private String describle = "";
    private String id = "";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BarcodeAPI.BARCODE_READ:
                    String s = (String) msg.obj;
                    String[] splitstr = s.split("-");
                    equipmentCodeTv.setText(splitstr[0]);
                    productLineCodeTv.setText(splitstr[1]);
                    departmentCodeTv.setText(splitstr[2]);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_apply);
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        ButterKnife.bind(this);
        initData();
        initView();
        initTitle();
    }


    private void initData() {
        id = GetCurrentUserIDUtil.currentUserId(this);
    }


    public void initTitle() {
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
                if (Sflag[0]) {
                    BarcodeAPI.getInstance().setLights(true);
                    BarcodeAPI.getInstance().scan();
                } else {
                    BarcodeAPI.getInstance().setLights(false);
                    BarcodeAPI.getInstance().stopScan();
                }
            }
        });
    }

    private void initView() {

        BarcodeAPI.getInstance().open();
        BarcodeAPI.getInstance().setScannerType(20);// 设置扫描头类型(10:5110; 20: N3680 40:MJ-2000)
        BarcodeAPI.getInstance().m_handler = mHandler;
        BarcodeAPI.getInstance().setEncoding("utf8");
        BarcodeAPI.getInstance().setScanMode(true);//打开连扫

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equipment_code = equipmentCodeTv.getText().toString().trim();
                product_line_code = productLineCodeTv.getText().toString().trim();
                department_code = departmentCodeTv.getText().toString().trim();
                describle = describeEt.getText().toString().trim();
                if (describle.length() == 0) {
                    describeEt.setError("描述不能为空");
                    return;
                }
                IProgressDialog mProgressDialog = new IProgressDialog() {
                    @Override
                    public Dialog getDialog() {
                        ProgressDialog dialog = new ProgressDialog(RepairApplyActivity.this);
                        dialog.setMessage(GlobalApi.ProgressDialog.UpData);
                        return dialog;
                    }
                };
                EasyHttp.post(GlobalApi.Repair.UpErr.PATH)
                        .params(GlobalApi.Repair.UpErr.department_code, department_code) //部门
                        .params(GlobalApi.Repair.UpErr.eqArchive_code, equipment_code) //设备
                        .params(GlobalApi.Repair.UpErr.productLine_code, product_line_code) //生产线
                        .params(GlobalApi.Repair.UpErr.applicationDescription, describle) //故障描述
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
