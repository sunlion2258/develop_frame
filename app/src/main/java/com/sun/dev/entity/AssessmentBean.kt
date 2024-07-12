package com.sun.dev.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by fengwj on 2024/7/10.
 */
@Entity(tableName = "access")
data class AssessmentBean(
    @PrimaryKey
    @ColumnInfo(name = "access_level_id")
    var accessLevelId: String,

    @ColumnInfo(name = "static_access_level")
    var staticAccessLevel: String,

    @ColumnInfo(name = "sleep_access_level")
    var sleepAccessLevel: String

) {
    override fun toString(): String {
        return "AssessmentBean(accessLevelId=$accessLevelId, staticAccessLevel=$staticAccessLevel, sleepAccessLevel=$sleepAccessLevel)"
    }
}