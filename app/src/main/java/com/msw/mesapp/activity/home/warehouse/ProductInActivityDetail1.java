package com.msw.mesapp.activity.home.warehouse;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.google.gson.JsonObject;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.bean.warehouse.ProductInBean;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
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
 * 未入库详情
 */
public class ProductInActivityDetail1 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.godown_code_tv)
    TextView godownCodeTv;
    @Bind(R.id.department_tv)
    TextView departmentTv;
    @Bind(R.id.rawType_tv)
    TextView rawTypeTv;
    @Bind(R.id.totalWeight_tv)
    TextView totalWeightTv;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.member_tv)
    TextView memberTv;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.bt)
    Button bt;

    public String code = "";
    public String headcode = "";
    public String godowner = "";
    public String productType = "";

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x011:
                    rawTypeTv.setText(productType);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_detail1);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        initData();
        initTitle();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("产品入库");
        add.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.bt)
    public void onViewClicked() {
        Map map = new HashMap();
        for (int i = 0; i < tableList.size(); i++) {
            Map mapItem = (Map) tableList.get(i);
            map.put("batchNumber" + i, mapItem.get("0").toString());
        }
        map.put("batchLen", String.valueOf(tableList.size()));
        map.put("headcode", headcode);
        map.put("godowner", godowner);
        ActivityUtil.switchTo(this, ProductInActivityDetail1Scan.class, map); //扫码验证
    }

    public void getProductType(String code) {
        EasyHttp.post(GlobalApi.WareHourse.ProductIn.getTypeByCode)
            .params("code", code)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(ProductInActivityDetail1.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        productType = jsonObject.optJSONObject("data").optString("name");
                        handler.sendEmptyMessage(0x011);
                        ToastUtil.showToast(ProductInActivityDetail1.this, "获取数据成功", ToastUtil.Success);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    public void initData() {
        //获取第一块数据
        EasyHttp.post(GlobalApi.WareHourse.ProductIn.getByCode)
            .params(GlobalApi.WareHourse.code, code)
            .sign(true)
            .timeStamp(true)//本次请求是否携带时间戳
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject data = jsonObject.optJSONObject("data");
                        headcode = data.optString("code");
                        String batchNumber = data.optString("batchNumber");
                        String payTime = DateUtil.getDateToString(data.optString("payTime"));
                        String payer = data.optJSONObject("payer").optString("name");
                        String department = data.optJSONObject("department").optString("name");
                        String model = data.optString("model");
                        getProductType(model);
                        String weight = data.optString("weight");
                        godowner = data.optString("godowner");
                        godownCodeTv.setText(batchNumber);
                        departmentTv.setText(department);
                        totalWeightTv.setText(weight);
                        memberTv.setText(payer);
                        timeTv.setText(payTime);
                        JSONArray productGodowns = data.optJSONArray("productGodowns");
                        for (int i = 0; i < productGodowns.length(); i++) {

                            JSONObject item = productGodowns.getJSONObject(i);
                            String batchNumber1 = item.optString("batchNumber");
                            String weight1 = item.optString("weight");
                            HashMap map = new HashMap();
                            map.put("0", batchNumber1);
                            map.put("1", weight1);
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
        List<ProductInBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = tableList.get(i);
            ProductInBean userData = new ProductInBean(String.valueOf(i + 1), map.get("0").toString(), map.get("1").toString(), "KG");
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
        columns.add(new Column<>("重量", "weight"));
        columns.add(new Column<>("单位", "unit"));
        final TableData<ProductInBean> tableData = new TableData<>("产品入库", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(ProductInActivityDetail1.this, R.color.seashell);
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
