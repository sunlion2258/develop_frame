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
import com.sun.dev.common.MyApplication
import com.sun.dev.databinding.ActivityTestBinding
import com.sun.dev.entity.User
import kotlinx.android.synthetic.main.activity_test.toolbar
import kotlinx.android.synthetic.main.activity_test.tv_insert_user
import kotlinx.android.synthetic.main.activity_test.tv_query_all_user
import kotlinx.android.synthetic.main.activity_test.tv_update_user
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

        tv_insert_user.setOnClickListener {
            GlobalScope.launch {
                MyApplication.db.userDao()
                    .insertUser(User(1, "18625916235"))
            }
        }
        tv_query_all_user.setOnClickListener {
            GlobalScope.launch {
                val allUser = MyApplication.db.userDao().getAllUser()
                for (user in allUser) {

                }

            }
        }

        tv_update_user.setOnClickListener {
            var currentUser: User

            GlobalScope.launch {
                val allUser = MyApplication.db.userDao().getAllUser()
                if (allUser.isNotEmpty()) {
                    for (user in allUser) {
                        if (user.phoneNum == "18625916235") {
                            currentUser = user

                            currentUser.phoneNum = "186"
                            GlobalScope.launch {
                                MyApplication.db.userDao()
                                    .updateUser(currentUser)
                            }
                        }
                    }

                }
            }
        }
    }
}