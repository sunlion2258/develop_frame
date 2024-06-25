@file:Suppress("DEPRECATION")

package com.sun.dev.fragment


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sun.dev.R
import com.sun.dev.adapter.ContentRecyclerAdapter
import com.sun.dev.base.BaseMVVMFragment
import com.sun.dev.databinding.FragmentCommonPaybackBinding
import com.sun.dev.entity.PayDataBean
import com.sun.dev.fragment.payback.ContentRepository
import com.sun.dev.fragment.payback.ContentVMFactory
import com.sun.dev.fragment.payback.ContentViewModel

/**
 *  Created by SunLion on 2019/4/30 14:07
 */
class PaybackFragment : BaseMVVMFragment<FragmentCommonPaybackBinding, ContentViewModel>() {

    private var mList = mutableListOf<PayDataBean>()

    override fun initContentViewID(): Int = R.layout.fragment_common_payback

    override fun initViewModel(): ContentViewModel =ViewModelProviders.of(activity!!,ContentVMFactory(ContentRepository())).get(
        ContentViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //测试列表
        val recyclerAdapter = ContentRecyclerAdapter()
        bindView.paybackRv.adapter=recyclerAdapter

        viewModel.allList.observe(viewLifecycleOwner, Observer {
            mList.addAll(it)
            recyclerAdapter.setNewData(it)
        })
        viewModel.getList()
    }

}

