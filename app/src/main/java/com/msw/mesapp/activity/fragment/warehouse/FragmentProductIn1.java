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
import com.msw.mesapp.activity.home.warehouse.ProductInActivityDetail1;
import com.msw.mesapp.activity.home.warehouse.ProductOutActivityDetail1;
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
 * 成品待入库
 */
public class FragmentProductIn1 extends Fragment {

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

    /**
     * 目标项是否在最后一个可见项之后
     */
    private boolean mShouldScroll;
    /**
     * 记录目标项位置
     */
    private int mToPosition;


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
        fun();
    }

    public void fun()
    {
        EasyHttp.post(GlobalApi.WareHourse.ProductIn.getByStatusByPage)
                .params("status", "0") //待入库
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
                                JSONObject item = content.optJSONObject(i);
                                String batchNumber = item.optString("batchNumber");
                                String payTime = DateUtil.getDateToString(item.optLong("payTime"));
                                String code1 = item.optString("code");
                                Map map = new HashMap();
                                map.put("0", batchNumber);
                                map.put("1", payTime);
                                map.put("2", code1);
                                list.add(map);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            adapter.notifyDataSetChanged();
                            //ToastUtil.showToast(getActivity(),message,ToastUtil.Success);
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
                        //ActivityUtil.toastShow(getActivity(), "点击了" + position);
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", s.get("2").toString());

                        ActivityUtil.switchTo(getActivity(), ProductInActivityDetail1.class, map);
                    }
                });
                holder.setText(R.id.tv1, s.get("0").toString());
                holder.setText(R.id.tv2, s.get("1").toString());
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
            fun();
        }
    }

    /**
     * 滑动到指定位置
     *
     * @param mRecyclerView
     * @param position
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}