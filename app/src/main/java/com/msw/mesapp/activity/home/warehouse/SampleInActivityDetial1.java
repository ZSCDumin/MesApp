package com.msw.mesapp.activity.home.warehouse;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.bean.warehouse.MaterialInBean;
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

public class SampleInActivityDetial1 extends AppCompatActivity {

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
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.tx7)
    TextView tx7;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.bt)
    Button bt;

    String code = "";
    String headcode = "";
    String[] ss = new String[10];
    List<HashMap<String, Objects>> tableList = new ArrayList<>();
    List<String> spinnerList = new ArrayList<>();
    List<String> spinnerList2 = new ArrayList<>();
    @Bind(R.id.ttx1)
    TextView ttx1;
    @Bind(R.id.ttx2)
    TextView ttx2;
    @Bind(R.id.ttx3)
    TextView ttx3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_in_sample_detial1);
        ButterKnife.bind(this);
        initTitle();
        initData();
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
        title.setText("样品入库");
        add.setVisibility(View.INVISIBLE);
    }

    public void initSpinner() {
        //适配器
        ArrayAdapter<String> arr_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
    }

    public void getDepartment() {
        EasyHttp.get("department/getAll")
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
                            JSONArray data = jsonObject.optJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject data0 = new JSONObject(data.get(i).toString());
                                spinnerList.add(data0.optString("name"));
                                spinnerList2.add(data0.optString("code"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            initSpinner();
                        } else {
                            ToastUtil.showToast(SampleInActivityDetial1.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(SampleInActivityDetial1.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
    }

    public void initData() {
        getDepartment();

        code = getIntent().getExtras().get("code").toString();

        EasyHttp.post(GlobalApi.WareHourse.SampleIn.PATH_Header_ByCode)
                .params(GlobalApi.WareHourse.code, code)
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
                            headcode = data.optString("code");

                            JSONObject godownEntryHeaderobj = data.optJSONObject("godownEntryHeader");
                            String batchNumber = "";
                            if (godownEntryHeaderobj != null)
                                batchNumber = godownEntryHeaderobj.optString("batchNumber"); //入库编码

                            JSONObject rawTypeobj = data.optJSONObject("rawType");
                            String rawTypeName = "";
                            if (rawTypeobj != null)
                                rawTypeName = rawTypeobj.optString("name"); //原料名称

                            JSONObject supplierobj = data.optJSONObject("supplier");
                            String supplier = "";
                            if (supplierobj != null)
                                supplier = supplierobj.optString("name"); //发货厂家

                            String DownDate = data.optString("date"); //到货日期
                            String createTime = DateUtil.getDateToString(Long.valueOf(data.optString("createTime"))); //创建时间

                            JSONObject departmentobj = data.optJSONObject("department");
                            String department = "";
                            if (departmentobj != null)
                                department = departmentobj.optString("name"); //收文部门

                            JSONObject createUserobj = data.optJSONObject("createUser");
                            String createUser = "";
                            if (createUserobj != null)
                                createUser = createUserobj.optString("name"); //创建者

                            String status = data.optString("status");
                            String receiptor = data.optJSONObject("receiptor").optString("name");
                            String receiveTime = DateUtil.getDateToString(Long.valueOf(data.optString("receiveTime")));

                            JSONArray sendEntriesArr = data.getJSONArray("godownTestInforms");
                            for (int i = 0; i < sendEntriesArr.length(); i++) {
                                JSONObject godwon0 = new JSONObject(sendEntriesArr.opt(i).toString());
                                HashMap map = new HashMap();
                                map.put("0", godwon0.optString("code"));
                                map.put("1", godwon0.optString("batchNumber")); //批号
                                tableList.add(map);
                            }

                            ss[0] = batchNumber; //入库编码
                            ss[1] = rawTypeName; //原料名称
                            ss[2] = supplier; //发货厂家
                            ss[3] = DownDate; //到货日期
                            ss[4] = createTime; //创建时间
                            ss[5] = department; //受文部门
                            ss[6] = createUser; //创建者
                            ss[7] = status;
                            ss[8] = receiptor;
                            ss[9] = receiveTime;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            initView();
                        } else {
                            ToastUtil.showToast(SampleInActivityDetial1.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(SampleInActivityDetial1.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
    }

    public void initView() {

        tx1.setText(ss[0]);
        tx2.setText(ss[1]);
        tx3.setText(ss[2]);
        tx4.setText(ss[3]);
        tx5.setText(ss[4]);
        tx7.setText(ss[6]);
        ttx1.setText(ss[7]);
        ttx2.setText(ss[8]);
        ttx3.setText(ss[9]);

        initTable();

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                ToastUtil.showToast(SampleInActivityDetial1.this, "选择了" + arg2, ToastUtil.Confusion);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();
                for (int i = 0; i < tableList.size(); i++) {
                    Map mapItem = (Map) tableList.get(i);
                    map.put("batchNumber" + String.valueOf(i), mapItem.get("1").toString());
                }
                map.put("batchLen", String.valueOf(tableList.size()));
                map.put("headcode", headcode);
                ActivityUtil.switchTo(SampleInActivityDetial1.this, SampleInActivityDetial1Scan.class, map); //扫码验证
            }
        });

    }

    public void initTable() {
        //smartTable 的初始化
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<MaterialInBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            // MaterialInBean userData = new MaterialInBean("12132", "45465", "565",new MaterialInBean.ChildData("ceshi"));
            Map map = (Map) tableList.get(i);
            MaterialInBean userData = new MaterialInBean(String.valueOf(i + 1), map.get("1").toString(), map.get("0").toString(), "");
            testData.add(userData);
        }

        WindowManager wm = this.getWindowManager();
        int screenWith = wm.getDefaultDisplay().getWidth();
        table.getConfig().setMinTableWidth(screenWith - 20); //设置最小宽度 屏幕宽度

        List<Column> columns = new ArrayList<>();
        Column column0 = new Column<>("序号", "id");
        column0.setFixed(true);
        column0.setAutoCount(false);
        columns.add(column0);
        columns.add(new Column<>("批号", "batchNumber"));
        columns.add(new Column<>("编号", "unit"));
        columns.add(new Column<>("", "weight"));

        final TableData<MaterialInBean> tableData = new TableData<>("入库单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(SampleInActivityDetial1.this, R.color.seashell);
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
