package com.srest.framework.util

import com.srest.framework.main.MethodEntry

object UrlMapper {

    fun getMappingHandlers(controller: Class<*>): List<MethodEntry> = controller.declaredMethods
            .filter { it.isAnnotationPresent(RequestMapping::class.java)}
            .map { val methodAnnotation = it.getAnnotation(RequestMapping::class.java)
        MethodEntry.build(controller.simpleName, it, methodAnnotation)
    }
}