package com.sun.dev.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sun.dev.entity.User

/**
 * Created by fengwj on 2024/6/27.
 */
@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

//    @Delete
    @Query("delete from user where phoneNum = :phoneNum")
    fun deleteUser(phoneNum: String)

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUser(): List<User>

    @Query("select * from user where phoneNum = :phoneNum")
    fun queryUser(phoneNum: String): User?
}