package com.bsr.bsrcoin.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface retrofitTransactionServices {
    @POST("transaction/create")
    fun addtransaction(@Body transaction:Transaction): Call<ResponseBody>
    @POST("/transaction/update/{id}")
    fun updatransaction(@Path("id")id: String,@Body transaction:Transaction ): Call<ResponseBody>
    @DELETE("transaction/delete/{id}")
    fun deletetransaction(@Path("id")id: String): Call<String>
    @GET("transaction/read")
    fun getalltransaction(): Call<TransactionList>
    @GET("transaction/gettransaction.php")
    fun gettransactionbyuserid(@Query("id") id: String?): Call<TransactionList>
}