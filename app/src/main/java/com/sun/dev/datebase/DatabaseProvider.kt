package com.sun.dev.datebase

import android.content.Context
import android.os.Environment
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

    fun getDatabase(context: Context): AppDatabase {
        //指定文件目录存储
        val externalDbFile = File(
            Environment.getExternalStorageDirectory().path + "/XY-SPHXT",
            Constants.DATABASE_NAME
        )
        externalDbFile.setReadOnly()
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
            externalDbFile.absolutePath
        )
            //测试数据库更新升级
//            .addMigrations(MIGRATION_1_2)
            .build()

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

    /**
     * 数据库版本 1 升级到 版本 2 的迁移类实例对象
     */
    private val MIGRATION_1_2: Migration = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
//            Log.i("Room_StudentDatabase", "数据库版本 1 升级到 版本 2")
//            database.execSQL("alter table user add column sex integer not null default 2")
            // 创建新表的SQL语句
            val CREATE_NEW_TABLE_SQL =
                "CREATE TABLE IF NOT EXISTS drillRecord (drillRecordId TEXT PRIMARY KEY NOT NULL,drillScore TEXT NOT NULL)"

            // 执行SQL语句创建新表
            database.execSQL(CREATE_NEW_TABLE_SQL)
        }
    }
}