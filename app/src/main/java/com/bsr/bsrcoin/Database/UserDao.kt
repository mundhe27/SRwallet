package com.bsr.bsrcoin.Database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserDao {

    @Insert
    fun InsertData(userEntity: UserEntity)

    @Delete
    fun deleteUser(userEntity: UserEntity)

    @Query("Select * from Users")
    fun getAllUsers():LiveData<List<UserEntity>>

    @Query("SELECT * FROM Users where email= :mail and password= :password and agent= :isagent Limit 1")
    fun getUser(mail: String?, password: String?, isagent: Boolean?): UserEntity

}