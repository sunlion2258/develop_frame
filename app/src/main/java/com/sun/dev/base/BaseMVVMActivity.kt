package com.sun.dev.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.sun.dev.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 *  Created by SunLion on 2019/4/29 17:08
 *  MVVM的Activity基类，持有一个ViewDataBind和一个ViewModel
 *  使用两个抽象函数初始化这两个实例
 */
abstract class BaseMVVMActivity<VDB : ViewDataBinding, VM : ViewModel> : AppCompatActivity(), CommonMethod {

    lateinit var bindViews: VDB
    lateinit var vm: VM
    //订阅
    var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViews = DataBindingUtil.setContentView(this, initContentViewID())
        //bindViews=DataBindingUtil.inflate<VDB>(LayoutInflater.from(this),initContentViewID(),null,false)
        bindViews.lifecycleOwner = this
        vm = initViewModel()

        mCompositeDisposable = CompositeDisposable()
        onMVVMCreated(savedInstanceState)

    }

    abstract fun initViewModel(): VM

    /**
     * 创建bindView和ViewModel后，在OnCreate方法中继续做的事情
     */
    abstract fun onMVVMCreated(savedInstanceState: Bundle?)

    /**
     * 添加订阅
     */
    fun addDisposable(mDisposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(mDisposable)
    }

    /**
     * 取消所有订阅
     */
    private fun clearDisposable() {
        mCompositeDisposable?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        //及时销毁订阅
        clearDisposable()
    }


}