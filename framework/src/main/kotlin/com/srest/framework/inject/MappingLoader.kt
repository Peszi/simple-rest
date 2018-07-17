package com.srest.framework.inject

import com.srest.framework.main.MethodEntry
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping

internal object MappingLoader {

    fun addEndpoints(beansMappers: MutableList<MethodEntry>, annotatedClasses: List<AnnotatedClass>) {
        annotatedClasses // TODO notify about overriding endpoint
                .filter { Controller::class in it.annotations }
                .forEach {
                    beansMappers.addAll(MappingLoader.getMappers(it.classObject))
                }
    }

    private fun getMappers(controller: Class<*>): List<MethodEntry> = controller.declaredMethods
            .filter { it.isAnnotationPresent(RequestMapping::class.java)}
            .map { val methodAnnotation = it.getAnnotation(RequestMapping::class.java)
                MethodEntry.build(controller.name, it, methodAnnotation)
            }
}