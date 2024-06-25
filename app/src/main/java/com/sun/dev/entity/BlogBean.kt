package com.sun.dev.entity

/**
 * Created by SunLion on 2019/12/13.
 * 博客对象
 */
class BlogBean {
     var title: String? = null
     var url: String? = null

    constructor(title: String?, url: String?) {
        this.title = title
        this.url = url
    }
}