package com.avafin.apptest.data

import com.avafin.apptest.data.model.FieldsModel
import com.avafin.apptest.data.model.FieldsProvider
import com.avafin.apptest.data.network.FieldsService
import okhttp3.ResponseBody
import javax.inject.Inject

// Generamos nuestro repositorio de datos
// con dagger hilt hacemos inyección de dependencias agregando @Inject constructor
class FieldsRepository @Inject constructor(
    private val api: FieldsService,   // inyectamos FieldsService y FieldsProvider
    private val fieldsProvider: FieldsProvider
) {
    // generamos una función suspendida ya que usaremos corrutinas
    // esta clase es la que gestiona la llamada a los campos
    // y es la que gestiona si esos campos se obtienen de una base de datos por ejemplo o como es el caso de retrofit
    suspend fun getAllFields(): FieldsModel {  // ResponseBody
        val response = api.getFields()
        // guardamos en memoria el response y lo hacemos en el provider ya que solo haremos esta llama una sola vez
        fieldsProvider.fields = response
        return response
    }
}