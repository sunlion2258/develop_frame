package com.sun.dev.activity

import android.graphics.Color
import android.view.View
import android.widget.PopupMenu
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseActivity
import com.sun.dev.common.Constants
import com.sun.dev.fragment.*
import com.sun.dev.util.toast
import kotlinx.android.synthetic.main.activity_title_with_content.*
import kotlinx.android.synthetic.main.fragment_draw.*
import kotlinx.android.synthetic.main.include_toolbar.view.*


/**
 * 制式Activity
 */
class TitleWithContentActivity : BaseActivity() {

    override fun initContentViewID(): Int = R.layout.activity_title_with_content

    override fun onViewCreated() {
        ImmersionBar.setTitleBar(this, info_toolbar)
        ImmersionBar
            .with(this)
            .statusBarDarkFont(true)
            .init()

        //设置ToolBar返回键监听
        info_toolbar.setBackOnclickListener(View.OnClickListener {
            finish()
        })

        val type = intent.getIntExtra(Constants.SP.TITLE_ACTIVITY_TYPE, 0)
        val transaction = supportFragmentManager.beginTransaction()
        when (type) {
            TYPE_MY_INFO -> {
                info_toolbar.setTitle(resources.getString(R.string.my_info))
                transaction.replace(R.id.info_content, MyInfoFragment()).commit()
            }
            TYPE_DRAWING -> {
                info_toolbar.setRightImage(R.mipmap.icon_menu)
                info_toolbar.toolbar_right.visibility = View.VISIBLE
                info_toolbar.setTitle(resources.getString(R.string.drawing))
                transaction.replace(R.id.info_content, DrawFragment()).commit()

                info_toolbar.setRightOnclickListener {
                    val popupMenu = PopupMenu(this, info_toolbar.toolbar_right)
                    val inflater = popupMenu.menuInflater
                    inflater.inflate(R.menu.toolsmenu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.red -> {
                                drawView.paint.color = Color.RED
                                it.isChecked = true
                                drawView.setPaintModel()
                            }
                            R.id.green -> {
                                drawView.paint.color = Color.GREEN
                                it.isChecked = true
                                drawView.setPaintModel()
                            }
                            R.id.blue -> {
                                drawView.paint.color = Color.BLUE
                                it.isChecked = true
                                drawView.setPaintModel()
                            }
                            R.id.cyan -> {
                                drawView.paint.color = Color.CYAN
                                it.isChecked = true
                                drawView.setPaintModel()
                            }
                            R.id.magenta -> {
                                drawView.paint.color = Color.MAGENTA
                                it.isChecked = true
                                drawView.setPaintModel()
                            }


                            R.id.width_1 -> {
                                drawView.setPaintModel()
                                drawView.paint.strokeWidth = 1F
                            }

                            R.id.width_2 -> {
                                drawView.setPaintModel()
                                drawView.paint.strokeWidth = 2F
                            }

                            R.id.width_3 -> {
                                drawView.setPaintModel()
                                drawView.paint.strokeWidth = 3F
                            }


                            R.id.clear -> drawView.clear()
                            R.id.reset -> drawView.resetCanvas()
                            R.id.save -> drawView.save()
                        }
                        return@setOnMenuItemClickListener true
                    }
                    popupMenu.show()
                }
            }

            TYPE_JUMP_URL -> {
                var url = intent.getStringExtra("url") ?: "https://www"
                if (!url.contains("http")) {
                    url = "https://$url"
                }
                toast(url)
                val fragment = JumpUrlFragment.newInstance(url)
                info_toolbar.setTitle(resources.getString(R.string.jump_url))
                transaction.replace(R.id.info_content, fragment).commit()
            }
        }
    }

    /**
     * 内容
     */
    companion object {
        //我的资料
        const val TYPE_MY_INFO = 0

        //位置
        const val TYPE_LOCATION = 1

        //画图
        const val TYPE_DRAWING = 2

        //联系方式
        const val TYPE_CONTACT = 3

        //跳转url
        const val TYPE_JUMP_URL = 4
    }
}
