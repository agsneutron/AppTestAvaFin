package com.avafin.apptest.core

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val username: String, private val password: String, private val data: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", getCredentials())
            .build()
        return chain.proceed(request)
    }

    private fun getCredentials(): String {
        val credentials = "$username:$password:$data"
        return "Basic " + android.util.Base64.encodeToString(credentials.toByteArray(), android.util.Base64.NO_WRAP)
    }
}
