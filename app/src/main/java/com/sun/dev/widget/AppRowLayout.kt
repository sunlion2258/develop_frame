package com.sun.dev.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.sun.dev.R

/**
 *  Created by SunLion on 2019/4/29 15:23
 */
class AppRowLayout : RelativeLayout {

    private var view: View = LayoutInflater.from(context).inflate(R.layout.item_row, this)
    private var imageLeft: ImageView = view.findViewById(R.id.row_left_iv)
    var title: TextView = view.findViewById(R.id.row_title_tv)
    var content: TextView = view.findViewById(R.id.row_content_tv)
    var imageRight: ImageView = view.findViewById(R.id.row_right_iv)

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        //找到布局中的三个控件
        //获取属性
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.AppRowLayout)
        val imageLeftId = typeArray.getResourceId(R.styleable.AppRowLayout_image_left, R.mipmap.icon_app)
        val textTitle = typeArray.getString(R.styleable.AppRowLayout_row_title_text)
        val textContent = typeArray.getString(R.styleable.AppRowLayout_row_content_text)
        val imageRightId = typeArray.getResourceId(R.styleable.AppRowLayout_image_right, R.mipmap.icon_right)

        //风格属性
        val rowStyle = typeArray.getInteger(R.styleable.AppRowLayout_row_style, 0)
        //设置风格
        if (rowStyle == 0) {
            //右边为箭头+文字风格
            setArrowWithTextStyle()
        } else {
            //右边为圆角文字风格
            setRightTextStyle()
        }

        //属性赋值
        imageLeft.setImageResource(imageLeftId)
        title.text = textTitle
        content.text = textContent
        imageRight.setImageResource(imageRightId)

        typeArray.recycle()
    }


    private fun setRightTextStyle() {
        imageRight.visibility = View.GONE
        content.visibility = View.GONE
    }

    private fun setArrowWithTextStyle() {
        imageRight.visibility = View.VISIBLE
        content.visibility = View.VISIBLE
    }
}