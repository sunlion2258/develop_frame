@file:Suppress("DEPRECATION")

package com.sun.dev.activity

import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.blankj.rxbus.RxBus
import com.blankj.rxbus.RxBus.Callback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseActivity
import com.sun.dev.common.Constants
import com.sun.dev.fragment.BlogFragment
import com.sun.dev.fragment.home.HomeFragment
import com.sun.dev.fragment.mine.MineFragment
import com.sun.dev.fragment.mine.MineRepository
import com.sun.dev.fragment.mine.MineVMFactory
import com.sun.dev.fragment.mine.MineViewModel
import com.sun.dev.util.toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.ParameterizedType


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
        //设置底部导航选择监听
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        nav_view.selectedItemId = 0

        //MineFragment的ViewModel
        ViewModelProviders.of(this, MineVMFactory(MineRepository(), this)).get(MineViewModel::class.java)

        //接收到其他方式登录点击
//        RxBus.getDefault().subscribe(this, Constants.RxBusTag.LOGIN_BACK, Callback<String> {
//            //手动调到首页
//            mainFragmentManager.select(0)
//            nav_view.selectedItemId = R.id.bottom_home
//        })
    }


    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    setImmersionBar(0)
                    mainFragmentManager.select(0)
                    return@OnNavigationItemSelectedListener true
                }
//                R.id.bottom_return_money -> {
//                    setImmersionBar(1)
//                    mainFragmentManager.select(1)
//                    return@OnNavigationItemSelectedListener true
//                }
                R.id.bottom_mine -> {
                    setImmersionBar(2)
                    mainFragmentManager.select(2)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
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
//            fragments.add(ContentFragment())
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
