package com.msw.mesapp.activity.fragment.production_management;

/**
 * Created by Mr.Meng on 2017/12/31.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentJiaojieban extends Fragment {

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

    private List<String> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.viewpaper_jiaojieban, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("aaaaaaaaaaaaaaaaa" + i);
        }
        setContent(location);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setContent(int i) {
        content.setText(list.get(i));
    }

    private int location = 0;

    @OnClick({R.id.backward_bt, R.id.forward_bt, R.id.bt1, R.id.bt2, R.id.bt3})
    public void onViewClicked(View view) {
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
            case R.id.bt1:
                ToastUtil.showToast(getActivity(), "完好", 1);
                break;
            case R.id.bt2:
                ToastUtil.showToast(getActivity(), "损坏", 1);
                break;
            case R.id.bt3:
                ToastUtil.showToast(getActivity(), "丢失", 1);
                break;
        }
    }
}