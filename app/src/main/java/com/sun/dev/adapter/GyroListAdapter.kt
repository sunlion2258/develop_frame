package com.sun.dev.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sun.dev.R
import com.sun.dev.bean.GyroBean
import com.sun.dev.bean.RedPacketRankBean

/**
 * Created by fengwj on 2024/6/26.
 */
class GyroListAdapter :
    BaseQuickAdapter<GyroBean, BaseViewHolder>(R.layout.item_gyro) {
    override fun convert(holder: BaseViewHolder, bean: GyroBean?) {
        holder.setText(
            R.id.tv_title, when (bean?.name) {
                "ACCELEROMETER" -> "加速度传感器：${bean.name}"
                "AMBIENT TEMPERATURE" -> "温度传感器：${bean.name}"
                "GYROSCOPE" -> "陀螺仪传感器：${bean.name}"
                "LIGHT" -> "光线传感器：${bean.name}"
                "MAGNETIC FIELD" -> "磁场传感器：${bean.name}"
                "PRESSURE" -> "压力传感器：${bean.name}"
                "PROXIMITY" -> "临近传感器：${bean.name}"
                "RELATIVE HUMIDITY" -> "湿度传感器：${bean.name}"
                "ORIENTATION" -> "方向传感器：${bean.name}"
                "GRAVITY" -> "重力传感器：${bean.name}"
                "LINEARACCEL",
                "LINEAR ACCELERATION" -> "线性加速传感器：${bean.name}"
                "ROTATION VECTOR" -> "旋转向量传感器：${bean.name}"
                "SIGNIFICANT_MOTION" -> "显著运动传感器：${bean.name}"
                "STR" -> "特定吸收率：${bean.name}"
                else->bean?.name
            }
        )
    }
}