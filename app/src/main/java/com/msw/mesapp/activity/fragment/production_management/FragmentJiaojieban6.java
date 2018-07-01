package com.msw.mesapp.activity.fragment.production_management;

/**
 * 工具交接——1
 * Created by Mr.Meng on 2017/12/31.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentJiaojieban6 extends Fragment {

    @Bind(R.id.backward_bt)
    Button backwardBt;
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.forward_bt)
    Button forwardBt;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.bt2)
    Button bt2;
    @Bind(R.id.bt3)
    Button bt3;
    private String handoverHeaderCode = "";
    private List<Map<String, String>> list;
    private int location = 0;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    Log.i("TAG", list.size() + "");
                    setContent(location);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.viewpaper_jiaojieban6, container, false);
        ButterKnife.bind(this, view);
        handoverHeaderCode = getActivity().getIntent().getExtras().get("handoverHeaderCode").toString();
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        list = new ArrayList<>();
        getData();
    }

    public void getData() {
        EasyHttp.post(GlobalApi.ProductManagement.Jiaojieban.getByHandoverTypeCode)
            .params(GlobalApi.ProductManagement.Jiaojieban.handoverTypeCode, "6")
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(getActivity(), "获取数据出错", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray data = jsonObject.optJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            JSONObject handoverContent = item.optJSONObject("handoverContent");
                            String name = handoverContent.optString("name");
                            String code = handoverContent.optString("code");
                            Map map = new HashMap();
                            map.put("code", code);
                            map.put("name", name);
                            list.add(map);
                        }
                        handler.sendEmptyMessage(0x101);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setContent(int i) {
        content.setText(list.get(i).get("name"));
    }

    @OnClick({R.id.backward_bt, R.id.forward_bt, R.id.bt1, R.id.bt2, R.id.bt3})
    public void onViewClicked(View view) {
        String contentCode = "";
        if (location < list.size())
            contentCode = list.get(location).get("code");
        switch (view.getId()) {
            case R.id.backward_bt:
                if (location > 0) {
                    location = location - 1;
                    setContent(location);
                } else {
                    ToastUtil.showToast(getActivity(), "到头了", 1);
                }

                break;
            case R.id.forward_bt:
                if (location < list.size() - 1) {
                    location = location + 1;
                    setContent(location);
                } else {
                    ToastUtil.showToast(getActivity(), "到头了", 1);
                }
                break;
            case R.id.bt1: //14
                if (location == list.size()) {
                    ToastUtil.showToast(getActivity(), "请切换下一个交接类型", ToastUtil.Warning);
                } else {
                    submit(contentCode, "14");
                    location = location + 1;
                }
                break;
            case R.id.bt2: //15
                if (location == list.size()) {
                    ToastUtil.showToast(getActivity(), "请切换下一个交接类型", ToastUtil.Warning);
                } else {
                    submit(contentCode, "15");
                    location = location + 1;
                }
                break;
            case R.id.bt3:
                getActivity().finish();
                break;
        }
    }

    public void submit(String contentCode, String statusCode) {
        EasyHttp.post(GlobalApi.ProductManagement.Jiaojieban.handoverRecordAdd)
            .params(GlobalApi.ProductManagement.Jiaojieban.headerCode, handoverHeaderCode)
            .params(GlobalApi.ProductManagement.Jiaojieban.contentCode, contentCode)
            .params(GlobalApi.ProductManagement.Jiaojieban.stateCode, statusCode)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(getActivity(), "获取数据出错", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String message = jsonObject.optString("message");
                        int code = jsonObject.optInt("code");
                        if (code == 0) {
                            ToastUtil.showToast(getActivity(), "提交成功", ToastUtil.Success);
                            if (location < list.size())
                                setContent(location);
                        } else
                            ToastUtil.showToast(getActivity(), message, ToastUtil.Success);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

    }
}