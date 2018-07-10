package com.srest.framework.main

import com.srest.framework.request.Request
import com.srest.framework.util.Logger
import kotlin.reflect.KClass

class ResponseManager(
        baseClass: KClass<*>
) {

    private val controllers = ComponentLoader.loadControllers(AnnotationScanner.getAnnotatedClasses(baseClass))

//    fun getIndex(): String {
//        val result = controllers[0].invoke("")
//        return if (result is String) return result else "error"
//    }

    fun getWebResponse(request: Request): String? {
        // if web controller exists
        return null
    }

    fun getResponse(request: Request): String? {
        val endpoint = request.endpoint.getEndpoint()
        for (controller in controllers) {
            if (endpoint.startsWith(controller.endpoint)) {
                val result = controller.getMethodResult(endpoint)
                if (result != null && result is String) return result
                else Logger.log.warn("No result or incorrect type!")
            }
        }
        return null
    }

}