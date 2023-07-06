package com.avafin.apptest.domain

import com.avafin.apptest.data.FieldsRepository
import javax.inject.Inject

// aquí generamos los casos de uso, que se encargan de una unica acción
class GetFieldsUseCase @Inject constructor(private val repository: FieldsRepository) { // inyectamos el repositorio
    // generamos una función suspendida para la corrutina
    // usamos invoke para que cuando se haga la llamada a la clase GetFieldsUseCase, de inmediato nos regrese la respuesta
    suspend operator fun invoke() = repository.getAllFields()
}