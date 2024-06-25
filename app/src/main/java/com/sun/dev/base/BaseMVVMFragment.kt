package com.sun.dev.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

/**
 *  Created by SunLion on 2019/4/29 17:15
 *  MVVM的Fragment基类，持有一个ViewDataBind和一个ViewModel
 *  使用两个抽象函数初始化这两个实例
 */
abstract class BaseMVVMFragment<VSD : ViewDataBinding, VM : ViewModel> : Fragment(), CommonMethod {

    lateinit var bindView: VSD
    lateinit var viewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(initContentViewID(), container, false)
        bindView = DataBindingUtil.bind(v)!!
        viewModel = initViewModel()
        bindView.lifecycleOwner = this
        return v
    }

    abstract fun initViewModel(): VM
}