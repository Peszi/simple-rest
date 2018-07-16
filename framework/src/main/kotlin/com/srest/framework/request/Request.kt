package com.srest.framework.request

class Request(
        val method: HttpMethod,
        val endpoint: String,
        val headers: MutableMap<String, String> = mutableMapOf()
) {

    fun appendHeader(headerLine: String) {
        val headerParams = headerLine.split(" ")
        headers[headerParams[0].substring(0, headerParams[0].length-1)] = headerParams[1]
    }

    companion object {

        fun build(requestHeader: String): Request {
            val requestType = requestHeader.split(" ")
            return Request(
                    HttpMethod.getMethod(requestType[0]), requestType[1]
            )
        }
    }
}