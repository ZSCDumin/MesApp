package com.msw.mesapp.activity.home.production_management.ShaiwangManagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShaiWangManagementDetails3 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.imageView)
    ImageView imageView;
    private String shakerName = "";
    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shai_wang_management_details3);
        ButterKnife.bind(this);
        shakerName = getIntent().getExtras().get("shakerName").toString();
        code = getIntent().getExtras().get("code").toString();
        intiView();
        getData();
    }

    public void intiView() {
        title.setText(shakerName + "筛网");
        add.setVisibility(View.INVISIBLE);
    }

    public void getData() {

        EasyHttp.post(GlobalApi.ProductManagement.ShaiwangCheck.getById)
            .params(GlobalApi.ProductManagement.ShaiwangCheck.code, code)
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(ShaiWangManagementDetails3.this, "获取数据失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject data = jsonObject.optJSONObject("data");
                        JSONObject inspector = data.optJSONObject("inspector");
                        String checker = inspector.optString("name");
                        String checkTime = data.optString("inspectorTime");
                        checkTime = DateUtil.getDateToString(checkTime);
                        String picture = data.optString("picture");
                        tv1.setText(checker);
                        tv2.setText(checkTime);
                        Glide.with(ShaiWangManagementDetails3.this).load(GlobalApi.BASEURL + "image/" + picture).into(imageView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
