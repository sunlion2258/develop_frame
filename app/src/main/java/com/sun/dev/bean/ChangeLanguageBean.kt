package com.sun.dev.bean

import java.io.Serializable


/**
 * Created by 223 on 2024/5/10.
 * 选择语言列表数据model
 */
class ChangeLanguageBean(
    val language: String,
    val languageCode: String,
) : Serializable