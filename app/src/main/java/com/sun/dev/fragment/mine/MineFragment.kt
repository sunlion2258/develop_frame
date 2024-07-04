@file:Suppress("DEPRECATION")

package com.sun.dev.fragment.mine

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.activity.ChangeLanguageActivity
import com.sun.dev.activity.GLBActivity
import com.sun.dev.activity.GyroActivity
import com.sun.dev.activity.MainActivity
import com.sun.dev.activity.SettingActivity
import com.sun.dev.activity.TestActivity
import com.sun.dev.activity.UnityTestActivity
import com.sun.dev.base.BaseMVVMFragment
import com.sun.dev.common.Constants
import com.sun.dev.databinding.FragmentMainMineBinding
import com.sun.dev.util.ClickUtils
import com.sun.dev.util.SharedHelper
import org.jetbrains.anko.support.v4.startActivity


/**
 *  Created by SunLion on 2019/4/29 17:54
 */
@Suppress("DEPRECATION")
class MineFragment : BaseMVVMFragment<FragmentMainMineBinding, MineViewModel>() {
    private var isDarkTheme = false

    override fun initContentViewID(): Int = R.layout.fragment_main_mine

    override fun initViewModel(): MineViewModel =
        ViewModelProviders.of(
            activity!!,
            MineVMFactory(MineRepository(), activity = MainActivity())
        ).get(MineViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        isDarkTheme = SharedHelper.getShared().getBoolean(Constants.SP.THEME_PREFS, false)
        if (isDarkTheme) {
            requireActivity().setTheme(R.style.AppTheme_Dark)
        } else {
            requireActivity().setTheme(R.style.AppTheme)
        }

        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

        bindView.vm = viewModel

        bindView.mineMediaRecord.setOnClickListener {
            AudioRecordActivity.start(activity!!, "我是录制")
        }

        bindView.mineFbx.setOnClickListener {
            if (!ClickUtils.isNotFastClick()) {
                return@setOnClickListener
            }
            startActivity<TestActivity>()
        }
        bindView.mineGyroscope.setOnClickListener {
            if (!ClickUtils.isNotFastClick()) {
                return@setOnClickListener
            }
            startActivity<GyroActivity>()
        }
        bindView.mineGlbModel.setOnClickListener {
            if (!ClickUtils.isNotFastClick()) {
                return@setOnClickListener
            }
            startActivity<GLBActivity>()
        }
        bindView.mineToUnity.setOnClickListener {
            if (!ClickUtils.isNotFastClick()) {
                return@setOnClickListener
            }
            startActivity<UnityTestActivity>()
        }
        bindView.logoutChangeLanguage.setOnClickListener {
            if (!ClickUtils.isNotFastClick()) {
                return@setOnClickListener
            }
            startActivity<ChangeLanguageActivity>()
        }
        bindView.mineChangeTheme.setOnClickListener {
            // 切换主题标志
            isDarkTheme = !isDarkTheme;

            SharedHelper.getEdit { sp -> sp.putBoolean(Constants.SP.THEME_PREFS, isDarkTheme) }
            // 重新启动活动以应用新主题
            recreate(requireActivity())
        }
        bindView.mineSetting.setOnClickListener {
            if (!ClickUtils.isNotFastClick()) {
                return@setOnClickListener
            }
            startActivity<SettingActivity>()
        }
    }

//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        if (!hidden) {
//            if (!CodeUtil.checkIsLogin()) {
//                startActivity<LoginActivity>()
//            }
//        }
//    }
}