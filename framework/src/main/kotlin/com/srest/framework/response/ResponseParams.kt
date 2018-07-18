package com.srest.framework.response

import com.srest.framework.request.HttpMethod

class ResponseParams(
        var contentType: String
) {
    val headers: MutableMap<String, String> = mutableMapOf()
}