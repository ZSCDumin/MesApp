package com.msw.mesapp.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.SharedPreferenceUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.UpdateAppUtils;

public class ChangeServerActivity extends AppCompatActivity {

    @Bind(R.id.server218_rb)
    RadioButton server218Rb;
    @Bind(R.id.server115_rb)
    RadioButton server115Rb;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.check_update_bt)
    Button checkUpdateBt;

    private String server218 = "http://218.77.105.241:30080/mes/";
    private String server115 = "http://115.157.192.47:8080/mes/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_server);
        ButterKnife.bind(this);

        title.setText("设置");
        add.setVisibility(View.INVISIBLE);
        String serverUrl = SharedPreferenceUtils.getString(this, SharedPreferenceUtils.BASEURL);
        if (server218.equals(serverUrl)) {
            server218Rb.setChecked(true);
        } else {
            server115Rb.setChecked(true);
        }
        server115Rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeServer();
            }
        });
        server218Rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeServer();
            }
        });
        getData();
    }

    public void changeServer() {
        if (server115Rb.isChecked()) {
            GlobalApi.BASEURL = server115;
            ToastUtil.showToast(this, "您选择了115服务器", 1);
        } else if (server218Rb.isChecked()) {
            GlobalApi.BASEURL = server218;
            ToastUtil.showToast(this, "您选择了218服务器", 1);
        }
        SharedPreferenceUtils.putString(this, SharedPreferenceUtils.BASEURL, GlobalApi.BASEURL);
    }

    @OnClick({R.id.back, R.id.check_update_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.check_update_bt:
                //检查更新
                if (checkUpdate() == 1) {
                    updateApp();
                } else {
                    ToastUtil.showToast(this, "应用已是最新版本！", 1);
                }
                break;
        }
    }

    private String version = "";
    private String url = "";

    public void getData() {
        EasyHttp.post(GlobalApi.AppUpdate.PATH)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(ChangeServerActivity.this, "获取数据出错", ToastUtil.Error);
                    }

                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray data = jsonObject.optJSONArray("data");
                            if (data != null) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject item = data.getJSONObject(i);
                                    version = item.optString("version");
                                    url = item.optString("url");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private String getVersionName() throws Exception {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }

    /**
     * 检查是否需要更新
     */
    public int checkUpdate() {
        int flag = 1;
        try {
            String version1 = getVersionName();
            if (Double.valueOf(version1) >= Double.valueOf(version)) {
                flag = 0;
            } else {
                flag = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 下载更新包
     */
    public void updateApp() {
        UpdateAppUtils.from(this)
                .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME) //更新检测方式，默认为VersionCode
                .serverVersionCode(1)
                .serverVersionName(version)
                .apkPath(url)
                .showNotification(true) //是否显示下载进度到通知栏，默认为true
                .update();
    }
}
