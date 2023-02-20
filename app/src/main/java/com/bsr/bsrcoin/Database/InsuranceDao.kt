package com.bsr.bsrcoin.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InsuranceDao {

    @Insert
    fun insertInsurance(insuranceEntity: InsuranceEntity)
    @Delete
    fun deleteInsurance(insuranceEntity: InsuranceEntity)
    @Query("Select * from Insurance")
    fun getAllInsurance():LiveData<List<InsuranceEntity>>
    @Query("Delete from Insurance")
    fun deleteAllInsurance()
}