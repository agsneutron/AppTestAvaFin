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
    private val getFieldsUseCase: GetFieldsUseCase
) : ViewModel() {
    val fieldsModel = MutableLiveData<ResponseBody>() //MutableLiveData<FieldsModel>()
    val isLoading = MutableLiveData<Boolean>()

    fun onCreate() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getFieldsUseCase()

            //if(result.ok==1){
                fieldsModel.postValue(result)
                isLoading.postValue(false)
            //}
        }
    }
}