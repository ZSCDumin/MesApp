package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
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

public class InspectWorkerDetailActivity extends AppCompatActivity {


    String[] s6 = new String[6];
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bt)
    Button bt;

    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_detail);
        ButterKnife.bind(this);

        initData();
        initView();
    }
    public void initData(){
        list.clear();
        s6[0] = getIntent().getExtras().get("1").toString(); //code
        s6[1] = getIntent().getExtras().get("2").toString(); //name
        s6[2] = getIntent().getExtras().get("3").toString(); //starttime
        s6[3] = getIntent().getExtras().get("4").toString(); //endTime
        s6[4] = getIntent().getExtras().get("5").toString(); //updateTime
        s6[5] = getIntent().getExtras().get("6").toString(); //checkCode

        EasyHttp.post(GlobalApi.Inspect.Worker.CheckHead.PATH)
                .params(GlobalApi.Inspect.Worker.CheckHead.code, s6[5])
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


                                     Map map;
                                     map = new HashMap(); map.put("1","车间号："); map.put("2",s6[1]); list.add(map);
                                     map = new HashMap(); map.put("1","设备名称："); map.put("2",data.optString("name")); list.add(map);
                                     map = new HashMap(); map.put("1","巡检内容："); map.put("2",data.optString("checkInfo")); list.add(map);
                                     map = new HashMap(); map.put("1","巡检标准："); map.put("2",data.optString("checkRequire")); list.add(map);
                                     map = new HashMap(); map.put("1","发起人："); map.put("2",data.optString("checkInitiator")); list.add(map);
                                     map = new HashMap(); map.put("1","发起时间："); map.put("2",s6[3]);list.add(map);
                                     map = new HashMap(); map.put("1","截止时间："); map.put("2",s6[4]);list.add(map);

                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                                 if (code == 0) {
                                     adapter.notifyDataSetChanged(); //显示添加的数据
                                 } else {
                                     ToastUtil.showToast(InspectWorkerDetailActivity.this, message, ToastUtil.Error);
                                 }
                             }
                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(InspectWorkerDetailActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
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
        title.setText("任务详情");
        add.setVisibility(View.INVISIBLE);
    }

    public void initView() {
        initTitle();
        initRefreshLayout();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EasyHttp.post(GlobalApi.Inspect.Worker.CheckHead.PATH)
                        .params(GlobalApi.Inspect.Worker.CheckHead.code, s6[5])
                        .sign(true)
                        .timeStamp(true)//本次请求是否携带时间戳
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String result) {
                                int code = 1;
                                String message = "出错";
                                int num =1;

                                try {
                                    JSONObject jsonObject = new JSONObject(result);

                                    code = (int) jsonObject.get("code");
                                    message = (String) jsonObject.get("message");
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    num = Integer.valueOf(data.optString("num"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (code == 0) {
                                    Map map = new HashMap();
                                    map.put("code",s6[5]); //把code传过去
                                    ActivityUtil.switchTo(InspectWorkerDetailActivity.this, InspectWorkerJudgeActivity.class,map);
                                    SPUtil.put(InspectWorkerDetailActivity.this, "inspectnum",String.valueOf(num));
                                    SPUtil.put(InspectWorkerDetailActivity.this, "inspectcode",s6[0]);
                                    SPUtil.put(InspectWorkerDetailActivity.this, "inspecttotal",String.valueOf(num));
                                } else {
                                    ToastUtil.showToast(InspectWorkerDetailActivity.this, message, ToastUtil.Error);
                                }
                            }
                            @Override
                            public void onError(ApiException e) {
                                ToastUtil.showToast(InspectWorkerDetailActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                            }
                        });

            }
        });

    }

    private void initRefreshLayout() {
//        refreshLayout.setVisibility(View.GONE);
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(1500);
//                initData();
//            }
//        });
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
