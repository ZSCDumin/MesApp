package com.msw.mesapp.activity.component;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.msw.mesapp.R;
import com.msw.mesapp.utils.ActivityUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QRCodeActivity extends AppCompatActivity implements QRCodeView.Delegate{

    @Bind(R.id.zxingview)
    ZXingView zxingview;
    @Bind(R.id.open_flashlight)
    Button openFlashlight;

    Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);

        zxingview .setDelegate(this);

        zxingview.startCamera();
        zxingview.startSpot();

        openFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    openFlashlight.setText("关闭");
                    zxingview.openFlashlight();//开灯
                    flag=false;
                }else{
                    openFlashlight.setText("开灯");
                    zxingview.closeFlashlight();
                    flag=true;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        zxingview.startCamera();
        zxingview.startSpot();
    }
    @Override
    protected void onStop() {
        zxingview.stopCamera();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        zxingview.onDestroy();
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            Intent mIntent = new Intent();
            mIntent.putExtra("qrcode", "");
            this.setResult(1,mIntent);
            zxingview.stopCamera();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //返回二维码识别结果
    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        ActivityUtil.toastShow(QRCodeActivity.this,"识别结果："+result);
        Intent mIntent = new Intent();
        mIntent.putExtra("qrcode", result);
        this.setResult(1,mIntent);
        zxingview.stopCamera();
        finish();
    }
    @Override
    public void onScanQRCodeOpenCameraError() {
        ActivityUtil.toastShow(QRCodeActivity.this,"读取二维码失败");
    }
    //震动
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
