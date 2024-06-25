package com.sun.dev.fragment.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.inter.BlogCallback

/**
 * Created by SunLion on 2019/12/13.
 */
@Suppress("UNCHECKED_CAST")
class BlogVMFactory(private val repository: BlogRepository,private val listener: BlogCallback) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BlogViewModel(repository,listener) as T
    }
}