package com.sun.dev.vmfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.dev.fragment.mine.AudioRecordActivity
import com.sun.dev.viewmodel.AudioRecordViewModel
import com.sun.dev.viewrepository.AudioRecordRepository

/**
 *  Created by SunLion on 2019/5/6 14:16
 */
@Suppress("UNCHECKED_CAST")
class AudioRecordVMFactory(private val activity: AudioRecordActivity, val repository: AudioRecordRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AudioRecordViewModel(activity, repository) as T
    }
}