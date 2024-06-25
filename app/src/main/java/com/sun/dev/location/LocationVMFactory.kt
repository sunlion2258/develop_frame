package com.sun.dev.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by SunLion on 2019/10/31.
 */
@Suppress("UNCHECKED_CAST")
class LocationVMFactory(val repository: LocationRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (LocationViewModel(repository) as T)
    }
}