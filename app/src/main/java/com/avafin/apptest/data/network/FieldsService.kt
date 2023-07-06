package com.avafin.apptest.data.network

import com.avafin.apptest.data.model.FieldsModel
import com.avafin.apptest.data.model.RequestData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Inject


/* DE acuerdo con uno de los principios SOLID, vamos a generar un repositorio que sea el que decida de donde
* obriene la información, es decir, retrofit, o firebase o cambiamos los endpoints, esta clasé es la única que tendriamos que modificar
* y el resto del proyecto no se entera ni sufre cambios*/
class FieldsService @Inject constructor(private val api:FieldsApiClient) {

    /*como usamos corrutinas, generamos una función suspendida para obtener todos los campos que nos manda el endpoint*/
    suspend fun getFields(): FieldsModel { // esta función suspendida regresa un modelo tipo FieldsModel
        return withContext(Dispatchers.IO) {
            // con la corrutina, se ejecuta esto en un hilo secundario para no saturar la  interfáz de usuario
            val response = api.getAllFields()
            response.body()!!// el endpoint siempre regresa una respuesta en el body
        }
    }
}