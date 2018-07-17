package com.srest.framework.inject

import com.srest.framework.annotation.Autowire
import com.srest.framework.annotation.Component
import com.srest.framework.main.MethodEntry
import com.srest.framework.request.Request
import com.srest.framework.response.Response
import com.srest.framework.response.ResponseParams
import com.srest.framework.util.Logger
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.reflect

internal object ClassInjector {

    fun injectBeans(beansBuffer: MutableMap<String, Any>, annotatedClasses: List<AnnotatedClass>) {
        // Filter all components
        val components = annotatedClasses.filter { Component::class in it.annotations }.map { it.classObject }

        // Empty constructors beans
        val simpleBeans = components.filter { it.constructors.isEmpty() || it.constructors.any { it.parameterTypes.none { it in components } } }
        simpleBeans.forEach { beansBuffer[it.name] = it.newInstance() }

        // Injected constructors beans
        val normalBeans = components.filterNot { it in simpleBeans }.toMutableList()
        while (true) {
            var beansInjected = false
            val iterator = normalBeans.iterator()
            while (iterator.hasNext()) {
                val bean = iterator.next()
                val constructor = bean.constructors
                        .firstOrNull { it.parameterTypes.all { it.name in beansBuffer.keys } } ?: continue
                val params = constructor.parameters
                        .map { beansBuffer[it.type.name] ?: throw RuntimeException("Cannot inject param!") }
                beansBuffer[bean.name] = constructor.newInstance(*params.toTypedArray()); iterator.remove()
                beansInjected = true
            }
            if (!beansInjected) {
                if (normalBeans.isNotEmpty())
                    throw RuntimeException("Injection loop with ${normalBeans.map{ it.simpleName }} !")
                break
            }
        }

        // Autowired beans
        beansBuffer.values.forEach { val bean = it
            it::class.memberProperties
                    .filter { it.findAnnotation<Autowire>() != null }
                    .forEach {
                        if (it is KMutableProperty<*>) {
                            it.setter.call(bean, beansBuffer[it.returnType.toString()]
                                    ?: throw RuntimeException("Bean not found!"))
                        }
                    }
        }
    }

    @JvmStatic
    fun invokeMethod(bean: Any, handler: MethodEntry, request: Request): Response {
        val responseParams = handler.toResponseParams()
        val arguments = mutableListOf<Any>()
        handler.method.parameters.filter { it.type == Request::class.java }.forEach { arguments.add(request) }
        handler.method.parameters.filter { it.type == ResponseParams::class.java }.forEach { arguments.add(responseParams) }
        try {
            val result = handler.method.invoke(bean, *arguments.toTypedArray())
            if (result is String) return Response(responseParams.contentType, result)
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        // map object to Json
        return Response(handler.requestContentType, "{ \"error\": \"json not supported\" }")
    }
}