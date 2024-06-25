package com.sun.dev.fragment.payback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sun.dev.entity.PayDataBean

/**
 *  Created by SunLion on 2019/4/29 17:57
 */
class ContentViewModel(val repository: ContentRepository) : ViewModel() {

    val allList = MutableLiveData<List<PayDataBean>>()

    val checkList: LiveData<List<PayDataBean>> = Transformations.map(allList) { list ->
        list.filter {
            it.state == PayDataBean.STATE_CHECK
        }
    }

    val waitMoneyList: LiveData<List<PayDataBean>> = Transformations.map(allList) { list ->
        list.filter {
            it.state == PayDataBean.STATE_WAIT_MONEY
        }
    }

    val waitReturnList: LiveData<List<PayDataBean>> = Transformations.map(allList) { list ->
        list.filter {
            it.state == PayDataBean.STATE_WAIT_RETURN
        }
    }

    fun getList() {
        allList.value = repository.getList()
    }
}