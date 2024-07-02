package com.sun.dev.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by fengwj on 2024/7/1.
 */
@Entity(tableName = "drillRecord")
data class DrillRecordBean(
    @PrimaryKey
    @ColumnInfo(name = "drillRecordId")
    var drillRecordId: String,

    @ColumnInfo(name = "drillScore")
    var score:String

) {
    override fun toString(): String {
        return "DrillRecordBean(drillRecordId='$drillRecordId', score='$score')"
    }
}