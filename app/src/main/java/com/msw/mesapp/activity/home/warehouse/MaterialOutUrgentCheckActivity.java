package com.msw.mesapp.activity.home.warehouse;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.bean.warehouse.MaterialInBean;
import com.msw.mesapp.bean.warehouse.MaterialOutBean;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.SPUtil;
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

public class MaterialOutUrgentCheckActivity extends AppCompatActivity {

    @Bind(R.id.bar)
    View bar;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.viewright)
    View viewright;
    @Bind(R.id.department_tv)
    TextView departmentTv;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Bind(R.id.audit_status_tv)
    TextView auditStatusTv;
    @Bind(R.id.style_tv)
    TextView styleTv;
    @Bind(R.id.godown_status_tv)
    TextView godownStatusTv;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.table1)
    SmartTable table1;
    @Bind(R.id.suggestion_et)
    EditText suggestionEt;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.disagree_rb)
    RadioButton disagreeRb;
    @Bind(R.id.agree_rb)
    RadioButton agreeRb;

    public String code = "";
    public static String auditorCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_out_urgent_check);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        getData();
    }

    @OnClick({R.id.back, R.id.submit})
    public void onViewClicked(View view) {
        auditorCode = SPUtil.get(this, GlobalKey.Login.CODE, "").toString();
        String note = suggestionEt.getText().toString();
        String selected_item = spinner.getSelectedItem().toString();
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if ("终止".equals(selected_item) || "打回".equals(selected_item)) {
                    if (disagreeRb.isChecked()) {
                        //不同意，结束
                        submit(code, "3", note, auditorCode, "0");
                    } else {
                        //同意，继续选择
                        submit(code, "2", note, auditorCode, "1");
                    }
                } else {
                    if (disagreeRb.isChecked()) {
                        //不同意，结束
                        submit(code, "3", note, auditorCode, selected_item);
                    } else {
                        //同意，继续选择
                        submit(code, "2", note, auditorCode, selected_item);
                    }
                }
                ActivityUtil.switchTo(MaterialOutUrgentCheckActivity.this, MaterialOutUrgentCheckActivity.class);
                break;
        }

    }

    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    public void getData() {
        agreeRb.setChecked(true);
        //获取剩余审批人
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.getRestAuditorByCode)
                .params(GlobalApi.WareHourse.code, code)
                .params(GlobalApi.WareHourse.curAuditorCode, auditorCode)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray data = jsonObject.optJSONArray("data");
                            data_list = new ArrayList<>();
                            data_list.add("终止");
                            data_list.add("打回");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                String code = item.optString("code");
                                data_list.add(code);
                            }
                            //适配器
                            arr_adapter = new ArrayAdapter<String>(MaterialOutUrgentCheckActivity.this, android.R.layout.simple_spinner_item, data_list);
                            //设置样式
                            arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //加载适配器
                            spinner.setAdapter(arr_adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ApiException e) {

                    }
                });

        //获取表头和table内容
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.PATH_CODE)
                .params(GlobalApi.WareHourse.code, code)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject data = jsonObject.optJSONObject("data");
                            String department = data.optJSONObject("department").optString("name").toString();
                            JSONArray contents = data.optJSONArray("pickingApplies");
                            for (int i = 0; i < contents.length(); i++) {
                                JSONObject item = contents.optJSONObject(i);
                                String rawType = item.optJSONObject("rawType").optString("name");
                                String batchNumber = item.optString("batchNumber");
                                String unit = item.optString("unit");
                                String weight = item.optString("weight");
                                HashMap map = new HashMap();
                                map.put("0", rawType);
                                map.put("1", batchNumber);
                                map.put("2", unit);
                                map.put("3", weight);
                                tableList.add(map);
                            }
                            initTable();
                            departmentTv.setText(department);
                            String applyDate = data.optString("applyDate");
                            dateTv.setText(applyDate);
                            String auditStatus = data.optString("auditStatus");
                            auditStatusTv.setText(auditStatus);
                            String processManage = data.optJSONObject("processManage").optString("name").toString();
                            styleTv.setText(processManage);
                            String pickingStatus = data.optString("pickingStatus");
                            godownStatusTv.setText(pickingStatus);
                            initTable();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ApiException e) {

                    }
                });

        //获取已审批记录
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.PATH_ApplyHeader)
                .params(GlobalApi.WareHourse.pickingApplyHeaderCode, code)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                String audit_result = item.optString("auditResult");
                                String suggestion = item.optString("note");
                                String member = item.optJSONObject("auditor").optString("name");
                                String time = DateUtil.getDateToString(Long.valueOf(item.optString("auditTime")));
                                HashMap map = new HashMap();
                                map.put("0", audit_result);
                                map.put("1", suggestion);
                                map.put("2", member);
                                map.put("3", time);
                                tableList1.add(map);
                            }
                            initTable1();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ApiException e) {

                    }
                });

    }

    List<HashMap<String, Objects>> tableList = new ArrayList<>();
    List<HashMap<String, Objects>> tableList1 = new ArrayList<>();

    public void initTable1() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<MaterialOutBean> list = new ArrayList<>();
        for (int i = 0; i < tableList1.size(); i++) {
            Map map = tableList1.get(i);
            MaterialOutBean userData = new MaterialOutBean(map.get("0").toString(), map.get("1").toString(), map.get("2").toString(), map.get("3").toString());
            list.add(userData);
        }

        WindowManager wm = this.getWindowManager();
        int screenWith = wm.getDefaultDisplay().getWidth();
        table1.getConfig().setMinTableWidth(screenWith - 20); //设置最小宽度=屏幕宽度-20

        List<Column> columns = new ArrayList<>();
        Column column0 = new Column<>("审核结果", "result");
        column0.setFixed(true);
        column0.setAutoCount(false);
        columns.add(column0);
        columns.add(new Column<>("审核意见", "suggestion"));
        columns.add(new Column<>("审核人", "auditor"));
        columns.add(new Column<>("审核时间", "date"));

        final TableData<MaterialOutBean> tableData = new TableData<>("审批列表", list, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table1.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(MaterialOutUrgentCheckActivity.this, R.color.seashell);
                } else {
                    return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                }
            }
        };
        table1.setSelectFormat(new BaseSelectFormat());
        table1.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table1.setTableData(tableData);
    }

    public void initTable() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<MaterialInBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = tableList.get(i);
            MaterialInBean userData = new MaterialInBean(String.valueOf(i + 1), map.get("0").toString(), map.get("1").toString(), map.get("2").toString(), map.get("3").toString());
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
        columns.add(new Column<>("批号", "batchNumber"));
        columns.add(new Column<>("单位", "unit"));
        columns.add(new Column<>("重量", "weight"));

        final TableData<MaterialInBean> tableData = new TableData<>("紧急审核单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(MaterialOutUrgentCheckActivity.this, R.color.seashell);
                } else {
                    return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                }
            }
        };
        table.setSelectFormat(new BaseSelectFormat());
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table.setTableData(tableData);
    }

    /**
     * @param code
     * @param auditStatus
     * @param note
     * @param auditorCode
     * @param nextAuditorCode
     */
    public void submit(String code, String auditStatus, String note, String auditorCode, String nextAuditorCode) {
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.updateAuditStatusByCodeUrgent)
                .params(GlobalApi.WareHourse.code, code)
                .params(GlobalApi.WareHourse.auditStatus, auditStatus)
                .params(GlobalApi.WareHourse.note, note)
                .params(GlobalApi.WareHourse.auditorCode, auditorCode)
                .params(GlobalApi.WareHourse.nextAuditorCode, nextAuditorCode)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ToastUtil.showToast(MaterialOutUrgentCheckActivity.this, "传递成功", 1);
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(MaterialOutUrgentCheckActivity.this, "传递失败", 1);
                    }
                });

    }
}
