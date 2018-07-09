package com.srest.framework.request

class Request(
        val method: HttpMethod,
        val endpoint: HttpEndpoint
) {

    fun appendHeader(headerLine: String) {
//        println(headerLine)
//        val headerParams = headerLine.split(" ")

    }

    companion object {

        fun build(requestHeader: String): Request {
            val requestType = requestHeader.split(" ")
            return Request(
                    HttpMethod.getMethod(requestType[0]),
                    HttpEndpoint.build(requestType[1])
            )
        }
    }
}