package com.bsr.bsrcoin.network

import java.time.temporal.TemporalAmount

data class BondList (
    val bond : List<Bond>
)
data class BBond(
    val userId: Int,
    val quantity:Long,
    val amount: Long,
    val date:String
)
data class Bond(
    val userId:Int,
    val bond_name : String,
    val bond_amount : Int,
    val bond_type : String,
    val history: String
)
