package com.msw.mesapp.activity.fragment;

/**
 * Created by Mr.Meng on 2017/12/31.
 */

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.flyco.animation.Attention.Swing;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.popup.BubblePopup;
import com.makeramen.roundedimageview.RoundedImageView;
import com.msw.mesapp.R;
import com.msw.mesapp.activity.LoginActivity;
import com.msw.mesapp.activity.me.ModifyPasswordActivity;
import com.msw.mesapp.base.GlobalKey;
import com.msw.mesapp.utils.ACacheUtil;
import com.msw.mesapp.utils.ActivityUtil;
import com.msw.mesapp.utils.DialogUtil;
import com.msw.mesapp.utils.SPUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.display.FadeInImageDisplayer;
import me.panpf.sketch.request.ShapeSize;
import me.panpf.sketch.shaper.CircleImageShaper;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.msw.mesapp.R.id.imageView;

public class FragmentMe extends Fragment {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.tvname)
    TextView tvname;
    @Bind(R.id.tvid)
    TextView tvid;
    @Bind(R.id.tvchangepw)
    TextView tvchangepw;
    @Bind(R.id.tvlogout)
    TextView tvlogout;
    @Bind(R.id.head)
    SketchImageView head;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.viewpaper_me, container, false);
        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title.setText("个人中心");
    }

    private void initView() {
        final String jpgUrl = "http://img2.touxiang.cn/file/20171124/5c8f1c9dd6479d6d434226d1151b81b1.jpg";
        head.getOptions()
                .setCacheInDiskDisabled(true)
                .setShaper(new CircleImageShaper())
                .setDecodeGifImage(true) //显示gif
                .setLoadingImage(R.mipmap.ic_autorenew) //加载时的图片
                .setErrorImage(R.mipmap.defualt_head)  //错误时的图片
                .setShapeSize(ShapeSize.byViewFixedSize()) //设置尺寸
                .setDisplayer(new FadeInImageDisplayer()); //显示图片的动画
        head.displayImage(jpgUrl);
        head.setClickRetryOnDisplayErrorEnabled(true);//加载失败时点击重新加载
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View inflate = View.inflate(getContext(), R.layout.popup_bubble_image, null);
                ImageView iv = ButterKnife.findById(inflate, R.id.iv_bubble);
                TextView tv = ButterKnife.findById(inflate, R.id.tv_bubble);
                //Glide.with(getContext()).load(jpgUrl).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.defualt_head).into(iv);
                tv.setText("好好工作，天天向上~");
                new BubblePopup(getContext(), inflate).anchorView(head)
                        .bubbleColor(Color.parseColor("#55C34A"))
                        .showAnim(new SlideBottomEnter())
                        .dismissAnim(new SlideBottomExit())
                        .show();
            }
        });
/*        Glide.with(getContext())
                .load(jpgUrl)
                .asBitmap().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE) //什么都不缓存
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.head2)
                .into(new BitmapImageViewTarget(head) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        head.setImageDrawable(circularBitmapDrawable);
                    }
                });*/
        String code = "";
        code = (String) SPUtil.get(getActivity(), GlobalKey.Login.CODE, code);
        tvid.setText(code);

        tvchangepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.switchTo(getActivity(), ModifyPasswordActivity.class);
            }
        });
        tvlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DialogUtil.createConfirmDialog(getActivity(), "是否注销用户？", "", "确定", "取消",
//                        new OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                getActivity().finish();
//                                SPUtil.put(getActivity(), GlobalKey.Login.CODE, "");
//                                ActivityUtil.switchTo(getActivity(), LoginActivity.class);
//                            }
//                        }, new OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        }, DialogUtil.NO_ICON
//                ).show();
                BaseAnimatorSet bas_in = new Swing();
                BaseAnimatorSet bas_out = new FadeExit();
                final NormalDialog dialog = new NormalDialog(getContext());
                dialog.content("是否确定注销用户?")//
                        .showAnim(bas_in)//
                        .dismissAnim(bas_out)//
                        .show();
                dialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        SPUtil.put(getActivity(), GlobalKey.Login.CODE, "");
                        ActivityUtil.switchTo(getActivity(), LoginActivity.class);
                        getActivity().finish();
                        dialog.dismiss();
                    }
                }
                );

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}