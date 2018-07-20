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
import com.msw.mesapp.bean.warehouse.MaterialInBean;
import com.msw.mesapp.utils.ActivityUtil;
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

public class MaterialInActivityDetail1 extends AppCompatActivity {


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
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tx4)
    TextView tx4;
    @Bind(R.id.tx7)
    TextView tx7;

    String code = "";
    String headcode = "";
    String[] ss = new String[10];
    List<HashMap<String, Objects>> tableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_in_detail1);
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
        title.setText("原料待收货");
        add.setVisibility(View.INVISIBLE);
    }

    public void initData() {
        code = getIntent().getExtras().get("code").toString();

        EasyHttp.post(GlobalApi.WareHourse.MaterialIn.PATH_Send_Header_ByCode)
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
                        code = jsonObject.optInt("code");
                        message = jsonObject.optString("message");
                        JSONObject data = jsonObject.optJSONObject("data");

                        headcode = data.optString("code");
                        String contractNumber = data.optString("contractNumber"); //合同号

                        JSONObject supplierobj = data.optJSONObject("supplier");
                        String supplier = supplierobj.optString("name"); //发货厂家

                        JSONObject senderobj = data.optJSONObject("sender");
                        String senderName = senderobj.optString("name"); //发货人

                        String sendDate = data.optString("sendDate"); //发货日期
                        String contact = data.optString("contact"); //联系电话
                        String materia_name = data.optString("name"); //物料名称
                        String weight = data.optString("weight"); //总量
                        JSONArray sendEntriesArr = data.getJSONArray("sendEntries");
                        for (int i = 0; i < sendEntriesArr.length(); i++) {
                            JSONObject godwon0 = new JSONObject(sendEntriesArr.opt(i).toString());
                            HashMap map = new HashMap();
                            map.put("0", godwon0.optString("code"));
                            map.put("1", godwon0.optString("batchNumber").trim()); //批号
                            map.put("2", godwon0.optString("unit")); //单位
                            map.put("3", godwon0.optString("weight")); //重量
                            map.put("4", godwon0.optString("status")); //状态:0未收;1已收
                            tableList.add(map);
                        }

                        ss[0] = contractNumber; //合同号
                        ss[1] = supplier; //发货厂家
                        ss[2] = senderName; //发货人
                        ss[3] = sendDate; //发货时间
                        ss[4] = contact; //电话
                        ss[5] = materia_name; //物料名称
                        ss[6] = weight; //总量

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (code == 0) {
                        initView();
                    } else {
                        ToastUtil.showToast(MaterialInActivityDetail1.this, message, ToastUtil.Error);
                    }
                }

                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(MaterialInActivityDetail1.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
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
        initTable();

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
                ActivityUtil.switchTo(MaterialInActivityDetail1.this, MaterialInActivityDetail1Scan.class, map); //扫码验证
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
            MaterialInBean userData = new MaterialInBean(String.valueOf(i + 1), map.get("1").toString(), map.get("2").toString(), map.get("3").toString());
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

        final TableData<MaterialInBean> tableData = new TableData<>("入库单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(MaterialInActivityDetail1.this, R.color.seashell);
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
