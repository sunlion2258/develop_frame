package com.sun.dev.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.CallLog
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.dev.R
import com.sun.dev.adapter.CallLogAdapter
import com.sun.dev.base.BaseFragment
import com.sun.dev.entity.CallLogBean
import kotlinx.android.synthetic.main.fragment_my_contact.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 通话记录
 */
@Suppress("NAME_SHADOWING", "UNUSED_VARIABLE")
class ContactFragment : BaseFragment() {

    private val callUri = CallLog.Calls.CONTENT_URI
    private val columns = arrayOf(
        CallLog.Calls.CACHED_NAME// 通话记录的联系人
        , CallLog.Calls.NUMBER// 通话记录的电话号码
        , CallLog.Calls.DATE// 通话记录的日期
        , CallLog.Calls.DURATION// 通话时长
        , CallLog.Calls.TYPE
    )// 通话类型}

    //通话记录的集合
    private val mList = mutableListOf<CallLogBean>()


    override fun initContentViewID(): Int = R.layout.fragment_my_contact

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview.adapter = CallLogAdapter(mList)

        getContentCallLog()

    }

    //获取通话记录
    @SuppressLint("MissingPermission", "SimpleDateFormat")
    private fun getContentCallLog() {
        val cursor = activity!!.contentResolver.query(
            callUri, // 查询通话记录的URI
            columns
            , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        )
        while (cursor!!.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))  //姓名
            val number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) //号码
            val dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)) //获取通话日期
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(dateLong))
            val time = SimpleDateFormat("HH:mm").format(Date(dateLong))
            val duration =
                cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION))//获取通话时长，值为多少秒
            val type =
                cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) //获取通话类型：1.呼入2.呼出3.未接
            val dayCurrent = SimpleDateFormat("dd").format(Date())
            val dayRecord = SimpleDateFormat("dd").format(Date(dateLong))

            val callLogBean = CallLogBean()
            if (!TextUtils.isEmpty(name)) {
                callLogBean.name = name
            } else {
                callLogBean.name = "无"
            }

            callLogBean.number = number
            callLogBean.dateLong = dateLong.toString()
            callLogBean.date = date

            mList.add(callLogBean)
        }
        if (mList.size > 0) {
            rl_empty_layout.visibility = View.GONE
            recyclerview.visibility = View.VISIBLE
        } else {
            rl_empty_layout.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE

        }

        recyclerview.adapter!!.notifyDataSetChanged()
    }

}
