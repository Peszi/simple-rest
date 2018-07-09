package com.srest.framework.request

class HttpEndpoint(
        val layers: List<String>
) {

    fun getEndpoint(): String {
        var endpoint = ""; layers.forEach { endpoint += "/$it" }; return endpoint
    }

    companion object {

        fun build(endpoint: String): HttpEndpoint
                = HttpEndpoint(endpoint.trim().split("/").filter { it.isNotEmpty() })
    }
}