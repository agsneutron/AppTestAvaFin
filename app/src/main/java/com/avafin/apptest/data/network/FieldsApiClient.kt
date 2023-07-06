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
    /* generamos una función suspendida, ya que vamos a usar corrutinas
    * la llamda será con el método POST y le decimos que a la url base de nuestro objeto retrofit, le agregue getRegistrationFields
    * para completar la url del endpoint*/
    @POST("getRegistrationFields")
    suspend fun getAllFields(): Response<FieldsModel>  //eel response es el tipo FieldsModel que ya definimos en data/model
}