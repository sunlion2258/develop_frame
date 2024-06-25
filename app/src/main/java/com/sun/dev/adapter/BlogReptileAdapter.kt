package com.sun.dev.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sun.dev.R
import com.sun.dev.entity.BlogBean

/**
 * Created by SunLion on 2019/12/18.
 */
class BlogReptileAdapter(mList: List<BlogBean>) :
    BaseQuickAdapter<BlogBean, BaseViewHolder>(R.layout.content_blog_reptile,mList) {
    override fun convert(helper: BaseViewHolder, item: BlogBean?) {

        helper.setText(R.id.tv_title, item!!.title)
    }
}