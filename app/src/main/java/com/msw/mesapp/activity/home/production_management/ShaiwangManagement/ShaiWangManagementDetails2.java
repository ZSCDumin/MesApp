package com.msw.mesapp.activity.home.production_management.ShaiwangManagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.utils.CompressUtil;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShaiWangManagementDetails2 extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.take_photo_bt)
    Button takePhotoBt;
    @Bind(R.id.re_takephoto_bt)
    Button reTakephotoBt;
    @Bind(R.id.submit_bt)
    Button submitBt;
    private File mPhotoFile;
    private String mPhotoPath;
    private String mPhotoPath1;
    public final static int CAMERA_RESULT = 1;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private String shakerName = "";
    public String message;
    private String imageCode = "";
    private String shakerCode = "";
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    Log.i("TAG", message);
                    if (message.equals("成功")) {
                        ToastUtil.showToast(ShaiWangManagementDetails2.this, message, ToastUtil.Success);
                        finish();
                    }
                    break;
                case 0x102:
                    submitData();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shai_wang_management_details2);
        ButterKnife.bind(this);
        shakerName = getIntent().getExtras().get("shakerName").toString();
        shakerCode = getIntent().getExtras().get("shakerCode").toString();
        intiView();
    }

    public void intiView() {
        title.setText(shakerName + "筛网");
        add.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.back, R.id.take_photo_bt, R.id.re_takephoto_bt, R.id.submit_bt})
    public void onViewClicked(View view) {
        checkPermission();
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.take_photo_bt:
                //拍照
                takePhoto();
                break;
            case R.id.re_takephoto_bt:
                //重拍
                takePhoto();
                break;
            case R.id.submit_bt:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateImage();
                    }
                }).start();
                break;
        }
    }


    public void updateImage() {
        OkHttpClient client = new OkHttpClient();
        File file = new File(mPhotoPath1);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(GlobalApi.ProductManagement.ShaiwangCheck.file, "after", fileBody)
            .build();
        Request request = new Request.Builder()
            .url(GlobalApi.BASEURL + GlobalApi.ProductManagement.ShaiwangCheck.upload)
            .post(requestBody)
            .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();
            JSONObject jsonObject = new JSONObject(result);
            imageCode = jsonObject.optJSONObject("data").optString("code");
            if (imageCode.length() > 0)
                handler.sendEmptyMessage(0x102);//通过handler发送一个更新数据的标记
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }


    public void submitData() {

        Log.i("TAG", imageCode + "---" + shakerCode + "---" + GetCurrentUserIDUtil.currentUserId(this) + "---" + new Date().getTime() + "---" + "1");

        EasyHttp.post(GlobalApi.ProductManagement.ShaiwangCheck.add)
            .params(GlobalApi.ProductManagement.ShaiwangCheck.picture, imageCode)
            .params(GlobalApi.ProductManagement.ShaiwangCheck.shakerCode, shakerName)
            .params(GlobalApi.ProductManagement.ShaiwangCheck.inspectorCode, GetCurrentUserIDUtil.currentUserId(this))
            .params(GlobalApi.ProductManagement.ShaiwangCheck.inspectorTime, DateUtil.getDateToString(new Date().getTime()))
            .params(GlobalApi.ProductManagement.ShaiwangCheck.state, "1")
            .execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ToastUtil.showToast(ShaiWangManagementDetails2.this, "提交失败", ToastUtil.Error);
                }

                @Override
                public void onSuccess(String s) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        message = jsonObject.optString("message");
                        handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }
            });
    }

    public void takePhoto() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//开始拍照
            mPhotoPath = getSDPath() + "/" + "before.png";//设置图片文件路径，getSDPath()和getPhotoFileName()具体实现在下面
            mPhotoPath1 = getSDPath() + "/" + "after.png";
            mPhotoFile = new File(mPhotoPath);
            if (!mPhotoFile.exists()) {
                mPhotoFile.createNewFile();//创建新文件
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,//Intent有了图片的信息
                Uri.fromFile(mPhotoFile));
            startActivityForResult(intent, CAMERA_RESULT);//跳转界面传回拍照所得数据
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
            .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RESULT) {
            CompressUtil.compressImage(mPhotoPath, mPhotoPath1, 50);
            Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath1);
            Log.i("TAG", bitmap.getRowBytes() * bitmap.getHeight() / 8 / 1024 + "KB");
            imageView.setImageBitmap(bitmap);
        }
    }

    public void checkPermission() {
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
            new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            }
        );
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            return;
        }
        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
            this,
            new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            },
            MY_PERMISSION_REQUEST_CODE
        );
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
