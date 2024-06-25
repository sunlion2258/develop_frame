package com.sun.dev.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * DrawView
 *
 * @author SunLion
 * @blame Android Team
 * @since 2019/11/12
 */
public class DrawView extends View {
    /*
     * 首先定义程序中所需的属性，然后添加构造方法
     * 重写onDraw（Canvas canvas）方法
     */
    private int viewWidth = 0;          //屏幕的宽度
    private int viewHeight = 0;         //屏幕的高度
    private float preX;                  //起始点的x坐标
    private float preY;                  //起始点的y坐标
    private android.graphics.Path path;                   //路径
    public Paint paint = null;          //画笔
    Bitmap cacheBitmap = null;          //定义一个内存中的图片，该图片作为缓冲区
    Canvas cacheCanvas = null;          //定义cacheBitmap上的Canvas对象

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    /**
     * 设置背景颜色，绘制cacheBitmap，绘制路径，保存当前绘图状态到栈中，调用resrore（）方法恢复所保存的状态
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xFFFFFFFF);       //设置背景颜色
        @SuppressLint("DrawAllocation")
        Paint bmpPaint = new Paint();       //采用默认设置创建一个画笔
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);        //绘制cacheBitmap
        canvas.drawPath(path, paint);        //绘制路径
        canvas.save();                      //保存canvas的状态
        canvas.restore();                   //回复canvas之前的保存的状态，放置保存后对canvas执行的操作对后续的绘制有影响

    }

    /**
     * 首先获取屏幕的宽度和高度，创建一个与该View相同大小的缓存区，
     * 创建一个新的画面，实例化一个路径，将内存中的位图会知道cacheCanvas中，最后实例化一个画笔，设置画笔的相关属性
     */
    public void init() {
        viewWidth = getContext().getResources().getDisplayMetrics().widthPixels;       //获取屏幕的宽度
        viewHeight = getContext().getResources().getDisplayMetrics().heightPixels;     //获取屏幕的高度
        cacheBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);      //创建一个与该View相同大小的缓存区
        cacheCanvas = new Canvas();         //创建一个新的画布
        path = new Path();
        cacheCanvas.setBitmap(cacheBitmap);     //在cacheCanvas上绘制cacheBitmap
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);              //设置填充方式为表便
        paint.setStrokeJoin(Paint.Join.ROUND);           //设置笔刷的图形样式
        paint.setStrokeCap(Paint.Cap.ROUND);             //设置画笔转弯处的连接风格
        paint.setStrokeWidth(2);                         //设置默认比触的宽度为2像素
        paint.setAntiAlias(true);                        //使用抗锯齿功能
        paint.setDither(true);                           //使用抖动效果

    }

    /**
     * 调用saveBitmap（）方法将当前绘图保存为PNG图片
     */
    public void save() {
        try {
            saveBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存绘制好的位图方法saveBitmap（）
     * 首先在SD卡上创建一个文件，然后创建一个文件输出流对象，调用Bitmap类的compress（）方法将绘图内容压缩为PNG歌是输出到创建的文件间输出流对象中
     * 最后将缓冲区的数据全部写出到输出流中，关闭文件输出流对象
     */
    @SuppressLint("WrongThread")
    private void saveBitmap() throws IOException {
        String storage = Environment.getExternalStorageDirectory().getPath();
        Toast.makeText(getContext(), storage, Toast.LENGTH_SHORT).show();     //显示得到的储存路径
        File dirFile = new File(storage + "/abcd");         //在storage/emulated/0目录下创建abcd文件夹
        dirFile.mkdir();                                        //创建一个新文件
        File file = new File(dirFile, System.currentTimeMillis() + ".jpg");
        FileOutputStream fileOS = new FileOutputStream(file);                           //创建一个文件输出流对象
        cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);             //将绘图内容压缩为PNG格式输出到输出流对象中，PNG为透明图片
        fileOS.flush();                 //将缓冲区中的数据全部写出道输出流中
        fileOS.close();                 //关闭文件输出流对象
    }

    /**
     * clear（）方法，实现橡皮擦功能
     */
    public void clear() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));       //设置图形重叠时的处理方式
        paint.setStrokeWidth(20);       //设置笔触的宽度
    }

    /**
     * 画布清除
     */
    public void resetCanvas() {
        cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    /**
     * 设置画笔模式
     */
    public void setPaintModel(){
        paint.setStrokeWidth(2);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }


    /**
     * 重写onTouchEvent（）方法，为该视图添加触摸事件监听器，
     * 首先获取触摸事件发生的位置，然后应用switch语句对事件的不同状态添加相应代码，最后调用inalidat（）方法更新视图
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);       //将绘图的起始点一道（x，y）坐标的位置
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                //判断是否在允许的范围内
                if (dx >= 5 || dy >= 5) {
                    path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    preX = x;
                    preY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                //绘制路径
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
            default:
                break;
        }
        invalidate();
        //返回true表明处理方法已经处理该事务
        return true;
    }
}
