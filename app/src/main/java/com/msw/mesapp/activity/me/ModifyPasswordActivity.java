package com.msw.mesapp.activity.me;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.ui.background.FloatBackground;
import com.msw.mesapp.ui.background.FloatCircle;
import com.msw.mesapp.ui.background.FloatRect;
import com.msw.mesapp.ui.background.FloatRing;
import com.msw.mesapp.ui.background.FloatText;
import com.msw.mesapp.ui.widget.ClearEditText;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.ProgressDialogCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.subsciber.IProgressDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ModifyPasswordActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.change_old)
    ClearEditText changeOld;
    @Bind(R.id.change_new)
    ClearEditText changeNew;
    @Bind(R.id.change_new2)
    ClearEditText changeNew2;
    @Bind(R.id.change_bt_submit)
    Button changeBtSubmit;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.float_view)
    FloatBackground floatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);
        initTitle();
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        initFloatView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("修改密码");
        add.setVisibility(View.INVISIBLE);

        changeBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = "", o = "", n0 = "", n1 = "";
                code = (String) SPUtil.get(ModifyPasswordActivity.this, GlobalKey.Login.CODE, code);
                o = changeOld.getText().toString();
                n0 = changeNew.getText().toString();
                n1 = changeNew2.getText().toString();

                updatePassWord(code, o, n0, n1);

            }
        });
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

    private void updatePassWord(String code, String o, String n0, String n1) {
        IProgressDialog mProgressDialog = new IProgressDialog() {
            @Override
            public Dialog getDialog() {
                ProgressDialog dialog = new ProgressDialog(ModifyPasswordActivity.this);
                dialog.setMessage(GlobalApi.ProgressDialog.UpdatePassWord);
                return dialog;
            }
        };
        EasyHttp.post(GlobalApi.UpdatePassword.PATH)
                .params(GlobalApi.UpdatePassword.CODE, code)
                .params(GlobalApi.UpdatePassword.OLDPASS, o)
                .params(GlobalApi.UpdatePassword.NEWPASS, n0)
                .params(GlobalApi.UpdatePassword.RENEWPASS, n1)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new ProgressDialogCallBack<String>(mProgressDialog, true, true) {
                    @Override
                    public void onSuccess(String loginModel) {
                        int code = 1;
                        String message = "出错";
                        try {
                            JSONObject jsonObject = JSON.parseObject(loginModel);
                            code = (int) jsonObject.get("code");
                            message = (String) jsonObject.get("message");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            //跳转
                            ToastUtil.showToast(ModifyPasswordActivity.this,message,ToastUtil.Success);
                            finish();
                        } else {
                            ToastUtil.showToast(ModifyPasswordActivity.this,message,ToastUtil.Error);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ToastUtil.showToast(ModifyPasswordActivity.this,"登录失败,请查看网络！",ToastUtil.Confusion);
                    }
                });
    }

    private void initFloatView(){

        floatView.addFloatView(new FloatText( 0.3f, 0.6f, "P"));
        floatView.addFloatView(new FloatText( 0.5f, 0.6f, "A"));
        floatView.addFloatView(new FloatText( 0.5f, 0.6f, "S"));
        floatView.addFloatView(new FloatText( 0.4f, 0.6f, "S"));
        floatView.addFloatView(new FloatText( 0.5f, 0.2f, "^_^"));
        floatView.addFloatView(new FloatText( 0.7f, 0.2f, "( ´･ω･)ﾉ(._.`)摸摸头"));
        floatView.addFloatView(new FloatRect(0.2f, 0.5f, 170, 30));
        floatView.addFloatView(new FloatCircle( 0.8f, 0.6f));
        floatView.addFloatView(new FloatCircle( 0.6f, 0.8f));
        floatView.addFloatView(new FloatRing( 0.4f, 0.8f, 10 ,40));
        floatView.addFloatView(new FloatRing( 0.6f, 0.2f, 15 ,20));

    }
}
