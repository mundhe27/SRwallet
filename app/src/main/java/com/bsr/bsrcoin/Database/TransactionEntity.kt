package com.bsr.bsrcoin.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Transactions")
data class TransactionEntity(


    @PrimaryKey(autoGenerate = true)val transactionId:Int,
    @ColumnInfo(name = "transaction_amount") val amount:Double,
    @ColumnInfo(name = "transaction_time") val time:String,
    @ColumnInfo(name ="from_account" ) val fromAccount:Long,
    @ColumnInfo(name ="to_account" ) val toAccount:Long,

)