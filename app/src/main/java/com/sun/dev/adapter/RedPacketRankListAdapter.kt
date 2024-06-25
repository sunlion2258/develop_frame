package com.sun.dev.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sun.dev.R
import com.sun.dev.bean.RedPacketRankBean

/**
 * Created by fengwj on 2023/12/1.
 */
class RedPacketRankListAdapter :
    BaseQuickAdapter<RedPacketRankBean, BaseViewHolder>(R.layout.item_red_packet_rank) {
    override fun convert(holder: BaseViewHolder, bean: RedPacketRankBean?) {
        val ivHeadImage = holder.getView<ImageView>(R.id.iv_head_image)
//        Glide.with(mContext).load(bean!!.avatar)
//            .into(ivHeadImage)

        holder.setText(R.id.tv_title, bean!!.nickName)
            .setText(R.id.tv_content, "${bean.gold}")
    }
}