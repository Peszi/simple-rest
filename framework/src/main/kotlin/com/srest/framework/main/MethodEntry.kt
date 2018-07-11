package com.srest.framework.main

import com.srest.framework.request.HttpMethod
import com.srest.framework.util.RequestMapping
import java.lang.reflect.Method

class MethodEntry(
        val bean: String,
        val method: Method,
        val httpMethod: HttpMethod,
        val suffixMatcher: Boolean
) {
    companion object {
        fun build(bean: String, method: Method, methodAnnotation: RequestMapping) =
                MethodEntry(bean, method, methodAnnotation.method, methodAnnotation.suffix)
    }
}