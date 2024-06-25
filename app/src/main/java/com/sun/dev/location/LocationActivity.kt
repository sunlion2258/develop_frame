@file:Suppress("DEPRECATION")

package com.sun.dev.location

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProviders
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.*
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityLocationBinding
import com.sun.dev.inter.MyCancelCallback
import kotlinx.android.synthetic.main.activity_location.*


/**
 * Created by SunLion on 2019/10/31.
 * 地图
 */
class LocationActivity : BaseMVVMActivity<ActivityLocationBinding, LocationViewModel>(), LocationSource,
    AMapLocationListener, AMap.OnMapTouchListener {

    //地图
    private var mAMap: AMap? = null
    //UI设置
    private var mUiSettings: UiSettings? = null

    //声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null

    private var mLocationOption: AMapLocationClientOption? = null

    //位置改变监听
    private var mListener: LocationSource.OnLocationChangedListener? = null
    //移动到屏幕中间
    private var useMoveToLocationWithMapMode = true

    //自定义定位小蓝点的Marker
    private var locationMarker: Marker? = null

    //坐标和经纬度转换工具
    private var projection: Projection? = null

    private var latLng:LatLng?=null

    override fun initContentViewID(): Int = R.layout.activity_location


    override fun initViewModel(): LocationViewModel =
        ViewModelProviders.of(this, LocationVMFactory(LocationRepository())).get(LocationViewModel::class.java)

    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        bindViews.vm = vm
        //此方法必须重写
        mv_map.onCreate(savedInstanceState)

        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

        initMap()
        initUI()

        iv_location.setOnClickListener {
            mAMap!!.moveCamera(CameraUpdateFactory.changeLatLng(latLng))
        }
//        locationbtn.setOnClickListener{
//            if (locationbtn.isChecked) {
//                mAMap!!.clear(true)
//                if (record != null) {
//                    record = null
//                }
//                record = PathRecord()
//                mStartTime = System.currentTimeMillis()
//                record.setDate(getcueDate(mStartTime))
//                mResultShow.setText("总距离")
//            } else {
//                mEndTime = System.currentTimeMillis()
//                mOverlayList.add(mTraceoverlay)
//                val decimalFormat = DecimalFormat("0.0")
//                mResultShow.setText(
//                    decimalFormat.format(getTotalDistance() / 1000.0) + "KM"
//                )
//                var mTraceClient: LBSTraceClient? = null
//                try {
//                    mTraceClient = LBSTraceClient(applicationContext)
//                    mTraceClient.queryProcessedTrace(
//                        2,
//                        Util.parseTraceLocationList(record.getPathline()),
//                        LBSTraceClient.TYPE_AMAP,
//                        this@LocationActivity
//                    )
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//                saveRecord(record.getPathline(), record.getDate())
//            }
//        }
    }


    /**
     * 初始化地图控件
     */

    private fun initMap() {
        mAMap = mv_map.map
        // 设置定位监听
        mAMap!!.setLocationSource(this)
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap!!.isMyLocationEnabled = true
        mAMap!!.setMyLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
        //手势监听
        mAMap!!.setOnMapTouchListener(this)


    }

    /**
     * 定义UI
     */
    private fun initUI() {
        mUiSettings = mAMap!!.uiSettings
        //比例尺展示
        mUiSettings!!.isScaleControlsEnabled = true
        //缩放按钮展示状态
        mUiSettings!!.isZoomControlsEnabled = false
        //指南针展示状态
        mUiSettings!!.isCompassEnabled = true
        //定位按钮状态
        mUiSettings!!.isMyLocationButtonEnabled = true
        //不允许倾斜
        mUiSettings!!.isTiltGesturesEnabled = false
        //不允许旋转
        mUiSettings!!.isRotateGesturesEnabled = false

    }

    /**
     * 激活定位
     */
    override fun activate(listener: LocationSource.OnLocationChangedListener?) {
        mListener = listener
        if (mLocationClient == null) {
            mLocationClient = AMapLocationClient(this)
            mLocationOption = AMapLocationClientOption()
            //设置定位监听
            mLocationClient!!.setLocationListener(this)
            //设置为高精度定位模式
            mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //是指定位间隔
            mLocationOption!!.interval = 3000
            //设置定位参数
            mLocationClient!!.setLocationOption(mLocationOption)
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient!!.startLocation()
        }
    }

    /**
     * 停止定位
     */
    override fun deactivate() {
        mListener = null
        if (mLocationClient != null) {
            mLocationClient!!.stopLocation()
            mLocationClient!!.onDestroy()
        }
        mLocationClient = null
    }

    /**
     * 位置改变监听
     */
    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.errorCode == 0) {
                 latLng = LatLng(amapLocation.latitude, amapLocation.longitude)
                //展示自定义定位小蓝点
                if (locationMarker == null) {
                    //首次定位
                    locationMarker = mAMap!!.addMarker(
                        MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                            .anchor(0.5f, 0.5f)
                    )

                    //首次定位,选择移动到地图中心点并修改级别到15级
                    mAMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                } else {

                    if (useMoveToLocationWithMapMode) {
                        //二次以后定位，使用sdk中没有的模式，让地图和小蓝点一起移动到中心点（类似导航锁车时的效果）
                        startMoveLocationAndMap(latLng!!)
                    } else {
                        startChangeLocation(latLng!!)
                    }

                }


            } else {
                val errText = "定位失败," + amapLocation.errorCode + ": " + amapLocation.errorInfo
                Log.e("AmapErr", errText)
            }
        }
    }

    /**
     * 修改自定义定位小蓝点的位置
     * @param latLng
     */
    private fun startChangeLocation(latLng: LatLng) {
        if (locationMarker != null) {
            val curLatlng = locationMarker!!.position
            if (curLatlng == null || curLatlng != latLng) {
                locationMarker!!.position = latLng
            }
        }

    }

    /**
     * 同时修改自定义定位小蓝点和地图的位置
     * @param latLng
     */
    private fun startMoveLocationAndMap(latLng: LatLng) {
        //将小蓝点提取到屏幕上
        if (projection == null) {
            projection = mAMap!!.projection
        }
        if (locationMarker != null && projection != null) {
            val markerLocation = locationMarker!!.position
            val screenPosition = mAMap!!.projection.toScreenLocation(markerLocation)
            locationMarker!!.setPositionByPixels(screenPosition.x, screenPosition.y)
        }
        //地图取消或者打断的回调
         val myCancelCallback = MyCancelCallback(locationMarker)

        //移动地图，移动结束后，将小蓝点放到放到地图上
        myCancelCallback.setTargetLatlng(latLng)
        //动画移动的时间，最好不要比定位间隔长，如果定位间隔2000ms 动画移动时间最好小于2000ms，可以使用1000ms
        //如果超过了，需要在myCancelCallback中进行处理被打断的情况
        mAMap!!.animateCamera(CameraUpdateFactory.changeLatLng(latLng), 1000, myCancelCallback)

    }

    override fun onTouch(p0: MotionEvent?) {
        Log.i("amap", "onTouch 关闭地图和小蓝点一起移动的模式")
        useMoveToLocationWithMapMode = false
    }


    /**
     * 必须重写
     */
    override fun onResume() {
        super.onResume()
        mv_map.onResume()
        useMoveToLocationWithMapMode = true
    }

    /**
     * 必须重写
     */
    override fun onPause() {
        super.onPause()
        mv_map.onPause()

        deactivate()
        useMoveToLocationWithMapMode = false
    }

    /**
     * 必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mv_map.onSaveInstanceState(outState)
    }

    /**
     * 必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        mv_map.onDestroy()
        if (null != mLocationClient) {
            mLocationClient!!.onDestroy()
        }
    }

}

