package com.msw.mesapp.activity.home.warehouse;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    @Bind(R.id.department_sp)
    Spinner departmentSp;
    @Bind(R.id.rawType_sp)
    Spinner rawTypeSp;
    @Bind(R.id.totalweight_tv)
    TextView totalweightTv;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.auditor_tv)
    TextView auditorTv;
    @Bind(R.id.audit_time_tv)
    TextView auditTimeTv;
    @Bind(R.id.back_bt)
    Button backBt;
    @Bind(R.id.submit_bt)
    Button confirmBt;

    public String message = "";
    public int totalWeight = 0;
    List<HashMap<String, Objects>> tableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in_add_detail1);
        ButterKnife.bind(this);
        initTitle();
        getData();
        getRawType();
        getDepartment();
        initTable();
        initView();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        title.setTextSize(18);
        title.setText("成品入库");
        add.setVisibility(View.INVISIBLE);
    }

    private ArrayAdapter<String> arr_adapter;
    private ArrayAdapter<String> arr_adapter1;
    private List<String> data_list;
    private List<String> data_list_code;
    private List<String> data_list1;
    private List<String> data_list1_code;

    public void initView() {
        totalweightTv.setText(String.valueOf(totalWeight));
        auditorTv.setText(SPUtil.get(this, GlobalKey.Login.CODE, "").toString());
        auditTimeTv.setText(DateUtil.getCurrentDate2());
    }

    public void getRawType() {
        data_list = new ArrayList<>();
        data_list_code = new ArrayList<>();
        EasyHttp.post(GlobalApi.WareHourse.ProductIn.getAllRawType)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray content = jsonObject.optJSONObject("data").optJSONArray("content");
                            for (int i = 0; i < content.length(); i++) {
                                JSONObject item = content.getJSONObject(i);
                                String code = item.optString("code");
                                String rawType = item.optString("name");
                                data_list_code.add(code);
                                data_list.add(rawType);
                            }
                            arr_adapter = new ArrayAdapter<>(ProductInAddActivityDetail1.this, android.R.layout.simple_spinner_item, data_list);
                            arr_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            rawTypeSp.setAdapter(arr_adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ToastUtil.showToast(ProductInAddActivityDetail1.this, "获取产品类型数据成功！", 1);
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(ProductInAddActivityDetail1.this, "获取产品类型数据成功！", 1);
                    }
                });
    }

    public void getDepartment() {
        data_list1 = new ArrayList<>();
        data_list1_code = new ArrayList<>();
        EasyHttp.post(GlobalApi.WareHourse.ProductIn.getAllDepartment)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray content = jsonObject.optJSONObject("data").optJSONArray("content");
                            for (int i = 0; i < content.length(); i++) {
                                JSONObject item = content.getJSONObject(i);
                                String code = item.optString("code");
                                String deparment = item.optString("name");
                                data_list1_code.add(code);
                                data_list1.add(deparment);
                            }
                            arr_adapter1 = new ArrayAdapter<>(ProductInAddActivityDetail1.this, android.R.layout.simple_spinner_item, data_list1);
                            arr_adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            departmentSp.setAdapter(arr_adapter1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ToastUtil.showToast(ProductInAddActivityDetail1.this, "获取部门数据成功！", 1);
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(ProductInAddActivityDetail1.this, "获取部门数据成功！", 1);
                    }
                });


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


    @OnClick({R.id.back, R.id.back_bt, R.id.submit_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back_bt:
                finish();
                break;
            case R.id.submit_bt:
                submit();
                break;
        }
    }

    public void submit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        List<ProductGodown> list = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = tableList.get(i);
            ProductGodown productGodown = new ProductGodown(map.get("1").toString(), map.get("2").toString());
            list.add(productGodown);
        }
        ProductGodownBean productGodownBean = new ProductGodownBean(data_list_code.get(rawTypeSp.getSelectedItemPosition()), data_list_code.get(departmentSp.getSelectedItemPosition()), String.valueOf(totalWeight), SPUtil.get(this, GlobalKey.Login.CODE, "").toString(), list);
        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        final String json = gson.toJson(productGodownBean);
        Log.i("TAG", json);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(GlobalApi.BASEURL + GlobalApi.WareHourse.ProductIn.add_godown_info)//请求的url
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    message = jsonObject.optString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ToastUtil.showToast(this, message, 1);
    }
}
