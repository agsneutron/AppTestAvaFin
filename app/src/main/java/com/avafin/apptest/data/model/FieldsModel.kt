package com.avafin.apptest.data.model
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

/*data class FieldsModel(
    val err: List<String>
)*/

data class FieldsModel(
    val ok: Int,
    val data: Data
)

data class Data(
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


data class RequestData(
    val login: String,
    val password: String,
    val data: JsonObject
)

data class Data2(
    val `new-registration`: Boolean
)