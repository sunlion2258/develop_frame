package com.sun.dev.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sun.dev.R
import com.sun.dev.bean.ChangeLanguageBean

/**
 * Created by fengwj on 2024/5/10.
 * 选择国际化语言适配器
 */
class ChangeLanguageListAdapter :
    BaseQuickAdapter<ChangeLanguageBean, BaseViewHolder>(R.layout.item_choose_internationalization_language) {
    override fun convert(holder: BaseViewHolder, bean: ChangeLanguageBean?) {
      holder.setText(R.id.tv_title,bean!!.language)
          .setText(R.id.tv_content,bean.languageCode)

    }
}