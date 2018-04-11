package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msw.mesapp.R;
import com.msw.mesapp.utils.StatusBarUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bluemobi.dylan.photoview.library.PhotoView;

public class PhotoviewActivity extends AppCompatActivity {

    @Bind(R.id.photoview)
    PhotoView photoview;

    String Ulr;//接收网址
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);
        ButterKnife.bind(this);
        initTitle();

        Ulr = getIntent().getStringExtra("url");

        Glide.with(PhotoviewActivity.this).load(Ulr).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.defualt_inspect).into(photoview);
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        title.setText("点检巡查");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        add.setVisibility(View.INVISIBLE);
    }
}
