package com.srest.framework.main

import com.srest.framework.DependencyEntity
import com.srest.framework.request.Request
import com.srest.framework.util.Logger
import kotlin.reflect.KClass

class DependencyManager(
        baseClass: KClass<*>
) {

    private val controllers = RestInitializer.loadControllers(AnnotationScanner.getAnnotatedClasses(baseClass))

    fun getData(request: Request): String? {
        val endpoint = request.endpoint.getEndpoint()
        controllers.forEach {
            if (endpoint.startsWith(it.endpoint)) {
                val result = it.invoke(endpoint)
                if (result != null && result is String) return result
                else Logger.log.warn("Incorrect result type!")
            }
        }
        return null
    }

}