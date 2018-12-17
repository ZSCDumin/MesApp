package com.msw.mesapp.activity.home.warehouse;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

//原料入库-》已经入库
public class MaterialInActivityDetail2 extends AppCompatActivity {


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
    @Bind(R.id.tx5)
    TextView tx5;
    @Bind(R.id.tx6)
    TextView tx6;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.back)
    ImageView back;

    String code = "";
    String[] ss = new String[10];
    List<HashMap<String, Objects>> tableList = new ArrayList<>();
    @Bind(R.id.tx4)
    TextView tx4;
    @Bind(R.id.tx7)
    TextView tx7;
    @Bind(R.id.tx8)
    TextView tx8;
    @Bind(R.id.tx9)
    TextView tx9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_in_detail2);
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
        title.setText("原料已收货");
        add.setVisibility(View.INVISIBLE);
    }

    public void initData() {
        code = getIntent().getExtras().get("code").toString();
        Log.i("TAG", code);
        EasyHttp.post(GlobalApi.WareHourse.MaterialIn.PATH_GoDown_Header_ByCode)
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

                        String headcode = data.optString("code");
                        String batchNumber = data.optString("batchNumber"); //入库编码

                        JSONObject sendEntryHeaderobj = data.optJSONObject("sendEntryHeader");
                        String contractNumber = "";
                        if (sendEntryHeaderobj != null)
                            contractNumber = sendEntryHeaderobj.optString("contractNumber"); //发货编码

                        JSONObject supplierobj = data.optJSONObject("supplier");
                        String supplier = "";
                        if (supplierobj != null)
                            supplier = supplierobj.optString("name"); //发货厂家

                        JSONObject rawTypeobj = data.optJSONObject("rawType");
                        String rawName = "";
                        if (rawTypeobj != null)
                            rawName = rawTypeobj.optString("name"); //原料类型

                        String weight = data.optString("weight"); //总量
                        String date = data.optString("date"); //到货日期
                        String createTime = DateUtil.getDateToString(Long.valueOf(data.optString("createTime"))); //制单时间

                        JSONObject createUserobj = data.optJSONObject("createUser");
                        String createUser = createUserobj.optString("name"); //制单工人

                        String status = data.optString("status");//样品状态

                        JSONArray godownEntriesAyy = data.getJSONArray("godownEntries");
                        for (int i = 0; i < godownEntriesAyy.length(); i++) {
                            JSONObject godwon0 = new JSONObject(godownEntriesAyy.opt(i).toString());
                            HashMap map = new HashMap();
                            map.put("0", godwon0.optString("code"));
                            map.put("1", godwon0.optString("batchNumber")); //批号
                            map.put("2", godwon0.optString("unit")); //单位
                            map.put("3", godwon0.optString("weight")); //重量
                            map.put("4", godwon0.optString("testResult")); //化验结果:0未化验; 1合格; 2不合格
                            tableList.add(map);
                        }

                        ss[0] = batchNumber; //入库单号
                        ss[1] = contractNumber; //发货编码
                        ss[2] = supplier; //发货厂家
                        ss[3] = rawName; //原料类型
                        ss[4] = weight; //总量
                        ss[5] = date; //到货日期
                        ss[6] = createTime; //制单日期
                        ss[7] = createUser; //制单工人
                        ss[8] = status.equals("0") ? "未入库" : "已入库";

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (code == 0) {
                        initView();
                    } else {
                        ToastUtil.showToast(MaterialInActivityDetail2.this, message, ToastUtil.Error);
                    }
                }

                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(MaterialInActivityDetail2.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                }
            });

    }

    public void initView() {

        tx1.setText(ss[0]);
        tx2.setText(ss[1]);
        tx3.setText(ss[2]);
        tx4.setText(ss[3]);
        tx5.setText(ss[4]);
        tx6.setText(ss[5]);
        tx7.setText(ss[6]);
        tx8.setText(ss[7]);
        tx9.setText(ss[8]);
        initTable();
    }

    public void initTable() {
        //smartTable 的初始化

        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        List<MaterialInBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            // MaterialInBean userData = new MaterialInBean("12132", "45465", "565",new MaterialInBean.ChildData("ceshi"));
            Map map = (Map) tableList.get(i);
            MaterialInBean userData = new MaterialInBean(String.valueOf(i + 1), map.get("1").toString(), map.get("2").toString(), map.get("3").toString());
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
        columns.add(new Column<>("单位", "unit"));
        columns.add(new Column<>("数量", "weight"));

        final TableData<MaterialInBean> tableData = new TableData<>("入库单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(MaterialInActivityDetail2.this, R.color.seashell);
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
