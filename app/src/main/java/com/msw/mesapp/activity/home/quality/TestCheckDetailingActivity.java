package com.msw.mesapp.activity.home.quality;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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
import com.msw.mesapp.bean.quality.QualityBean;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
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

/**
 * 化验审核之产品审核之详细
 */
public class TestCheckDetailingActivity extends AppCompatActivity {

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
    @Bind(R.id.tv12)
    TextView tv12;
    @Bind(R.id.tv22)
    TextView tv22;
    @Bind(R.id.tv32)
    TextView tv32;
    @Bind(R.id.tv42)
    TextView tv42;
    @Bind(R.id.tv52)
    TextView tv52;
    @Bind(R.id.tv62)
    TextView tv62;

    String id = "" ; //人id
    String code = ""; //主键
    String batchNumber = ""; //批号
    String judge = ""; //判定
    String number = ""; //数量
    String insideCode = ""; //内部编号
    String productDate = ""; //生产日期
    String testDate = ""; //测试日期

    private String column0[] = {"振实密度","水分","SSA"  ,"pH","D1" ,"D10","D50","D90","D99","筛上物","ppb(Fe)","ppb{Ni)","ppb(Cr)","ppb(Zn)","ppb(总量)","Ni+Co+Mn","Co","Mn","Ni","Na"  ,"Mg"  ,"Ca"  ,"Fe"  ,"Cu"  ,"Cd"  ,"Zn"  ,"S"  ,"Cl-","Zr"  };
    private String column1[] = {"g/cm3"   , "%"  ,"m2/g" , "/","μm","μm","μm","μm","μm", "%"    ,  "/"    , "/"     , "/"     , "/"     , "/"       , "%"      , "%", "%", "%", "ppm", "ppm", "ppm", "ppm", "ppm", "ppm", "ppm", "ppm", "%", "ppm"};
    private String column2[] = {">=2.0"   ,"<=1.0","4.0~7.0","7.0-9.0",">=2.5",">=5.0","9.8~10.5","<=22","<=35","<=0.3","/","/","/","/","<=100","60~64","12.2~13.0","11.6~12.2","37.6~38.8","<=120","<=100","<=100","<=50","<=50","<=20","<=40","<=1000","<=0.03","/"};
    private String column3[] = {">=2.0"   ,"<=1.0","4.0~7.0","7.0-9.0",">=2.5",">=5.0","9.8~10.5","<=22","<=35","<=0.3","/","/","/","/","<=100","60~64","12.2~13.0","11.6~12.2","37.6~38.8","<=120","<=100","<=100","<=50","<=50","<=20","<=40","<=1000","<=0.03","/"};
    private String column4[] = new String[40];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_check_detail);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {

        code = getIntent().getExtras().get("code").toString();
        id = GetCurrentUserIDUtil.currentUserId(this);

        EasyHttp.post(GlobalApi.Quality.RawPresoma.ByCode.PATH)
                .params(GlobalApi.Quality.RawPresoma.ByCode.CODE, code) //主键
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        int code = 1;
                        String message = "出错";
                        JSONObject data ;

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            code = (int) jsonObject.get("code");
                            message = (String) jsonObject.get("message");
                            data = jsonObject.getJSONObject("data");

                            batchNumber = data.optString("batchNumber");
                            judge = data.getJSONObject("judge").optString("name");
                            number = data.optString("number");
                            insideCode = data.optString("insideCode");
                            productDate = DateUtil.getDateToString( Long.valueOf(data.optString("productDate")) );
                            testDate = DateUtil.getDateToString( Long.valueOf(data.optString("testDate")) );

                            for (int i = 1; i <= 40; i++) {
                                String cc = "c" + i;
                                column4[i - 1] = data.optString(cc);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0 ) {
                            initHeadInfo();
                            initTable();
                        } else {
                            ToastUtil.showToast(TestCheckDetailingActivity.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(TestCheckDetailingActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
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
        title.setText("原料未审核");
        add.setText("");
    }

    private void initView() {
        initTitle();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyHttp.post(GlobalApi.Quality.RawPresoma.Audit.PATH)
                        .params(GlobalApi.Quality.RawPresoma.Audit.code, code) //主键
                        .params(GlobalApi.Quality.RawPresoma.Audit.auditorCode, id) //审核人编码
                        .params(GlobalApi.Quality.RawPresoma.Audit.statusCode, "2") //状态编码,2是审核状态
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
                                if (code == 0 ) {
                                    ToastUtil.showToast(TestCheckDetailingActivity.this, message, ToastUtil.Success);
                                    finish();
                                } else {
                                    ToastUtil.showToast(TestCheckDetailingActivity.this, message, ToastUtil.Error);
                                }
                            }
                            @Override
                            public void onError(ApiException e) {
                                ToastUtil.showToast(TestCheckDetailingActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                            }
                        });
            }
        });
    }

    private void initHeadInfo() {
        tv12.setText(batchNumber);
        tv22.setText(judge);
        tv32.setText(number);
        tv42.setText(insideCode);
        tv52.setText(productDate);
        tv62.setText(testDate);
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
        columns.add(new Column("采购标准", new Column<>("2016-11-21", "purchase1"), new Column<>("2017-07-01", "purchase2")));
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
                    return ContextCompat.getColor(TestCheckDetailingActivity.this, R.color.seashell);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //ActivityUtil.switchTo(TestCheckDetailingActivity.this, TestCheckActivity.class);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
