package com.srest.framework.util

import com.srest.framework.main.MethodEntry

object UrlMapper {

    fun getMappingHandlers(controller: Class<*>): Map<String, MethodEntry> {
        val methods = mutableMapOf<String, MethodEntry>()
        val beanName = controller.simpleName
        controller.declaredMethods.filter { it.isAnnotationPresent(RequestMapping::class.java)}.forEach {
            val methodAnnotation = it.getAnnotation(RequestMapping::class.java)
            Logger.log.info("binding request [${methodAnnotation.method.name}] '${methodAnnotation.mapping}'")
            methods[methodAnnotation.mapping] = MethodEntry.build(beanName, it, methodAnnotation)
        }
        return methods
    }
}