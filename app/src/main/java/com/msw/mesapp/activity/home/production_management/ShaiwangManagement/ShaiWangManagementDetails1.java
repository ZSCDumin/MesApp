package com.msw.mesapp.activity.home.production_management.ShaiwangManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShaiWangManagementDetails1 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();
    private String shakerName = "";
    private String shakerCode = "";
    @Bind(R.id.classicsFooter)
    ClassicsFooter classicsFooter;
    @Bind(R.id.fresh)
    SmartRefreshLayout fresh;
    private int page = 0;
    private int totalPages = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shai_wang_management_details1);
        ButterKnife.bind(this);
        shakerName = getIntent().getExtras().get("shakerName").toString();
        shakerCode = getIntent().getExtras().get("shakerCode").toString();
        intiView();
        initRefreshLayout();
        getData(1);
    }

    public void getData(int flag) {
        if (flag == 1) {
            list.clear();
            page = 0;
        }
        EasyHttp.post(GlobalApi.ProductManagement.ShaiwangCheck.getByShakerCodeLikeByPage)
            .params(GlobalApi.ProductManagement.ShaiwangCheck.shakerCode, shakerName)
            .params(GlobalApi.ProductManagement.ShaiwangCheck.sortFieldName, "inspectorTime")
            .params(GlobalApi.ProductManagement.ShaiwangCheck.asc, "0")
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(ShaiWangManagementDetails1.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.optJSONObject("data");
                        JSONArray content = data.optJSONArray("content");
                        for (int i = 0; i < content.length(); i++) {
                            JSONObject item = content.getJSONObject(i);
                            String code = item.optString("code");
                            String inspectorTime = item.optString("inspectorTime");
                            inspectorTime = DateUtil.getDateToString(inspectorTime);
                            Map<String, Object> map = new HashMap<>();
                            map.put("1", inspectorTime);
                            map.put("2", code);
                            list.add(map);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    public void intiView() {

        title.setText(shakerName + "筛网");
        add.setImageResource(R.mipmap.add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_materialin, list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setText(R.id.tv1, s.get("1").toString());
                holder.setText(R.id.tv2, "");
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //查看详情
                        Map<String, Object> map = new HashMap<>();
                        map.put("shakerCode", shakerCode);
                        map.put("shakerName", shakerName);
                        map.put("code", s.get("2").toString());
                        ActivityUtil.switchTo(ShaiWangManagementDetails1.this, ShaiWangManagementDetails3.class, map);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.back, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                Map<String, Object> map = new HashMap<>();
                map.put("shakerCode", shakerCode);
                map.put("shakerName", shakerName);
                ActivityUtil.switchTo(ShaiWangManagementDetails1.this, ShaiWangManagementDetails2.class, map);
                break;
        }
    }

    public void loadMoreData() {
        page = page + 1;
        if (page > totalPages) {
            classicsFooter.setLoadmoreFinished(true);
        } else {
            getData(0);
        }
    }

    private void initRefreshLayout() {
        fresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1500);
                getData(1);
                classicsFooter.setLoadmoreFinished(false);
            }
        });
        fresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);//传入false表示加载失败
                loadMoreData();
            }
        });
    }
}
