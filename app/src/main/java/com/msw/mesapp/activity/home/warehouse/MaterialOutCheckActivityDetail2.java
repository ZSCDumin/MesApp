package com.msw.mesapp.activity.home.warehouse;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.msw.mesapp.bean.warehouse.MaterialInBean;
import com.msw.mesapp.bean.warehouse.ProductOutCheckBean;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.StatusBarUtils;
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
import java.util.Objects;

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
    @Bind(R.id.auditStatus_tv)
    TextView auditStatusTv;
    @Bind(R.id.processType_tv)
    TextView processTypeTv;
    @Bind(R.id.godown_status_tv)
    TextView godownStatusTv;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.table1)
    SmartTable table1;
    @Bind(R.id.back_bt)
    Button backBt;
    List<Map<String, Object>> list = new ArrayList<>();
    List<HashMap<String, Objects>> tableList = new ArrayList<>();
    List<HashMap<String, Objects>> tableList1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_out_check_detail2);
        ButterKnife.bind(this);
        initTitle();
        initData(getIntent().getExtras().get("code").toString());
        initTable();
        initTable1();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setTextSize(18);
        title.setText("原料出库");
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
                            JSONObject data = jsonObject.optJSONObject("data");

                            String department = data.optJSONObject("department").optString("name");
                            String applyDate = data.optString("applyDate");
                            String auditStatus = data.optString("auditStatus");
                            String pickingStatus = data.optString("pickingStatus");
                            String process = data.optJSONObject("process").optString("name");
                            departmentTv.setText(department);
                            auditStatusTv.setText(auditStatus);
                            applyDateTv.setText(applyDate);
                            godownStatusTv.setText(pickingStatus);
                            processTypeTv.setText(process);

                            JSONArray pickingApplies = data.optJSONArray("pickingApplies");

                            for (int i = 0; i < pickingApplies.length(); i++) {
                                JSONObject item = pickingApplies.getJSONObject(i);

                                String rawType = item.optString("rawType");
                                String batchNumber = item.optString("batchNumber");
                                String unit = item.optString("unit");
                                String weight = item.optString("weight");
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

                        } else {
                            ToastUtil.showToast(MaterialOutCheckActivityDetail2.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(MaterialOutCheckActivityDetail2.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.PATH_ApplyHeader)
                .params(GlobalApi.WareHourse.pickingApplyHeaderCode, code) //
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        int code = 1;
                        String message = "出错";
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                String auditResult = item.optString("auditResult");
                                String auditor = item.optJSONObject("auditor").optString("name");
                                String auditTime = DateUtil.getDateToString(Long.valueOf(item.optString("auditTime")));
                                String note = item.optString("note");
                                HashMap map = new HashMap();
                                map.put("0", auditor);
                                map.put("1", note);
                                map.put("2", auditResult);
                                map.put("3", auditTime);
                                tableList1.add(map);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {

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

    public void initTable() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<MaterialInBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = tableList.get(i);
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
        columns.add(new Column<>("类型", "rawType"));
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
                    return ContextCompat.getColor(MaterialOutCheckActivityDetail2.this, R.color.seashell);
                } else {
                    return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                }
            }
        };
        table.setSelectFormat(new BaseSelectFormat());
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table.setTableData(tableData);
    }

    public void initTable1() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<ProductOutCheckBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList1.size(); i++) {
            Map map = tableList1.get(i);
            ProductOutCheckBean userData = new ProductOutCheckBean(map.get("0").toString(), map.get("1").toString(), map.get("2").toString(), map.get("3").toString());
            testData.add(userData);
        }

        WindowManager wm = this.getWindowManager();
        int screenWith = wm.getDefaultDisplay().getWidth();
        table1.getConfig().setMinTableWidth(screenWith - 20); //设置最小宽度=屏幕宽度-20

        List<Column> columns = new ArrayList<>();
        Column column0 = new Column<>("审核人", "auditor");
        column0.setFixed(true);
        column0.setAutoCount(false);
        columns.add(column0);
        columns.add(new Column<>("审核意见", "auditNote"));
        columns.add(new Column<>("审核结果", "auditResult"));
        columns.add(new Column<>("审核时间", "auditTime"));

        final TableData<ProductOutCheckBean> tableData = new TableData<>("出库审核单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table1.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(MaterialOutCheckActivityDetail2.this, R.color.seashell);
                } else {
                    return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                }
            }
        };
        table1.setSelectFormat(new BaseSelectFormat());
        table1.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table1.setTableData(tableData);
    }
}
