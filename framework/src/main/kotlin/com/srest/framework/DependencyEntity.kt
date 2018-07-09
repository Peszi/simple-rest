package com.srest.framework

import com.srest.framework.request.HttpMethod
import com.srest.framework.util.RequestMapping
import java.lang.reflect.Method

class DependencyEntity(
        private val dependency: Any,
        private val methods: Map<String, MethodEntry>
) {

    fun invoke(endpoint: String): Any? {
        return methods.get(endpoint)?.methodExecutor?.invoke(
                dependency
        ) ?: return null
    }
}

class MethodEntry(
        val httpMethod: HttpMethod,
        val methodExecutor: Method
) {
    companion object {
        fun build(method: Method, methodAnnotation: RequestMapping) = MethodEntry(
                methodAnnotation.method, method
        )
    }
}