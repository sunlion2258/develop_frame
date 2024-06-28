package com.sun.dev.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by SunLion on 2020/8/20.
 * 水波纹
 */

public class WaterRipple extends View {
    private Paint mPaint;

    public WaterRipple(Context context, Paint paint) {
        super(context);
        if (paint == null) {
            this.mPaint = new Paint();
        } else {
            this.mPaint = paint;
        }
        setVisibility(View.INVISIBLE);//刚开始设置不可见
    }

    public WaterRipple(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = (Math.min(getWidth(), getHeight())) / 2;
        canvas.drawCircle(radius, radius, radius, mPaint);
    }
}
