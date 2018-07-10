package com.srest.framework.main

import com.srest.framework.DependencyEntity
import com.srest.framework.MethodEntry
import com.srest.framework.util.Logger
import com.srest.framework.util.RequestMapping

object ComponentLoader {

    @JvmStatic
    fun loadControllers(controllers: Set<Class<*>>): List<DependencyEntity> {
        val controllersMap = mutableListOf<DependencyEntity>()
        controllers.forEach {
            val methods = mutableListOf<MethodEntry>()
            it.declaredMethods.filter { it.isAnnotationPresent(RequestMapping::class.java)}.forEach {
                val methodAnnotation = it.getAnnotation(RequestMapping::class.java)
                Logger.log.info("binding request [${methodAnnotation.method.name}] '${methodAnnotation.location}'")
                methods.add(MethodEntry.build(it, methodAnnotation))
            }
            controllersMap.add(DependencyEntity("", it.newInstance(), methods)) // TODO endpoints
        }
        return controllersMap
    }
}