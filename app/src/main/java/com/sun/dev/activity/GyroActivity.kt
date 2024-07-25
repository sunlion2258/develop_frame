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

    private var lastAccelerationValues = FloatArray(3)
    private var lastGyroValues = FloatArray(3)

    private val displacement = FloatArray(2) // 用于存储X和Y轴的位移值


    private var ballX = 0f
    private var ballY = 0f
    private var velocityX = 0f
    private var velocityY = 0f

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

        val gyroscopeSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

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


        ballMovement(5)
    }

    /**
     * level 小球运动速度，1-10之间
     */
    private fun ballMovement(level: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            for (index in -100..100) {
                delay(10 * level.toLong())

                if (index in 19..41) {
                    val currentIndex = Random.nextInt(20, 40)
                    velocityX = currentIndex * 0.1f
                    velocityY = currentIndex * 0.1f
                } else {
                    // 更新速度和位置
                    velocityX = index * 0.1f
                    velocityY = index * 0.1f
                }

                ballX = velocityX * 0.1f
                ballY = velocityY * 0.14f


                // 限制小球在屏幕范围内
                ballX = min(max(ballX, -1f), 1f)
                ballY = min(max(ballY, -1f), 1f)

                ballView.setBallXY(ballX,ballY)

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
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    // 更新加速度传感器的值
                    lastAccelerationValues = floatArrayOf(x, y, z)
                    calculateDisplacement()

                    // 更新速度和位置
                    velocityX += x * 0.1f
                    velocityY += y * 0.1f

                    ballX -= velocityX * 0.01f
                    ballY += velocityY * 0.01f

                    // 限制小球在屏幕范围内
                    ballX = min(max(ballX, -1f), 1f)
                    ballY = min(max(ballY, -1f), 1f)

                    ballView.invalidate()
                }

                Sensor.TYPE_GYROSCOPE -> {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    // 更新陀螺仪传感器的值
                    lastGyroValues = floatArrayOf(x, y, z)
                    calculateDisplacement()
                }
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            //精度改变时调用
        }
    }

    private fun calculateDisplacement() {
        // 假设时间间隔为1秒
        val deltaTime = 1.0f

        // 简单的位移计算公式，考虑加速度和角速度的影响
        val ax = lastAccelerationValues[0]
        val ay = lastAccelerationValues[1]

        // 计算X和Y的位移
        displacement[0] += ax * deltaTime
        displacement[1] += ay * deltaTime

        // 确保位移值在-1到1之间
        displacement[0] = displacement[0].coerceIn(-1f, 1f)
        displacement[1] = displacement[1].coerceIn(-1f, 1f)


        mXYList.add(GyroBean("X轴位置：$displacement[0]  Y轴：$ displacement[1] "))
        mXYAdapter.setNewData(mXYList)
        rv_gyro_xy.smoothScrollToPosition(mXYList.size - 1)
    }
}