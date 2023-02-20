package com.bsr.bsrcoin.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bsr.bsrcoin.Database.LoanDatabase
import com.bsr.bsrcoin.Database.LoanEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoansViewModel(application: Application):AndroidViewModel(application) {
    val loansDao=LoanDatabase.getDatabase(application).getLoanDao()
    val allLoans:LiveData<List<LoanEntity>>
    init {
        allLoans=loansDao.getAllLoans()
    }
    fun insertLoan(loanEntity: LoanEntity)=viewModelScope.launch(Dispatchers.IO){
        loansDao.insertLoan(loanEntity)
    }
    fun deleteLoan(loanEntity: LoanEntity)=viewModelScope.launch(Dispatchers.IO){
        loansDao.deleteLoan(loanEntity)
    }
    fun deleteAllLoans()=viewModelScope.launch(Dispatchers.IO){
        loansDao.deleteAllLoans()
    }
}