package com.sun.dev.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivitySettingBinding
import com.sun.dev.datebase.DatabaseProvider
import com.sun.dev.entity.AssessmentBean
import com.sun.dev.viewmodel.SettingModel
import com.sun.dev.viewrepository.SettingRepository
import com.sun.dev.vmfactory.SettingFactory
import kotlinx.android.synthetic.main.activity_test.toolbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

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
        val db = DatabaseProvider.getDatabase(this@SettingActivity)
        GlobalScope.launch {
            val allAssessmentLevel = db.assessmentLevelDao().getAllAssessmentLevel()
            if (allAssessmentLevel.isNotEmpty()) {

                runOnUiThread {
                    toast("有数据了:  ${allAssessmentLevel[0]}")
                }
                return@launch
            }

            db.assessmentLevelDao().insertAssessmentLevel(AssessmentBean("1", "5", "6"))
            for (assessmentBean in allAssessmentLevel) {
                runOnUiThread {
                    toast(assessmentBean.toString())
                }
            }
        }
    }

    override fun initContentViewID(): Int = R.layout.activity_setting

}