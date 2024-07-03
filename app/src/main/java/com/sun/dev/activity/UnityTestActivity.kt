@file:Suppress("DEPRECATION")

package com.sun.dev.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityUnityTestBinding
import com.sun.dev.viewmodel.UnityModel
import com.sun.dev.viewrepository.UnityRepository
import com.sun.dev.vmfactory.UnityFactory
import com.unity3d.player.GameCallHelper
import com.unity3d.player.UnityPlayerActivity
import kotlinx.android.synthetic.main.activity_unity_test.toolbar
import kotlinx.android.synthetic.main.activity_unity_test.tv_to_unity


/**
 * Created by SunLion on 2024/6/20.
 */
class UnityTestActivity : BaseMVVMActivity<ActivityUnityTestBinding, UnityModel>() {

    override fun initContentViewID(): Int = R.layout.activity_unity_test

    override fun initViewModel(): UnityModel =
        ViewModelProviders.of(this, UnityFactory(UnityRepository()))
            .get(UnityModel::class.java)

    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar)
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

        tv_to_unity.setOnClickListener {
            /**
             * game107:		僵尸复仇者
             * game1010:	环球旅行
             */

            GameCallHelper.setGameName("game1010")

            val intent = Intent(this@UnityTestActivity, UnityPlayerActivity::class.java)
            startActivity(intent)
        }
    }
}