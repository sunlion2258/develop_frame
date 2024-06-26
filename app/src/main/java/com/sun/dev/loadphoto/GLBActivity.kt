@file:Suppress("DEPRECATION")

package com.sun.dev.loadphoto

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.SkeletonNode
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
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
            .build()
            .thenAccept { renderable: ModelRenderable ->
                modelRenderable = renderable
            }
            .exceptionally { throwable: Throwable? ->
                Toast.makeText(this, "无法加载模型", Toast.LENGTH_LONG).show()
                null
            }

        modelRenderable.let {
            val jointNode: Node?= SkeletonNode().findByName("Human_LeftArm")
            if (jointNode != null) {
                val newRotation: Quaternion = Quaternion.axisAngle(Vector3(0f, 1f, 0f), 45f)
                animateJoint(jointNode, newRotation);
            }
        }
    }

    private fun animateJoint(node: Node, rotation: Quaternion) {
        node.localRotation = rotation
    }
}