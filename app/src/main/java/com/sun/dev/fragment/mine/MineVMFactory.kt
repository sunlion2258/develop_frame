package com.sun.dev.fragment.mine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.activity.MainActivity

/**
 * Created by SunLion on 2019/4/29 18:05
 */
@Suppress("UNCHECKED_CAST")
class MineVMFactory(val repository: MineRepository,val activity: MainActivity) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MineViewModel(repository,activity) as T
    }
}