package com.srest.framework.request

internal enum class HttpMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    companion object {

        @JvmStatic
        fun getMethod(method: String): HttpMethod = when(method.trim()) {
            "POST" -> POST
            "PUT" -> PUT
            "DELETE" -> DELETE
            else -> GET
        }
    }
}