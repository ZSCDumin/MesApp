package com.msw.mesapp.activity.home.warehouse;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.msw.mesapp.bean.warehouse.ProductOutBean;
import com.msw.mesapp.bean.warehouse.ProductOutCheckBean;
import com.msw.mesapp.utils.ActivityUtil;
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

public class ProductOutCheckActivityDetail2 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.tx1)
    TextView tx1;
    @Bind(R.id.tx2)
    TextView tx2;
    @Bind(R.id.tx3)
    TextView tx3;
    @Bind(R.id.tx4)
    TextView tx4;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.txc1)
    TextView txc1;
    @Bind(R.id.txc2)
    TextView txc2;
    @Bind(R.id.table1)
    SmartTable table1;
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.tx5)
    TextView tx5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_out_check_detail2);
        ButterKnife.bind(this);
        initTitle();
        initData();
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
        title.setText("产品出库");
        add.setVisibility(View.INVISIBLE);
    }

    public String code = "";

    public void initData() {
        code = getIntent().getExtras().get("code").toString();
        EasyHttp.post(GlobalApi.WareHourse.ProductOut.getByCode)
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
                            JSONObject data = jsonObject.optJSONObject("data");

                            String number = data.optString("number");
                            tx1.setText(number);
                            String rawType = data.optJSONObject("rawType").optString("name");
                            tx2.setText(rawType);
                            String company = data.optJSONObject("company").optString("name");
                            tx3.setText(company);
                            String transportMode = data.optString("pickingStatus");
                            tx4.setText(transportMode);
                            String auditStatus = data.optString("auditStatus");
                            tx5.setText(auditStatus);
                            String outStatus = data.optString("outStatus");

                            String applicant = data.optJSONObject("applicant").optString("name");
                            txc1.setText(applicant);
                            String applyTime = DateUtil.getDateToString(Long.valueOf(data.optString("applyTime")));
                            txc2.setText(applyTime);

                            JSONArray productSends = data.optJSONArray("productSends");
                            for (int i = 0; i < productSends.length(); i++) {
                                JSONObject item = productSends.getJSONObject(i);
                                String batchNumber = item.optString("batchNumber");
                                HashMap map = new HashMap();
                                map.put("1", batchNumber);
                                map.put("2", outStatus);
                                tableList.add(map);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {

                        } else {
                            ToastUtil.showToast(ProductOutCheckActivityDetail2.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(ProductOutCheckActivityDetail2.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
        EasyHttp.post(GlobalApi.WareHourse.ProductOut.getByProductSendHeader)
                .params(GlobalApi.WareHourse.productSendHeaderCode, code) //
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
                            ToastUtil.showToast(ProductOutCheckActivityDetail2.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(ProductOutCheckActivityDetail2.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
    }


    List<HashMap<String, Objects>> tableList = new ArrayList<>();
    List<HashMap<String, Objects>> tableList1 = new ArrayList<>();

    public void initTable() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<ProductOutBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = tableList.get(i);
            ProductOutBean userData = new ProductOutBean(String.valueOf(i + 1), map.get("1").toString(), map.get("2").toString());
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
        columns.add(new Column<>("状态", "status"));


        final TableData<ProductOutBean> tableData = new TableData<>("出库单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(ProductOutCheckActivityDetail2.this, R.color.seashell);
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
                    return ContextCompat.getColor(ProductOutCheckActivityDetail2.this, R.color.seashell);
                } else {
                    return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                }
            }
        };
        table1.setSelectFormat(new BaseSelectFormat());
        table1.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table1.setTableData(tableData);
    }

    @OnClick(R.id.bt)
    public void onViewClicked() {
        ActivityUtil.switchTo(this, ProductOutCheckActivity.class);
    }
}
