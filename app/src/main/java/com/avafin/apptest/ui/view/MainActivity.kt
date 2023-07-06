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
import com.avafin.apptest.data.model.FieldsModel
import com.avafin.apptest.databinding.ActivityMainBinding
import com.avafin.apptest.ui.viewmodel.FieldsViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern

// con la notación AndroidEntryPoint de dagger hilt preparamps a la clase
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fieldsViewModel: FieldsViewModel by viewModels()
    // definimos la variable que va a contener el linearlayput donde se van a agregar los campos que nos devuelve el endpoint
    var linearLayoutForm: LinearLayout? = null
    // definimos el botón que va a contener la referencia del botón del layout
    var btnRegister: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fieldsViewModel.onCreate() // llamamos a oncreate del view model que va a tener la logica
        linearLayoutForm = findViewById<LinearLayout>(R.id.form) // asignamos la referencia al layout
        btnRegister = findViewById<Button>(R.id.btnRegister) // agrgamos la referencia al botón

        // fieldsModel es un MutableLiveData por lo tanto llamaos a observe para cachar los cambios
        fieldsViewModel.fieldsModel.observe(this, Observer {
            // los campos obtenidos del response del endpoint generamos el formulario dinámigo
            generateForm(it)
        })
        // isLoading también es un MutableLiveData con observe cachamos los cambios y con el valor Booleano
        // hacemos visible o invisible el ProgressBar
        fieldsViewModel.isLoading.observe(this, Observer {
            binding.loading.isVisible = it
        })

        btnRegister!!.setOnClickListener {
            // Código a ejecutar cuando se haga clic en el botón
            // formValidation hace las validaciones de los campos y regresa un Boolean, si es true hay errores en los campos
            // de acuerdo a los datos de cada campo como MaxLengh y Regex, si es false no hay errores
            if (formValidation()){
                Toast.makeText(applicationContext, "Hay errores de captura, por favor resuelve y vuelve a dar click en validar", Toast.LENGTH_LONG)
                    .show()
            }else{
                Toast.makeText(applicationContext, "Todos los campos fueron validados correctamente", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }


    fun formValidation(): Boolean{
        val count = linearLayoutForm!!.childCount // contabilizamos cuantos campos se agregaron al linearlayout
        val gson = Gson()
        val jsonData = gson.toJson(fieldsViewModel.fieldsModel.value) // pasamos a formato json
        val formData = parseJson(jsonData) // parseJson mapea los campos
        var errorExist: Boolean = false // para setear si hay errores o no y regresarlo
        for (i in 0 until count) {
            //obtener todos los hijos (campos)
            val viewGroup = linearLayoutForm!!.getChildAt(i) // se obtiene cada uno de los hijos (campos)
            // el requerimiento dice que se muestren los campos que su atributo visible=true
            // los únicos campos que son visibles son los tipo text, por eso solo metí validación para esos campos
            if (viewGroup is TextInputLayout) { // si el campo es del tipo TextInputLayout, es decir los campos con atributo type=text
                val value = (viewGroup as TextInputLayout).editText!!.text.toString() // obtengo el value de cada campo
                val nombre = viewGroup.hint.toString() as String // obtengo el nombre del campo

                // reseteo los erroes de cada campo
                viewGroup.isErrorEnabled = false
                viewGroup.error=""

                if (formData.containsKey(nombre)) { // reviso que el nombre del campo exista en el mapa
                    val valorBuscado = formData[nombre] // obtengo el Data de cada campo, es decir todos sus atributos
                    // Realiza las operaciones con el valor encontrado
                    val maxlen=valorBuscado?.get("maxlength") as Int // obtengo el valor del atributo maxlength de cada campo
                    // comparo el valor del atributo maxlength con el tamaño del campo
                    if (value.length > maxlen){
                        // si es mayor el número de caracteres del campo al atributo maxlength, entonces muestra el error
                        errorExist=true
                        viewGroup.isErrorEnabled = true
                        viewGroup.error="El tamaño de texto excede el máximo de: " + valorBuscado?.get("maxlength").toString()
                    }else{
                        // en otro caso revisa que el contenido del campo cumpla con el regex que tiene el atributo regex del campo
                        val pattern = Pattern.compile(valorBuscado?.get("regex").toString())
                        val matcher = pattern.matcher(value)
                        // si el contenido del campo no hace match con el regex, muestra el mensaje de error
                        if (!matcher.matches()) {
                            errorExist=true
                            viewGroup.isErrorEnabled = true
                            viewGroup.error="No cumple con regex: " + valorBuscado?.get("regex").toString()
                        }
                    }
                }

            }

        }
       return errorExist // regresa true o false
    }
    fun generateForm(json: FieldsModel) {
        val inflater = this.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val gson = Gson()
        val jsonData = gson.toJson(json) // convierte FieldModel a json para mapear
        val formData = parseJson(jsonData) // mapea los campos del json
        binding.loading.isVisible = true // muestra el progresbarr
        for ((key, value) in formData) { // por cada campo en el mapa
            if (value["visible"] == true){ // revisa si el atributo visible es true
                // genera un case dependiendo del atributo type
                // NOTA: como el requerimiento es que se muestren solo los campos con atributo visible=true
                //       solo me enfoqué en los tipos type=text, aunque si quitamos la condición if (value["visible"] == true),
                //       muestra también los campos select con sus valores
                when (value["type"]) {
                    "text" -> addFormTextField(key, value, inflater) // agrega linearLayoutForm un campo tipo text
                    "select" -> addFormSelectField(key, value,inflater) // agrega linearLayoutForm un campo tipo select
                    //"checkbox" -> addFormCheckField(key, value) // agrega linearLayoutForm un campo tipo checkbox

                }
            }

        }
        binding.loading.isVisible = false // pone invisible el progressbar

    }

    //funcion que genera un mapa a partir de FieldsModel que se obtubo del endpoint
    private fun parseJson(json: String): Map<String, Map<String, Any>> {
        val formData = mutableMapOf<String, Map<String, Any>>()

        try {
            val jsonObject = JSONObject(json)
            val jsonData = jsonObject.getJSONObject("data") // obtiene el json data
            val keys = jsonData.keys() // obtiene las llaves de cada elemento de data

            while (keys.hasNext()) { // recorre el mapa
                val key = keys.next() as String // obtiene cada elemento
                val fieldData = jsonData.getJSONObject(key) // genera un json de cada elemento
                val fieldMap = mutableMapOf<String, Any>() // mapa para contener el nombre de cada campo y su respectivo data (lista de atributos)

                val fieldKeys = fieldData.keys() // por cada campo
                while (fieldKeys.hasNext()) {
                    val fieldKey = fieldKeys.next() as String // obtiene el nombre de cada campo
                    val fieldValue = fieldData.get(fieldKey) // obtiene el data (lista de atributos) de cada campo
                    fieldMap[fieldKey] = fieldValue // agrega a fieldMap cada elemento (campo)
                }

                formData[key] = fieldMap //agrega a formData los elementos (campos) y su respectivo data (lista de atributos)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return formData
    }

    // función que agrega un campo  tipo text a linearLayoutForm
    private fun addFormTextField(key: String,fieldData: Map<String, Any>, inflater: LayoutInflater){
        // obtiene el layout donde se encuentra el Text Imput
        val view: View = LayoutInflater.from(this).inflate(R.layout.form_text_input, null)
        // obtiene del layout el textField que se va a agregar a linearLayoutForm
        val textField = view.findViewById<TextInputLayout>(R.id.textField)
        textField.setHint(key) // setea el valor de hint al text input
        linearLayoutForm?.addView(textField) // se agrega el TExt Input a linearLayoutForm
    }

    private fun addFormSelectField(key: String,fieldData: Map<String, Any>, inflater: LayoutInflater){
        // obtiene el layout donde se encuentra el select
        val view: View = LayoutInflater.from(this).inflate(R.layout.form_select_field, null)
        // obtiene del layout el select que se va a agregar a linearLayoutForm
        val selectField = view.findViewById<TextInputLayout>(R.id.listField) as TextInputLayout
        // setea el valor de hint al select
        selectField.setHint(key)

        // generamos un AutoCompleteTextView donde vamos a dejar cada elemento del atributo values del campo select
        val autoCompleteTextView: AutoCompleteTextView
        autoCompleteTextView = selectField.editText as AutoCompleteTextView // ligamos al campo select
        // al dar click sobre el campo select va a desplegar los valores de opcción que vienen en el atributo values
        val lista: MutableList<String> = mutableListOf() // lista donde se van a guardar los valores
        // aquí hago diferencia para los tipos de SELECT, ya qye para el campo language la estructura de values es
        // (llave,valor) y para Gender la estructura es (valor)
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
            //
            val adapter = ArrayAdapter(this, R.layout.item_exposed_dropdown, lista)
            autoCompleteTextView.setAdapter(adapter)
        }


        linearLayoutForm?.addView(selectField)
    }



}