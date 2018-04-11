package com.msw.mesapp.activity.fragment;

/**
 * Created by Mr.Meng on 2017/12/31.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.ui.widget.TitlePopup;
import com.msw.mesapp.utils.ActivityUtil;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentPlan extends Fragment {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.plan_add)
    ImageView planAdd;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    TitlePopup titlePopup;
    @Bind(R.id.classicsFooter)
    ClassicsFooter classicsFooter;

    MyAdapter myAdapter;
    List<String> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.viewpaper_plans, container, false);
        ButterKnife.bind(this, view);
        initPopup();
        initData();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title.setText("待办事项");
        planAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titlePopup.show(view);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        initRefreshLayout();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 0));//添加分割线
    }

    /**
     * 初始化(配置)下拉刷新组件
     */
    private void initRefreshLayout() {
        refreshLayout.autoRefresh();
        refreshLayout.setPrimaryColorsId(R.color.skybule, android.R.color.white);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                getData();
                classicsFooter.setLoadmoreFinished(false);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失败
                if (list.size() <= 50-10) {
                    for (int i = 0; i < 10; i++)
                        list.add("我是新增项" + (i + 1) + "号");
                    myAdapter.notifyDataSetChanged();
                } else {
                    classicsFooter.setLoadmoreFinished(true);
                    //refreshlayout.finishLoadmoreWithNoMoreData();//将不会再次触发加载更多事件
                }
            }
        });
    }

    //初始化数据
    public void initData() {
        final String[] text1 = new String[1];
        EasyHttp.get("http://ip.chinaz.com/getip.aspx")
                //.get("/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json")
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String ss) {
                        text1[0] = ss;
                        for (int i = 0; i < 10; i++) {//list.add(text1[0]);
                            list.add("设备巡检"+i);
                        }
                        myAdapter = new MyAdapter(getActivity(), R.layout.item_plan, list);
                        recyclerView.setAdapter(myAdapter);
                        initView();
                    }

                    @Override
                    public void onError(ApiException e) {
                        myAdapter = new MyAdapter(getActivity(), R.layout.item_plan, list);
                        recyclerView.setAdapter(myAdapter);
                        initView();
                        ActivityUtil.toastShow(getActivity(), "获取数据失败");
                    }
                });
    }

    //获取更新数据
    public void getData() {
        list.clear();
        EasyHttp.get("http://ip.chinaz.com/getip.aspx")
                //.get("/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json")
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String ss) {
                        for (int i = 0; i < 10; i++) {
                            //list.add(ss);
                            list.add("设备巡检"+i+i);
                        }
                        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                        myAdapter.notifyItemRangeChanged(0, list.size());
                    }

                    @Override
                    public void onError(ApiException e) {
                        ActivityUtil.toastShow(getActivity(), "获取数据失败");
                    }
                });
    }

    //
    public void initPopup() {
        titlePopup = new TitlePopup(getActivity(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //给标题栏弹窗添加子类
        titlePopup.addAction(new TitlePopup.ActionItem(getActivity(), "发起聊天", R.drawable.mm_title_btn_compose_normal));
        titlePopup.addAction(new TitlePopup.ActionItem(getActivity(), "听筒模式", R.drawable.mm_title_btn_receiver_normal));
        titlePopup.addAction(new TitlePopup.ActionItem(getActivity(), "登录网页", R.drawable.mm_title_btn_keyboard_normal));
        titlePopup.addAction(new TitlePopup.ActionItem(getActivity(), "扫一扫", R.drawable.mm_title_btn_qrcode_normal));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(TitlePopup.ActionItem item, int position) {
                ActivityUtil.toastShow(getActivity(), "点击了" + item.mTitle.toString());
            }
        });
    }


    //用通用适配器
    public class MyAdapter extends CommonAdapter<String> {
        public MyAdapter(Context context, int layoutId, List<String> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, String s, final int position) {
            holder.setText(R.id.text1, s);
            //holder.setImageDrawable(R.id.img, getResources().getDrawable(R.drawable.ic_home_black_24dp));
            holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityUtil.toastShow(getActivity(), "0点击了" + position);
                }
            });
        }

    }

}