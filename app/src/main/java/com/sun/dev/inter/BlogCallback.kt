package com.sun.dev.inter

import com.sun.dev.entity.BlogBean

/**
 * Created by SunLion on 2019/12/20.
 * 博客数据抓取回调
 */
interface BlogCallback {
    fun callBack(mutableList: MutableList<BlogBean>)
}