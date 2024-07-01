@file:Suppress("DEPRECATION")

package com.sun.dev.loadphoto

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.adapter.GyroListAdapter
import com.sun.dev.adapter.GyroXYZListAdapter
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.bean.GyroBean
import com.sun.dev.databinding.ActivityGyroBinding
import kotlinx.android.synthetic.main.activity_gyro.rv_gyro_list
import kotlinx.android.synthetic.main.activity_gyro.rv_gyro_xy
import kotlinx.android.synthetic.main.activity_test.toolbar
import org.jetbrains.anko.toast

/**
 * 陀螺仪页面
 * Created by SunLion on 2024/6/25.
 */
@Suppress("DEPRECATION")
class GyroActivity : BaseMVVMActivity<ActivityGyroBinding, GyroModel>() {
    private var sensorManager: SensorManager? = null
    private var mAdapter = GyroListAdapter()
    private var mList = mutableListOf<GyroBean>()

    private var mXYAdapter = GyroXYZListAdapter()
    private var mXYList = mutableListOf<GyroBean>()

    override fun initContentViewID(): Int = R.layout.activity_gyro

    override fun initViewModel(): GyroModel =
        ViewModelProviders.of(this, GyroFactory(GyroRepository()))
            .get(GyroModel::class.java)

    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar)
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensorList = sensorManager!!.getSensorList(Sensor.TYPE_ALL)
        for (sensor in sensorList) {
            mList.add(GyroBean(sensor.name))
        }

        rv_gyro_list.layoutManager = LinearLayoutManager(this)
        rv_gyro_list.adapter = mAdapter
        mAdapter.setNewData(mList)

        rv_gyro_xy.layoutManager = LinearLayoutManager(this)
        rv_gyro_xy.adapter = mXYAdapter


        val gyroscopeSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        if (gyroscopeSensor != null) {
            val registered =  sensorManager!!.registerListener(
                sensorEventListener,
                gyroscopeSensor,
                SensorManager.SENSOR_DELAY_UI
            )
            if (!registered) {
                Log.e("MainActivity", "Failed to register sensor listener")
            }

        } else {

            toast("没有陀螺仪传感器")
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(sensorEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.let {
            it?.unregisterListener(sensorEventListener)
        }
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
            mXYList.add(GyroBean("X轴坐标：$x  Y轴：$y   Z轴： $z"))
            mXYAdapter.setNewData(mXYList)
            rv_gyro_xy.smoothScrollToPosition(mXYList.size - 1)
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            //精度改变时调用
        }
    }
}