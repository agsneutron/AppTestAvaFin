package com.avafin.apptest.data.model

import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton
// generamos el provider que nos va a permitir guardar en memoria el response del endpoint
@Singleton
class FieldsProvider @Inject constructor() {
    lateinit var fields: FieldsModel //ResponseBody
}