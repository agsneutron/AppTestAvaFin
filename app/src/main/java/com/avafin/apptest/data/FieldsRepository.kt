package com.avafin.apptest.data

import com.avafin.apptest.data.model.FieldsModel
import com.avafin.apptest.data.model.FieldsProvider
import com.avafin.apptest.data.network.FieldsService
import okhttp3.ResponseBody
import javax.inject.Inject

class FieldsRepository @Inject constructor(
    private val api: FieldsService,
    private val fieldsProvider: FieldsProvider
) {

    suspend fun getAllFields(): FieldsModel {  // ResponseBody
        val response = api.getFields()
        fieldsProvider.fields = response
        return response
    }
}