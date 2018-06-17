package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.ui.widget.TitlePopup;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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

public class InspectMonitorActivity extends AppCompatActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.classicsFooter)
    ClassicsFooter classicsFooter;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    TitlePopup titlePopup;
    private RecyclerView.Adapter adapter;

    String id = "";
    int page = 0; //获取数据的第几页
    int totalPages = 0; //总共几页
    int totalElements = 0; //总共多少条数据

    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_monitor);
        StatusBarUtils.setStatusBarColor(this, R.color.titlecolor);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    //
    public void initData() {
        id = GetCurrentUserIDUtil.currentUserId(this);
        list.clear();
        page = 0;
        EasyHttp.post(GlobalApi.Inspect.Monitor.ByPage.PATH)
                .params(GlobalApi.Inspect.Monitor.ByPage.examState, "1") //获取已巡检但未审核
                .params(GlobalApi.Inspect.Monitor.ByPage.page, String.valueOf(page)) //从第0 业开始获取
                .params(GlobalApi.Inspect.Monitor.ByPage.size, "20") //一次获取多少
                .params(GlobalApi.Inspect.Monitor.ByPage.sort, "code") //根据code排序
                .params(GlobalApi.Inspect.Monitor.ByPage.asc, "0") //升序
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
                            JSONArray content = data.getJSONArray("content");

                            for (int i = 0; i < content.length(); i++) {
                                JSONObject content0 = new JSONObject(content.get(i).toString());
                                Map map = new HashMap<>();

                                map.put("01", content0.optString("code"));
                                map.put("02", content0.optString("checkHeadCode"));
                                map.put("03", "完成时间：" + content0.optString("time"));
                                map.put("04", "巡检人：" + content0.optString("checkPerson"));

                                for (int j = 1; j <= 14; j++) {
                                    map.put(String.valueOf(j) + "1", content0.optString("state" + j));
                                    map.put(String.valueOf(j) + "2", content0.optString("abnormal" + j));
                                }

                                list.add(map);
                            }

                            totalPages = data.optInt("totalPages");
                            totalElements = data.optInt("totalElements");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            adapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showToast(InspectMonitorActivity.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(InspectMonitorActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
    }

    //
    public void initView() {
        initTitle();
        initRefreshLayout();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_inspect2, list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setText(R.id.tv1, s.get("04").toString());
                holder.setText(R.id.tv2, s.get("03").toString());
                holder.setOnClickListener(R.id.bt2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ActivityUtil.toastShow(InspectMonitorActivity.this, "点击了进入" + position);
                        ActivityUtil.switchTo(InspectMonitorActivity.this, InspectMonitorJudgeActivity.class, s);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    //
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
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1500);
                initData();
                classicsFooter.setLoadmoreFinished(false);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失败
                getData();
            }
        });
    }

    private void getData() {
        page++;
        if (page > totalPages) classicsFooter.setLoadmoreFinished(true);
        else {
            EasyHttp.post(GlobalApi.Inspect.Monitor.ByPage.PATH)
                    .params(GlobalApi.Inspect.Monitor.ByPage.examState, "1") //获取没有审核的数据
                    .params(GlobalApi.Inspect.Monitor.ByPage.page, String.valueOf(page)) //从第0 业开始获取
                    .params(GlobalApi.Inspect.Monitor.ByPage.size, "20") //一次获取多少
                    .params(GlobalApi.Inspect.Monitor.ByPage.sort, "code") //根据code排序
                    .params(GlobalApi.Inspect.Monitor.ByPage.asc, "1") //升序
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
                                JSONArray content = data.getJSONArray("content");

                                for (int i = 0; i < content.length(); i++) {
                                    JSONObject content0 = new JSONObject(content.get(i).toString());
                                    Map map = new HashMap<>();

                                    map.put("01", content0.optString("code"));
                                    map.put("02", content0.optString("checkHeadCode"));
                                    map.put("03", "完成时间：" + content0.optString("time"));
                                    map.put("04", "巡检人：" + content0.optString("checkPerson"));
                                    //map.put("05","审核人："+content0.optString("examPerson"));
                                    //map.put("06",content0.optString("examState"));
                                    //map.put("06","巡检日期："+content0.optString("examDate"));

                                    for (int j = 1; j <= 14; j++) {
                                        map.put(String.valueOf(j) + "1", content0.optString("state" + j));
                                        map.put(String.valueOf(j) + "2", content0.optString("abnormal" + j));
                                    }

                                    list.add(map);
                                }

                                totalPages = data.optInt("totalPages");
                                totalElements = data.optInt("totalElements");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (code == 0) {
                                adapter.notifyDataSetChanged(); //显示添加的数据
                            } else {
                                ToastUtil.showToast(InspectMonitorActivity.this, message, ToastUtil.Error);
                            }
                        }

                        @Override
                        public void onError(ApiException e) {
                            ToastUtil.showToast(InspectMonitorActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                        }
                    });
        }
    }

}
