package com.sun.dev.fragment.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *  Created by SunLion on 2019/4/29 18:05
 */
@Suppress("UNCHECKED_CAST")
class HomeVMFactory(val repository: HomeRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}