package com.srest.framework.inject

import com.fasterxml.jackson.databind.ObjectMapper
import com.srest.framework.annotation.util.Autowire
import com.srest.framework.annotation.Component
import com.srest.framework.annotation.util.RequestParam
import com.srest.framework.main.MethodEntry
import com.srest.framework.request.Request
import com.srest.framework.response.ContentType
import com.srest.framework.response.Response
import com.srest.framework.response.ResponseEntity
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Parameter
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

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
    fun invokeMethod(jsonMapper: ObjectMapper, bean: Any, mapper: MethodEntry, request: Request): Response? {
        val response = mapper.toResponse()
        val arguments = getArguments(mapper.beanMethod.parameters, request, response)
        try {
            val result = mapper.beanMethod.invoke(bean, *arguments.toTypedArray())
            var contentData: Any?
            if (result is ResponseEntity<*>) {
                contentData = result.responseData
                response.responseCode = result.responseCode
            } else {
                contentData = result
                response.responseCode = 200
            }
            if (contentData is String) { response.setData(contentData) }
            if (mapper.responseBody) {
                response.setType(ContentType.APP_JSON)
                if (contentData !is String) response.setData(jsonMapper.writeValueAsString(contentData))
            } else if (contentData !is String && response.contentType == ContentType.TEXT_HTML) {
                throw RuntimeException("Html response available with String response!")
            }
            return response
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getArguments(parameters: Array<Parameter>, request: Request, response: Response): List<Any> {
        val arguments = mutableListOf<Any>()
        parameters.forEach {
            when {
                it.type == Request::class.java -> arguments.add(request)
                it.type == Response::class.java -> arguments.add(response)
                it.isAnnotationPresent(RequestParam::class.java) -> {
                    val annotation = it.getAnnotation(RequestParam::class.java)
                    val paramName = if (annotation.name.isNotEmpty()) annotation.name else it.name
                    val paramValue = request.params[paramName]
                    if (paramValue != null) { arguments.add(paramValue) } else {
                        if (annotation.required) throw RuntimeException("Parameter needed '$paramName'")
                        arguments.add("")
                    }
                }
            }
        }
        return arguments
    }
}