@file:Suppress("DEPRECATION")

package com.sun.dev.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityUnityTestBinding
import com.sun.dev.dialog.LoadProgressDialog
import com.sun.dev.viewmodel.UnityModel
import com.sun.dev.viewrepository.UnityRepository
import com.sun.dev.vmfactory.UnityFactory
import com.unity3d.player.UnityPlayerActivity
import kotlinx.android.synthetic.main.activity_unity_test.toolbar
import kotlinx.android.synthetic.main.activity_unity_test.tv_to_unity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            val intent = Intent(this@UnityTestActivity, UnityPlayerActivity::class.java)
            val bundle = Bundle()
            bundle.putString("gameName", "game1010")
            bundle.putInt("language", 1)
            intent.putExtras(bundle)
            startActivityForResult(intent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val loadProgressDialog = LoadProgressDialog(this@UnityTestActivity, "自动进入第二个游戏")
            loadProgressDialog.show()

            GlobalScope.launch(Dispatchers.Main) {
                delay(2000)

                loadProgressDialog.dismiss()
                val intent = Intent(this@UnityTestActivity, UnityPlayerActivity::class.java)
                val bundle = Bundle()
                bundle.putString("gameName", "game107")
                bundle.putInt("language", 1)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }
}