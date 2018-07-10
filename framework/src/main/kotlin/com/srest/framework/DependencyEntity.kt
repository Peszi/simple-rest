package com.srest.framework

import com.srest.framework.request.HttpMethod
import com.srest.framework.util.RequestMapping
import java.lang.reflect.Method

class DependencyEntity(
        val endpoint: String,
        private val dependency: Any,
        private val methods: List<MethodEntry>
) {

    fun getMethodResult(endpoint: String): Any? {
        methods.filter {
            endpoint.equals(it.mapping, true) || (it.suffix && endpoint.startsWith(it.mapping))
        }.forEach { return it.methodExecutor.invoke(dependency) ?: null }
        return null
    }
}

class MethodEntry(
        val mapping: String,
        val suffix: Boolean,
        val httpMethod: HttpMethod,
        val methodExecutor: Method
) {
    companion object {
        fun build(method: Method, methodAnnotation: RequestMapping) =
                MethodEntry(methodAnnotation.location, methodAnnotation.suffix, methodAnnotation.method, method)
    }
}