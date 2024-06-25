package com.sun.dev.fragment.mine

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityAudioRecordBinding
import com.sun.dev.viewrepository.AudioRecordRepository
import com.sun.dev.vmfactory.AudioRecordVMFactory
import com.sun.dev.viewmodel.AudioRecordViewModel
import kotlinx.android.synthetic.main.activity_audio_record.*
import org.jetbrains.anko.toast

/**
 * Created by fengwj on 2024/1/3.
 * 音频录制
 */
class AudioRecordActivity : BaseMVVMActivity<ActivityAudioRecordBinding, AudioRecordViewModel>() {
    companion object {
        fun start(
            activity: Activity,
            title: String?,
        ) {
            val intent = Intent(activity, AudioRecordActivity::class.java)
            intent.putExtra("title", title)
            activity.startActivity(intent)
        }
    }

    override fun initContentViewID(): Int = R.layout.activity_audio_record

    override fun initViewModel(): AudioRecordViewModel =
        ViewModelProviders.of(this, AudioRecordVMFactory(this, AudioRecordRepository()))
            .get(AudioRecordViewModel::class.java)


    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this,toolbar_record)
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .navigationBarColor(android.R.color.white)
            .navigationBarDarkIcon(true)
            .init()

        bindViews.vm = vm

        bindViews.toolbarRecord.setTitle("我是录制")
        bindViews.btnRecord.setOnClickListener {
            AudioRecorder().startRecording()
            bindViews.btnRecord.text="录制中"
        }
        //添加延时，保证StatusBar完全显示后再进行动画。
        Handler().postDelayed({
            AudioRecorder().stopRecording()
            bindViews.btnRecord.text="录制完成"
            toast("录制已完成，请打开文件管理查看")
            finish()
        }, 6000)

    }
}