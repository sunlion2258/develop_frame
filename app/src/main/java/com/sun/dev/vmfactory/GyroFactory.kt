package com.sun.dev.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.viewmodel.GyroModel
import com.sun.dev.viewrepository.GyroRepository

/**
 * Created by SunLion on 2019/12/5.
 */
@Suppress("UNCHECKED_CAST")
class GyroFactory(private val repository: GyroRepository):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GyroModel(repository) as T

    }
}