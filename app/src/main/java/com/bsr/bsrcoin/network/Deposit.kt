package com.bsr.bsrcoin.network

data class DepositList (
   val deposit : List<Deposit>
)

data class Deposit(
   val userId : Int,
   val deposit_amount : Int,
   val deposit_type : String,
   val rate_of_Interest : Int,
   val duration : String,
   val expiry : String?,
   val depositId : Int,
   val renewedOn: String
)
