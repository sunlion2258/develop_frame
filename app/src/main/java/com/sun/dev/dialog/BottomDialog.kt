package com.sun.dev.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.Window.FEATURE_NO_TITLE
import androidx.fragment.app.DialogFragment
import com.sun.dev.R
import com.sun.dev.common.Constants
import com.sun.dev.login.LoginActivity
import com.sun.dev.util.BottomAnimationUtil
import com.sun.dev.util.SharedHelper
import kotlinx.android.synthetic.main.dialog_bottom.view.*
import org.jetbrains.anko.startActivity


/**
 * Created by SunLion on 2019/11/11.
 *
 * 封装底部弹窗，由fragment控制生命周期，宽高自定义
 */
@Suppress("DEPRECATION")
class BottomDialog : DialogFragment() {
    fun newInstance(): BottomDialog {
        val args = Bundle()
        val fragment = BottomDialog()
        fragment.arguments = args
        return fragment
    }

    override fun onStart() {
        super.onStart()

        val window = dialog!!.window
        val params = window!!.attributes
        params.gravity = Gravity.BOTTOM
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.requestWindowFeature(FEATURE_NO_TITLE)
        val view = inflater.inflate(R.layout.dialog_bottom, container, false)

        //执行动画
        BottomAnimationUtil().slideToUp(view)

        /**
         * 退出操作，删除缓存
         */
        view.tv_quit.setOnClickListener {
            dismiss()
            context!!.startActivity<LoginActivity>()
            //删除Token
            SharedHelper.getEdit { sp -> sp.remove(Constants.SP.TOKEN)}
            //删除电话号码
            SharedHelper.getEdit { sp -> sp.remove(Constants.SP.PHONE_NUM) }
            //改变登陆状态
            SharedHelper.getEdit { sp -> sp.putBoolean(Constants.SP.IS_LOGIN, false) }
        }

        return view
    }

    /**
     *在onResume中来控制宽高
     */
    override fun onResume() {
        super.onResume()

        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params
        dialog!!.window!!.setBackgroundDrawable(null)
        val m = activity!!.windowManager
        val d = m.defaultDisplay
        val p = dialog!!.window!!.attributes
        p.width = (d.width)
        dialog!!.window!!.attributes = p
    }
}