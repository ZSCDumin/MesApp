package com.msw.mesapp.activity.home.id_management;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.bean.id_management.DepartmentData;
import com.msw.mesapp.bean.id_management.MemberData;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sam.android.utils.viewhelper.CommonExpandableListAdapter;


public class IdManagementActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.expand_list_view)
    ExpandableListView expandableListView;
    @Bind(R.id.imgsearch)
    ImageView imgsearch;

    private CommonExpandableListAdapter commonExpandableListAdapter;

    List<Map<String, Object>> list = new ArrayList<>();
    @Bind(R.id.fresh)
    SwipeRefreshLayout fresh;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    if (fresh.isRefreshing()) {
                        commonExpandableListAdapter.notifyDataSetChanged();
                        fresh.setRefreshing(false);
                    } else
                        commonExpandableListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_management);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initSearchView();
    }


    private void initSearchView() {
        searchView.setHint("搜索");
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
                commonExpandableListAdapter.notifyDataSetChanged();
                return false;
            }

            //文本内容发生改变时
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchView.isSearchOpen()) {
                    searchView.closeSearch();//关闭搜索框
                } else {
                    searchView.showSearch(true);//显示搜索框
                }
            }
        });
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("ID管理");
        add.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        fresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        fresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDepartmentData(GlobalApi.BASEURL + GlobalApi.IdManagement.PATH);
            }
        });
        commonExpandableListAdapter = new CommonExpandableListAdapter<MemberData, DepartmentData>(this, R.layout.item_detail_adapter_child1, R.layout.item_detail_adapter_group) {
            @Override
            protected void getChildView(ViewHolder holder, int groupPositon, int childPositon, boolean isLastChild, final MemberData data) {
                TextView v1 = holder.getView(R.id.tv1);
                v1.setText(data.getMember_name());
                v1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUtil.showToast(IdManagementActivity.this, data.getMember_code(), 1);
                        Map map = new HashMap();
                        map.put("name", data.getMember_name());
                        map.put("code", data.getMember_code());
                        ActivityUtil.switchTo(IdManagementActivity.this, MemberNameActivity.class, map);
                    }
                });
            }

            @Override
            protected void getGroupView(ViewHolder holder, int groupPositon, boolean isExpanded, DepartmentData data) {
                TextView textView = holder.getView(R.id.grouptxt);//分组名字
                ImageView arrowImage = holder.getView(R.id.groupIcon);//分组箭头
                textView.setText(data.getDepartment_name());
                //根据分组是否展开设置自定义箭头方向
                arrowImage.setImageResource(isExpanded ? R.mipmap.ic_arrow_expanded : R.mipmap.ic_arrow_uexpanded);
            }
        };

        //初始化部门信息
        getDepartmentData(GlobalApi.BASEURL + GlobalApi.IdManagement.PATH);
        expandableListView.setAdapter(commonExpandableListAdapter);

    }

    public void getDepartmentData(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast(IdManagementActivity.this, "网络故障", ToastUtil.Error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray data = jsonObject.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);
                        String department_code = item.optString("code");
                        String department_name = item.optString("name");
                        DepartmentData departmentData = new DepartmentData(department_name, department_code);
                        List<MemberData> memberDataList = new ArrayList<>();
                        JSONArray users = item.optJSONArray("users");
                        for (int j = 0; j < users.length(); j++) {
                            JSONObject user = users.getJSONObject(j);
                            String user_code = user.optString("code");
                            String user_name = user.optString("name");
                            MemberData memberData = new MemberData(user_code, user_name);
                            memberDataList.add(memberData);
                        }
                        commonExpandableListAdapter.getGroupData().add(departmentData);
                        commonExpandableListAdapter.getChildrenData().add(memberDataList);
                    }
                    handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}