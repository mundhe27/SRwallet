package com.bsr.bsrcoin.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
interface retrofitShareServices {
        @POST("share/create")
        fun addShare(@Body share: Share): Call<ResponseBody>
        @POST("share/buyshare.php")
        fun buyshare(@Body bShare:BShare):Call<ResponseBody>
        @POST("share/update/{id}")
        fun updateShare(@Path("id")id: String,@Body share: Share): Call<ResponseBody>
        @DELETE("share/delete/{id}")
        fun deleteShare(@Path("id")id: String): Call<String>
        @GET("share/read")
        fun getallShare(): Call<shareList>
        @GET("share/getshare.php")
        fun getSharebyuserid(@Query("id") id:Int): Call<shareList>

}