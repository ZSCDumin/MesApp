package com.msw.mesapp.activity.home.warehouse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 正常流程下的审批详情
 */
public class MaterialOutCheckActivityDetail2 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.department_tv)
    TextView departmentTv;
    @Bind(R.id.apply_date_tv)
    TextView applyDateTv;
    @Bind(R.id.auditStatus)
    TextView auditStatus;
    @Bind(R.id.processType)
    TextView processType;
    @Bind(R.id.godown_status_tv)
    TextView godownStatusTv;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.audit_result_tv)
    TextView auditResultTv;
    @Bind(R.id.audit_suggestion)
    TextView auditSuggestion;
    @Bind(R.id.auditor)
    TextView auditor;
    @Bind(R.id.audit_time)
    TextView auditTime;
    @Bind(R.id.back_bt)
    Button backBt;
    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_out_check_detail2);
        ButterKnife.bind(this);
        title.setText("原料入库");
        add.setVisibility(View.INVISIBLE);
    }

    public void initData(String code) {
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.PATH_CODE)
                .params(GlobalApi.WareHourse.code, code)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        int code = 1;
                        String message = "出错";

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            code = jsonObject.optInt("code");
                            message = jsonObject.optString("message");
                            JSONArray data = jsonObject.optJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject content0 = data.optJSONObject(i);
                                String department = content0.optString("department"); //获取部门信息
                                String processManage = content0.optString("processManage");
                                String applyDate = content0.optString("applyDate");
                                String auditStatus = content0.optString("auditStatus");
                                String pickingStatus = content0.optString("pickingStatus");

                                Map listmap = new HashMap<>();
                                listmap.put("1", department); //出库单编号
                                listmap.put("2", applyDate); //申请日期
                                listmap.put("3", auditStatus); //审核状态
                                listmap.put("4", processManage); //流程类型
                                listmap.put("5", pickingStatus); //领取状态
                                list.add(listmap);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            adapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(MaterialOutCheckActivityDetail2.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(MaterialOutCheckActivityDetail2.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
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
