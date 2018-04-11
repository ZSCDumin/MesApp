package com.msw.mesapp.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.animation.Attention.Swing;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.msw.mesapp.R;

/**
 * Created by Mr.Meng on 2018/3/15.
 */

public class EditextDialog extends BaseDialog<EditextDialog> implements
        View.OnClickListener{
    private Context context;
    private EditText et;
    private TextView no;
    private TextView yes;
    private String ss="";

    public EditextDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(new SlideBottomEnter());

        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(context, R.layout.custom_edittext, null);
        et = (EditText) inflate.findViewById(R.id.et);
        no = (TextView) inflate.findViewById(R.id.no);
        yes = (TextView) inflate.findViewById(R.id.yes);

        yes.setOnClickListener(this);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.yes:
                // 点击了确认按钮
                dismiss();
                if (this.mClickListener != null) {
                    this.mClickListener.onOKClick(et);
                }
                break;
            default:
                break;
        }
    }

    //创建接口
    public static interface OnOKClickListener {
        public void onOKClick(EditText et);
    }
    //生命接口对象
    private OnOKClickListener mClickListener;
    //设置监听器 也就是实例化接口
    public void setOnClickListener(final OnOKClickListener clickListener) {
        this.mClickListener = clickListener;
    }
}
