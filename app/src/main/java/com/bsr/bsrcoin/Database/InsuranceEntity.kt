package com.bsr.bsrcoin.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Insurance")
data class InsuranceEntity(
    @PrimaryKey(autoGenerate = true) val insuranceId:Int=0,
    @ColumnInfo(name = "insurance_amount")val amount:Float,
    @ColumnInfo(name = "insurance_type")val insuranceType:String,
    @ColumnInfo(name = "agent_name")val agentName:String,
    @ColumnInfo(name = "duration")val duration:String
)