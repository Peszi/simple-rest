package com.srest.framework.inject

import com.srest.framework.annotation.RestController
import com.srest.framework.annotation.util.ResponseBody
import com.srest.framework.main.MethodEntry
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping

internal object MappingLoader {

    fun addEndpoints(beansMappers: MutableList<MethodEntry>, annotatedClasses: List<AnnotatedClass>) {
        annotatedClasses // TODO notify about overriding endpoint
                .filter { Controller::class in it.annotations || RestController::class in it.annotations }
                .forEach {
                    val isRestController = RestController::class in it.annotations
                    beansMappers.addAll(MappingLoader.getMappers(it.classObject, isRestController))
                }
    }

    private fun getMappers(controller: Class<*>, isRestController: Boolean): List<MethodEntry> = controller.declaredMethods
            .filter { it.isAnnotationPresent(RequestMapping::class.java)}
            .map {
                val methodAnnotation = it.getAnnotation(RequestMapping::class.java)
                val responseBody: Boolean = it.isAnnotationPresent(ResponseBody::class.java) || isRestController
                MethodEntry.build(controller.name, it, methodAnnotation, responseBody)
            }
}