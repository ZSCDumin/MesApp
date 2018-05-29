package com.msw.mesapp.activity.home.warehouse;

import android.annotation.SuppressLint;
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
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductOutActivityDetail1Scan extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.cancel)
    Button cancel;
    @Bind(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_out_detail1_scan);
        ButterKnife.bind(this);
        initView();
    }

    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> batchList = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BarcodeAPI.BARCODE_READ:
                    String s = msg.obj.toString().trim();
                    boolean tt = true;
                    for (int i = 0; i < batchList.size(); i++) {
                        Map map = batchList.get(i);
                        if (map.get("2").equals(s))
                            tt = false;
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

    private void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("成品出库");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BarcodeAPI.getInstance().scan();
            }
        });
    }

    boolean BtFlag = true;

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
        BarcodeAPI.getInstance().m_handler = mHandler;
        BarcodeAPI.getInstance().setEncoding("utf8");
        BarcodeAPI.getInstance().setScanMode(true);//打开连扫
        BarcodeAPI.getInstance().scan();
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

    @OnClick({R.id.cancel, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                BarcodeAPI.getInstance().m_handler = null;
                BarcodeAPI.getInstance().close();
                break;
            case R.id.submit:
                //跳转到详细页
                String code = getIntent().getExtras().get("code").toString();
                EasyHttp.post(GlobalApi.WareHourse.ProductOut.updateOutStatusByCode)
                        .params(GlobalApi.WareHourse.code, code)
                        .params(GlobalApi.WareHourse.senderCode, SPUtil.get(this, GlobalKey.Login.CODE, "").toString())
                        .params(GlobalApi.WareHourse.outStatus, "1")
                        .sign(true)
                        .timeStamp(true)//本次请求是否携带时间戳
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onSuccess(String result) {
                                ToastUtil.showToast(ProductOutActivityDetail1Scan.this, "更新出库状态成功", 1);
                            }

                            @Override
                            public void onError(ApiException e) {
                                ToastUtil.showToast(ProductOutActivityDetail1Scan.this, "更新出库状态失败", 1);
                            }
                        });
                Map map = new HashMap();
                map.put("code", getIntent().getExtras().get("code").toString());
                ActivityUtil.switchTo(this, ProductOutActivityDetail2.class, map);
                break;
        }
    }
}
