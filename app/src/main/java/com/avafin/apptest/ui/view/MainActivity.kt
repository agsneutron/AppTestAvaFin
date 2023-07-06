package com.avafin.apptest.ui.view

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.avafin.apptest.R
import com.avafin.apptest.data.model.Data
import com.avafin.apptest.data.model.FieldsModel
import com.avafin.apptest.databinding.ActivityMainBinding
import com.avafin.apptest.ui.viewmodel.FieldsViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fieldsViewModel: FieldsViewModel by viewModels()
    var linearLayoutForm: LinearLayout? = null
    var btnRegister: Button?=null
    lateinit var dataFields: FieldsModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fieldsViewModel.onCreate()
        linearLayoutForm = findViewById<LinearLayout>(R.id.form)
        btnRegister = findViewById<Button>(R.id.btnRegister)

        fieldsViewModel.fieldsModel.observe(this, Observer {
            //binding.tvQuote.text = it.toString()
            //binding.tvQuote.text = it.data.amlCheck.toString()
            //binding.tvAuthor.text = it.data.customerFirstname.toString()
            //generateForm(it, findViewById<LinearLayout>(R.id.viewContainer))


            // Genera el formulario dinámicamente utilizando DynamicFormGenerator
            //val dynamicFormGenerator = DynamicFormGenerator(this)
            // Parsea el JSON y obtén el mapa de datos del formulario
            generateForm(it)


        })
        fieldsViewModel.isLoading.observe(this, Observer {
            binding.loading.isVisible = it
        })

        btnRegister!!.setOnClickListener {
            // Código a ejecutar cuando se haga clic en el botón
            // Aquí puedes realizar las acciones que deseas realizar al hacer clic en el botón
            formValidation()
        }
    }

    fun formValidation(){
        val count = linearLayoutForm!!.childCount
        val gson = Gson()
        val jsonData = gson.toJson(dataFields)
        val formData = parseJson(jsonData)
        for (i in 0 until count) {
            //get the childs of the first layer
            val viewGroup = linearLayoutForm!!.getChildAt(i)
            Log.e("---- llChild", viewGroup.id.toString())
            if (viewGroup is TextInputLayout) {
                val value = (viewGroup as TextInputLayout).editText!!.text.toString()
                val nombre = viewGroup.hint.toString() as String
                val claveBuscada = "customer-lastname" // Clave que deseas buscar

                viewGroup.isErrorEnabled = false
                viewGroup.error=""

                if (formData.containsKey(nombre)) {
                    val valorBuscado = formData[nombre]
                    // Realiza las operaciones con el valor encontrado
                    val maxlen=valorBuscado?.get("maxlength") as Int
                    if (value.length > maxlen){
                        viewGroup.isErrorEnabled = true
                        viewGroup.error="El tamaño de texto excede el máximo de: " + valorBuscado?.get("maxlength").toString()
                    }else{
                        val pattern = Pattern.compile(valorBuscado?.get("regex").toString())
                        val matcher = pattern.matcher(value)

                        if (!matcher.matches()) {
                            viewGroup.isErrorEnabled = true
                            viewGroup.error="No cumple con regex: " + valorBuscado?.get("regex").toString()
                        }
                    }
                }

            }

        }
       print(count)
    }
    fun generateForm(json: FieldsModel) {
        val inflater = this.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gson = Gson()
        val jsonData = gson.toJson(json)
        val formData = parseJson(jsonData)
        dataFields = json

        for ((key, value) in formData) {
            if (value["visible"] == true){
                when (value["type"]) {
                    "text" -> addFormTextField(key, value, inflater)
                    "select" -> addFormSelectField(key, value,inflater)
                    //"checkbox" -> addFormCheckField(key, value)
                    //else -> addFormTextField(key, value, inflater)
                }
            }

        }

    }

    private fun parseJson(json: String): Map<String, Map<String, Any>> {
        val formData = mutableMapOf<String, Map<String, Any>>()

        try {
            val jsonObject = JSONObject(json)
            val jsonData = jsonObject.getJSONObject("data")
            val keys = jsonData.keys()

            while (keys.hasNext()) {
                val key = keys.next() as String
                val fieldData = jsonData.getJSONObject(key)
                val fieldMap = mutableMapOf<String, Any>()

                val fieldKeys = fieldData.keys()
                while (fieldKeys.hasNext()) {
                    val fieldKey = fieldKeys.next() as String
                    val fieldValue = fieldData.get(fieldKey)
                    fieldMap[fieldKey] = fieldValue
                }

                formData[key] = fieldMap
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return formData
    }

    private fun addFormTextField(key: String,fieldData: Map<String, Any>, inflater: LayoutInflater){
        val view: View = LayoutInflater.from(this).inflate(R.layout.form_text_input, null)
        val textField = view.findViewById<TextInputLayout>(R.id.textField)
        textField.setHint(key)
        linearLayoutForm?.addView(textField)
    }

    private fun addFormSelectField(key: String,fieldData: Map<String, Any>, inflater: LayoutInflater){

        val view: View = LayoutInflater.from(this).inflate(R.layout.form_select_field, null)
        val selectField = view.findViewById<TextInputLayout>(R.id.listField) as TextInputLayout
        //userNameIDTextInputLayout.id = indice
        selectField.setHint(key)

        val autoCompleteTextView: AutoCompleteTextView
        autoCompleteTextView = selectField.editText as AutoCompleteTextView
        val lista: MutableList<String> = mutableListOf()
        if (key=="language"){
            val valuesJO = fieldData["values"] as JSONObject
            // Obtener las claves del JSONObject
            val keys = valuesJO.keys()
            // Recorrer el JSONObject
            while (keys.hasNext()) {
                val key = keys.next()
                val value = valuesJO[key]
                lista.add(value.toString()) //Lista de valores para AutoCompleteTextView
            }
            val adapter = ArrayAdapter(this, R.layout.item_exposed_dropdown, lista)
            autoCompleteTextView.setAdapter(adapter)
        }else {
            val valuesData = fieldData["values"] as JSONArray
            for (i in 0 until valuesData.length()) {
                val element = valuesData[i]
                lista.add(element.toString()) //Lista de valores para AutoCompleteTextView
            }
            //val list: List<Any> = listOf(valuesData) //Lista de valores para AutoCompleteTextView
            val adapter = ArrayAdapter(this, R.layout.item_exposed_dropdown, lista)
            autoCompleteTextView.setAdapter(adapter)
        }


        linearLayoutForm?.addView(selectField)
    }




    private fun getInputType(fieldData: Map<String, Any>): Int {
        val fieldType = fieldData["type"] as String
        return when (fieldType) {
            "text" -> InputType.TYPE_CLASS_TEXT
            "select" -> InputType.TYPE_CLASS_TEXT
            "checkbox" -> InputType.TYPE_CLASS_TEXT
            else -> InputType.TYPE_CLASS_TEXT
        }
    }


}