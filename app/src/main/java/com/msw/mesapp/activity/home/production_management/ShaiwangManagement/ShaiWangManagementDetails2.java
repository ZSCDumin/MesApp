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
import com.msw.mesapp.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x101:
                    ToastUtil.showToast(ShaiWangManagementDetails2.this, message, ToastUtil.Default);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shai_wang_management_details2);
        ButterKnife.bind(this);
        intiView();
    }

    public void intiView() {

        title.setText("41号筛网");
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
                        submitData();
                    }
                }).start();
                //ActivityUtil.switchTo(this, ShaiWangManagement.class);
                break;
        }
    }

    public String message;

    public void submitData() {
        OkHttpClient client = new OkHttpClient();
        File file = new File(mPhotoPath1);
        Request request = new Request.Builder()
                .url(GlobalApi.BASEURL + "image/upload")
                .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), file))
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String result = response.body().string();
            JSONObject jsonObject = new JSONObject(result);
            message = jsonObject.optString("message");
            handler.sendEmptyMessage(0x101);//通过handler发送一个更新数据的标记
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void takePhoto() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//开始拍照
            mPhotoPath = getSDPath() + "/" + "before.jpg";//设置图片文件路径，getSDPath()和getPhotoFileName()具体实现在下面
            mPhotoPath1 = getSDPath() + "/" + "after.jpg";
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
