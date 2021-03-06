package com.msw.mesapp.activity.home.production_management.CheckScalesManagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckScalesManagementDetails2 extends Activity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.bt2)
    Button bt2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.bt3)
    Button bt3;
    @Bind(R.id.tv4)
    TextView tv4;
    @Bind(R.id.bt4)
    Button bt4;
    @Bind(R.id.tv5)
    TextView tv5;
    @Bind(R.id.bt5)
    Button bt5;
    @Bind(R.id.tv6)
    TextView tv6;
    @Bind(R.id.submit_bt)
    Button submitBt;
    @Bind(R.id.baiban)
    RadioButton baiban;
    @Bind(R.id.wanban)
    RadioButton wanban;

    private int flag = 1; //是否合格的标记，1为合格
    private String dutyCode = "1"; //是否合格的标记，1为合格
    private String code = "";
    private String name = "";
    private String scale_value = "";


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x011:
                    tv1.setText(scale_value);
                    check(tv1.getText().toString());
                    break;
                case 0x012:
                    tv2.setText(scale_value);
                    check(tv2.getText().toString());
                    break;
                case 0x013:
                    tv3.setText(scale_value);
                    check(tv3.getText().toString());
                    break;
                case 0x014:
                    tv4.setText(scale_value);
                    check(tv4.getText().toString());
                    break;
                case 0x015:
                    tv5.setText(scale_value);
                    check(tv5.getText().toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_scales_management_details2);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        name = getIntent().getExtras().get("name").toString();
        initView();
    }

    public void initView() {
        title.setText(name);
        add.setVisibility(View.INVISIBLE);
    }

    public void check(String value) {
        double t1 = Double.valueOf(value);
        if (Math.abs(t1 - 100.0) > 0.2) {
            flag = 0;
        }
        if (flag == 1) {
            tv6.setText("合格");
        } else {
            tv6.setText("不合格");
        }
    }

    @OnClick({R.id.back, R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4, R.id.bt5, R.id.submit_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bt1:
                getScaleData(1);
                break;
            case R.id.bt2:
                getScaleData(2);
                break;
            case R.id.bt3:
                getScaleData(3);
                break;
            case R.id.bt4:
                getScaleData(4);
                break;
            case R.id.bt5:
                getScaleData(5);
                break;
            case R.id.submit_bt:
                if (baiban.isChecked()) {
                    dutyCode = "1";
                } else if (wanban.isChecked()) {
                    dutyCode = "2";
                }
                submit();
                break;
        }
    }

    public void submit() {
        EasyHttp.post(GlobalApi.ProductManagement.CheckScale.add)
            .params("equipmentCode", code)
            .params("dutyCode", dutyCode)
            .params("leftUp", tv1.getText().toString())
            .params("rightUp", tv2.getText().toString())
            .params("center", tv3.getText().toString())
            .params("leftDown", tv4.getText().toString())
            .params("rightDown", tv5.getText().toString())
            .params("judgment", flag + "")
            .params("auditorCode", GetCurrentUserIDUtil.currentUserId(this))
            .params("confirm", "0")
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(CheckScalesManagementDetails2.this, "提交数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int code = jsonObject.optInt("code");
                        String message = jsonObject.optString("message");
                        if (code == 0) {
                            ToastUtil.showToast(CheckScalesManagementDetails2.this, "提交成功", ToastUtil.Success);
                            finish();
                        } else
                            ToastUtil.showToast(CheckScalesManagementDetails2.this, message, ToastUtil.Success);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    public void getScaleData(final int n) {
        EasyHttp.post(GlobalApi.ProductManagement.CheckScale.getRealDateByEquipmentCode)
            .params("equipmentCode", code)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(CheckScalesManagementDetails2.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.optJSONObject("data");
                        String message = jsonObject.optString("message");
                        scale_value = data.optString("value");
                        if (message.equals("成功")) {
                            ToastUtil.showToast(CheckScalesManagementDetails2.this, "提交成功", ToastUtil.Success);
                            if (n == 1)
                                handler.sendEmptyMessage(0x011);
                            else if (n == 2)
                                handler.sendEmptyMessage(0x012);
                            else if (n == 3)
                                handler.sendEmptyMessage(0x013);
                            else if (n == 4)
                                handler.sendEmptyMessage(0x014);
                            else if (n == 5)
                                handler.sendEmptyMessage(0x015);
                        } else
                            ToastUtil.showToast(CheckScalesManagementDetails2.this, message, ToastUtil.Success);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    }

    public void getData() {
        EasyHttp.post(GlobalApi.ProductManagement.CheckScale.getByEquipmentCodeByPage)
            .params("equipmentCode", code)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(CheckScalesManagementDetails2.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int code = jsonObject.optInt("code");
                        String message = jsonObject.optString("message");
                        if (code == 0)
                            ToastUtil.showToast(CheckScalesManagementDetails2.this, "提交成功", ToastUtil.Success);
                        else
                            ToastUtil.showToast(CheckScalesManagementDetails2.this, message, ToastUtil.Success);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
