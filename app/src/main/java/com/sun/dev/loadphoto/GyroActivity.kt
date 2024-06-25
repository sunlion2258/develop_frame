@file:Suppress("DEPRECATION")

package com.sun.dev.loadphoto

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import cn.qqtheme.framework.util.LogUtils
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityTestBinding
import kotlinx.android.synthetic.main.activity_test.toolbar

/**
 * 陀螺仪页面
 * Created by SunLion on 2024/6/25.
 */
@Suppress("DEPRECATION")
class GyroActivity : BaseMVVMActivity<ActivityTestBinding, TestModel>() {
    private var sensorManager: SensorManager? = null

    override fun initContentViewID(): Int = R.layout.activity_test

    override fun initViewModel(): TestModel =
        ViewModelProviders.of(this, TestFactory(TestRepository()))
            .get(TestModel::class.java)

    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar)
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()
        /**
         * 陀螺仪相关逻辑
         */
        initGyroscope()
    }

    private fun initGyroscope() {
        //陀螺仪管理器
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val gyroscopeSensor: Sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager?.registerListener(
            sensorEventListener,
            gyroscopeSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private var sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            //实时获取陀螺仪坐标

            //正值表示设备向右旋转，负值表示向左旋转
            val x = event!!.values[0]
            //正值表示设备向上旋转，负值表示向下旋转。
            val y = event.values[1]
            //正值表示顺时针旋转，负值表示逆时针旋转。
            val z = event.values[2]
            LogUtils.debug("X 坐标 $x")
            LogUtils.debug("Y 坐标 $y")
            LogUtils.debug("Z 坐标 $z")
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            //精度改变时调用
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.let {
            it?.unregisterListener(sensorEventListener)
        }
    }
}