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
import android.provider.Settings
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_setting.seekBar

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

        // 获取当前屏幕亮度
        val currentBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 0)
        seekBar.progress = currentBrightness
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 调节屏幕亮度
                val layoutParams = window.attributes
                layoutParams.screenBrightness = progress / 255.0f
                window.attributes = layoutParams
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 不需要实现
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 不需要实现
            }
        })


    }

    override fun initContentViewID(): Int = R.layout.activity_setting

}