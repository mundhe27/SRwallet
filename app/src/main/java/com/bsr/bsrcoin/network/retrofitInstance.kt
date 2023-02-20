package com.bsr.bsrcoin.network

import com.bsr.bsrcoin.MysqlConst.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class retrofitInstance {

    companion object{
        val baseUrl= Constants.ROOT_URL

        fun getretrofitConnection() : Retrofit {

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}