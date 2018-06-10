package com.msw.mesapp.activity.home.production_management.ShaiwangManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.utils.ActivityUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShaiWangManagementDetails1 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();
    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shai_wang_management_details1);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        getData();
        intiView();
    }

    public void getData() {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("1", "2018-6-6");
            map.put("2", "" + i);
            list.add(map);
        }
    }

    public void intiView() {

        title.setText("41号筛网");
        add.setImageResource(R.mipmap.add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_materialin, list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setText(R.id.tv1, s.get("1").toString());
                holder.setText(R.id.tv2, "");
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //查看详情
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", s.get("2").toString());
                        Log.i("TAG", code);
                        ActivityUtil.switchTo(ShaiWangManagementDetails1.this, ShaiWangManagementDetails3.class, map);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.back, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                Map<String, Object> map = new HashMap<>();
                map.put("code", code);
                Log.i("TAG", code);
                ActivityUtil.switchTo(ShaiWangManagementDetails1.this, ShaiWangManagementDetails2.class, map);
                break;
        }
    }
}
