package com.srest.framework.main

import com.srest.framework.annotation.Component
import com.srest.framework.util.ClassScanner
import com.srest.framework.util.Controller
import com.srest.framework.util.Logger
import com.srest.framework.util.UrlMapper
import kotlin.reflect.KClass

internal class DependencyManager(
        baseClass: KClass<*>
) {

    private val beansBuffer: MutableMap<String, Any> = mutableMapOf()
    private val mappingHandlers: MutableMap<String, MethodEntry> = mutableMapOf()

    init {
        ClassScanner.scanForClasses(baseClass).forEach { lookupComponent(it) }
    }

    private fun lookupComponent(objectClass: Class<*>) {
        // Inject components
        if (ClassScanner.isAnnotated(objectClass, Component::class)) {
            Logger.log.info("injecting component '${objectClass.simpleName}'")
            beansBuffer[objectClass.simpleName] = objectClass.newInstance()
        }
        // Store mapping handlers
        if (ClassScanner.isAnnotated(objectClass, Controller::class)) {
            mappingHandlers.putAll(UrlMapper.getMappingHandlers(objectClass))
        }
    }

    fun getBean(name: String): Any {
        return beansBuffer[name] ?: throw RuntimeException("bean not exists!")
    }
}