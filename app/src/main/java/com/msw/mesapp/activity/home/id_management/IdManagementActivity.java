package com.msw.mesapp.activity.home.id_management;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.home.equipment.DeviceInfoActivity;
import com.msw.mesapp.activity.home.equipment.QrManageActivity;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    CommonExpandableListAdapter<ChildData, GroupData> commonExpandableListAdapter;
    List<ChildData> childDataList = new ArrayList<>();
    List<GroupData> groupDataList = new ArrayList<>();
    List<Map<String, Object>> list = new ArrayList<>();


    /**
     * 分组数据
     */
    class GroupData {
        String department_name;
    }

    /**
     * 孩子数据
     */
    class ChildData {
        String member_name;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_management);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initSearchView();
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), childDataList.get(childPosition).member_name, Toast.LENGTH_LONG).show();
                ActivityUtil.switchTo(IdManagementActivity.this, MemberNameActivity.class);
                return true;
            }
        });
    }


    private void initData() {
        expandableListView.setAdapter(commonExpandableListAdapter = new CommonExpandableListAdapter<ChildData, GroupData>(this, R.layout.item_detail_adapter_child1, R.layout.item_detail_adapter_group) {
            @Override
            protected void getChildView(ViewHolder holder, int groupPositon, int childPositon, boolean isLastChild, ChildData data) {
                TextView v1 = holder.getView(R.id.tv1);
                v1.setText(data.member_name);
            }

            @Override
            protected void getGroupView(ViewHolder holder, int groupPositon, boolean isExpanded, GroupData data) {
                TextView textView = holder.getView(R.id.grouptxt);//分组名字
                ImageView arrowImage = holder.getView(R.id.groupIcon);//分组箭头
                textView.setText(data.department_name);
                //根据分组是否展开设置自定义箭头方向
                arrowImage.setImageResource(isExpanded ? R.mipmap.ic_arrow_expanded : R.mipmap.ic_arrow_uexpanded);
            }
        });

        //初始化部门数据
        for (int i = 0; i < 10; i++) {
            ChildData childData = new ChildData();
            childData.member_name = "张山" + i;
            childDataList.add(childData);
        }
        //初始化成员数据
        for (int i = 0; i < 10; i++) {
            GroupData groupData = new GroupData();
            groupData.department_name = "调研部" + i;
            groupDataList.add(groupData);
            commonExpandableListAdapter.getGroupData().add(i, groupData);
            commonExpandableListAdapter.getChildrenData().add(childDataList);
        }
        commonExpandableListAdapter.notifyDataSetChanged();
        expandableListView.setAdapter(commonExpandableListAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
