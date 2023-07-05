package com.avafin.apptest.domain

import com.avafin.apptest.data.FieldsRepository
import javax.inject.Inject

class GetFieldsUseCase @Inject constructor(private val repository: FieldsRepository) {
    suspend operator fun invoke() = repository.getAllFields()
}