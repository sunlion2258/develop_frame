package com.sun.dev.fragment.blog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sun.dev.R
import com.sun.dev.adapter.BlogReptileAdapter
import com.sun.dev.base.BaseMVVMFragment
import com.sun.dev.common.Constants
import com.sun.dev.databinding.FragmentBlogReptileBinding
import com.sun.dev.entity.BlogBean
import com.sun.dev.inter.BlogCallback
import kotlinx.android.synthetic.main.fragment_blog_reptile.*

/**
 * Created by SunLion on 2019年12月13日14:18:17
 *  获取Html数据
 */
@Suppress("DEPRECATION")
class BlogReptileFragment : BaseMVVMFragment<FragmentBlogReptileBinding, BlogViewModel>(){
    var layoutManager:StaggeredGridLayoutManager?=null

    private val mList = mutableListOf<BlogBean>()

    //接口实现
    val listener = object : BlogCallback {
        override fun callBack(mutableList: MutableList<BlogBean>) {
            mList.clear()
            mList.addAll(mutableList)
            (recycler_view.adapter as BlogReptileAdapter).notifyDataSetChanged()
        }
    }


    override fun initViewModel(): BlogViewModel =
        ViewModelProviders.of(this, BlogVMFactory(BlogRepository(),listener)).get(BlogViewModel::class.java)

    override fun initContentViewID() = R.layout.fragment_blog_reptile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView.vm = viewModel

        recycler_view.adapter = BlogReptileAdapter(mList)
        layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //解决最后一个动画，so直接去掉动画
        recycler_view.layoutManager=layoutManager

        recycler_view.animation=null
        layoutManager!!.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE;
        //获取数据
        viewModel.getDataByJsoup(Constants.URL.BLOG_URL)
    }

}