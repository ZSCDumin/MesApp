package com.msw.mesapp.activity.home.production_management.JiaojiebanManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ToastUtil;
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
import butterknife.OnClick;

public class JiaoJieBanManagementDetails3 extends AppCompatActivity {

    Button backBt;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private String name = "";
    private String headerCode = "";
    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiao_jie_ban_management_details3);
        ButterKnife.bind(this);
        name = getIntent().getExtras().get("name").toString();
        headerCode = getIntent().getExtras().get("headerCode").toString();
        initTitle();
        getData();
    }


    public void initTitle() {
        title.setText(name);
        add.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_jiaojieban_content, list) {
            @Override
            protected void convert(ViewHolder holder, final Map s, final int position) {
                holder.setText(R.id.type_tv, s.get("1").toString());
                holder.setText(R.id.content_tv, s.get("2").toString());
                holder.setText(R.id.result_tv, s.get("3").toString());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public void getData() {
        Log.i("TAG", headerCode);
        EasyHttp.post(GlobalApi.ProductManagement.Jiaojieban.getByCode1)
            .params(GlobalApi.ProductManagement.Jiaojieban.code, headerCode)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(JiaoJieBanManagementDetails3.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.optJSONObject("data");
                        JSONArray handoverRecords = data.optJSONArray("handoverRecords");
                        for (int i = 0; i < handoverRecords.length(); i++) {
                            JSONObject item = handoverRecords.getJSONObject(i);
                            String content = item.optJSONObject("contentCode").optString("name");
                            String status = item.optJSONObject("stateCode").optString("name");
                            Map<String, Object> map = new HashMap<>();
                            map.put("1", "");
                            map.put("2", content);
                            map.put("3", status);
                            list.add(map);
                        }
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
