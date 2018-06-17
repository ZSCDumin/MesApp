package com.msw.mesapp.activity.home.id_management;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemberNameActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.bind_bt)
    Button bindBt;
    @Bind(R.id.cardId_tv)
    TextView cardIdTv;

    private String name = "";
    private String code = "";
    private String interCardId = "";
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_name);
        ButterKnife.bind(this);
        name = getIntent().getExtras().get("name").toString();
        code = getIntent().getExtras().get("code").toString();
        initTitle();
        initNFC();
    }

    public void initNFC() {
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            ToastUtil.showToast(this, "手机系统不支持NFC功能！", ToastUtil.Warning);
            finish();
            return;
        }
        if (!mAdapter.isEnabled()) {
            ToastUtil.showToast(this, "请在系统设置中先启用NFC功能！", ToastUtil.Warning);
            finish();
            return;
        }
        mPendingIntent = PendingIntent
                .getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText(name);
        add.setVisibility(View.INVISIBLE);
        Animation circle_anim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            imageView.startAnimation(circle_anim);  //开始动画
        }
    }

    public void binding() {
        EasyHttp.post(GlobalApi.IdManagement.bindCard)
                .params(GlobalApi.IdManagement.code, code)
                .params(GlobalApi.IdManagement.enableIc, "1")
                .params(GlobalApi.IdManagement.inteCircCard, interCardId)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(MemberNameActivity.this, "绑定失败", ToastUtil.Error);
                    }

                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.optString("code");
                            String message = jsonObject.optString("message");
                            if (code.equals("0"))
                                ToastUtil.showToast(MemberNameActivity.this, "绑定成功", ToastUtil.Success);
                            else
                                ToastUtil.showToast(MemberNameActivity.this, message, ToastUtil.Default);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onNewIntent(Intent paramIntent) {
        setIntent(paramIntent);
        resolveIntent(paramIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mAdapter == null)
            return;
        if (!this.mAdapter.isEnabled()) {
            ToastUtil.showToast(this, "请在系统设置中先启用NFC功能！", ToastUtil.Warning);
        }
        this.mAdapter.enableForegroundDispatch(this, this.mPendingIntent, null, null);
    }


    protected void resolveIntent(Intent intent) {

        // 得到是否检测到TAG触发
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            // 获取标签id数组
            byte[] bytesId = tag.getId();
            interCardId = bytesToHexString(bytesId);
            cardIdTv.setText("卡号：" + interCardId);
            Log.i("ID", interCardId);
        }
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.bind_bt)
    public void onViewClicked() {
        binding();
    }
}