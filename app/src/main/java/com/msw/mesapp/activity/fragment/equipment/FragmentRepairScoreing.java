package com.msw.mesapp.activity.fragment.equipment;

/**
 * Created by Mr.Meng on 2017/12/31.
 * 未审核
 */

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
import com.msw.mesapp.activity.home.equipment.RepairBillActivity;
import com.msw.mesapp.activity.home.equipment.RepairWorkActivity;
import com.msw.mesapp.activity.home.equipment.RepairWorkDetail2Activity;
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

public class FragmentRepairScoreing extends Fragment {

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

    private boolean mShouldScroll;
    /**
     * 目标项是否在最后一个可见项之后
     */
    private int mToPosition;

    /**
     * 记录目标项位置
     */


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
        EasyHttp.post(GlobalApi.Repair.PATH_ByFlagInPages)
            .params(GlobalApi.Repair.code, "1") //查找所有已接单的数据
            .params(GlobalApi.Repair.page, String.valueOf(page)) //从第0 业开始获取
            .params(GlobalApi.Repair.size, "20") //一次获取多少
            .params(GlobalApi.Repair.sort, "applicationTime") //根据code排序
            .params(GlobalApi.Repair.asc, "0") //升序
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
                            JSONObject item = content.getJSONObject(i);
                            JSONObject equipment = item.optJSONObject("equipment");
                            String departmentname = item.optJSONObject("department").optString("name");
                            String equipmentname = "空值";
                            if (equipment != null) {
                                equipmentname = equipment.optString("name"); //设备名称
                            }
                            JSONObject flag = item.getJSONObject("flag");
                            String flagname = flag.optString("name"); //是否接单
                            String codeStr = item.optString("code"); //主键
                            Map listmap = new HashMap<>();
                            listmap.put("1", "部门:" + departmentname + " - - " + "设备:" + equipmentname); //部门+设备名称
                            listmap.put("2", flagname); //是否接单
                            listmap.put("3", codeStr); //主键
                            Log.i("codeStr", codeStr);
                            list.add(listmap);
                        }
                    } catch (Exception e) {
                        ToastUtil.showToast(getActivity(), "数据异常", ToastUtil.Error);
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
        initSearchView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(getActivity(), R.layout.item_repair_report, list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getActivity() instanceof RepairBillActivity) {
                            ToastUtil.showToast(getActivity(), "无法查看具体内容", ToastUtil.Warning);
                        } else if (getActivity() instanceof RepairWorkActivity) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("code", s.get("3").toString());
                            ActivityUtil.switchTo(getActivity(), RepairWorkDetail2Activity.class, map);
                            getActivity().finish();

                        }
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

    /**
     * 初始化搜索框
     */
    private void initSearchView() {
        searchView.setVoiceSearch(false); //or true    ，是否支持声音的
        searchView.setSubmitOnClick(true);  //设置为true后，单击ListView的条目，search_view隐藏。实现数据的搜索
        searchView.setEllipsize(true);   //搜索框的ListView中的Item条目是否是单显示
        //搜索显示的提示
        List<String> listitem = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listitem.add(list.get(i).get("2").toString());
        }
        String[] array = listitem.toArray(new String[listitem.size()]);
        searchView.setSuggestions(array);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            //数据提交时
            //1.点击ListView的Item条目会回调这个方法
            //2.点击系统键盘的搜索/回车后回调这个方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                int i = 0;
                for (Map<String, Object> temp : list) {
                    i++;
                    if (temp.get("2").toString().contains(query)) {
                        break;
                    }
                }
                //recyclerView.smoothScrollToPosition(i);//刷新完后调转到第一条内容处
                smoothMoveToPosition(recyclerView, i);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override//文本内容发生改变时
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchView.isSearchOpen()) {
                    searchView.closeSearch();//隐藏搜索框
                } else {
                    searchView.showSearch(true);//显示搜索框
                }
            }
        });
    }

    private void getData() {
        page++;
        if (page > totalPages) classicsFooter.setLoadmoreFinished(true);
        else {
            EasyHttp.post(GlobalApi.Repair.PATH_ByFlagInPages)
                .params(GlobalApi.Repair.code, "2") //人id
                .params(GlobalApi.Repair.page, String.valueOf(page)) //从第0 业开始获取
                .params(GlobalApi.Repair.size, "20") //一次获取多少
                .params(GlobalApi.Repair.sort, "applicationTime") //根据code排序
                .params(GlobalApi.Repair.asc, "0") //升序
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
                                JSONObject item = content.getJSONObject(i);
                                JSONObject equipment = item.optJSONObject("equipment");
                                String departmentname = item.optJSONObject("department").optString("name");
                                String equipmentname = "空值";
                                if (equipment != null) {
                                    equipmentname = equipment.optString("name"); //设备名称
                                }
                                JSONObject flag = item.getJSONObject("flag");
                                String flagname = flag.optString("name"); //是否接单
                                String codeStr = item.optString("code"); //主键
                                Map listmap = new HashMap<>();
                                listmap.put("1", "部门:" + departmentname + " - - " + "设备:" + equipmentname); //部门+设备名称
                                listmap.put("2", flagname); //是否接单
                                listmap.put("3", codeStr); //主键
                                Log.i("codeStr", codeStr);
                                list.add(listmap);
                            }

                        } catch (Exception e) {
                            ToastUtil.showToast(getActivity(), "数据异常", ToastUtil.Error);
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


//        { //搜索显示的提示
//            List<String> listitem = new ArrayList<>();
//            for(int i=0;i<list.size();i++){
//                listitem.add(list.get(i).get("2").toString());
//            }
//            String[] array = listitem.toArray(new String[listitem.size()]);
//            searchView.setSuggestions(array);
//        }
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