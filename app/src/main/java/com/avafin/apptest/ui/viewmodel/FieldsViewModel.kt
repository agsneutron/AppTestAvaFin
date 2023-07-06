package com.avafin.apptest.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avafin.apptest.data.model.FieldsModel
import com.avafin.apptest.domain.GetFieldsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class FieldsViewModel @Inject constructor(
    private val getFieldsUseCase: GetFieldsUseCase // inyectamos nuestro caso de uso
) : ViewModel() {
    val fieldsModel = MutableLiveData<FieldsModel>() //MutableLiveData<ResponseBody>()
    val isLoading = MutableLiveData<Boolean>()

    fun onCreate() {
        // hacemos llamada a nuestro caso de uso para que nos regrese todos los campos y los almacene en memoria
        // hacemos la llamada a la corrutina con viewModelScope
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getFieldsUseCase()

            if(result.ok==1){
                fieldsModel.postValue(result)
                isLoading.postValue(false)
            }
        }
    }
}