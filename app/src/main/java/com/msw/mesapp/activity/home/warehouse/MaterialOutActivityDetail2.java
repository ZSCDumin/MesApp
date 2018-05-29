package com.msw.mesapp.activity.home.warehouse;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.msw.mesapp.bean.warehouse.MaterialOutBean;
import com.msw.mesapp.utils.ActivityUtil;
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

public class MaterialOutActivityDetail2 extends AppCompatActivity {

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
    @Bind(R.id.txc3)
    TextView txc3;
    @Bind(R.id.txc4)
    TextView txc4;
    @Bind(R.id.bt)
    Button bt;

    public String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_out_detail2);
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
        title.setText("原料出库");
        add.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.bt)
    public void onViewClicked() {
        ActivityUtil.switchTo(this, MaterialOutActivity.class);
    }

    public void initData() {
        //获取第一块数据
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.PATH_CODE)
                .params(GlobalApi.WareHourse.code, code)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject data = jsonObject.optJSONObject("data");
                            String department = data.optJSONObject("department").optString("name").toString();
                            JSONArray contents = data.optJSONArray("pickingApplies");
                            for (int i = 0; i < contents.length(); i++) {
                                JSONObject item = contents.optJSONObject(i);
                                String rawType = item.optJSONObject("rawType").optString("name");
                                String batchNumber = item.optString("batchNumber");
                                String unit = item.optString("unit");
                                String weight = item.optString("weight");
                                HashMap map = new HashMap();
                                map.put("0", rawType);
                                map.put("1", batchNumber);
                                map.put("2", unit);
                                map.put("3", weight);
                                tableList.add(map);
                            }
                            tx1.setText(department);
                            String applyDate = data.optString("applyDate");
                            tx2.setText(applyDate);
                            String auditStatus = data.optString("auditStatus");
                            tx3.setText(auditStatus);
                            String processManage = data.optJSONObject("processManage").optString("name").toString();
                            tx4.setText(processManage);
                            String pickingStatus = data.optString("pickingStatus");
                            tx5.setText(pickingStatus);
                            initTable();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ApiException e) {

                    }
                });

        //初始化第三块数据
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.PATH_ApplyHeader)
                .params(GlobalApi.WareHourse.pickingApplyHeaderCode, code)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray data = jsonObject.optJSONArray("data");

                            for(int i =0;i<data.length();i++){
                                JSONObject item = data.getJSONObject(i);

                                String audit_result = item.optString("auditResult");
                                String suggestion = item.optString("note");
                                String member = item.optJSONObject("auditor").optString("name");
                                String time = item.optString("auditTime");
                                txc1.setText(audit_result);
                                txc2.setText(suggestion);
                                txc3.setText(member);
                                txc4.setText(time);
                            }

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
        List<MaterialOutBean> testData = new ArrayList<>();
        for (int i = 0; i < tableList.size(); i++) {
            Map map = tableList.get(i);
            MaterialOutBean userData = new MaterialOutBean(String.valueOf(i + 1), map.get("0").toString(), map.get("1").toString(), map.get("2").toString(), map.get("3").toString());
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
        columns.add(new Column<>("原料类型", "rawType"));
        columns.add(new Column<>("批号", "batchNumber"));
        columns.add(new Column<>("单位", "unit"));
        columns.add(new Column<>("重量", "weight"));

        final TableData<MaterialOutBean> tableData = new TableData<>("原料审核单", testData, columns);
        tableData.setShowCount(false);

        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false).setShowTableTitle(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(MaterialOutActivityDetail2.this, R.color.seashell);
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
