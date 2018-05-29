package com.msw.mesapp.activity.home.warehouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 正常情况审核流程
 */
public class MaterialOutCheckActivityDetail1 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.department_tv)
    TextView departmentTv;
    @Bind(R.id.applyTime_tv)
    TextView applyTimeTv;
    @Bind(R.id.auditStatus_tv)
    TextView auditStatusTv;
    @Bind(R.id.processType_tv)
    TextView processTypeTv;
    @Bind(R.id.godown_status_tv)
    TextView godownStatusTv;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.suggestion_et)
    EditText suggestionEt;
    @Bind(R.id.disagree_bt)
    Button disagreeBt;
    @Bind(R.id.agree_bt)
    Button agreeBt;
    public String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_out_check_detail1);
        code = getIntent().getExtras().get("code").toString();
    }

    @OnClick({R.id.back, R.id.disagree_bt, R.id.agree_bt})
    public void onViewClicked(View view) {
        String note = suggestionEt.getText().toString();
        String auditorCode = SPUtil.get(this, GlobalKey.Login.CODE, "").toString();
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.disagree_bt:
                submit(code, "3", note, auditorCode);
                break;
            case R.id.agree_bt:
                submit(code, "2", note, auditorCode);
                break;
        }
    }

    /**
     * 将提交结果统一为一个函数进行处理，根据参数变化即可
     * code
     * auditStatus
     * note
     * auditorCode
     */
    public void submit(String code, String auditStatus, String note, String auditorCode) {
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.updateAuditStatusByCode) //获取发货单
                .params(GlobalApi.WareHourse.code, code)
                .params(GlobalApi.WareHourse.auditStatus, auditStatus)
                .params(GlobalApi.WareHourse.note, note)
                .params(GlobalApi.WareHourse.auditorCode, auditorCode)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ToastUtil.showToast(MaterialOutCheckActivityDetail1.this, "成功！", 1);
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(MaterialOutCheckActivityDetail1.this, "失败！", 1);
                    }
                });
    }
}
