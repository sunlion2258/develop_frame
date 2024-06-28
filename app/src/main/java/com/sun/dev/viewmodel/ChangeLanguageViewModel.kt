package com.sun.dev.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.ViewModel
import com.sun.dev.viewrepository.ChangeLanguageRepository

/**
 *  Created by SunLion on 2024年5月10日
 */
@SuppressLint("CheckResult")
class ChangeLanguageViewModel(private val activity:Activity, private val repository: ChangeLanguageRepository) : ViewModel() {

}