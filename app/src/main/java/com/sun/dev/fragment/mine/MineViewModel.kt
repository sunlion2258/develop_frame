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
import com.sun.dev.util.toast
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
            R.id.mine_drawing -> activity.startActivity<TitleWithContentActivity>(
                Pair(
                    Constants.SP.TITLE_ACTIVITY_TYPE,
                    TitleWithContentActivity.TYPE_DRAWING
                )
            )
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


}
