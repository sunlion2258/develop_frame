@file:Suppress("DEPRECATION")

package com.sun.dev.loadphoto

import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityGlbBinding
import kotlinx.android.synthetic.main.activity_glb.toolbar


/**
 * Created by SunLion on 2024/6/20.
 */
@Suppress("DEPRECATION")
class GLBActivity : BaseMVVMActivity<ActivityGlbBinding, TestModel>() {
    private var arFragment: ArFragment? = null
    private var modelRenderable: ModelRenderable? = null

    override fun initContentViewID(): Int = R.layout.activity_glb

    override fun initViewModel(): TestModel =
        ViewModelProviders.of(this, TestFactory(TestRepository()))
            .get(TestModel::class.java)

    override fun onMVVMCreated(savedInstanceState: Bundle?) {
        ImmersionBar.setTitleBar(this, toolbar)
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .init()
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment?

        // 加载.glb模型
        ModelRenderable.builder()
            .setSource(this@GLBActivity, Uri.parse("file:///android_asset/Human.glb"))
//            .setIsFilamentGltf(true)
            .build()
            .thenAccept { renderable: ModelRenderable ->
                modelRenderable = renderable
            }
            .exceptionally { throwable: Throwable? ->
                Toast.makeText(this, "无法加载模型", Toast.LENGTH_LONG).show()
                null
            }

        // 设置点击事件来放置模型
        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->
            if (modelRenderable == null) {
                return@setOnTapArPlaneListener
            }
            // 创建锚点
            val anchor = hitResult.createAnchor()
            val anchorNode =
                AnchorNode(anchor)
            anchorNode.parent = arFragment!!.arSceneView.scene

            // 创建模型节点
            val modelNode = TransformableNode(arFragment!!.transformationSystem)
            modelNode.parent = anchorNode
            modelNode.setRenderable(modelRenderable)
            modelNode.select()
        }

    }
}