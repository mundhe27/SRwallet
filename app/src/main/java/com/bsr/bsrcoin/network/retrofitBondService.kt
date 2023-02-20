package com.bsr.bsrcoin.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET

interface retrofitBondService {

    @POST("bond/create")
    fun addBond(@Body bond:Bond): Call<ResponseBody>
    @POST("bond/buybond.php")
    fun buybond(@Body bBond:BBond):Call<ResponseBody>
    @POST("bond/update/2")
    fun updateBond(@Body bond: Bond): Call<ResponseBody>
    @DELETE("bond/delete/{id}")
    fun deletebond(@Path("id")id: String):Call<String>
    @GET("bond/read")
    fun getallBond(): Call<BondList>
    @GET("bond/getbond.php")
    fun getBondbyuserid(@Query("id") id:Int): Call<BondList>

}
