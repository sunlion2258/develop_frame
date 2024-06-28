@file:Suppress("DEPRECATION")

package com.sun.dev.fragment.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smartrefresh.header.DeliveryHeader
import com.scwang.smartrefresh.header.DropBoxHeader
import com.scwang.smartrefresh.header.FunGameBattleCityHeader
import com.scwang.smartrefresh.header.FunGameHitBlockHeader
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.header.PhoenixHeader
import com.scwang.smartrefresh.header.StoreHouseHeader
import com.scwang.smartrefresh.header.TaurusHeader
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.scwang.smartrefresh.layout.header.TwoLevelHeader
import com.sun.dev.R
import com.sun.dev.activity.CreateSessionActivity
import com.sun.dev.adapter.RedPacketRankListAdapter
import com.sun.dev.base.BaseMVVMFragment
import com.sun.dev.bean.RedPacketRankBean
import com.sun.dev.databinding.FragmentMainHomeBinding
import com.sun.dev.loadphoto.TestActivity
import com.sun.dev.login.LoginActivity
import com.sun.dev.util.CodeUtil
import com.sun.dev.util.GlideImageLoader
import kotlinx.android.synthetic.main.fragment_main_home.home_banner
import kotlinx.android.synthetic.main.fragment_main_home.home_scroll_text
import kotlinx.android.synthetic.main.fragment_main_home.home_toolbar
import kotlinx.android.synthetic.main.fragment_main_home.lottie_2
import kotlinx.android.synthetic.main.include_toolbar.view.toolbar_left
import kotlinx.android.synthetic.main.include_toolbar.view.toolbar_right
import org.jetbrains.anko.support.v4.startActivity

/**
 *  Created by SunLion on 2019/4/29 17:54
 */
@Suppress("DEPRECATION")
class HomeFragment : BaseMVVMFragment<FragmentMainHomeBinding, HomeViewModel>(),
    View.OnClickListener {

    //红包排行榜适配器
    private var mRedRankListAdapter = RedPacketRankListAdapter()

    //红包排行榜集合
    private var mRedRankList = mutableListOf<RedPacketRankBean>()

    override fun initContentViewID(): Int = R.layout.fragment_main_home

    override fun initViewModel(): HomeViewModel =
        ViewModelProviders.of(this, HomeVMFactory(HomeRepository())).get(HomeViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ImmersionBar.setTitleBar(activity, home_toolbar)
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .navigationBarColor(android.R.color.black)
            .navigationBarDarkIcon(true)
            .init()

        bindView.vm = viewModel
        // 测试滚动
        home_scroll_text.setDataSource(
            mutableListOf(
                "qq_36813531 刚刚关注了博主",
                "老ln 打赏了88元，送我上青云",
                "WeChat_38113686 打赏了博主",
            )
        )
        home_scroll_text.startPlay()
        // 测试banner
        initBanner()

        home_toolbar.setTitle(requireActivity().resources.getString(R.string.main_bottom_home))
        home_toolbar.setRightImage(R.mipmap.add_focus)
        home_toolbar.toolbar_left.setOnClickListener(this)
        home_toolbar.toolbar_right.setOnClickListener(this)
        lottie_2.setOnClickListener(this)

        for (index in 0..1) {
            val redPacketRankBean = RedPacketRankBean(
                "${resources.getString(R.string.test_name)}$index",
                "${resources.getString(R.string.test_name)}$index"
            )
            mRedRankList.add(redPacketRankBean)
        }

        bindView.recyclerView.layoutManager = LinearLayoutManager(activity)
        bindView.recyclerView.adapter = mRedRankListAdapter
        mRedRankListAdapter.setNewData(mRedRankList)
        mRedRankListAdapter.setOnItemClickListener { baseQuickAdapter, view, i ->

        }

        bindView.refreshLayout.setRefreshHeader(ClassicsHeader(requireActivity()))
        //淘宝二楼
//        bindView.refreshLayout.setRefreshHeader(TwoLevelHeader(requireActivity()))
        bindView.refreshLayout.setOnRefreshListener {
            bindView.refreshLayout.finishRefresh(1000)
        }

        bindView.refreshLayout.setOnLoadMoreListener {
            bindView.refreshLayout.finishLoadMore(1000)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toolbar_right -> {
                if (!CodeUtil.checkIsLogin()) {
                    startActivity<LoginActivity>()
                } else {
                    CreateSessionActivity.start(requireActivity(), "创建会话")
                }
            }

            R.id.lottie_2 -> {
                startActivity<TestActivity>()
            }
        }
    }


    /**
     * 初始化banner
     */
    private fun initBanner() {
        home_banner.setImageLoader(GlideImageLoader())
        home_banner.setImages(mutableListOf(R.mipmap.banner1, R.mipmap.banner1))
        home_banner.setDelayTime(2500)
        home_banner.start()
    }
}