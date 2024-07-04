package com.sun.dev.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.viewmodel.SettingModel
import com.sun.dev.viewrepository.SettingRepository

/**
 * Created by SunLion on 2019/12/5.
 */
@Suppress("UNCHECKED_CAST")
class SettingFactory(private val repository: SettingRepository):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingModel(repository) as T
    }
}