package com.bsr.bsrcoin.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [InsuranceEntity::class], version = 2)
abstract class InsuranceDatabase :RoomDatabase(){
    abstract fun getInsuranceDao():InsuranceDao
    companion object{
        var db_instance:InsuranceDatabase?=null
        private val migration:Migration=object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("Alter Table Insurance add column duration TEXT not null default '1 years'")
            }
        }
        fun getDatabase(context: Context):InsuranceDatabase
        {
            return db_instance?: synchronized(this)
            {
                val instance= Room.databaseBuilder(context.applicationContext,InsuranceDatabase::class.java,"Insurance_Database").addMigrations(
                    migration).build()
                db_instance=instance
                instance
            }
        }
    }
}