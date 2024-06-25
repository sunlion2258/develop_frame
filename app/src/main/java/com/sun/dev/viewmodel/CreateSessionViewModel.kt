package com.sun.dev.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.sun.dev.activity.CreateSessionActivity
import com.sun.dev.fragment.mine.AudioRecordActivity
import com.sun.dev.viewrepository.AudioRecordRepository
import com.sun.dev.viewrepository.CreateSessionRepository

/**
 *  Created by SunLion on 2024年1月3日
 */
@SuppressLint("CheckResult")
class CreateSessionViewModel(private val activity: CreateSessionActivity, private val repository: CreateSessionRepository) : ViewModel() {

}