package com.srest.framework.request

internal class HttpEndpoint(
        val layers: List<String>
) {

    companion object {

        fun build(endpoint: String): HttpEndpoint
                = HttpEndpoint(endpoint.trim().split("/").filter { it.isNotEmpty() })
    }
}