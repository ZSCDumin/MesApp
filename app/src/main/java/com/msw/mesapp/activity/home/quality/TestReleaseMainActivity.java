package com.msw.mesapp.activity.home.quality;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class TestReleaseMainActivity extends AppCompatActivity {

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

    int p1 = 0;
    int p2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_release_main);
        ButterKnife.bind(this);

        initPermission();
        initData();
        initView();
    }
    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("化验发布");
        add.setVisibility(View.INVISIBLE);
    }
    public void initData() {

    }
    public void initPermission(){

        String permission_code = (String) SPUtil.get(TestReleaseMainActivity.this, GlobalKey.permiss.SPKEY, new String(""));
        String[] split_pc = permission_code.split("-");


        for(int i = 0;i<split_pc.length;i++){
            if(split_pc[i].equals( GlobalKey.permiss.TestRelease) ) p1 = 1;
            if(split_pc[i].equals(GlobalKey.permiss.TestReleaseProcess) ) p2=1;
        }
        if(p1 == 0 && p2 == 0){
            finish();
            ToastUtil.showToast(TestReleaseMainActivity.this,"权限不足！",ToastUtil.Error);
        }
        if( p1 + p2  == 1){
            if(p1 == 1){ finish(); ActivityUtil.switchTo(TestReleaseMainActivity.this,TestReleaseActivity.class);}
            if(p2 == 1){finish(); ActivityUtil.switchTo(TestReleaseMainActivity.this,TestReleaseProcessActivity.class);}
        }
    }

    public void initView() {
        initTitle();
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.switchTo(TestReleaseMainActivity.this,TestReleaseActivity.class);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.switchTo(TestReleaseMainActivity.this,TestReleaseProcessActivity.class);
            }
        });
    }
}
