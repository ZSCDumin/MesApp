package com.msw.mesapp.activity.fragment.warehouse;

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
import android.widget.ImageView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.home.warehouse.MaterialOutActivityDetail1;
import com.msw.mesapp.activity.home.warehouse.MaterialOutCheckActivityDetail2;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 已审核
 */
public class FragmentMaterialOutCheck2 extends Fragment {

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

    int page = 0; //获取数据的第几页
    int totalPages = 0; //总共几页
    int totalElements = 0; //总共多少条数据


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
        page = 0;
        EasyHttp.post(GlobalApi.WareHourse.MaterialOut.PATH_AUDIT)
            .params(GlobalApi.WareHourse.auditStatus, "2")
            .params(GlobalApi.WareHourse.page, String.valueOf(page)) //从第0 业开始获取
            .params(GlobalApi.WareHourse.size, "20") //一次获取多少
            .params(GlobalApi.WareHourse.sort, "code") //根据code排序
            .params(GlobalApi.WareHourse.asc, "1") //升序
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    int code = 1;
                    String message = "出错";

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        code = jsonObject.optInt("code");
                        message = jsonObject.optString("message");
                        JSONArray data = jsonObject.optJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject content0 = data.optJSONObject(i);
                            String applyDate = DateUtil.getDateToString1(content0.optLong("applyDate")); //获取申请时间
                            String department = content0.optJSONObject("applicant").optJSONObject("department").optString("name");
                            String applicant = content0.optJSONObject("applicant").optString("name"); //
                            String number = department + "-" + applicant;
                            String code1 = content0.optString("code");
                            Map listmap = new HashMap<>();
                            listmap.put("1", number);
                            listmap.put("2", applyDate);
                            listmap.put("3", code1);
                            list.add(listmap);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (code == 0) {
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showToast(getActivity(), message, ToastUtil.Error);
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
                        ActivityUtil.switchTo(getActivity(), MaterialOutCheckActivityDetail2.class, map);
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
                refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失败
                getData();
            }
        });
    }

    private void getData() {
        page++;
        if (page > totalPages) classicsFooter.setLoadmoreFinished(true);
        else {
            EasyHttp.post(GlobalApi.WareHourse.MaterialIn.PATH_Send_Header_ByPage)
                .params(GlobalApi.WareHourse.page, String.valueOf(page)) //从第0 业开始获取
                .params(GlobalApi.WareHourse.size, "20") //一次获取多少
                .params(GlobalApi.WareHourse.sort, "code") //根据code排序
                .params(GlobalApi.WareHourse.asc, "0") //升序
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        int code = 1;
                        String message = "出错";

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            code = jsonObject.optInt("code");
                            message = jsonObject.optString("message");
                            JSONObject data = jsonObject.optJSONObject("data");
                            JSONArray content = data.optJSONArray("content");

                            totalPages = data.optInt("totalPages");
                            totalElements = data.optInt("totalElements");


                            for (int i = 0; i < content.length(); i++) {
                                JSONObject content0 = content.optJSONObject(i);

                                String head_code = content0.optString("code"); //获取编码

                                JSONObject rawTypeobj = content0.optJSONObject("rawType");
                                String materia_name = rawTypeobj.optString("name"); //原料名字

                                String inDate = content0.optString("date"); //到货日期
                                String status = content0.optString("status"); //查看是否入库

                                if (status.equals("0")) { //获取所有为入库的数据
                                    Map listmap = new HashMap<>();
                                    listmap.put("1", materia_name); //部门+设备名称
                                    listmap.put("2", DateUtil.getDateToString(inDate)); //是否接单
                                    listmap.put("3", head_code); //主键
                                    list.add(listmap);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            adapter.notifyDataSetChanged(); //显示添加的数据
                        } else {
                            ToastUtil.showToast(getActivity(), message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(getActivity(), GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}