package com.sun.dev.inter

import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker

/**
 * Created by SunLion on 2019/11/25.
 *
 * 地图 监控地图动画移动情况，如果结束或者被打断，都需要执行响应的操作
 */
class MyCancelCallback(private var locationMarker: Marker?) : AMap.CancelableCallback {
    private var targetLatlng: LatLng? = null
    fun setTargetLatlng(latlng: LatLng) {
        this.targetLatlng = latlng
    }

    override fun onFinish() {
        if (locationMarker != null && targetLatlng != null) {
            locationMarker!!.position = targetLatlng
        }
    }

    override fun onCancel() {
        if (locationMarker != null && targetLatlng != null) {
            locationMarker!!.position = targetLatlng
        }
    }
}