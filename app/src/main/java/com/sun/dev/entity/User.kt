package com.sun.dev.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by fengwj on 2024/6/27.
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: Int,
    var phoneNum: String
)
