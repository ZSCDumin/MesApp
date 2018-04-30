package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepairBillActivity extends AppCompatActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.bt2)
    Button bt2;
    @Bind(R.id.bt3)
    Button bt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    public void initData() {

    }
    public void initPermission(){

        String permission_code = (String) SPUtil.get(RepairBillActivity.this, GlobalKey.permiss.SPKEY, new String(""));
        String[] split_pc = permission_code.split("-");

        int p1 = 0;
        int p2 = 0;
        int p3 = 0;
        for(int i = 0;i<split_pc.length;i++){
            if(split_pc[i].equals( GlobalKey.permiss.Repair_Reporter) ) p1 = 1;
            if(split_pc[i].equals(GlobalKey.permiss.Repair_Worker) ) p2 = 1;
            if(split_pc[i].equals(GlobalKey.permiss.Repair_Soorer) ) p3=1;
        }
        if(p1 == 0 && p2 == 0 && p3 == 0){
            finish();
            ToastUtil.showToast(RepairBillActivity.this,"权限不足！",ToastUtil.Error);
        }
        if(p1 == 1 && p2 == 0 && p3 == 0){ finish(); ActivityUtil.switchTo(RepairBillActivity.this,RepairReportActivity.class);}
        if(p1 == 0 && p2 == 1 && p3 == 0){ finish(); ActivityUtil.switchTo(RepairBillActivity.this,RepairWorkActivity.class);}
        if(p1 == 0 && p2 == 0 && p3 == 1){finish(); ActivityUtil.switchTo(RepairBillActivity.this,RepairScoreActivity.class);}

    }
    public void initView() {
        initPermission();

        initTitle();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.switchTo(RepairBillActivity.this,RepairReportActivity.class);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.switchTo(RepairBillActivity.this,RepairWorkActivity.class);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.switchTo(RepairBillActivity.this,RepairScoreActivity.class);
            }
        });

    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("设备维修");
        add.setVisibility(View.INVISIBLE);
    }


}
