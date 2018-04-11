package com.msw.mesapp.activity.home.equipment;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.ui.widget.TitlePopup;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.NotifyUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InspectMonitorActivity extends AppCompatActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.classicsFooter)
    ClassicsFooter classicsFooter;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    TitlePopup titlePopup;
    private RecyclerView.Adapter adapter;

    List<Map<String, Object>> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_monitor);
        StatusBarUtils.setStatusBarColor(this,R.color.titlecolor);
        ButterKnife.bind(this);


        initData();
        initView();
    }
    //
    public void initData(){
        for(int i=0;i<10;i++){
            Map listmap = new HashMap<String, Object>();
            listmap.put("1","2018-08-"+(int)(Math.random()*30));
            listmap.put("2","核查任务："+(int)(Math.random()*10)+"车间需要点检");
            list.add(listmap);
        }
    }
    //
    public void initView(){
        initTitle();
        initRefreshLayout();
        initPopup();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this,R.layout.item_inspect,list) {
            @Override
            protected void convert(ViewHolder holder, Map s, final int position) {
                holder.setText(R.id.tv1,s.get("2").toString());
                holder.setText(R.id.tv2,s.get("1").toString());
                holder.setOnClickListener(R.id.bt1, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ActivityUtil.toastShow(InspectMonitorActivity.this, "点击了详情" + position);
                        ActivityUtil.switchTo(InspectMonitorActivity.this,InspectMonitorDetailActivity.class);
                    }
                });
                holder.setOnClickListener(R.id.bt2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ActivityUtil.toastShow(InspectMonitorActivity.this, "点击了进入" + position);
                        ActivityUtil.switchTo(InspectMonitorActivity.this,InspectMonitorJudgeActivity.class);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
    //
    public void initTitle(){
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        add.setImageResource(R.mipmap.icon_list);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titlePopup.show(view);
            }
        });
    }
    /**
     * 初始化(配置)下拉刷新组件
     */
    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000/*,false*/);//传入false表示加载失败
            }
        });
    }
    //初始化标题列表
    public void initPopup() {
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //给标题栏弹窗添加子类
        titlePopup.addAction(new TitlePopup.ActionItem(this, "按任务发布时间", R.drawable.mm_title_btn_compose_normal));
        titlePopup.addAction(new TitlePopup.ActionItem(this, "按任务截止时间", R.drawable.mm_title_btn_receiver_normal));
        titlePopup.addAction(new TitlePopup.ActionItem(this, "设置提醒时间", R.drawable.mm_title_btn_keyboard_normal));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(TitlePopup.ActionItem item, int position) {
                ActivityUtil.toastShow(InspectMonitorActivity.this, "点击了" + item.mTitle.toString());
                switch (position){
                    case 0:
                        Collections.sort(list, new Comparator<Map<String,Object>>(){

                            @Override
                            public int compare(Map<String, Object> h1, Map<String, Object> h2) {
                                String name1 =(String)h1.get("1");
                                String name2= (String)h2.get("1");
                                return name1.compareTo(name2);
                            }
                        });
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        break;
                    case 2:
                        new NotifyUtil(InspectMonitorActivity.this,1).notify_normal_singline(
                                PendingIntent.getActivity(InspectMonitorActivity.this,1, new Intent(InspectMonitorActivity.this,InspectWorkerActivity.class), PendingIntent.FLAG_UPDATE_CURRENT),
                                R.mipmap.ic_launcher,
                                "您有一条新通知",
                                "双十一大优惠！！！",
                                "仿真皮肤充气娃娃，女朋友带回家！",
                                true/*声音*/, true/*震动*/, false/*灯光*/
                        );
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
