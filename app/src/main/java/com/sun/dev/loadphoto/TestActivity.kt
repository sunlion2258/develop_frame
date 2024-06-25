@file:Suppress("DEPRECATION")

package com.sun.dev.loadphoto

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityTestBinding
import kotlinx.android.synthetic.main.activity_test.toolbar


/**
 * Created by SunLion on 2019/12/5.
 */
@Suppress("DEPRECATION")
class TestActivity : BaseMVVMActivity<ActivityTestBinding, TestModel>(),
    View.OnClickListener {

    override fun initContentViewID(): Int = R.layout.activity_test

    override fun onClick(v: View?) {
    }


    override fun initViewModel(): TestModel =
        ViewModelProviders.of(this, TestFactory(TestRepository()))
            .get(TestModel::class.java)

    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar)
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

    }
}