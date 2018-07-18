package com.srest.framework.main

import com.srest.framework.request.HttpMethod
import com.srest.framework.response.ContentType
import com.srest.framework.response.ResponseParams
import com.srest.framework.util.RequestMapping
import java.lang.reflect.Method

class MethodEntry(
        val bean: String,
        val method: Method,

        val requestEndpoint: String,
        val requestMethod: HttpMethod,
        val requestContentType: String
) {

    fun toResponseParams() = ResponseParams(
            requestContentType
    )

    companion object {
        fun build(bean: String, method: Method, annotation: RequestMapping) =
                MethodEntry(bean, method, annotation.mapping, annotation.method, annotation.contentType)

        fun buildPageEntry(bean: String, method: Method, endpoint: String) =
                MethodEntry(bean, method, endpoint, HttpMethod.GET, ContentType.HTML_TYPE)

        fun buildComponentEntry(bean: String, method: Method, endpoint: String) =
                MethodEntry(bean, method, endpoint, HttpMethod.POST, ContentType.HTML_TYPE)

        fun buildFileEntry(bean: String, method: Method, endpoint: String, extension: String) =
                MethodEntry(bean, method, endpoint, HttpMethod.GET,  ContentType.getTypeForExtension(extension))
    }
}