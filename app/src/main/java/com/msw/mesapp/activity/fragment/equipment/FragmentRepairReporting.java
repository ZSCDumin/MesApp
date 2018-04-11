package com.msw.mesapp.activity.fragment.equipment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.HomeActivity;
import com.msw.mesapp.activity.LoginActivity;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.bean.LoginBean;
import com.msw.mesapp.utils.ACacheUtil;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mr.Meng on 2018/3/22.
 */

public class FragmentRepairReporting extends Fragment {
    @Bind(R.id.et1)
    MaterialEditText et1;
    @Bind(R.id.et2)
    MaterialEditText et2;
    @Bind(R.id.et3)
    MaterialEditText et3;
    @Bind(R.id.et4)
    EditText et4;
    @Bind(R.id.bt)
    Button bt;

    private String t1 = "";
    private String t2 = "";
    private String t3 = "";
    private String t4 = "";
    private String id = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_repair_reporting, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();

        return view;
    }

    private void initData() {
        id = (String) SPUtil.get(getActivity(), GlobalKey.Login.CODE, id);
    }

    private void initView() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1 = et1.getText().toString();
                t2 = et2.getText().toString();
                t3 = et3.getText().toString();
                t4 = et4.getText().toString();
                //if(t1.length() == 0) { et1.setError("输入不能为空"); return;}
                //if(t2.length() == 0) { et2.setError("输入不能为空"); return;}
                //if(t3.length() == 0) { et3.setError("输入不能为空"); return;}
                //if(t4.length() == 0) { et4.setError("输入不能为空"); return;}

                IProgressDialog mProgressDialog = new IProgressDialog() {
                    @Override
                    public Dialog getDialog() {
                        ProgressDialog dialog = new ProgressDialog(getActivity());
                        dialog.setMessage(GlobalApi.ProgressDialog.UpData);
                        return dialog;
                    }
                };
                EasyHttp.post(GlobalApi.Repair.UpErr.PATH)
                        .params(GlobalApi.Repair.UpErr.department_code, t1) //部门
                        .params(GlobalApi.Repair.UpErr.eqArchive_code, t2) //设备名称
                        .params(GlobalApi.Repair.UpErr.productLine_code, t3) //生产线
                        .params(GlobalApi.Repair.UpErr.applicationDescription, t4) //故障描述
                        .params(GlobalApi.Repair.UpErr.applicationPerson_code, id) //申请人id
                        .sign(true)
                        .timeStamp(true)//本次请求是否携带时间戳
                        .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                            @Override
                            public void onSuccess(String loginModel) {
                                int code = 1;
                                String message = "出错";

                                try {
                                    JSONObject jsonObject = JSON.parseObject(loginModel);
                                    code = (int) jsonObject.get("code");
                                    message = (String) jsonObject.get("message");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (code == 0) {
                                    ToastUtil.showToast(getActivity(), message, ToastUtil.Success);
                                    getActivity().finish();
                                    ActivityUtil.switchTo(getActivity(),getActivity().getClass());
                                } else {
                                    ToastUtil.showToast(getActivity(), message, ToastUtil.Error);
                                }
                            }
                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                ToastUtil.showToast(getActivity(), GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                            }
                        });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
