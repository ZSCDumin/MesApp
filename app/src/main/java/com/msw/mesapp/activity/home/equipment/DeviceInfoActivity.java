package com.msw.mesapp.activity.home.equipment;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dothantech.lpapi.LPAPI;
import com.msw.mesapp.R;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.printer.OneCode;
import com.msw.mesapp.utils.printer.PrinterUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DeviceInfoActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;

    String ss[] = new String[6];

    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.btn_printer)
    Button btnPrinter;
    @Bind(R.id.qr_id_tv)
    TextView qrIdTv;
    @Bind(R.id.ttv1)
    TextView ttv1;
    @Bind(R.id.ttv2)
    TextView ttv2;
    @Bind(R.id.ttv3)
    TextView ttv3;
    @Bind(R.id.qr_code_iv)
    ImageView qrCodeIv;

    private PrinterUtil printerUtil;
    private LPAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);

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
        title.setText(ss[1]);
        add.setVisibility(View.INVISIBLE);
    }

    public void initData() {
        ss[0] = getIntent().getExtras().get("1").toString(); //code
        ss[1] = getIntent().getExtras().get("2").toString(); //code
        ss[2] = getIntent().getExtras().get("3").toString(); //code
        ss[3] = getIntent().getExtras().get("4").toString(); //code
        ss[4] = getIntent().getExtras().get("5").toString(); //code
        ss[5] = getIntent().getExtras().get("6").toString(); //code

    }

    public void initView() {
        initTitle();
        initBluetooth();

        printerUtil = new PrinterUtil(this, btnPrinter);
        api = printerUtil.api;

        ttv1.setText(ss[0]);
        ttv2.setText(ss[2]);
        ttv3.setText(ss[4]);

        final String pss = ss[0] + "-" + ss[2] + "-" + ss[4];
        qrIdTv.setVisibility(View.GONE);

        qrCodeIv.setImageBitmap(OneCode.creatBarcode(getApplicationContext(), pss, 1000, 400, true));

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printerUtil.printText1DBarcode(pss, pss, null);
            }
        });
    }

    private void initBluetooth() {
        /* 启动蓝牙 */
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null != adapter) {
            if (!adapter.isEnabled()) {
                if (!adapter.enable()) {
                    finish();
                    return;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        printerUtil.fini();
        super.onDestroy();
    }
}