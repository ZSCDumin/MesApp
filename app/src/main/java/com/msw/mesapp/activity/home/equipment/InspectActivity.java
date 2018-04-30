package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.activity.HomeActivity;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InspectActivity extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        ButterKnife.bind(this);

        initTitle();
        initView();
    }
    public void initTitle(){
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initPermission(){
        String permission_code = (String) SPUtil.get(InspectActivity.this, GlobalKey.permiss.SPKEY, new String(""));
        String[] split_pc = permission_code.split("-");

        int t = 0;
        String tp1 = "";
        String tp2 = "";

        for(int i = 0;i<split_pc.length;i++){
            if(split_pc[i].equals(GlobalKey.permiss.Inspect_Worker) ) { t++; tp1 = split_pc[i]; }
            if(split_pc[i].equals(GlobalKey.permiss.Inspect_Monitor)) { t++; tp2 = split_pc[i]; }
        }
        if(t == 0 ){
            finish();
            ToastUtil.showToast(InspectActivity.this,"权限不足！",ToastUtil.Error);
        }else if(t == 1){
            if(tp1.equals(GlobalKey.permiss.Inspect_Worker)) {
                finish();
                ActivityUtil.switchTo(InspectActivity.this, InspectWorkerActivity.class);
            }
            else if(tp2.equals(GlobalKey.permiss.Inspect_Monitor)) {
                finish();
                ActivityUtil.switchTo(InspectActivity.this, InspectMonitorActivity.class);
            }
            else {
                finish();
                ToastUtil.showToast(InspectActivity.this,"权限指令出错！",ToastUtil.Error);
            }
        }
    }
    //
    public void initView(){
        initPermission();

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.switchTo(InspectActivity.this,InspectWorkerActivity.class);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.switchTo(InspectActivity.this,InspectMonitorActivity.class);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
