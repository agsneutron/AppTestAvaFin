package com.avafin.apptest.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    // getRetrofit regresa un objeto retrofit
    fun getRetrofit(): Retrofit {
        // retornamos la configuración de retrofit con la url base del endpoint
        return Retrofit.Builder()
            .baseUrl("https://platon.cf-it.at/affiliate")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}