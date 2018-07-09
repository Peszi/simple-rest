package com.srest.framework.main

import com.srest.framework.DependencyEntity
import com.srest.framework.MethodEntry
import com.srest.framework.util.RequestMapping

object RestInitializer {

    @JvmStatic
    fun loadControllers(controllers: Set<Class<*>>): Map<String, DependencyEntity> {
        val controllersMap = mutableMapOf<String, DependencyEntity>()
        controllers.forEach {
            val methods = mutableMapOf<String, MethodEntry>()
            it.declaredMethods.filter { it.isAnnotationPresent(RequestMapping::class.java)}.forEach {
                val methodAnnotation = it.getAnnotation(RequestMapping::class.java)
                println("M " + methodAnnotation.method.name + " E " + methodAnnotation.mapping)
                methods.put(methodAnnotation.mapping, MethodEntry.build(it, methodAnnotation))
            }
            controllersMap.put("", DependencyEntity(it.newInstance(), methods))
        }
        return controllersMap
    }
}