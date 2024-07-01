package com.sun.dev.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.adapter.ChangeLanguageListAdapter
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.bean.ChangeLanguageBean
import com.sun.dev.common.Constants
import com.sun.dev.databinding.ActivityChangeLanguageBinding
import com.sun.dev.util.SharedHelper
import com.sun.dev.viewmodel.ChangeLanguageViewModel
import com.sun.dev.viewrepository.ChangeLanguageRepository
import com.sun.dev.vmfactory.ChangeLanguageVMFactory
import kotlinx.android.synthetic.main.activity_change_language.toolbar_change_language


/**
 * Created by fengwj on 2024/5/10.
 */
class ChangeLanguageActivity :
    BaseMVVMActivity<ActivityChangeLanguageBinding, ChangeLanguageViewModel>() {
    private var mListAdapter = ChangeLanguageListAdapter()
    private var mList = mutableListOf<ChangeLanguageBean>()

    override fun initViewModel(): ChangeLanguageViewModel =
        ViewModelProviders.of(
            this,
            ChangeLanguageVMFactory(this, ChangeLanguageRepository())
        )[ChangeLanguageViewModel::class.java]



    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar_change_language)
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .navigationBarColor(android.R.color.black)
            .navigationBarDarkIcon(true)
            .init()
        mListAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->
            val changeLanguageBean = mList[i]
            val language = changeLanguageBean.language
            val languageCode = changeLanguageBean.languageCode
            SharedHelper.getEdit { sp -> sp.putString(Constants.SP.LANGUAGE, languageCode) }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                recreate()
            }
        }

        bindViews.recyclerViewChangeLanguage.layoutManager = LinearLayoutManager(this)
        bindViews.recyclerViewChangeLanguage.adapter = mListAdapter

        mList.add(0, ChangeLanguageBean("English", "en"))
        mList.add(1, ChangeLanguageBean("简体中文", "zh"))
        mList.add(2, ChangeLanguageBean("繁體中文", "zh-rHK"))
        mListAdapter.setNewData(mList)

    }

    override fun initContentViewID(): Int = R.layout.activity_change_language
}