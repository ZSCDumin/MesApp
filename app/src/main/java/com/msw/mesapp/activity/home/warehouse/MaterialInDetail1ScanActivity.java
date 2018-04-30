package com.msw.mesapp.activity.home.warehouse;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dev.BarcodeAPI;
import com.msw.mesapp.R;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DialogUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MaterialInDetail1ScanActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    TextView add;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.bt2)
    Button bt2;
    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BarcodeAPI.BARCODE_READ:
                    String s = (String) msg.obj;
                    s = s.replace('\n',' '); s = s.replace('\r',' ');
                    Map listmap = new HashMap<>();
                    listmap.put("1", s);
                    listmap.put("2", ""+((long) (Math.random() * 10)*1000));
                    list.add(listmap);
                    adapter.notifyDataSetChanged();

                    //BarcodeAPI.getInstance().stopScan();
                    //setVibratortime(50);
                    // voiceUtil.play(1, 0);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_in_detail1_scan);
        ButterKnife.bind(this);
        initData();
        initView();
    }
    private void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("收料单");
        add.setText("完成");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.createConfirmDialog(MaterialInDetail1ScanActivity.this, "提示", "匹配成功，是否开始取样？", "确定", "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityUtil.switchTo(MaterialInDetail1ScanActivity.this,MaterialInDetail1PrintActivity.class);
                                finish();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }, DialogUtil.NO_ICON
                ).show();
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            Map listmap = new HashMap<String, Object>();
            listmap.put("1", "镍钴锰基体" + (int) (Math.random() * 30));
            listmap.put("2", ""+((long) (Math.random() * 10)*1000));
            list.add(listmap);
        }
    }

    private void initView() {
        initTitle();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_material_in_detail, list) {
            @Override
            protected void convert(ViewHolder holder, Map s, final int position) {
                holder.setText(R.id.tv1,s.get("1").toString());
                holder.setText(R.id.tv2,s.get("2").toString());

            }
        };
        recyclerView.setAdapter(adapter);

        BarcodeAPI.getInstance().open();
        BarcodeAPI.getInstance().setScannerType(20);// 设置扫描头类型(10:5110; 20: N3680 40:MJ-2000)
        int ver= Build.VERSION.SDK_INT;
        BarcodeAPI.getInstance().m_handler = mHandler;
        BarcodeAPI.getInstance().setEncoding("gbk");
        BarcodeAPI.getInstance().setScanMode(true);//打开连扫


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.size()-1 >= 0) {
                    list.remove(list.size() - 1);
                    adapter.notifyDataSetChanged();
                }else {
                    ActivityUtil.toastShow(MaterialInDetail1ScanActivity.this,"数据为空！");
                }
            }
        });
        final boolean[] Sflag = {true};
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Sflag[0]){
                    BarcodeAPI.getInstance().setLights(true);
                    BarcodeAPI.getInstance().scan();
                    bt2.setText("停止");
                    Sflag[0] = false;
                }else{
                    BarcodeAPI.getInstance().setLights(false);
                    BarcodeAPI.getInstance().stopScan();
                    bt2.setText("扫码");
                    Sflag[0] = true;
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BarcodeAPI.getInstance().m_handler = mHandler;
    }

    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        BarcodeAPI.getInstance().m_handler = null;
        BarcodeAPI.getInstance().close();
        //System.exit(0);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_INFO:
            case KeyEvent.KEYCODE_MUTE:
                if (event.getRepeatCount() == 0) {
                    BarcodeAPI.getInstance().scan();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
