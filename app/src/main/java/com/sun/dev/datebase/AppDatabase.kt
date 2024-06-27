package com.sun.dev.datebase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sun.dev.dao.UserDao
import com.sun.dev.entity.User

/**
 * Created by fengwj on 2024/6/27.
 */

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}