package com.msw.mesapp.activity;


import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.bean.LoginBean;
import com.msw.mesapp.ui.background.FloatBackground;
import com.msw.mesapp.ui.widget.ClearEditText;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.ActivityUtil;
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


public class LoginNfcActivity extends AppCompatActivity {

    String TAG = "LoginNFCActivity";
    @Bind(R.id.username)
    ClearEditText username;
    PendingIntent mPendingIntent;
    @Bind(R.id.float_view)
    FloatBackground floatView;
    String permission_code = "";
    NfcAdapter mAdapter;
    @Bind(R.id.Login)
    Button Login;
    @Bind(R.id.login_with_account_tv)
    TextView loginWithAccountTv;

    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_nfc);
        StatusBarUtils.setActivityTranslucent(this);//全屏
        ButterKnife.bind(this);
        // 获取默认的NFC控制器
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


    public void LoginWIthNFC(String interCardID) {

        IProgressDialog mProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(LoginNfcActivity.this);
                dialog.setMessage(GlobalApi.ProgressDialog.Login);
                return dialog;
            }
        };
        EasyHttp.post(GlobalApi.Login.PATHNFC)
                .params(GlobalApi.Login.NFC, interCardID)
                .sign(true)
                .timeStamp(true)
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
                            //跳转
                            ToastUtil.showToast(LoginNfcActivity.this, "欢迎用户：" + loginBean.getData().getCode(), ToastUtil.Success);
                            finish();
                            ActivityUtil.switchTo(LoginNfcActivity.this, HomeActivity.class);
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
            id = bytesToHexString(bytesId);
            username.setText(id);
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

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                ToastUtil.showToast(LoginNfcActivity.this, "再按一次退出程序", ToastUtil.Info);
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                ActivityManager.getAppManager().AppExit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.Login, R.id.login_with_account_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Login:
                LoginWIthNFC(id);
                break;
            case R.id.login_with_account_tv:
                ActivityUtil.switchTo(this, LoginActivity.class);
                break;
        }
    }
}