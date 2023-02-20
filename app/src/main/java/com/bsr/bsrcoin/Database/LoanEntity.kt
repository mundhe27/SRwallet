package com.bsr.bsrcoin.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Loans")
data class LoanEntity(
    @PrimaryKey(autoGenerate = true)val LoanId:Int=0,
    @ColumnInfo(name = "loan_amount")val amount:Float,
    @ColumnInfo(name = "loan_type")val loanType:String,
    @ColumnInfo(name = "agent_name")val agentName:String,
    @ColumnInfo(name = "rate_of_interest")val rateInterest:Float,
    @ColumnInfo(name = "duration")val duration: String,
)