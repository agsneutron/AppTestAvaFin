package com.avafin.apptest.data.network

import com.avafin.apptest.data.model.FieldsModel
import com.avafin.apptest.data.model.RequestData
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FieldsApiClient {
    @POST("getRegistrationFields")
    suspend fun getAllFields(): ResponseBody //Response<FieldsModel>
}