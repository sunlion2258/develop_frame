package com.sun.dev.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sun.dev.fragment.BlogFragment
import com.sun.dev.fragment.PaybackFragment
import com.sun.dev.fragment.blog.BlogReptileFragment

/**
 * Created by SunLion on 2019/11/28.
 */
@Suppress("DEPRECATION")
class ContentPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {

    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()

    init {
        titles.add("学习参考")
        titles.add("爬虫数据")
        titles.add("控件之道")
        titles.add("借款明细")
        fragments.add(BlogFragment.newInstance("https://blog.csdn.net/qq_36255612"))
        fragments.add(BlogReptileFragment())
        fragments.add(PaybackFragment())
        fragments.add(PaybackFragment())
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}