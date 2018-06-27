package com.msw.mesapp.activity.home.equipment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.ui.widget.TitlePopup;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InspectWorkerActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    String id = "";
    List<Map<String, Object>> list = new ArrayList<>();
    TitlePopup titlePopup;
    private CommonAdapter adapter;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    if (refreshLayout.isRefreshing()) {
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);//设置不刷新
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_worker);
        ButterKnife.bind(this);
        StatusBarUtils.setStatusBarColor(this, R.color.titlecolor);
        initData();
        initView();
    }

    public void initData() {
        id = GetCurrentUserIDUtil.currentUserId(this);
        list.clear();
        EasyHttp.post(GlobalApi.Inspect.Worker.ByPage.PATH)
            .params(GlobalApi.Inspect.Worker.ByPage.code, GetCurrentUserIDUtil.currentUserId(this)) //获取当天时间,格式：2018-04-012
            .params(GlobalApi.Inspect.Worker.ByPage.status, "0") //从第0 业开始获取
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    String message = "出错";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String tallyTaskHeaderCode = item.optString("code");
                            JSONArray tallyTasks = item.optJSONArray("tallyTasks");
                            for (int j = 0; j < tallyTasks.length(); j++) {
                                JSONObject tallyTask = tallyTasks.getJSONObject(j);
                                String tallyTaskCode = tallyTask.optString("code");
                                String status = tallyTask.optString("status");
                                String content = tallyTask.optJSONObject("guide").optString("content");
                                String standard = tallyTask.optJSONObject("guide").optString("standard");
                                String imageCode = tallyTask.optJSONObject("guide").optString("imageCode");
                                String createTime = DateUtil.getDateToString(Long.valueOf(tallyTask.optString("createTime")));
                                Map map = new HashMap();
                                map.put("1", content);
                                map.put("2", createTime);
                                map.put("3", standard);
                                map.put("4", id);
                                map.put("5", tallyTaskCode);
                                map.put("6", tallyTaskHeaderCode);
                                map.put("7", imageCode);
                                map.put("8", status);
                                list.add(map);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(InspectWorkerActivity.this, message, ToastUtil.Error);
                    }
                }

                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(InspectWorkerActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                }
            });
    }

    public void initView() {
        initTitle();
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_inspect, list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setIsRecyclable(false);
                holder.setText(R.id.tv1, s.get("1").toString());
                holder.setText(R.id.tv2, s.get("2").toString());

                if (s.get("8").toString().equals("1")) {
                    holder.getView(R.id.ll).setEnabled(false);
                    holder.getView(R.id.bt1).setBackgroundColor(Color.BLACK);
                    holder.getView(R.id.bt2).setBackgroundColor(Color.BLACK);
                } else {
                    holder.setOnClickListener(R.id.bt1, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) { //查看详情
                            ActivityUtil.switchTo(InspectWorkerActivity.this, InspectWorkerDetailActivity.class, s);
                        }
                    });
                    holder.setOnClickListener(R.id.bt2, new View.OnClickListener() { //进入工作
                        @Override
                        public void onClick(View view) {
                            ActivityUtil.switchTo(InspectWorkerActivity.this, InspectWorkerJudgeActivity.class, s);
                            finish();
                        }
                    });
                }
            }
        };
        recyclerView.setAdapter(adapter);
        initRefreshLayout();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        add.setImageResource(R.mipmap.icon_list);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titlePopup.show(view);
            }
        });
        add.setVisibility(View.GONE);
    }

    /**
     * 初始化(配置)下拉刷新组件
     */
    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("fresh", "..........");
                initData();
            }
        });
    }

}
