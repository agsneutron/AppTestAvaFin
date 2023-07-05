package com.avafin.apptest.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://platon.cf-it.at/affiliate")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}