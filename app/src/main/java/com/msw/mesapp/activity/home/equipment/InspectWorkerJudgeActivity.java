package com.msw.mesapp.activity.home.equipment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.ui.widget.EditextDialog;
import com.msw.mesapp.ui.widget.HorizontalProgressBarWithNumber;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.display.FadeInImageDisplayer;
import me.panpf.sketch.request.ShapeSize;

public class InspectWorkerJudgeActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.t1)
    TextView t1;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.t2)
    TextView t2;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.t3)
    TextView t3;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.line)
    LinearLayout line;
    @Bind(R.id.img)
    SketchImageView img;
    @Bind(R.id.horizbar)
    HorizontalProgressBarWithNumber horizbar;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.bt2)
    Button bt2;

    String code = "";
    private String inspectnum = "";
    private String inspectcode = "";
    private String inspecttotal = "";
    private int numInt = 0;

    String checkHeadCode = "";
    String picture = "";
    String standard = "";
    String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_judge);
        ButterKnife.bind(this);
        initData();
        initView();
        horizbar.setProgress((int) (100 * ( (Float.valueOf(inspecttotal) - numInt + 1.0)  / Float.valueOf(inspecttotal))) );
    }

    private void initData() {
        code = getIntent().getExtras().get("code").toString(); //code
        inspectnum = (String) SPUtil.get(InspectWorkerJudgeActivity.this, "inspectnum", inspectnum);
        inspectcode = (String) SPUtil.get(InspectWorkerJudgeActivity.this, "inspectcode", inspectcode);
        inspecttotal = (String) SPUtil.get(InspectWorkerJudgeActivity.this, "inspecttotal", inspecttotal);
        numInt = Integer.valueOf(inspectnum);

        EasyHttp.post(GlobalApi.Inspect.Worker.CheckHead.PATH)
                .params(GlobalApi.Inspect.Worker.CheckHead.code, code)
                .sign(true)
                .timeStamp(true)//本次请求是否携带时间戳
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        int code = 1;
                        String message = "出错";
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            code = (int) jsonObject.get("code");
                            message = (String) jsonObject.get("message");
                            JSONObject data = jsonObject.getJSONObject("data");

                            JSONObject check = data.getJSONObject("check"+String.valueOf(numInt));

                            checkHeadCode = data.optString("code");
                            picture = check.optString("picture");
                            standard = check.optString("standard");
                            content = check.optString("content");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            initView();
                        } else {
                            ToastUtil.showToast(InspectWorkerJudgeActivity.this, message, ToastUtil.Error);
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(InspectWorkerJudgeActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
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

    private void initBanner() {
        final String jpgUrl = picture;
        img.getOptions()
                .setCacheInDiskDisabled(false) //是否缓存
                .setDecodeGifImage(true) //显示gif
                .setLoadingImage(R.mipmap.ic_autorenew) //加载时的图片
                .setErrorImage(R.mipmap.defualt_inspect)  //错误时的图片
                //.setResize(new Resize(ActivityUtil.getScreenWidth(this),400, Resize.Mode.EXACTLY_SAME))
                .setShapeSize(new ShapeSize(ActivityUtil.getScreenWidth(this), 460, ImageView.ScaleType.FIT_XY))
                .setDisplayer(new FadeInImageDisplayer()); //显示图片的动画
        img.setZoomEnabled(true); //开启手势
        //img.setShowImageFromEnabled(true); //调试用的
        img.displayImage(jpgUrl);
        img.setClickRetryOnDisplayErrorEnabled(true);//加载失败时点击重新加载
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InspectWorkerJudgeActivity.this, PhotoviewActivity.class);
                intent.putExtra("url", jpgUrl);//传递点击图片网址
                startActivity(intent);
            }
        });


    }

    public void initView() {
        initTitle();
        initBanner();
        setTextMarquee(tv1);
        setTextMarquee(tv2);
        setTextMarquee(tv3);
        tv1.setText(content);
        tv2.setText(standard);
        if (numInt == 1) bt2.setText("完成");
        final boolean[] resFlag = {false};
        final String[] resStr = {""};
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BaseAnimatorSet bas_in = new SlideBottomEnter();
                BaseAnimatorSet bas_out = new FadeExit();
                final EditextDialog editextDialog = new EditextDialog(InspectWorkerJudgeActivity.this);
                editextDialog.onCreateView();
                editextDialog.setUiBeforShow();
                editextDialog.showAnim(bas_in);
                editextDialog.dismissAnim(bas_out);
                editextDialog.setCanceledOnTouchOutside(true);//点击空白区域能不能退出
                editextDialog.setCancelable(true);//按返回键能不能退出
                editextDialog.show();
                editextDialog.setOnClickListener(new EditextDialog.OnOKClickListener() {
                    @Override
                    public void onOKClick(EditText et) {
                        String ss = et.getText().toString();
                        if (ss.length() > 0) {
                            line.setVisibility(View.VISIBLE);
                            tv3.setText(ss);
                            bt2.setText("下一项");
                            if (numInt == 1) bt2.setText("完成");
                            resFlag[0] = true;
                            resStr[0] = ss;
                            //ToastUtil.showToast(InspectWorkerJudgeActivity.this, ss, ToastUtil.Info);
                        }
                    }
                });
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resFlag[0]) {
                    new InspectVar().getInstance().workerRES1.add("1"); //不合格
                    new InspectVar().getInstance().workerRES2.add(resStr[0]);
                } else {
                    new InspectVar().getInstance().workerRES1.add("0"); //合格
                    new InspectVar().getInstance().workerRES2.add("");
                }
                if (numInt == 1) {
                    EasyHttp.post(GlobalApi.Inspect.Worker.updateTime.PATH)
                            .params(GlobalApi.Inspect.Worker.updateTime.code, inspectcode)
                            .params(GlobalApi.Inspect.Worker.updateTime.updateTime, DateUtil.getOldorNewDate(1)) //将日期改为下一天
                            .sign(true)
                            .timeStamp(true)//本次请求是否携带时间戳
                            .execute(new SimpleCallBack<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    int code = 1;
                                    String message = "出错";
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        code = (int) jsonObject.get("code");
                                        message = (String) jsonObject.get("message");

                                    }catch (Exception e) { e.printStackTrace(); }
                                    if (code == 0) {
                                        ToastUtil.showToast(InspectWorkerJudgeActivity.this, message, ToastUtil.Success);
                                    } else {
                                        ToastUtil.showToast(InspectWorkerJudgeActivity.this, message, ToastUtil.Error);
                                    }
                                }
                                @Override
                                public void onError(ApiException e) {
                                    ToastUtil.showToast(InspectWorkerJudgeActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                                }
                            });

                    int tt = new InspectVar().getInstance().workerRES1.size();
                    for( ;tt<=14;tt++){
                        new InspectVar().getInstance().workerRES1.add("");
                        new InspectVar().getInstance().workerRES2.add("");
                    }
                    int ii = 0;

                    EasyHttp.post(GlobalApi.Inspect.Worker.AppCheck.PATH)
                            .params(GlobalApi.Inspect.Worker.AppCheck.checkHeadCode, checkHeadCode) //表头编码（确定从属车间）
                            .params(GlobalApi.Inspect.Worker.AppCheck.checkPerson, (String) SPUtil.get(InspectWorkerJudgeActivity.this, GlobalKey.Login.CODE, code)) //巡检人
                            .params(GlobalApi.Inspect.Worker.AppCheck.time, DateUtil.getCurrentDate2()) //
                            .params(GlobalApi.Inspect.Worker.AppCheck.examState, "1") //1是已巡检但未审核

                            .params(GlobalApi.Inspect.Worker.AppCheck.state1,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal1,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state2,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal2,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state3,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal3,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state4,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal4,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state5,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal5,new InspectVar().getInstance().workerRES2.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state6,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal6,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state7,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal7,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state8,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal8,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state9,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal9,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state10,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal10,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state11,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal11,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state12,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal12,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state13,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal13,new InspectVar().getInstance().workerRES2.get(ii++))
                            .params(GlobalApi.Inspect.Worker.AppCheck.state14,new InspectVar().getInstance().workerRES1.get(ii))
                            .params(GlobalApi.Inspect.Worker.AppCheck.abnormal14,new InspectVar().getInstance().workerRES2.get(ii++))

                            .sign(true)
                            .timeStamp(true)//本次请求是否携带时间戳
                            .execute(new SimpleCallBack<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    int code = 1;
                                    String message = "出错";
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        code = (int) jsonObject.get("code");
                                        message = (String) jsonObject.get("message");

                                    }catch (Exception e) { e.printStackTrace(); }
                                    if (code == 0) {
                                        ToastUtil.showToast(InspectWorkerJudgeActivity.this, message, ToastUtil.Success);
                                        ActivityUtil.switchTo(InspectWorkerJudgeActivity.this, InspectWorkerActivity.class);

                                        ActivityManager.getAppManager().finishAllActivity();//结束所有activity
                                        finish();
                                    } else {
                                        ToastUtil.showToast(InspectWorkerJudgeActivity.this, message, ToastUtil.Error);
                                    }
                                }
                                @Override
                                public void onError(ApiException e) {
                                    ToastUtil.showToast(InspectWorkerJudgeActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                                }
                            });

                } else {
                    Map map = new HashMap();
                    map.put("code",code); //把code传过去
                    ActivityUtil.switchTo(InspectWorkerJudgeActivity.this, InspectWorkerJudgeActivity.class,map);
                    SPUtil.put(InspectWorkerJudgeActivity.this, "inspectnum", String.valueOf(numInt - 1));
                    ActivityManager.getAppManager().addActivity(InspectWorkerJudgeActivity.this);

                }
            }
        });
    }

    public void setTextMarquee(TextView textView) {
        if (textView != null) {
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine(true);
            textView.setSelected(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
        }
    }

    //图片加载
//    public class ImageApp extends ImageLoader {
//
//        @Override
//        public void displayImage(Context context, Object path, final ImageView imageView) {
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);//等比例放大图片
//            //不缓存图片，加载失败显示图片，
//            Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.defualt_inspect).into(imageView);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
