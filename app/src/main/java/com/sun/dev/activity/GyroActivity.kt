@file:Suppress("DEPRECATION")

package com.sun.dev.activity

import android.graphics.Color
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
import com.sun.dev.viewmodel.GyroModel
import com.sun.dev.viewrepository.GyroRepository
import com.sun.dev.vmfactory.GyroFactory
import kotlinx.android.synthetic.main.activity_gyro.ballView
import kotlinx.android.synthetic.main.activity_gyro.rv_gyro_list
import kotlinx.android.synthetic.main.activity_gyro.rv_gyro_xy
import kotlinx.android.synthetic.main.activity_gyro.toolbar_gyro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

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

    private var ballX = 0.00
    private var ballY = 0.00
    private var velocityX = 0.00
    private var velocityY = 0.00

    override fun initContentViewID(): Int = R.layout.activity_gyro

    override fun initViewModel(): GyroModel =
        ViewModelProviders.of(this, GyroFactory(GyroRepository()))
            .get(GyroModel::class.java)

    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar_gyro)
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

        //陀螺仪
//        val gyroscopeSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        //加速度
        val gyroscopeSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (gyroscopeSensor != null) {
            val registered = sensorManager!!.registerListener(
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

    /**
     * level 小球运动速度，1-10之间
     */
    private fun ballMovement(level: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            for (index in -100..100) {
                delay(10 * level.toLong())

                // 更新速度和位置
                velocityX = index * 0.10
                velocityY = index * 0.10

                ballX = velocityX * 0.10
                ballY = velocityY * 0.14


                // 限制小球在屏幕范围内
                ballX = min(max(ballX, -1.00), 1.00)
                ballY = min(max(ballY, -1.00), 1.00)

                ballView.setBallXY(ballX.toFloat(), ballY.toFloat())

                val nextIntColor = Random.nextInt(1, 10)
                when (nextIntColor) {
                    1 -> {
                        ballView.paint.color = Color.BLACK
                    }

                    2 -> {
                        ballView.paint.color = Color.BLUE
                    }

                    3 -> {
                        ballView.paint.color = Color.CYAN
                    }

                    4 -> {
                        ballView.paint.color = Color.DKGRAY
                    }

                    5 -> {
                        ballView.paint.color = Color.GRAY
                    }

                    6 -> {
                        ballView.paint.color = Color.GREEN
                    }

                    7 -> {
                        ballView.paint.color = Color.LTGRAY
                    }

                    8 -> {
                        ballView.paint.color = Color.MAGENTA
                    }

                    9 -> {
                        ballView.paint.color = Color.RED
                    }

                    10 -> {
                        ballView.paint.color = Color.YELLOW
                    }

                    else -> {
                        ballView.paint.color = Color.GREEN
                    }
                }
                ballView.invalidate()
            }
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
            when (event!!.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val y = event.values[0]
                    val x = event.values[1]

                    velocityX = x * 0.10
                    velocityY = y * 0.10

                    ballX = velocityX * 1f
                    ballY = velocityY * 1f

                    // 限制小球在屏幕范围内
                    ballX = min(max(ballX, -1.00), 1.00)
                    ballY = min(max(ballY, -1.00), 1.00)

                    ballView.setBallXY(ballX.toFloat(), -ballY.toFloat())
                    ballView.invalidate()

                    mXYList.add(GyroBean("X轴：${x     }     Y轴：${y}"))
                    mXYAdapter.setNewData(mXYList)
                    rv_gyro_xy.smoothScrollToPosition(mXYList.size - 1)
                }

                Sensor.TYPE_GYROSCOPE -> {
                    val x = event.values[0]
                    val y = event.values[1]

                }
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            //精度改变时调用
        }
    }
}