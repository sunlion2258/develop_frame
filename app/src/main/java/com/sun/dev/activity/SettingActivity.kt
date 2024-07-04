package com.sun.dev.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivitySettingBinding
import com.sun.dev.viewmodel.SettingModel
import com.sun.dev.viewrepository.SettingRepository
import com.sun.dev.vmfactory.SettingFactory
import kotlinx.android.synthetic.main.activity_test.toolbar

/**
 * Created by fengwj on 2024/7/4.
 */
class SettingActivity : BaseMVVMActivity<ActivitySettingBinding, SettingModel>() {
    override fun initViewModel(): SettingModel =
        ViewModelProviders.of(this, SettingFactory(SettingRepository()))
            .get(SettingModel::class.java)


    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar)
        ImmersionBar.with(this)
            .statusBarDarkFont(false)
            .init()
    }

    override fun initContentViewID(): Int= R.layout.activity_setting
}