package com.babel.cdm.components.common

open class CDMComponentsError(
    val id: Int,
    val description: String) {

    enum class Code(val code: Int){
        DEFAULT_ERROR(1001)
    }

}


