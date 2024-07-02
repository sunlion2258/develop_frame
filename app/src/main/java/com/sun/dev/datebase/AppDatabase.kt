package com.sun.dev.datebase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sun.dev.dao.DrillRecordDao
import com.sun.dev.dao.UserDao
import com.sun.dev.entity.DrillRecordBean
import com.sun.dev.entity.User

/**
 * Created by fengwj on 2024/6/27.
 */

@Database(entities = [User::class,DrillRecordBean::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun drillRecordDao():DrillRecordDao
}