package com.sun.dev.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *       Created by SunLion on 2019/5/6 14:16
 */
@Suppress("UNCHECKED_CAST")
class LoginVMFactory(private val activity: LoginActivity, val repository: LoginRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(activity, repository) as T
    }
}