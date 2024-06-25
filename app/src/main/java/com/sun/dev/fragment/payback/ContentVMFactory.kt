package com.sun.dev.fragment.payback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *       Created by SunLion on 2019/4/29 18:05
 */
@Suppress("UNCHECKED_CAST")
class ContentVMFactory(val repository: ContentRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContentViewModel(repository) as T
    }
}