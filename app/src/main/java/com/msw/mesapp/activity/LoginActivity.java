package com.msw.mesapp.activity;


import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
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
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

//https://github.com/huangdali/newkjdemo/
//okhttp3:http://blog.csdn.net/itachi85/article/details/51190687

/**
 * 登录页面
 * (看例子之前看一遍下面直白的解释,看完之后再看一遍就更明白MVP模式了)
 * --------M层   对P层传递过来的userInfo进行登录(网络请求)判断,处理完成之后将处理结果回调给P层
 * --------P层   传递完数据给M层处理之后,实例化回调对象,成功了就通知V层登录成功,失败了就通知V层显示错误信息
 * --------V层   负责响应用户的交互(获取数据---->提示操作结果)
 */
public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivity";
    @Bind(R.id.username)
    ClearEditText username;
    @Bind(R.id.password)
    ClearEditText password;
    @Bind(R.id.Login)
    Button Login;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    PendingIntent mPendingIntent;
    @Bind(R.id.float_view)
    FloatBackground floatView;
    @Bind(R.id.text_wjmm)
    TextView textWjmm;

    //nfc
    String strUI = "";
    Intent m_intent;
    NfcAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtils.setActivityTranslucent(this);//全屏
        ButterKnife.bind(this);

        isUserOn();
        initView();

    }

    //判断用户是否存在
    public void isUserOn() {

        String name = "";
        name = (String) SPUtil.get(LoginActivity.this, GlobalKey.Login.CODE, name);
        if (name.length() > 0) {
            //ToastUtil.showToast(this, "欢迎用户：" + name, ToastUtil.Success);
            ActivityUtil.switchTo(LoginActivity.this, HomeActivity.class);
            finish();
        }

    }

    //尝试登录
    public void tryLogin() {
        String s1 = "请输入用户名",s2="请输入密码";
        SpannableStringBuilder ssbuilder1 = new SpannableStringBuilder(s1),ssbuilder2 = new SpannableStringBuilder(s2);
        ssbuilder1.setSpan(new ForegroundColorSpan(Color.RED), 0, s1.length(), 0);
        ssbuilder2.setSpan(new ForegroundColorSpan(Color.RED), 0, s2.length(), 0);

        String usernameStr = username.getText().toString();
        String passwordStr = password.getText().toString();

        if (usernameStr.trim().isEmpty()) {
            username.setError(ssbuilder1);
        } else if (passwordStr.trim().isEmpty()) {
            password.setError(ssbuilder2);
        } else {
            onLogin(usernameStr, passwordStr);
        }
    }

    //正在登录
    public void onLogin(final String name, final String pass) {
        IProgressDialog mProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage(GlobalApi.ProgressDialog.Login);
                return dialog;
            }
        };
        EasyHttp.post(GlobalApi.Login.PATH)
                .params(GlobalApi.Login.CODE, name)
                .params(GlobalApi.Login.PASSWORD, pass)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                //.cacheMode(CacheMode.FIRSTREMOTE)//先请求网络，请求网络失败后再加载缓存
                //.cacheKey(GlobalKey.Login.DATA)//缓存key
                //.retryCount(5)//重试次数
                .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                    @Override
                    public void onSuccess(String loginModel) {
                        int status = 1;
                        String message = "出错";
                        LoginBean loginBean = null;
                        try {
                            JSONObject jsonObject = JSON.parseObject(loginModel);
                            status = (int) jsonObject.get("code");
                            message = (String) jsonObject.get("message");
                            loginBean = JSON.parseObject(loginModel, LoginBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (status == 0) {
                            //保存用户信息
                            SPUtil.put(LoginActivity.this, GlobalKey.Login.CODE, name);
                            ACacheUtil.get(LoginActivity.this).put(GlobalKey.Login.DATA, loginModel);
                            //跳转
                            ToastUtil.showToast(LoginActivity.this, message+loginBean.getData().getCode(), ToastUtil.Success);
                            ActivityUtil.switchTo(LoginActivity.this, HomeActivity.class);
                            finish();
                        } else {
                            ToastUtil.showToast(LoginActivity.this, message, ToastUtil.Error);
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtil.showToast(LoginActivity.this, "登录失败,请查看网络！", ToastUtil.Confusion);
                    }
                });
    }
    private void initView(){
        initFloatView();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLogin();
            }
        });

        textWjmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseAnimatorSet bas_in = new FlipVerticalSwingEnter();
                BaseAnimatorSet bas_out = new FadeExit();
                final NormalDialog dialog = new NormalDialog(LoginActivity.this);
                dialog.content("请联系管理人员！")//
                        .btnNum(1)
                        .btnText("666666")
                        .showAnim(bas_in)//
                        .dismissAnim(bas_out)//
                        .show();
                dialog.setOnBtnClickL(new OnBtnClickL() {
                                          @Override
                                          public void onBtnClick() {
                                              dialog.dismiss();
                                          }
                                      }
                );
            }
        });
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

    @Override
    public void onResume() {// 响应intent
        super.onResume();
        //ToastUtil.showToast(this,"onResume触发！",ToastUtil.Default);
    }
    @Override
    protected void onPause() {
        //ToastUtil.showToast(this,"onPause触发！",ToastUtil.Default);
        super.onPause();
    }
    @Override
    public void onNewIntent(Intent intent) {
        //ToastUtil.showToast(LoginActivity.this,"onNewIntent触发！",ToastUtil.Default);
    }
    @Override
    protected void onUserLeaveHint() { //监视home建退出后，关闭程序
        super.onUserLeaveHint();
        //finish();
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
                //Toast.makeText(HomeActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                ToastUtil.showToast(LoginActivity.this,"再按一次退出程序",ToastUtil.Info);
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