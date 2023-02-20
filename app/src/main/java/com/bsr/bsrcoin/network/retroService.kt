package com.bsr.bsrcoin.network

import com.bsr.bsrcoin.Database.WalletModelData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface retroService {
    @POST("insurance/create")
    fun addInsurance(@Body insurance: Insurance):Call<ResponseBody>
    @PATCH("insurance/2")
    fun updateInsurance(@Body insurance: Insurance):Call<ResponseBody>
    @DELETE("insurance/1")
    fun deleteInsurance()
    @GET("insurances")
    fun getInsurance():Call<List<Insurance>>
    @GET("wallet/read")
    //wallet methods added
    fun getWallets(@Query("id")id:String = "")
    :Call<WalletModelData>
    @GET("wallet/user_wallets.php")
    fun getUserWallets(@Query("userId") userId:String)
    :Call<WalletModelData>
    @GET("wallet/read")
    fun getWallets():Call<WalletModelData>
    @POST("wallet/create")
    fun createWallet():Call<WalletModelData>
    @GET("wallet/update")
    fun updateWallet():Call<WalletModelData>
    @GET("wallet/delete")
    fun deleteWallet():Call<WalletModelData>




}