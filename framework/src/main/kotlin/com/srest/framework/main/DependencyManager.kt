package com.srest.framework.main

import com.srest.framework.DependencyEntity
import com.srest.framework.request.Request
import com.srest.framework.util.RequestLogger
import kotlin.reflect.KClass

class DependencyManager(
        baseClass: KClass<*>
) {

    private val controllers: Map<String, DependencyEntity> = RestInitializer.loadControllers(AnnotationScanner.getAnnotatedClasses(baseClass))

    fun getData(request: Request): String? {
        val endpoint = request.endpoint.getEndpoint()
        controllers.keys.forEach {
            if (endpoint.startsWith(it)) {
                println("checking ... '" + request.endpoint.getEndpoint() + "'")
                controllers.keys.forEach { println("> " + it) }
                val result = controllers.get(it)?.invoke(endpoint)
                if (result != null && result is String) return result
                else RequestLogger.log.warn("result not string!")
            }
        }
        return null
    }

}