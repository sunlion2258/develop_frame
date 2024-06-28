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
    private lateinit var ivBack: ImageView
    private lateinit var ivLeft: ImageView
    private lateinit var ivRight: ImageView
    private lateinit var ivToRightLeft: ImageView
    private lateinit var tvChatModel: TextView

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.include_toolbar, this)
        title = view.findViewById(R.id.toolbar_title)
        ivBack = view.findViewById(R.id.toolbar_back)
        ivLeft = view.findViewById(R.id.toolbar_left)
        ivRight = view.findViewById(R.id.toolbar_right)
        ivToRightLeft = view.findViewById(R.id.iv_to_right_left)
        tvChatModel = view.findViewById(R.id.tv_chat_model)

        ivToRightLeft.isSelected = true

        //获取自定义属性
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.AppToolBar)
        val titleText = typeArray.getString(R.styleable.AppToolBar_toolbar_title)
        //赋值
        title.text = titleText
        when (typeArray.getInt(R.styleable.AppToolBar_toolbar_style, 0)) {
            0 -> showBackTitleRight()
            1 -> showBackAndTitle()
            2 -> showOnlyTitle()
            3 -> showTitleAndRight()
        }
        //默认设置back点击事件为finish
        ivBack.setOnClickListener {
            context as Activity
            context.finish()
        }
        typeArray.recycle()
    }

    private fun showTitleAndRight() {
        title.visibility = View.VISIBLE
        ivBack.visibility = View.GONE
        ivRight.visibility = View.VISIBLE
        ivLeft.visibility = View.GONE
        backgroundColor = Color.TRANSPARENT
    }

    private fun showBackTitleRight() {
        title.visibility = View.VISIBLE
        ivBack.visibility = View.VISIBLE
        ivLeft.visibility = View.GONE
        ivRight.visibility = View.VISIBLE
        backgroundColor = Color.TRANSPARENT
    }

    private fun showBackAndTitle() {
        title.visibility = View.VISIBLE
        ivBack.visibility = View.VISIBLE
        ivLeft.visibility = View.GONE
        ivRight.visibility = View.GONE
        backgroundColor = Color.WHITE
    }

    private fun showOnlyTitle() {
        title.visibility = View.VISIBLE
        ivBack.visibility = View.GONE
        ivLeft.visibility = View.GONE
        ivRight.visibility = View.GONE
        backgroundColor = Color.WHITE
        backgroundColor = Color.TRANSPARENT
    }

    fun setTitle(text: String) {
        title.text = text
    }

    fun setBackOnclickListener(listener: OnClickListener) {
        ivBack.setOnClickListener(listener)
    }


    fun setLeftOnclickListener(listener: OnClickListener) {
        ivLeft.setOnClickListener(listener)
    }

    fun setRightOnclickListener(listener: OnClickListener) {
        ivRight.setOnClickListener(listener)
    }

    fun setLeftImage(id: Int) {
        ivLeft.setImageResource(id)
    }

    fun setRightImage(id: Int) {
        ivRight.setImageResource(id)
    }

    fun setToRightLeftImageState(b: Boolean) {
        ivToRightLeft.isSelected = b
    }

    fun getToRightLeftImageState(): Boolean {
        return ivToRightLeft.isSelected
    }


    fun setToRightLeftVisibility(int: Int) {
        ivToRightLeft.visibility = int
    }

    fun setToRightOnclickListener(listener: OnClickListener) {
        ivToRightLeft.setOnClickListener(listener)
    }

    fun setChatModelVisibility() {
        tvChatModel.visibility = View.VISIBLE
    }

    fun setChatModelInvisibility() {
        tvChatModel.visibility = View.INVISIBLE
    }

    fun setChatModelOnclickListener(listener: OnClickListener) {
        tvChatModel.setOnClickListener(listener)
    }

    fun getChatModelContent(): String {
        return tvChatModel.text.toString().trim()
    }

    fun setChatModelContent(string: String) {
        tvChatModel.text = string
    }

}

