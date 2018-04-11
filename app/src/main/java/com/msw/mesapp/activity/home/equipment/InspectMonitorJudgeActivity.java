package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.Attention.Swing;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.msw.mesapp.R;
import com.msw.mesapp.ui.widget.CollapsibleTextView;
import com.msw.mesapp.ui.widget.EditextDialog;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

public class InspectMonitorJudgeActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.line)
    LinearLayout line;
    @Bind(R.id.linediss)
    LinearLayout linediss;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.bt1)
    FButton bt1;
    @Bind(R.id.bt2)
    FButton bt2;

    private RecyclerView.Adapter adapter;
    List<String> list = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_monitor_judge);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < content.length; i++)
            list.add("内容" + i + "：" + content[i]);
    }

    private void initView() {
        initTitle();
        setTextMarquee(tv3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<String>(this, R.layout.item_inspect_monitor, list) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                ((CollapsibleTextView) (holder.getView(R.id.tv1))).setDesc(s, TextView.BufferType.NORMAL);
            }
        };
        recyclerView.setAdapter(adapter);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityUtil.switchTo(InspectMonitorJudgeActivity.this, InspectMonitorJudgeErrActivity.class);
                //ActivityManager.getAppManager().addActivity(InspectMonitorJudgeActivity.this);
                BaseAnimatorSet bas_in = new Swing();
                BaseAnimatorSet bas_out = new FadeExit();
                final EditextDialog editextDialog = new EditextDialog(InspectMonitorJudgeActivity.this);
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
                            linediss.setVisibility(View.VISIBLE);
                            tv3.setText(ss);
                            ToastUtil.showToast(InspectMonitorJudgeActivity.this, ss, ToastUtil.Info);
                        }
                    }
                });
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.toastShow(InspectMonitorJudgeActivity.this, "完成");
                finish();
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
        title.setText("点检核查");
        add.setVisibility(View.INVISIBLE);
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
}
