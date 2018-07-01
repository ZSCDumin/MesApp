package com.msw.mesapp.activity.home.production_management.CheckScalesManagement;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckScalesManagementDetails3 extends Activity {

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
    @Bind(R.id.tv10)
    TextView tv10;
    @Bind(R.id.tv11)
    TextView tv11;
    @Bind(R.id.back_bt)
    Button backBt;

    private String code = "";
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_scales_management_details3);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        name = getIntent().getExtras().get("name").toString();
        initView();
        getData();
    }

    public void getData() {
        EasyHttp.post(GlobalApi.ProductManagement.CheckScale.getByCode)
            .params("code", code)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(CheckScalesManagementDetails3.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.optJSONObject("data");
                        String auditTime = DateUtil.getDateToString1(data.optLong("auditTime"));
                        String dutyName = data.optJSONObject("dutyCode").optString("name");
                        String leftUp = data.optString("leftUp");
                        String rightUp = data.optString("rightUp");
                        String center = data.optString("center");
                        String leftDown = data.optString("leftDown");
                        String rightDown = data.optString("rightDown");
                        String judgment = data.optString("judgment");
                        String auditorCode = data.optJSONObject("auditorCode").optString("name");

                        String confirmorCode = "";
                        if (data.optJSONObject("confirmorCode") != null) {
                            data.optJSONObject("confirmorCode").optString("name");
                        }
                        String confirmTime = "";
                        if (data.optString("confirmTime") != null)
                            DateUtil.getDateToString1(data.optLong("confirmTime"));

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
                        tv9.setText(auditorCode);
                        tv10.setText(confirmorCode);
                        tv11.setText(confirmTime);
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

    @OnClick({R.id.back, R.id.back_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back_bt:
                finish();
                break;
        }
    }
}
