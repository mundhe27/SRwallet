package com.bsr.bsrcoin.network

data class shareList (
    val share : List<Share>
)
data class BShare(
    val userId: Int,
    val amount: Long,
    val quantity:Long,
    val date:String
)
data class Share (
    val userId : Int,
    val share_name : String,
    val share_amount : Int,
    val share_type : String,
    val history: String
)
