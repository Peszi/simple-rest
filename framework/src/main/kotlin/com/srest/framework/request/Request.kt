package com.srest.framework.request

class Request(
        val method: HttpMethod,
        val endpoint: String,
        val params: Map<String, String>,
        val headers: Map<String, String>
) {

    fun isAccepting(contentType: String) = headers["Accept"]?.run { return contains(contentType, true) } ?: false

    companion object {

        fun build(requestHeaders: List<String>): Request {
            val requestType = requestHeaders.first().split(" ")
            val requestPath = requestType[1].split("?")
            return Request(
                    HttpMethod.getMethod(requestType[0]),
                    requestPath[0],
                    parseParams(if (requestPath.size == 2) requestPath[1] else ""),
                    requestHeaders
                            .filterIndexed{ index, _ -> index > 0 }.map { it.split(": ") }
                            .filter { it.size >= 2 }.map { it[0] to it[1] }.toMap()
            )
        }

        private fun parseParams(queryChain: String): Map<String, String> {
            val params = mutableMapOf<String, String>()
            if (queryChain.isEmpty()) return params
            queryChain.split("&")
                    .forEach {
                        val param = it.split("=")
                        params[param[0]] = if (param.size == 2) param[1] else ""
                    }
            return params
        }
    }
}