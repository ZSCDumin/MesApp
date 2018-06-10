package com.msw.mesapp.activity.home.production_management.CheckScalesManagement;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.utils.ActivityUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckScalesManagementDetails2 extends Activity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.bt2)
    Button bt2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.bt3)
    Button bt3;
    @Bind(R.id.tv4)
    TextView tv4;
    @Bind(R.id.bt4)
    Button bt4;
    @Bind(R.id.tv5)
    TextView tv5;
    @Bind(R.id.bt5)
    Button bt5;
    @Bind(R.id.tv6)
    TextView tv6;
    @Bind(R.id.submit_bt)
    Button submitBt;

    private int flag = 1; //是否合格的标记，1为合格

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_scales_management_details2);
        ButterKnife.bind(this);

        initView();
    }

    public void initView() {
        title.setText("41号秤");
        add.setVisibility(View.INVISIBLE);

    }

    public void check(String value) {

        double t1 = Double.valueOf(value);
        Log.i("TAG", value);

        if (Math.abs(t1 - 100.0) > 0.2) {
            flag = 0;
        }

        if (flag == 1) {
            tv6.setText("合格");
        } else {
            tv6.setText("不合格");
        }
    }

    @OnClick({R.id.back, R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4, R.id.bt5, R.id.submit_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bt1:
                check(tv1.getText().toString());
                break;
            case R.id.bt2:
                check(tv2.getText().toString());
                break;
            case R.id.bt3:
                check(tv3.getText().toString());
                break;
            case R.id.bt4:
                check(tv4.getText().toString());
                break;
            case R.id.bt5:
                check(tv5.getText().toString());
                break;
            case R.id.submit_bt:
                ActivityUtil.switchTo(this, CheckScalesManagementDetails3.class);
                break;
        }
    }
}
