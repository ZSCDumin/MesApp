package com.msw.mesapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.msw.mesapp.ui.widget.ClearEditText;
import com.msw.mesapp.utils.ACacheUtil;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SharedPreferenceUtils;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.username)
    ClearEditText username;
    @Bind(R.id.password)
    ClearEditText password;
    @Bind(R.id.Login)
    Button Login;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.float_view)
    FloatBackground floatView;
    @Bind(R.id.text_wjmm)
    TextView textWjmm;

    String permission_code = "";

    @Bind(R.id.setting)
    ImageView setting;
    @Bind(R.id.login_with_nfc_tv)
    TextView loginWithNfcTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtils.setActivityTranslucent(this);//全屏
        ButterKnife.bind(this);
        ActivityManager.getAppManager().addActivity(this); //添加当前Activity到Activity列表中
        isUserOn();
        initView();

    }

    //判断用户是否存在
    public void isUserOn() {

        String name = "";
        String serverUrl;
        name = SharedPreferenceUtils.getString(this, GlobalKey.Login.CODE, name);
        serverUrl = SharedPreferenceUtils.getString(this, SharedPreferenceUtils.BASEURL);
        if (serverUrl != null) {
            if (serverUrl.length() > 0)
                GlobalApi.BASEURL = serverUrl;
        }
        if (name != null) {
            if (name.length() > 0) {
                ActivityUtil.switchTo(LoginActivity.this, HomeActivity.class);
                finish();
            }
        }
        ToastUtil.showToast(this, GlobalApi.BASEURL, 1);

    }

    //尝试登录
    public void tryLogin() {
        String s1 = "请输入用户名", s2 = "请输入密码";
        SpannableStringBuilder ssbuilder1 = new SpannableStringBuilder(s1), ssbuilder2 = new SpannableStringBuilder(s2);
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
                .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                    @Override
                    public void onSuccess(String loginModel) {
                        int status = 1;
                        String message = "出错";
                        LoginBean loginBean = null;
                        try {
                            JSONObject jsonObject = new JSONObject(loginModel);
                            status = (int) jsonObject.get("code");
                            message = (String) jsonObject.get("message");
                            JSONObject data = new JSONObject(jsonObject.get("data").toString());

                            loginBean = JSON.parseObject(loginModel, LoginBean.class);

                            JSONArray roles = data.optJSONArray("roles");

                            //获取权限
                            for (int i = 0; i < roles.length(); i++) {
                                JSONObject rolesItem = roles.optJSONObject(i);
                                JSONArray models = rolesItem.optJSONArray("models"); //获取模型
                                for (int j = 0; j < models.length(); j++) {
                                    JSONObject modelsItem = models.optJSONObject(j);
                                    String code = modelsItem.optString("code");
                                    permission_code += (code + "-");
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (status == 0) {
                            //保存用户信息
                            final String url = GlobalApi.BASEURL;
                            SharedPreferenceUtils.putString(LoginActivity.this, GlobalKey.Permission.SPKEY, permission_code);
                            SharedPreferenceUtils.putString(LoginActivity.this, GlobalKey.Login.CODE, name);
                            ACacheUtil.get(LoginActivity.this).put(GlobalKey.Login.DATA, loginModel);
                            //跳转
                            ToastUtil.showToast(LoginActivity.this, message + loginBean.getData().getCode(), ToastUtil.Success);
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

    private void initView() {

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
                        .btnText("开发者：杜敏")
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

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                ToastUtil.showToast(LoginActivity.this, "再按一次退出程序", ToastUtil.Info);
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.setting, R.id.login_with_nfc_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting:
                ActivityUtil.switchTo(this, ChangeServerActivity.class);
                break;
            case R.id.login_with_nfc_tv:
                ActivityUtil.switchTo(this, LoginNfcActivity.class);
                break;
        }
    }
}