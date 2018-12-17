package com.msw.mesapp.activity.home.warehouse;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.selected.BaseSelectFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.utils.DensityUtils;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.bean.warehouse.MaterialInBean;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 正常情况审核流程
 */
public class MaterialOutCheckActivityDetail1 extends AppCompatActivity {


    public String code = "";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_out_check_detail1);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        initTitle();
        initData();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        title.setTextSize(18);
        title.setText("原料出库");
        add.setVisibility(View.INVISIBLE);
    }

    public void initData() {
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.PATH_CODE)
            .params(GlobalApi.WareHourse.code, code) //
            .sign(true)
            .timeStamp(true)//本次请求是否携带时间戳
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    int code = 1;
                    String message = "出错";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        code = jsonObject.optInt("code");
                        message = jsonObject.optString("message");
                        JSONObject data = jsonObject.optJSONObject("data");

                        String department = data.optJSONObject("department").optString("name");
                        String applyDate = data.optString("applyDate");
                        String auditStatus = data.optString("auditStatus");
                        if ("0".equals(auditStatus)) {
                            auditStatus = "未提交";
                        } else if ("1".equals(auditStatus)) {
                            auditStatus = "在审核";
                        } else if ("2".equals(auditStatus)) {
                            auditStatus = "通过";
                        } else if ("3".equals(auditStatus)) {
                            auditStatus = "不通过";
                        }
                        int pickingStatus = data.optInt("pickingStatus");
                        String process = data.optJSONObject("process").optString("name");
                        departmentTv.setText(department);
                        auditStatusTv.setText(auditStatus);
                        applyTimeTv.setText(DateUtil.getDateToString(applyDate));
                        godownStatusTv.setText(pickingStatus == 0 ? "未出库" : "已出库");
                        processTypeTv.setText(process);

                        JSONArray pickingApplies = data.optJSONArray("pickingApplies");

                        for (int i = 0; i < pickingApplies.length(); i++) {
                            JSONObject item = pickingApplies.getJSONObject(i);
                            String rawType = item.optJSONObject("rawType").optString("name");
                            String batchNumber = item.optString("batchNumber");
                            String unit = item.optString("unit");
                            String weight = item.optString("weight").equals("null") ? "0" : item.optString("weight");
                            HashMap map = new HashMap();
                            map.put("1", rawType);
                            map.put("2", batchNumber);
                            map.put("3", unit);
                            map.put("4", weight);
                            tableList.add(map);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (code == 0) {
                        initTable();
                        ToastUtil.showToast(MaterialOutCheckActivityDetail1.this, "初始化数据成功", ToastUtil.Success);
                    } else {
                        ToastUtil.showToast(MaterialOutCheckActivityDetail1.this, message, ToastUtil.Error);
                    }
                }

                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(MaterialOutCheckActivityDetail1.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                }
            });
    }

    @OnClick({R.id.back, R.id.disagree_bt, R.id.agree_bt})
    public void onViewClicked(View view) {
        String note = suggestionEt.getText().toString();
        String auditorCode = GetCurrentUserIDUtil.currentUserId(this);
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
                    int code = 1;
                    String message = "出错";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        code = jsonObject.optInt("code");
                        message = jsonObject.optString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code == 0) {
                        ToastUtil.showToast(MaterialOutCheckActivityDetail1.this, "提交审核成功", ToastUtil.Success);
                        finish();
                    } else {
                        ToastUtil.showToast(MaterialOutCheckActivityDetail1.this, message, ToastUtil.Error);
                    }
                }

                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(MaterialOutCheckActivityDetail1.this, "出现异常！", 1);
                }
            });
    }

    List<HashMap<String, Objects>> tableList = new ArrayList<>();

    public void initTable() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<MaterialInBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = (Map) tableList.get(i);
            MaterialInBean userData = new MaterialInBean(String.valueOf(i + 1), map.get("1").toString(), map.get("2").toString(), map.get("3").toString(), map.get("4").toString());
            testData.add(userData);
        }

        WindowManager wm = this.getWindowManager();
        int screenWith = wm.getDefaultDisplay().getWidth();
        table.getConfig().setMinTableWidth(screenWith - 20); //设置最小宽度=屏幕宽度-20

        List<Column> columns = new ArrayList<>();
        Column column0 = new Column<>("序号", "id");
        column0.setFixed(true);
        column0.setAutoCount(false);
        columns.add(column0);
        columns.add(new Column<>("类型", "materialStyle"));
        columns.add(new Column<>("批号", "batchNumber"));
        columns.add(new Column<>("单位", "unit"));
        columns.add(new Column<>("重量", "weight"));

        final TableData<MaterialInBean> tableData = new TableData<>("入库单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(MaterialOutCheckActivityDetail1.this, R.color.seashell);
                } else {
                    return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                }
            }
        };
        table.setSelectFormat(new BaseSelectFormat());
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table.setTableData(tableData);
    }
}
