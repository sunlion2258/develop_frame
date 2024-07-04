package com.sun.dev.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.viewmodel.GLBModel
import com.sun.dev.viewrepository.GLBRepository

/**
 * Created by SunLion on 2019/12/5.
 */
@Suppress("UNCHECKED_CAST")
class GLBFactory(private val repository: GLBRepository):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GLBModel(repository) as T

    }
}