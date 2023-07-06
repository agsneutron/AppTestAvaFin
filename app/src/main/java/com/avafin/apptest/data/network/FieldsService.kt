package com.avafin.apptest.data.network

import com.avafin.apptest.data.model.Data2
import com.avafin.apptest.data.model.FieldsModel
import com.avafin.apptest.data.model.RequestData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Inject

class FieldsService @Inject constructor(private val api:FieldsApiClient) {

    suspend fun getFields(): FieldsModel {
        return withContext(Dispatchers.IO) {
            val response = api.getAllFields()
            response.body()!!
        }
    }
}