@file:Suppress("DEPRECATION")

package com.sun.dev.fragment.mine

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.activity.MainActivity
import com.sun.dev.base.BaseMVVMFragment
import com.sun.dev.databinding.FragmentMainMineBinding
import com.sun.dev.loadphoto.GLBActivity
import com.sun.dev.loadphoto.GyroActivity
import com.sun.dev.loadphoto.TestActivity
import com.sun.dev.loadphoto.UnityTestActivity
import com.sun.dev.login.LoginActivity
import com.sun.dev.util.CodeUtil
import org.jetbrains.anko.support.v4.startActivity

/**
 *  Created by SunLion on 2019/4/29 17:54
 */
@Suppress("DEPRECATION")
class MineFragment : BaseMVVMFragment<FragmentMainMineBinding, MineViewModel>() {
    override fun initContentViewID(): Int = R.layout.fragment_main_mine

    override fun initViewModel(): MineViewModel =
        ViewModelProviders.of(
            activity!!,
            MineVMFactory(MineRepository(), activity = MainActivity())
        ).get(MineViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

        bindView.vm = viewModel

        bindView.mineMediaRecord.setOnClickListener {
            AudioRecordActivity.start(activity!!, "我是录制")
        }

        bindView.mineFbx.setOnClickListener {
            startActivity<TestActivity>()
        }
        bindView.mineGyroscope.setOnClickListener {
            startActivity<GyroActivity>()
        }
        bindView.mineGlbModel.setOnClickListener {
            startActivity<GLBActivity>()
        }
        bindView.mineToUnity.setOnClickListener {
            startActivity<UnityTestActivity>()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (!CodeUtil.checkIsLogin()) {
                startActivity<LoginActivity>()
            }
        }
    }
}