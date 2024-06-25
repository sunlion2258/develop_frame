package com.sun.dev.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.activity.CreateSessionActivity
import com.sun.dev.fragment.mine.AudioRecordActivity
import com.sun.dev.viewmodel.AudioRecordViewModel
import com.sun.dev.viewmodel.CreateSessionViewModel
import com.sun.dev.viewrepository.AudioRecordRepository
import com.sun.dev.viewrepository.CreateSessionRepository

/**
 *       Created by SunLion on 2019/5/6 14:16
 */
@Suppress("UNCHECKED_CAST")
class CreateSessionVMFactory(private val activity: CreateSessionActivity, val repository: CreateSessionRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateSessionViewModel(activity, repository) as T
    }
}