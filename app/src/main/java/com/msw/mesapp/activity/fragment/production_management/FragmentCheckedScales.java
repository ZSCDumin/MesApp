package com.msw.mesapp.activity.fragment.production_management;

/**
 * Created by Mr.Meng on 2017/12/31.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msw.mesapp.R;
import com.msw.mesapp.activity.home.production_management.CheckScalesManagement.CheckScalesCheckDetails3;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
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


public class FragmentCheckedScales extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();
    private String eqiupmentCode = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.viewpaper_checkscale, container, false);
        ButterKnife.bind(this, view);
        eqiupmentCode = getActivity().getIntent().getExtras().get("code").toString();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        getData();
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
                        map.put("code", s.get("1").toString());
                        map.put("name", s.get("4").toString());
                        ActivityUtil.switchTo(getActivity(), CheckScalesCheckDetails3.class, map);
                    }
                });
                holder.setText(R.id.tv1, s.get("2").toString());
                holder.setText(R.id.tv2, s.get("3").toString());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public void getData() {
        EasyHttp.post(GlobalApi.ProductManagement.CheckScale.getByConfirm)
            .params("confirm", "1")
            .params("equipmentCode", eqiupmentCode)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(getActivity(), "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray data = jsonObject.optJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String code = item.optString("code");
                            String dutyCode = item.optJSONObject("dutyCode").optString("name");
                            String equipmentName = item.optJSONObject("equipmentCode").optString("name");
                            String auditTime = DateUtil.getDateToString1(item.optLong("auditTime"));
                            Map<String, Object> map = new HashMap<>();
                            map.put("1", code);
                            map.put("2", auditTime);
                            map.put("3", dutyCode);
                            map.put("4", equipmentName);
                            list.add(map);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}