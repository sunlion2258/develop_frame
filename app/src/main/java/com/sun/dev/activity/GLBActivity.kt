@file:Suppress("DEPRECATION")

package com.sun.dev.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import cn.qqtheme.framework.util.LogUtils
import com.google.android.filament.Engine
import com.google.android.filament.gltfio.FilamentAsset
import com.google.android.filament.utils.Mat4
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityGlbBinding
import com.sun.dev.viewmodel.TestModel
import com.sun.dev.viewrepository.TestRepository
import com.sun.dev.vmfactory.TestFactory
import com.sun.dev.widget.CustomViewer
import kotlinx.android.synthetic.main.activity_glb.surface_view
import kotlinx.android.synthetic.main.activity_glb.toolbar


/**
 * Created by SunLion on 2024/6/20.
 */
@Suppress("DEPRECATION")
class GLBActivity : BaseMVVMActivity<ActivityGlbBinding, TestModel>() {

    private var customViewer: CustomViewer = CustomViewer()
    private var asset: FilamentAsset? = null
    private var engine: Engine? = null

    override fun initContentViewID(): Int = R.layout.activity_glb

    override fun initViewModel(): TestModel =
        ViewModelProviders.of(this, TestFactory(TestRepository()))
            .get(TestModel::class.java)

    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar)
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()

        customViewer.run {
            loadEntity()
            setSurfaceView(requireNotNull(surface_view))


            loadGlb(this@GLBActivity, "grogu", "Human")
            loadIndirectLight(this@GLBActivity, "venetian_crossroads_2k")

            asset = getAsset()
            engine = getEngine()


//            setModelScale(1.6f)
//            getJoints(asset!!, engine!!)

            val entities = asset!!.entities
            val transformManager = engine!!.transformManager

            entities.forEach { entity ->
                val transformInstance = transformManager.getInstance(entity)
                if (transformInstance != 0) {
                    val name = asset!!.getName(entity)
                    LogUtils.debug("Joint name ----------", name)
                    if (name.contains("RootNode")) {
                        val transformInstance = transformManager.getInstance(entity)
                        if (transformInstance != 0) {
                            val transformArray = FloatArray(16)
                            transformManager.getTransform(transformInstance, transformArray)
                            val scaleMatrix = Mat4.of(
                                1.5f, 0.0f, 0.0f, 0.0f,
                                0f, 1.5f, 0.0f, 0.0f,
                                0f, 0.0f, 1.5f, 0.0f,
                                0f, 0.0f, 0.0f, 0.5f,
                            )

                            val currentTransform = Mat4.of(*transformArray)
                            val newTransform = scaleMatrix * currentTransform
                            transformManager.setTransform(
                                transformInstance,
                                newTransform.toFloatArray()
                            )
                        }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        customViewer.onResume()
    }

    override fun onPause() {
        super.onPause()
        customViewer.onPause()
    }


    /**
     * 缩放模型
     */
    private fun setModelScale(scale: Float) {
        val transformManager = engine!!.transformManager
        val root = asset!!.root

        val instance = transformManager.getInstance(root)
        if (transformManager.hasComponent(root)) {
            val transformArray = FloatArray(16)
            transformManager.getTransform(instance, transformArray)

            val scaleMatrix = Mat4.of(
                scale, 0.0f, 0.0f, 0.0f,
                0f, scale, 0.0f, 0.0f,
                0f, 0.0f, scale, 0.0f,
                0f, 0.0f, 0.0f, 1.0f,
            )

            val currentTransform = Mat4.of(*transformArray)
            val newTransform = scaleMatrix * currentTransform
            transformManager.setTransform(instance, newTransform.toFloatArray())
        }
    }

    /**
     * 获取关节点
     */
    private fun getJoints(asset: FilamentAsset, engine: Engine): List<Pair<String, FloatArray>> {
        val joints = mutableListOf<Pair<String, FloatArray>>()
        for (entity in asset.entities) {
            val nodeName = asset.getName(entity)
            if (nodeName != null) {
                val transformManager = engine.transformManager
                val transformInstance = transformManager.getInstance(entity)

                if (transformInstance != 0) {
                    val transform = FloatArray(16)
                    transformManager.getWorldTransform(transformInstance, transform)

                    // 获取关节的坐标 (假设使用的是第12, 13, 14索引来表示平移)
                    val position = floatArrayOf(transform[12], transform[13], transform[14])
                    joints.add(Pair(nodeName, position))
                }
            }
        }
        return joints
    }

    override fun onDestroy() {
        customViewer.modelViewer.destroyModel()
        customViewer.onDestroy()
        super.onDestroy()
    }
}

