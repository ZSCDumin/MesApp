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
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.StatusBarUtils;
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

public class ProductOutActivityDetail2 extends AppCompatActivity {

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
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.tx3)
    TextView tx3;
    @Bind(R.id.tx4)
    TextView tx4;
    @Bind(R.id.tx5)
    TextView tx5;
    @Bind(R.id.tx6)
    TextView tx6;
    @Bind(R.id.tx7)
    TextView tx7;
    @Bind(R.id.tx8)
    TextView tx8;
    @Bind(R.id.tx9)
    TextView tx9;
    @Bind(R.id.bt)
    Button bt;

    public String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_out_detail2);
        ButterKnife.bind(this);
        initTitle();
        getData();
        initTable();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        title.setText("产品出库");
        add.setVisibility(View.INVISIBLE);
    }

    public void getData() {
        code = getIntent().getExtras().get("code").toString();
        EasyHttp.post(GlobalApi.WareHourse.ProductOut.getByCode)
            .params(GlobalApi.WareHourse.code, code)
            .sign(true)
            .timeStamp(true)//本次请求是否携带时间戳
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject data = jsonObject.optJSONObject("data");
                        String number = data.optString("number");
                        tx1.setText(number);
                        String company = data.optJSONObject("company").optString("name");
                        tx2.setText(company);
                        String applicant = data.optJSONObject("applicant").optString("name");
                        tx3.setText(applicant);
                        String applyTime = DateUtil.getDateToString(data.optString("applyTime"));
                        tx4.setText(applyTime);

                        String auditStatus = data.optString("auditStatus").equals("0") ? "未审核" : "已审核";
                        tx5.setText(auditStatus);
                        if (auditStatus.equals("未审核")) {
                            tx7.setText("");
                            tx6.setText("");
                        }

                        String sender = data.optJSONObject("sender").optString("name");
                        tx8.setText(sender);
                        String sendTime = DateUtil.getDateToString(data.optString("sendTime"));
                        tx9.setText(sendTime);

                        String outStatus = data.optString("outStatus").equals("0") ? "未出库" : "已出库";
                        JSONArray productSends = data.optJSONArray("productSends");

                        for (int i = 0; i < productSends.length(); i++) {
                            JSONObject item = productSends.getJSONObject(i);
                            String batchNumber = item.optString("batchNumber");
                            HashMap map = new HashMap();
                            map.put("0", batchNumber);
                            map.put("1", outStatus);
                            tableList.add(map);
                        }
                        initTable();
                        initTable();
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

    public void initTable() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<ProductOutBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = tableList.get(i);
            ProductOutBean userData = new ProductOutBean(String.valueOf(i + 1), map.get("0").toString(), map.get("1").toString());
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
        columns.add(new Column<>("状态", "outStatus"));

        final TableData<ProductOutBean> tableData = new TableData<>("产品出库", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(ProductOutActivityDetail2.this, R.color.seashell);
                } else {
                    return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                }
            }
        };
        table.setSelectFormat(new BaseSelectFormat());
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table.setTableData(tableData);
    }

    @OnClick({R.id.back, R.id.bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bt:
                ActivityUtil.switchTo(this, ProductOutActivity.class);
                finish();
                break;
        }
    }
}
