package com.srest.framework.util

import com.srest.framework.request.HttpMethod

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestMapping(
        val method: HttpMethod,
        val location: String = ""
)