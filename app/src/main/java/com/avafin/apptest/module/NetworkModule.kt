package com.avafin.apptest.module

import com.avafin.apptest.core.AuthInterceptor
import com.avafin.apptest.data.model.RequestData
import com.avafin.apptest.data.network.FieldsApiClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import javax.inject.Singleton

@Module  // le decimos a dagger hilt que esto es un módulo, ya que será el que proveerá dependencias
// definimos el alcance con InstallIn, es decir cuando va a morir la instancia, en este caso será a nivel de aplicachón
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton // con Singleton, se usa el patron de diseño para mantener solo una instancia
    @Provides // para proveer retrofit
    fun provideRetrofit(): Retrofit {
        val jsonParser = JsonParser()
        // definimos las credenciales
        val login = "testaffiliateexternal"
        val password = "testaffiliateexternal"
        val isNewRegistration = true

        val jsonObject = JSONObject()
        jsonObject.put("login", login)
        jsonObject.put("password", password)

        val dataObject = JsonObject()
        dataObject.addProperty("new-registration", true)

        jsonObject.put("data", dataObject)
        // jsonData tiene la estructura del json de las credenciales para poder consultar el endpoint
        val jsonData = jsonParser.parse(jsonObject.toString()).asJsonObject

        // generamos el Http Client para pasarle el json de las credenciasles
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBody = Gson().toJson(jsonData).toRequestBody("application/json".toMediaTypeOrNull())
                val modifiedRequest = originalRequest.newBuilder()
                    .post(requestBody)
                    .build()
                chain.proceed(modifiedRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://platon.cf-it.at/affiliate/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton // con Singleton, se usa el patron de diseño para mantener solo una instancia
    @Provides // para proveer retrofit
    fun provideFieldsApiClient(retrofit: Retrofit): FieldsApiClient {
        return retrofit.create(FieldsApiClient::class.java)
    }
}