package com.sun.dev.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.viewmodel.TestModel
import com.sun.dev.viewrepository.TestRepository

/**
 * Created by SunLion on 2019/12/5.
 */
@Suppress("UNCHECKED_CAST")
class TestFactory(private val repository: TestRepository):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TestModel(repository) as T

    }
}