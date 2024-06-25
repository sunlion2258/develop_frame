package com.sun.dev.util.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

import com.sun.dev.R;


/**
 * Created by SunLion on 2020/8/20.
 * 圆形图片
 */

public class CircleView extends View {
    private BitmapShader bitmapShaderp ;
    private ShapeDrawable shapeDrawable;
    public CircleView(Context context) {
        this(context,null);
    }
    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmap();
    }
    private void initBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_avatar_default);
        shapeDrawable = new ShapeDrawable(new OvalShape());
        bitmapShaderp = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shapeDrawable.getPaint().setShader(bitmapShaderp);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        shapeDrawable.setBounds(0,0,getWidth(),getHeight());
        shapeDrawable.draw(canvas);
    }
}
