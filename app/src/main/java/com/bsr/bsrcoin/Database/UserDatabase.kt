package com.bsr.bsrcoin.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class],version = 1)
abstract class UserDatabase:RoomDatabase() {
    abstract fun getUserDao():UserDao

    companion object{
        var db_instance:UserDatabase?=null
        fun getDbInstance(context: Context):UserDatabase
        {
            return db_instance?: synchronized(this){
                val instance=
                    Room.databaseBuilder(context.applicationContext,UserDatabase::class.java,"Users_Database").build()
                db_instance=instance
                instance
            }

        }

    }
}