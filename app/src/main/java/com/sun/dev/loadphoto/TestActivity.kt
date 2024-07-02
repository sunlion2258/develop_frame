@file:Suppress("DEPRECATION")

package com.sun.dev.loadphoto

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.common.Constants
import com.sun.dev.databinding.ActivityTestBinding
import com.sun.dev.datebase.DatabaseProvider
import com.sun.dev.entity.DrillRecordBean
import com.sun.dev.entity.User
import com.sun.dev.util.toast
import kotlinx.android.synthetic.main.activity_test.et_phone
import kotlinx.android.synthetic.main.activity_test.toolbar
import kotlinx.android.synthetic.main.activity_test.tv_delete_user
import kotlinx.android.synthetic.main.activity_test.tv_insert_user
import kotlinx.android.synthetic.main.activity_test.tv_query_all_user
import kotlinx.android.synthetic.main.activity_test.tv_update_user
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * Created by SunLion on 2019/12/5.
 */
@Suppress("DEPRECATION")
class TestActivity : BaseMVVMActivity<ActivityTestBinding, TestModel>(),
    View.OnClickListener {

    private var inputNum: String? = null

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

        val db = DatabaseProvider.getDatabase(this@TestActivity)

        tv_insert_user.setOnClickListener {
            inputNum = et_phone.text.toString().trim()
            if (!TextUtils.isEmpty(inputNum)) {
                GlobalScope.launch {
                    for (user in db.userDao().getAllUser()) {
                        if (user.phoneNum == inputNum) {
                            toast("用户表中数据存在请修改")
                            return@launch
                        }
                    }

                    db.userDao().insertUser(User(et_phone.text.toString().trim(), "哈哈", 5, 19))
                    for (drillRecordBean in db.drillRecordDao().getAllDrillRecord()) {
                        if (drillRecordBean.drillRecordId==inputNum){
                            toast("记录表中数据存在请修改")
                            return@launch
                        }
                    }

                    db.drillRecordDao().insertDrillRecord(DrillRecordBean(et_phone.text.toString().trim(), "90"))
                }

                val internalDbFile = getDatabasePath(Constants.DATABASE_NAME)
                if (internalDbFile.exists()) {
                    DatabaseProvider.updateExternalDatabase(this@TestActivity, internalDbFile)
                }
            } else {
                toast("请输入手机号")
            }
        }
        tv_query_all_user.setOnClickListener {
            inputNum = et_phone.text.toString().trim()
            val builder = StringBuilder()
            GlobalScope.launch {
                val userDao = db.userDao()
                val allUser = userDao.getAllUser()
                for (user in allUser) {
                    builder.append(user.toString())
                    builder.append("\n")
                }
                builder.append("\n\n")

                for (drillRecordBean in db.drillRecordDao().getAllDrillRecord()) {
                    builder.append("训练记录： $drillRecordBean")
                    builder.append("\n")
                }

                runOnUiThread {
                    toast(builder.toString())
                }
            }
        }

        tv_update_user.setOnClickListener {
            inputNum = et_phone.text.toString().trim()
            if (!TextUtils.isEmpty(inputNum)) {
                GlobalScope.launch {
                    val allUser = db.userDao().getAllUser()
                    if (allUser.isNotEmpty()) {
                        for (user in allUser) {
                            if (user.phoneNum == inputNum) {
                                db.userDao().updateUser(User(user.phoneNum, "拉拉", 8, 19))
                            }
                        }
                    }
                }
            } else {
                toast("请输入手机号")
            }
        }
        tv_delete_user.setOnClickListener {
            inputNum = et_phone.text.toString().trim()
            if (!TextUtils.isEmpty(inputNum)) {
                GlobalScope.launch {
                    db.userDao().deleteUser(et_phone.text.toString().trim())
                }
            } else {
                toast("请输入手机号")
            }
        }
    }
}