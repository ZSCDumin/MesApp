package com.msw.mesapp.activity.home.production_management.JiaojiebanManagement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JiaojiebanManagementDetails4 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.baiban)
    RadioButton baiban;
    @Bind(R.id.wanban)
    RadioButton wanban;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.next)
    Button next;
    List<String> dataList = new ArrayList<>();
    List<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private String code = "";
    private String name = "";
    private String handoverHeaderCode = "";
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", code);
                    map.put("name", name);
                    map.put("handoverHeaderCode", handoverHeaderCode);
                    ActivityUtil.switchTo(JiaojiebanManagementDetails4.this, JiaoJieBanManagementDetails2.class, map);
                    finish();
                    break;
                case 0x102:
                    adapter.notifyDataSetChanged();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiaojieban_management_details4);
        ButterKnife.bind(this);
        code = getIntent().getExtras().get("code").toString();
        name = getIntent().getExtras().get("name").toString();
        initView();
        getAllPerson();
    }

    public void initView() {
        title.setText(name);
        add.setVisibility(View.INVISIBLE);
        dataList.add("请选择");
        adapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item,
            dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.back, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.next:
                String duty_code = "";
                String successor_code = "";
                if (baiban.isChecked()) {
                    duty_code = "1";
                } else if (wanban.isChecked()) {
                    duty_code = "2";
                }
                successor_code = list.get(spinner.getSelectedItemPosition() - 1);
                Log.i("successor_code", successor_code);
                if (successor_code.equals("请选择")) {
                    ToastUtil.showToast(this, "请选择交班人", ToastUtil.Warning);
                } else {
                    generateHandoverHeader(code, DateUtil.getDateToString1(new Date().getTime()), duty_code, successor_code, GetCurrentUserIDUtil.currentUserId(this));
                }
                break;
        }
    }

    public void getAllPerson() {
        EasyHttp.post(GlobalApi.ProductManagement.Jiaojieban.getAllPerson)
            .params(GlobalApi.ProductManagement.Jiaojieban.code, GetCurrentUserIDUtil.currentUserId(this))
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(JiaojiebanManagementDetails4.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray data = jsonObject.optJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String code = item.optString("code");
                            String name = item.optString("name");
                            list.add(code);
                            dataList.add(name);
                        }
                        handler.sendEmptyMessage(0x102);//通过handler发送一个更新数据的标记
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    public void generateHandoverHeader(String jobs_code, String handover_date, String duty_code, String shifter_code, String successor_code) {

        Log.i("TAG", jobs_code + "------" + handover_date + "------" + duty_code + "------" + shifter_code + "------" + successor_code);
        EasyHttp.post(GlobalApi.ProductManagement.Jiaojieban.handoverHeaderAdd)
            .params(GlobalApi.ProductManagement.Jiaojieban.jobsCode, jobs_code)
            .params(GlobalApi.ProductManagement.Jiaojieban.handover_date, handover_date)
            .params(GlobalApi.ProductManagement.Jiaojieban.duty_code, duty_code)
            .params(GlobalApi.ProductManagement.Jiaojieban.shifter_code, shifter_code)
            .params(GlobalApi.ProductManagement.Jiaojieban.successor_code, successor_code)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(JiaojiebanManagementDetails4.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.optJSONObject("data");
                        String code = data.optString("code");
                        handoverHeaderCode = code;
                        handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
