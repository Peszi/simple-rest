package com.srest.framework.response

class ErrorResponse(
        val code: Int,
        val error: String,
        val endpoint: String,
        val message: String = "No message included!"
) {
    companion object {
        fun buildNotFound(endpoint: String) = ErrorResponse(404, "Not found!", endpoint)
    }
}