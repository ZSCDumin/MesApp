package com.msw.mesapp.activity.home.equipment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.Attention.Swing;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.msw.mesapp.R;
import com.msw.mesapp.base.GlobalApi;
import com.msw.mesapp.ui.widget.CollapsibleTextView;
import com.msw.mesapp.ui.widget.EditextDialog;
import com.msw.mesapp.utils.DateUtil;
import com.msw.mesapp.utils.GetCurrentUserIDUtil;
import com.msw.mesapp.utils.StatusBarUtils;
import com.msw.mesapp.utils.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    @Bind(R.id.t3)
    TextView t3;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.bt2)
    Button bt2;
    @Bind(R.id.t)
    TextView t;
    @Bind(R.id.linediss)
    LinearLayout linediss;
    private RecyclerView.Adapter adapter;
    List<Map<String, Object>> list = new ArrayList<>();

    String code = "";
    String CheckHeadCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_monitor_judge);
        ButterKnife.bind(this);
        initData();
        //initView();
    }

    private void initData() {
        final Map[] map = {new HashMap()};
        map[0].put("1", "巡检主键：" + getIntent().getExtras().get("01").toString());
        map[0].put("2", "");
        list.add(map[0]);
        code = getIntent().getExtras().get("01").toString();

        map[0] = new HashMap();
        map[0].put("1", "表头编码：" + getIntent().getExtras().get("02").toString());
        map[0].put("2", "");
        list.add(map[0]);
        CheckHeadCode = getIntent().getExtras().get("02").toString();

        map[0] = new HashMap();
        map[0].put("1", getIntent().getExtras().get("03").toString());
        map[0].put("2", "");
        list.add(map[0]);

        map[0] = new HashMap();
        map[0].put("1", getIntent().getExtras().get("04").toString());
        map[0].put("2", "");
        list.add(map[0]);

        EasyHttp.post(GlobalApi.Inspect.Worker.CheckHead.PATH)
                .params(GlobalApi.Inspect.Worker.CheckHead.code, CheckHeadCode)
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

                            int num = Integer.valueOf(data.optString("num"));

                            for (int i = 1; i <= num; i++) {

                                JSONObject check = data.getJSONObject("check" + String.valueOf(i));

                                String content = check.getString("content");

                                Map mmap = new HashMap();
                                mmap.put("1", "内容" + i + ":" + content);

                                if (getIntent().getExtras().get(String.valueOf(i) + "1").toString().equals(("0")))
                                    mmap.put("2", "合格");
                                else
                                    mmap.put("2", "不合格");
                                list.add(mmap);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (code == 0) {
                            initView();
                        } else {
                            ToastUtil.showToast(InspectMonitorJudgeActivity.this, message, ToastUtil.Error);
                        }
                    }
                    @Override
                    public void onError(ApiException e) {
                        ToastUtil.showToast(InspectMonitorJudgeActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                    }
                });
    }

    private void initView() {
        initTitle();
        setTextMarquee(tv3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置为listview的布局
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));//添加分割线
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_inspect_monitor, list) {
            @Override
            protected void convert(ViewHolder holder, Map s, final int position) {
                ((CollapsibleTextView) (holder.getView(R.id.tv1))).setDesc(s.get("1").toString(), TextView.BufferType.NORMAL);
                holder.setText(R.id.tv2, s.get("2").toString());
            }
        };
        recyclerView.setAdapter(adapter);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EasyHttp.post(GlobalApi.Inspect.Monitor.UpdateExamPerson.PATH)
                        .params(GlobalApi.Inspect.Monitor.UpdateExamPerson.code, code) //获取没有审核的数据
                        .params(GlobalApi.Inspect.Monitor.UpdateExamPerson.examPerson, GetCurrentUserIDUtil.currentUserId(InspectMonitorJudgeActivity.this)) //
                        .params(GlobalApi.Inspect.Monitor.UpdateExamPerson.examState, "2") //
                        .params(GlobalApi.Inspect.Monitor.UpdateExamPerson.examDate, DateUtil.getCurrentDate2()) //
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (code == 0) {
                                    ToastUtil.showToast(InspectMonitorJudgeActivity.this, message, ToastUtil.Success);
                                    finish();
                                } else {
                                    ToastUtil.showToast(InspectMonitorJudgeActivity.this, message, ToastUtil.Error);
                                }
                            }

                            @Override
                            public void onError(ApiException e) {
                                ToastUtil.showToast(InspectMonitorJudgeActivity.this, GlobalApi.ProgressDialog.INTERR, ToastUtil.Confusion);
                            }
                        });
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
        title.setText("点检审核");
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
