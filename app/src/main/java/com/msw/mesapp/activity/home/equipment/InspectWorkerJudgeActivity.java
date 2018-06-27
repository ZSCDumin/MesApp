package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.ActivityUtil;
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

public class InspectWorkerJudgeActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.content_tv)
    TextView contentTv;
    @Bind(R.id.stander_tv)
    TextView standerTv;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.bt2)
    Button bt2;

    private String image = "";
    private String content = "";
    private String standard = "";
    private String tallyTaskHeaderCode = "";
    private String tallyTaskCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_judge);
        ButterKnife.bind(this);
        initData();
        initTitle();
    }

    private void initData() {
        image = getIntent().getExtras().get("7").toString();
        content = getIntent().getExtras().get("1").toString();
        standard = getIntent().getExtras().get("3").toString();
        tallyTaskCode = getIntent().getExtras().get("5").toString();
        tallyTaskHeaderCode = getIntent().getExtras().get("6").toString();
        Glide.with(this).load(GlobalApi.BASEURL + "image/" + image).into(imageView);
        contentTv.setText(content);
        standerTv.setText(standard);
    }


    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("点检巡查");
        add.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.back, R.id.bt1, R.id.bt2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bt1://不合格
                submitData("2");
                break;
            case R.id.bt2://合格
                submitData("1");
                break;
        }
    }

    public void submitData(final String status) {
        EasyHttp.post(GlobalApi.Inspect.Worker.CheckHead.PATH)
            .params(GlobalApi.Inspect.Worker.CheckHead.tallyTaskCode, tallyTaskCode)
            .params(GlobalApi.Inspect.Worker.CheckHead.tallyTaskHeaderCode, tallyTaskHeaderCode)
            .params(GlobalApi.Inspect.Worker.CheckHead.result, status)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        Log.i("TAG", tallyTaskCode + " " + tallyTaskHeaderCode + " " + status);
                        JSONObject jsonObject = new JSONObject(result);
                        String message = jsonObject.optString("message");
                        String code = jsonObject.optString("code");
                        if (code.equals("0")) {
                            ToastUtil.showToast(InspectWorkerJudgeActivity.this, "提交成功", ToastUtil.Success);
                            ActivityUtil.switchTo(InspectWorkerJudgeActivity.this, InspectWorkerActivity.class);
                            finish();
                        } else
                            ToastUtil.showToast(InspectWorkerJudgeActivity.this, message, ToastUtil.Default);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(InspectWorkerJudgeActivity.this, "提交出错", ToastUtil.Error);
                }
            });
    }
}
