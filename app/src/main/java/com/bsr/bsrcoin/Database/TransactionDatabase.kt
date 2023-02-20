package com.bsr.bsrcoin.Database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [TransactionEntity::class],version = 1)
abstract class TransactionDatabase:RoomDatabase(){
    abstract fun getTransactionDao():TransactionDao
    companion object{
        var db_instance:TransactionDatabase?=null
        fun getTransactionDatabaseInstance(context: Context):TransactionDatabase
        {
            return db_instance?: synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,TransactionDatabase::class.java,"Transaction_Database").build()
                db_instance=instance
                instance
            }
        }
    }
}