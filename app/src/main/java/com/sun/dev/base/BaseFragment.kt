package com.sun.dev.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.sun.dev.R

/**
 *       Created by SunLion on 2019/4/30 14:08
 */
abstract class BaseFragment : Fragment(), CommonMethod {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(initContentViewID(), container, false)
    }
}