package com.sun.dev.datebase

import android.content.Context
import android.os.Environment
import androidx.room.Room
import com.sun.dev.common.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by fengwj on 2024/6/27.
 * 数据库创建、copy到外部储存、卸载重装从外部储存恢复数据库
 */
object DatabaseProvider {
    fun getDatabase(context: Context): AppDatabase {
        // 使用外部存储路径
        val externalDbFile = File(context.getExternalFilesDir(null), Constants.DATABASE_NAME)
        val internalDbFile = context.getDatabasePath(Constants.DATABASE_NAME)

        // 检查外部存储中是否存在数据库文件，并复制到内部存储中
        if (!internalDbFile.exists() && externalDbFile.exists()) {
            try {
                copyFile(externalDbFile, internalDbFile)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        // 返回 Room 数据库实例
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            internalDbFile.absolutePath
        ).build()
    }

    @Throws(IOException::class)
    private fun copyFile(sourceFile: File, destFile: File) {
        FileInputStream(sourceFile).channel.use { source ->
            FileOutputStream(destFile).channel.use { destination ->
                source.transferTo(0, source.size(), destination)
            }
        }
    }

    /**
     * 更新外部数据库
     */
    fun updateExternalDatabase(context: Context, newDatabaseFile: File) {
        val externalDbFile = File(context.getExternalFilesDir(null), Constants.DATABASE_NAME)
        if (externalDbFile.exists()) {
            externalDbFile.delete()
        }
        newDatabaseFile.copyTo(externalDbFile)
    }
}