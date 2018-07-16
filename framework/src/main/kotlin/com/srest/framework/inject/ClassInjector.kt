package com.srest.framework.inject

import com.srest.framework.annotation.Component
import com.srest.framework.main.MethodEntry
import com.srest.framework.request.Request
import com.srest.framework.response.Response
import com.srest.framework.response.ResponseParams

internal object ClassInjector {

    fun injectBeans(beansBuffer: MutableMap<String, Any>, annotatedClasses: List<AnnotatedClass>) {
        val components = annotatedClasses.filter { Component::class in it.annotations }.map { it.classObject }

        // Empty constructors
        val simpleBeans = components.filter { it.constructors.isEmpty() || it.constructors.any { it.parameterTypes.none { it in components } } }
        simpleBeans.forEach { println("with empty constructor ${it.simpleName}"); beansBuffer[it.simpleName] = it.newInstance() }

        // Not empty constructors

        components
                .filterNot { it in simpleBeans }
                .forEach { println("with injection ${it.simpleName}")
                    val constructor = it.constructors.firstOrNull { it.parameterTypes.all { it in simpleBeans } } ?: throw RuntimeException("Incorrect constructor!")
                    val params = mutableListOf<Any>()
                    constructor.parameters.forEach {
                        if (it.type in simpleBeans) {
                            println("Param " + it.type.simpleName)
                            params.add(beansBuffer[it.type.simpleName] ?: throw RuntimeException("Cannot inject!"))
                        } else {
                            throw RuntimeException("Cannot inject incorrect param!")
                        }
                    }
                    beansBuffer[it.simpleName] = constructor.newInstance(*params.toTypedArray())
                }
//        components.forEach {
//            println("Class " + it.classObject.simpleName)
//            it.classObject.declaredConstructors.forEach {
//                println(" Constructor " + it.parameters.size)
//                it.parameters.forEach {
//                    println("  -param " + it.name + " type " + it.type)
//                }
//            }
//        }
//                .forEach { beansBuffer[it.classObject.simpleName] = it.classObject.newInstance() }
    }

    @JvmStatic
    fun invokeMethod(bean: Any, handler: MethodEntry, request: Request): Response {
        val responseParams = handler.toResponseParams()
        val arguments = mutableListOf<Any>()
        handler.method.parameters.filter { it.type == Request::class.java }.forEach { arguments.add(request) }
        handler.method.parameters.filter { it.type == ResponseParams::class.java }.forEach { arguments.add(responseParams) }
        val result = handler.method.invoke(bean, *arguments.toTypedArray()) // TODO args
        if (result is String) return Response(responseParams.contentType, result)
        // map object to Json
        return Response(handler.requestContentType, "{ \"error\": \"json not supported\" }")
    }
}