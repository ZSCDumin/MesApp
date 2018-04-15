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

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.msw.mesapp.R;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QrManageActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.classicsFooter)
    ClassicsFooter classicsFooter;
    @Bind(R.id.imgsearch)
    ImageView imgsearch;

    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();
    /**
     * 目标项是否在最后一个可见项之后
     */
    private boolean mShouldScroll;
    /**
     * 记录目标项位置
     */
    private int mToPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_manage);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("条码管理");
        add.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            Map listmap = new HashMap<>();
            listmap.put("1", "金池能源材料有限公司" + (6 + i)); //厂家编号
            listmap.put("2", "KC664" + (57 + i) + ":"); //内部编号
            list.add(listmap);
        }
    }

    public void initView() {
        initTitle();
        initRefreshLayout();
        initSearchView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_materialin, list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ActivityUtil.toastShow(getActivity(), "点击了" + position);
                        Map<String, Object> map = new HashMap<>();
                        map.put("1", s.get("1").toString());
                        map.put("2", s.get("2").toString());
                        map.put("3", list.size());

                        ActivityUtil.switchTo(QrManageActivity.this, DeviceInfoActivity.class);
                    }
                });
                holder.setText(R.id.tv, s.get("1").toString());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void initSearchView() {
        searchView.setVoiceSearch(false); //or true    ，是否支持声音的
        searchView.setSubmitOnClick(true);  //设置为true后，单击ListView的条目，search_view隐藏。实现数据的搜索
        searchView.setEllipsize(true);   //搜索框的ListView中的Item条目是否是单显示
        //搜索显示的提示
        List<String> listitem = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listitem.add(list.get(i).get("1").toString());
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
                    if (temp.get("1").toString().contains(query)) {
                        break;
                    }
                }
                //recyclerView.smoothScrollToPosition(i);//刷新完后调转到第一条内容处
                smoothMoveToPosition(recyclerView, i);
                adapter.notifyDataSetChanged();
                return false;
            }

            //文本内容发生改变时
            @Override
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

    /**
     * 初始化滑动列表
     */
    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                getData();
                adapter.notifyDataSetChanged();
                classicsFooter.setLoadmoreFinished(false);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失败
                if (list.size() <= 40 - 10) {
                    for (int i = 0; i < 10; i++) {
                        Map listmap = new HashMap<>();
                        listmap.put("1", "金池能源材料有限公司" + (6 + i)); //厂家编号
                        listmap.put("2", "KC664" + (57 + i) + ":"); //内部编号
                        list.add(listmap);
                    }
                    { //搜索显示的提示
                        List<String> listitem = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            listitem.add(list.get(i).get("1").toString());
                        }
                        String[] array = listitem.toArray(new String[listitem.size()]);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    classicsFooter.setLoadmoreFinished(true);
                }
            }
        });
    }

    private void getData() {
        list.clear();
        for (int i = 0; i < 20; i++) {
            Map listmap = new HashMap<>();
            listmap.put("1", "金池能源材料有限公司" + (6 + i)); //厂家编号
            listmap.put("2", "KC664" + (57 + i) + ":"); //内部编号
            list.add(listmap);
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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
