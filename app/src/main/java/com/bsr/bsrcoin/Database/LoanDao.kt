package com.bsr.bsrcoin.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoanDao {
    @Insert
    fun insertLoan(loanEntity: LoanEntity)
    @Delete
    fun deleteLoan(loanEntity: LoanEntity)
    @Query("Select * from Loans")
    fun getAllLoans():LiveData<List<LoanEntity>>
    @Query("Delete from Loans")
    fun deleteAllLoans()
}