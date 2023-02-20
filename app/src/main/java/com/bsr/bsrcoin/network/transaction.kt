package com.bsr.bsrcoin.network
data class TransactionList (
    val transaction : List<Transaction>
)
data class Transaction (
    val from_userId:Int,
    val to_userId:Int,
    val from_walletId:Int,
    val to_walletId:Int,
    val from_account:Long,
    val to_account:Long,
    val transaction_type:String,
    val transaction_amount:Int,
    val transaction_time:String,
    val description:String
)
