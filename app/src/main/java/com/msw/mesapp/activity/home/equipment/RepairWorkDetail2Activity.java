package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepairWorkDetail2Activity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.et)
    EditText et;
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.line)
    LinearLayout line;

    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();

    String code = "";
    int lineflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_work_detail2);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    public void initData() {
        list.clear();
        code = getIntent().getExtras().get("code").toString();
        final String[] departmentcode = {""};
        final String[] eqArchivecode = {""};
        final String[] productLinecode = {""};
        final String[] applicationDescription = {""};
        final String[] applicationTime = {""};
        final String[] orderTime = {""};
        final String[] repairManname = {""};
        final String[] finishTime = {""};
        final String[] repairmanDescription = {""};
        final String[] evaluatorname = {""};
        final String[] evaluationname = {""};

        EasyHttp.post(GlobalApi.Repair.PATH_ByCode)
                .params("code", code)
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
                            JSONObject data = jsonObject.getJSONObject("data");

                            JSONObject department = data.getJSONObject("department");
                            departmentcode[0] = department.optString("code"); //部门

                            JSONObject eqArchive = data.getJSONObject("eqArchive");
                            eqArchivecode[0] = eqArchive.optString("code"); //设备

                            JSONObject productLine = data.getJSONObject("productLine");
                            productLinecode[0] = productLine.optString("code"); //生产线

                            applicationDescription[0] = data.optString("applicationDescription"); //故障描述
                            applicationTime[0] = data.optString("applicationTime"); //申请时间
                            applicationTime[0] = DateUtil.getDateToString(Long.valueOf(applicationTime[0]));


                            JSONObject flag = data.getJSONObject("flag");
                            int flagcode = flag.optInt("code"); //是否接单
                            String flagname = flag.optString("name"); //是否接单
                            lineflag = flagcode;

                            if (flagcode >= 1) {
                                orderTime[0] = data.optString("orderTime"); //接单时间
                                orderTime[0] = DateUtil.getDateToString(Long.valueOf(orderTime[0]));
                                JSONObject repairMan = data.getJSONObject("repairMan");
                                repairManname[0] = repairMan.optString("name"); //维修人
                            }
                            if (flagcode >= 2) {
                                JSONObject repairMan = data.getJSONObject("repairMan");
                                repairManname[0] = repairMan.optString("name"); //维修人
                                finishTime[0] = data.optString("finishTime"); //维修时间
                                finishTime[0] = DateUtil.getDateToString(Long.valueOf(finishTime[0]));
                                repairmanDescription[0] = data.optString("repairmanDescription"); //维修描述
                            }
                            if (flagcode >= 3) {
                                JSONObject evaluator = data.getJSONObject("evaluator");
                                evaluatorname[0] = evaluator.optString("name"); //评价人
                                JSONObject evaluation = data.getJSONObject("evaluation");
                                evaluationname[0] = evaluation.optString("name"); //评价满意度
                            }


                            Map listmap;
                            listmap = new HashMap<>();
                            listmap.put("1", "部门:");
                            listmap.put("2", departmentcode[0]);
                            list.add(listmap);
                            listmap = new HashMap<>();
                            listmap.put("1", "设备名称:");
                            listmap.put("2", eqArchivecode[0]);
                            list.add(listmap);
                            listmap = new HashMap<>();
                            listmap.put("1", "生产线:");
                            listmap.put("2", productLinecode[0]);
                            list.add(listmap);
                            listmap = new HashMap<>();
                            listmap.put("1", "故障描述:");
                            listmap.put("2", applicationDescription[0]);
                            list.add(listmap);
                            listmap = new HashMap<>();
                            listmap.put("1", "申请时间:");
                            listmap.put("2", applicationTime[0]);
                            list.add(listmap);
                            if (flagcode >= 1) {
                                listmap = new HashMap<>();
                                listmap.put("1", "接单时间:");
                                listmap.put("2", orderTime[0]);
                                list.add(listmap);
                                listmap = new HashMap<>();
                                listmap.put("1", "维修人:");
                                listmap.put("2", repairManname[0]);
                                list.add(listmap);
                            }
                            if (flagcode >= 2) {
                                listmap = new HashMap<>();
                                listmap.put("1", "维修时间:");
                                listmap.put("2", finishTime[0]);
                                list.add(listmap);
                                listmap = new HashMap<>();
                                listmap.put("1", "维修描述:");
                                listmap.put("2", repairmanDescription[0]);
                                list.add(listmap);
                            }
                            if (flagcode >= 3) {
                                listmap = new HashMap<>();
                                listmap.put("1", "评价人:");
                                listmap.put("2", evaluatorname[0]);
                                list.add(listmap);
                                listmap = new HashMap<>();
                                listmap.put("1", "评价满意度:");
                                listmap.put("2", evaluationname[0]);
                                list.add(listmap);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            if(lineflag != 1) line.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged(); //显示添加的数据
                        } else {
                            ToastUtil.showToast(RepairWorkDetail2Activity.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(RepairWorkDetail2Activity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
    }

    public void initView() {
        initTitle();
        initRefreshLayout();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getText().toString().length() == 0) { et.setError("输入不能为空"); return; }

                String repairmanDescription = et.getText().toString();

                EasyHttp.post(GlobalApi.Repair.Work.PATH)
                        .params(GlobalApi.Repair.Work.code, code)
                        .params(GlobalApi.Repair.Work.repairmanDescription, repairmanDescription)
                        .sign(true)
                        .timeStamp(true)//本次请求是否携带时间戳
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String result) {
                                int code = 1;
                                String message = "出错";

                                try {
                                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
                                    code = (int) jsonObject.get("code");
                                    message = (String) jsonObject.get("message");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (code == 0) {
                                    finish();
                                    ActivityUtil.switchTo(RepairWorkDetail2Activity.this,RepairWorkActivity.class);
                                } else {
                                    ToastUtil.showToast(RepairWorkDetail2Activity.this, message, ToastUtil.Error);
                                }
                            }
                            @Override
                            public void onError(ApiException e) {
                                ToastUtil.showToast(RepairWorkDetail2Activity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                            }
                        });
            }
        });

    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                ActivityUtil.switchTo(RepairWorkDetail2Activity.this,RepairWorkActivity.class);
            }
        });
        title.setText("维修进度");
        add.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始化(配置)下拉刷新组件
     */
    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1500);
                initData();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_repair_report_detail, list) {
            @Override
            protected void convert(ViewHolder holder, Map s, final int position) {
                holder.setText(R.id.tv1,s.get("1").toString());
                holder.setText(R.id.tv2,s.get("2").toString());


            }
        };
        recyclerView.setAdapter(adapter);
    }

}
