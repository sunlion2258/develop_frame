package com.sun.dev.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.sun.dev.R
import com.sun.dev.util.SizeUtil
import org.jetbrains.anko.backgroundColor

/**
 * Created by SunLion on 2019/4/29 19:24
 * 自定义简单的ToolBar
 */
class AppToolBar : RelativeLayout {

    //ToolBar
    private lateinit var view: View
    //中间的标题
    private lateinit var title: TextView
    //返回键
    private lateinit var back: ImageView
    //客服
    private lateinit var kefu: ImageView
    //消息
    private lateinit var message: ImageView

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.include_toolbar, this)
        title = view.findViewById(R.id.toolbar_title)
        back = view.findViewById(R.id.toolbar_back)
        kefu = view.findViewById(R.id.toolbar_service)
        message = view.findViewById(R.id.toolbar_message)
        //获取自定义属性
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.AppToolBar)
        val titleText = typeArray.getString(R.styleable.AppToolBar_toolbar_title)
        //赋值
        title.text = titleText
        when (typeArray.getInt(R.styleable.AppToolBar_toolbar_style, 0)) {
            0 -> showHomePage()
            1 -> showBackAndTitle()
            2 -> showOnlyTitle()
            6 -> showBlackColor()
        }
        //默认设置back点击事件为finish
        back.setOnClickListener {
            context as Activity
            context.finish()
        }
        typeArray.recycle()
    }

    private fun showBlackColor() {
        title.visibility = View.VISIBLE
        back.visibility = View.VISIBLE
        message.visibility = View.GONE
        kefu.visibility = View.GONE
    }

    private fun showHomePage() {
        title.visibility = View.VISIBLE
        back.visibility = View.GONE
        kefu.visibility = View.VISIBLE
        message.visibility = View.VISIBLE
        backgroundColor = Color.TRANSPARENT
    }

    private fun showBackAndTitle() {
        title.visibility = View.VISIBLE
        back.visibility = View.VISIBLE
        kefu.visibility = View.GONE
        message.visibility = View.GONE
        backgroundColor = Color.WHITE
    }

    private fun showOnlyTitle() {
        title.visibility = View.VISIBLE
        back.visibility = View.GONE
        kefu.visibility = View.GONE
        message.visibility = View.GONE
        backgroundColor = Color.WHITE
    }

    fun setTitle(text: String) {
        title.text = text
    }

    fun setBackOnclickListener(listener: OnClickListener) {
        back.setOnClickListener(listener)
    }


    fun setKefuOnclickListener(listener: OnClickListener) {
        kefu.setOnClickListener(listener)
    }

    fun setMessageOnclickListener(listener: OnClickListener) {
        message.setOnClickListener(listener)
    }

    fun setMessageImage(id: Int) {
        message.setImageResource(id)
    }
}

