package com.bsr.bsrcoin.network

import com.bsr.bsrcoin.MysqlConst.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBondAndDepositInstance {
    companion object {
        val baseUrl = Constants.ROOT_URL

        fun getretrofitBondAndDepositConnection(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}