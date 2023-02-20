package com.bsr.bsrcoin.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [LoanEntity::class], version = 2)
abstract class LoanDatabase:RoomDatabase() {
abstract fun getLoanDao():LoanDao
companion object{
    var db_instance:LoanDatabase?=null
    private val migration:Migration= object : Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("Alter table Loans add column rate_of_interest real not null default 25 ")
            database.execSQL("Alter table Loans add Column duration text not null default 'Upto 2 years'")
        }

    }
    fun getDatabase(context: Context):LoanDatabase
    {
        return db_instance?: synchronized(this)
        {
            val instance= Room.databaseBuilder(context.applicationContext,LoanDatabase::class.java,"Loan_database").addMigrations(
                migration).build()
            db_instance=instance
            instance
        }
    }
}

}