package com.xs.carappinterviewtask.data.model.shared

data class ErrorResponse(
    val message: String,
    val status:String? = null,
    val code:Int? = null,
    val errors: List<String>?,
)
