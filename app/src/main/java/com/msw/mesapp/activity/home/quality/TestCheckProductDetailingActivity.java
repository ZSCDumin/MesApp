package com.msw.mesapp.activity.home.quality;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
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
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.bean.quality.QualityBean;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import sam.android.utils.viewhelper.CommonExpandableListAdapter;

/**
 * 产品审核》未审核
 */
public class TestCheckProductDetailingActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    TextView add;

    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.expandablelistview)
    ExpandableListView expandablelistview;

    String id = ""; //人id
    String code = ""; //主键

    String batchNumber = ""; //批号
    String judge = ""; //判定
    String number = ""; //数量
    String testDate = ""; //测试日期
    String auditor = "";//审核人
    String auditDate = ""; //审核时间

    CommonExpandableListAdapter<ChildData, GroupData> commonExpandableListAdapter;
    List<ChildData>  temp = new ArrayList<>();


    private String column0[] = {"振实密度", "水分", "SSA", "PH", "Li2CO3", "LiOH", "总LiOH", "D1", "D10", "D50", "D90", "D99", "粒度宽度系数", "ppb(Fe)", "ppb{Ni)", "ppb(Cr)", "ppb(Zn)", "ppb(总量)", "Co", "Mn", "Ni", "Li", "Co", "Mn", "Ni", "Na", "Mg", "Ca", "Fe", "Cu", "Zn", "S", "Al", "0.1C首次放电容量", "0.1C首次效率", "1C首次放电容量","主原料"};
    private String column1[] = {"g/cm3", "ppm", "m2/g", "/", "%", "%", "10ppm", "µm", "µm", "µm", "µm", "µm", "粒度宽度系数", "Fe", "Ni", "Cr", "Zn", "总量", "mol%", "mol%", "mol%", "%", "%", "%", "%", "ppm", "ppm", "ppm", "ppm", "ppm", "ppm", "ppm", "ppm", "mAh/g", "%", "mAh/g",""};
    private String column2[] = {"≥2.00", "≤500", "0.20～0.40", "≤11.80", "", "", "≤100", "", "≥6.00", "11.00～14.00", "≤30.00", "", "", "", "", "", "", "≤50", "", "", "", "7.0±0.5", "12.20±1.0", "11.4±1.0", "36.2±1.0", "≤200", "≤200", "≤200", "≤50", "≤50", "≤50", "", "1000±300", "", "", "",""};
    private String column3[] = {"2.3-2.7", "≤200", "0.22～0.38", "≤11.80", "≤0.25", "≤0.20", "≤120", "≥3.00", "≥5.00", "11.30～13.3", "≤30.00", "≤40.00", "", "", "", "", "", "≤50", "19.7±0.5", "19.9±0.5", "60.4±0.5", "7.0±0.5", "12.20±1.0", "11.4±1.0", "36.2±1.0", "≤200", "≤200", "≤200", "≤30", "≤20", "≤30", "≤1500", "1000±200", "≥177.5", "≥88.0", "≥162",""};
    private String column4[] = new String[40];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_check_process_detail);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {

        code = getIntent().getExtras().get("code").toString();
        id = (String) SPUtil.get(TestCheckProductDetailingActivity.this, GlobalKey.Login.CODE, id);

        EasyHttp.post(GlobalApi.Quality.Product.ByCode.PATH)
                .params(GlobalApi.Quality.Product.ByCode.CODE, code) //主键
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        int code = 1;
                        String message = "出错";
                        JSONObject data;

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            code = (int) jsonObject.get("code");
                            message = (String) jsonObject.get("message");
                            data = jsonObject.getJSONObject("data");

                            batchNumber = data.optString("batchNumber");
                            judge = data.getJSONObject("judge").optString("name");
                            number = data.optString("number");
                            testDate = DateUtil.getDateToString(Long.valueOf(data.optString("testDate")));
                            auditor = data.getJSONObject("auditor").optString("name");
                            auditDate = DateUtil.getDateToString(Long.valueOf(data.optString("auditDate")));

                            for (int i = 1; i <= 40; i++) {
                                String cc = "p" + i;
                                column4[i - 1] = data.optString(cc);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            ChildData childData = new ChildData();
                            childData.childName1 = "批号"; childData.childName2=batchNumber; temp.add(childData);childData = new ChildData();
                            childData.childName1 = "判定"; childData.childName2=judge; temp.add(childData);childData = new ChildData();
                            childData.childName1 = "数量"; childData.childName2=number; temp.add(childData);childData = new ChildData();
                            childData.childName1 = "检测日期"; childData.childName2=testDate; temp.add(childData);childData = new ChildData();
                            //childData.childName1 = "审核人"; childData.childName2=auditor; temp.add(childData);childData = new ChildData();
                           // childData.childName1 = "审核时间"; childData.childName2=auditDate; temp.add(childData);childData = new ChildData();


                            initHeadInfo();
                            initTable();
                        } else {
                            ToastUtil.showToast(TestCheckProductDetailingActivity.this, message, ToastUtil.Success);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(TestCheckProductDetailingActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("产品未审核");
        add.setText("");
    }

    private void initView() {
        initTitle();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyHttp.post(GlobalApi.Quality.Product.Audit.PATH)
                        .params(GlobalApi.Quality.Product.Audit.code, code) //主键
                        .params(GlobalApi.Quality.Product.Audit.auditorCode, id) //审核人编码
                        .params(GlobalApi.Quality.Product.Audit.statusCode, "2") //状态编码,2是审核状态
                        .sign(true)
                        .timeStamp(true)//本次请求是否携带时间戳
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String result) {
                                int code = 1;
                                String message = "出错";

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    code = (int) jsonObject.get("code");
                                    message = (String) jsonObject.get("message");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (code == 0) {
                                    ToastUtil.showToast(TestCheckProductDetailingActivity.this, message, ToastUtil.Success);
                                    finish();
                                } else {
                                    ToastUtil.showToast(TestCheckProductDetailingActivity.this, message, ToastUtil.Error);
                                }
                            }

                            @Override
                            public void onError(ApiException e) {
                                ToastUtil.showToast(TestCheckProductDetailingActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                            }
                        });
            }
        });
    }

    private void initHeadInfo() {
        expandablelistview.setAdapter(commonExpandableListAdapter = new CommonExpandableListAdapter<ChildData, GroupData>(this,R.layout.item_detail_adapter_child, R.layout.item_detail_adapter_group ) {
            @Override
            protected void getChildView(ViewHolder holder, int groupPositon, int childPositon, boolean isLastChild, ChildData data) {

                TextView v1 = holder.getView(R.id.tv3); TextView v2 = holder.getView(R.id.tv3);
                v1.setText(data.childName1);  v2.setText(data.childName2);
//                ((TextView)holder.getView(R.id.tv1)).setText(data.childName1);
//                ((TextView)holder.getView(R.id.tv2)).setText(data.childName2);
            }

            @Override
            protected void getGroupView(ViewHolder holder, int groupPositon, boolean isExpanded, GroupData data) {
                TextView textView = holder.getView(R.id.grouptxt);//分组名字
                ImageView arrowImage = holder.getView(R.id.groupIcon);//分组箭头
                textView.setText(data.groupName);
                //根据分组是否展开设置自定义箭头方向
                arrowImage.setImageResource(isExpanded?R.mipmap.ic_arrow_expanded :R.mipmap.ic_arrow_uexpanded);
            }
        });
        expandablelistview.setAdapter(commonExpandableListAdapter);
        GroupData groupData = new GroupData(); groupData.groupName = "展开/收起";
        commonExpandableListAdapter.getGroupData().add(groupData);


        commonExpandableListAdapter.getChildrenData().add(temp);
        commonExpandableListAdapter.notifyDataSetChanged();
    }

    private void initTable() {
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        final List<QualityBean> testData = new ArrayList<>();

        for (int i = 0; i < column0.length; i++) {
            QualityBean userData = new QualityBean(column0[i], column1[i], column2[i], column3[i], column4[i], new QualityBean.ChildData("测试" + i));
            testData.add(userData);
        }


        WindowManager wm = this.getWindowManager();
        int screenWith = wm.getDefaultDisplay().getWidth();
        table.getConfig().setMinTableWidth(screenWith); //设置最小宽度 屏幕宽度

        List<Column> columns = new ArrayList<>();
        Column column0 = new Column<>("检测项目", "name");
        column0.setFixed(true);
        column0.setAutoCount(true);
        columns.add(column0);
        columns.add(new Column<>("单位", "unit"));
        columns.add(new Column("三级控制标准", new Column<>("标准1", "purchase1"), new Column<>("标准2", "purchase2")));
        columns.add(new Column<>("结果", "result"));

        final TableData<QualityBean> tableData = new TableData<>("成品分析数据", testData, columns);
        tableData.setShowCount(true);


        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(TestCheckProductDetailingActivity.this, R.color.seashell);
                } else {
                    return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                }
            }
        };
        table.setSelectFormat(new BaseSelectFormat());
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);
        table.setTableData(tableData);


    }


    private void getData() {
    }

    /**
     * 分组数据
     */
    class GroupData
    {
        String groupName;
    }

    /**
     * 孩子数据
     */
    class ChildData
    {
        String childName1;
        String childName2;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //ActivityUtil.switchTo(TestCheckDetailingActivity.this, TestCheckActivity.class);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
