package com.sun.dev.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityCreateSessionBinding
import com.sun.dev.viewmodel.CreateSessionViewModel
import com.sun.dev.viewrepository.CreateSessionRepository
import com.sun.dev.vmfactory.CreateSessionVMFactory
import kotlinx.android.synthetic.main.activity_create_session.*

/**
 * Created by SunLion on 2024年2月21日.
 */
class CreateSessionActivity :
    BaseMVVMActivity<ActivityCreateSessionBinding, CreateSessionViewModel>() {
    override fun initViewModel(): CreateSessionViewModel =
        ViewModelProviders.of(this, CreateSessionVMFactory(this, CreateSessionRepository()))
            .get(CreateSessionViewModel::class.java)

    companion object {
        fun start(
            activity: Activity,
            title: String?,
        ) {
            val intent = Intent(activity, CreateSessionActivity::class.java)
            intent.putExtra("title", title)
            activity.startActivity(intent)
        }
    }

    override fun initContentViewID(): Int = R.layout.activity_create_session
    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar_create_session)
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .navigationBarColor(android.R.color.black)
            .navigationBarDarkIcon(true)
            .init()
        bindViews.toolbarCreateSession.setTitle("我是创建新会话")
    }

}