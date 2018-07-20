package com.srest.framework.main

import com.srest.framework.annotation.util.ResponseBody
import com.srest.framework.request.HttpMethod
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.util.RequestMapping
import java.lang.reflect.Method

class MethodEntry(
        val beanName: String,
        val beanMethod: Method,

        val requestEndpoint: String,
        val requestMethod: HttpMethod,
        private val requestContentType: String,

        val responseBody: Boolean = false
) {

    fun toResponse() = Response(
            requestContentType
    )

    companion object {
        fun build(bean: String, method: Method, annotation: RequestMapping, responseBody: Boolean) =
                MethodEntry(bean, method, annotation.mapping, annotation.method, annotation.contentType, responseBody)

        fun buildPageEntry(bean: String, method: Method, endpoint: String) =
                MethodEntry(bean, method, endpoint, HttpMethod.GET, ContentType.TEXT_HTML)

        fun buildComponentEntry(bean: String, method: Method, endpoint: String) =
                MethodEntry(bean, method, endpoint, HttpMethod.POST, ContentType.TEXT_HTML)

        fun buildFileEntry(bean: String, method: Method, endpoint: String, extension: String) =
                MethodEntry(bean, method, endpoint, HttpMethod.GET,  ContentType.getTypeForExtension(extension))
    }
}