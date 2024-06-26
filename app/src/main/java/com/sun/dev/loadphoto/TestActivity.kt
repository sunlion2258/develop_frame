@file:Suppress("DEPRECATION")

package com.sun.dev.loadphoto

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.ar.core.ArCoreApk
import com.google.ar.sceneform.ux.ArFragment
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityTestBinding
import kotlinx.android.synthetic.main.activity_test.toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast


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