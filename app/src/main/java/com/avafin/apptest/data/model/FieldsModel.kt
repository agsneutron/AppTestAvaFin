package com.avafin.apptest.data.model
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

// definimos el modelo de respuesta que nos regresará retrofit
data class FieldsModel(
    val ok: Int,
    val data: Data
)

data class Data(
    /* Serializamos los nombres de los campos que regresa el endpoint, ya que como variables no son válidos nombres como:
      customer-lastname o customer-monthly-income, y así oodemos renombrarlos a una forma más convencional*/
    @SerializedName("customer-lastname") val customerLastname: Field,
    @SerializedName("customer-phone") val customerPhone: Field,
    @SerializedName("customer-monthly-income") val customerMonthlyIncome: Field,
    @SerializedName("bank-iban") val bankIban: Field,
    @SerializedName("language") val language: LanguageField,
    @SerializedName("customer-personcode") val customerPersoncode: Field,
    @SerializedName("customer-email") val customerEmail: Field,
    @SerializedName("customer-firstname") val customerFirstname: Field,
    @SerializedName("customer-gender") val customerGender: SelectField,
    @SerializedName("customer-birthday") val customerBirthday: Field,
    @SerializedName("pep-status") val pepStatus: CheckboxField,
    @SerializedName("aml-check") val amlCheck: CheckboxField,


    )

    // generamos un data cláss para los campos tipo texto
    data class Field(
        val req: Boolean,
        val group: String,
        val visible: Boolean,
        val order: Int,
        val maxlength: Int?,
        val type: String,
        val newline: Boolean,
        val hidetitle: Boolean,
        val split: Boolean,
        val mapper: String,
        val clientzoneVisible: Boolean,
        val clientzoneEditable: Boolean,
        val clientzoneCheck: String?,
        val clientzoneRequired: Boolean,
        val clVisible: Boolean,
        val step: Int,
        val autoApprove: Boolean,
        val conditionType: Int,
        val condition: List<Any>? = null,
        val regex: String? = null
    )

    // generamos un data cláss para los campos tipo select, pero que los values tienen la estructura (llave,valor)
    data class LanguageField(
        val req: Boolean,
        val group: String,
        val visible: Boolean,
        val order: Int,
        val maxlength: Int?,
        val type: String,
        val newline: Boolean,
        val hidetitle: Boolean,
        val split: Boolean,
        val mapper: String,
        val clientzoneVisible: Boolean,
        val clientzoneEditable: Boolean,
        val clientzoneCheck: String?,
        val clientzoneRequired: Boolean,
        val clVisible: Boolean,
        val step: Int,
        val autoApprove: Boolean,
        val conditionType: Int,
        val condition: List<Any>? = null,
        val values: Map<String, String>
    )

// generamos un data cláss para los campos tipo select, pero que los values tienen la estructura (valor) sin llave
    data class SelectField(
        val req: Boolean,
        val group: String,
        val visible: Boolean,
        val order: Int,
        val maxlength: Int?,
        val type: String,
        val newline: Boolean,
        val hidetitle: Boolean,
        val split: Boolean,
        val mapper: String,
        val clientzoneVisible: Boolean,
        val clientzoneEditable: Boolean,
        val clientzoneCheck: String?,
        val clientzoneRequired: Boolean,
        val clVisible: Boolean,
        val step: Int,
        val autoApprove: Boolean,
        val conditionType: Int,
        val condition: List<Any>? = null,
        val values: List<String>
    )

    // generamos un data cláss para los campos tipo checkbox
    data class CheckboxField(
        val req: Boolean,
        val group: String,
        val visible: Boolean,
        val order: Int,
        val maxlength: Int?,
        val type: String,
        val newline: Boolean,
        val hidetitle: Boolean,
        val split: Boolean,
        val mapper: String,
        val clientzoneVisible: Boolean,
        val clientzoneEditable: Boolean,
        val clientzoneCheck: String?,
        val clientzoneRequired: Boolean,
        val clVisible: Boolean,
        val step: Int,
        val autoApprove: Boolean,
        val conditionType: Int,
        val condition: List<Any>? = null,
        val regex: String? = null
    )


// creamos un data class para el json con las credenciales de acceso al endpoint
data class RequestData(
    val login: String,
    val password: String,
    val data: JsonObject
)

