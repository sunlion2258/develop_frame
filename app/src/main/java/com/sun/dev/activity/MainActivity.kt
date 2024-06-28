@file:Suppress("DEPRECATION")

package com.sun.dev.activity

import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseActivity
import com.sun.dev.fragment.BlogFragment
import com.sun.dev.fragment.home.HomeFragment
import com.sun.dev.fragment.mine.MineFragment
import com.sun.dev.fragment.mine.MineRepository
import com.sun.dev.fragment.mine.MineVMFactory
import com.sun.dev.fragment.mine.MineViewModel
import com.sun.dev.util.toast
import kotlinx.android.synthetic.main.activity_main.container
import kotlinx.android.synthetic.main.activity_main.lottie_home
import kotlinx.android.synthetic.main.activity_main.lottie_mine
import kotlinx.android.synthetic.main.activity_main.rl_main
import kotlinx.android.synthetic.main.activity_main.rl_mine
import kotlinx.android.synthetic.main.activity_main.tv_main
import kotlinx.android.synthetic.main.activity_main.tv_mine


class MainActivity : BaseActivity() {
    override fun initContentViewID(): Int = R.layout.activity_main

    override fun onViewCreated() {
        super.onViewCreated()

        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(false)
            .navigationBarColor(android.R.color.white)
            .navigationBarDarkIcon(true)
            .init()

        //初始化Fragment管理类
        mainFragmentManager =
            MainFragmentManager(supportFragmentManager, container.id)


        rl_main.setOnClickListener {
            setImmersionBar(0)
            mainFragmentManager.select(0)
            tv_main.setTextColor(resources.getColor(R.color.common_blue))
            tv_mine.setTextColor(resources.getColor(R.color.color_c5))
            lottie_home.repeatCount=0
            lottie_home.playAnimation()
        }
        rl_mine.setOnClickListener {
            setImmersionBar(2)
            mainFragmentManager.select(2)
            tv_main.setTextColor(resources.getColor(R.color.color_c5))
            tv_mine.setTextColor(resources.getColor(R.color.common_blue))
            lottie_mine.repeatCount=0
            lottie_mine.playAnimation()
        }


        //MineFragment的ViewModel
        ViewModelProviders.of(this, MineVMFactory(MineRepository(), this)).get(MineViewModel::class.java)
    }

    private lateinit var mainFragmentManager: MainFragmentManager

    class MainFragmentManager(
        private val fragmentManager: FragmentManager,
        private val containerId: Int
    ) {

        private var lastFragment = 0
        private var fragments = mutableListOf<Fragment>()

        //初始化Fragment，并且显示第一个Fragment
        init {
            fragments.add(HomeFragment())
            fragments.add(BlogFragment.newInstance("https://blog.csdn.net/qq_36255612"))
            fragments.add(MineFragment())
            fragmentManager.beginTransaction().replace(containerId, fragments[0])
                .commitAllowingStateLoss()
        }

        fun select(position: Int) {
            val transaction = fragmentManager.beginTransaction()
            if (lastFragment != position) {
                //隐藏上一个fragment
                transaction.hide(fragments[lastFragment])
                //如果这个fragment没有添加到Transaction中，那么进行添加
                if (!fragments[position].isAdded) {
                    transaction.add(containerId, fragments[position])
                }
                transaction.show(fragments[position]).commitAllowingStateLoss()
                lastFragment = position
            }
        }
    }

    /**
     * 修改状态栏
     */
    private fun setImmersionBar(position: Int) {
        when (position) {
            0 -> {
                ImmersionBar.with(this)
                    .statusBarDarkFont(true)
                    .navigationBarColor(android.R.color.white)
                    .navigationBarDarkIcon(true)
                    .init()
            }
            1 -> {
                ImmersionBar.with(this)
                    .statusBarDarkFont(true)
                    .navigationBarColor(android.R.color.white)
                    .navigationBarDarkIcon(true)
                    .init()
            }
            2 -> {
                ImmersionBar.with(this)
                    .statusBarDarkFont(true)
                    .navigationBarColor(android.R.color.white)
                    .navigationBarDarkIcon(true)
                    .init()
            }
        }
    }

    // 用来计算返回键的点击间隔时间
    private var exitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && event!!.action == KeyEvent.ACTION_DOWN
        ) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                toast("再按一次退出程序")
                exitTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)

    }
}
