package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.utils.StatusBarUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InspectWorkerDetailActivity extends AppCompatActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.task_describe_tv)
    TextView taskDescribeTv;
    @Bind(R.id.task_requirement_tv)
    TextView taskRequirementTv;
    @Bind(R.id.task_createTime_tv)
    TextView taskCreateTimeTv;
    @Bind(R.id.task_doer_tv)
    TextView taskDoerTv;

    private String taskDescribe = "";
    private String taskRequirement = "";
    private String taskCreateTime = "";
    private String taskDoer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_detail);
        ButterKnife.bind(this);
        taskDescribe = getIntent().getExtras().get("1").toString();
        taskCreateTime = getIntent().getExtras().get("2").toString();
        taskRequirement = getIntent().getExtras().get("3").toString();
        taskDoer = getIntent().getExtras().get("4").toString();
        initView();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        title.setText("任务详情");
        add.setVisibility(View.INVISIBLE);
    }

    public void initView() {
        initTitle();
        taskDescribeTv.setText(taskDescribe);
        taskRequirementTv.setText(taskRequirement);
        taskCreateTimeTv.setText(taskCreateTime);
        taskDoerTv.setText(taskDoer);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
