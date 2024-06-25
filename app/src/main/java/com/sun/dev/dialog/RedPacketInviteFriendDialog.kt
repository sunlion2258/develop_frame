package com.sun.dev.dialog

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.sun.dev.R
import com.sun.dev.event.RedPacketDeleteIdEvent
import com.sun.dev.event.RedPacketInviteFriendEvent
import com.sun.dev.util.QRCodeUtil
import org.greenrobot.eventbus.EventBus


/**
 * Created by fengwj on 2023/12/1.
 * 邀请吧友弹窗
 */
class RedPacketInviteFriendDialog(
    context: Context,
    var shareUrl: String?,
    var packageId:String
) : DialogFragment() {
    lateinit var tvContent: TextView
    lateinit var tvCancel: TextView
    lateinit var tvGoShare: TextView
    lateinit var ivImage: ImageView
    lateinit var ivDetele: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view: View =
            inflater.inflate(R.layout.dialog_red_package_invite_friends, container, false)
        tvCancel = view.findViewById(R.id.tv_cancel)
        tvGoShare = view.findViewById(R.id.tv_go_share)
        ivDetele = view.findViewById(R.id.iv_close)
        ivImage = view.findViewById(R.id.act_invite_qr_code_img)
        initView()
        return view
    }

    private fun initView() {
        if (!TextUtils.isEmpty(shareUrl)) {
            val createQRImage = QRCodeUtil.createQRImage(
                shareUrl, 800, 800,
                BitmapFactory.decodeResource(resources, R.mipmap.icon_app), ""
            )
            ivImage.setImageBitmap(createQRImage)
        }
        ivDetele.setOnClickListener {
            EventBus.getDefault().post(RedPacketDeleteIdEvent(packageId))
            dismiss() }
        tvCancel.setOnClickListener { dismiss() }
        tvGoShare.setOnClickListener {
            EventBus.getDefault().post(RedPacketInviteFriendEvent(shareUrl!!))
            dismiss()
        }
    }
}