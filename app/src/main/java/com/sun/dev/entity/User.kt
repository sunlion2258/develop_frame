package com.sun.dev.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by fengwj on 2024/6/27.
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "phoneNum")
    var phoneNum: String,

    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "sex")
    var sex:Int,
     @ColumnInfo(name = "age")
    var age:Int
) {
    override fun toString(): String {
        return "User(phoneNum='$phoneNum', name='$name', sex=$sex, age=$age)"
    }
}
