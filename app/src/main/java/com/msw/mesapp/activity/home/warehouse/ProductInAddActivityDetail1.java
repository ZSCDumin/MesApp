package com.msw.mesapp.activity.home.warehouse;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.bean.warehouse.MaterialInBean;
import com.msw.mesapp.bean.warehouse.ProductGodown;
import com.msw.mesapp.bean.warehouse.ProductGodownBean;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductInAddActivityDetail1 extends AppCompatActivity {

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
    @Bind(R.id.rawType_sp)
    Spinner rawTypeSp;
    @Bind(R.id.totalweight_tv)
    TextView totalWeightTv;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.auditor_tv)
    TextView auditorTv;
    @Bind(R.id.audit_time_tv)
    TextView auditTimeTv;
    @Bind(R.id.back_bt)
    Button backBt;
    @Bind(R.id.confirm_bt)
    Button confirmBt;

    public int totalWeight = 0;

    List<HashMap<String, Objects>> tableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_add_detail1);
        ButterKnife.bind(this);
        getData();
        initTitle();
        initTable();
        initView();
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
        title.setText("成品入库");
        add.setVisibility(View.INVISIBLE);
    }

    private ArrayAdapter<String> arr_adapter;
    private List<String> data_list;

    public void initView() {
        data_list = new ArrayList<>();
        data_list.add("类型1");
        data_list.add("类型2");
        data_list.add("类型3");

        totalWeightTv.setText(String.valueOf(totalWeight));
        auditorTv.setText(SPUtil.get(this, GlobalKey.Login.CODE, "").toString());
        auditTimeTv.setText(DateUtil.getCurrentDate2());
        godownCodeTv.setText("1111");
        departmentTv.setText("制造部门");
        //适配器
        arr_adapter = new ArrayAdapter<String>(ProductInAddActivityDetail1.this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        rawTypeSp.setAdapter(arr_adapter);
    }

    public void getData() {

        String length = getIntent().getExtras().get("batchLen").toString();
        for (int i = 0; i < Integer.valueOf(length); i++) {
            HashMap map = new HashMap();
            String batchNumber = getIntent().getExtras().get("batchNumber" + i).toString();
            totalWeight += Integer.valueOf(batchNumber.split("-")[1]);
            map.put("1", batchNumber);
            map.put("2", batchNumber.split("-")[1]);
            tableList.add(map);
        }
    }

    public void initTable() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<MaterialInBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = tableList.get(i);
            MaterialInBean userData = new MaterialInBean(String.valueOf(i + 1), map.get("1").toString(), "KG", map.get("2").toString());
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


        final TableData<MaterialInBean> tableData = new TableData<>("产品入库单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(ProductInAddActivityDetail1.this, R.color.seashell);
                } else {
                    return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                }
            }
        };
        table.setSelectFormat(new BaseSelectFormat());
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table.setTableData(tableData);
    }

    @OnClick({R.id.back, R.id.back_bt, R.id.confirm_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back_bt:
                finish();
                break;
            case R.id.confirm_bt:
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .build();
                List<ProductGodown> list = new ArrayList<>();
                for (int i = 0; i < tableList.size(); i++) {
                    Map map = tableList.get(i);
                    ProductGodown productGodown = new ProductGodown(map.get("1").toString(), map.get("2").toString());
                    list.add(productGodown);
                }
                ProductGodownBean productGodownBean = new ProductGodownBean(rawTypeSp.getSelectedItem().toString(), departmentTv.getText().toString(), String.valueOf(totalWeight), SPUtil.get(this, GlobalKey.Login.CODE, "").toString(), list);
                Gson gson = new Gson();
                //使用Gson将对象转换为json字符串
                String json = gson.toJson(productGodownBean);

                RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                        , json);
                Request request = new Request.Builder()
                        .url(GlobalApi.BASEURL + GlobalApi.WareHourse.ProductIn.add_godown_info)//请求的url
                        .post(requestBody)
                        .build();
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    String result = response.body().string();
                    Log.i("TAG", result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
