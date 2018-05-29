package com.msw.mesapp.activity;


import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.bean.LoginBean;
import com.msw.mesapp.ui.background.FloatBackground;
import com.msw.mesapp.ui.background.FloatCircle;
import com.msw.mesapp.ui.background.FloatRing;
import com.msw.mesapp.ui.background.FloatText;
import com.msw.mesapp.ui.widget.ClearEditText;
import com.msw.mesapp.utils.ACacheUtil;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import org.json.JSONArray;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;



public class LoginNfcActivity extends AppCompatActivity {
    String TAG = "LoginActivity";
    @Bind(R.id.username)
    ClearEditText username;
    PendingIntent mPendingIntent;
    @Bind(R.id.float_view)
    FloatBackground floatView;

    String permission_code = "";

    //nfc
    Intent m_intent;
    NfcAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_nfc);
        StatusBarUtils.setActivityTranslucent(this);//全屏
        ButterKnife.bind(this);
        //ToastUtil.showToast(this,"onCreate触发！",ToastUtil.Default);

        initView();
        processTag(getIntent());
        Initialization();//初始化nfc
    }

    private void initView(){
        initFloatView();
    }
    private void initFloatView() {

        floatView.addFloatView(new FloatText(0.1f, 0.2f, "M"));
        floatView.addFloatView(new FloatText(0.2f, 0.21f, "I"));
        floatView.addFloatView(new FloatText(0.3f, 0.18f, "N"));
        floatView.addFloatView(new FloatText(0.4f, 0.21f, "M"));
        floatView.addFloatView(new FloatText(0.5f, 0.19f, "E"));
        floatView.addFloatView(new FloatText(0.6f, 0.21f, "T"));
        floatView.addFloatView(new FloatText(0.7f, 0.2f, "A"));
        floatView.addFloatView(new FloatText(0.8f, 0.18f, "L"));
        floatView.addFloatView(new FloatText(0.9f, 0.2f, "S"));
        floatView.addFloatView(new FloatCircle(0.8f, 0.6f));
        floatView.addFloatView(new FloatCircle(0.6f, 0.8f));
        floatView.addFloatView(new FloatRing(0.4f, 0.8f, 8, 30));
        floatView.addFloatView(new FloatRing(0.6f, 0.5f, 15, 20));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                floatView.startFloat();
            }
        }).start();

    }
    private void Initialization() {
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mAdapter == null) {
            ToastUtil.showToast(this,"设备不支持NFC功能！",ToastUtil.Default);
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }
    public void processTag(Intent intent) {// 处理tag

        if (!NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())){
            //这里我们做了一个判断，即如果返回的不是NFC事件，直接返回，不做处理；
            //return;
        }

        m_intent = intent;
        String str = "";
        Boolean isNdef = false;
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //str += "Tech List:" ;

        for (int i = 0; i < tagFromIntent.getTechList().length; i++) {
            // str+=tagFromIntent.getTechList()[i]+"\r\n";
        }
        //str+="\r\n";
        //str += "Tech List:" + tagFromIntent.getTechList()[0] + "\n";// 打印卡的技术列表
        byte[] aa = tagFromIntent.getId();
        str += bytesToHexString(aa);// 获取卡的UID
        for (String tech : tagFromIntent.getTechList()) {
            if (tech.equals("android.nfc.tech.NfcA")) {
                isNdef = true;
            }
        }
        if (isNdef) {
            NfcA nfcaTag = NfcA.get((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));// 我们的厂牌是NfcA技术,所以生成一个NfcaTag

            try {
                // ndefTag.connect();
                nfcaTag.connect();// 连接卡
                String atqa = "";
                for (byte tmpByte : nfcaTag.getAtqa()) {
                    atqa += tmpByte;
                }
                //str += "tag Atqa:" + bytesToHexString(nfcaTag.getAtqa()) + "\n";// 获取卡的atqa
                //str += "tag SAK:" + nfcaTag.getSak() + "\n";// 获取卡的sak
                //str += "max len:" + nfcaTag.getMaxTransceiveLength() + "\n";// 获取卡片能接收的最大指令长度
                byte[] cmd = null;
                // cmd=new byte[]{0x41,0x54,0x4D,0x0A,0x52,0x01,0x00,0x01,(byte)
                // 0xff,(byte)0xff,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,0x0D};
                cmd = new byte[]{0x60, 0x08, (byte) 0xff, (byte) 0xff,
                        (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};// 卡请求
                // TypeA 类型的第一个数据是0x60，我要想读的扇区是第2扇区的第0块，
                // 也就是第8块的数据，KeyA密码是六个字节的0xff,0xff,0xff,0xff,0xff,0xff
                //str += "Card Number:" + nfcaTag.transceive(cmd);
                // 发送命令到卡片(找不到这个标签扇区读取指令,有知道的同学告诉一声我们的卡是ISO/IEC 14443
                // typeA标准的)，主要就是这个方法得不到返回的结果。因为指令不正确
            } catch (Exception e) {
                e.printStackTrace();
                //str+="\r\nerror:"+e.getMessage();
            } finally {
                try {
                    nfcaTag.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        username.setText(str);

        IProgressDialog mProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(LoginNfcActivity.this);
                dialog.setMessage(GlobalApi.ProgressDialog.Login);
                return dialog;
            }
        };
        EasyHttp.post(GlobalApi.Login.PATHNFC)
                .params(GlobalApi.Login.NFC, str)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                    @Override
                    public void onSuccess(String loginModel) {
                        int status = 1;
                        String message = "出错";
                        LoginBean loginBean = null;
                        try {
                           org.json.JSONObject jsonObject = new org.json.JSONObject(loginModel);
                            status = (int) jsonObject.get("code");
                            message = (String) jsonObject.get("message");
                            org.json.JSONObject data = new org.json.JSONObject(jsonObject.get("data").toString());

                            loginBean = JSON.parseObject(loginModel, LoginBean.class);

                            JSONArray roles = data.optJSONArray("roles");

                            //获取权限
                            for(int i=0;i<roles.length();i++){
                                org.json.JSONObject rolesItem = roles.optJSONObject(i);
                                JSONArray models = rolesItem.optJSONArray("models"); //获取模型
                                for(int j=0;j<models.length();j++){
                                    org.json.JSONObject modelsItem = models.optJSONObject(j);
                                    String code = modelsItem.optString("code");
                                    permission_code += (code + "-");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (status == 0) {
                            //保存用户信息
                            SPUtil.put(LoginNfcActivity.this, GlobalKey.permiss.SPKEY, permission_code);
                            SPUtil.put(LoginNfcActivity.this, GlobalKey.Login.CODE, loginBean.getData().getCode());
                            ACacheUtil.get(LoginNfcActivity.this).put(GlobalKey.Login.DATA, loginModel);
                            //跳转
                            ToastUtil.showToast(LoginNfcActivity.this, "欢迎用户："+ loginBean.getData().getCode(), ToastUtil.Success);
                            ActivityManager.getAppManager().finishAllActivity();
                            ActivityUtil.switchTo(LoginNfcActivity.this, HomeActivity.class);
                            finish();
                        } else {
                            ToastUtil.showToast(LoginNfcActivity.this, message, ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtil.showToast(LoginNfcActivity.this, "登录失败,请查看网络！", ToastUtil.Confusion);
                    }
                });
    }
    private String bytesToHexString(byte[] src) { // 字符序列转换为16进制字符串
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    @Override
    public void onResume() {// 响应intent
        super.onResume();
        //ToastUtil.showToast(this,"onResume触发！",ToastUtil.Default);
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }
    @Override
    protected void onPause() {
        //ToastUtil.showToast(this,"onPause触发！",ToastUtil.Default);
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        //ToastUtil.showToast(LoginNfcActivity.this,"onNewIntent触发！",ToastUtil.Default);
        processTag(intent);
    }
    @Override
    protected void onUserLeaveHint() { //监视home建退出后，关闭程序
        super.onUserLeaveHint();
        finish();
        System.exit(0);
        //System.exit(0);
    }
    @Override
    protected void onDestroy() {
        //ToastUtil.showToast(this,"onDestroy触发！",ToastUtil.Default);
        super.onDestroy();

    }
    //记录用户首次点击返回键的时间
    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                ToastUtil.showToast(LoginNfcActivity.this,"再按一次退出程序",ToastUtil.Info);
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}