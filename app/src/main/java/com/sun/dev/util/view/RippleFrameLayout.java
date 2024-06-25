package com.sun.dev.util.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by SunLion on 2020/8/20.
 * 自定义动画,支付宝咻咻咻
 */

public class RippleFrameLayout extends FrameLayout {
    private int rippleColor = Color.parseColor("#FFC3C3");//水波纹的颜色
    private int radius = 0;//水波纹圆的半径
    private long anim_duration = 3000;//动画执行的时间
    private int water_ripple_count = 4;
    private int scale = 5;//动画缩放比例
    private long animDelay;//动画延迟的时间
    private Paint paint;
    private AnimatorSet animatorSet;
    private ArrayList<Animator> animatorList;
    private FrameLayout.LayoutParams rippleParams;
    private ArrayList<WaterRipple> rippleViewList= new ArrayList<>();
    private boolean isAnimRunning = false;


    public RippleFrameLayout(Context context) {
        this(context,null);
    }
    public RippleFrameLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public RippleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        addChildView();
        initAnim();
    }
    /**
     * 初始化动画
     */
    private void initAnim() {
        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorList=new ArrayList<Animator>();
        for(int i=0;i<rippleViewList.size();i++){//几个水波纹
            final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleViewList.get(i), "ScaleX", 1.0f, scale);
            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleXAnimator.setStartDelay(i * animDelay);
            scaleXAnimator.setDuration(anim_duration);
            animatorList.add(scaleXAnimator);
            final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleViewList.get(i), "ScaleY", 1.0f, scale);
            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleYAnimator.setStartDelay(i * animDelay);
            scaleYAnimator.setDuration(anim_duration);
            animatorList.add(scaleYAnimator);
            final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(rippleViewList.get(i), "Alpha", 1.0f, 0f);
            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setStartDelay(i * animDelay);
            alphaAnimator.setDuration(anim_duration);
            animatorList.add(alphaAnimator);
        }
        animatorSet.playTogether(animatorList);
    }
    /**
     * 添加水波纹子view
     */
    private void addChildView() {
        radius = 28;
        animDelay=350;
        rippleParams=new FrameLayout.LayoutParams((int)(2*(radius)),(int)(2*(radius)));
        rippleParams.gravity = Gravity.CENTER;
        for(int i=0;i<water_ripple_count;i++){//几个水波纹
            WaterRipple rippleView=new WaterRipple(getContext(),paint);
            addView(rippleView,rippleParams);
            rippleViewList.add(rippleView);
        }
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(rippleColor);
        paint.setStyle(Paint.Style.FILL);
    }
    /**
     * 开启动画
     */
    public void startRippleAnimation(){
        if(!isRunning()){
            for(WaterRipple waterRipple :rippleViewList){
                waterRipple.setVisibility(VISIBLE);
            }
            animatorSet.start();
            isAnimRunning=true;
        }
    }
    /**
     * 动画停止运行
     */
    public void stopAnimation(){
        if(isRunning()){
            animatorSet.cancel();
            isAnimRunning=false;
        }
    }
    /**
     * 判断是否动画在运行
     * @return
     */
    public boolean isRunning(){
        return isAnimRunning;
    }
    /**
     * ui不可见时关闭动画
     */
//    @Override
//    protected void onWindowVisibilityChanged(int visibility) {
//        super.onWindowVisibilityChanged(visibility);
//        if(visibility== View.INVISIBLE||visibility ==View.GONE){
//            stopAnimation();
//        }
//    }
}
