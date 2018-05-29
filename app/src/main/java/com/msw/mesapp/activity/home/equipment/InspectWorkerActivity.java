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
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.ui.widget.TitlePopup;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.SPUtil;
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

public class InspectWorkerActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_inspect_worker);
        StatusBarUtils.setStatusBarColor(this,R.color.titlecolor);
        ButterKnife.bind(this);

        initData();
        initView();
    }
    //
    public void initData(){
        id = (String) SPUtil.get(InspectWorkerActivity.this, GlobalKey.Login.CODE, id);
        list.clear();
        page = 0;
        EasyHttp.post(GlobalApi.Inspect.Worker.ByPage.PATH)
                .params(GlobalApi.Inspect.Worker.ByPage.updateTime, DateUtil.getCurrentDate2()) //获取当天时间,格式：2018-04-012
                .params(GlobalApi.Inspect.Worker.ByPage.page, String.valueOf(page)) //从第0 业开始获取
                .params(GlobalApi.Inspect.Worker.ByPage.size, "20") //一次获取多少
                .params(GlobalApi.Inspect.Worker.ByPage.sort, "code") //根据code排序
                .params(GlobalApi.Inspect.Worker.ByPage.asc, "1") //升序
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
                            JSONObject data =  jsonObject.getJSONObject("data");
                            JSONArray content = data.getJSONArray("content");

                            for(int i=0;i<content.length();i++){
                                JSONObject content0 = new JSONObject(content.get(i).toString());
                                Map map = new HashMap<>();
                                map.put("1",content0.optString("code"));
                                map.put("2",content0.optString("name")+"车间需要点检");
                                map.put("3",content0.optString("startTime"));
                                map.put("4",content0.optString("endTime"));
                                map.put("5",content0.optString("updateTime"));
                                map.put("6",content0.optString("checkCode"));
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
                            ToastUtil.showToast(InspectWorkerActivity.this, message, ToastUtil.Error);
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(InspectWorkerActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
    }
    //
    public void initView(){
        initTitle();
        initRefreshLayout();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this,R.layout.item_inspect,list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setText(R.id.tv1,s.get("2").toString());
                holder.setText(R.id.tv2,s.get("4").toString());

                holder.setOnClickListener(R.id.bt1, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ActivityUtil.toastShow(InspectWorkerActivity.this, "点击了详情" + position);
                        ActivityUtil.switchTo(InspectWorkerActivity.this,InspectWorkerDetailActivity.class,s);
                        //SPUtil.put(InspectWorkerActivity.this, "inspectnum", "3");
                        //finish();
                    }
                });
                holder.setOnClickListener(R.id.bt2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ActivityUtil.toastShow(InspectWorkerActivity.this, "点击了进入" + position);
                        EasyHttp.post(GlobalApi.Inspect.Worker.CheckHead.PATH)
                                .params(GlobalApi.Inspect.Worker.CheckHead.code, s.get("6").toString())
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
                                            map.put("code",s.get("6").toString()); //把code传过去
                                            ActivityUtil.switchTo(InspectWorkerActivity.this,InspectWorkerJudgeActivity.class,map);
                                            SPUtil.put(InspectWorkerActivity.this, "inspectnum",String.valueOf(num));
                                            SPUtil.put(InspectWorkerActivity.this, "inspectcode",s.get("1"));
                                            SPUtil.put(InspectWorkerActivity.this, "inspecttotal",String.valueOf(num));
                                        } else {
                                            ToastUtil.showToast(InspectWorkerActivity.this, message, ToastUtil.Error);
                                        }
                                    }
                                    @Override
                                    public void onError(ApiException e) {
                                        ToastUtil.showToast(InspectWorkerActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                                    }
                                });
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
    //
    public void initTitle(){
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
            EasyHttp.post(GlobalApi.Inspect.Worker.ByPage.PATH)
                    .params(GlobalApi.Inspect.Worker.ByPage.updateTime, DateUtil.getCurrentDate2()) //获取当天时间,格式：2018-04-012
                    .params(GlobalApi.Inspect.Worker.ByPage.page, String.valueOf(page)) //从第0 业开始获取
                    .params(GlobalApi.Inspect.Worker.ByPage.size, "20") //一次获取多少
                    .params(GlobalApi.Inspect.Worker.ByPage.sort, "code") //根据code排序
                    .params(GlobalApi.Inspect.Worker.ByPage.asc, "1") //升序
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
                                JSONObject data =  jsonObject.getJSONObject("data");
                                JSONArray content = data.getJSONArray("content");

                                for(int i=0;i<content.length();i++){
                                    JSONObject content0 = new JSONObject(content.get(i).toString());
                                    Map map = new HashMap<>();
                                    map.put("1",content0.optString("code"));
                                    map.put("2",content0.optString("name")+"车间需要点检");
                                    map.put("3",content0.optString("startTime"));
                                    map.put("4",content0.optString("endTime"));
                                    map.put("5",content0.optString("updateTime"));
                                    map.put("6",content0.optString("checkCode"));
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
                                ToastUtil.showToast(InspectWorkerActivity.this, message, ToastUtil.Error);
                            }
                        }

                        @Override
                        public void onError(ApiException e) {
                            ToastUtil.showToast(InspectWorkerActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                        }
                    });
        }
    }



}
