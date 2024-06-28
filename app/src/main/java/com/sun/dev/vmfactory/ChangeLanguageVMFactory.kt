package com.sun.dev.vmfactory

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.viewmodel.ChangeLanguageViewModel
import com.sun.dev.viewrepository.ChangeLanguageRepository

/**
 *  Created by SunLion on 2024/5/10 14:16
 */
@Suppress("UNCHECKED_CAST")
class ChangeLanguageVMFactory(val activity:Activity, val repository: ChangeLanguageRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChangeLanguageViewModel(activity,repository) as T
    }
}