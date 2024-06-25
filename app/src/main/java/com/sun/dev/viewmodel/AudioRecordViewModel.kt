package com.sun.dev.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.sun.dev.fragment.mine.AudioRecordActivity
import com.sun.dev.viewrepository.AudioRecordRepository

/**
 *  Created by SunLion on 2024年1月3日
 */
@SuppressLint("CheckResult")
class AudioRecordViewModel(private val activity: AudioRecordActivity, private val repository: AudioRecordRepository) : ViewModel() {

}