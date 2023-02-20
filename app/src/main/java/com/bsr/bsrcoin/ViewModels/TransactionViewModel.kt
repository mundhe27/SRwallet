package com.bsr.bsrcoin.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bsr.bsrcoin.Database.TransactionDatabase
import com.bsr.bsrcoin.Database.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application):AndroidViewModel(application) {
val transactionDao=TransactionDatabase.getTransactionDatabaseInstance(application).getTransactionDao()
    val allTransactions:LiveData<List<TransactionEntity>>
    init {
        allTransactions=transactionDao.getAllTransactions()
    }
    fun insertTransaction(transactionEntity: TransactionEntity)=viewModelScope.launch(Dispatchers.IO) {
        transactionDao.insertTransaction(transactionEntity)
    }
    fun deleteTransaction(transactionEntity: TransactionEntity)=viewModelScope.launch(Dispatchers.IO) {
        transactionDao.deleteTransaction(transactionEntity)
    }
}