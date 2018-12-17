package com.msw.mesapp.activity.fragment.warehouse;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.home.warehouse.MaterialOutActivityDetail1;
import com.msw.mesapp.activity.home.warehouse.ProductOutActivityDetail1;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mr.Meng on 2017/12/31.
 * 成品待出库
 */
public class FragmentProductOut1 extends Fragment {

    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.classicsFooter)
    ClassicsFooter classicsFooter;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.imgsearch)
    ImageView imgsearch;

    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.viewpaper_testchecking, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initData() {
        list.clear();
        EasyHttp.post(GlobalApi.WareHourse.ProductOut.getByOutStatusAndNumberLike)
                .params(GlobalApi.WareHourse.outStatus, "0")
                .params("number", "")
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.optJSONObject(i);
                                String code = item.optString("code");
                                String number = item.optString("number");
                                String createDate = item.optString("createDate");
                                Map map = new HashMap<>();
                                map.put("1", number); //出库单编号
                                map.put("2", createDate); //开单日期
                                map.put("3", code);
                                list.add(map);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(getActivity(), GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
    }

    private void initView() {
        initRefreshLayout();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(getActivity(), R.layout.item_materialin, list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", s.get("3").toString());
                        ActivityUtil.switchTo(getActivity(), ProductOutActivityDetail1.class, map);
                    }
                });
                holder.setText(R.id.tv1, s.get("1").toString());
                holder.setText(R.id.tv2, s.get("2").toString());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化滑动列表
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
                refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}