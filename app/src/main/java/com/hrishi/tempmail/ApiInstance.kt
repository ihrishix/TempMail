package com.hrishi.tempmail

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInstance {

    val api : TempMailApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.1secmail.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TempMailApi::class.java)
    }
}