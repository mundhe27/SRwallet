package com.bsr.bsrcoin.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bsr.bsrcoin.Database.InsuranceDatabase
import com.bsr.bsrcoin.Database.InsuranceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsuranceViewModel(application: Application):AndroidViewModel(application) {
    val insuranceDao=InsuranceDatabase.getDatabase(application).getInsuranceDao()
    val allInsurance:LiveData<List<InsuranceEntity>>
    init {
        allInsurance=insuranceDao.getAllInsurance()
    }
    fun insertInsurance(insuranceEntity: InsuranceEntity)=viewModelScope.launch(Dispatchers.IO) {
        insuranceDao.insertInsurance(insuranceEntity)
    }
    fun deleteInsurance(insuranceEntity: InsuranceEntity)=viewModelScope.launch(Dispatchers.IO) {
        insuranceDao.deleteInsurance(insuranceEntity)
    }
    fun deleteAllInsurance()=viewModelScope.launch(Dispatchers.IO) {
        insuranceDao.deleteAllInsurance()
    }

}