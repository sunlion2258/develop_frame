package com.sun.dev.fragment.blog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sun.dev.entity.BlogBean
import com.sun.dev.inter.BlogCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**
 * Created by SunLion on 2019/12/13.
 */
class BlogViewModel(private var repository: BlogRepository, private val listener: BlogCallback) :
    ViewModel() {


    var doc: Document? = null
    var urlElements: Elements? = null

    var pageElements: Elements? = null

    //view回调集合
    val list = MutableLiveData<List<BlogBean>>()
    //操作集合
    private var handleList = mutableListOf<BlogBean>()

    fun getDataByJsoup(strUrlPath: String) {
        handleList.clear()
        //协程
        GlobalScope.launch {
            GlobalScope.async {
                doc = Jsoup.connect(strUrlPath).get()
                println("---------------${doc!!.html()}")
                //标题和url都在<div class="article-item-box csdn-tracking-statistics">中
                urlElements = doc!!.select(".article-list").select("h4")

                //遍历并添加
                urlElements!!.forEach {
                    val url = it.select("a").attr("href")
                    val blogBean = BlogBean(it.text(), url)
                    handleList.add(blogBean)
                }
                //回调数据
                listener.callBack(handleList)
                return@async
            }

        }
    }
}