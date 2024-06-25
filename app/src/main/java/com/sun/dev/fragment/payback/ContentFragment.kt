@file:Suppress("DEPRECATION")

package com.sun.dev.fragment.payback

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.adapter.ContentPagerAdapter
import com.sun.dev.base.BaseMVVMFragment
import com.sun.dev.databinding.FragmentMainPaybackBinding
import kotlinx.android.synthetic.main.fragment_main_payback.*

/**
 *  Created by SunLion on 2019/4/29 17:54
 */
@Suppress("DEPRECATION")
class ContentFragment : BaseMVVMFragment<FragmentMainPaybackBinding, ContentViewModel>() {
    override fun initContentViewID(): Int = R.layout.fragment_main_payback

    override fun initViewModel(): ContentViewModel =
        ViewModelProviders.of(this, ContentVMFactory(ContentRepository())).get(ContentViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()


        //适配器
        viewpager.adapter = ContentPagerAdapter((context as FragmentActivity).supportFragmentManager)
        payback_tabs.setupWithViewPager(viewpager)
        viewpager.currentItem = 0
    }

}