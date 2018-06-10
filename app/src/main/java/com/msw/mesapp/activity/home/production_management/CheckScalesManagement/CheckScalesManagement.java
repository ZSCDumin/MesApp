package com.msw.mesapp.activity.home.production_management.CheckScalesManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.utils.ActivityUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckScalesManagement extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_scales_management);
        ButterKnife.bind(this);
        title.setText("核秤管理");
        add.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                ActivityUtil.switchTo(this, CheckScalesMemberActivity.class);//核秤人
                break;
            case R.id.tv2:
                ActivityUtil.switchTo(this, CheckScalesCheckActivity.class);//核秤审核人
                break;
            case R.id.back:
                finish();
                break;
        }
    }

}
