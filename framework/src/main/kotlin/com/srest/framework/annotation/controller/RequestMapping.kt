package com.srest.framework.util

import com.srest.framework.request.HttpMethod
import com.srest.framework.response.ContentType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestMapping(
        val method: HttpMethod,
        val mapping: String = "",
        val contentType: String = ContentType.TEXT_HTML
)