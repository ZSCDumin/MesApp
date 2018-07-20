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
import com.msw.mesapp.activity.home.warehouse.MaterialInActivityDetail2;
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

//获取第三方发货单列表
public class FragmentMaterialIn2 extends Fragment {

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
        EasyHttp.post(GlobalApi.WareHourse.MaterialIn.PATH_GoDown_Header_ByPage) //获取发货单
            .params(GlobalApi.WareHourse.page, String.valueOf(page)) //从第0业开始获取
            .params(GlobalApi.WareHourse.size, "10") //一次获取多少
            .params(GlobalApi.WareHourse.sort, "code") //根据code排序
            .params(GlobalApi.WareHourse.asc, "1") //升序
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
                            String materia_name = "";
                            JSONObject rawTypeobj = content0.optJSONObject("rawType");
                            if (rawTypeobj != null)
                                materia_name = rawTypeobj.optString("name"); //原料名字


                            String weight = content0.optString("weight"); //重量
                            String DownDate = content0.optString("date"); //到货日期

                            Map listmap = new HashMap<>();
                            listmap.put("1", materia_name + ":" + weight + "kg"); //物料名称+重量
                            listmap.put("2", DownDate); //
                            listmap.put("3", head_code); //
                            list.add(listmap);
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
        initSearchView();
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
                        map.put("code", s.get("3").toString());

                        ActivityUtil.switchTo(getActivity(), MaterialInActivityDetail2.class, map);
                    }
                });
                holder.setText(R.id.tv1, s.get("1").toString());
                holder.setText(R.id.tv2, s.get("2").toString());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化搜索框
     */
    private void initSearchView() {
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
            EasyHttp.post(GlobalApi.WareHourse.MaterialIn.PATH_GoDown_Header_ByPage) //获取发货单
                .params(GlobalApi.WareHourse.page, String.valueOf(page)) //从第0业开始获取
                .params(GlobalApi.WareHourse.size, "20") //一次获取多少
                .params(GlobalApi.WareHourse.sort, "code") //根据code排序
                .params(GlobalApi.WareHourse.asc, "1") //升序
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
                                String materia_name = "";
                                if (rawTypeobj != null) {
                                    materia_name = rawTypeobj.optString("name"); //原料名字
                                }

                                String weight = content0.optString("weight"); //重量
                                String DownDate = content0.optString("date"); //到货日期

                                Map listmap = new HashMap<>();
                                listmap.put("1", materia_name + ":" + weight + "kg"); //物料名称+重量
                                listmap.put("2", DownDate); //
                                listmap.put("3", head_code); //
                                list.add(listmap);

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