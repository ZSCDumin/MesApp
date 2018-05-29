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
import com.msw.mesapp.bean.warehouse.ProductInBean;
import com.msw.mesapp.bean.warehouse.ProductOutBean;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
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

public class ProductOutActivityDetail1 extends AppCompatActivity {

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
    @Bind(R.id.txc3)
    TextView txc3;
    @Bind(R.id.txc4)
    TextView txc4;
    @Bind(R.id.txc5)
    TextView txc5;
    @Bind(R.id.cancle_bt)
    Button cancle_bt;
    @Bind(R.id.product_out_bt)
    Button product_out_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_out_detail1);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.cancle_bt, R.id.product_out_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.cancle_bt:
                finish();
                break;
            case R.id.product_out_bt:
                Map map = new HashMap();
                for (int i = 0; i < tableList.size(); i++) {
                    Map mapItem = (Map) tableList.get(i);
                    map.put("batchNumber" + i, mapItem.get("0").toString());
                }
                map.put("batchLen", String.valueOf(tableList.size()));
                map.put("code", code);
                ActivityUtil.switchTo(this, ProductOutActivityDetail1Scan.class, map); //扫码验证
                break;
        }
    }

    public String code = "";

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
                            String rawType = data.optJSONObject("rawType").optString("name");
                            tx2.setText(rawType);
                            String company = data.optJSONObject("company").optString("name");
                            tx3.setText(company);
                            String transportMode = data.optString("transportMode");
                            tx4.setText(transportMode);
                            String applicant = data.optJSONObject("applicant").optString("name");
                            txc1.setText(applicant);
                            String applyTime = data.optString("applyTime");
                            txc2.setText(applyTime);
                            String auditStatus = data.optString("auditStatus");
                            txc3.setText(auditStatus);

                            String outStatus = data.optString("outStatus");
                            String sender = data.optString("sender");
                            txc4.setText(sender);
                            String sendTime = data.optString("sendTime");
                            txc5.setText(sendTime);

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
                    return ContextCompat.getColor(ProductOutActivityDetail1.this, R.color.seashell);
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
