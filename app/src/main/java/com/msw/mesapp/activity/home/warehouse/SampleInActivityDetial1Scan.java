package com.msw.mesapp.activity.home.warehouse;

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
import com.msw.mesapp.activity.HomeActivity;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SampleInActivityDetial1Scan extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.add)
    ImageView add;

    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();
    List<Map<String, Object>> batchList = new ArrayList<>();
    int count = 1;
    String headcode = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BarcodeAPI.BARCODE_READ:
                    String s = (String) msg.obj;
                    s = s.replace('\n', ' ');
                    s = s.replace('\r', ' ');
                    boolean tt = true;
                    for (int i = 0; i < batchList.size(); i++) {
                        Map map = batchList.get(i);
                        if (map.get("2").equals(s)) tt = false;
                    }
                    if (tt) {
                        Map listmap = new HashMap<>();
                        listmap.put("1", String.valueOf(batchList.size()));
                        listmap.put("2", s);
                        batchList.add(listmap);
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    boolean BtFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_in_detail1_scan);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        String batchLen = getIntent().getExtras().get("batchLen").toString();
        headcode = getIntent().getExtras().get("headcode").toString();
        ToastUtil.showToast(SampleInActivityDetial1Scan.this, headcode, ToastUtil.Success);

        for (int i = 0; i < Integer.valueOf(batchLen); i++) { //获得所有的 batch list
            Map map = new HashMap();
            map.put("1", String.valueOf(i + 1));
            map.put("2", getIntent().getExtras().get("batchNumber" + String.valueOf(i)).toString());
            list.add(map);
            //batchList.add(getIntent().getExtras().get("batchNumber" + String.valueOf(i)).toString());
        }
    }

    private void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("样品待入库");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarcodeAPI.getInstance().scan();
            }
        });
    }

    private void initView() {
        initTitle();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_material_in_detai_scan, batchList) {
            @Override
            protected void convert(ViewHolder holder, Map s, final int position) {
                holder.setText(R.id.tv1, s.get("1").toString());
                holder.setText(R.id.tv2, s.get("2").toString());
                if (!BtFlag)
                    if (s.get("3").toString().equals("1"))
                        holder.setImageResource(R.id.img, R.mipmap.cross_right);
                    else
                        holder.setImageResource(R.id.img, R.mipmap.cross_false);
                holder.getView(R.id.img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ToastUtil.showToast(MaterialInActivityDetail1Scan.this, String.valueOf(position), ToastUtil.Success);
                        batchList.remove(position);
                        adapter.notifyItemRemoved(position);
                        for (int i = position; i < batchList.size(); i++) {
                            Map map = batchList.get(i);
                            map.put("1", String.valueOf(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        BarcodeAPI.getInstance().open();
        BarcodeAPI.getInstance().setScannerType(20);// 设置扫描头类型(10:5110; 20: N3680 40:MJ-2000)
        int ver = Build.VERSION.SDK_INT;
        BarcodeAPI.getInstance().m_handler = mHandler;
        BarcodeAPI.getInstance().setEncoding("gbk");
        BarcodeAPI.getInstance().setScanMode(true);//打开连扫
        BarcodeAPI.getInstance().scan();

        final String userCode = (String) SPUtil.get(SampleInActivityDetial1Scan.this, GlobalKey.Login.CODE, new String());
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < batchList.size(); i++) {
                    Map map1 = batchList.get(i);
                    for (int j = 0; j < list.size(); j++) {
                        Map map2 = list.get(j);
                        Map map = new HashMap();
                        map.put("1", map1.get("1"));
                        map.put("2", map1.get("2"));
                        if ((map1.get("2").toString()).equals(map2.get("2").toString())) {
                            map.put("3", "1");
                            batchList.set(i, map);
                            break;
                        } else if (j == list.size() - 1) {
                            map.put("3", "0");
                            batchList.set(i, map);
                            BtFlag = false;
                        } else
                            continue;
                    }
                }
                if (!BtFlag) adapter.notifyDataSetChanged();
                if (BtFlag && batchList.size() > 0)
                    EasyHttp.post(GlobalApi.WareHourse.MaterialIn.PATH_Send_Updata)
                            .params(GlobalApi.WareHourse.code, headcode) //
                            .params(GlobalApi.WareHourse.status, "2") //
                            .params(GlobalApi.WareHourse.userCode, userCode) //
                            .sign(true)
                            .timeStamp(true)//本次请求是否携带时间戳
                            .execute(new SimpleCallBack<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    int code = 1;
                                    String message = "出错";
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        code = jsonObject.optInt("code");
                                        message = jsonObject.optString("message");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (code == 0) {
                                        ToastUtil.showToast(SampleInActivityDetial1Scan.this, message, ToastUtil.Success);
                                        finish();
                                    } else {
                                        ToastUtil.showToast(SampleInActivityDetial1Scan.this, message, ToastUtil.Error);
                                    }
                                }

                                @Override
                                public void onError(ApiException e) {
                                    ToastUtil.showToast(SampleInActivityDetial1Scan.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                                }
                            });
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
