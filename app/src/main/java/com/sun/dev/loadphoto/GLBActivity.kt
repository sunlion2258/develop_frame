@file:Suppress("DEPRECATION")

package com.sun.dev.loadphoto

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.sun.dev.R
import com.sun.dev.base.BaseMVVMActivity
import com.sun.dev.databinding.ActivityGlbBinding
import com.sun.dev.widget.CustomViewer
import kotlinx.android.synthetic.main.activity_glb.surface_view
import kotlinx.android.synthetic.main.activity_glb.toolbar


/**
 * Created by SunLion on 2024/6/20.
 */
@Suppress("DEPRECATION")
class GLBActivity : BaseMVVMActivity<ActivityGlbBinding, TestModel>() {

    var customViewer: CustomViewer = CustomViewer()

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

            //Enviroments and Lightning (OPTIONAL)
            loadIndirectLight(this@GLBActivity, "venetian_crossroads_2k")
            //loadEnviroment(this@MainActivity, "venetian_crossroads_2k");
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

    override fun onDestroy() {
        super.onDestroy()
        customViewer.onDestroy()
    }
}