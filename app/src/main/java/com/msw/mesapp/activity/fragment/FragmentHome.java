package com.msw.mesapp.activity.fragment;

/**
 * Created by Mr.Meng on 2017/12/31.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.activity.home.equipment.InspectActivity;
import com.msw.mesapp.activity.home.equipment.RepairBillActivity;
import com.msw.mesapp.activity.home.quality.TestCheckMainActivity;
import com.msw.mesapp.activity.home.quality.TestReleaseMainActivity;
import com.msw.mesapp.activity.home.warehouse.MaterialInActivity;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentHome extends Fragment {


    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.device1)
    LinearLayout device1;
    @Bind(R.id.device2)
    LinearLayout device2;
    @Bind(R.id.device3)
    LinearLayout device3;
    @Bind(R.id.device4)
    LinearLayout device4;
    @Bind(R.id.quality1)
    LinearLayout quality1;
    @Bind(R.id.quality2)
    LinearLayout quality2;
    @Bind(R.id.quality3)
    LinearLayout quality3;
    @Bind(R.id.quality4)
    LinearLayout quality4;
    @Bind(R.id.iokun1)
    LinearLayout iokun1;
    @Bind(R.id.iokun2)
    LinearLayout iokun2;
    @Bind(R.id.iokun3)
    LinearLayout iokun3;
    @Bind(R.id.iokun4)
    LinearLayout iokun4;
    @Bind(R.id.panku1)
    LinearLayout panku1;
    @Bind(R.id.panku2)
    LinearLayout panku2;
    @Bind(R.id.panku3)
    LinearLayout panku3;
    @Bind(R.id.panku4)
    LinearLayout panku4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.viewpaper_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title.setText("主页");
        device1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "设备巡检");
                ToastUtil.showToast(getActivity(),"设备巡检",ToastUtil.Default);
                ActivityUtil.switchTo(getActivity(), InspectActivity.class);
            }
        });
        device2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "维修申请");
                ToastUtil.showToast(getActivity(),"维修申请",ToastUtil.Default);
                ActivityUtil.switchTo(getActivity(), RepairBillActivity.class);
            }
        });
        quality1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "化验审核");
                ToastUtil.showToast(getActivity(),"化验审核",ToastUtil.Default);
                ActivityUtil.switchTo(getActivity(), TestCheckMainActivity.class);
            }
        });
        quality2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "化验发布");
                ToastUtil.showToast(getActivity(),"化验发布",ToastUtil.Default);
                ActivityUtil.switchTo(getActivity(), TestReleaseMainActivity.class);
            }
        });
        iokun1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.toastShow(getActivity(), "原料入库");
                ToastUtil.showToast(getActivity(),"原料入库",ToastUtil.Default);
                ActivityUtil.switchTo(getActivity(), MaterialInActivity.class);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}