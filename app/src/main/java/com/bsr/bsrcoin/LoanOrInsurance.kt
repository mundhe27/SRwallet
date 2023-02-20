package com.bsr.bsrcoin



data class LoanOrInsurance(
    val Id:Int,
    val amount:Float,
    val Type:String,
    val agentName:String,
    val rateInterest:Float?,
    val duration: String,
    val from:String,
)