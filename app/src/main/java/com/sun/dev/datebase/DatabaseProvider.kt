package com.sun.dev.datebase

import android.content.Context
import android.os.Environment
import androidx.room.Room
import com.sun.dev.common.Constants
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by fengwj on 2024/6/27.
 * 数据库创建、copy到外部储存、卸载重装从外部储存恢复数据库
 */
object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                Constants.DATABASE_NAME
            ).build()
            INSTANCE = instance
            instance
        }
    }


    /**
     * copy数据库到外部储存
     */
    fun backupDatabase(context: Context) {
        val dbPath = context.getDatabasePath(Constants.DATABASE_NAME).absolutePath
        val backupPath =
            Environment.getExternalStorageDirectory().absolutePath + "/backup_${Constants.DATABASE_NAME}"

        try {
            val src = File(dbPath)
            val dst = File(backupPath)
            if (src.exists()) {
                val srcChannel = FileInputStream(src).channel
                val dstChannel = FileOutputStream(dst).channel
                dstChannel.transferFrom(srcChannel, 0, srcChannel.size())
                srcChannel.close()
                dstChannel.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }


        /**
         * 卸载重装app时恢复数据库
         */
        fun restoreDatabase(context: Context) {
            val dbPath = context.getDatabasePath(Constants.DATABASE_NAME).absolutePath
            val backupPath =
                Environment.getExternalStorageDirectory().absolutePath + "/backup_${Constants.DATABASE_NAME}"

            try {
                val src = File(backupPath)
                val dst = File(dbPath)
                if (src.exists()) {
                    val srcChannel = FileInputStream(src).channel
                    val dstChannel = FileOutputStream(dst).channel
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size())
                    srcChannel.close()
                    dstChannel.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}