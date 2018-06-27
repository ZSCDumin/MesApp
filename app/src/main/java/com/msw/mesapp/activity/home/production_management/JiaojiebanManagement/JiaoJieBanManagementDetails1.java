package com.msw.mesapp.activity.home.production_management.JiaojiebanManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ActivityUtil;
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

public class JiaoJieBanManagementDetails1 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();
    @Bind(R.id.classicsFooter)
    ClassicsFooter classicsFooter;
    @Bind(R.id.fresh)
    SmartRefreshLayout fresh;
    private String code = "";
    private String name = "";
    private int totalPages = 0;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shai_wang_management_details1);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        name = getIntent().getExtras().get("name").toString();
        intiView();
        initRefreshLayout();
        getData(1);
    }

    public void loadMoreData() {
        page = page + 1;
        if (page > totalPages) classicsFooter.setLoadmoreFinished(true);
        else {
            getData(0);
        }
    }

    public void getData(int flag) {
        if (flag == 1) {
            list.clear();
            page = 0;
        }
        EasyHttp.post(GlobalApi.ProductManagement.Jiaojieban.getAllByPage)
            .params("page", page + "")
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(JiaoJieBanManagementDetails1.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.optJSONObject("data");
                        JSONArray content = data.optJSONArray("content");
                        totalPages = data.optInt("totalPages");
                        for (int i = 0; i < content.length(); i++) {
                            JSONObject item = content.getJSONObject(i);
                            String name = item.optString("name");
                            String code = item.optString("code");
                            Map<String, Object> map = new HashMap<>();
                            map.put("1", name);
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

        title.setText(name);
        add.setImageResource(R.mipmap.add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_jiaojieban, list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", s.get("3").toString());
                        Log.i("TAG", s.get("3").toString());
                        ActivityUtil.switchTo(JiaoJieBanManagementDetails1.this, JiaoJieBanManagementDetails3.class, map);
                    }
                });
                holder.setText(R.id.tv1, s.get("1").toString());
                holder.setText(R.id.tv2, "");
                if (s.get("2").toString().equals("1"))
                    holder.setImageResource(R.id.imageView, R.mipmap.checked);
                else
                    holder.setImageResource(R.id.imageView, R.mipmap.unchecked);
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
                map.put("code", code);
                Log.i("TAG", code);
                ActivityUtil.switchTo(JiaoJieBanManagementDetails1.this, JiaoJieBanManagementDetails2.class, map);
                break;
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
