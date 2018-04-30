package com.msw.mesapp.activity.home.quality;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
 * 化验审核之产品审核之详细
 */
public class TestCheckProcessedDetailActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    TextView add;
    @Bind(R.id.table)
    SmartTable table;
    @Bind(R.id.expandablelistview)
    ExpandableListView expandablelistview;

    String ctype = ""; //类型，1是预混，2是粉碎粒度，3是碳酸锂，4扣电
    String id = ""; //人id
    String code = ""; //主键

    String batchNumber = ""; //批号
    String type = "" ;//类型
    String judge = ""; //判定
    String number = ""; //数量
    String furnaceNum = ""; //炉号
    String lithiumSoluble = ""; //可溶锂
    String productDate = ""; //生产日期
    String testDate = ""; //测试日期
    String supplier = ""; //主原料厂家
    String auditor = ""; //审核人
    String auditDate = ""; //审核时间
    String publisher = ""; //发布人
    String publishDate = ""; //发布时间


    CommonExpandableListAdapter<ChildData, GroupData> commonExpandableListAdapter;
    List<ChildData>  temp = new ArrayList<>();


    private String column0[] = {"振实密度", "水分", "SSA", "pH", "D1", "D10", "D50", "D90", "D99", "筛上物", "ppb(Fe)", "ppb{Ni)", "ppb(Cr)", "ppb(Zn)", "ppb(总量)", "Ni+Co+Mn", "Co", "Mn", "Ni", "Na", "Mg", "Ca", "Fe", "Cu", "Cd", "Zn", "S", "Cl-", "Zr"};
    private String column1[] = {"g/cm3", "%", "m2/g", "/", "μm", "μm", "μm", "μm", "μm", "%", "/", "/", "/", "/", "/", "%", "%", "%", "%", "ppm", "ppm", "ppm", "ppm", "ppm", "ppm", "ppm", "ppm", "%", "ppm"};
    private String column2[] = {">=2.0", "<=1.0", "4.0~7.0", "7.0-9.0", ">=2.5", ">=5.0", "9.8~10.5", "<=22", "<=35", "<=0.3", "/", "/", "/", "/", "<=100", "60~64", "12.2~13.0", "11.6~12.2", "37.6~38.8", "<=120", "<=100", "<=100", "<=50", "<=50", "<=20", "<=40", "<=1000", "<=0.03", "/"};
    private String column3[] = {">=2.0", "<=1.0", "4.0~7.0", "7.0-9.0", ">=2.5", ">=5.0", "9.8~10.5", "<=22", "<=35", "<=0.3", "/", "/", "/", "/", "<=100", "60~64", "12.2~13.0", "11.6~12.2", "37.6~38.8", "<=120", "<=100", "<=100", "<=50", "<=50", "<=20", "<=40", "<=1000", "<=0.03", "/"};
    private String column4[] = new String[40];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_check_process_detail2);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {

        code = getIntent().getExtras().get("code").toString();
        ctype = getIntent().getExtras().get("type").toString();
        id = (String) SPUtil.get(TestCheckProcessedDetailActivity.this, GlobalKey.Login.CODE, id);

        String Path = "";
        switch (ctype){
            case "1": Path = GlobalApi.Quality.ProcessPremix.ByCode.PATH;break;
            case "2": Path = GlobalApi.Quality.ProcessSize.ByCode.PATH;break;
            case "3": Path = GlobalApi.Quality.ProcessLithium.ByCode.PATH;break;
            case "4": Path = GlobalApi.Quality.ProcessBuckle.ByCode.PATH;break;
            default:Path = GlobalApi.Quality.ProcessPremix.ByCode.PATH;break;
        }

        EasyHttp.post(Path)
                .params(GlobalApi.Quality.ProcessPremix.ByCode.CODE, code) //主键
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

                            batchNumber = RidnullStr(data.optString("batchNumber"));
                            //judge = data.getJSONObject("judge").optString("name");
                            //number = data.optString("number");

                            if(!ctype.equals("1")) {
                                furnaceNum = RidnullStr(data.optString("furnaceNum"));
                            }

                            if(ctype.equals("1")) {
                                type = RidnullStr(data.optString("type"));
                                lithiumSoluble = RidnullStr(data.optString("lithiumSoluble"));
                                supplier = RidnullStr(data.getJSONObject("supplier").optString("name"));
                                //productDate = RidnullStr(DateUtil.getDateToString(Long.valueOf(data.optString("productDate"))));
                            }

                            testDate = RidnullStr(DateUtil.getDateToString(Long.valueOf(data.optString("testDate"))));
                            auditor = RidnullStr(data.getJSONObject("auditor").optString("name"));
                            auditDate = RidnullStr(DateUtil.getDateToString(Long.valueOf(data.optString("auditDate"))));
                            //publisher = RidnullStr(data.getJSONObject("publisher").optString("name"));
                            //publishDate = RidnullStr(DateUtil.getDateToString(Long.valueOf(data.optString("publishDate"))));


                            for (int i = 1; i <= 40; i++) {
                                String cc = "pc" + i;
                                column4[i - 1] = data.optString(cc);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            ChildData childData = new ChildData();
                            childData.childName1 = "批号"; childData.childName2=batchNumber; temp.add(childData);childData = new ChildData();
                            //childData.childName1 = "判定"; childData.childName2=judge; temp.add(childData);childData = new ChildData();
                            //childData.childName1 = "数量"; childData.childName2=number; temp.add(childData);childData = new ChildData();

                            if(!ctype.equals("1")) {
                                childData.childName1 = "炉号";childData.childName2 = furnaceNum;temp.add(childData);childData = new ChildData();
                            }
                            if(ctype.equals("1")) {
                                childData.childName1 = "类型";childData.childName2 = type;temp.add(childData);childData = new ChildData();
                                childData.childName1 = "可溶锂";childData.childName2 = lithiumSoluble;temp.add(childData);childData = new ChildData();
                                childData.childName1 = "主原料厂家";childData.childName2 = supplier;temp.add(childData);childData = new ChildData();
                                //childData.childName1 = "生产日期"; childData.childName2=productDate; temp.add(childData);childData = new ChildData();
                            }
                            childData.childName1 = "检测日期"; childData.childName2=testDate; temp.add(childData);childData = new ChildData();

                            childData.childName1 = "审核人"; childData.childName2=auditor; temp.add(childData);childData = new ChildData();
                            childData.childName1 = "审核时间"; childData.childName2=auditDate; temp.add(childData);childData = new ChildData();
                            //childData.childName1 = "发布人"; childData.childName2=publisher; temp.add(childData);childData = new ChildData();
                            //childData.childName1 = "发布时间"; childData.childName2=publishDate; temp.add(childData);childData = new ChildData();

                            initHeadInfo();
                            initTable();
                        } else {
                            ToastUtil.showToast(TestCheckProcessedDetailActivity.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(TestCheckProcessedDetailActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
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
        title.setText("制程数据");
        add.setText("");
    }

    private void initView() {
        initTitle();
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
        columns.add(new Column("控制标准", new Column<>("2016-11-21", "purchase1"), new Column<>("2017-07-01", "purchase2")));
        columns.add(new Column<>("结果", "result"));

        final TableData<QualityBean> tableData = new TableData<>("金驰622前驱体质量控制点表", testData, columns);
        tableData.setShowCount(true);


        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextColor(getResources().getColor(android.R.color.white));

        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.blue))).setShowXSequence(false).setShowYSequence(false);
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(TestCheckProcessedDetailActivity.this, R.color.seashell);
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

    private String RidnullStr(String str){
        if(str==null) return "";
        return str;
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //ActivityUtil.switchTo(TestCheckDetailingActivity.this, TestCheckActivity.class);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
