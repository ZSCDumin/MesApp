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
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
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

public class ProductOutCheckActivityDetail1 extends AppCompatActivity {


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
    @Bind(R.id.tx5)
    TextView tx5;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.txc1)
    TextView txc1;
    @Bind(R.id.txc2)
    TextView txc2;
    @Bind(R.id.suggestion_et)
    EditText suggestionEt;
    @Bind(R.id.disagree_bt)
    Button disagreeBt;
    @Bind(R.id.agree_bt)
    Button agreeBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_out_check_detail);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initTable();
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
                            ToastUtil.showToast(ProductOutCheckActivityDetail1.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(ProductOutCheckActivityDetail1.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
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
        EasyHttp.post(GlobalApi.WareHourse.ProductOut.updateAuditStatusByCode) //获取发货单
                .params(GlobalApi.WareHourse.code, code)
                .params(GlobalApi.WareHourse.auditStatus, auditStatus)
                .params(GlobalApi.WareHourse.note, note)
                .params(GlobalApi.WareHourse.auditorCode, auditorCode)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ToastUtil.showToast(ProductOutCheckActivityDetail1.this, "成功！", 1);
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(ProductOutCheckActivityDetail1.this, "失败！", 1);
                    }
                });
    }

    List<HashMap<String, Objects>> tableList = new ArrayList<>();

    public void initTable() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<ProductOutBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = (Map) tableList.get(i);
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


        final TableData<ProductOutBean> tableData = new TableData<>("入库单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(ProductOutCheckActivityDetail1.this, R.color.seashell);
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
