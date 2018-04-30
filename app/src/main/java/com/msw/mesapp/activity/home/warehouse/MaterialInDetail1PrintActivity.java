package com.msw.mesapp.activity.home.warehouse;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dothantech.lpapi.LPAPI;
import com.msw.mesapp.R;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.printer.PrinterUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MaterialInDetail1PrintActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    TextView add;
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.btn_printer)
    Button btnPrinter;
    private PrinterUtil printerUtil;
    private LPAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_in_detail1_print);
        ButterKnife.bind(this);
        initData();
        initView();
    }


    private void initData() {
    }

    private void initView() {
        initBluetooth();
        initTitle();


        printerUtil = new PrinterUtil(this,btnPrinter);
        api = printerUtil.api;

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //printerUtil.printText1DBarcode("湖南大学", "123456789066", null);
                api.startJob(48, 48, 0);
                api.draw2DQRCode("你大爷",4,4,20);
                printerUtil.onPrintState(api.commitJob());
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
    private void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("取样通知单");
        add.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onDestroy() {
        printerUtil.fini();
        super.onDestroy();
    }
}
