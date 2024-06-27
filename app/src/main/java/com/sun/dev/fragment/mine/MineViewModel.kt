package com.sun.dev.fragment.mine

import android.Manifest
import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sun.dev.R
import com.sun.dev.activity.MainActivity
import com.sun.dev.activity.TitleWithContentActivity
import com.sun.dev.common.Constants
import com.sun.dev.dialog.BottomDialog
import com.sun.dev.dialog.EdittextDialog
import com.sun.dev.location.LocationActivity
import com.sun.dev.util.toast
import com.tbruyelle.rxpermissions2.RxPermissions
import org.jetbrains.anko.startActivity

/**
 *  Created by SunLion on 2019/4/29 17:57
 */
@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS")
@SuppressLint("StaticFieldLeak")
class MineViewModel(repository: MineRepository, val activity: MainActivity) : ViewModel() {

    private val account = MutableLiveData<String>()

    init {
        account.value = repository.getAccount()
    }

    /**
     * 所有点击事件
     */
    var mineViewOnClick = View.OnClickListener {
        when (it.id) {
            R.id.mine_my -> activity.startActivity<TitleWithContentActivity>(
                Pair(
                    Constants.SP.TITLE_ACTIVITY_TYPE,
                    TitleWithContentActivity.TYPE_MY_INFO
                )
            )
            R.id.mine_location -> {
                checkPermission(it)
            }
            R.id.mine_drawing -> activity.startActivity<TitleWithContentActivity>(
                Pair(
                    Constants.SP.TITLE_ACTIVITY_TYPE,
                    TitleWithContentActivity.TYPE_DRAWING
                )
            )
            R.id.mine_contact -> {
                checkContactPermission(it)
            }
            //退出按钮点击
            R.id.tv_quit -> {
                val fragment = BottomDialog().newInstance()
                fragment.show(activity.supportFragmentManager, "myAlert")
            }
            R.id.mine_jump_url -> {
                val fragment = EdittextDialog().newInstance()
                fragment.show(activity.supportFragmentManager, "MineViewModel")
            }
        }
    }


    /**
     * 检查权限
     */
    @SuppressLint("CheckResult")
    private fun checkPermission(view: View) {
        RxPermissions(view.context as FragmentActivity).request(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).subscribe {
            if (it) {
                activity.startActivity<LocationActivity>()
            } else {
                toast("需要同意定位权限")
            }
        }
    }

    /**
     * 通话记录权限
     */
    @SuppressLint("CheckResult")
    private fun checkContactPermission(view: View) {
        RxPermissions(view.context as FragmentActivity).request(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG
        ).subscribe {
            if (it) {
                activity.startActivity<TitleWithContentActivity>(
                    Pair(
                        Constants.SP.TITLE_ACTIVITY_TYPE,
                        TitleWithContentActivity.TYPE_CONTACT
                    )
                )
            } else {
                toast("需要同意相关权限")
            }
        }
    }
}
