package com.msw.mesapp.activity.home.production_management.CheckScalesManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckScalesCheckDetails2 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.tv4)
    TextView tv4;
    @Bind(R.id.tv5)
    TextView tv5;
    @Bind(R.id.tv6)
    TextView tv6;
    @Bind(R.id.tv7)
    TextView tv7;
    @Bind(R.id.tv8)
    TextView tv8;
    @Bind(R.id.tv9)
    TextView tv9;
    @Bind(R.id.cancle_bt)
    Button cancleBt;
    @Bind(R.id.confirm_bt)
    Button confirmBt;

    private String code = "";
    private String name = "";
    String auditTime = "";
    String dutyName = "";
    String dutyCode = "";
    String leftUp = "";
    String rightUp = "";
    String center = "";
    String leftDown = "";
    String rightDown = "";
    String judgment = "";
    String auditorCode = "";
    String auditorName = "";
    private String eqiupmentCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_scales_check_details2);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        name = getIntent().getExtras().get("equipmentName").toString();
        eqiupmentCode = getIntent().getExtras().get("eqiupmentCode").toString();
        initView();
        getData();
    }

    public void getData() {
        EasyHttp.post(GlobalApi.ProductManagement.CheckScale.getByCode)
            .params("code", code)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(CheckScalesCheckDetails2.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.optJSONObject("data");
                        auditTime = DateUtil.getDateToString1(data.optLong("auditTime"));
                        dutyName = data.optJSONObject("dutyCode").optString("name");
                        dutyCode = data.optJSONObject("dutyCode").optString("code");
                        leftUp = data.optString("leftUp");
                        rightUp = data.optString("rightUp");
                        center = data.optString("center");
                        leftDown = data.optString("leftDown");
                        rightDown = data.optString("rightDown");
                        judgment = data.optString("judgment");
                        auditorCode = data.optJSONObject("auditorCode").optString("code");
                        auditorName = data.optJSONObject("auditorCode").optString("name");

                        tv1.setText(auditTime);
                        tv2.setText(dutyName);
                        tv3.setText(leftUp);
                        tv4.setText(rightUp);
                        tv5.setText(center);
                        tv6.setText(leftDown);
                        tv7.setText(rightDown);
                        if (judgment.equals("0")) {
                            tv8.setText("不合格");
                        } else {
                            tv8.setText("合格");
                        }
                        tv9.setText(auditorName);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    public void initView() {
        title.setText(name);
        add.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.back, R.id.cancle_bt, R.id.confirm_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.cancle_bt:
                finish();
                break;
            case R.id.confirm_bt:
                Log.i("TAG", code + "----" + eqiupmentCode + "---" + dutyCode);
                submit();
                break;
        }
    }

    public void submit() {
        EasyHttp.post(GlobalApi.ProductManagement.CheckScale.update)
            .params("code", code)
            .params("equipmentCode", eqiupmentCode)
            .params("dutyCode", dutyCode)
            .params("leftUp", leftUp)
            .params("rightUp", rightUp)
            .params("center", center)
            .params("leftDown", leftDown)
            .params("rightDown", rightDown)
            .params("judgment", judgment)
            .params("auditorCode", auditorCode)
            .params("auditorTime", auditTime)
            .params("confirm", "1")
            .params("confirmorCode", GetCurrentUserIDUtil.currentUserId(this))
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(CheckScalesCheckDetails2.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int code = jsonObject.optInt("code");
                        String message = jsonObject.optString("message");
                        if (code == 0) {
                            ToastUtil.showToast(CheckScalesCheckDetails2.this, "提交成功", ToastUtil.Success);
                        } else {
                            ToastUtil.showToast(CheckScalesCheckDetails2.this, message, ToastUtil.Default);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
