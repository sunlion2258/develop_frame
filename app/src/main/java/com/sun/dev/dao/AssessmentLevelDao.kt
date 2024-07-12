package com.sun.dev.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sun.dev.entity.AssessmentBean

/**
 * Created by fengwj on 2024/7/10.
 */
@Dao
interface AssessmentLevelDao {
    @Insert
    fun insertAssessmentLevel(assessmentBean: AssessmentBean)

    @Query("delete from access where access_level_id = :accessLevelId")
    fun deleteAssessmentLevel(accessLevelId: String)

    @Update
    fun updateAssessmentLevel(assessmentBean: AssessmentBean)

    @Query("select * from access")
    fun getAllAssessmentLevel(): List<AssessmentBean>

    @Query("select * from  access where access_level_id = :accessLevelId")
    fun queryAssessmentLevel(accessLevelId: String): AssessmentBean?
}