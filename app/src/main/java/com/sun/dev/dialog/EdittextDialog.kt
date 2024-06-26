package com.sun.dev.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.sun.dev.R
import com.sun.dev.activity.TitleWithContentActivity
import com.sun.dev.common.Constants
import com.sun.dev.util.BottomAnimationUtil
import com.sun.dev.util.startExtActivity
import kotlinx.android.synthetic.main.dialog_edittext.view.et_content
import kotlinx.android.synthetic.main.dialog_edittext.view.tv_confirm


/**
 * Created by SunLion on 2021年12月6日.
 *
 * 带有输入框的dialog，由fragment控制生命周期，宽高自定义
 */
@Suppress("DEPRECATION")
class EdittextDialog : DialogFragment() {
    fun newInstance(): EdittextDialog {
        val args = Bundle()
        val fragment = EdittextDialog()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.requestWindowFeature(FEATURE_NO_TITLE)
        val view = inflater.inflate(R.layout.dialog_edittext, container, false)

        //执行动画
        BottomAnimationUtil().slideToUp(view)

        /**
         * 退出操作，删除缓存
         */
        view.tv_confirm.setOnClickListener {
            dismiss()
            /*  context!!.startActivity<LoginActivity>()
              //删除Token
              SharedHelper.getEdit { sp -> sp.remove(Constants.SP.TOKEN)}
              //删除电话号码
              SharedHelper.getEdit { sp -> sp.remove(Constants.SP.PHONE_NUM) }
              //改变登陆状态
              SharedHelper.getEdit { sp -> sp.putBoolean(Constants.SP.IS_LOGIN, false) }*/

            var content = view.et_content.text.toString()
            if (TextUtils.isEmpty(content)) {
                content = "https://www.bilibili.com/video/BV1Zs421T7Na/?spm_id_from=333.1007.tianma.1-3-3.click"
            }
            val pairs = mutableListOf<Pair<String, Any>>()
            pairs.add(Constants.SP.TITLE_ACTIVITY_TYPE to TitleWithContentActivity.TYPE_JUMP_URL)
            pairs.add("url" to content)
            requireActivity().startExtActivity<TitleWithContentActivity>(values = pairs)
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
        val m = requireActivity().windowManager
        val d = m.defaultDisplay
        val p = dialog!!.window!!.attributes
        p.width = (d.width)
        dialog!!.window!!.attributes = p
    }
}