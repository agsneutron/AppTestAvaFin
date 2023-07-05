package com.avafin.apptest.data.model

import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldsProvider @Inject constructor() {
    lateinit var fields: ResponseBody//FieldsModel
}