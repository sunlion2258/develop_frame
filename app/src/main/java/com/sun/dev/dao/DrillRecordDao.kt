package com.sun.dev.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sun.dev.entity.DrillRecordBean

/**
 * Created by fengwj on 2024/7/1.
 */
@Dao
interface DrillRecordDao {
    @Insert
    fun insertDrillRecord(drillRecordBean: DrillRecordBean)

    @Query("delete from drillRecord where drillRecordId = :drillRecordId")
    fun deleteDrillRecord(drillRecordId: String)

    @Update
    fun updateDrillRecord(drillRecordBean: DrillRecordBean)

    @Query("select * from drillRecord")
    fun getAllDrillRecord(): List<DrillRecordBean>

    @Query("select * from  drillRecord where drillRecordId = :drillRecordId")
    fun queryDrillRecord(drillRecordId: String): DrillRecordBean?
}