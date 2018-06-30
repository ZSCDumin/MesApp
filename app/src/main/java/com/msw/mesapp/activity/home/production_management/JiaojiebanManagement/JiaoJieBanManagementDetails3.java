package com.msw.mesapp.activity.home.production_management.JiaojiebanManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JiaoJieBanManagementDetails3 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.type_tv)
    TextView typeTv;
    @Bind(R.id.content_tv)
    TextView contentTv;
    @Bind(R.id.result_tv)
    TextView resultTv;
    @Bind(R.id.back_bt)
    Button backBt;

    private String name = "";
    private String handoverTypeName = "";
    private String handoverContentName = "";
    private String handoverStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiao_jie_ban_management_details3);
        ButterKnife.bind(this);
        name = getIntent().getExtras().get("name").toString();
        handoverTypeName = getIntent().getExtras().get("handoverTypeName").toString();
        handoverContentName = getIntent().getExtras().get("handoverContentName").toString();
        handoverStatus = getIntent().getExtras().get("handoverStatus").toString();
        initTitle();
    }


    public void initTitle() {
        title.setText(name);
        add.setVisibility(View.INVISIBLE);
        typeTv.setText(handoverTypeName);
        contentTv.setText(handoverContentName);
        resultTv.setText(handoverStatus);
    }

    @OnClick({R.id.back, R.id.back_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back_bt:
                finish();
                break;
        }
    }
}
