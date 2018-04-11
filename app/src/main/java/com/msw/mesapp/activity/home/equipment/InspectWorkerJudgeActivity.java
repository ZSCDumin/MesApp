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
import com.msw.mesapp.ui.widget.EditextDialog;
import com.msw.mesapp.ui.widget.HorizontalProgressBarWithNumber;
import com.msw.mesapp.utils.ActivityManager;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.SPUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;

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

    private String num = "";
    private int numInt = 0;

    private static String[] content = {
            "机器表面清洁及周边清理"
            , "检查炉体两侧挂板是否完好"
            , "周边工装盘、转运车摆放"
            , "检查转运滑轮、链条防护罩等"
            , "检查控制面板/控制按钮"
            , "警示灯运转是否正常"
            , "三色灯是否正常"
            , "检查限位器、行程开关是否正常"
            , "检查风机运转"
            , "辊道电机减速箱"
            , "辊道电机检查"
            , "检查辊棒与炉体"
            , "检查硅碳棒"
            , "氧气管道检查"
    };
    private static String[] standard = {
            "无杂物、灰尘"
            , "无变形、缺失、破损"
            , "整齐不杂乱"
            , "是否损坏，缺失"
            , "操作正常、无损坏"
            , "功能正常，设备异常时可正常报警"
            , "绿色：运行、黄色：开机未运行、红色：停机"
            , "产品进出时，能有效检测辊道启动或停止"
            , "无异常声响"
            , "是否有漏油、渗油、是否有油渍"
            , "耳听：无异常声响;触摸：手心放置于电机尾部，是否有风感"
            , "各传动轴承运转无卡阻"
            , "从炉头观察是否掉落、断裂"
            , "耳听：无异常声响（漏气声）"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_judge);
        ButterKnife.bind(this);
        initData();
        initView();

        horizbar.setProgress(100 * (numInt));
    }

    private void initData() {
        num = (String) SPUtil.get(InspectWorkerJudgeActivity.this, "inspectnum", num);
        numInt = Integer.valueOf(num);
    }

    public void initTitle() {
        StatusBarUtils.setActivityTranslucent(this); //设置全屏
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("点检巡查" + (numInt + 1));
        add.setVisibility(View.INVISIBLE);
    }

    private void initBanner() {
        final String jpgUrl = "http://img.orz520.com/t0162c63eb1057c0cd2.jpg";
        img.getOptions()
                .setCacheInDiskDisabled(false) //是否缓存
                .setDecodeGifImage(true) //显示gif
                .setLoadingImage(R.mipmap.ic_autorenew) //加载时的图片
                .setErrorImage(R.mipmap.defualt_inspect)  //错误时的图片
                //.setResize(new Resize(ActivityUtil.getScreenWidth(this),400, Resize.Mode.EXACTLY_SAME))
                .setShapeSize(new ShapeSize(ActivityUtil.getScreenWidth(this), 420, ImageView.ScaleType.FIT_XY))
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
        tv1.setText(InspectWorkerJudgeActivity.content[numInt]);
        tv2.setText(InspectWorkerJudgeActivity.standard[numInt]);
        if (numInt == 0) bt2.setText("完成");
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
                            if (numInt == 0) bt2.setText("完成");
                            resFlag[0] = true;
                            resStr[0] = ss;
                            ToastUtil.showToast(InspectWorkerJudgeActivity.this, ss, ToastUtil.Info);
                        }
                    }
                });
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resFlag[0]) {
                    new InspectVar().getInstance().workerRES1.add("0"); //不合格
                    new InspectVar().getInstance().workerRES2.add(resStr[0]);
                } else {
                    new InspectVar().getInstance().workerRES1.add("1"); //合格
                    new InspectVar().getInstance().workerRES2.add("");
                }
                if (numInt == 0) {
                    ActivityUtil.switchTo(InspectWorkerJudgeActivity.this, InspectWorkerActivity.class);
                    ActivityUtil.toastShow(InspectWorkerJudgeActivity.this, "完成");

                    String ss = "";
                    for (int i = 0; i < new InspectVar().getInstance().workerRES1.size(); i++) {
                        ss += new InspectVar().getInstance().workerRES1.get(i) + new InspectVar().getInstance().workerRES2.get(i) + ",";
                    }
                    ActivityUtil.toastShow(InspectWorkerJudgeActivity.this, ss);

                    ActivityManager.getAppManager().finishAllActivity();//结束所有activity
                    finish();
                } else {
                    SPUtil.put(InspectWorkerJudgeActivity.this, "inspectnum", String.valueOf(numInt - 1));
                    ActivityUtil.switchTo(InspectWorkerJudgeActivity.this, InspectWorkerJudgeActivity.class);
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
