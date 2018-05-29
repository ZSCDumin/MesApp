package com.msw.mesapp.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.activity.home.warehouse.MaterialOutCheckActivityDetail1;
import com.msw.mesapp.activity.home.warehouse.MaterialOutUrgentCheckActivity;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.MSW on 2018/5/6.
 */

public class CardFragment extends Fragment {
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tvview)
    View tvview;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    String[] ss1 = new String[6];

    public static CardFragment newInstance(String[] ss) {
        CardFragment cardFragment = new CardFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("ss", ss);
        cardFragment.setArguments(bundle);
        return cardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.item_cardview, container, false);
        ButterKnife.bind(this, view);
        Bundle args = getArguments();
        if (args != null) {
            ss1 = args.getStringArray("ss");
        }

        if (ss1 != null) {
            tv1.setText(ss1[0]);
            tv2.setText(ss1[1]);
            tv3.setText(DateUtil.getDateToString(Long.valueOf(ss1[2])));
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.cardview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cardview:
                //进行数据库更新操作
                EasyHttp.post(GlobalApi.UndoThingsItems.PATH1)
                        .params(GlobalApi.UndoThingsItems.STATUS, "1")
                        .params(GlobalApi.UndoThingsItems.CODE, ss1[5])
                        .sign(true)
                        .timeStamp(true)//本次请求是否携带时间戳
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String result) {
                                //设置消息状态为已读
                                if (ss1[4].equals("0"))
                                    tvview.setBackgroundResource(R.mipmap.blue_dot);
                                else
                                    tvview.setBackgroundResource(R.mipmap.red_dot);
                                ToastUtil.showToast(getActivity(), "更新消息为已读成功!", ToastUtil.Error);
                            }

                            @Override
                            public void onError(ApiException e) {
                                ToastUtil.showToast(getActivity(), "更新消息为已读出错!", ToastUtil.Error);
                            }
                        });
                //跳转页面
                String destActivity = ss1[3].split("-")[0];
                String code = ss1[3].split("-")[2];
                String isUrgent = ss1[3].split("-")[1];
                Map map = new HashMap<>();
                map.put("code", code);
                if (destActivity.equals("1")) { //领料申请
                    if (isUrgent.equals("0")) { //紧急
                        ActivityUtil.switchTo(getActivity(), MaterialOutUrgentCheckActivity.class, map);
                    } else { //正常
                        ActivityUtil.switchTo(getActivity(), MaterialOutCheckActivityDetail1.class, map);
                    }
                } else {
                    //待续
                }
                break;
        }
    }
}
