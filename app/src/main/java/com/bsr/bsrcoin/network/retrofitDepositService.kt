package com.bsr.bsrcoin.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface retrofitDepositService {
    @POST("deposit/create")
    fun addDeposit(@Body deposit: Deposit): Call<ResponseBody>
    @PATCH("deposit/update")
    fun updateDeposit(@Body deposit: Deposit): Call<ResponseBody>
    @DELETE("deposit/delete/{id}")
    fun deleteDeposit(@Path("id") id: Int)
    @GET("deposit/read")
    fun getallDeposit(): Call<DepositList>
    @GET("deposit/getdeposit.php")
    fun getDepositbyuserid(@Query("id") id:Int): Call<DepositList>
}
