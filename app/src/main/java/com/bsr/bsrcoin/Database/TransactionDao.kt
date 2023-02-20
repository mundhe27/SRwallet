package com.bsr.bsrcoin.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface TransactionDao {
    @Insert
    fun insertTransaction(transactionEntity: TransactionEntity)
    @Delete
    fun deleteTransaction(transactionEntity: TransactionEntity)
    @Query("Select * from Transactions")
    fun getAllTransactions():LiveData<List<TransactionEntity>>
}