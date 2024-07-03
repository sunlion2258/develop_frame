package com.sun.dev.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.viewmodel.UnityModel
import com.sun.dev.viewrepository.UnityRepository

/**
 * Created by SunLion on 2019/12/5.
 */
@Suppress("UNCHECKED_CAST")
class UnityFactory(private val repository: UnityRepository):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UnityModel(repository) as T

    }
}